package lan.survivor.environment;

import lan.survivor.element.Terrain;
import android.util.SparseArray;

public class Physics {

	public static final short PASSABLE = 0;
	public static final short DIGGABLE = 1;
	public static SparseArray<Boolean[]> terrainProperties = new SparseArray<Boolean[]>();

	static {
		terrainProperties.put(Terrain.TERRAIN_SEA, new Boolean[] { true, false });
		terrainProperties.put(Terrain.TERRAIN_SAND, new Boolean[] { true, true });
		terrainProperties.put(Terrain.TERRAIN_DIRT, new Boolean[] { true, true });
		terrainProperties.put(Terrain.TERRAIN_GRASSLAND, new Boolean[] { true, true });
		terrainProperties.put(Terrain.TERRAIN_SEA_DEAP, new Boolean[] { false, false });
		terrainProperties.put(Terrain.TERRAIN_SNOW, new Boolean[] { true, true });
		terrainProperties.put(Terrain.TERRAIN_ROCKS, new Boolean[] { true, false });
		terrainProperties.put(Terrain.TERRAIN_GRASS, new Boolean[] { true, true });
	}

	public static boolean getTerrainProperty(int terrain, short type) {
		switch (type) {
		case PASSABLE:
			return terrainProperties.get(terrain)[PASSABLE];
		case DIGGABLE:
			return terrainProperties.get(terrain)[DIGGABLE];
		}
		return false;
	}
}
