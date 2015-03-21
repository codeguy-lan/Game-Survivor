package lan.survivor.element.item.equip;

import lan.survivor.actions.StatusManager;
import lan.survivor.element.item.ItemElement;
import lan.survivor.utils.Const;
import lan.survivor.utils.DataBaseLogic;

public class EquipElement extends ItemElement {

	public static int type = Const.TYPE_EQUIP;
	public static int maxStack = 1;
	public int part = 0;
	public int attack = 0, defense = 0, speed = 0;

	public EquipElement(int id) {
		speciesID = id;
		int[] properties = DataBaseLogic.getEquipProperties(id);
		part = properties[0];
		attack = properties[1];
		defense = properties[2];
		speed = properties[3];
	}

	public void equipEffect() {
		StatusManager.addEquipStatus(attack, defense, speed);
	}

	public void takeOffEffect() {
		StatusManager.addEquipStatus(-attack, -defense, -speed);
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
