package com.mbs.my.bat.stats;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class EditPlayerActivity extends Activity{

	private static final int CAMERA_REQUEST = 1888; 
	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";
	private static final int MAX_LENGTH = 15;
	
	private BatStatsDBHelper db;
    private ImageView imageView;
    private Bitmap photo;
    private Intent cameraIntent;
    private ImageView playerPic;
    
    //buttons
    private EditText playerText;
    private EditText teamText;
    
    private Player curPlayer;
    private Long playerID;
    private boolean photoUpdated;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_player);
		
		db = new BatStatsDBHelper(this);
        
        
        playerText = (EditText) findViewById(R.id.playerName);
        teamText = (EditText) findViewById(R.id.playerTeam);
        
        photoUpdated = false;
        
        playerPic = (ImageView) findViewById(R.id.imageView1);
        //load player
        Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    playerID = extras.getLong("PLAYER_ID");
		}
		db.open();
		curPlayer = db.getPlayerByID(playerID);
        
        //camera setup
        imageView = (ImageView)this.findViewById(R.id.imageView1);
                
        setPlayerDetails();
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        
        imageView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
               takePicture();
            }
        });
	}

	private void setPlayerDetails(){
		//update text fields
        playerText.setText(curPlayer.getName());
        teamText.setText(curPlayer.getTeam());
        
        //set picture
        File imgFile = null;
	    if(curPlayer.getPlayerPic() != null){
	    	imgFile = new  File(curPlayer.getPlayerPic());
	    }
		
	  	if(imgFile != null){

	  		try{
	  			 Bitmap playerBMP = BitmapFactory.decodeFile(imgFile.getPath());
	 		    playerPic.setImageBitmap(playerBMP);
	  		}
	  		catch(Exception e){
	  			Toast.makeText(getApplicationContext(), "Problem loading player Picture!!!", Toast.LENGTH_LONG).show();
	  		}
		   
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.player_edit_actions, menu);
		return true;
	}
	
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {  
	            photo = (Bitmap) data.getExtras().get("data"); 
	            imageView.setImageBitmap(photo);
	            photoUpdated = true;
	        }  
	        else{
	        	Toast.makeText(getApplicationContext(), "Photo Not Taken.", Toast.LENGTH_LONG).show();
	        }
	    } 

	
	
	 @Override
 	public boolean onOptionsItemSelected(MenuItem item) {
		 
 		// Handle item selection
 		if (item.getItemId() == R.id.action_save){
 			 if(validatePlayer()){
	    		   	updatePlayer();
		    	   	Intent intent = new Intent(this, PlayerHomeActivity.class);
		    	   	intent.putExtra("PLAYER_ID", playerID);
		       		startActivity(intent);
		       		this.finish();
		       		return true;
	    	   }
 		}
 		else if(item.getItemId() == android.R.id.home){
 			Intent intent = new Intent(this, PlayerHomeActivity.class);
 		   	intent.putExtra("PLAYER_ID", playerID);
 	   		startActivity(intent);
 	   		this.finish();
			return true;
		}
 		else if(item.getItemId() == R.id.action_camera){
 			takePicture();
 	        return true;
 		} 		 
 		else{
 			return super.onOptionsItemSelected(item);
 		}
		return true;
		
 	}
	
	private void takePicture(){
		cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
	    startActivityForResult(cameraIntent, CAMERA_REQUEST);
	}
	private void updatePlayer(){
		 //if photo exists, save to disk
		
        
    	if(photoUpdated){
    		File f = new File("");
        	try{
        		
        		File picFile = new File(curPlayer.getPlayerPic());
	            if(picFile.exists()){
	            	picFile.delete();
	            }
	            
        		f = createImageFile();        		
        		
        		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
        		
        		db.open();         		
        		db.updatePlayer(playerID, playerText.getText().toString(), teamText.getText().toString(), f.getPath());        		
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
    	else{
    		db.open();         		
    		db.updatePlayer(playerID, playerText.getText().toString(), teamText.getText().toString(), curPlayer.getPlayerPic());        		
    		db.close();
    		
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
	public void onBackPressed() {
		Intent intent = new Intent(this, PlayerHomeActivity.class);
	   	intent.putExtra("PLAYER_ID", playerID);
   		startActivity(intent);
   		this.finish();
	}

}
