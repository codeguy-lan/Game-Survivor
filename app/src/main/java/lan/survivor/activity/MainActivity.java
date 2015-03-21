package lan.survivor.activity;

import lan.survivor.R;
import lan.survivor.actions.StatusManager;
import lan.survivor.environment.MonsterMaker;
import lan.survivor.environment.World;
import lan.survivor.utils.GameDataManager;
import lan.survivor.view.EffectView;
import lan.survivor.view.GameView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class MainActivity extends Activity {

	public boolean isContinue;

	Button moveButton, bagButton, pickButton, actionButton, createButton, equipButton, sleepButton;
	TextView stamina, fatigue, food, water, worldTime, days;
	ImageView day;
	ImageButton sideMenuMinButton, sideMenuMaxButton;
	ViewFlipper sideMenu;
	Animation animFlipIn, animFlipOut;

	GameView gameView;
	EffectView effectView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Intent intent = getIntent();
		isContinue = intent.getBooleanExtra("continue", false);

		gameView = (GameView) findViewById(R.id.renderer);
		gameView.setContinue(isContinue);
		effectView = (EffectView) findViewById(R.id.effectView);
		gameView.setEffectView(effectView);
		day = (ImageView) findViewById(R.id.img_day);
		worldTime = (TextView) findViewById(R.id.text_worldtime);
		days = (TextView) findViewById(R.id.text_days);
		gameView.setWorldInfo(days, day, worldTime);

		stamina = (TextView) findViewById(R.id.text_stamina);
		fatigue = (TextView) findViewById(R.id.text_fatigue);
		food = (TextView) findViewById(R.id.text_food);
		water = (TextView) findViewById(R.id.text_water);
		gameView.setStatusText(stamina, fatigue, food, water);

		moveButton = (Button) findViewById(R.id.btnMove);
		bagButton = (Button) findViewById(R.id.btnBag);
		pickButton = (Button) findViewById(R.id.btnPick);
		actionButton = (Button) findViewById(R.id.btnAction);
		createButton = (Button) findViewById(R.id.btnCreate);
		equipButton = (Button) findViewById(R.id.btnEquip);
		sleepButton = (Button) findViewById(R.id.btnSleep);
		gameView.setButton(moveButton, bagButton, pickButton, actionButton, createButton, equipButton, sleepButton);

		sideMenu = (ViewFlipper) findViewById(R.id.main_sideMenu);
		animFlipIn = AnimationUtils.loadAnimation(this, R.anim.flip_in);
		animFlipOut = AnimationUtils.loadAnimation(this, R.anim.flip_out);
		sideMenuMinButton = (ImageButton) findViewById(R.id.main_btnSide_min);
		sideMenuMaxButton = (ImageButton) findViewById(R.id.main_btnSide_max);

		sideMenuMinButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sideMenu.setInAnimation(animFlipIn);
				sideMenu.showNext();
			}
		});

		sideMenuMaxButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sideMenu.setOutAnimation(animFlipOut);
				sideMenu.showPrevious();
			}
		});

		gameView.setupDialogView(this);

	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		StatusManager.end();
		World.end();
		MonsterMaker.end();
		// GameView.renderThread.setRunning(false);
	}

	@Override
	public void onBackPressed() {
		Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Save game and exit?");
		builder.setCancelable(false);
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				StatusManager.setPause(true);
				World.setPause(true);
				MonsterMaker.setPause(true);
				GameView.renderThread.setPause(true);
				GameDataManager.saveGameData(MainActivity.this);
				finish();
			}
		});

		builder.setNeutralButton("Exit without save.", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});

		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, 1, Menu.NONE, getApplicationContext().getString(R.string.optionMenuFinish));
		menu.add(Menu.NONE, 2, Menu.NONE, getApplicationContext().getString(R.string.optionMenuPause));
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// End
		case 1:
			finish();
			break;
		// Pause
		case 2:
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
