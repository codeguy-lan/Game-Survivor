package lan.survivor.element.item.edible;

import lan.survivor.actions.ItemEffect;
import lan.survivor.element.item.ItemElement;
import lan.survivor.utils.Const;
import lan.survivor.utils.DataBaseLogic;

public class EdibleElement extends ItemElement {

	public static final int RAW = 0;
	public static final int COOKED = 1;
	public static final int ROTEN = 2;

	public static int type = Const.TYPE_EDIBLE;
	public static int maxStack = 10;

	public int foodStatus = RAW;

	public EdibleElement(int id) {
		this(id, 1);
	}

	public EdibleElement(int id, int amount) {
		speciesID = id;
		stackCount = amount;
	}

	public ItemEffect[] getStatusEffect() {
		return DataBaseLogic.getItemEffect(speciesID);
	}

	@Override
	public int getType() {
		return type;
	}

	@Override
	public int getMaxStack() {
		return maxStack;
	}
}
