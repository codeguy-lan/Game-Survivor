package lan.survivor.view;

import java.util.Random;
import lan.survivor.utils.Const;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class EffectView extends View {

	private int viewW, viewH;
	private int centerX, centerY;
	private short type = Const.V_FINE;
	private Paint p = new Paint();
	private Random rand = new Random();
	private double sin, cos, angle;
	private int rStart, rEnd;

	public EffectView(Context context, AttributeSet attrs) {
		super(context, attrs);
		p.setColor(Color.LTGRAY);
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (type != Const.V_FINE) {
			if (type == Const.V_RAIN_HARD) {
				for (int i = 0; i < 7; i++) {
					rStart = rand.nextInt(300) + 50;
					angle = Math.toRadians(rand.nextInt(360));
					sin = Math.sin(angle);
					cos = Math.cos(angle);
					rEnd = rand.nextInt(100);
					canvas.drawLine((float) (centerX + rStart * cos), (float) (centerY + rStart * sin), (float) (centerX + (rStart + rEnd) * cos), (float) (centerY + (rStart + rEnd) * sin), p);
				}
			}
			rStart = rand.nextInt(300) + 50;
			angle = Math.toRadians(rand.nextInt(360));
			sin = Math.sin(angle);
			cos = Math.cos(angle);
			rEnd = rand.nextInt(100);
			canvas.drawLine((float) (centerX + rStart * cos), (float) (centerY + rStart * sin), (float) (centerX + (rStart + rEnd) * cos), (float) (centerY + (rStart + rEnd) * sin), p);
			synchronized (this) {
				try {
					wait(40);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			invalidate();
		}
	}

	public void setViewSize(int width, int height) {
		viewW = width;
		viewH = height;
		centerX = viewW / 2;
		centerY = viewH / 2;
	}

	public void setFine() {
		type = Const.V_FINE;
		invalidate();
	}

	public void setRain(short degree) {
		type = degree;
		invalidate();
	}

}
