package lan.survivor.view;

import lan.survivor.actions.StatusManager;
import lan.survivor.element.Survivor;
import lan.survivor.environment.SubWorldMap;
import lan.survivor.environment.WorldMap;
import lan.survivor.utils.Const;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;

public class RenderThread extends Thread {

	private SurfaceHolder pSurfaceHolder;
	private Handler pHandler;

	/** Current width and height of the surface/canvas. */
	private int pViewWidth = 1;
	private int pViewHeight = 1;

	/** Indicate whether the surface has been created & is ready to draw */
	private boolean pRun = false;
	private boolean pPause = false;

	/** Used to figure out elapsed time between frames */
	private long pLastTime;

	WorldMap worldMap;
	SubWorldMap subWorldMap;
	Survivor survivor;

	public RenderThread(SurfaceHolder surfaceHolder, Context context, Handler handler) {
		pSurfaceHolder = surfaceHolder;
		pHandler = handler;
	}

	@Override
	public void run() {
		while (pRun) {
			if (!pPause) {
				Canvas c = null;
				try {
					c = pSurfaceHolder.lockCanvas(null);
					if (c != null) {
						synchronized (pSurfaceHolder) {
							updatePhysics();
							doDraw(c);
						}
					}
				} finally {
					// do this in a finally so that if an exception is thrown
					// during the above, we don't leave the Surface in an
					// inconsistent state
					if (c != null) {
						pSurfaceHolder.unlockCanvasAndPost(c);
					}
				}
			}
		}
	}

	public void setPause(boolean b) {
		synchronized (pSurfaceHolder) {
			if (pRun) {
				pPause = b;
			}
		}
	}

	public void setRunning(boolean b) {
		pRun = b;
	}

	/* Callback invoked when the surface dimensions change. */
	public void setSurfaceSize(int width, int height, WorldMap wm, SubWorldMap swm, Survivor s) {
		// synchronized to make sure these all change atomically
		synchronized (pSurfaceHolder) {
			pViewWidth = width;
			pViewHeight = height;
			worldMap = wm;
			subWorldMap = swm;
			survivor = s;
			worldMap.updateMap(0, survivor);
			subWorldMap.updateMap(0, survivor);
		}
	}

	/**
     */
	private void doDraw(Canvas canvas) {
		if (WorldMap.isInSubMap) {
			subWorldMap.drawMap(canvas);
		} else {
			worldMap.drawMap(canvas);
		}
		survivor.draw(canvas);
	}

	/**
     */
	private void updatePhysics() {
		long now = System.currentTimeMillis();
		double elapsed = (now - pLastTime) / 1000.0;
		if (WorldMap.scrollDirection != -1) {
			if (WorldMap.isInSubMap) {
				subWorldMap.updateMap((float) (StatusManager.equipStatus[StatusManager.SPEED] * elapsed), survivor);
			} else {
				worldMap.updateMap((float) (StatusManager.equipStatus[StatusManager.SPEED] * elapsed), survivor);
			}
		}
		pLastTime = now;
	}

	/**
	 * @param event
	 * @return
	 */
	// public boolean onTouchEvent(MotionEvent event) {
	//
	// float touchedX, touchedY;
	// int action = event.getAction();
	// touchedX = event.getX();
	// touchedY = event.getY();
	// switch (action) {
	// case MotionEvent.ACTION_DOWN:
	//
	// break;
	// case MotionEvent.ACTION_MOVE:
	//
	// break;
	// case MotionEvent.ACTION_UP:
	//
	// break;
	// default:
	// break;
	// }
	// return true;
	// }

	public void moveCharacter(float x, float y, int width, int height) {
		double degree = Math.toDegrees(Math.atan2(height - y, x - width));
		if (degree > -45 && degree < 45) {
			// right
			Log.v("MOVE", "right");
			WorldMap.scrollDirection = Const.DIRECTION_R;
			SubWorldMap.scrollDirection = Const.DIRECTION_R;
			survivor.facing = Const.DIRECTION_R;
		} else if (degree > 45 && degree < 135) {
			// up
			Log.v("MOVE", "up");
			WorldMap.scrollDirection = Const.DIRECTION_U;
			SubWorldMap.scrollDirection = Const.DIRECTION_U;
			survivor.facing = Const.DIRECTION_U;
		} else if (degree > 135 || degree < -135) {
			// left
			Log.v("MOVE", "left");
			WorldMap.scrollDirection = Const.DIRECTION_L;
			SubWorldMap.scrollDirection = Const.DIRECTION_L;
			survivor.facing = Const.DIRECTION_L;
		} else if (degree > -135 && degree < -45) {
			// down
			Log.v("MOVE", "down");
			WorldMap.scrollDirection = Const.DIRECTION_D;
			SubWorldMap.scrollDirection = Const.DIRECTION_D;
			survivor.facing = Const.DIRECTION_D;
		}
		survivor.isMoving = true;
	}

	public void stopCharacter() {
		WorldMap.scrollDirection = -1;
		SubWorldMap.scrollDirection = -1;
		survivor.isMoving = false;
	}

}
