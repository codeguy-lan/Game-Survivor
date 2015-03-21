package lan.survivor.utils;

import java.util.HashMap;
import lan.survivor.R;
import lan.survivor.element.Terrain;
import android.util.SparseArray;

public class RefTable {

	private static final SparseArray<int[]> terrainInfo = new SparseArray<int[]>();
	private static final SparseArray<String> itemClassName = new SparseArray<String>();
	private static final HashMap<String, Integer> creationList = new HashMap<String, Integer>();
	private static final SparseArray<int[]> fireProcessList = new SparseArray<int[]>();

	/** terrain text info */
	static {
		terrainInfo.put(Terrain.TERRAIN_SEA, new int[] { R.string.name_terrain_sea, R.string.detail_terrain_sea });
		terrainInfo.put(Terrain.TERRAIN_SEA_DEAP, new int[] { R.string.name_terrain_sea, R.string.detail_terrain_sea });
		terrainInfo.put(Terrain.TERRAIN_SAND, new int[] { R.string.name_terrain_sand, R.string.detail_terrain_sand });
		terrainInfo.put(Terrain.TERRAIN_DIRT, new int[] { R.string.name_terrain_dirt, R.string.detail_terrain_dirt });
		terrainInfo.put(Terrain.TERRAIN_GRASS, new int[] { R.string.name_terrain_grass, R.string.detail_terrain_grass });
		terrainInfo.put(Terrain.TERRAIN_GRASSLAND, new int[] { R.string.name_terrain_grassland, R.string.detail_terrain_grassland });
		terrainInfo.put(Terrain.TERRAIN_ROCKS, new int[] { R.string.name_terrain_rocks, R.string.detail_terrain_rocks });
		terrainInfo.put(Terrain.TERRAIN_SNOW, new int[] { R.string.name_terrain_snow, R.string.detail_terrain_snow });
	}

	static {
		DataBaseLogic.registItemClassName(itemClassName);
	}

	/** creation recipe */
	static {
		creationList.put(new String(new char[] { ID.MTR_WOOD, '-', ID.MTR_BRANCH, '-', ID.MTR_FLINT }), ID.BLD_FIRE);
		creationList.put(new String(new char[] { ID.MTR_WOOD, '-', ID.MTR_WOOD, '-', ID.MTR_WOOD, '-', ID.MTR_WOOD, '-', ID.MTR_WOOD }), ID.BLD_SHELTER);
	}

	/** process list for fire */
	static {
		fireProcessList.put(ID.EDB_MEAT_RAW, new int[] { ID.EDB_MEAT_COOKED });
		fireProcessList.put(ID.EDB_FISH_RAW, new int[] { ID.EDB_FISH_COOKED });
		fireProcessList.put(ID.TOL_BOTTLE_SEA_FILL, new int[] { ID.TOL_BOTTLE_EMPTY, ID.MTR_SALT });
	}

	public static int getTerrainNameID(short terrainID) {
		return terrainInfo.get(terrainID)[0];
	}

	public static int getTerrainDetailID(short terrainID) {
		return terrainInfo.get(terrainID)[1];
	}

	public static String getItemClassName(int id) {
		return itemClassName.get(id);
	}

	public static int getCreationResultID(String key) {
		if (creationList.containsKey(key)) {
			return creationList.get(key);
		} else {
			return -1;
		}
	}

	public static int[] getFireProcessID(int key) {
		return fireProcessList.get(key, new int[] { -1 });
	}
}
