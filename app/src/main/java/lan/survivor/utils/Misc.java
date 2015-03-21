package lan.survivor.utils;

import java.util.Random;

public class Misc {

	public static Random rand = new Random();

	/**
	 * return -1, 0, 1
	 * 
	 * @return int
	 */
	public static int randomize() {
		return rand.nextInt(3) - 1;
	}

	public static int rollMinus(int in) {
		return rand.nextInt(in + 1) * (rand.nextBoolean() ? 1 : -1);
	}

	public static int roll(int in) {
		return rand.nextInt(in + 1);
	}

	public static boolean bingo(int in) {
		return rand.nextInt(in + 1) == 0;
	}

	public interface CreationItemAction {

		public void onSet();

		public void onTake();
	}
}