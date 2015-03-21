package lan.survivor.window;

import lan.survivor.R;
import lan.survivor.actions.CreationManager;
import lan.survivor.actions.InventoryManager;
import lan.survivor.adapter.GridAdapter;
import lan.survivor.adapter.ItemListAdapter;
import lan.survivor.element.BaseElement;
import lan.survivor.element.Terrain;
import lan.survivor.element.item.ItemElement;
import lan.survivor.utils.Misc.CreationItemAction;
import lan.survivor.view.GameView;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

public class CreationWindow {

	private Context pContext;
	private CreationResultWindow crWindow;

	private View pCreateView;
	private ListView pCrateListView;
	private ItemListAdapter pListAdapter;
	private GridView pGridView;
	private GridAdapter pGridAdapter;
	private Button pButtonCreate, pButtonClear, pButtonCancel;
	private AlertDialog pCreateWindow;

	public CreationWindow(Context context) {
		pContext = context;
		crWindow = new CreationResultWindow(pContext);
		// LayoutInflater inflater = (LayoutInflater) pContext.getSystemService("LAYOUT_INFLATER_SERVICE");
		pCreateView = View.inflate(pContext, R.layout.creation_view, null);

		pCrateListView = (ListView) pCreateView.findViewById(R.id.create_list_view);
		pGridView = (GridView) pCreateView.findViewById(R.id.create_gridview);

		pButtonCreate = (Button) pCreateView.findViewById(R.id.gridview_btnCreate);
		pButtonClear = (Button) pCreateView.findViewById(R.id.gridview_btnClear);
		pButtonCancel = (Button) pCreateView.findViewById(R.id.gridview_btnCancel);

		pButtonCreate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final BaseElement creation = CreationManager.getCreation();
				if (creation != null) {
					Log.v("CREATION", "" + creation.getClass().getName());
					crWindow.setListener(new CreationItemAction() {
						@Override
						public void onTake() {
							Log.v("CREATION", "ON TAKE CALLED");
							Terrain t = GameView.survivor.currentTerrainBlock();
							CreationManager.clearGrid();
							InventoryManager.refresh();
							t.refreshItem();
							if (!InventoryManager.add((ItemElement) creation)) {
								Toast.makeText(pContext, R.string.toast_drop_creation, Toast.LENGTH_SHORT).show();
								t.addElement(creation);
							}
							CreationManager.makeCreationResourceList(InventoryManager.inventory, t.elements);
							pListAdapter = new ItemListAdapter(pContext, R.layout.item_list, CreationManager.createList);
							pCrateListView.setAdapter(pListAdapter);
							updateList();
						}

						@Override
						public void onSet() {
							Log.v("CREATION", "ON SET CALLED");
							Terrain t = GameView.survivor.facingTerrainBlock();
							if (t.hasElements()) {
								Toast.makeText(pContext, R.string.toast_cancel_creation, Toast.LENGTH_SHORT).show();
							} else {
								Terrain currentT = GameView.survivor.currentTerrainBlock();
								CreationManager.clearGrid();
								InventoryManager.refresh();
								currentT.refreshItem();
								t.addElement(creation);
								creation.afterSet(t);
								CreationManager.makeCreationResourceList(InventoryManager.inventory, t.elements);
								pListAdapter = new ItemListAdapter(pContext, R.layout.item_list, CreationManager.createList);
								pCrateListView.setAdapter(pListAdapter);
								updateList();
								pCreateWindow.dismiss();
							}
						}
					});
					crWindow.showCreationResult(creation);
				} else {
					Toast.makeText(pContext, R.string.toast_no_creation, Toast.LENGTH_SHORT).show();
				}
			}
		});

		pButtonClear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CreationManager.cancelGrid();
				updateList();
			}
		});

		pButtonCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CreationManager.cancelGrid();
				pCreateWindow.dismiss();
			}
		});

		pCreateWindow = new AlertDialog.Builder(pContext).create();
		pCreateWindow.setView(pCreateView);
		pCreateWindow.setCancelable(false);
	}

	public void showWindow() {
		CreationManager.makeCreationResourceList(InventoryManager.inventory, GameView.survivor.currentTerrainBlock().elements);
		pListAdapter = new ItemListAdapter(pContext, R.layout.item_list, CreationManager.createList);
		pCrateListView.setAdapter(pListAdapter);
		pCrateListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parrent, View v, int position, long id) {
				Log.v("CREAT", "List item selected:" + position + "," + id);
				CreationManager.addToGrid(CreationManager.createList.get(position));
				updateList();
			}
		});

		pGridAdapter = new GridAdapter(pContext, CreationManager.gridList);
		pGridView.setAdapter(pGridAdapter);
		pGridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				CreationManager.removeFromGrid(CreationManager.gridList.get(position));
				updateList();
			}
		});
		pCreateWindow.show();
	}

	private void updateList() {
		pListAdapter.notifyDataSetChanged();
		pGridAdapter.notifyDataSetChanged();
	}

}
