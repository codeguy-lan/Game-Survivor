package lan.survivor.environment;

import lan.survivor.element.Survivor;
import lan.survivor.element.Terrain;
import lan.survivor.utils.Const;
import lan.survivor.utils.Generator;
import android.graphics.Canvas;

public class SubWorldMap {

	public static Terrain[][] map = new Terrain[Const.SUBMAP_TERRAINS_ROW][Const.SUBMAP_TERRAINS_COLUMN];
	public static int mapWidth;
	public static int mapHeight;
	public static float currentX = 0; // left
	public static float currentY = 0; // top
	public static int leftTopRow = 0;
	public static int leftTopColumn = 0;
	public static int scrollDirection = -1;

	private int pRowInView = Const.VIEW_TERRAINS_ROW + 1;
	private int pColumnInView = Const.VIEW_TERRAINS_COLUMN + 1;

	private int pOffsetX = 0;
	private int pOffsetY = 0;
	private int pBoundRight = 0;
	private int pBoundBottom = 0;

	public SubWorldMap(int viewWidth, int viewHeight) {
		mapWidth = WorldMap.terrainBlockW * Const.SUBMAP_TERRAINS_COLUMN;
		mapHeight = WorldMap.terrainBlockH * Const.SUBMAP_TERRAINS_ROW;
		pBoundRight = mapWidth - viewWidth - WorldMap.terrainBlockW;
		pBoundBottom = mapHeight - viewHeight - WorldMap.terrainBlockH;
		for (short i = 0; i < Const.SUBMAP_TERRAINS_ROW; i++) {
			for (short j = 0; j < Const.SUBMAP_TERRAINS_COLUMN; j++) {
				map[i][j] = new Terrain((short) Terrain.TERRAIN_GRASSLAND, i, j);
				Generator.generateObjectOnTerrain(map[i][j]);
			}
		}
		currentX = mapWidth / 2;
		currentY = mapHeight / 2;
	}

	public static Terrain getTerrainBlock(float x, float y) {
		return map[(int) (y / WorldMap.terrainBlockH)][(int) (x / WorldMap.terrainBlockW)];
	}


	public void updateMap(float delta, Survivor s) {
		switch (scrollDirection) {
		case Const.DIRECTION_L:
			// two point to determine: left top, left bottom
			if (getTerrainBlock(s.currentX - s.charaHalfW - delta, s.currentY - s.charaHalfH).passable && getTerrainBlock(s.currentX - s.charaHalfW - delta, s.currentY + s.charaHalfH).passable) {
				currentX -= delta;
			}
			break;
		case Const.DIRECTION_U:
			// left top, right top
			if (getTerrainBlock(s.currentX - s.charaHalfW, s.currentY - s.charaHalfH - delta).passable && getTerrainBlock(s.currentX + s.charaHalfW, s.currentY - s.charaHalfH - delta).passable) {
				currentY -= delta;
			}
			break;
		case Const.DIRECTION_R:
			// right top, right bottom
			if (getTerrainBlock(s.currentX + s.charaHalfW + delta, s.currentY - s.charaHalfH).passable && getTerrainBlock(s.currentX + s.charaHalfW + delta, s.currentY + s.charaHalfH).passable) {
				currentX += delta;
			}
			break;
		case Const.DIRECTION_D:
			// left bottom, right bottom
			if (getTerrainBlock(s.currentX - s.charaHalfW, s.currentY + s.charaHalfH + delta).passable && getTerrainBlock(s.currentX + s.charaHalfW, s.currentY + s.charaHalfH + delta).passable) {
				currentY += delta;
			}
			break;
		default:
			break;
		}
		if (currentX < 0) {
			currentX = 0;
		} else if (currentX > pBoundRight) {
			currentX = pBoundRight;
		}
		if (currentY < 0) {
			currentY = 0;
		} else if (currentY > pBoundBottom) {
			currentY = pBoundBottom;
		}
		leftTopColumn = (int) (currentX / WorldMap.terrainBlockW);
		leftTopRow = (int) (currentY / WorldMap.terrainBlockH);
		pOffsetX = (int) (currentX - leftTopColumn * WorldMap.terrainBlockW);
		pOffsetY = (int) (currentY - leftTopRow * WorldMap.terrainBlockH);
		s.updatePosition();
	}

	public void drawMap(Canvas c) {
		for (int i = 0; i < pRowInView; i++) {
			for (int j = 0; j < pColumnInView; j++) {
				map[leftTopRow + i][leftTopColumn + j].draw(c, i, j, pOffsetX, pOffsetY);
			}
		}
	}
}
