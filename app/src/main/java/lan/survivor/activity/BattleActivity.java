package lan.survivor.activity;

import java.util.Random;
import lan.survivor.R;
import lan.survivor.actions.MonsterAction;
import lan.survivor.actions.StatusManager;
import lan.survivor.element.Survivor;
import lan.survivor.element.monster.MonsterElement;
import lan.survivor.environment.MonsterMaker;
import lan.survivor.utils.RenderResPool;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BattleActivity extends Activity {

	ImageView imgSelf, imgMonster;
	TextView txtSelfHP, txtMonsterHP, txtBattleStatus;
	Button btnAttack, btnItem, btnEnd;
	String battleText;

	int monsterID;
	MonsterElement monster;
	Random rand;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.battle);

		Intent intent = getIntent();
		monsterID = intent.getIntExtra("monsterID", -1);
		Log.v("BATTLE", "" + monsterID);
		monster = MonsterMaker.getMonster(monsterID);

		imgSelf = (ImageView) findViewById(R.id.battle_imgSelf);
		imgMonster = (ImageView) findViewById(R.id.battle_imgMonster);
		txtSelfHP = (TextView) findViewById(R.id.battle_txtSelfHP);
		txtMonsterHP = (TextView) findViewById(R.id.battle_txtMonsterHP);
		txtBattleStatus = (TextView) findViewById(R.id.battle_txtStatus);
		btnAttack = (Button) findViewById(R.id.battle_btnAttack);
		btnItem = (Button) findViewById(R.id.battle_btnItem);
		btnEnd = (Button) findViewById(R.id.battle_btnEscape);

		imgSelf.setImageBitmap(Survivor.getBattleImg());
		imgMonster.setImageBitmap(RenderResPool.getImg(monster.speciesID));
		txtSelfHP.setText("HP:" + String.valueOf((int) StatusManager.status[StatusManager.HP]));
		txtMonsterHP.setText("HP:" + String.valueOf(monster.hp));
		battleText = "battle start!\n";
		txtBattleStatus.setText(battleText);

		btnAttack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				attack();
			}
		});

		btnEnd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	public void attack() {
		setButtonClickable(false);
		monster.hp -= StatusManager.equipStatus[StatusManager.ATTACK];
		battleText += "you did " + StatusManager.equipStatus[StatusManager.ATTACK] + " damage to the target";
		txtBattleStatus.setText(battleText);
		synchronized (this) {
			try {
				wait(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (monster.hp < 1) {
			monster.hp = 0;
			Toast.makeText(this, "you win the battle!", Toast.LENGTH_SHORT).show();
			MonsterMaker.monsterDefeat(monster);
			finish();
		} else {
			monsterAction();
		}

	}

	public void monsterAction() {
		if (monster.atk == 0) {
			escape();
		} else {
			MonsterAction.monsterAct(monster);
		}
		setButtonClickable(true);
	}

	public void escape() {
		rand = new Random();
		if (rand.nextInt(4) == 0) {
			Toast.makeText(this, "the monster has escaped!", Toast.LENGTH_SHORT).show();
			MonsterMaker.removeMonster(monster);
			finish();
		}
	}

	public void setButtonEnable(boolean b) {
		btnAttack.setEnabled(b);
		btnItem.setEnabled(b);
		btnEnd.setEnabled(b);
	}

	private void setButtonClickable(boolean b) {
		btnAttack.setClickable(b);
		btnItem.setClickable(b);
		btnEnd.setClickable(b);
	}
}
