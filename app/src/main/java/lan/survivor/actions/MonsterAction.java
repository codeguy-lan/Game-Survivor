package lan.survivor.actions;

import lan.survivor.element.monster.MonsterElement;

public class MonsterAction {

	public static void monsterAct(MonsterElement monster) {
		StatusManager.status[StatusManager.HP] -= monster.atk;
	}
}
