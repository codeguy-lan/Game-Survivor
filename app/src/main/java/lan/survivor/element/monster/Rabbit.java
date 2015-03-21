package lan.survivor.element.monster;

import lan.survivor.element.item.edible.EdibleElement;
import lan.survivor.utils.ID;

public class Rabbit extends MonsterElement {

	public Rabbit() {
		speciesID = ID.MST_RABBIT;
		loot.add(new EdibleElement(ID.EDB_MEAT_RAW, 2));
		hp = 10;
		atk = 0;
		def = 0;
	}
}
