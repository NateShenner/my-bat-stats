package com.mbs.my.bat.stats;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;

import com.mbs.my.bat.stats.R;

import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class AddPlayerActivity extends Activity implements OnClickListener{


	private static final int CAMERA_REQUEST = 1888; 
	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";
	private static final int MAX_LENGTH = 15;
	
	private BatStatsDBHelper db;
    private ImageView imageView;
    private Bitmap photo;
    private String playerName;
    private Intent cameraIntent;
    
    //buttons
    private Button btn_CreatePlayer;
    private EditText playerText;
    private EditText teamText;
    
    private long playerID;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_player);
        db = new BatStatsDBHelper(this);
        
        //set listeners
        btn_CreatePlayer = (Button)findViewById(R.id.btn_createPlayer);
        btn_CreatePlayer.setOnClickListener(this);
        
        playerText = (EditText) findViewById(R.id.playerName);
        teamText = (EditText) findViewById(R.id.playerTeam);
        
        //camera setup
        this.imageView = (ImageView)this.findViewById(R.id.imageView1);
        Button photoButton = (Button) this.findViewById(R.id.addPic);
        photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
                startActivityForResult(cameraIntent, CAMERA_REQUEST); 
            }
        });
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add_player, menu);
        return true;
    }
    
    private void navigateHomePage(){
		Intent intent = new Intent(getBaseContext(), MainActivity.class);
    	startActivity(intent);
    	this.finish();
	}
    
    /**
     * Method to add new player to the database
     * @param view
     */
    public void createPlayer(){
    	   	
    	playerName = playerText.getText().toString();
    	String playerTeam = teamText.getText().toString();
    
    	db.open();        
        
        playerID = db.insertPlayer(
        		playerName,
        		playerTeam);        
        db.insertPlayerStats(playerID, Calendar.getInstance().get(Calendar.YEAR), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        db.close();
        
        //if photo exists, save to disk
        if(photo != null){
        	File f = new File("");
        	try{
        		
        		f = createImageFile();
        		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
        		
        		db.open();         		
        		db.updatePlayer(playerID, playerName, playerTeam, f.getPath());        		
        		db.close();
        		
        		FileOutputStream fo = null;
        		  try {
                      fo = new FileOutputStream(f.getAbsoluteFile());
                  } catch (FileNotFoundException e) {
                	  Toast.makeText(getApplicationContext(), "Problem creating Picture file!", Toast.LENGTH_LONG).show();
                  }
                  try {
                      fo.write(bytes.toByteArray());
                  } catch (IOException e) {
                	  Toast.makeText(getApplicationContext(), "Problem creating Picture file!", Toast.LENGTH_LONG).show();
                  }
        		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        		
        	    Uri contentUri = Uri.fromFile(f);
        	    mediaScanIntent.setData(contentUri);
        	    this.sendBroadcast(mediaScanIntent);
        	}
        	catch(IOException e){
        		Toast.makeText(getApplicationContext(), "Problem creating Picture file!", Toast.LENGTH_LONG).show();
        	}
        }
    }
    
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {  
            photo = (Bitmap) data.getExtras().get("data"); 
            imageView.setImageBitmap(photo);
        }  
        else{
        	Toast.makeText(getApplicationContext(), "Photo Not Taken", Toast.LENGTH_LONG).show();
        }
    } 
    
    private File createImageFile() throws IOException {
        // Create an image file name
    	
    	File image = new File("");
        String imageFileName = JPEG_FILE_PREFIX + playerID;
        File albumDir = Player.getAlbumDir();
        
        
        if(!albumDir.exists()){
        	albumDir.mkdir();
        }
        try{
        	image = new File( albumDir + imageFileName + JPEG_FILE_SUFFIX);        	
        }
        catch(Exception e)
        {
        	Toast.makeText(getApplicationContext(), "Exception is " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return image;
    }
    
    
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    if (item.getItemId() == R.id.menu_home){
        	navigateHomePage();
            return true;
	    }
        else{
        	return super.onOptionsItemSelected(item);
        }
	    
	}
    
    @Override
	   public void onClick(View v) {
	      
    	// If add button was clicked
	       if (btn_CreatePlayer.isPressed()) {
	    	   if(validatePlayer()){
	    		   createPlayer();
		    	   	Intent intent = new Intent(this, PlayerHomeActivity.class);
		    	   	intent.putExtra("PLAYER_ID", playerID);
		       		startActivity(intent);
		       		this.finish();
	    	   }
	           	
	       } 
    }

	private boolean validatePlayer() {
		boolean valid = true;
		//validate String lengths
		if(playerText.length() < 1){
			valid = false;
			Toast.makeText(getApplicationContext(), "Player Name is a mandatory Field.", Toast.LENGTH_LONG).show();
		}
		if(teamText.length() < 1){
			valid = false;
			Toast.makeText(getApplicationContext(), "Player Team is a mandatory Field.", Toast.LENGTH_LONG).show();
		}
		if(playerText.length() > MAX_LENGTH || teamText.length() > MAX_LENGTH)
		{
			valid = false;
			Toast.makeText(getApplicationContext(), "Max Length for Player Name and Team is 15.", Toast.LENGTH_LONG).show();
		}
		
		return valid;
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getBaseContext(), MainActivity.class);
    	startActivity(intent);
    	this.finish();
	}
	
	
	
   
}
