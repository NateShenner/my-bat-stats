package com.mbs.my.bat.stats;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
 
public class BatStatsDBHelper 
{
	//player fields
    public static final String KEY_ROWID = "_id";
    public static final String KEY_PLAYERNAME = "name";
    public static final String KEY_PLAYERTEAM = "team";  
    public static final String KEY_PLAYERPIC = "playerpic";
    
    //player stats fields
    public static final String KEY_PLAYERSTATROWID = "_id";
    public static final String KEY_PLAYERID = "playerid";    
    public static final String KEY_SEASON = "season";
    public static final String KEY_PA = "plateappearances";
    public static final String KEY_ATBAT = "atbat";
    public static final String KEY_HIT = "hit";
    public static final String KEY_RUN= "run";
    public static final String KEY_RBI= "rbi";
    public static final String KEY_WALK= "walk";
    public static final String KEY_K = "strikeout";
    public static final String KEY_SAC= "sacrifice";
    public static final String KEY_HBP ="hitbypitch";
    public static final String KEY_DOUBLE= "double";
    public static final String KEY_TRIPLE= "triple";
    public static final String KEY_HR= "homerun";
    
    //game log fields
    public static final String KEY_GAMELOGROWID = "_id";
    public static final String KEY_LOGPLAYERID = "playerid";    
    public static final String KEY_LOGSEASON = "season";
    public static final String KEY_LOGPA = "plateappearances";
    public static final String KEY_LOGATBAT = "atbat";
    public static final String KEY_LOGHIT = "hit";
    public static final String KEY_LOGRUN= "run";
    public static final String KEY_LOGRBI= "rbi";
    public static final String KEY_LOGWALK= "walk";
    public static final String KEY_LOGK = "strikeout";
    public static final String KEY_LOGSAC= "sacrifice";
    public static final String KEY_LOGHBP = "hitbypitch";
    public static final String KEY_LOGDOUBLE= "double";
    public static final String KEY_LOGTRIPLE= "triple";
    public static final String KEY_LOGHR= "homerun";
        
    
    private static final String TAG = "BatStatsDBHelper";
 
    private static final String DATABASE_NAME = "batstats";
    private static final String TABLE_PLAYER = "player";
    private static final String TABLE_PLAYERSTATS = "playerstats";
    private static final String TABLE_GAMELOG = "gamelog";
    private static final int DATABASE_VERSION = 3;
 
    private static final String TABLE_CREATE_PLAYER =
        "create table player (_id integer primary key autoincrement, "
        + "name text not null, team text not null, playerpic text not null);";
 
    private static final String TABLE_CREATE_PLAYER_STATS =
    	"create table playerstats (_id integer primary key autoincrement, "
    		+ "atbat integer not null, plateappearances integer not null, hit integer not null, "
    		+ "run integer not null, rbi integer not null,strikeout integer not null, walk integer not null, "
    		+ "sacrifice integer not null, hitbypitch integer not null, double integer not null, "
    		+ "triple integer not null, homerun integer not null, " 
    		+ "season integer not null, playerID integer not null, " 
    		+ "foreign key (playerID) references player(_id));";
    
    private static final String TABLE_CREATE_GAME_LOG =
        	"create table gamelog (_id integer primary key autoincrement, "
        		+ "atbat integer not null, plateappearances integer not null, hit integer not null, "
        		+ "run integer not null, rbi integer not null, strikeout integer not null, walk integer not null, "
        		+ "sacrifice integer not null, hitbypitch integer not null, double integer not null, "
        		+ "triple integer not null, homerun integer not null, " 
        		+ "season integer not null, playerID integer not null, " 
        		+ "foreign key (playerID) references player(_id));";
    private final Context context; 
 
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;
 
    public BatStatsDBHelper(Context ctx) 
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
 
    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
 
        @Override
        public void onCreate(SQLiteDatabase db) 
        {
        	db.execSQL("PRAGMA foreign_keys=ON;");
            db.execSQL(TABLE_CREATE_PLAYER);
            db.execSQL(TABLE_CREATE_PLAYER_STATS);
            db.execSQL(TABLE_CREATE_GAME_LOG);
        }
 
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
        int newVersion) 
        {        	
            if (oldVersion == 1) {
                db.execSQL("ALTER TABLE " + TABLE_PLAYERSTATS + " ADD COLUMN " + KEY_RBI + " integer");
                db.execSQL("UPDATE " + TABLE_PLAYERSTATS  + " SET " +
                        KEY_RBI  + "=" + "0");
                db.execSQL("ALTER TABLE " + TABLE_PLAYERSTATS + " ADD COLUMN " + KEY_PA + " integer");
                db.execSQL("UPDATE " + TABLE_PLAYERSTATS  + " SET " +
                        KEY_PA  + "=" + "0");
                db.execSQL("ALTER TABLE " + TABLE_PLAYERSTATS + " ADD COLUMN " + KEY_K + " integer");
                db.execSQL("UPDATE " + TABLE_PLAYERSTATS  + " SET " +
                        KEY_K  + "=" + "0");
                db.execSQL("ALTER TABLE " + TABLE_PLAYERSTATS + " ADD COLUMN " + KEY_HBP + " integer");
                db.execSQL("UPDATE " + TABLE_PLAYERSTATS  + " SET " +
                        KEY_HBP  + "=" + "0");
                db.execSQL(TABLE_CREATE_GAME_LOG);
            }
            if(oldVersion == 2){
            	db.execSQL(TABLE_CREATE_GAME_LOG);
            	db.execSQL("ALTER TABLE " + TABLE_PLAYERSTATS + " ADD COLUMN " + KEY_PA + " integer");
                db.execSQL("UPDATE " + TABLE_PLAYERSTATS  + " SET " +
                        KEY_PA  + "=" + "0");
                db.execSQL("ALTER TABLE " + TABLE_PLAYERSTATS + " ADD COLUMN " + KEY_K + " integer");
                db.execSQL("UPDATE " + TABLE_PLAYERSTATS  + " SET " +
                        KEY_K  + "=" + "0");
                db.execSQL("ALTER TABLE " + TABLE_PLAYERSTATS + " ADD COLUMN " + KEY_HBP + " integer");
                db.execSQL("UPDATE " + TABLE_PLAYERSTATS  + " SET " +
                        KEY_HBP  + "=" + "0");
            }
        }
    }    
 
    //---opens the database---
    public BatStatsDBHelper open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }
 
    //---closes the database---    
    public void close() 
    {
        DBHelper.close();
    }
 
    //---insert a player into the database---
    public long insertPlayer(String name, String team) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_PLAYERNAME, name);
        initialValues.put(KEY_PLAYERTEAM, team);
        initialValues.put(KEY_PLAYERPIC, team);
        return db.insert(TABLE_PLAYER, null, initialValues);
    }
    
    //---insert a players stats into the database
    public long insertPlayerStats(long playerID, int season, int pa, int atbat, int hit, 
    		int run, int rbi, int walk,int k, int sac, int hbp, int doub, int triple, int homerun ){
    	ContentValues initialValues = new ContentValues();
    	initialValues.put(KEY_PLAYERID,  playerID);
    	initialValues.put(KEY_SEASON,  season);
    	initialValues.put(KEY_PA,  pa);
    	initialValues.put(KEY_ATBAT,  atbat);
    	initialValues.put(KEY_HIT,  hit);
    	initialValues.put(KEY_RUN,  run);
    	initialValues.put(KEY_RBI,  rbi);
    	initialValues.put(KEY_K,  k);
    	initialValues.put(KEY_WALK,  walk);
    	initialValues.put(KEY_SAC,  sac);
    	initialValues.put(KEY_HBP,  hbp);
    	initialValues.put(KEY_DOUBLE,  doub);
    	initialValues.put(KEY_TRIPLE,  triple);
    	initialValues.put(KEY_HR,  homerun);    	
    	return db.insert(TABLE_PLAYERSTATS, null, initialValues);
    }
 
    //---deletes a particular player---
    public boolean deletePlayer(long rowId) 
    {
        return db.delete(TABLE_PLAYER, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    //---deletes a particiular players stats---
    public boolean deletePlayerStats(long playerID){
    	return db.delete(TABLE_PLAYERSTATS, KEY_PLAYERID + "=" + playerID, null) > 0;
    }
 
    //---retrieves all the players---
    public Cursor getAllPlayers() 
    {
        return db.query(TABLE_PLAYER, new String[] {
        		KEY_ROWID, 
        		KEY_PLAYERNAME,
        		KEY_PLAYERTEAM,
        		KEY_PLAYERPIC}, 
                null, 
                null, 
                null, 
                null, 
                null);
    }
 
    //---retrieves a particular player by ID---
    public Player getPlayerByID(long rowId) throws SQLException 
    {
    	Player row = new Player();
        Cursor mCursor =
                db.query(true, TABLE_PLAYER, new String[] {
                		KEY_ROWID,
                		KEY_PLAYERNAME, 
                		KEY_PLAYERTEAM,
                		KEY_PLAYERPIC
                		}, 
                		KEY_ROWID + "=" + rowId, 
                		null,
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
            row = new Player();
			row.setID(mCursor.getLong(0));
			row.setName(mCursor.getString(1));
			row.setTeam(mCursor.getString(2));
			row.setPlayerPic(mCursor.getString(3));
        }
        return row;
    }
    
    public PlayerStats getPlayerStatsbyPlayerID(long playerID){
    	PlayerStats row = new PlayerStats();
        Cursor mCursor =
                db.query(true, TABLE_PLAYERSTATS, new String[] {
                		KEY_ROWID,
                		KEY_PLAYERID, 
                		KEY_SEASON,
                		KEY_PA,
                		KEY_ATBAT,
                		KEY_HIT,
                		KEY_RUN,
                		KEY_RBI,
                		KEY_K,
                		KEY_WALK,
                		KEY_SAC,
                		KEY_HBP,
                		KEY_DOUBLE,
                		KEY_TRIPLE,
                		KEY_HR
                		}, 
                		KEY_PLAYERID + "=" + playerID, 
                		null,
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
            row = new PlayerStats();
			row.setID(mCursor.getLong(0));
			row.setPlayerID(mCursor.getLong(1));
			row.setSeason(mCursor.getInt(2));
			row.setPA(mCursor.getInt(3));
			row.setAtBat(mCursor.getInt(4));
			row.setHit(mCursor.getInt(5));
			row.setRun(mCursor.getInt(6));
			row.setRBI(mCursor.getInt(7));
			row.setK(mCursor.getInt(8));
			row.setWalk(mCursor.getInt(9));
			row.setSacrifice(mCursor.getInt(10));
			row.setHBP(mCursor.getInt(11));
			row.setDouble(mCursor.getInt(12));
			row.setTriple(mCursor.getInt(13));
			row.setHomerun(mCursor.getInt(14));			
        }
        return row;
    }
 
    public PlayerStats getPlayerStatsbyPlayerIDandSeason(long playerID, int season){
    	PlayerStats row = new PlayerStats();
        Cursor mCursor =
                db.query(true, TABLE_PLAYERSTATS, new String[] {
                		KEY_ROWID,
                		KEY_PLAYERID, 
                		KEY_SEASON,
                		KEY_PA,
                		KEY_ATBAT,
                		KEY_HIT,
                		KEY_RUN,
                		KEY_RBI,
                		KEY_K,
                		KEY_WALK,
                		KEY_SAC,
                		KEY_HBP,
                		KEY_DOUBLE,
                		KEY_TRIPLE,
                		KEY_HR
                		}, 
                		KEY_PLAYERID + "=" + playerID + " AND " + KEY_SEASON + "=" + season , 
                		null,
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
            row = new PlayerStats();
			row.setID(mCursor.getLong(0));
			row.setPlayerID(mCursor.getLong(1));
			row.setSeason(mCursor.getInt(2));
			row.setPA(mCursor.getInt(3));
			row.setAtBat(mCursor.getInt(4));
			row.setHit(mCursor.getInt(5));
			row.setRun(mCursor.getInt(6));
			row.setRBI(mCursor.getInt(7));
			row.setK(mCursor.getInt(8));
			row.setWalk(mCursor.getInt(9));
			row.setSacrifice(mCursor.getInt(10));
			row.setHBP(mCursor.getInt(11));
			row.setDouble(mCursor.getInt(11));
			row.setTriple(mCursor.getInt(12));
			row.setHomerun(mCursor.getInt(13));			
        }
        return row;
    }
    
    //---updates a player---
    public boolean updatePlayer(long playerID, String name, 
    String team, String playerPic) 
    {
        ContentValues args = new ContentValues();
        args.put(KEY_PLAYERNAME, name);
        args.put(KEY_PLAYERTEAM, team);
        args.put(KEY_PLAYERPIC, playerPic);
        return db.update(TABLE_PLAYER, args, 
                         KEY_ROWID + "=" + playerID, null) > 0;
    }
    
    public boolean updatePlayerStats(long playerID, int season, int pa, int atbat, int hit, 
    		int run, int rbi, int k, int walk, int sac, int hbp, int doub, int triple, int homerun){
    	ContentValues args = new ContentValues();
    	
    	args.put(KEY_SEASON,  season);
    	args.put(KEY_PA, pa);
    	args.put(KEY_ATBAT,  atbat);
    	args.put(KEY_HIT,  hit);
    	args.put(KEY_RUN,  run);
    	args.put(KEY_RBI,  rbi);
    	args.put(KEY_K, k);
    	args.put(KEY_WALK,  walk);
    	args.put(KEY_SAC,  sac);
    	args.put(KEY_HBP, hbp);
    	args.put(KEY_DOUBLE,  doub);
    	args.put(KEY_TRIPLE,  triple);
    	args.put(KEY_HR,  homerun);    
    	
    	return db.update(TABLE_PLAYERSTATS, args, 
                KEY_PLAYERID + "=" + playerID + " AND " + KEY_SEASON + "=" + season, null) > 0;
    }
    
    /**
     * Getting all players
     * returns list of players
     * */
    public List<Player> getAllPlayersList(){
        List<Player> players = new ArrayList<Player> ();
 
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PLAYER;
 
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	Player curPlayer = new Player();
            	curPlayer.setID(cursor.getLong(0));
            	curPlayer.setName(cursor.getString(1));
            	curPlayer.setTeam(cursor.getString(2));
            	curPlayer.setPlayerPic(cursor.getString(3));
            	players.add(curPlayer);
            } while (cursor.moveToNext());
        }
 
        // closing connection
        cursor.close();
        db.close();
 
        // returning players
        return players;
    }
    
    /**
     * Getting all players
     * returns list of players
     * */
    public List<Long> getAllSeasonsForPlayer(long playerID){
        List<Long> seasons = new ArrayList<Long> ();
 
        // Select All Query
        String selectQuery = "SELECT distinct season FROM " + TABLE_PLAYERSTATS + " WHERE playerID=" + playerID;
 
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	seasons.add(cursor.getLong(0));
            	
            } while (cursor.moveToNext());
        }
 
        // closing connection
        cursor.close();
        db.close();
 
        // returning players
        return seasons;
    }
    
    /**
     * Getting all players
     * returns list of players
     * */
    public List<String> getAllSeasonsForPlayerAsString(long playerID){
        List<String> seasons = new ArrayList<String> ();
 
        // Select All Query
        String selectQuery = "SELECT distinct season FROM " + TABLE_PLAYERSTATS + " WHERE playerID=" + playerID;
 
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	seasons.add(cursor.getLong(0) + "");
            	
            } while (cursor.moveToNext());
        }
 
        // closing connection
        cursor.close();
        db.close();
 
        // returning players
        return seasons;
    }
}