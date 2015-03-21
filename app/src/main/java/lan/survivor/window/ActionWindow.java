package lan.survivor.window;

import java.util.ArrayList;
import lan.survivor.R;
import lan.survivor.actions.EventManager;
import lan.survivor.actions.InventoryManager;
import lan.survivor.activity.BattleActivity;
import lan.survivor.adapter.ActionItemListAdapter;
import lan.survivor.element.Survivor;
import lan.survivor.element.Terrain;
import lan.survivor.element.building.BuildingElement;
import lan.survivor.element.item.ItemElement;
import lan.survivor.element.item.tool.ToolElement;
import lan.survivor.element.provider.ProviderElement;
import lan.survivor.environment.MonsterMaker;
import lan.survivor.environment.Physics;
import lan.survivor.environment.WorldMap;
import lan.survivor.utils.DataBaseLogic;
import lan.survivor.utils.RefTable;
import lan.survivor.utils.RenderResPool;
import lan.survivor.view.GameView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ActionWindow {

	private Context pContext;

	View titleView, bodyView;
	ImageView icon;
	TextView title, detail;
	Button digButton, okButton;
	ListView providerListView;
	ActionItemListAdapter adapter;
	AlertDialog actionDialog;

	public ActionWindow(Activity activity) {
		pContext = activity;
		titleView = View.inflate(pContext, R.layout.item_dialog_title, null);
		bodyView = View.inflate(pContext, R.layout.action_dialog, null);
		icon = (ImageView) titleView.findViewById(R.id.item_dialog_icon);
		title = (TextView) titleView.findViewById(R.id.item_dialog_name);
		detail = (TextView) bodyView.findViewById(R.id.action_dialog_detail);
		digButton = (Button) bodyView.findViewById(R.id.action_dialog_btnDig);
		okButton = (Button) bodyView.findViewById(R.id.action_dialog_btnOK);
		providerListView = (ListView) bodyView.findViewById(R.id.action_list_view);

		actionDialog = new AlertDialog.Builder(pContext).create();
		actionDialog.setCustomTitle(titleView);
		actionDialog.setView(bodyView);
		actionDialog.setCancelable(false);
	}

	public void action(final Terrain facingT) {
		if (!facingT.hasElements()) {
			if (facingT.terrainType == Terrain.TERRAIN_SEA_DEAP) {
				WorldMap.isInSubMap = true;
				Survivor.updatePosition();
			} else {
				showAsToolWindow(facingT);
			}
		} else {
			if (facingT.getMonster() != null) {
				MonsterMaker.setPause(true);
				int monsterID = MonsterMaker.getListPosition(facingT);
				if (monsterID != -1) {
					GameView.inBattle = true;
					Intent intent = new Intent(pContext, BattleActivity.class);
					intent.putExtra("monsterID", monsterID);
					pContext.startActivity(intent);
					return;
				} else {
					Toast.makeText(pContext, R.string.toast_no_action, Toast.LENGTH_SHORT).show();
					MonsterMaker.setPause(false);
				}
			}
			ProviderElement pe = facingT.getProviderElement();
			BuildingElement be = facingT.getBuildingElement();
			if (pe == null && be == null) {
				Toast.makeText(pContext, R.string.toast_no_action, Toast.LENGTH_SHORT).show();
				return;
			} else if (be != null) {
				be.showActionBuildingWindow(pContext);
			} else if (pe != null) {
				showAsProvideWindow(pe, facingT);
			}
		}
	}

	public void showAsProvideWindow(final ProviderElement pe, final Terrain t) {
		icon.setImageBitmap(RenderResPool.getImg(pe.speciesID));
		String titleAndDescription[] = DataBaseLogic.getTitleAndDescription(pe.speciesID);
		title.setText(titleAndDescription[0]);
		detail.setText(titleAndDescription[1]);
		adapter = new ActionItemListAdapter(pContext, R.layout.item_list, ((ProviderElement) pe).provideList());
		providerListView.setAdapter(adapter);
		providerListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parrent, View v, int position, long id) {
				ItemElement ie = pe.provide(t, position);
				if (ie != null) {
					if (!EventManager.getItem(ie)) {
						GameView.survivor.currentTerrainBlock().addElement(ie);
					}
					adapter.notifyDataSetChanged();
				}
			}
		});
		digButton.setVisibility(View.INVISIBLE);
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				t.updateProvider(pe);
				actionDialog.dismiss();
			}
		});
		actionDialog.show();
	}

	public void showAsToolWindow(final Terrain t) {
		final ArrayList<ItemElement> toolList = InventoryManager.getTypeList(ToolElement.class);
		icon.setImageBitmap(WorldMap.terrainTexture[t.terrainType]);
		title.setText(RefTable.getTerrainNameID(t.terrainType));
		detail.setText(RefTable.getTerrainDetailID(t.terrainType));
		adapter = new ActionItemListAdapter(pContext, R.layout.item_list, toolList);
		providerListView.setAdapter(adapter);
		providerListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parrent, View v, int position, long id) {
				ToolElement te = (ToolElement) toolList.get(position);
				te.onTakeAction(pContext, t, actionDialog);
			}
		});
		if (Physics.getTerrainProperty(t.terrainType, Physics.DIGGABLE)) {
			digButton.setVisibility(View.VISIBLE);
			digButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					showDigDialog(t);
				}
			});
		} else {
			digButton.setVisibility(View.INVISIBLE);
		}
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				actionDialog.dismiss();
			}
		});
		actionDialog.show();
	}

	protected void showDigDialog(final Terrain t) {
		View bodyView = View.inflate(pContext, R.layout.item_dialog, null);
		TextView detail = (TextView) bodyView.findViewById(R.id.item_dialog_detail);
		detail.setText(pContext.getString(R.string.action_dig));
		Button okButton = (Button) bodyView.findViewById(R.id.item_dialog_btn1);
		okButton.setText("yes");
		Button diableButton = (Button) bodyView.findViewById(R.id.item_dialog_btn2);
		diableButton.setVisibility(View.INVISIBLE);
		Button noButton = (Button) bodyView.findViewById(R.id.item_dialog_btn3);
		noButton.setText("no");

		final AlertDialog actDialog = new AlertDialog.Builder(pContext).create();
		actDialog.setView(bodyView);

		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EventManager.dig(t);
				actDialog.dismiss();
			}
		});

		noButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				actDialog.dismiss();
			}
		});

		actDialog.show();
	}
}
