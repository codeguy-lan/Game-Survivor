package lan.survivor.environment;

import java.util.concurrent.CopyOnWriteArrayList;
import lan.survivor.actions.StatusManager;
import lan.survivor.element.BaseElement;
import lan.survivor.element.building.Fire;
import lan.survivor.utils.Const;
import lan.survivor.utils.GameData;
import lan.survivor.utils.Misc;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class World {

	public static final String DAY = "DAY:";
	public static final String COLUMN = ":";
	public static final String SHARP = "00";
	public static final short TIME_FORMAT = 24;
	public static final short TIME_INTERVAL = 10;
	public static final short TIME_INTERVAL_FOR_SEC_NORMAL = 10;
	public static final short TIME_INTERVAL_FOR_SEC_SLEEP = 0;
	public static final short HOUR_DAY_START = 4;
	public static final short HOUR_NIGHT_START = 17;
	public static final int VISIBLE_MIN = 80;
	public static int VISIBLE_MAX;
	public static int DARKNESS_PER_HOUR;
	public static int LIGHTNESS_PER_HOUR;

	public static int days = 1;
	public static short worldTimeH = 8;
	public static short worldTimeM = 0;
	public static short timeIntervalChange = TIME_INTERVAL_FOR_SEC_NORMAL;
	public static short dayNight = Const.V_DAY;
	public static short weather = Const.V_FINE;
	public static float visibleRange;

	private static CopyOnWriteArrayList<BaseElement> effectElements = new CopyOnWriteArrayList<BaseElement>();
	private static short pTimeCounter = 0;
	private static short pWeatherCounter = 0;
	private static short pWeatherLast = 0;
	private static Handler pHandler;
	private static Message pMsg;

	private static WorldThread worldThread;
	private static boolean pRunning = false;
	private static boolean pPause = false;

	public static void init(Handler h, GameData gd, int vcX, int vcY) {
		pHandler = h;
		VISIBLE_MAX = vcY + vcX / 2;
		DARKNESS_PER_HOUR = VISIBLE_MAX / 10;
		LIGHTNESS_PER_HOUR = VISIBLE_MAX / 6;
		if (gd != null) {
			days = gd.days;
			worldTimeH = gd.worldTimeH;
			worldTimeM = gd.worldTimeM;
			visibleRange = gd.visibleRange;
			timeIntervalChange = gd.timeIntervalChange;
			dayNight = gd.dayNight;
			weather = gd.weather;
		} else {
			visibleRange = VISIBLE_MAX;
		}
	}

	public static void start() {
		if (worldThread == null) {
			Log.v("status", "new world thread");
			worldThread = new WorldThread();
		}
		if (!pRunning) {
			pRunning = true;
			worldThread.start();
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

	public static void registElement(BaseElement e) {
		effectElements.add(e);
	}

	public static float getVisibleRange() {
		if (WorldMap.isInSubMap) {
			return 0;
		} else {
			return visibleRange;
		}
	}

	public static void toSleepMode(boolean b) {
		if (b) {
			timeIntervalChange = TIME_INTERVAL_FOR_SEC_SLEEP;
		} else {
			timeIntervalChange = TIME_INTERVAL_FOR_SEC_NORMAL;
		}
	}

	public static void spendTime(int h, int m) {
		worldTimeM += m;
		worldTimeH += h;
		StatusManager.spendTimeUpdateStatus(h * 60 + m);
	}

	private static class WorldThread extends Thread {
		@Override
		public void run() {
			while (pRunning) {
				if (!pPause) {
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					updateWorldTime();
					updateWeather();
				}
			}
		}

		private static void updateWorldTime() {
			pTimeCounter++;
			if (pTimeCounter > timeIntervalChange) {
				pTimeCounter = 0;
				worldTimeM += TIME_INTERVAL;
				while (worldTimeM >= 60) {
					worldTimeM -= 60;
					worldTimeH++;
					if (worldTimeH >= TIME_FORMAT) {
						days++;
						worldTimeH -= TIME_FORMAT;
					}
					if (worldTimeH > HOUR_NIGHT_START || worldTimeH < HOUR_DAY_START) {
						dayNight = Const.V_NIGHT;
						visibleRange -= DARKNESS_PER_HOUR;
						if (visibleRange < VISIBLE_MIN) {
							visibleRange = VISIBLE_MIN;
						}
					} else {
						dayNight = Const.V_DAY;
						visibleRange += LIGHTNESS_PER_HOUR;
						if (visibleRange > VISIBLE_MAX) {
							visibleRange = VISIBLE_MAX;
						}
					}
				}
				updateWorldTimeText();
				updateDayTimeImg();
			}
		}

		private static void updateWorldTimeText() {
			pMsg = pHandler.obtainMessage();
			Bundle b = new Bundle();
			b.putString("time", worldTimeH + COLUMN + (worldTimeM == 0 ? SHARP : worldTimeM));
			b.putString("days", DAY + days);
			pMsg.setData(b);
			pMsg.what = Const.V_TIME;
			pHandler.sendMessage(pMsg);
		}

		private static void updateDayTimeImg() {
			pMsg = pHandler.obtainMessage();
			if (worldTimeH > HOUR_DAY_START && worldTimeH < HOUR_NIGHT_START) {
				pMsg.what = Const.V_DAY;
			} else {
				pMsg.what = Const.V_NIGHT;
			}
			pHandler.sendMessage(pMsg);
		}

		private static void updateWeather() {
			pWeatherCounter++;
			if (pWeatherCounter > pWeatherLast) {
				pWeatherCounter = 0;
				switch (weather) {
				case Const.V_FINE:
					if (Misc.bingo(3)) {
						weather = Const.V_RAIN_HARD;
						pWeatherLast = (short) (30 + Misc.roll(30));
					} else if (Misc.bingo(2)) {
						weather = Const.V_RAIN_SMALL;
						pWeatherLast = (short) (45 + Misc.roll(30));
					} else {
						weather = Const.V_FINE;
						pWeatherLast = (short) (45 + Misc.roll(30));
					}
					break;
				case Const.V_RAIN_SMALL:
					if (Misc.bingo(3)) {
						weather = Const.V_RAIN_HARD;
						pWeatherLast = (short) (30 + Misc.roll(30));
					} else {
						weather = Const.V_FINE;
						pWeatherLast = (short) (60 + Misc.roll(30));
					}
					break;
				case Const.V_RAIN_HARD:
					if (Misc.bingo(3)) {
						weather = Const.V_RAIN_SMALL;
						pWeatherLast = (short) (45 + Misc.roll(30));
					} else {
						weather = Const.V_FINE;
						pWeatherLast = (short) (60 + Misc.roll(30));
					}
					break;
				}
				if (weather == Const.V_RAIN_HARD) {
					putOutFire();
				}
				updateEffectView();
			}
		}

		private static void putOutFire() {
			for (BaseElement ee : effectElements) {
				if (ee instanceof Fire) {
					((Fire) ee).putOut();
				}
			}
		}

		private static void updateEffectView() {
			pMsg = pHandler.obtainMessage();
			pMsg.what = weather;
			pHandler.sendMessage(pMsg);
		}
	}
}
