package com.martaocio.farmergoody;

import java.util.GregorianCalendar;

public class GameSession {
	
	private int currentLevel=0;
	private int currentMoney=0;
	private int score =0;
	private Vehicle vehicleUsed=Vehicle.NONE;
	private GregorianCalendar lastModified=new GregorianCalendar();
	
	public GameSession(){
		this.currentLevel=1;
		this.currentMoney=0;
		this.score=0;
		this.vehicleUsed=Vehicle.NONE;
		this.lastModified=new GregorianCalendar();
	}
	
	public int getCurrentLevel() {
		return currentLevel;
	}
	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
		this.lastModified=new GregorianCalendar();
	}
	public int getCurrentMoney() {
		return currentMoney;
	}
	public void setCurrentMoney(int currentMoney) {
		this.currentMoney = currentMoney;
		this.lastModified=new GregorianCalendar();
	}
	public Vehicle getVehicleUsed() {
		return vehicleUsed;
		
	}
	public void setVehicleUsed(Vehicle vehicleUsed) {
		this.vehicleUsed = vehicleUsed;
		this.lastModified=new GregorianCalendar();
	}
	public GregorianCalendar getLastModified() {
		return lastModified;
	}
	public void setLastModified() {
		this.lastModified=new GregorianCalendar();;
	}
	
	public boolean isEmptySession(){
		
		if(currentLevel==1 && currentMoney==0 && vehicleUsed.equals(Vehicle.NONE)){
			return true;
		}
		return false;
	}
	
	public void flush(){
		this.currentLevel=1;
		this.currentMoney=0;
		this.score=0;
		this.vehicleUsed=Vehicle.NONE;
		this.lastModified=new GregorianCalendar();
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
