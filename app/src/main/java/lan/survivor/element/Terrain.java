package lan.survivor.element;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import lan.survivor.element.building.BuildingElement;
import lan.survivor.element.item.ItemElement;
import lan.survivor.element.monster.MonsterElement;
import lan.survivor.element.provider.ProviderElement;
import lan.survivor.environment.Physics;
import lan.survivor.environment.World;
import lan.survivor.environment.WorldMap;
import lan.survivor.view.GameView;
import android.graphics.Canvas;
import android.util.Log;

public class Terrain implements Serializable {

	/**  */
	private static final long serialVersionUID = -3645237073632457344L;
	public static final int TERRAIN_BLACK = 0;
	public static final int TERRAIN_DIRT = 1;
	public static final int TERRAIN_GRASSLAND = 2;
	public static final int TERRAIN_ROCKS = 3;
	public static final int TERRAIN_SAND = 4;
	public static final int TERRAIN_SEA = 5;
	public static final int TERRAIN_SEA_DEAP = 6;
	public static final int TERRAIN_SNOW = 7;
	public static final int TERRAIN_GRASS = 8;
	/** ++++++++++++++++++++++++++++++++++++++++ */
	public static final int TERRAIN_ALL = 9;

	public static int tempX;
	public static int tempY;

	public short terrainType;
	public boolean passable;
	public short blockRow;
	public short blockColumn;
	public short lightened;
	public ArrayList<BaseElement> elements; // only create when needed

	// @Override
	// public void read(Kryo kryo, Input in) {
	// terrainType = in.readShort();
	// passable = in.readBoolean();
	// blockRow = in.readShort();
	// blockColumn = in.readShort();
	// lightened = in.readShort();
	// elements = (ArrayList<BaseElement>) kryo.readClassAndObject(in);
	// }
	//
	// @Override
	// public void write(Kryo kryo, Output out) {
	// out.writeShort(terrainType);
	// out.writeBoolean(passable);
	// out.writeShort(blockRow);
	// out.writeShort(blockColumn);
	// out.writeShort(lightened);
	// kryo.writeClassAndObject(out, elements);
	// }

	public Terrain(short type, short row, short column) {
		terrainType = type;
		passable = Physics.getTerrainProperty(terrainType, Physics.PASSABLE);
		blockRow = row;
		blockColumn = column;
		lightened = 0;
	}

	public void draw(Canvas c, int row, int column, int offsetX, int offsetY) {
		tempX = Math.abs(column * WorldMap.terrainBlockW + WorldMap.terrainHalfW - offsetX - GameView.viewCenterX);
		tempY = Math.abs(row * WorldMap.terrainBlockH + WorldMap.terrainHalfH - offsetY - GameView.viewCenterY);
		if (tempX * tempX + tempY * tempY < World.getVisibleRange() * World.getVisibleRange() || lightened > 0) {
			c.drawBitmap(WorldMap.terrainTexture[terrainType], column * WorldMap.terrainBlockW - offsetX, row * WorldMap.terrainBlockH - offsetY, null);
			if (elements != null) {
				for (BaseElement e : elements) {
					e.centerX = (column + 0.5f) * WorldMap.terrainBlockW - offsetX;
					e.centerY = (row + 0.5f) * WorldMap.terrainBlockH - offsetY;
					e.draw(c);
				}
			}
		} else {
			c.drawBitmap(WorldMap.terrainTexture[0], column * WorldMap.terrainBlockW - offsetX, row * WorldMap.terrainBlockH - offsetY, null);
		}
	}

	public void addElements(ArrayList<?> list) {
		for (Object e : list) {
			addElement((BaseElement) e);
		}
	}

	public void addElement(BaseElement e) {
		if (elements == null) {
			elements = new ArrayList<BaseElement>();
		}
		if (passable && (e instanceof ProviderElement || e instanceof BuildingElement)) {
			passable = false;
		}
		if (e instanceof ItemElement) {
			for (BaseElement be : elements) {
				if (be.speciesID == e.speciesID) {
					Log.v("TERRAIN", "STACK:" + e.speciesID);
					((ItemElement) be).stack(((ItemElement) e).stackCount, false);
					return;
				}
			}
		}
		// Log.v("TERRAIN", "NEW:" + e.speciesID);
		elements.add(e);
	}

	public boolean hasElements() {
		if (elements == null || elements.size() < 1) {
			return false;
		}
		return true;
	}

	public BuildingElement getBuildingElement() {
		for (BaseElement e : elements) {
			if (e instanceof BuildingElement) {
				return (BuildingElement) e;
			}
		}
		return null;
	}

	public ProviderElement getProviderElement() {
		for (BaseElement e : elements) {
			if (e instanceof ProviderElement) {
				return (ProviderElement) e;
			}
		}
		return null;
	}

	public void updateProvider(ProviderElement pe) {
		if (!pe.alive) {
			pe.afterDeath(this);
			elements.remove(pe);
		}
	}

	public void refreshItem() {
		if (hasElements()) {
			Iterator<BaseElement> i = elements.iterator();
			while (i.hasNext()) {
				BaseElement be = i.next();
				if (be instanceof ItemElement) {
					if (((ItemElement) be).stackCount < 1) {
						i.remove();
					}
				}
			}
		}
	}

	public MonsterElement getMonster() {
		for (BaseElement e : elements) {
			if (e instanceof MonsterElement) {
				return (MonsterElement) e;
			}
		}
		return null;
	}
}
