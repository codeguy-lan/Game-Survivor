package lan.survivor.element.building;

import lan.survivor.element.Terrain;
import lan.survivor.element.item.ItemElement;
import lan.survivor.element.item.tool.Bottle;
import lan.survivor.utils.ID;
import lan.survivor.window.WellWindow;
import android.content.Context;

public class Well extends BuildingElement {

	public boolean alive = true;

	private static WellWindow ww;

	public Well() {
		speciesID = ID.BLD_WELL;
	}

	@Override
	public void afterSet(Terrain t) {
		super.afterSet(t);
	}

	@Override
	public void showActionBuildingWindow(final Context context) {
		if (ww == null) {
			ww = new WellWindow(context, this);
		} else {
			ww.setWell(this);
		}
		ww.showWindow();
	}

	public void fill() {
		for (ItemElement se : containerList) {
			if (se instanceof Bottle) {
				((Bottle) se).fill(0, Bottle.MAX_AMOUNT);
			}
		}
	}
}
