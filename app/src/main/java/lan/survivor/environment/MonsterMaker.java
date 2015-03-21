package lan.survivor.environment;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import lan.survivor.element.Terrain;
import lan.survivor.element.monster.MonsterElement;
import lan.survivor.element.monster.Rabbit;
import lan.survivor.utils.Const;
import lan.survivor.view.GameView;
import android.util.Log;

public class MonsterMaker {

	public static final short MAX_MONSTERS = 25;
	public static final short RANGE_MAX = 10;
	public static final short RANGE_MIN = 3;

	private static CopyOnWriteArrayList<MonsterInfo> monsters = new CopyOnWriteArrayList<MonsterInfo>();
	private static Random rand = new Random();
	private static boolean pRunning = false;
	private static boolean pPause = false;
	private static int startX, startY, endX, endY;
	private static short centerX, centerY;

	private static MonsterThread monsterThread;

	public static void start() {
		if (monsterThread == null) {
			Log.v("status", "new monster thread");
			monsterThread = new MonsterThread();
		}
		if (!pRunning) {
			pRunning = true;
			monsterThread.start();
		}
	}

	public static void setPause(boolean b) {
		if (pRunning) {
			pPause = b;
		}
	}

	public static void end() {
		pRunning = false;
	}

	private static class MonsterThread extends Thread {
		@Override
		public void run() {
			while (pRunning) {
				if (!pPause) {
					try {
						sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// Log.v("Monster Maker", "update!!!!!");
					updateMonsters();
				}
			}
		}

		private synchronized static void updateMonsters() {
			if (pPause) {
				return;
			}
			centerX = GameView.survivor.currentTerrainBlock().blockColumn;
			centerY = GameView.survivor.currentTerrainBlock().blockRow;
			startX = centerX - RANGE_MAX;
			startY = centerY - RANGE_MAX;
			endX = centerX + RANGE_MAX;
			endY = centerY + RANGE_MAX;
			if (startX < 0) {
				startX = 0;
			}
			if (startY < 0) {
				startY = 0;
			}
			if (endX > Const.MAP_TERRAINS_COLUMN) {
				endX = Const.MAP_TERRAINS_COLUMN;
			}
			if (endY > Const.MAP_TERRAINS_ROW) {
				endY = Const.MAP_TERRAINS_ROW;
			}
			disappearMonster();
			for (int y = startY; y < endY; y++) {
				for (int x = startX; x < endX; x++) {
					if (monsters.size() < MAX_MONSTERS) {
						if (y > centerY - RANGE_MIN && y < centerY + RANGE_MIN && x > centerX - RANGE_MIN && x < centerX + RANGE_MIN) {
							// under min range, do nothing
						} else {
							createFromTerrain(WorldMap.map[y][x]);
						}
					}
				}
			}
		}

		private static void disappearMonster() {
			for (MonsterInfo mi : monsters) {
				mi.lastTime--;
				if (mi.lastTime < 1) {
					// monster disappears
					mi.location.elements.remove(mi.monster);
					monsters.remove(mi);
				}
			}
		}

		private static void createFromTerrain(Terrain t) {
			if (t.hasElements()) {
				return;
			}
			if (t.terrainType == Terrain.TERRAIN_GRASS) {
				if (rand.nextInt(50) == 0) {
					MonsterElement me = new Rabbit();
					monsters.add(new MonsterInfo(me, t, rand.nextInt(10) + 2));
					t.addElement(me);
				}
			}
		}
	}

	public static synchronized int getListPosition(Terrain t) {
		for (int i = 0; i < monsters.size(); i++) {
			if (monsters.get(i).location == t) {
				return i;
			}
		}
		return -1;
	}

	public static MonsterElement getMonster(int position) {
		if (position < monsters.size()) {
			return monsters.get(position).monster;
		}
		return null;
	}

	public static void removeMonster(MonsterElement monster) {
		for (MonsterInfo mi : monsters) {
			if (mi.monster == monster) {
				mi.location.elements.remove(mi.monster);
				monsters.remove(mi);
			}
		}
	}

	public static void monsterDefeat(MonsterElement monster) {
		for (MonsterInfo mi : monsters) {
			if (mi.monster == monster) {
				mi.location.addElements(monster.getLoot());
				mi.location.elements.remove(mi.monster);
				monsters.remove(mi);
			}
		}
	}

}
