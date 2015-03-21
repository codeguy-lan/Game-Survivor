package lan.survivor.utils;

public class Const {

	public static final int DIRECTION_D = 0;
	public static final int DIRECTION_L = 1;
	public static final int DIRECTION_R = 2;
	public static final int DIRECTION_U = 3;

	public static final int MAP_TERRAINS_ROW = 100;
	public static final int MAP_TERRAINS_COLUMN = 100;
	public static final int SUBMAP_TERRAINS_ROW = 20;
	public static final int SUBMAP_TERRAINS_COLUMN = 20;
	public static final int VIEW_TERRAINS_ROW = 15;
	public static final int VIEW_TERRAINS_COLUMN = 10;

	public static final short V_STATUS = 0;
	public static final short V_TIME = 1;
	public static final short V_DAY = 2;
	public static final short V_NIGHT = 3;
	public static final short V_FINE = 4;
	public static final short V_RAIN_SMALL = 5;
	public static final short V_RAIN_HARD = 6;
	public static final short V_SLEEP_BUTTON = 7;
	public static final short V_WAKE_BUTTON = 8;

	// bag capacity on initial
	public static final int BAG_INITIAL_CAPACITY = 7;
	// Grid view max capacity
	public static final int MAX_GRID_COUNT = 9;

	// status change rate differs from different condition
	public static final int CONDITION_REST = 0;
	public static final int CONDITION_NORMAL = 1;
	public static final int CONDITION_HARD = 2;
	public static final int CONDITION_EXTREME = 3;

	public static final String[] SLEEP_TEXT = { ".", "..", "..z", "..zz", "..zzZ", "..zzZZ" };

	// survivor status decrease definition: X to decrease per second
	public static final float HP_CHANGE_REST = 1f / 5f;
	public static final float HP_CHANGE_NORMAL = 1f / 10f;
	public static final float HP_CHANGE_HARD = 1f / 15f;
	public static final float HP_CHANGE_EXTREME = 1f / 20f;
	public static final float FOOD_CHANGE_REST = -1f / 12f;
	public static final float FOOD_CHANGE_NORMAL = -1f / 8f;
	public static final float FOOD_CHANGE_HARD = -1f / 4f;
	public static final float FOOD_CHANGE_EXTREME = -1f / 2f;
	public static final float WATER_CHANGE_REST = -1f / 9f;
	public static final float WATER_CHANGE_NORMAL = -1f / 6f;
	public static final float WATER_CHANGE_HARD = -1f / 3f;
	public static final float WATER_CHANGE_EXTREME = -1f / 1f;
	public static final float SP_CHANGE_REST = -1f / 45f;
	public static final float SP_CHANGE_NORMAL = -1f / 30f;
	public static final float SP_CHANGE_HARD = -1f / 15f;
	public static final float SP_CHANGE_EXTREME = -1f / 5f;

	/** element type definition */
	public static final int TYPE_TOOL = 0;
	public static final int TYPE_MATERIAL = 1;
	public static final int TYPE_EQUIP = 2;
	public static final int TYPE_EDIBLE = 3;
	public static final int TYPE_EDIBLE_COOKABLE = 4;
	public static final int TYPE_LIGHTING = 5;

}
