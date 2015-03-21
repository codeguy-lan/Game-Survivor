package lan.survivor.element.building;

import lan.survivor.actions.StatusManager;
import lan.survivor.element.Terrain;
import lan.survivor.utils.Const;
import lan.survivor.utils.ID;
import lan.survivor.utils.RenderResPool;
import lan.survivor.window.ShelterWindow;
import android.content.Context;
import android.graphics.Canvas;

public class Shelter extends BuildingElement {

	public boolean alive = true;

	private static ShelterWindow sw;
	private static final short animPeriod = 30;
	private short animCount = 0;
	private short index = 0;

	public Shelter() {
		speciesID = ID.BLD_SHELTER;
	}

	@Override
	public void draw(Canvas c) {
		super.draw(c);
		if (StatusManager.inShelter) {
			animCount++;
			if (animCount > animPeriod) {
				animCount = 0;
				index++;
				if (index == Const.SLEEP_TEXT.length) {
					index = 0;
				}
			}
			c.drawText(Const.SLEEP_TEXT[index], centerX + RenderResPool.PE_HALF_W, centerY - RenderResPool.PE_HALF_H, RenderResPool.p);
		}
	}

	@Override
	public void afterSet(Terrain t) {
		super.afterSet(t);
	}

	@Override
	public void showActionBuildingWindow(final Context context) {
		if (sw == null) {
			sw = new ShelterWindow(context, this);
		} else {
			sw.setShelter(this);
		}
		sw.showWindow();
	}
}
