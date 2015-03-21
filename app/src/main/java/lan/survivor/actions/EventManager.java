package lan.survivor.actions;

import java.util.Random;
import lan.survivor.R;
import lan.survivor.application.GameApplication;
import lan.survivor.element.Terrain;
import lan.survivor.element.building.Well;
import lan.survivor.element.item.ItemElement;
import lan.survivor.element.item.edible.EdibleElement;
import lan.survivor.element.item.equip.EquipElement;
import lan.survivor.element.item.tool.Bottle;
import lan.survivor.environment.World;
import lan.survivor.utils.ID;
import android.widget.Toast;

public class EventManager {

	private static Random rand = new Random();

	public static boolean getItem(ItemElement ie) {
		if (InventoryManager.add(ie)) {
			return true;
		} else {
			Toast.makeText(GameApplication.getAppContext(), R.string.toast_bag_full, Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	public static boolean pickup(ItemElement ie, Terrain t) {
		if (InventoryManager.add(ie)) {
			t.elements.remove(ie);
			return true;
		} else {
			Toast.makeText(GameApplication.getAppContext(), R.string.toast_bag_full, Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	public static void useItem(Terrain currentT, ItemElement se) {
		if (se instanceof Bottle) {
			if (se.speciesID == ID.TOL_BOTTLE_FILL) {
				((Bottle) se).drink();
			} else {
				Toast.makeText(GameApplication.getAppContext(), R.string.toast_no_drink, Toast.LENGTH_SHORT).show();
			}
			return;
		}
		if (se instanceof EdibleElement) {
			StatusManager.addStatusEffect(((EdibleElement) se).getStatusEffect());
			InventoryManager.decrease(se);
			return;
		}
		if (se instanceof EquipElement) {
			EquipManager.equip((EquipElement) se);
			return;
		}
		Toast.makeText(GameApplication.getAppContext(), R.string.toast_no_use, Toast.LENGTH_SHORT).show();
	}

	public static void drop(Terrain currentT, ItemElement ce) {
		currentT.addElement(ce);
		InventoryManager.remove(ce);
	}

	public static void sleep() {
		StatusManager.inSleep = true;
	}

	public static void wakeUp() {
		StatusManager.inSleep = false;
		StatusManager.inShelter = false;
	}

	public static void dig(Terrain t) {
		World.spendTime(0, 20);
		StatusManager.addStatusEffect(new ItemEffect(StatusManager.SP, -5, 0));
		if (t.terrainType == Terrain.TERRAIN_GRASS) {
			if (rand.nextInt(5) == 0) {
				t.addElement(new Well());
				Toast.makeText(GameApplication.getAppContext(), R.string.toast_dig_well, Toast.LENGTH_SHORT).show();
				return;
			}
		}
		Toast.makeText(GameApplication.getAppContext(), R.string.toast_no_action, Toast.LENGTH_SHORT).show();
	}

	// public static void fireCook(EdibleElement ee) {
	// }
}
