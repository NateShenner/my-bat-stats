package com.mbs.my.bat.stats;

import java.io.File;

import android.os.Environment;

public class Player {
	
	private long ID;
	private String name;
	private String team;
	private String playerpic;
	

	private static final String ALBUM_NAME = "MyBatStats";

	public void setID(long id) {
		this.ID = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTeam(String team) {
		this.team = team;
	}
	
	public void setPlayerPic(String playerPic){
		this.playerpic = playerPic;
	}

	public long getID() {
		return ID;
	}

	public String getName() {
		return name;
	}

	public String getTeam() {
		return team;
	}
	
	public String getPlayerPic(){
		return playerpic;
	}

	/**
	 * Override toString to display Person data in spinner
	 */
	@Override
	public String toString() {
		return getName() + " [" + getTeam() + "]";
	}
	
	
	
	public static File getAlbumDir() {
    	
		File storageDir = new File(
			    Environment.getExternalStoragePublicDirectory(
			        Environment.DIRECTORY_PICTURES
			    ), 
			    getAlbumName()
			);	
		return storageDir;
	}
    
    private static String getAlbumName(){
    	return ALBUM_NAME;
    }
	
	
	
}
