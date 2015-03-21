package lan.survivor.element.item.tool;

import lan.survivor.element.Terrain;
import lan.survivor.element.item.ItemElement;
import lan.survivor.utils.Const;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

public abstract class ToolElement extends ItemElement {

	public static int type = Const.TYPE_TOOL;
	public static int maxStack = 1;

	public abstract void onTakeAction(Context c, Terrain t, AlertDialog d);

	public abstract void onAnimate(Canvas canvas);

	public abstract void recieveTouch(MotionEvent event);

	@Override
	public int getType() {
		return type;
	}

	@Override
	public int getMaxStack() {
		return maxStack;
	}
}
