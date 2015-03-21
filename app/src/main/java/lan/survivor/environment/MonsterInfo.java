package lan.survivor.environment;

import lan.survivor.element.Terrain;
import lan.survivor.element.monster.MonsterElement;

public class MonsterInfo {
	public MonsterElement monster;
	public Terrain location;
	public int lastTime;

	public MonsterInfo(MonsterElement m, Terrain t, int last) {
		monster = m;
		location = t;
		lastTime = last;
	}
}
