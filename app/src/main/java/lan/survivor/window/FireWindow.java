package lan.survivor.window;

import lan.survivor.R;
import lan.survivor.actions.InventoryManager;
import lan.survivor.adapter.GridAdapter;
import lan.survivor.adapter.ItemListAdapter;
import lan.survivor.element.building.Fire;
import lan.survivor.utils.RenderResPool;
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
import android.widget.Toast;

public class FireWindow {

	private Context pContext;
	private Fire pFire;

	private View pFireView;
	private ImageView pIconView;
	private TextView pTextView;
	private ListView pInventoryView;
	private ItemListAdapter pListAdapter;
	private GridView pGridView;
	private GridAdapter pGridAdapter;
	private Button pButtonProcess, pButtonPutOnOff, pButtonClose;
	private AlertDialog pFireWindow;

	public FireWindow(Context context, Fire fire) {
		pContext = context;
		pFire = fire;
		pFireView = View.inflate(pContext, R.layout.building_view, null);

		pIconView = (ImageView) pFireView.findViewById(R.id.building_icon);
		pTextView = (TextView) pFireView.findViewById(R.id.building_text);
		pInventoryView = (ListView) pFireView.findViewById(R.id.building_list_view);
		pGridView = (GridView) pFireView.findViewById(R.id.building_gridview);
		pButtonProcess = (Button) pFireView.findViewById(R.id.building_btn1);
		pButtonPutOnOff = (Button) pFireView.findViewById(R.id.building_btn2);
		pButtonClose = (Button) pFireView.findViewById(R.id.building_btnClose);

		pButtonProcess.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (pFire.fireOn) {
					pFire.process();
					updateList();
				} else {
					Toast.makeText(pContext, R.string.toast_fire_process_fail, Toast.LENGTH_SHORT).show();
				}
			}
		});

		pButtonPutOnOff.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (pFire.fireOn) {
					pFire.putOut();
					pFireWindow.dismiss();
					InventoryManager.refresh();
					Toast.makeText(pContext, R.string.toast_fire_putoff, Toast.LENGTH_SHORT).show();
				} else {
					if (pFire.lightOn()) {
						pFireWindow.dismiss();
						InventoryManager.refresh();
						Toast.makeText(pContext, R.string.toast_fire_puton, Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(pContext, R.string.toast_fire_puton_fail, Toast.LENGTH_SHORT).show();
					}
				}
			}
		});

		pButtonClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pFireWindow.dismiss();
				InventoryManager.refresh();
			}
		});

		pFireWindow = new AlertDialog.Builder(pContext).create();
		pFireWindow.setView(pFireView);
		pFireWindow.setCancelable(false);
	}

	public void setFire(Fire fire) {
		pFire = fire;
	}

	public void showWindow() {
		pIconView.setImageBitmap(RenderResPool.getImg(pFire.speciesID));
		if (pFire.fireOn) {
			pTextView.setText(pContext.getString(R.string.fire_on_text));
			pButtonPutOnOff.setText("PutOut");
		} else {
			pTextView.setText(pContext.getString(R.string.fire_off_text));
			pButtonPutOnOff.setText("PutOn");
		}
		pListAdapter = new ItemListAdapter(pContext, R.layout.item_list, InventoryManager.inventory);
		pInventoryView.setAdapter(pListAdapter);
		pInventoryView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parrent, View v, int position, long id) {
				pFire.addToGrid(InventoryManager.inventory.get(position));
				updateList();
			}
		});

		pGridAdapter = new GridAdapter(pContext, pFire.containerList);
		pGridView.setAdapter(pGridAdapter);
		pGridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				pFire.removeFromGrid(pFire.containerList.get(position));
				updateList();
			}
		});
		pFireWindow.show();
	}

	private void updateList() {
		pListAdapter.notifyDataSetChanged();
		pGridAdapter.notifyDataSetChanged();
	}
}
