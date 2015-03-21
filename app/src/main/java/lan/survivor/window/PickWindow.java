package lan.survivor.window;

import java.util.ArrayList;
import lan.survivor.R;
import lan.survivor.actions.EventManager;
import lan.survivor.adapter.ItemListAdapter;
import lan.survivor.element.BaseElement;
import lan.survivor.element.Terrain;
import lan.survivor.element.item.ItemElement;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class PickWindow {
	private Context pContext;

	private View pPickView;
	private ListView pPickListView;
	private ItemListAdapter pListAdapter;
	private Button pButtonClose;
	private AlertDialog pPickWindow;

	public PickWindow(Context context) {
		pContext = context;
		pPickView = View.inflate(pContext, R.layout.pick_view, null);
		pPickListView = (ListView) pPickView.findViewById(R.id.pick_listView);
		pButtonClose = (Button) pPickView.findViewById(R.id.pick_btnClose);

		pButtonClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pPickWindow.dismiss();
			}
		});

		pPickWindow = new AlertDialog.Builder(pContext).create();
		pPickWindow.setView(pPickView);
	}

	public void showWindow(final Terrain t) {
		final ArrayList<ItemElement> pickList = new ArrayList<ItemElement>();
		createPickList(pickList, t);
		pListAdapter = new ItemListAdapter(pContext, R.layout.item_list, pickList);
		pPickListView.setAdapter(pListAdapter);
		pPickListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parrent, View v, int position, long id) {
				ItemElement ie = pickList.get(position);
				if (EventManager.pickup(ie, t)) {
					pickList.remove(ie);
					if (pickList.size() < 1) {
						pPickWindow.dismiss();
					}
				}
				updateWindow();
			}
		});
		pPickWindow.show();
	}

	private void updateWindow() {
		pListAdapter.notifyDataSetChanged();
	}

	private void createPickList(ArrayList<ItemElement> pickList, Terrain t) {
		if (t.elements != null) {
			for (BaseElement be : t.elements) {
				if (be instanceof ItemElement) {
					pickList.add((ItemElement) be);
				}
			}
		}
	}
}
