package com.martaocio.farmergoody;

import java.util.GregorianCalendar;

public class GameSession {
	
	private int currentLevel=1;
	private int currentMoney=0;
	private Vehicle vehicleUsed=Vehicle.NONE;
	private GregorianCalendar lastModified=new GregorianCalendar();
	
	public int getCurrentLevel() {
		return currentLevel;
	}
	public void setCurrentLevel(int currentLevel) {
		this.currentLevel = currentLevel;
	}
	public int getCurrentMoney() {
		return currentMoney;
	}
	public void setCurrentMoney(int currentMoney) {
		this.currentMoney = currentMoney;
	}
	public Vehicle getVehicleUsed() {
		return vehicleUsed;
	}
	public void setVehicleUsed(Vehicle vehicleUsed) {
		this.vehicleUsed = vehicleUsed;
	}
	public GregorianCalendar getLastModified() {
		return lastModified;
	}
	public void setLastModified(GregorianCalendar lastModified) {
		this.lastModified = lastModified;
	}

}
