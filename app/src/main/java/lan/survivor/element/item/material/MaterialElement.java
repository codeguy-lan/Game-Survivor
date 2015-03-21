package lan.survivor.element.item.material;

import lan.survivor.element.item.ItemElement;
import lan.survivor.utils.Const;

public class MaterialElement extends ItemElement {

	public static int type = Const.TYPE_MATERIAL;
	public static int maxStack = 5;

	public MaterialElement(int id) {
		this(id, 1);
	}

	public MaterialElement(int id, int amount) {
		stackCount = amount;
		speciesID = id;
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