package lan.survivor.actions;

import java.util.concurrent.CopyOnWriteArrayList;
import lan.survivor.environment.World;
import lan.survivor.utils.Const;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class StatusManager {

	public static final int STATUS_MAX_INIT = 100;
	public static int STATUS_MAX = STATUS_MAX_INIT;
	public static int condition = Const.CONDITION_NORMAL;

	public static final int FOOD = 0;
	public static final int WATER = 1;
	public static final int SP = 2;
	public static final int HP = 3;
	public static final float[][] change = new float[][] { { Const.FOOD_CHANGE_REST, Const.FOOD_CHANGE_NORMAL, Const.FOOD_CHANGE_HARD, Const.FOOD_CHANGE_EXTREME },
			{ Const.WATER_CHANGE_REST, Const.WATER_CHANGE_NORMAL, Const.WATER_CHANGE_HARD, Const.WATER_CHANGE_EXTREME },
			{ Const.SP_CHANGE_REST, Const.SP_CHANGE_NORMAL, Const.SP_CHANGE_HARD, Const.SP_CHANGE_EXTREME },
			{ Const.HP_CHANGE_REST, Const.HP_CHANGE_NORMAL, Const.HP_CHANGE_HARD, Const.HP_CHANGE_EXTREME } };

	public static float[] status = new float[] { STATUS_MAX_INIT, STATUS_MAX_INIT, STATUS_MAX_INIT, STATUS_MAX_INIT };
	public static boolean inSleep = false;
	public static boolean inShelter = false;
	public static float spRecovery = 2;

	public static final int ATTACK = 0;
	public static final int DEFENSE = 1;
	public static final int SPEED = 2;
	public static final int ATTACK_DEFAULT = 5;
	public static final int DEFENCE_DEFAULT = 0;
	public static final int MOVE_SPEED_DEFAULT = 100;
	public static int[] equipStatus = new int[] { ATTACK_DEFAULT, DEFENCE_DEFAULT, MOVE_SPEED_DEFAULT };

	private static CopyOnWriteArrayList<ItemEffect> statusList = new CopyOnWriteArrayList<ItemEffect>();
	private static StatusTask pStatusTask;
	private static boolean pRunning = false;
	private static boolean pPause = false;
	private static Handler pHandler;
	private static Message pMsg;

	public static void registHandler(Handler h) {
		pHandler = h;
	}

	public static void start() {
		if (pStatusTask == null) {
			Log.v("status", "new status task");
			pStatusTask = new StatusTask();
		}
		if (!pRunning) {
			pRunning = true;
			pStatusTask.execute();
		}
	}

	public static void end() {
		Log.v("status", "end status task");
		pRunning = false;
		pStatusTask = null;
	}

	public static void setPause(boolean b) {
		if (pRunning) {
			pPause = b;
		}
	}

	public static void addEquipStatus(int atk, int def, int spd) {
		equipStatus[ATTACK] += atk;
		equipStatus[DEFENSE] += def;
		equipStatus[SPEED] += spd;
	}

	public synchronized static void addStatusEffect(ItemEffect ses) {
		statusList.add(ses);
	}

	public synchronized static void addStatusEffect(ItemEffect[] ses) {
		for (ItemEffect se : ses) {
			statusList.add(se);
		}
	}

	public synchronized static void spendTimeUpdateStatus(int sec) {
		for (int i = 0; i < sec; i++) {
			updateStatus();
		}
	}

	private static class StatusTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			while (stillAlive()) {
				if (pRunning) {
					if (!pPause) {
						try {
							synchronized (this) {
								wait(1000);
							}
						} catch (InterruptedException e) {
						}
						updateStatus();
					}
				} else {
					return true;
				}
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean alive) {
			if (alive) {
				Log.v("STATUS", "status stoped");
			} else {
				// notify death...
				pRunning = false;
				Log.v("STATUS", "survivor has died");
			}
		}
	}

	private static boolean stillAlive() {
		if (status[HP] > 0) {
			return true;
		}
		return false;
	}

	private synchronized static void updateStatus() {
		status[HP] += change[HP][condition];
		status[FOOD] += change[FOOD][condition];
		status[WATER] += change[WATER][condition];
		if (inSleep) {
			status[SP] += spRecovery;
		} else {
			status[SP] += change[SP][condition];
		}

		for (ItemEffect e : statusList) {
			if (e.time < 0) {
				statusList.remove(e);
			} else {
				updateInstantStatus(e.type, e.amount);
				e.time--;
			}
		}
		if (status[HP] > STATUS_MAX) {
			status[HP] = STATUS_MAX;
		}
		if (status[FOOD] < 0) {
			status[FOOD] = 0;
		} else if (status[FOOD] > STATUS_MAX) {
			status[FOOD] = STATUS_MAX;
		}
		if (status[WATER] < 0) {
			status[WATER] = 0;
		} else if (status[WATER] > STATUS_MAX) {
			status[WATER] = STATUS_MAX;
		}
		if (status[SP] < 0) {
			status[SP] = 0;
			// force sleep
			World.toSleepMode(true);
			inSleep = true;
			changeSleepButton(true);
		} else if (status[SP] > STATUS_MAX) {
			status[SP] = STATUS_MAX;
		}
		updateTextInfo();
	}

	private synchronized static void updateInstantStatus(int type, int amount) {
		status[type] += amount;
		updateTextInfo();
	}

	private static void updateTextInfo() {
		pMsg = pHandler.obtainMessage();
		Bundle b = new Bundle();
		b.putString("food", String.valueOf((int) status[FOOD]));
		b.putString("water", String.valueOf((int) status[WATER]));
		b.putString("health", String.valueOf((int) status[HP]));
		b.putString("stamina", String.valueOf((int) status[SP]));
		pMsg.setData(b);
		pMsg.what = Const.V_STATUS;
		pHandler.sendMessage(pMsg);
	}

	private static void changeSleepButton(boolean sleep) {
		pMsg = pHandler.obtainMessage();
		pMsg.what = sleep ? Const.V_SLEEP_BUTTON : Const.V_WAKE_BUTTON;
		pHandler.sendMessage(pMsg);
	}
}
