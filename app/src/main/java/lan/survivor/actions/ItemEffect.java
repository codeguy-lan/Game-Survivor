package lan.survivor.actions;

public class ItemEffect {

	public int type;
	public int amount;
	public int time;

	public ItemEffect(int tp, int amt, int tm) {
		type = tp;
		amount = amt;
		time = tm;
	}
}