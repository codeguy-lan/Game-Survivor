package lan.survivor.view;

import lan.survivor.element.item.tool.ToolElement;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class InteractionView extends View {

	private static ToolElement te;

	public InteractionView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		te.onAnimate(canvas);
	}

	public void setTool(ToolElement tool) {
		te = tool;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		te.recieveTouch(event);
		return true;
	}

}
