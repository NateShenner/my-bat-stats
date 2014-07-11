package com.mbs.my.bat.stats;

import java.util.Calendar;

import com.mbs.my.bat.stats.R;

import android.os.Bundle;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class AddGameActivity extends Activity implements OnClickListener {

	private long playerID = 0;
	private BatStatsDBHelper db;
	private Player curPlayer;

	private String seasonAsString;
	private int season;
	private int pa;
	private int hits;
	private int runs;
	private int rbis;
	private int ks;
	private int walks;
	private int sac;
	private int hbp;
	private int doub;
	private int triple;
	private int homerun;

	private TextView curTextView;
	private TextView newgameText;
	private EditText seasonText;
	private TextView paText;
	private TextView hitText;
	private TextView runText;
	private TextView rbiText;
	private TextView kText;
	private TextView walkText;
	private TextView sacText;
	private TextView hbpText;
	private TextView doubText;
	private TextView tripleText;
	private TextView hrText;

	// buttons
	private ImageButton btn_PA_Plus;
	private ImageButton btn_PA_Minus;
	private ImageButton btn_Hits_Plus;
	private ImageButton btn_Hits_Minus;
	private ImageButton btn_Runs_Plus;
	private ImageButton btn_Runs_Minus;
	private ImageButton btn_Rbis_Plus;
	private ImageButton btn_Rbis_Minus;
	private ImageButton btn_k_Plus;
	private ImageButton btn_k_Minus;
	private ImageButton btn_walk_Plus;
	private ImageButton btn_walk_Minus;
	private ImageButton btn_sac_Plus;
	private ImageButton btn_sac_Minus;
	private ImageButton btn_hbp_Plus;
	private ImageButton btn_hbp_Minus;
	private ImageButton btn_double_Plus;
	private ImageButton btn_double_Minus;
	private ImageButton btn_triple_Plus;
	private ImageButton btn_triple_Minus;
	private ImageButton btn_hr_Plus;
	private ImageButton btn_hr_Minus;
	
	private Button btn_AddGame;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_game);
		db = new BatStatsDBHelper(this);
		db.open();

		setButtons();
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			playerID = extras.getLong("PLAYER_ID");
		}
		curPlayer = db.getPlayerByID(playerID);
		// Set Defaults
		setDefaults();
		updateStats();
		getActionBar().setDisplayHomeAsUpEnabled(true);

	}
	
	private void setButtons(){
		//set Buttons
		btn_PA_Plus = (ImageButton) findViewById(R.id.btn_plus_pa);
		btn_PA_Minus = (ImageButton) findViewById(R.id.btn_minus_pa);
		btn_Hits_Plus = (ImageButton) findViewById(R.id.btn_plus_hits);
		btn_Hits_Minus = (ImageButton) findViewById(R.id.btn_minus_hits);
		btn_Runs_Plus = (ImageButton) findViewById(R.id.btn_plus_runs);
		btn_Runs_Minus = (ImageButton) findViewById(R.id.btn_minus_runs);
		btn_Rbis_Plus = (ImageButton) findViewById(R.id.btn_plus_rbis);
		btn_Rbis_Minus = (ImageButton) findViewById(R.id.btn_minus_rbis);
		btn_k_Plus = (ImageButton) findViewById(R.id.btn_plus_k);
		btn_k_Minus = (ImageButton) findViewById(R.id.btn_minus_k);
		btn_walk_Plus = (ImageButton) findViewById(R.id.btn_plus_walk);
		btn_walk_Minus = (ImageButton) findViewById(R.id.btn_minus_walk);
		btn_sac_Plus = (ImageButton) findViewById(R.id.btn_plus_sac);
		btn_sac_Minus = (ImageButton) findViewById(R.id.btn_minus_sac);
		btn_hbp_Plus = (ImageButton) findViewById(R.id.btn_plus_hbp);
		btn_hbp_Minus = (ImageButton) findViewById(R.id.btn_minus_hbp);
		btn_double_Plus = (ImageButton) findViewById(R.id.btn_plus_double);
		btn_double_Minus = (ImageButton) findViewById(R.id.btn_minus_double);
		btn_triple_Plus = (ImageButton) findViewById(R.id.btn_plus_triple);
		btn_triple_Minus = (ImageButton) findViewById(R.id.btn_minus_triple);
		btn_hr_Plus = (ImageButton) findViewById(R.id.btn_plus_hr);
		btn_hr_Minus = (ImageButton) findViewById(R.id.btn_minus_hr);
		
		
		//add listeners
		btn_PA_Plus.setOnClickListener(this);
		btn_PA_Minus.setOnClickListener(this);
		btn_Hits_Plus.setOnClickListener(this);
		btn_Hits_Minus.setOnClickListener(this);
		btn_Runs_Plus.setOnClickListener(this);
		btn_Runs_Minus.setOnClickListener(this);
		btn_Rbis_Plus.setOnClickListener(this);
		btn_Rbis_Minus.setOnClickListener(this);
		btn_k_Plus.setOnClickListener(this);
		btn_k_Minus.setOnClickListener(this);
		btn_walk_Plus.setOnClickListener(this);
		btn_walk_Minus.setOnClickListener(this);
		btn_sac_Plus.setOnClickListener(this);
		btn_sac_Minus.setOnClickListener(this);
		btn_hbp_Plus.setOnClickListener(this);
		btn_hbp_Minus.setOnClickListener(this);
		btn_double_Plus.setOnClickListener(this);
		btn_double_Minus.setOnClickListener(this);
		btn_triple_Plus.setOnClickListener(this);
		btn_triple_Minus.setOnClickListener(this);
		btn_hr_Plus.setOnClickListener(this);
		btn_hr_Minus.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_game_actions, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		if (item.getItemId() == R.id.action_save){
			if (addGame()) {
				Intent intent = new Intent(getBaseContext(),
						PlayerHomeActivity.class);
				intent.putExtra("PLAYER_ID", playerID);
				intent.putExtra("SEASON", seasonAsString);
				startActivity(intent);
				this.finish();
			}
			return true;
		}
		else if(item.getItemId() == android.R.id.home){
 			Intent intent = new Intent(this, PlayerHomeActivity.class);
 		   	intent.putExtra("PLAYER_ID", playerID);
 	   		startActivity(intent);
 	   		this.finish();
			return true;
		}
		else{
			return super.onOptionsItemSelected(item);
		}
	}

	private void setDefaults() {


		seasonText = (EditText) findViewById(R.id.txt_season);
		seasonText.setText("" + Calendar.getInstance().get(Calendar.YEAR));

		paText = (TextView) findViewById(R.id.txt_pa);
		paText.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		paText.setText("0");

		hitText = (TextView) findViewById(R.id.txt_hits);
		hitText.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		hitText.setText("0");

		runText = (TextView) findViewById(R.id.txt_runs);
		runText.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		runText.setText("0");

		rbiText = (TextView) findViewById(R.id.txt_rbis);
		rbiText.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		rbiText.setText("0");
		
		kText = (TextView) findViewById(R.id.txt_k);
		kText.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		kText.setText("0");

		walkText = (TextView) findViewById(R.id.txt_walk);
		walkText.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		walkText.setText("0");

		sacText = (TextView) findViewById(R.id.txt_sac);
		sacText.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		sacText.setText("0");
		
		hbpText = (TextView) findViewById(R.id.txt_hbp);
		hbpText.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
		hbpText.setText("0");

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
			seasonAsString = seasonText.getText().toString();
			pa = Integer.parseInt(paText.getText().toString());
			hits = Integer.parseInt(hitText.getText().toString());
			runs = Integer.parseInt(runText.getText().toString());
			rbis = Integer.parseInt(rbiText.getText().toString());
			ks = Integer.parseInt(kText.getText().toString());
			walks = Integer.parseInt(walkText.getText().toString());
			sac = Integer.parseInt(sacText.getText().toString());
			hbp = Integer.parseInt(hbpText.getText().toString());
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
		if (hits > pa) {
			Toast.makeText(getApplicationContext(),
					"Hits cannot be greater than Plate Appearances", Toast.LENGTH_LONG)
					.show();
			return false;
		}
		if (doub > hits) {
			Toast.makeText(getApplicationContext(),
					"Double cannot be greater Than Hits",
					Toast.LENGTH_LONG).show();
			return false;
		}
		if (triple > hits) {
			Toast.makeText(getApplicationContext(),
					"Triple cannot be greater Than Hits",
					Toast.LENGTH_LONG).show();
			return false;
		}
		if (homerun > hits) {
			Toast.makeText(getApplicationContext(),
					"Homerun cannot be greater Than Hits",
					Toast.LENGTH_LONG).show();
			return false;
		}
		if  ((doub + triple + homerun) > hits) {
			Toast.makeText(
					getApplicationContext(),
					"Double+Triple+Homerun cannot be greater Than Hits",
					Toast.LENGTH_LONG).show();
			return false;
		}
		if(hits + walks + sac + hbp + ks > pa){
			Toast.makeText(
					getApplicationContext(),
					"Hits + Walks + Sac + HBP + K cannot be greater Than Plate Appearances",
					Toast.LENGTH_LONG).show();
			return false;
		}
		return true;

	}

	public boolean addGame() {

		if (updateStats()) {
			int atbats = pa - walks - sac - hbp;
			try{
				PlayerStats currentStats = db.getPlayerStatsbyPlayerIDandSeason(
						playerID, season);
				
				
				if (currentStats != null) {
					db.insertGameLog(playerID, season, pa, atbats, hits, runs, rbis, walks, ks, sac, hbp, doub, triple, homerun);
					
					db.updatePlayerStats(playerID, season,pa + currentStats.getPA(),
							atbats + currentStats.getAtBat(),
							hits + currentStats.getHit(),
							runs + currentStats.getRun(),							
							rbis + currentStats.getRBI(),
							ks + currentStats.getK(),
							walks + currentStats.getWalk(),
							sac + currentStats.getSacrifice(),
							hbp + currentStats.getHBP(),
							doub + currentStats.getDouble(),
							triple + currentStats.getTriple(), homerun
									+ currentStats.getHomerun());
				}
			}catch(CursorIndexOutOfBoundsException e1){
				db.insertGameLog(playerID, season, pa, atbats, hits, runs, rbis, walks, ks, sac, hbp, doub, triple, homerun);
				db.insertPlayerStats(playerID, season, pa, atbats, hits, runs,
						rbis, walks, ks, sac, hbp, doub, triple, homerun);
			}
			

			db.close();
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View arg0) {
		
		
		if(btn_PA_Plus.isPressed() && !paText.getText().toString().equals("10")){
			paText.setText( (Integer.parseInt(paText.getText().toString()) + 1 ) + "");
		}
		else if(btn_PA_Minus.isPressed() && !paText.getText().toString().equals("0")){
				paText.setText( (Integer.parseInt(paText.getText().toString()) - 1 ) + "");
		}
		else if(btn_Hits_Plus.isPressed() && !hitText.getText().toString().equals("10")){
			hitText.setText( (Integer.parseInt(hitText.getText().toString()) + 1 ) + "");
		}
		else if(btn_Hits_Minus.isPressed() && !hitText.getText().toString().equals("0")){
				hitText.setText( (Integer.parseInt(hitText.getText().toString()) - 1 ) + "");
		}
		else if(btn_Runs_Plus.isPressed() && !runText.getText().toString().equals("10")){
			runText.setText( (Integer.parseInt(runText.getText().toString()) + 1 ) + "");
		}
		else if(btn_Runs_Minus.isPressed() && !runText.getText().toString().equals("0")){
				runText.setText( (Integer.parseInt(runText.getText().toString()) - 1 ) + "");
		}
		else if(btn_Rbis_Plus.isPressed() && !rbiText.getText().toString().equals("10")){
			rbiText.setText( (Integer.parseInt(rbiText.getText().toString()) + 1 ) + "");
		}
		else if(btn_Rbis_Minus.isPressed() && !rbiText.getText().toString().equals("0")){
				rbiText.setText( (Integer.parseInt(rbiText.getText().toString()) - 1 ) + "");
		}
		else if(btn_k_Plus.isPressed() && !kText.getText().toString().equals("10")){
			kText.setText( (Integer.parseInt(kText.getText().toString()) + 1 ) + "");
		}
		else if(btn_k_Minus.isPressed() && !kText.getText().toString().equals("0")){
				kText.setText( (Integer.parseInt(kText.getText().toString()) - 1 ) + "");
		}
		else if(btn_walk_Plus.isPressed() && !walkText.getText().toString().equals("10")){
			walkText.setText( (Integer.parseInt(walkText.getText().toString()) + 1 ) + "");
		}
		else if(btn_walk_Minus.isPressed() && !walkText.getText().toString().equals("0")){
				walkText.setText( (Integer.parseInt(walkText.getText().toString()) - 1 ) + "");
		}
		else if(btn_sac_Plus.isPressed() && !sacText.getText().toString().equals("10")){
			sacText.setText( (Integer.parseInt(sacText.getText().toString()) + 1 ) + "");
		}
		else if(btn_sac_Minus.isPressed() && !sacText.getText().toString().equals("0")){
				sacText.setText( (Integer.parseInt(sacText.getText().toString()) - 1 ) + "");
		}
		else if(btn_hbp_Plus.isPressed() && !hbpText.getText().toString().equals("10")){
			hbpText.setText( (Integer.parseInt(hbpText.getText().toString()) + 1 ) + "");
		}
		else if(btn_hbp_Minus.isPressed() && !hbpText.getText().toString().equals("0")){
				hbpText.setText( (Integer.parseInt(hbpText.getText().toString()) - 1 ) + "");
		}
		else if(btn_double_Plus.isPressed() && !doubText.getText().toString().equals("10")){
			doubText.setText( (Integer.parseInt(doubText.getText().toString()) + 1 ) + "");
		}
		else if(btn_double_Minus.isPressed() && !doubText.getText().toString().equals("0")){
				doubText.setText( (Integer.parseInt(doubText.getText().toString()) - 1 ) + "");
		}
		else if(btn_triple_Plus.isPressed() && !tripleText.getText().toString().equals("10")){
			tripleText.setText( (Integer.parseInt(tripleText.getText().toString()) + 1 ) + "");
		}
		else if(btn_triple_Minus.isPressed() && !tripleText.getText().toString().equals("0")){
				tripleText.setText( (Integer.parseInt(tripleText.getText().toString()) - 1 ) + "");
		}
		else if(btn_hr_Plus.isPressed() && !hrText.getText().toString().equals("10")){
			hrText.setText( (Integer.parseInt(hrText.getText().toString()) + 1 ) + "");
		}
		else if(btn_hr_Minus.isPressed() && !hrText.getText().toString().equals("0")){
				hrText.setText( (Integer.parseInt(hrText.getText().toString()) - 1 ) + "");
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

		if (v.getId() == R.id.txt_pa) {
		
			curTextView = paText;
			screenTitle = "Plate Appearances";
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
		else if(v.getId() == R.id.txt_k){
			curTextView = kText;
			screenTitle = "Strikeouts";
		}
		else if(v.getId() == R.id.txt_walk){
			curTextView = walkText;
			screenTitle = "Walks";
		}
		else if(v.getId() == R.id.txt_sac){
			curTextView = sacText;
			screenTitle = "Sacrifice Hits";
		}
		else if(v.getId() == R.id.txt_hbp){
			curTextView = hbpText;
			screenTitle = "Hit By Pitch";
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
		np.setValue(Integer.parseInt(curTextView.getText().toString()));
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
