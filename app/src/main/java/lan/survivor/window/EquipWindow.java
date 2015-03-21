package lan.survivor.window;

import lan.survivor.R;
import lan.survivor.actions.EquipManager;
import lan.survivor.actions.InventoryManager;
import lan.survivor.actions.StatusManager;
import lan.survivor.adapter.ItemListAdapter;
import lan.survivor.element.item.ItemElement;
import lan.survivor.element.item.equip.EquipElement;
import lan.survivor.utils.RenderResPool;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class EquipWindow {

	private Context pContext;

	private View pEquipView;
	private ListView pEquipListView;
	private ItemListAdapter pListAdapter;
	private TextView pTextAtk, pTextDef, pTextSpd;
	private ImageView pImgHead, pImgBody, pImgPants, pImgBoots, pImgHands, pImgWeapon;
	private Button pButtonClose;
	private AlertDialog pEquipWindow;
	private OnClickListener imgClick;

	public EquipWindow(Context context) {
		pContext = context;
		pEquipView = View.inflate(pContext, R.layout.equip_view, null);
		pEquipListView = (ListView) pEquipView.findViewById(R.id.equip_listView);
		pTextAtk = (TextView) pEquipView.findViewById(R.id.equip_txtAtk);
		pTextDef = (TextView) pEquipView.findViewById(R.id.equip_txtDef);
		pTextSpd = (TextView) pEquipView.findViewById(R.id.equip_txtSpd);
		pImgHead = (ImageView) pEquipView.findViewById(R.id.equip_imgHead);
		pImgBody = (ImageView) pEquipView.findViewById(R.id.equip_imgBody);
		pImgHands = (ImageView) pEquipView.findViewById(R.id.equip_imgHands);
		pImgPants = (ImageView) pEquipView.findViewById(R.id.equip_imgPants);
		pImgBoots = (ImageView) pEquipView.findViewById(R.id.equip_imgBoots);
		pImgWeapon = (ImageView) pEquipView.findViewById(R.id.equip_imgWeapon);
		pButtonClose = (Button) pEquipView.findViewById(R.id.equip_btnClose);

		imgClick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.equip_imgHead:
					if (EquipManager.takeOff(EquipManager.EQUIP_WEAPON)) {
						updateWindow();
					}
					break;
				case R.id.equip_imgBody:
					if (EquipManager.takeOff(EquipManager.EQUIP_BODY)) {
						updateWindow();
					}
					break;
				case R.id.equip_imgHands:
					if (EquipManager.takeOff(EquipManager.EQUIP_HANDS)) {
						updateWindow();
					}
					break;
				case R.id.equip_imgPants:
					if (EquipManager.takeOff(EquipManager.EQUIP_PANTS)) {
						updateWindow();
					}
					break;
				case R.id.equip_imgBoots:
					if (EquipManager.takeOff(EquipManager.EQUIP_BOOTS)) {
						updateWindow();
					}
					break;
				case R.id.equip_imgWeapon:
					if (EquipManager.takeOff(EquipManager.EQUIP_WEAPON)) {
						updateWindow();
					}
					break;
				}
			}
		};

		pImgHead.setOnClickListener(imgClick);
		pImgBody.setOnClickListener(imgClick);
		pImgHands.setOnClickListener(imgClick);
		pImgPants.setOnClickListener(imgClick);
		pImgBoots.setOnClickListener(imgClick);
		pImgWeapon.setOnClickListener(imgClick);

		pButtonClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pEquipWindow.dismiss();
			}
		});

		pEquipWindow = new AlertDialog.Builder(pContext).create();
		pEquipWindow.setView(pEquipView);
	}

	public void showWindow() {
		updateEquipInfo();
		pListAdapter = new ItemListAdapter(pContext, R.layout.item_list, InventoryManager.inventory);
		pEquipListView.setAdapter(pListAdapter);
		pEquipListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parrent, View v, int position, long id) {
				ItemElement se = InventoryManager.inventory.get(position);
				if (se instanceof EquipElement) {
					EquipManager.equip((EquipElement) se);
					updateWindow();
				}
			}
		});
		pEquipWindow.show();
	}

	private void updateWindow() {
		pListAdapter.notifyDataSetChanged();
		updateEquipInfo();
	}

	private void updateEquipInfo() {
		pTextAtk.setText(String.valueOf(StatusManager.equipStatus[StatusManager.ATTACK]));
		pTextDef.setText(String.valueOf(StatusManager.equipStatus[StatusManager.DEFENSE]));
		pTextSpd.setText(String.valueOf(StatusManager.equipStatus[StatusManager.SPEED]));
		pImgHead.setImageBitmap(RenderResPool.getImg(EquipManager.equips[EquipManager.EQUIP_HEAD].speciesID));
		pImgBody.setImageBitmap(RenderResPool.getImg(EquipManager.equips[EquipManager.EQUIP_BODY].speciesID));
		pImgHands.setImageBitmap(RenderResPool.getImg(EquipManager.equips[EquipManager.EQUIP_HANDS].speciesID));
		pImgPants.setImageBitmap(RenderResPool.getImg(EquipManager.equips[EquipManager.EQUIP_PANTS].speciesID));
		pImgBoots.setImageBitmap(RenderResPool.getImg(EquipManager.equips[EquipManager.EQUIP_BOOTS].speciesID));
		pImgWeapon.setImageBitmap(RenderResPool.getImg(EquipManager.equips[EquipManager.EQUIP_WEAPON].speciesID));
	}
}
