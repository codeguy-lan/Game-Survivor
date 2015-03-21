package lan.survivor.utils;

import java.io.Serializable;
import java.util.ArrayList;
import lan.survivor.element.Terrain;
import lan.survivor.element.item.ItemElement;
import lan.survivor.element.item.equip.EquipElement;

public class GameData implements Serializable {

	/**  */
	private static final long serialVersionUID = 2004506373051809656L;

	/** WorldMap */
	public Terrain[][] map;
	public float mapCurrentX, mapCurrentY;
	public Terrain[][] subMap;
	public float subMapCurrentX, subMapCurrentY;
	/** World */
	public int days;
	public short worldTimeH;
	public short worldTimeM;
	public short timeIntervalChange;
	public short dayNight;
	public short weather;
	public float visibleRange;
	/** Survivor */
	public float charaCurrentX, charaCurrentY;
	public int facing;
	/** Inventory */
	public ArrayList<ItemElement> inventory;
	public int bagCapacity;
	/** Equipment */
	public EquipElement[] equips;
	/** Status */
	public int[] equipStatus;
	public float[] status;
	public int STATUS_MAX;
	public int condition;
	public boolean inSleep;
	public boolean inShelter;

}
