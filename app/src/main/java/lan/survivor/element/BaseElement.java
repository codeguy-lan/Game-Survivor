package lan.survivor.element;

import java.io.Serializable;
import lan.survivor.utils.ID;
import lan.survivor.utils.RefTable;
import android.graphics.Canvas;

public abstract class BaseElement implements Serializable {

	private static final long serialVersionUID = 7533207851995238256L;

	public float centerX = 0;
	public float centerY = 0;
	public int speciesID = -1;

	abstract public void draw(Canvas c);

	public static BaseElement[] createElements(int total, int[] ids) {
		BaseElement[] result = new BaseElement[total];
		for (short i = 0; i < total; i++) {
			result[i] = createElement(ids[i]);
		}
		return result;
	}

	public static BaseElement createElement(int id) {
		if (id == -1) {
			return null;
		}
		String sub = "";
		int type = 0;
		if (id >= ID.BLD_SECTION && id < ID.MTR_SECTION) {
			sub = "building.";
			type = 1;
		} else if (id >= ID.MTR_SECTION && id < ID.EDB_SECTION) {
			sub = "item.material.MaterialElement";
		} else if (id >= ID.EDB_SECTION && id < ID.TOL_SECTION) {
			sub = "item.edible.EdibleElement";
		} else if (id >= ID.TOL_SECTION && id < ID.EQP_SECTION) {
			sub = "item.tool.";
			type = 1;
		} else if (id >= ID.EQP_SECTION && id < ID.MST_SECTION) {
			sub = "item.equip.EquipElement";
		} else {
			sub = "monster.";
		}
		try {
			if (type == 0) {
				return (BaseElement) Class.forName("lan.survivor.element." + sub).getConstructor(int.class).newInstance(id);
			} else {
				return (BaseElement) Class.forName("lan.survivor.element." + sub + RefTable.getItemClassName(id)).newInstance();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void afterSet(Terrain t) {
		// leave this for the extended class
	}
}
