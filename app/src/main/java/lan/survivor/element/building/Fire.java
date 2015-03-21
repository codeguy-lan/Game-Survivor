package lan.survivor.element.building;

import lan.survivor.R;
import lan.survivor.application.GameApplication;
import lan.survivor.element.BaseElement;
import lan.survivor.element.Terrain;
import lan.survivor.element.item.ItemElement;
import lan.survivor.environment.World;
import lan.survivor.environment.WorldMap;
import lan.survivor.utils.Const;
import lan.survivor.utils.ID;
import lan.survivor.utils.RefTable;
import lan.survivor.utils.RenderResPool;
import lan.survivor.window.FireWindow;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Fire extends BuildingElement {

	public static final short LIGHT_RANGE = 2;

	public boolean alive = true;
	public boolean fireOn = true;

	private static FireWindow fw;
	private static Bitmap[] imgFire = new Bitmap[2];
	private static final short ANIM_CHANGE = 5;
	private short animCount = 0;
	private short index = 0;

	public Fire() {
		speciesID = ID.BLD_FIRE;
		imgFire[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameApplication.getAppContext().getResources(), R.drawable.fire), 50, 50, true);
		imgFire[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(GameApplication.getAppContext().getResources(), R.drawable.fire1), 50, 50, true);
	}

	@Override
	public void draw(Canvas c) {
		if (alive) {
			if (fireOn) {
				animCount++;
				if (animCount > ANIM_CHANGE) {
					animCount = 0;
					index ^= 1;
				}
				c.drawBitmap(imgFire[index], centerX - RenderResPool.PE_HALF_W, centerY - RenderResPool.PE_HALF_H, null);
			} else {
				super.draw(c);
			}
		}
	}

	@Override
	public void afterSet(Terrain t) {
		super.afterSet(t);

		for (int i = t.blockRow - LIGHT_RANGE; i <= t.blockRow + LIGHT_RANGE; i++) {
			for (int j = t.blockColumn - LIGHT_RANGE; j <= t.blockColumn + LIGHT_RANGE; j++) {
				WorldMap.map[i][j].lightened++;
			}
		}
		// make the range like a circle
		WorldMap.map[t.blockRow - LIGHT_RANGE][t.blockColumn - LIGHT_RANGE].lightened--;
		WorldMap.map[t.blockRow - LIGHT_RANGE][t.blockColumn + LIGHT_RANGE].lightened--;
		WorldMap.map[t.blockRow + LIGHT_RANGE][t.blockColumn - LIGHT_RANGE].lightened--;
		WorldMap.map[t.blockRow + LIGHT_RANGE][t.blockColumn + LIGHT_RANGE].lightened--;
		if (World.weather == Const.V_RAIN_HARD) {
			putOut();
		}
		World.registElement(this);
	}

	@Override
	public void showActionBuildingWindow(Context context) {
		if (fw == null) {
			fw = new FireWindow(context, this);
		} else {
			fw.setFire(this);
		}
		fw.showWindow();
	}

	public boolean lightOn() {
		for (ItemElement se : containerList) {
			if (se.speciesID == ID.MTR_FLINT) {
				containerList.remove(se);
				fireOn = true;
				speciesID = ID.BLD_FIRE;
				return true;
			}
		}
		return false;
	}

	public void process() {
		for (ItemElement se : containerList) {
			// if (se instanceof EdibleElement) {
			// EventManager.fireCook((EdibleElement) se);
			// } else {
			int[] ids = RefTable.getFireProcessID(se.speciesID);
			BaseElement[] results = BaseElement.createElements(ids.length, ids);
			if (results[0] != null) {
				for (BaseElement e : results) {
					containerList.add(containerList.indexOf(se), (ItemElement) e);
				}
				containerList.remove(se);
			}
			// }
		}
	}

	public void putOut() {
		if (!fireOn) {
			return;
		}
		fireOn = false;
		speciesID = ID.BLD_FIRE_OFF;
		for (int i = terrain.blockRow - LIGHT_RANGE; i <= terrain.blockRow + LIGHT_RANGE; i++) {
			for (int j = terrain.blockColumn - LIGHT_RANGE; j <= terrain.blockColumn + LIGHT_RANGE; j++) {
				WorldMap.map[i][j].lightened--;
			}
		}
	}
}
