package com.mbs.my.bat.stats;

import java.util.Calendar;

import com.mbs.my.bat.stats.R;

import android.os.Bundle;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class AddGameActivity extends Activity implements OnClickListener {

	private long playerID = 0;
	private BatStatsDBHelper db;
	private Player curPlayer;

	private int season;
	private int atbats;
	private int hits;
	private int runs;
	private int rbis;
	private int walks;
	private int sac;
	private int doub;
	private int triple;
	private int homerun;

	private TextView curTextView;
	private TextView newgameText;
	private EditText seasonText;
	private TextView atbatText;
	private TextView hitText;
	private TextView runText;
	private TextView rbiText;
	private TextView walkText;
	private TextView sacText;
	private TextView doubText;
	private TextView tripleText;
	private TextView hrText;

	// buttons
	private Button btn_AddGame;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_game);
		db = new BatStatsDBHelper(this);
		db.open();
		// set listeners
		btn_AddGame = (Button) findViewById(R.id.btn_addplayer);
		btn_AddGame.setOnClickListener(this);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			playerID = extras.getLong("PLAYER_ID");
		}
		curPlayer = db.getPlayerByID(playerID);
		// Set Defaults
		setDefaults();
		updateStats();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_game, menu);
		return true;
	}

	private void setDefaults() {

		newgameText = (TextView) findViewById(R.id.lbl_newgame);
		newgameText.append(" : " + curPlayer.getName());

		seasonText = (EditText) findViewById(R.id.txt_season);
		seasonText.setText("" + Calendar.getInstance().get(Calendar.YEAR));

		atbatText = (TextView) findViewById(R.id.txt_atbats);
		atbatText.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		atbatText.setText("0");

		hitText = (TextView) findViewById(R.id.txt_hits);
		hitText.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		hitText.setText("0");

		runText = (TextView) findViewById(R.id.txt_runs);
		runText.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		runText.setText("0");

		rbiText = (TextView) findViewById(R.id.txt_rbis);
		rbiText.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		rbiText.setText("0");

		walkText = (TextView) findViewById(R.id.txt_walk);
		walkText.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		walkText.setText("0");

		sacText = (TextView) findViewById(R.id.txt_sac);
		sacText.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		sacText.setText("0");

		doubText = (TextView) findViewById(R.id.txt_double);
		doubText.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		doubText.setText("0");

		tripleText = (TextView) findViewById(R.id.txt_triple);
		tripleText.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		tripleText.setText("0");

		hrText = (TextView) findViewById(R.id.txt_hr);
		hrText.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		hrText.setText("0");
	}

	private boolean updateStats() {
		try {

			season = Integer.parseInt(seasonText.getText().toString());
			atbats = Integer.parseInt(atbatText.getText().toString());
			hits = Integer.parseInt(hitText.getText().toString());
			runs = Integer.parseInt(runText.getText().toString());
			rbis = Integer.parseInt(rbiText.getText().toString());
			walks = Integer.parseInt(walkText.getText().toString());
			sac = Integer.parseInt(sacText.getText().toString());
			doub = Integer.parseInt(doubText.getText().toString());
			triple = Integer.parseInt(tripleText.getText().toString());
			homerun = Integer.parseInt(hrText.getText().toString());
			if (validateStats()) {
				return true;
			} else {
				return false;
			}

		} catch (NumberFormatException e) {
			Toast.makeText(getApplicationContext(),
					"All Stat Fields Must be a Number", Toast.LENGTH_LONG)
					.show();
			return false;
		}

	}

	private boolean validateStats() {
		if (hits > atbats) {
			Toast.makeText(getApplicationContext(),
					"Hits cannot be greater than At Bats", Toast.LENGTH_LONG)
					.show();
			return false;
		}
		if ((doub > atbats) || (doub > hits)) {
			Toast.makeText(getApplicationContext(),
					"Double cannot be greater Than At Bats/Hits",
					Toast.LENGTH_LONG).show();
			return false;
		}
		if ((triple > atbats) || (triple > hits)) {
			Toast.makeText(getApplicationContext(),
					"Triple cannot be greater Than At Bats/Hits",
					Toast.LENGTH_LONG).show();
			return false;
		}
		if ((homerun > atbats) || (homerun > hits)) {
			Toast.makeText(getApplicationContext(),
					"Homerun cannot be greater Than At Bats/Hits",
					Toast.LENGTH_LONG).show();
			return false;
		}
		if (((doub + triple + homerun) > atbats)
				|| ((doub + triple + homerun) > hits)) {
			Toast.makeText(
					getApplicationContext(),
					"Double+Triple+Homerun cannot be greater Than At Bats/Hits",
					Toast.LENGTH_LONG).show();
			return false;
		}
		return true;

	}

	public boolean addGame() {

		if (updateStats()) {

			try{
				PlayerStats currentStats = db.getPlayerStatsbyPlayerIDandSeason(
						playerID, season);
				
				if (currentStats != null) {
					db.updatePlayerStats(playerID, season,
							atbats + currentStats.getAtBat(),
							hits + currentStats.getHit(),
							runs + currentStats.getRun(),
							rbis + currentStats.getRBI(),
							walks + currentStats.getWalk(),
							sac + currentStats.getWalk(),
							doub + currentStats.getDouble(),
							triple + currentStats.getTriple(), homerun
									+ currentStats.getHomerun());
				}
			}catch(CursorIndexOutOfBoundsException e1){
				db.insertPlayerStats(playerID, season, atbats, hits, runs,
						rbis, walks, sac, doub, triple, homerun);
			}
			

			db.close();
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View arg0) {
		// If add button was clicked
		if (btn_AddGame.isPressed()) {

			if (addGame()) {
				Intent intent = new Intent(getBaseContext(),
						PlayerHomeActivity.class);
				intent.putExtra("PLAYER_ID", playerID);
				intent.putExtra("SEASON", (long)season);
				startActivity(intent);
				this.finish();
			}

		}

	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, PlayerHomeActivity.class);
		intent.putExtra("PLAYER_ID", playerID);
		startActivity(intent);
		this.finish();
	}

	public void show(View v) {

		// Determine which TextView was clicked.
		String screenTitle = "";

		if (v.getId() == R.id.txt_atbats) {
		
			curTextView = atbatText;
			screenTitle = "At Bats";
		}
		else if(v.getId() == R.id.txt_hits){
			curTextView = hitText;
			screenTitle = "Hits";
		}
		else if(v.getId() == R.id.txt_runs){
			curTextView = runText;
			screenTitle = "Runs";
		}
			
		else if(v.getId() == R.id.txt_rbis){
			curTextView = rbiText;
			screenTitle = "RBI";
		}
		else if(v.getId() == R.id.txt_walk){
			curTextView = walkText;
			screenTitle = "Walks";
		}
		else if(v.getId() == R.id.txt_sac){
			curTextView = sacText;
			screenTitle = "Sacrifice Hits";
		}
		else if(v.getId() == R.id.txt_double){
			curTextView = doubText;
			screenTitle = "Doubles";
		}
		else if(v.getId() == R.id.txt_triple){
			curTextView = tripleText;
			screenTitle = "Triples";
		}
		else if(v.getId() == R.id.txt_hr){
			curTextView = hrText;
			screenTitle = "Homeruns";
		}

		final Dialog d = new Dialog(AddGameActivity.this);
		d.setTitle(screenTitle);
		d.setContentView(R.layout.number_picker_dialog);
		Button b1 = (Button) d.findViewById(R.id.button1);
		Button b2 = (Button) d.findViewById(R.id.button2);
		final NumberPicker np = (NumberPicker) d
				.findViewById(R.id.numberPicker1);
		np.setMaxValue(10);
		np.setMinValue(0);
		np.setWrapSelectorWheel(false);
		b1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				curTextView.setText(String.valueOf(np.getValue())); // set the
																	// value to
																	// textview
				d.dismiss();
			}
		});
		b2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				d.dismiss(); // dismiss the dialog
			}
		});
		d.show();

	}

}
