package com.mbs.my.bat.stats;

public class GameLog {
	
	
	private long ID;
	private long playerID;
	private int season;
	private int pa;
	private int atbat;
	private int hit;
	private int run;
	private int rbi;
	private int k;
	private int walk;
	private int sacrifice;
	private int hbp;
	private int doub;
	private int triple;
	private int homerun;
	

	//setters
	public void setID(long id) {
		this.ID = id;
	}

	public void setPlayerID(long playerID) {
		this.playerID = playerID;
	}

	public void setSeason(int season) {
		this.season = season;
	}
	
	public void setPA(int pa) {
		this.pa = pa;		
	}
	
	public void setAtBat(int atBat) {
		this.atbat = atBat;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}
	
	public void setRun(int run) {
		this.run = run;
	}
	
	public void setRBI(int rbi) {
		this.rbi = rbi;
	}
	
	public void setK(int k) {
		this.k = k;
	}
	
	public void setWalk(int walk) {
		this.walk = walk;
	}
	
	public void setSacrifice(int sac) {
		this.sacrifice = sac;
	}
	
	public void setHBP(int hbp) {
		this.hbp = hbp;
	}
	
	public void setDouble(int doub) {
		this.doub = doub;
	}
	
	public void setTriple(int triple) {
		this.triple = triple;
	}
	
	public void setHomerun(int homerun){
		this.homerun = homerun;
	}
	
	//getters
	public long getID() {
		return this.ID;
	}

	public long getPlayerID() {
		return this.playerID;
	}

	public int getSeason() {
		return this.season;
	}
	
	public int getPA() {
		return this.pa;
	}
	
	public int getAtBat() {
		return getPA() - getWalk() - getSacrifice() - getHBP();
	}

	public int getHit() {
		return this.hit;
	}
	
	public int getRun() {
		return this.run;
	}
	
	public int getRBI() {
		return this.rbi;
	}
	
	public int getK() {
		return this.k;
	}
	
	public int getWalk() {
		return this.walk;
	}
	
	public int getSacrifice() {
		return this.sacrifice;
	}
	
	public int getHBP(){
		return this.hbp;
	}
	
	public int getDouble() {
		return this.doub;
	}
	
	public int getTriple() {
		return this.triple;
	}
	
	public int getHomerun(){
		return this.homerun;
	}

	@Override
	public String toString() {
		
		return "[GameID]=" + getID() +
				"[Season]=" + getSeason() +
				"[PA]=" + getPA() +
				"[AB]=" + getAtBat() +
				"[Hit]=" + getHit() +
				"[Run]=" + getRun() +
				"[RBI]=" + getRBI() +
				"[K]=" + getK() +
				"[BB]=" + getWalk() +
				"[Sac]=" + getSacrifice() +
				"[HBP]=" + getHBP() +
				"[2B]=" + getDouble() +
				"[3B]=" + getTriple() +
				"[HR]=" + getHomerun();
				
	}
	
	
	
}
