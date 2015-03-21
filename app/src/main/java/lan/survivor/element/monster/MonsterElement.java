package lan.survivor.element.monster;

import java.util.ArrayList;
import lan.survivor.element.BaseElement;
import lan.survivor.element.item.ItemElement;
import lan.survivor.utils.RenderResPool;
import android.graphics.Canvas;

public class MonsterElement extends BaseElement {

	public boolean alive = true;
	public ArrayList<ItemElement> loot = new ArrayList<ItemElement>();
	public int hp;
	public int atk;
	public int def;

	@Override
	public void draw(Canvas c) {
		if (alive) {
			c.drawBitmap(RenderResPool.getImg(speciesID), centerX - RenderResPool.PE_HALF_W, centerY - RenderResPool.PE_HALF_H, null);
		}
	}

	public ArrayList<ItemElement> getLoot() {
		return loot;
	}
}