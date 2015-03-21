package lan.survivor.utils;

import java.util.ArrayList;
import lan.survivor.actions.ItemEffect;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.SparseArray;

public class DataBaseLogic {

	private final static String[] ITEM_CLASS_COLUMN = { "_id", "name" };
	private final static String[] ITEM_COLUMN = { "title", "description" };
	private final static String[] EFFECTS_COLUMN = { "food_effect", "food_eft_time", "water_effect", "water_eft_time", "sp_effect", "sp_eft_time", "hp_effect", "hp_eft_time" };
	private final static String[] EQUIP_COLUMN = { "part", "attack", "defense", "speed" };
	private final static String[] PROVIDER_COLUMN = { "parent_id", "item_id", "base_number", "adjust_number" };

	private static SQLiteDatabase db;
	private static Cursor cursor;

	static {
		if (db == null) {
			db = DataBaseHelper.openDataBase();
		}
	}

	public static void registItemClassName(SparseArray<String> hMap) {
		cursor = db.query("items", ITEM_CLASS_COLUMN, "type=? OR type=?", new String[] { "building", "tool" }, null, null, null);
		try {
			while (cursor.moveToNext()) {
				hMap.put(cursor.getInt(0), cursor.getString(1));
			}
		} finally {
			cursor.close();
		}
	}

	public static String[] getTitleAndDescription(int id) {
		cursor = db.query("items", ITEM_COLUMN, "_id=" + id, null, null, null, null);
		try {
			if (cursor.moveToFirst()) {
				return new String[] { cursor.getString(0), cursor.getString(1) };
			}
			return null;
		} finally {
			cursor.close();
		}
	}

	public static ItemEffect[] getItemEffect(int id) {
		cursor = db.query("item_effects", EFFECTS_COLUMN, "_id=" + id, null, null, null, null);
		try {
			if (cursor.moveToFirst()) {
				return new ItemEffect[] { new ItemEffect(0, cursor.getInt(0), cursor.getInt(1)), new ItemEffect(1, cursor.getInt(2), cursor.getInt(3)),
						new ItemEffect(2, cursor.getInt(4), cursor.getInt(5)), new ItemEffect(3, cursor.getInt(6), cursor.getInt(7)) };
			}
			return null;
		} finally {
			cursor.close();
		}
	}

	public static int[] getEquipProperties(int id) {
		cursor = db.query("equip_properties", EQUIP_COLUMN, "_id=" + id, null, null, null, null);
		try {
			if (cursor.moveToFirst()) {
				return new int[] { cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getInt(3) };
			}
			return new int[] { 0, 0, 0, 0 };
		} finally {
			cursor.close();
		}
	}

	public static ArrayList<int[]> getProviderItemList(int id) {
		ArrayList<int[]> itemList = new ArrayList<int[]>();
		getProviderItemListFromDB(id, itemList);
		return itemList;
	}

	private static void getProviderItemListFromDB(int id, ArrayList<int[]> list) {
		boolean hasParent = false;
		int parentId = 0;
		cursor = db.query("provider_list", PROVIDER_COLUMN, "provider_id=" + id, null, null, null, null);
		try {
			while (cursor.moveToNext()) {
				list.add(new int[] { cursor.getInt(1), cursor.getInt(2), cursor.getInt(3) });
			}
		} finally {
			cursor.moveToFirst();
			if ((parentId = cursor.getInt(0)) != 0) {
				hasParent = true;
			}
			cursor.close();
		}
		if (hasParent) {
			getProviderItemListFromDB(parentId, list);
		}
	}
}