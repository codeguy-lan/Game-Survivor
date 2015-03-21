package lan.survivor.window;

import lan.survivor.R;
import lan.survivor.actions.EventManager;
import lan.survivor.actions.InventoryManager;
import lan.survivor.adapter.ItemListAdapter;
import lan.survivor.application.GameApplication;
import lan.survivor.element.item.ItemElement;
import lan.survivor.element.item.tool.Bottle;
import lan.survivor.utils.DataBaseLogic;
import lan.survivor.utils.RenderResPool;
import lan.survivor.view.GameView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class InventoryWindow {

	private Context pContext;

	private View popupView;
	private PopupWindow mInventoryWindow;
	private View mAncorView;
	private int[] mOffset = { 0, 0 };
	private ListView inventoryListView;
	private ItemListAdapter mAdapter;

	public InventoryWindow(Activity activity, View ancorView) {
		pContext = activity;
		popupView = View.inflate(pContext, R.layout.inventory_view, null);
		mAncorView = ancorView;
		mInventoryWindow = new PopupWindow(GameApplication.getAppContext());
		mInventoryWindow.setWidth(220);
		mInventoryWindow.setHeight(270);
		// mInventoryWindow.setWindowLayoutMode(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
		mInventoryWindow.setContentView(popupView);
		mInventoryWindow.setClippingEnabled(false);
		mInventoryWindow.setOutsideTouchable(true);
		mInventoryWindow.setFocusable(true);
		setOffset(-150, -350);

		inventoryListView = (ListView) popupView.findViewById(R.id.inventory_list_view);
		mAdapter = new ItemListAdapter(pContext, R.layout.item_list, InventoryManager.inventory);
		inventoryListView.setAdapter(mAdapter);
		inventoryListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parrent, View v, int position, long id) {
				Log.v("POPUP", "item selected:" + position + "," + id);
				final ItemElement ie = InventoryManager.inventory.get(position);
				View titleView = View.inflate(pContext, R.layout.item_dialog_title, null);
				View bodyView = View.inflate(pContext, R.layout.item_dialog, null);
				ImageView icon = (ImageView) titleView.findViewById(R.id.item_dialog_icon);
				icon.setImageBitmap(RenderResPool.getImg(ie.speciesID));
				TextView title = (TextView) titleView.findViewById(R.id.item_dialog_name);
				String[] titleAndDescription = DataBaseLogic.getTitleAndDescription(ie.speciesID);
				if (ie.getMaxStack() == 1) {
					if (ie instanceof Bottle) {
						title.setText(titleAndDescription[0] + "(" + ((Bottle) ie).fluidAmount + ")");
					} else {
						title.setText(titleAndDescription[0]);
					}
				} else {
					// title.setText(pContext.getString(RefTable.getNameID(ie.speciesID)).split(",")[1] + "X" + ie.stackCount);
					title.setText(titleAndDescription[0] + "X" + ie.stackCount);
				}
				TextView detail = (TextView) bodyView.findViewById(R.id.item_dialog_detail);
				detail.setText(titleAndDescription[1]);
				Button useButton = (Button) bodyView.findViewById(R.id.item_dialog_btn1);
				Button dropButton = (Button) bodyView.findViewById(R.id.item_dialog_btn2);
				Button cancelButton = (Button) bodyView.findViewById(R.id.item_dialog_btn3);

				final AlertDialog itemDialog = new AlertDialog.Builder(pContext).create();
				itemDialog.setCustomTitle(titleView);
				itemDialog.setView(bodyView);

				useButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						EventManager.useItem(GameView.survivor.currentTerrainBlock(), ie);
						mAdapter.notifyDataSetChanged();
						itemDialog.dismiss();
					}
				});

				dropButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						EventManager.drop(GameView.survivor.currentTerrainBlock(), ie);
						mAdapter.notifyDataSetChanged();
						itemDialog.dismiss();
					}
				});

				cancelButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						itemDialog.dismiss();
					}
				});

				itemDialog.show();
			}
		});
	}

	public void showPulldown() {
		mAdapter.notifyDataSetChanged();
		mInventoryWindow.showAsDropDown(mAncorView, mOffset[0], mOffset[1]);
	}

	public void dismissPulldown() {
		mInventoryWindow.dismiss();
	}

	public boolean isShowing() {
		return mInventoryWindow.isShowing();
	}

	public void setOffset(int x, int y) {
		mOffset[0] = x;
		mOffset[1] = y;
	}

	public final PopupWindow getPopupWindow() {
		return mInventoryWindow;
	}

}
