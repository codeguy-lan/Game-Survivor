package lan.survivor.window;

import lan.survivor.R;
import lan.survivor.actions.InventoryManager;
import lan.survivor.actions.ItemEffect;
import lan.survivor.actions.StatusManager;
import lan.survivor.adapter.GridAdapter;
import lan.survivor.adapter.ItemListAdapter;
import lan.survivor.element.building.Well;
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

public class WellWindow {

	private Context pContext;
	private Well pWell;

	private View pWellView;
	private ImageView pIconView;
	private TextView pTextView;
	private ListView pInventoryView;
	private ItemListAdapter pListAdapter;
	private GridView pGridView;
	private GridAdapter pGridAdapter;
	private Button pButtonFill, pButtonDrink, pButtonClose;
	private AlertDialog pWellWindow;

	public WellWindow(Context context, Well well) {
		pContext = context;
		pWell = well;
		pWellView = View.inflate(pContext, R.layout.building_view, null);

		pIconView = (ImageView) pWellView.findViewById(R.id.building_icon);
		pTextView = (TextView) pWellView.findViewById(R.id.building_text);
		pInventoryView = (ListView) pWellView.findViewById(R.id.building_list_view);
		pGridView = (GridView) pWellView.findViewById(R.id.building_gridview);
		pButtonFill = (Button) pWellView.findViewById(R.id.building_btn1);
		pButtonDrink = (Button) pWellView.findViewById(R.id.building_btn2);
		pButtonClose = (Button) pWellView.findViewById(R.id.building_btnClose);

		pButtonFill.setText("Fill");
		pButtonFill.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pWell.fill();
				updateList();
			}
		});
		pButtonDrink.setText("Drink");
		pButtonDrink.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				StatusManager.addStatusEffect(new ItemEffect(StatusManager.WATER, StatusManager.STATUS_MAX, 0));
				Toast.makeText(pContext, R.string.toast_drink_water, Toast.LENGTH_SHORT).show();
			}
		});

		pButtonClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pWellWindow.dismiss();
				InventoryManager.refresh();
			}
		});

		pWellWindow = new AlertDialog.Builder(pContext).create();
		pWellWindow.setView(pWellView);
		pWellWindow.setCancelable(false);
	}

	public void setWell(Well well) {
		pWell = well;
	}

	public void showWindow() {
		pIconView.setImageBitmap(RenderResPool.getImg(pWell.speciesID));
		pTextView.setText(pContext.getString(R.string.well_text));
		pListAdapter = new ItemListAdapter(pContext, R.layout.item_list, InventoryManager.inventory);
		pInventoryView.setAdapter(pListAdapter);
		pInventoryView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parrent, View v, int position, long id) {
				pWell.addToGrid(InventoryManager.inventory.get(position));
				updateList();
			}
		});

		pGridAdapter = new GridAdapter(pContext, pWell.containerList);
		pGridView.setAdapter(pGridAdapter);
		pGridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				pWell.removeFromGrid(pWell.containerList.get(position));
				updateList();
			}
		});
		pWellWindow.show();
	}

	private void updateList() {
		pListAdapter.notifyDataSetChanged();
		pGridAdapter.notifyDataSetChanged();
	}
}
