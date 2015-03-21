package lan.survivor.window;

import lan.survivor.R;
import lan.survivor.actions.InventoryManager;
import lan.survivor.actions.StatusManager;
import lan.survivor.adapter.GridAdapter;
import lan.survivor.adapter.ItemListAdapter;
import lan.survivor.element.building.Shelter;
import lan.survivor.environment.World;
import lan.survivor.utils.RenderResPool;
import lan.survivor.view.GameView;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ShelterWindow {

	private Context pContext;
	private Shelter pShelter;

	private View pShelterView;
	private ImageView pIconView;
	private TextView pTextView;
	private ListView pInventoryView;
	private ItemListAdapter pListAdapter;
	private GridView pGridView;
	private GridAdapter pGridAdapter;
	private Button pButtonRest, pButtonTemp, pButtonClose;
	private AlertDialog pShelterWindow;

	public ShelterWindow(Context context, Shelter s) {
		pContext = context;
		pShelter = s;
		pShelterView = View.inflate(pContext, R.layout.building_view, null);

		pIconView = (ImageView) pShelterView.findViewById(R.id.building_icon);
		pTextView = (TextView) pShelterView.findViewById(R.id.building_text);
		pInventoryView = (ListView) pShelterView.findViewById(R.id.building_list_view);
		pGridView = (GridView) pShelterView.findViewById(R.id.building_gridview);
		pButtonRest = (Button) pShelterView.findViewById(R.id.building_btn1);
		pButtonTemp = (Button) pShelterView.findViewById(R.id.building_btn2);
		pButtonClose = (Button) pShelterView.findViewById(R.id.building_btnClose);

		pButtonRest.setText("Rest");
		pButtonRest.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				World.toSleepMode(true);
				StatusManager.inSleep = true;
				StatusManager.inShelter = true;
				GameView.changeSleepButtonText(true);
				pShelterWindow.dismiss();
			}
		});
		pButtonTemp.setVisibility(View.INVISIBLE);

		pButtonClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pShelterWindow.dismiss();
				InventoryManager.refresh();
			}
		});

		pShelterWindow = new AlertDialog.Builder(pContext).create();
		pShelterWindow.setView(pShelterView);
		pShelterWindow.setCancelable(false);
	}

	public void setShelter(Shelter s) {
		pShelter = s;
	}

	public void showWindow() {
		pIconView.setImageBitmap(RenderResPool.getImg(pShelter.speciesID));
		pTextView.setText(pContext.getString(R.string.shelter_text));
		pListAdapter = new ItemListAdapter(pContext, R.layout.item_list, InventoryManager.inventory);
		pInventoryView.setAdapter(pListAdapter);
		pInventoryView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parrent, View v, int position, long id) {
				pShelter.addToGrid(InventoryManager.inventory.get(position));
				updateList();
			}
		});

		pGridAdapter = new GridAdapter(pContext, pShelter.containerList);
		pGridView.setAdapter(pGridAdapter);
		pGridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				pShelter.removeFromGrid(pShelter.containerList.get(position));
				updateList();
			}
		});
		pShelterWindow.show();
	}

	private void updateList() {
		pListAdapter.notifyDataSetChanged();
		pGridAdapter.notifyDataSetChanged();
	}
}
