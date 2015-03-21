package lan.survivor.utils;

import java.util.Random;
import lan.survivor.element.Terrain;
import lan.survivor.element.provider.ProviderElement;

public class Generator {

	public static Random rand = new Random();

	public static void generateObjectOnTerrain(Terrain t) {
		// rand.setSeed(System.currentTimeMillis());
		if (t.terrainType == Terrain.TERRAIN_GRASS) {
			if (rand.nextInt(5) == 0) {
				t.addElement(new ProviderElement(ID.PVD_TREE_NORMAL));
			} else if (rand.nextInt(5) == 0) {
				t.addElement(new ProviderElement(ID.PVD_TREE_BANANA));
			}
		} else if (t.terrainType == Terrain.TERRAIN_DIRT) {
			if (rand.nextInt(4) == 1) {
				t.addElement(new ProviderElement(ID.PVD_BUSH));
			}
		} else if (t.terrainType == Terrain.TERRAIN_ROCKS) {
			if (rand.nextInt(5) == 1) {
				t.addElement(new ProviderElement(ID.PVD_ROCK));
			}
		} else if (t.terrainType == Terrain.TERRAIN_SAND) {
			if (rand.nextInt(5) == 1) {
				t.addElement(new ProviderElement(ID.PVD_TREE_COCONUT));
			}
		}
	}
}
