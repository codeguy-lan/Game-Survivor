package lan.survivor.actions;

import lan.survivor.element.item.equip.EquipElement;
import lan.survivor.utils.ID;
import lan.survivor.view.GameView;
import android.util.Log;

public class EquipManager {

	public static final short EQUIP_EMPTY = 0;
	public static final short EQUIP_HEAD = 1;
	public static final short EQUIP_BODY = 2;
	public static final short EQUIP_HANDS = 3;
	public static final short EQUIP_PANTS = 4;
	public static final short EQUIP_BOOTS = 5;
	public static final short EQUIP_WEAPON = 6;

	public static EquipElement[] equips = new EquipElement[7];

	static {
		equips[EQUIP_EMPTY] = new EquipElement(ID.EQP_EMPTY);
		equips[EQUIP_HEAD] = equips[EQUIP_EMPTY];
		equips[EQUIP_BODY] = equips[EQUIP_EMPTY];
		equips[EQUIP_HANDS] = equips[EQUIP_EMPTY];
		equips[EQUIP_PANTS] = equips[EQUIP_EMPTY];
		equips[EQUIP_BOOTS] = equips[EQUIP_EMPTY];
		equips[EQUIP_WEAPON] = equips[EQUIP_EMPTY];
	}

	private EquipManager() {

	}

	public static void equip(EquipElement eqp) {
		if (equips[eqp.part].speciesID != ID.EQP_EMPTY) {
			Log.v("EQUIP", "ADD STH");
			InventoryManager.add(equips[eqp.part]);
			equips[eqp.part].takeOffEffect();
		}
		equips[eqp.part] = eqp;
		eqp.equipEffect();
		InventoryManager.remove(eqp);
	}

	public static boolean takeOff(int part) {
		if (equips[part].speciesID == ID.EQP_EMPTY) {
			return false;
		}
		equips[part].takeOffEffect();
		if (!InventoryManager.add(equips[part])) {
			GameView.survivor.currentTerrainBlock().addElement(equips[part]);
		}
		equips[part] = equips[EQUIP_EMPTY];
		Log.v("EQUIP TAKEOFF", "EMPTY:" + equips[part].speciesID);
		return true;
	}
}
