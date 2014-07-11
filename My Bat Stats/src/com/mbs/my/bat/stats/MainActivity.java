package com.mbs.my.bat.stats;



import java.util.List;

import com.mbs.my.bat.stats.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
//import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private BatStatsDBHelper db;
	// Spinner element
    private List <Player> mPlayerList;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private LinearLayout mLinearLayout;
    private CharSequence mTitle;
    private CharSequence mDrawerTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
              
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);   
        db = new BatStatsDBHelper(this);
        db.open();
        mTitle = "My Bat Stats";
        mDrawerTitle = "Load Player";
        mLinearLayout = (LinearLayout) findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer_list);
        mPlayerList = db.getAllPlayersList();
        ArrayAdapter <Player> dataAdapter = new ArrayAdapter <Player>(this,
				R.layout.drawer_list_item, mPlayerList);
        // Set the adapter for the list view
        mDrawerList.setAdapter(dataAdapter);
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        
        

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        Resources res = getResources(); //resource handle
        Drawable drawable = res.getDrawable(R.drawable.baseballdiamond); //new Image that was added to the res folder
        RelativeLayout relLayout = (RelativeLayout)findViewById(R.id.activity_main); 
        relLayout.setBackgroundDrawable(drawable);
        
        
    }

	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.android_main_actions, menu);
        return true;
    }
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	   if(item.getItemId() == R.id.action_addplayer){
            addPlayer();
            return true;
	   }
	   else{
		   if (mDrawerToggle.onOptionsItemSelected(item)) {
			      return true;
			    }
		   else{
			   return super.onOptionsItemSelected(item);
		   }
	    }
	}
    
    public void addPlayer(){
    	
    	Intent intent = new Intent(this, AddPlayerActivity.class);
    	startActivity(intent);
    	this.finish();
    }
    
    public void loadPlayer(int position){
    try{
    	
	    	Bundle b = new Bundle();
	        b.putLong("PLAYER_ID", mPlayerList.get(position).getID());       
    	
    		Intent intent = new Intent(getBaseContext(), PlayerHomeActivity.class);
        	intent.putExtras(b);
        	startActivity(intent);
        	this.finish();
    	}
    catch(NullPointerException e){
    	Toast.makeText(getApplicationContext(), "No Player Selected. Use Menu to Add Player.", Toast.LENGTH_LONG).show();
    }
    	
    	
    }

	@Override
	public void onClick(View v) {
	
		// If load button was clicked
	      // if (imgLoadPlayer.isPressed()) {
	          // 	loadPlayer();
	      // } 
		
	}

	@Override
	public void onBackPressed() {
		Toast.makeText(getApplicationContext(), "Nowhere to Go! Press Home to Exit MyBatStats.", Toast.LENGTH_LONG).show();
	}
	
	/* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
    
    private void selectItem(int position) {
     
    	loadPlayer(position);
        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
      
        mDrawerLayout.closeDrawer(mLinearLayout);
    }
	
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
    /**
     * Animate the Navigation drawer on launch to show the user it exists
     */
    @Override
    protected void onResume() {
        super.onResume(); 
        
        Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		boolean launch = true;
		 
		if (extras != null) {
			launch = extras.getBoolean("LAUNCH");			
		}
		
		
		if(launch){
			mDrawerLayout.postDelayed(new Runnable() {
	            @Override
	            public void run() {
	            	mDrawerLayout.openDrawer(Gravity.LEFT);
	            }
	        }, 1000);
		}
        
    }

}
