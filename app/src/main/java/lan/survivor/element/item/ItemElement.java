package lan.survivor.element.item;

import lan.survivor.element.BaseElement;
import lan.survivor.utils.ID;
import lan.survivor.utils.RenderResPool;
import android.graphics.Canvas;

public abstract class ItemElement extends BaseElement {

	public static String X = "X ";
	public int stackCount = 1;

	public abstract int getType();

	public abstract int getMaxStack();

	public int stack(int n, boolean checkLimit) {
		if (checkLimit) {
			int addition = getMaxStack() - stackCount;
			if (n > addition) {
				stackCount += addition;
				return n - addition;
			} else {
				stackCount += n;
				return 0;
			}
		} else {
			stackCount += n;
			return 0;
		}
	}

	@Override
	public void draw(Canvas c) {
		c.drawBitmap(RenderResPool.getImg(speciesID), centerX - RenderResPool.SE_HALF_W, centerY - RenderResPool.SE_HALF_H, null);
		c.drawText(X + String.valueOf(stackCount), centerX, centerY + RenderResPool.SE_HALF_H, RenderResPool.p);
	}

	public static ItemElement createResourceItem(int id, int count) {
		if (id == -1) {
			return null;
		}
		String sub = "";
		if (id >= ID.MTR_SECTION && id < ID.EDB_SECTION) {
			sub = "item.material.MaterialElement";
		} else if (id >= ID.EDB_SECTION && id < ID.TOL_SECTION) {
			sub = "item.edible.EdibleElement";
		} else {
			return null;
		}
		try {
			return (ItemElement) Class.forName("lan.survivor.element." + sub).getConstructor(int.class, int.class).newInstance(id, count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
