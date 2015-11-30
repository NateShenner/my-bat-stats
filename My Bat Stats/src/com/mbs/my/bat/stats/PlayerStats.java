package com.mbs.my.bat.stats;

public class PlayerStats {
	
	
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
	
	public double calcOBP(){
		double curOBP = .000;
		if(getAtBat() != 0){
			curOBP = (double)(getHit() + getWalk() + getHBP()) / (double)(getAtBat() + getWalk() + getHBP() + getSacrifice());
		}
		return curOBP;
	}
	
	public double calcOPS(){
		double curOPS = .000;
		curOPS = calcOBP() + calcSlugging();
		return curOPS;
	}
	
	public int calcSingles(){
		int curSingles = 0;
		curSingles = getHit() - (getDouble() + getTriple() + getHomerun());
		return curSingles;
	}
	
	public double calcSlugging(){
		double curSlugging = .000;
		if(getAtBat() != 0){
			curSlugging =  (double)(calcSingles() + ( (2 * getDouble()) + (3 * getTriple()) + (4 * getHomerun()) )) / (double)getAtBat();
		}
		return curSlugging;
	}
	
	public double calcBattingAvg(){
		double curBAAVG = 0;
		if(getAtBat() != 0){
			curBAAVG = (double) getHit() / (double) getAtBat();
		}
		return curBAAVG;
	}

	
}
