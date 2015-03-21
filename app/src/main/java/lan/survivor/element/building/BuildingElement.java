package lan.survivor.element.building;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lan.survivor.actions.InventoryManager;
import lan.survivor.element.BaseElement;
import lan.survivor.element.Terrain;
import lan.survivor.element.item.ItemElement;
import lan.survivor.element.item.tool.Bottle;
import lan.survivor.utils.Const;
import lan.survivor.utils.RenderResPool;
import android.content.Context;
import android.graphics.Canvas;

public abstract class BuildingElement extends BaseElement {

	public Terrain terrain;
	public boolean alive = true;
	public CopyOnWriteArrayList<ItemElement> containerList = new CopyOnWriteArrayList<ItemElement>();

	public abstract void showActionBuildingWindow(Context context);

	@Override
	public void afterSet(Terrain t) {
		terrain = t;
	}

	public List<ItemElement> provideList() {
		return containerList;
	}

	@Override
	public void draw(Canvas c) {
		if (alive) {
			c.drawBitmap(RenderResPool.getImg(speciesID), centerX - RenderResPool.PE_HALF_W, centerY - RenderResPool.PE_HALF_H, null);
		}
	}

	public void addToGrid(ItemElement se) {
		if (containerList.size() < Const.MAX_GRID_COUNT) {
			if (se.stackCount > 0) {
				try {
					if (se instanceof Bottle) {
						containerList.add(((Bottle) se).getNewCopy());
					} else {
						containerList.add(se.getClass().getConstructor(int.class).newInstance(se.speciesID));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				se.stackCount--;
				if (se.stackCount < 1) {
					InventoryManager.remove(se);
				}
			}
		}
	}

	public void removeFromGrid(ItemElement se) {
		if (InventoryManager.add(se)) {
			containerList.remove(se);
		}
	}
}
