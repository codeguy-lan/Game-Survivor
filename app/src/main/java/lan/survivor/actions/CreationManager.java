package lan.survivor.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lan.survivor.element.BaseElement;
import lan.survivor.element.item.ItemElement;
import lan.survivor.utils.Const;
import lan.survivor.utils.RefTable;
import android.util.Log;

public class CreationManager {

	public static List<ItemElement> gridList = new ArrayList<ItemElement>();
	public static List<ItemElement> createList = new ArrayList<ItemElement>();

	private CreationManager() {

	}

	public synchronized static void addToGrid(ItemElement se) {
		if (gridList.size() < Const.MAX_GRID_COUNT) {
			if (se.stackCount > 0) {
				try {
					gridList.add(se.getClass().getConstructor(int.class).newInstance(se.speciesID));
				} catch (Exception e) {
					e.printStackTrace();
				}
				se.stackCount--;
			}
		}
	}

	public synchronized static void removeFromGrid(ItemElement se) {
		for (ItemElement e : createList) {
			if (e.speciesID == se.speciesID) {
				if (e.stack(1, true) == 0) {
					gridList.remove(se);
					return;
				}
			}
		}
	}

	public synchronized static void cancelGrid() {
		for (ItemElement se : gridList) {
			for (ItemElement e : createList) {
				if (e.speciesID == se.speciesID) {
					if (e.stack(1, true) == 0) {
						break;
					}
				}
			}
		}
		clearGrid();
	}

	public synchronized static void clearGrid() {
		gridList.clear();
	}

	public static void makeCreationResourceList(ArrayList<ItemElement> inventoryList, ArrayList<BaseElement> terrainList) {
		// inventoryList could not be null, only check terrainList
		createList = new ArrayList<ItemElement>(inventoryList);
		if (terrainList != null) {
			for (BaseElement be : terrainList) {
				if (be instanceof ItemElement) {
					createList.add((ItemElement) be);
				}
			}
		}
	}

	public static BaseElement getCreation() {
		if (gridList.size() > 0) {
			char[] input = new char[gridList.size()];
			int i = 0;
			for (ItemElement se : gridList) {
				input[i] = (char) se.speciesID;
				i++;
			}
			Arrays.sort(input);
			int id = RefTable.getCreationResultID(createRecipe(input));
			if (id == -1) {
				return null;
			} else {
				return BaseElement.createElement(id);
			}
		}
		return null;
	}

	public static void dump() {
		for (ItemElement e : createList) {
			Log.v("DUMP CREATE LIST", "id:" + e.speciesID + " name:" + e.getClass().getName());
		}
	}

	private static String createRecipe(char[] chars) {
		String recipe = Character.toString(chars[0]);
		for (int i = 1; i < chars.length; i++) {
			recipe += ('-' + Character.toString(chars[i]));
		}
		return recipe;
	}
}
