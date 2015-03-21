package lan.survivor.environment;

import java.io.IOException;

import lan.survivor.R;
import lan.survivor.application.GameApplication;
import lan.survivor.element.Survivor;
import lan.survivor.element.Terrain;
import lan.survivor.utils.Const;
import lan.survivor.utils.GameData;
import lan.survivor.utils.Generator;
import lan.survivor.utils.MapLoader;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class WorldMap {

	public static Terrain[][] map = new Terrain[Const.MAP_TERRAINS_ROW][Const.MAP_TERRAINS_COLUMN];
	public static Bitmap[] terrainTexture = new Bitmap[Terrain.TERRAIN_ALL];
	public static int mapWidth;
	public static int mapHeight;
	public static int terrainBlockW;
	public static int terrainBlockH;
	public static int terrainHalfW;
	public static int terrainHalfH;
	public static float currentX = 0; // left
	public static float currentY = 0; // top
	public static int leftTopRow = 0;
	public static int leftTopColumn = 0;
	public static int scrollDirection = -1;
	public static boolean isInSubMap = false;

	private int pRowInView = Const.VIEW_TERRAINS_ROW + 1;
	private int pColumnInView = Const.VIEW_TERRAINS_COLUMN + 1;

	private int pOffsetX = 0;
	private int pOffsetY = 0;
	private int pBoundRight = 0;
	private int pBoundBottom = 0;

	public WorldMap(int viewWidth, int viewHeight, GameData gd) {
		terrainBlockW = viewWidth / Const.VIEW_TERRAINS_COLUMN;
		terrainBlockH = viewHeight / Const.VIEW_TERRAINS_ROW;
		terrainHalfW = terrainBlockW / 2;
		terrainHalfH = terrainBlockH / 2;
		mapWidth = terrainBlockW * Const.MAP_TERRAINS_COLUMN;
		mapHeight = terrainBlockH * Const.MAP_TERRAINS_ROW;
		pBoundRight = mapWidth - viewWidth - terrainBlockW;
		pBoundBottom = mapHeight - viewHeight - terrainBlockH;
		if (gd != null) {
			map = gd.map;
			currentX = gd.mapCurrentX;
			currentY = gd.mapCurrentY;
		} else {
			try {
				MapLoader.loadMap(GameApplication.getAppContext(), map);
			} catch (IOException e) {
				//e.printStackTrace();
				for (short i = 0; i < Const.MAP_TERRAINS_ROW; i++) {
					for (short j = 0; j < Const.MAP_TERRAINS_COLUMN; j++) {
						map[i][j] = new Terrain((short) ((i * j) % 8 + 1), i, j);
						Generator.generateObjectOnTerrain(map[i][j]);
					}
				}
			}
			currentX = mapWidth / 10;
			currentY = mapHeight / 5 * 4;
		}
		loadTerrainTexture();
	}

	public static Terrain getTerrainBlock(float x, float y) {
		return map[(int) (y / terrainBlockH)][(int) (x / terrainBlockW)];
	}

	private void loadTerrainTexture() {
		Resources res = GameApplication.getAppContext().getResources();
		int n = 0;
		Bitmap orig = BitmapFactory.decodeResource(res, R.drawable.terrains);
		int singleImgWidth = orig.getWidth() / 5;
		int singleImgHeight = orig.getHeight() / 2;
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 5; j++) {
				terrainTexture[n++] = Bitmap
						.createScaledBitmap(Bitmap.createBitmap(orig, j * singleImgWidth, i * singleImgHeight, singleImgWidth, singleImgHeight), terrainBlockW, terrainBlockH, true);
				if (n > 8) {
					break;
				}
			}
		}
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
		leftTopColumn = (int) (currentX / terrainBlockW);
		leftTopRow = (int) (currentY / terrainBlockH);
		pOffsetX = (int) (currentX - leftTopColumn * terrainBlockW);
		pOffsetY = (int) (currentY - leftTopRow * terrainBlockH);
		Survivor.updatePosition();
	}

	public void drawMap(Canvas c) {
		for (int i = 0; i < pRowInView; i++) {
			for (int j = 0; j < pColumnInView; j++) {
				map[leftTopRow + i][leftTopColumn + j].draw(c, i, j, pOffsetX, pOffsetY);
			}
		}
	}
}
