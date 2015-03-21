package lan.survivor.element.item.tool;

import lan.survivor.R;
import lan.survivor.application.GameApplication;
import lan.survivor.element.Terrain;
import lan.survivor.utils.ID;
import lan.survivor.window.InteractionWindow;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Bow extends ToolElement {

	private final static short S_INIT = 0;
	private final static short S_JUDGE = 1;
	private final static short S_OVER = 2;

	private static Bitmap fishingImg;
	private static Rect rect;
	private static Paint p = new Paint();
	private static short state = S_INIT;
	private static short judgeTime = 1;
	private static short randomTime = 3;
	private static Handler handler = new Handler();
	private static Runnable r, r2;

	public Bow() {
		speciesID = ID.TOL_FISHING_ROD;
		if (fishingImg == null) {
			fishingImg = BitmapFactory.decodeResource(GameApplication.getAppContext().getResources(), R.drawable.fishing);
		}
		p.setColor(Color.RED);
		p.setStyle(Style.FILL);
		p.setTextSize(100);
		p.setStrokeWidth(20);
	}

	@Override
	public void onTakeAction(final Context c, final Terrain t, final AlertDialog d) {
		if (t.terrainType != Terrain.TERRAIN_SEA && t.terrainType != Terrain.TERRAIN_SEA_DEAP) {
			Toast.makeText(c, R.string.toast_no_use, Toast.LENGTH_SHORT).show();
			return;
		}
		View bodyView = View.inflate(c, R.layout.item_dialog, null);
		TextView detail = (TextView) bodyView.findViewById(R.id.item_dialog_detail);
		detail.setText(c.getString(R.string.action_fishing));
		Button okButton = (Button) bodyView.findViewById(R.id.item_dialog_btn1);
		okButton.setText("yes");
		Button diableButton = (Button) bodyView.findViewById(R.id.item_dialog_btn2);
		diableButton.setVisibility(View.INVISIBLE);
		Button noButton = (Button) bodyView.findViewById(R.id.item_dialog_btn3);
		noButton.setText("no");

		InteractionWindow.regitserTool(this);

		final AlertDialog actDialog = new AlertDialog.Builder(c).create();
		actDialog.setView(bodyView);

		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				actDialog.dismiss();
				d.dismiss();
				if (rect == null) {
					rect = new Rect(0, 0, InteractionWindow.windowWidth, InteractionWindow.windowHeight);
				}
				state = S_INIT;
				r = new Runnable() {
					@Override
					public void run() {
						r2 = new Runnable() {
							@Override
							public void run() {
								state = S_OVER;
								InteractionWindow.updateView();
							}
						};
						handler.postDelayed(r2, judgeTime * 1000);
						state = S_JUDGE;
						InteractionWindow.updateView();
					}
				};
				handler.postDelayed(r, randomTime * 1000);
				InteractionWindow.showWindow();
			}
		});

		noButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				actDialog.dismiss();
			}
		});

		actDialog.show();
	}

	@Override
	public void onAnimate(Canvas canvas) {
		switch (state) {
		case S_INIT:
			canvas.drawBitmap(fishingImg, null, rect, null);
			break;
		case S_JUDGE:
			canvas.drawBitmap(fishingImg, null, rect, null);
			canvas.drawText("!", InteractionWindow.windowWidth / 2, InteractionWindow.windowHeight / 2, p);
			break;
		case S_OVER:
			canvas.drawBitmap(fishingImg, null, rect, null);
			break;
		}
	}

	@Override
	public void recieveTouch(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			Log.v("INTERACTION", "on touch up");
			switch (state) {
			case S_INIT:
				handler.removeCallbacks(r);
				Log.v("INTERACTION", "on touch before fish");
				Toast.makeText(GameApplication.getAppContext(), "There is no any fish yet!!", Toast.LENGTH_SHORT).show();
				break;
			case S_JUDGE:
				handler.removeCallbacks(r2);
				Log.v("INTERACTION", "on touch get fish");
				Toast.makeText(GameApplication.getAppContext(), "You get one fish!!", Toast.LENGTH_SHORT).show();
				break;
			case S_OVER:
				Log.v("INTERACTION", "on touch after fish");
				Toast.makeText(GameApplication.getAppContext(), "You missed the fish!!", Toast.LENGTH_SHORT).show();
				break;
			}
			InteractionWindow.dismissWindow();
		}
	}
}
