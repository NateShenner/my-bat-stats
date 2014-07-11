package com.mbs.my.bat.stats;


import java.io.File;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.GridLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class PlayerHomeActivity extends FragmentActivity implements ActionBar.TabListener {

	
	
	AppSectionsPagerAdapter mAppSectionsPagerAdapter;
	android.support.v4.view.ViewPager mViewPager;
	private Player curPlayer;
	private String playerID;
	private BatStatsDBHelper db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player_main);
		 mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
		 final ActionBar actionBar = getActionBar();
		 actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		 
		 mViewPager = (android.support.v4.view.ViewPager) findViewById(R.id.pager);
	        mViewPager.setAdapter(mAppSectionsPagerAdapter);
	        mViewPager.setOnPageChangeListener(new android.support.v4.view.ViewPager.SimpleOnPageChangeListener() {
	            @Override
	            public void onPageSelected(int position) {
	                // When swiping between different app sections, select the corresponding tab.
	                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
	                // Tab.
	                actionBar.setSelectedNavigationItem(position);
	            }
	        });

	        // For each of the sections in the app, add a tab to the action bar.
	        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
	            // Create a tab with text corresponding to the page title defined by the adapter.
	            // Also specify this Activity object, which implements the TabListener interface, as the
	            // listener for when this tab is selected.
	        	
	            actionBar.addTab(
	                    actionBar.newTab()
	                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
	                            .setTabListener(this));
	        }
	        getActionBar().setDisplayHomeAsUpEnabled(true);
            //getActionBar().setHomeButtonEnabled(true);
	        
	        
	        Intent intent = getIntent();
    		Bundle extras = intent.getExtras();
    		
    		 
    		if (extras != null) {
    			playerID = extras.getLong("PLAYER_ID") + "";
    			
    		}

    		// database handler
    		db = new BatStatsDBHelper(this);
    		db.open();

    		curPlayer = db.getPlayerByID(Long.parseLong(playerID));
    		
		
	}
	
	@Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }
	
	

	

	
	

	
	
	 public static class AppSectionsPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

		 	public static final String PAGE_TITLE_PLAYER_CARD = "Player Card";
		 	public static final String PAGE_TITLE_GAME_LOG = "Game Log";
		 	
	        public AppSectionsPagerAdapter(android.support.v4.app.FragmentManager fm) {
	            super(fm);
	        }

	        @Override
	        public android.support.v4.app.Fragment getItem(int i) {
	            switch (i) {
	                case 0:
	                    return new PlayerHomeSectionFragment();

	                default:
	                    return new GameLogSectionFragment();
	            }
	        }

	        @Override
	        public int getCount() {
	            return 2;
	        }

	        @Override
	        public CharSequence getPageTitle(int position) {
	        	switch (position) {
                case 0:
                    return PAGE_TITLE_PLAYER_CARD;

                default:
                    return PAGE_TITLE_GAME_LOG;
            }
	        }
	    }
	 
	 /**
	     * A fragment that launches other parts of the demo application.
	     */
	    public static class PlayerHomeSectionFragment extends android.support.v4.app.Fragment implements OnItemSelectedListener {
	    	private TextView playerName;
	    	private TextView teamName;
	    	private ImageView playerPic;
	    	private BatStatsDBHelper db;
	    	private Player curPlayer;
	    	private Spinner seasonSpinner;
	    	private String selectedSeason = "0";
	    	private String lastselectedSeason = "0";

	    	// table columns
	    	int COL_PA = 0;
	    	int COL_ATBATS = 1;
	    	int COL_HITS = 2;
	    	int COL_RUNS = 3;
	    	int COL_RBIS = 4;
	    	int COL_K = 5;
	    	int COL_WALKS = 6;
	    	int COL_SAC = 7;
	    	int COL_HBP = 8;
	    	int COL_DOUBLE = 9;
	    	int COL_TRIPLE = 10;
	    	int COL_HR = 11;

	    	int NUM_COLS = 12;

	    	int COL_AVG = 0;
	    	int COL_OBP = 1;
	    	int COL_SLG = 2;
	    	int COL_OPS = 3;

	    	int NUM_COLS_2 = 4;
	        @Override
	        public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                Bundle savedInstanceState) {
	            View rootView = inflater.inflate(R.layout.activity_player_home, container, false);
	            db = new BatStatsDBHelper(this.getActivity().getApplicationContext());

	    		playerName = (TextView) rootView.findViewById(R.id.lbl_playername);
	    		teamName = (TextView) rootView.findViewById(R.id.lbl_teamname);
	    		playerPic = (ImageView) rootView.findViewById(R.id.img_player);
	    		seasonSpinner = (Spinner) rootView.findViewById(R.id.spinnerseason);
	    		seasonSpinner.setOnItemSelectedListener(this);
	    		

	    		setPlayerDetails();
	    		setupStatsTableHeader(rootView);
	    		setupStatsTableData(rootView);
	    		
	    		
	            return rootView;
	        }
	        @SuppressWarnings("deprecation")
	    	private void setupStatsTableHeader(View rootView) {
	    		// populate cols array
	    		String[] cols = new String[NUM_COLS];
	    		cols[COL_PA] = "PA";
	    		cols[COL_ATBATS] = "AB";
	    		cols[COL_HITS] = "H";
	    		cols[COL_RUNS] = "R";
	    		cols[COL_RBIS] = "RBI";
	    		cols[COL_K] = "K";
	    		cols[COL_WALKS] = "BB";
	    		cols[COL_SAC] = "SAC";
	    		cols[COL_HBP] = "HBP";
	    		cols[COL_DOUBLE] = "2B";
	    		cols[COL_TRIPLE] = "3B";
	    		cols[COL_HR] = "HR";

	    		String[] cols2 = new String[NUM_COLS_2];
	    		cols2[COL_AVG] = "AVG";
	    		cols2[COL_OBP] = "OBP";
	    		cols2[COL_SLG] = "SLG";
	    		cols2[COL_OPS] = "OPS";

	    		// Get the TableLayout
	    		TableLayout tl = (TableLayout) rootView.findViewById(R.id.statstable);
	    		TableLayout t2 = (TableLayout) rootView.findViewById(R.id.statstableadvanced);

	    		// Create a TableRow and give it an ID
	    		TableRow tr = new TableRow(this.getActivity().getApplicationContext());
	    		tr.setId(1);
	    		TableRow tr2 = new TableRow(this.getActivity().getApplicationContext());
	    		tr2.setId(2);

	    		// Go through each item in the array
	    		for (int current = 0; current < NUM_COLS; current++) {
	    			// Create a TextView to house the name of the province
	    			TextView labelTV = new TextView(this.getActivity().getApplicationContext());
	    			labelTV.setId(200 + current);
	    			labelTV.setText(cols[current]);
	    			labelTV.setTextColor(Color.BLACK);
	    			labelTV.setPadding(0, 0, 15, 0);
	    			labelTV.setGravity(Gravity.CENTER);
	    			labelTV.setBackgroundColor(Color.GRAY);

	    			tr.addView(labelTV);
	    		}
	    		// Add the TableRow to the TableLayout
	    		tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,
	    				LayoutParams.WRAP_CONTENT));

	    		// ADVANCED TABLE
	    		// Go through each item in the array
	    		for (int current = 0; current < NUM_COLS_2; current++) {
	    			// Create a TextView to house the name of the province
	    			TextView labelTV = new TextView(this.getActivity().getApplicationContext());
	    			labelTV.setId(200 + current);
	    			labelTV.setText(cols2[current]);
	    			labelTV.setTextColor(Color.BLACK);
	    			labelTV.setPadding(0, 0, 15, 0);
	    			labelTV.setGravity(Gravity.CENTER);
	    			labelTV.setBackgroundColor(Color.GRAY);

	    			tr2.addView(labelTV);
	    		}
	    		// Add the TableRow to the TableLayout
	    		t2.addView(tr2, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,
	    				LayoutParams.WRAP_CONTENT));

	    	}

	    	@SuppressWarnings("deprecation")
	    	private void setupStatsTableData(View rootView) {

	    		db.open();
	    		PlayerStats curPlayerStats = new PlayerStats();
	    		if(!seasonSpinner.getSelectedItem().equals(getResources().getString(R.string.spinner_value_career))){
	    			int season = Integer.parseInt((String)seasonSpinner.getSelectedItem());
	    			curPlayerStats = db.getPlayerStatsbyPlayerIDandSeason(curPlayer.getID(), season );
	    		}
	    		else{
	    			//Career Stats
	    			List <Long> seasonList = db.getAllSeasonsForPlayer(curPlayer.getID());
	    			for(long curSeason : seasonList){
	    				db.open();
	    				PlayerStats curStats = db.getPlayerStatsbyPlayerIDandSeason(curPlayer.getID(), (int)curSeason);
	    				curPlayerStats.setPA(curPlayerStats.getPA() + curStats.getPA());
	    				curPlayerStats.setHit(curPlayerStats.getHit() + curStats.getHit());
	    				curPlayerStats.setRun(curPlayerStats.getRun() + curStats.getRun());
	    				curPlayerStats.setRBI(curPlayerStats.getRBI() + curStats.getRBI());
	    				curPlayerStats.setK(curPlayerStats.getK() + curStats.getK());
	    				curPlayerStats.setWalk(curPlayerStats.getWalk() + curStats.getWalk());
	    				curPlayerStats.setSacrifice(curPlayerStats.getSacrifice() + curStats.getSacrifice());
	    				curPlayerStats.setHBP(curPlayerStats.getHBP() + curStats.getHBP());
	    				curPlayerStats.setDouble(curPlayerStats.getDouble() + curStats.getDouble());
	    				curPlayerStats.setTriple(curPlayerStats.getTriple() + curStats.getTriple());
	    				curPlayerStats.setHomerun(curPlayerStats.getHomerun() + curStats.getHomerun());
	    			}
	    			
	    		}

	    		// populate cols array
	    		DecimalFormat df = new DecimalFormat(".000");
	    		String[] colsData = new String[NUM_COLS];
	    		colsData[COL_PA] = "" + curPlayerStats.getPA();
	    		colsData[COL_ATBATS] = "" + curPlayerStats.getAtBat();
	    		colsData[COL_HITS] = "" + curPlayerStats.getHit();
	    		colsData[COL_RUNS] = "" + curPlayerStats.getRun();
	    		colsData[COL_RBIS] = "" + curPlayerStats.getRBI();
	    		colsData[COL_K] = "" + curPlayerStats.getK();
	    		colsData[COL_WALKS] = "" + curPlayerStats.getWalk();
	    		colsData[COL_SAC] = "" + curPlayerStats.getSacrifice();
	    		colsData[COL_HBP] = "" + curPlayerStats.getHBP();
	    		colsData[COL_DOUBLE] = "" + curPlayerStats.getDouble();
	    		colsData[COL_TRIPLE] = "" + curPlayerStats.getTriple();
	    		colsData[COL_HR] = "" + curPlayerStats.getHomerun();

	    		// Advanced
	    		String[] colsData2 = new String[NUM_COLS_2];
	    		colsData2[COL_AVG] = df.format(curPlayerStats.calcBattingAvg());
	    		colsData2[COL_OBP] = df.format(curPlayerStats.calcOBP());
	    		colsData2[COL_SLG] = df.format(curPlayerStats.calcSlugging());
	    		colsData2[COL_OPS] = df.format(curPlayerStats.calcOPS());

	    		// Get the TableLayout
	    		TableLayout tl = (TableLayout) rootView.findViewById(R.id.statstable);
	    		TableLayout t2 = (TableLayout) rootView.findViewById(R.id.statstableadvanced);
	    		// Create a TableRow and give it an ID
	    		TableRow tr = new TableRow(this.getActivity().getApplicationContext());
	    		tr.setId(3);
	    		TableRow tr2 = new TableRow(this.getActivity().getApplicationContext());
	    		tr2.setId(4);

	    		// Go through each item in the array
	    		for (int current = 0; current < NUM_COLS; current++) {
	    			// Create a TextView to house the name of the province
	    			TextView labelTV = new TextView(this.getActivity().getApplicationContext());
	    			labelTV.setId(300 + current);
	    			labelTV.setText(colsData[current]);
	    			labelTV.setTextColor(Color.BLACK);
	    			labelTV.setPadding(0, 0, 15, 0);
	    			labelTV.setGravity(Gravity.CENTER);
	    			labelTV.setBackgroundColor(Color.WHITE);

	    			tr.addView(labelTV);
	    		}
	    		// Add the TableRow to the TableLayout
	    		tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,
	    				LayoutParams.WRAP_CONTENT));

	    		// ADVANCED STATS
	    		for (int current = 0; current < NUM_COLS_2; current++) {
	    			// Create a TextView to house the name of the province
	    			TextView labelTV = new TextView(this.getActivity().getApplicationContext());
	    			labelTV.setId(300 + current);
	    			labelTV.setText(colsData2[current]);
	    			labelTV.setTextColor(Color.BLACK);
	    			labelTV.setPadding(0, 0, 15, 0);
	    			labelTV.setGravity(Gravity.CENTER);
	    			labelTV.setBackgroundColor(Color.WHITE);

	    			tr2.addView(labelTV);
	    		}
	    		// Add the TableRow to the TableLayout
	    		t2.addView(tr2, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,
	    				LayoutParams.WRAP_CONTENT));
	    		db.close();
	    	}

	    	private void setPlayerDetails() {

	    		long playerID = 0;
	    		String seasonNum = "0";
	    		Intent intent = getActivity().getIntent();
	    		Bundle extras = intent.getExtras();
	    		
	    		 
	    		if (extras != null) {
	    			playerID = extras.getLong("PLAYER_ID");
	    			seasonNum = extras.getString("SEASON");
	    			lastselectedSeason = seasonNum;
	    			
	    		}

	    		// database handler
	    		db.open();

	    		curPlayer = db.getPlayerByID(playerID);

	    		// load spinner
	    		// database handler

	    		
	    		List<String> seasonListAsString = db.getAllSeasonsForPlayerAsString(curPlayer.getID());
	    		seasonListAsString.add(getResources().getString(R.string.spinner_value_career));
	    		
	    		
	    		// Creating adapter for spinner
	    		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity().getApplicationContext(),android.R.layout.simple_spinner_item,seasonListAsString);
	    		
	    		// Drop down layout style - list view with radio button
	    		dataAdapter
	    				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    		seasonSpinner.setAdapter(dataAdapter);
	    		// check if we have changed the season and selected that value.
	    		if(lastselectedSeason != null){
	    				if(lastselectedSeason.equals("0")){
	    					lastselectedSeason = ((String)seasonSpinner.getSelectedItem());
	    				}
	    		}
	    		else{
	    			lastselectedSeason = ((String)seasonSpinner.getSelectedItem());
	    		}
	    		if(seasonNum != null){
	    			if(!seasonNum.equals("0")){
	    				
	    				for (int i = 0; i < seasonSpinner.getAdapter().getCount(); i++) {
	    				    if ( ((String)seasonSpinner.getItemAtPosition(i)).equals(lastselectedSeason)) {
	    				    	seasonSpinner.setSelection(i);
	    				        break;
	    				    }
	    				}
	    			}
	    		}
	    		
	    		

	    		playerName.setText(curPlayer.getName());
	    		teamName.setText(curPlayer.getTeam());
	    		db.close();
	    		File imgFile = null;
	    		if (curPlayer.getPlayerPic() != null) {
	    			imgFile = new File(curPlayer.getPlayerPic());
	    		}

	    		if (imgFile != null) {

	    			try {
	    				Bitmap playerBMP = BitmapFactory.decodeFile(imgFile.getPath());
	    				playerPic.setImageBitmap(playerBMP);
	    			} catch (Exception e) {
	    				Toast.makeText(this.getActivity().getApplicationContext(),
	    						"Problem loading player Picture!!!", Toast.LENGTH_LONG)
	    						.show();
	    			}

	    		}

	    	}

	    	


	    	@Override
	    	public void onItemSelected(AdapterView<?> parent, View view, 
	                int pos, long id) {
	    		String curSeason = (String)parent.getItemAtPosition(pos);
	    		selectedSeason = curSeason;
	    		if(!curSeason.equals(lastselectedSeason)){
	    			Intent intent = new Intent(getActivity().getBaseContext(), PlayerHomeActivity.class);
	    			intent.putExtra("PLAYER_ID", curPlayer.getID());
	    			intent.putExtra("SEASON", selectedSeason);
	    			startActivity(intent);
	    			this.getActivity().finish();
	    		}
	    		
	    		
	    	}
	    	@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
	    }

	    
	    @Override
    	public boolean onCreateOptionsMenu(Menu menu) {
    		// Inflate the menu; this adds items to the action bar if it is present.
    		getMenuInflater().inflate(R.menu.player_home_actions, menu);
    		getMenuInflater().inflate(R.menu.player_home, menu);
    		return true;
    	}
	    /**
	     * Game Log screen (Fragment)
	     */
	    public static class GameLogSectionFragment extends android.support.v4.app.Fragment {
	    	// table columns
	    	int COL_PA = 0;
	    	int COL_ATBATS = 1;
	    	int COL_HITS = 2;
	    	int COL_RUNS = 3;
	    	int COL_RBIS = 4;
	    	int COL_K = 5;
	    	int COL_WALKS = 6;
	    	int COL_SAC = 7;
	    	int COL_HBP = 8;
	    	int COL_DOUBLE = 9;
	    	int COL_TRIPLE = 10;
	    	int COL_HR = 11;

	    	int NUM_COLS = 12;
	    	
	    	 Long playerID;	    	
	    	 Long curGameID;
	    	 private BatStatsDBHelper db;
	    	 public static final int MAX_GAMES = 10;
	    	 
			@Override
	        public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                Bundle savedInstanceState) {
				Intent intent = getActivity().getIntent();
	    		Bundle extras = intent.getExtras();
	    		if (extras != null) {
	    			playerID = extras.getLong("PLAYER_ID");
	    		}
	    		
	            View rootView = inflater.inflate(R.layout.activity_player_gamelog, container, false);
	            db = new BatStatsDBHelper(this.getActivity().getApplicationContext());
	            db.open();
	            setLogTableHeader(rootView);
	            setLogTableContent(rootView);
	            return rootView;
			}

			private void setLogTableHeader(View rootView) {
				// populate cols array
	    		String[] cols = new String[NUM_COLS];
	    		cols[COL_PA] = "PA";
	    		cols[COL_ATBATS] = "AB";
	    		cols[COL_HITS] = "H";
	    		cols[COL_RUNS] = "R";
	    		cols[COL_RBIS] = "RBI";
	    		cols[COL_K] = "K";
	    		cols[COL_WALKS] = "BB";
	    		cols[COL_SAC] = "SAC";
	    		cols[COL_HBP] = "HBP";
	    		cols[COL_DOUBLE] = "2B";
	    		cols[COL_TRIPLE] = "3B";
	    		cols[COL_HR] = "HR";

	    		// Get the TableLayout
	    		TableLayout t = (TableLayout)rootView.findViewById(R.id.gamelogtable);

	    		// Create a TableRow and give it an ID
	    		TableRow tr = new TableRow(this.getActivity().getApplicationContext());
	    		tr.setId(1);
	    		
	    		// Go through each item in the array
	    		for (int current = 0; current < NUM_COLS; current++) {
	    			// Create a TextView to house the name of the province
	    			TextView labelTV = new TextView(this.getActivity().getApplicationContext());
	    			labelTV.setId(200 + current);
	    			labelTV.setText(cols[current]);
	    			labelTV.setTextColor(Color.BLACK);
	    			labelTV.setPadding(0, 0, 15, 0);
	    			labelTV.setGravity(Gravity.CENTER);
	    			labelTV.setBackgroundColor(Color.GRAY);
	    			tr.addView(labelTV);
	    		}
	    		//Blank Header column for delete
	    		TextView blankView = new TextView(this.getActivity().getApplicationContext());
	    		blankView.setText("");
	    		blankView.setBackgroundColor(Color.GRAY);
	    		tr.addView(blankView);
	    		// Add the TableRow to the TableLayout
	    		t.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,
	    				LayoutParams.WRAP_CONTENT));
			}
			
			private void setLogTableContent(final View rootView){
				db.open();
				curGameID = new Long(0);
	    		List <GameLog> gameLogs = new ArrayList<GameLog>();
	    		gameLogs = db.getPlayerGamesbyPlayerID(playerID, MAX_GAMES);
	    		TableLayout t = (TableLayout) rootView.findViewById(R.id.gamelogtable);
	    		
	    		
	    		for(final GameLog curGame : gameLogs){
	    		
	    			final PlayerStats curStats = db.getPlayerStatsbyPlayerIDandSeason(playerID, curGame.getSeason());
	    			// Create a TableRow and give it an ID
		    		TableRow tr = new TableRow(this.getActivity().getApplicationContext());
		    		tr.setPadding(0, 0, 0, 40);
		    		tr.setId( (int) curGame.getID());
		    		String[] colsData = new String[NUM_COLS];
		    		colsData[COL_PA] = "" + curGame.getPA();
		    		colsData[COL_ATBATS] = "" + curGame.getAtBat();
		    		colsData[COL_HITS] = "" + curGame.getHit();
		    		colsData[COL_RUNS] = "" + curGame.getRun();
		    		colsData[COL_RBIS] = "" + curGame.getRBI();
		    		colsData[COL_K] = "" + curGame.getK();
		    		colsData[COL_WALKS] = "" + curGame.getWalk();
		    		colsData[COL_SAC] = "" + curGame.getSacrifice();
		    		colsData[COL_HBP] = "" + curGame.getHBP();
		    		colsData[COL_DOUBLE] = "" + curGame.getDouble();
		    		colsData[COL_TRIPLE] = "" + curGame.getTriple();
		    		colsData[COL_HR] = "" + curGame.getHomerun();
		    		
		    		// Go through each item in the array
		    		for (int current = 0; current < NUM_COLS; current++) {
		    			
		    			TextView labelTV = new TextView(this.getActivity().getApplicationContext());
		    			labelTV.setId(300 + current);
		    			labelTV.setText(colsData[current]);
		    			labelTV.setTextColor(Color.BLACK);
		    			labelTV.setPadding(0, 0, 15, 0);
		    			labelTV.setGravity(Gravity.CENTER);
		    			labelTV.setBackgroundColor(Color.WHITE);
		    			tr.addView(labelTV);
		    		}
		    		ImageButton del = new ImageButton(this.getActivity().getApplicationContext());
		    		del.setBackgroundResource(R.drawable.ic_discard_gray);
		    		del.setClickable(true);
		    		del.setOnClickListener(new android.view.View.OnClickListener()
		    		{
		    			 @Override public void onClick(final View v)
		    		        {
		    		           
		    		            
		    		            AlertDialog.Builder alertDialog = new AlertDialog.Builder(rootView.getContext());

		    		    		// Setting Dialog Title
		    		    		alertDialog.setTitle("Confirm Delete Game...");

		    		    		// Setting Dialog Message
		    		    		alertDialog.setMessage("Are you sure you want delete this game?");

		    		    		// Setting Icon to Dialog
		    		    		alertDialog.setIcon(R.drawable.ic_launcher);

		    		    		// Setting Positive "Yes" Button
		    		    		alertDialog.setPositiveButton("YES",
		    		    				new DialogInterface.OnClickListener() {
		    		    					public void onClick(DialogInterface dialog, int which) {
		    		    						 // row is your row, the parent of the clicked button
		    			    		            View row = (View) v.getParent();
		    			    		            // container contains all the rows, you could keep a variable somewhere else to the container which you can refer to here
		    			    		            ViewGroup container = ((ViewGroup)row.getParent());
		    			    		            // delete the row and invalidate your view so it gets redrawn
		    			    		            container.removeView(row);
		    			    		            container.invalidate();
		    		    						 db.open();
		    				    		         db.deleteGameLog(row.getId());
		    				    		         db.updatePlayerStats(playerID, curGame.getSeason(), curStats.getPA() - curGame.getPA(), curStats.getAtBat() - curGame.getAtBat(), curStats.getHit() - curGame.getHit(), curStats.getRun() - curGame.getRun(), curStats.getRBI() - curGame.getRBI(), curStats.getK() - curGame.getK(), curStats.getWalk() - curGame.getWalk(), curStats.getSacrifice() - curGame.getSacrifice(), curStats.getHBP() - curGame.getHBP(), curStats.getDouble() - curGame.getDouble(), curStats.getTriple() - curGame.getTriple(), curStats.getHomerun() - curGame.getHomerun());
		    				    		         Intent intent = new Intent(getActivity().getBaseContext(), PlayerHomeActivity.class);
		    				 	    			intent.putExtra("PLAYER_ID", curGame.getPlayerID());
		    				 	    			intent.putExtra("SEASON", curGame.getSeason());
		    				 	    			startActivity(intent);
		    				 	    			getActivity().finish();
		    		    					}
		    		    				});

		    		    		// Setting Negative "NO" Button
		    		    		alertDialog.setNegativeButton("NO",
		    		    				new DialogInterface.OnClickListener() {
		    		    					public void onClick(DialogInterface dialog, int which) {
		    		    						dialog.cancel();
		    		    					}
		    		    				});

		    		    		// Showing Alert Message
		    		    		alertDialog.show();
		    		           
		    		        }
		    		    });
		    		
		    		tr.addView(del);
		    		
		    		// Add the TableRow to the TableLayout
		    		t.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT,
		    				LayoutParams.WRAP_CONTENT));
	    		}
    			
	    		db.close();
			}

			
	    }
	        
	    
	    @Override
    	public boolean onOptionsItemSelected(MenuItem item) {
    		// Handle item selection
    		if (item.getItemId() == R.id.action_addgame){
    			addGame();
    			return true;
    		}
    		else if( item.getItemId() == R.id.action_edit){
    			editPlayer();
    			return true;
    		}
    		else if( item.getItemId() == R.id.action_delete){
    			deletePlayer();
    			return true;
    		}
    		else if( item.getItemId() == R.id.menu_clearstats){
    			clearStats();
    			return true;
    		}		
    		else if(item.getItemId() == android.R.id.home){
    			navigateHomePage();
    			return true;
    		}
    		else{
    			return super.onOptionsItemSelected(item);
    		}
    	}

    	private void clearStats() {
    		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

    		// Setting Dialog Title
    		alertDialog.setTitle("Confirm Clear Stats...");

    		// Setting Dialog Message
    		alertDialog.setMessage("Are you sure you want clear  "
    				+ curPlayer.getName() + "'s stats?");

    		// Setting Icon to Dialog
    		alertDialog.setIcon(R.drawable.ic_launcher);

    		// Setting Positive "Yes" Button
    		alertDialog.setPositiveButton("YES",
    				new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dialog, int which) {

    						db.open();
    						db.clearGameLog(curPlayer.getID());
    						db.deletePlayerStats(curPlayer.getID());
    						db.insertPlayerStats(curPlayer.getID(), Calendar
    								.getInstance().get(Calendar.YEAR), 0, 0, 0, 0,
    								0, 0, 0, 0, 0, 0, 0, 0);
    						Toast.makeText(getApplicationContext(),curPlayer.getName()
    										+ "'s stats have been cleared!",
    								Toast.LENGTH_SHORT).show();
    						db.close();
    						Intent intent = new Intent(getApplicationContext(), PlayerHomeActivity.class);
    						intent.putExtra("PLAYER_ID", curPlayer.getID());
    						intent.putExtra("SEASON", "0");
    						startActivity(intent);
    						
    					}
    				});

    		// Setting Negative "NO" Button
    		alertDialog.setNegativeButton("NO",
    				new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dialog, int which) {
    						dialog.cancel();
    					}
    				});

    		// Showing Alert Message
    		alertDialog.show();

    	}

    	private void editPlayer() {
    		Intent intent = new Intent(this, EditPlayerActivity.class);
    		intent.putExtra("PLAYER_ID", curPlayer.getID());
    		startActivity(intent);
    		finish();
    	}

    	private void deletePlayer() {

    		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

    		// Setting Dialog Title
    		alertDialog.setTitle("Confirm Delete...");

    		// Setting Dialog Message
    		alertDialog.setMessage("Are you sure you want delete Player: "
    				+ curPlayer.getName() + "?");

    		// Setting Icon to Dialog
    		alertDialog.setIcon(R.drawable.ic_launcher);

    		// Setting Positive "Yes" Button
    		alertDialog.setPositiveButton("YES",
    				new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dialog, int which) {

    						db.open();
    						db.deletePlayerStats(curPlayer.getID());
    						db.deletePlayer(curPlayer.getID());
    						File picFile = new File(curPlayer.getPlayerPic());
    						if (picFile.exists()) {
    							picFile.delete();
    						}
    						Toast.makeText(getApplicationContext(),
    								"Player " + curPlayer.getName() + " Deleted",
    								Toast.LENGTH_SHORT).show();
    						db.close();
    						navigateHomePage();
    					}
    				});

    		// Setting Negative "NO" Button
    		alertDialog.setNegativeButton("NO",
    				new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dialog, int which) {
    						dialog.cancel();
    					}
    				});

    		// Showing Alert Message
    		alertDialog.show();

    	}

    	private void navigateHomePage() {
    		Intent intent = new Intent(this, MainActivity.class);
    		intent.putExtra("LAUNCH", false);
    		startActivity(intent);
    		finish();
    	}

    	private void addGame() {

    		Intent intent = new Intent(this.getBaseContext(), AddGameActivity.class);
    		intent.putExtra("PLAYER_ID", curPlayer.getID());
    		startActivity(intent);
    		finish();
    	}
    	
    	@Override
    	public void onBackPressed() {
    		Intent intent = new Intent(this, MainActivity.class);
    		intent.putExtra("LAUNCH", false);
    	   	startActivity(intent);
       		this.finish();
    	}

		
}
