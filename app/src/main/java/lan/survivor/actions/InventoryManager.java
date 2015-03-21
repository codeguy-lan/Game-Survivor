package lan.survivor.actions;

import java.util.ArrayList;
import java.util.Iterator;
import lan.survivor.element.item.ItemElement;
import lan.survivor.element.item.equip.EquipElement;
import lan.survivor.element.item.tool.Bottle;
import lan.survivor.element.item.tool.FishingRod;
import lan.survivor.utils.Const;
import lan.survivor.utils.ID;
import android.util.Log;

public class InventoryManager {

	public static ArrayList<ItemElement> inventory = new ArrayList<ItemElement>();
	public static int bagCapacity = Const.BAG_INITIAL_CAPACITY;
	static {
		inventory.add(new Bottle(ID.TOL_BOTTLE_FILL, 250));
		inventory.add(new EquipElement(ID.EQP_KNIFE));
		inventory.add(new FishingRod());
	}

	private InventoryManager() {

	}

	public static boolean add(ItemElement se) {
		for (ItemElement e : inventory) {
			if (e.speciesID == se.speciesID && e.stackCount < e.getMaxStack()) {
				int remain = e.stack(se.stackCount, true);
				if (remain == 0) {
					return true;
				} else {
					se.stackCount = remain;
					// return false;
				}
			}
		}
		if (inventory.size() >= bagCapacity) {
			return false;
		}
		while (inventory.size() < bagCapacity) {
			if (se.stackCount > se.getMaxStack()) {
				try {
					ItemElement temp;
					temp = se.getClass().newInstance();
					temp.stackCount = se.getMaxStack();
					se.stackCount -= se.getMaxStack();
					inventory.add(temp);
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				} catch (InstantiationException e1) {
					e1.printStackTrace();
				}
			} else {
				inventory.add(se);
				return true;
			}
		}
		return false;
	}

	public static void decrease(ItemElement se) {
		if (se.stackCount > 0) {
			se.stackCount -= 1;
		}
		if (se.stackCount == 0) {
			remove(se);
		}
	}

	public static void remove(ItemElement se) {
		inventory.remove(se);
		// render update
	}

	public static void printInventoryList() {
		Log.v("INVENTORY", "########LIST DUMP#########");
		for (ItemElement e : inventory) {
			Log.v("INVENTORY", "[LIST]name:" + e.speciesID + " count:" + e.stackCount);
		}
	}

	public static void refresh() {
		synchronized (inventory) {
			if (inventory.size() > 0) {
				Iterator<ItemElement> i = inventory.iterator();
				while (i.hasNext()) {
					if (i.next().stackCount < 1) {
						i.remove();
					}
				}
			}
		}
	}

	public static boolean hasElement(Class<?> c) {
		for (ItemElement se : inventory) {
			if (c.isInstance(se)) {
				return true;
			}
		}
		return false;
	}

	public static ArrayList<ItemElement> getTypeList(Class<?> c) {
		ArrayList<ItemElement> result = new ArrayList<ItemElement>();
		for (ItemElement se : inventory) {
			if (c.isInstance(se)) {
				result.add(se);
			}
		}
		return result;
	}
}
