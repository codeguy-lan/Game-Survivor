package lan.survivor.view;

import lan.survivor.R;
import lan.survivor.actions.EquipManager;
import lan.survivor.actions.EventManager;
import lan.survivor.actions.InventoryManager;
import lan.survivor.actions.StatusManager;
import lan.survivor.activity.MainActivity;
import lan.survivor.element.Survivor;
import lan.survivor.environment.MonsterMaker;
import lan.survivor.environment.SubWorldMap;
import lan.survivor.environment.World;
import lan.survivor.environment.WorldMap;
import lan.survivor.utils.Const;
import lan.survivor.utils.GameData;
import lan.survivor.utils.GameDataManager;
import lan.survivor.utils.RenderResPool;
import lan.survivor.window.ActionWindow;
import lan.survivor.window.CreationWindow;
import lan.survivor.window.EquipWindow;
import lan.survivor.window.InteractionWindow;
import lan.survivor.window.InventoryWindow;
import lan.survivor.window.PickWindow;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

	public static boolean inPause;
	public static boolean inBattle;
	public static WorldMap worldMap;
	public static SubWorldMap subWorldMap;
	public static Survivor survivor;
	public static int viewCenterX;
	public static int viewCenterY;

	public static RenderThread renderThread;
	public static EffectView effectView;

	public boolean isContinue;
	public GameData gData;

	InventoryWindow inventoryWindow;
	CreationWindow creationWindow;
	ActionWindow actionWindow;
	EquipWindow equipWindow;
	PickWindow pickWindow;

	private Context pContext;
	private int pMoveButtonHalfWidth;
	private int pMoveButtonHalfHeight;
	private static ImageView pImgDay;
	private static Bitmap imgDay, imgNight;
	private static TextView pTextDays, pTextWorldTime, pTextHealth, pTextStamina, pTextFood, pTextWater;
	private static Button pButtonMove, pButtonBag, pButtonPick, pButtonAction, pButtonCreate, pButtonEquip, pButtonSleep;

	private static Handler statusInfoHandler = new Handler() {
		@Override
		public void handleMessage(Message m) {
			switch (m.what) {
			case Const.V_STATUS:
				pTextHealth.setText(m.getData().getString("health"));
				pTextStamina.setText(m.getData().getString("stamina"));
				pTextFood.setText(m.getData().getString("food"));
				pTextWater.setText(m.getData().getString("water"));
				break;
			case Const.V_TIME:
				pTextWorldTime.setText(m.getData().getString("time"));
				pTextDays.setText(m.getData().getString("days"));
				break;
			case Const.V_DAY:
				pImgDay.setImageBitmap(imgDay);
				break;
			case Const.V_NIGHT:
				pImgDay.setImageBitmap(imgNight);
				break;
			case Const.V_FINE:
				effectView.setFine();
				break;
			case Const.V_RAIN_SMALL:
				effectView.setRain(Const.V_RAIN_SMALL);
				break;
			case Const.V_RAIN_HARD:
				effectView.setRain(Const.V_RAIN_HARD);
				break;
			case Const.V_SLEEP_BUTTON:
				changeSleepButtonText(true);
				break;
			case Const.V_WAKE_BUTTON:
				changeSleepButtonText(false);
				break;
			}
		}
	};

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		pContext = context;
		inPause = false;
		inBattle = false;
		// create thread only; it's started in surfaceCreated()
		renderThread = new RenderThread(holder, context, statusInfoHandler);
		StatusManager.registHandler(statusInfoHandler);
		RenderResPool.initialLoad(context);
		imgDay = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(pContext.getResources(), R.drawable.day), 20, 20, true);
		imgNight = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(pContext.getResources(), R.drawable.night), 20, 20, true);
		// setFocusable(true); // make sure we get key events

	}

	public void setContinue(boolean b) {
		isContinue = b;
		gData = null;
		if (isContinue) {
			gData = GameDataManager.loadData(pContext);
			StatusManager.status = gData.status;
			StatusManager.equipStatus = gData.equipStatus;
			StatusManager.STATUS_MAX = gData.STATUS_MAX;
			StatusManager.condition = gData.condition;
			StatusManager.inShelter = gData.inShelter;
			StatusManager.inSleep = gData.inSleep;
			EquipManager.equips = gData.equips;
			InventoryManager.inventory = gData.inventory;
			InventoryManager.bagCapacity = gData.bagCapacity;
		}
	}

	public void setEffectView(EffectView ev) {
		effectView = ev;
	}

	public void setWorldInfo(TextView days, ImageView day, TextView worldTime) {
		pTextDays = days;
		pImgDay = day;
		pTextWorldTime = worldTime;
		pTextDays.setText("DAY:1");
		pImgDay.setImageBitmap(imgDay);
		pTextWorldTime.setText("8:00");
	}

	public void setStatusText(TextView hp, TextView sp, TextView f, TextView w) {
		pTextHealth = hp;
		pTextStamina = sp;
		pTextFood = f;
		pTextWater = w;
		pTextHealth.setText("100");
		pTextStamina.setText("100");
		pTextFood.setText("100");
		pTextWater.setText("100");
	}

	public void setButton(Button move, Button bag, Button pick, Button action, Button create, Button equip, Button sleep) {
		pButtonMove = move;
		pButtonBag = bag;
		pButtonPick = pick;
		pButtonAction = action;
		pButtonCreate = create;
		pButtonEquip = equip;
		pButtonSleep = sleep;
	}

	public RenderThread getThread() {
		return renderThread;
	}

	// @Override
	// public boolean onTouchEvent(MotionEvent event) {
	// return renderThread.onTouchEvent(event);
	// }

	/**
	 * Standard window-focus override. Notice focus lost so we can pause on focus lost. e.g. user switches to take a call.
	 */
	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		pMoveButtonHalfWidth = pButtonMove.getWidth() / 2;
		pMoveButtonHalfHeight = pButtonMove.getHeight() / 2;
		if (!hasWindowFocus && renderThread != null) {
			if (inBattle) {
				StatusManager.setPause(true);
				World.setPause(true);
				MonsterMaker.setPause(true);
				renderThread.setPause(true);
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		if (!inBattle) {
			viewCenterX = width / 2;
			viewCenterY = height / 2;
			worldMap = new WorldMap(width, height, gData);
			subWorldMap = new SubWorldMap(width, height);
			survivor = new Survivor();
			World.init(statusInfoHandler, gData, viewCenterX, viewCenterY);
			renderThread.setSurfaceSize(width, height, worldMap, subWorldMap, survivor);
			effectView.setViewSize(width, height);
			InteractionWindow.setWindowSize(viewCenterX, viewCenterY);
			setupButton();
			World.start();
			MonsterMaker.start();
			StatusManager.start();
			renderThread.start();
			gData = null;
		} else {
			inBattle = false;
			StatusManager.setPause(false);
			World.setPause(false);
			MonsterMaker.setPause(false);
			renderThread.setPause(false);
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// start the thread here so that we don't busy-wait in run()
		// waiting for the surface to be created
		renderThread.setRunning(true);
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// we have to tell thread to shut down & wait for it to finish, or else
		// it might touch the Surface after we return.
		if (!inBattle) {
			boolean retry = true;
			renderThread.setRunning(false);
			while (retry) {
				try {
					renderThread.join();
					retry = false;
				} catch (InterruptedException e) {
				}
			}
		}
	}

	public void setupButton() {
		pButtonMove.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
				}
				if (StatusManager.inSleep) {
					return true;
				}
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					renderThread.moveCharacter(event.getX(), event.getY(), pMoveButtonHalfWidth, pMoveButtonHalfHeight);
					return true;
				}
				case MotionEvent.ACTION_MOVE: {
					renderThread.moveCharacter(event.getX(), event.getY(), pMoveButtonHalfWidth, pMoveButtonHalfHeight);
					return true;
				}
				case MotionEvent.ACTION_UP: {
					renderThread.stopCharacter();
					return true;
				}
				default:
					return true;
				}
			}
		});

		pButtonBag.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (StatusManager.inSleep) {
					return;
				}
				InventoryManager.printInventoryList();
				if (inventoryWindow.isShowing()) {
					inventoryWindow.dismissPulldown();
				} else {
					inventoryWindow.showPulldown();
				}
			}
		});

		pButtonPick.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (StatusManager.inSleep) {
					return;
				}
				pickWindow.showWindow(survivor.currentTerrainBlock());
				// EventManager.pickup(survivor.currentTerrainBlock());
			}
		});

		pButtonAction.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (StatusManager.inSleep) {
					return;
				}
				// EventManager.action(survivor.facingTerrainBlock());
				actionWindow.action(survivor.facingTerrainBlock());
			}
		});

		pButtonCreate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (StatusManager.inSleep) {
					return;
				}
				creationWindow.showWindow();
			}
		});

		pButtonEquip.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (StatusManager.inSleep) {
					return;
				}
				equipWindow.showWindow();
			}
		});

		pButtonSleep.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (StatusManager.inSleep) {
					pButtonSleep.setText("Sleep");
					World.toSleepMode(false);
					EventManager.wakeUp();
				} else {
					pButtonSleep.setText("WakeUp");
					EventManager.sleep();
				}
			}
		});
	}

	public void setupDialogView(MainActivity activity) {
		inventoryWindow = new InventoryWindow(activity, pButtonBag);
		creationWindow = new CreationWindow(activity);
		actionWindow = new ActionWindow(activity);
		equipWindow = new EquipWindow(activity);
		pickWindow = new PickWindow(activity);
		InteractionWindow.setupWindow(activity, this);
	}

	public static void changeSleepButtonText(boolean sleep) {
		if (sleep) {
			pButtonSleep.setText("WakeUp");
		} else {
			pButtonSleep.setText("Sleep");
		}
	}
}
