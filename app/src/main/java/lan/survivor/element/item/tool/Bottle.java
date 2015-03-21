package lan.survivor.element.item.tool;

import lan.survivor.R;
import lan.survivor.actions.ItemEffect;
import lan.survivor.actions.StatusManager;
import lan.survivor.element.Terrain;
import lan.survivor.utils.ID;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Bottle extends ToolElement {

	public static int MAX_AMOUNT = 300;
	public int fluidAmount;

	public Bottle() {
		this(ID.TOL_BOTTLE_EMPTY, 0);
	}

	public Bottle(int id, int amount) {
		fluidAmount = amount;
		speciesID = id;
	}

	@Override
	public void onTakeAction(final Context c, final Terrain t, final AlertDialog d) {
		if (t.terrainType != Terrain.TERRAIN_SEA) {
			Toast.makeText(c, R.string.toast_no_use, Toast.LENGTH_SHORT).show();
			return;
		}
		View bodyView = View.inflate(c, R.layout.item_dialog, null);
		TextView detail = (TextView) bodyView.findViewById(R.id.item_dialog_detail);
		detail.setText(c.getString(R.string.action_take_water));
		Button okButton = (Button) bodyView.findViewById(R.id.item_dialog_btn1);
		okButton.setText("yes");
		Button diableButton = (Button) bodyView.findViewById(R.id.item_dialog_btn2);
		diableButton.setVisibility(View.INVISIBLE);
		Button noButton = (Button) bodyView.findViewById(R.id.item_dialog_btn3);
		noButton.setText("no");

		final AlertDialog actDialog = new AlertDialog.Builder(c).create();
		actDialog.setView(bodyView);

		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				fill(t.terrainType, MAX_AMOUNT);
				Toast.makeText(c, R.string.toast_fill_water, Toast.LENGTH_SHORT).show();
				actDialog.dismiss();
				d.dismiss();
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

	public void fill(int type, int amount) {
		fluidAmount += amount;
		if (fluidAmount > MAX_AMOUNT) {
			fluidAmount = MAX_AMOUNT;
		}
		if (type == Terrain.TERRAIN_SEA) {
			speciesID = ID.TOL_BOTTLE_SEA_FILL;
		} else {
			speciesID = ID.TOL_BOTTLE_FILL;
		}
	}

	public void drink() {
		int amount = StatusManager.STATUS_MAX - (int) StatusManager.status[StatusManager.WATER];
		if (fluidAmount > amount) {
			StatusManager.addStatusEffect(new ItemEffect(StatusManager.WATER, amount, 0));
			fluidAmount -= amount;
		} else {
			StatusManager.addStatusEffect(new ItemEffect(StatusManager.WATER, fluidAmount, 0));
			fluidAmount = 0;
			speciesID = ID.TOL_BOTTLE_EMPTY;
		}
	}

	public Bottle getNewCopy() {
		Bottle newInstance = new Bottle(speciesID, fluidAmount);
		return newInstance;
	}

	@Override
	public void onAnimate(Canvas canvas) {
	}

	@Override
	public void recieveTouch(MotionEvent event) {
	}
}
