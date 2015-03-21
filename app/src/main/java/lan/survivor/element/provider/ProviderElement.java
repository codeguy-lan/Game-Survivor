package lan.survivor.element.provider;

import java.util.ArrayList;
import java.util.List;
import lan.survivor.element.BaseElement;
import lan.survivor.element.Terrain;
import lan.survivor.element.item.ItemElement;
import lan.survivor.element.item.edible.EdibleElement;
import lan.survivor.element.item.material.MaterialElement;
import lan.survivor.utils.DataBaseLogic;
import lan.survivor.utils.ID;
import lan.survivor.utils.Misc;
import lan.survivor.utils.RenderResPool;
import android.graphics.Canvas;

public class ProviderElement extends BaseElement {

	public boolean alive = true;
	public ArrayList<ItemElement> containerList = new ArrayList<ItemElement>();

	public ProviderElement(int id) {
		speciesID = id;
		ArrayList<int[]> list = DataBaseLogic.getProviderItemList(speciesID);
		for (int[] item : list) {
			containerList.add(createItem(item[0], item[1] + Misc.rollMinus(item[2])));
		}
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

	public ItemElement getItem(Terrain t, ItemElement ce) {
		if (ce.stackCount > 0) {
			ce.stackCount--;
			if (isTree()) {
				if (ce.speciesID == ID.MTR_WOOD && ce.stackCount == 0) {
					alive = false;
					t.passable = true;
				}
			}
			if (ce instanceof MaterialElement) {
				return new MaterialElement(ce.speciesID);
			} else if (ce instanceof EdibleElement) {
				return new EdibleElement(ce.speciesID);
			}
		}
		return null;
	}

	public ItemElement provide(Terrain t, int pos) {
		ItemElement ce = containerList.get(pos);
		return getItem(t, ce);
	}

	public void afterDeath(Terrain t) {
		for (ItemElement se : containerList) {
			if (se.stackCount > 0) {
				t.addElement(se);
			}
		}
	}

	private ItemElement createItem(int id, int baseNumber) {
		return ItemElement.createResourceItem(id, baseNumber);
	}

	private boolean isTree() {
		return (speciesID == ID.PVD_TREE_NORMAL || speciesID == ID.PVD_TREE_BANANA || speciesID == ID.PVD_TREE_BANANA_EMPTY || speciesID == ID.PVD_TREE_COCONUT);
	}
}
