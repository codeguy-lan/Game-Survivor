package lan.survivor.window;

import lan.survivor.R;
import lan.survivor.application.GameApplication;
import lan.survivor.element.item.tool.ToolElement;
import lan.survivor.view.InteractionView;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

public class InteractionWindow {

	public static int windowWidth = 300;
	public static int windowHeight = 300;

	private static InteractionWindow instance = new InteractionWindow();
	private static Context pContext;
	private static View popupView;
	private static InteractionView iView;
	private static PopupWindow pInteractionWindow;
	private static View pParentView;

	private InteractionWindow() {
		pInteractionWindow = new PopupWindow(GameApplication.getAppContext());
	}

	public InteractionWindow getInstance() {
		return instance;
	}

	public static void setupWindow(Context context, View parent) {
		pContext = context;
		popupView = View.inflate(pContext, R.layout.interaction_view, null);
		iView = (InteractionView) popupView.findViewById(R.id.InteractionView);
		pParentView = parent;
		pInteractionWindow.setContentView(popupView);
		pInteractionWindow.setClippingEnabled(false);
		pInteractionWindow.setOutsideTouchable(false);
		// pInteractionWindow.setFocusable(true);
	}

	public static void setWindowSize(int w, int h) {
		windowWidth = w;
		windowHeight = h;
		pInteractionWindow.setWidth(windowWidth);
		pInteractionWindow.setHeight(windowHeight);
	}

	public static void regitserTool(ToolElement te) {
		iView.setTool(te);
	}

	public static void showWindow() {
		pInteractionWindow.showAtLocation((View) pParentView.getParent(), Gravity.CENTER, 0, 0);
		iView.invalidate();
	}

	public static void dismissWindow() {
		pInteractionWindow.dismiss();
	}

	public static boolean isShowing() {
		return pInteractionWindow.isShowing();
	}

	public static void updateView() {
		if (isShowing()) {
			iView.invalidate();
		}
	}
}
