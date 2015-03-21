package lan.survivor.element;

import lan.survivor.R;
import lan.survivor.actions.StatusManager;
import lan.survivor.application.GameApplication;
import lan.survivor.environment.SubWorldMap;
import lan.survivor.environment.WorldMap;
import lan.survivor.utils.Const;
import lan.survivor.utils.RenderResPool;
import lan.survivor.view.GameView;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Survivor {

	public static int charaWidth;
	public static int charaHeight;
	public static int charaHalfW;
	public static int charaHalfH;

	/** [0]down [1]left [2]right [3]up */
	public static Bitmap[][] charaImg = new Bitmap[4][3];

	// center point of character
	public static float currentX;
	public static float currentY;
	public static int facing = Const.DIRECTION_D;
	public static boolean isMoving = false;

	private static final short moveAnimPeriod = 15;
	private short moveAnimCount = 0;
	private short moveIndex = 0;

	private static final short animPeriod = 30;
	private short animCount = 0;
	private short index = 0;

	public Survivor() {
		Resources res = GameApplication.getAppContext().getResources();
		charaWidth = WorldMap.terrainBlockW - 10;
		charaHeight = charaWidth;
		charaHalfW = charaWidth / 2;
		charaHalfH = charaHalfW;
		Bitmap orig = BitmapFactory.decodeResource(res, R.drawable.chara);
		int singleImgWidth = orig.getWidth() / 3;
		int singleImgHeight = orig.getHeight() / 4;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
				charaImg[i][j] = Bitmap.createScaledBitmap(Bitmap.createBitmap(orig, j * singleImgWidth, i * singleImgHeight, singleImgWidth, singleImgHeight), charaWidth, charaHeight, true);
			}
		}

		if (WorldMap.isInSubMap) {
			currentX = SubWorldMap.currentX + GameView.viewCenterX;
			currentY = SubWorldMap.currentY + GameView.viewCenterY;
		} else {
			currentX = WorldMap.currentX + GameView.viewCenterX;
			currentY = WorldMap.currentY + GameView.viewCenterY;
		}
	}

	public static void updatePosition() {
		if (WorldMap.isInSubMap) {
			currentX = SubWorldMap.currentX + GameView.viewCenterX;
			currentY = SubWorldMap.currentY + GameView.viewCenterY;
		} else {
			currentX = WorldMap.currentX + GameView.viewCenterX;
			currentY = WorldMap.currentY + GameView.viewCenterY;
		}
	}

	public void draw(Canvas c) {
		if (!StatusManager.inShelter) {
			if (isMoving) {
				moveAnimCount++;
				if (moveAnimCount > moveAnimPeriod) {
					moveAnimCount = 0;
					moveIndex++;
					if (moveIndex == 3) {
						moveIndex = 0;
					}
				}
			}
			c.drawBitmap(charaImg[facing][moveIndex], GameView.viewCenterX - charaHalfW, GameView.viewCenterY - charaHalfH, null);
			if (StatusManager.inSleep) {
				animCount++;
				if (animCount > animPeriod) {
					animCount = 0;
					index++;
					if (index == Const.SLEEP_TEXT.length) {
						index = 0;
					}
				}
				c.drawText(Const.SLEEP_TEXT[index], GameView.viewCenterX + charaHalfW, GameView.viewCenterY - charaHalfH, RenderResPool.p);
			}
		}
	}

	public Terrain currentTerrainBlock() {
		if (WorldMap.isInSubMap) {
			return SubWorldMap.getTerrainBlock(currentX, currentY);
		} else {
			return WorldMap.getTerrainBlock(currentX, currentY);
		}
	}

	public Terrain facingTerrainBlock() {
		Terrain current = currentTerrainBlock();
		switch (facing) {
		case Const.DIRECTION_L:
			return WorldMap.isInSubMap ? SubWorldMap.map[current.blockRow][current.blockColumn - 1] : WorldMap.map[current.blockRow][current.blockColumn - 1];
		case Const.DIRECTION_U:
			return WorldMap.isInSubMap ? SubWorldMap.map[current.blockRow - 1][current.blockColumn] : WorldMap.map[current.blockRow - 1][current.blockColumn];
		case Const.DIRECTION_R:
			return WorldMap.isInSubMap ? SubWorldMap.map[current.blockRow][current.blockColumn + 1] : WorldMap.map[current.blockRow][current.blockColumn + 1];
		case Const.DIRECTION_D:
			return WorldMap.isInSubMap ? SubWorldMap.map[current.blockRow + 1][current.blockColumn] : WorldMap.map[current.blockRow + 1][current.blockColumn];
		}
		return null;
	}

	public static Bitmap getBattleImg() {
		return charaImg[0][1];
	}
}