package com.martaocio.farmergoody.domain;

import android.content.Context;

public class AchievementBox {

	private boolean monocycleAchievement = false;
	private boolean firstMotoAchievement = false;
	private boolean scooterAchievement = false;
	private boolean coolMotoAchievement = false;
	private boolean antarcticaAchievement = false;
	private int numberStepsFirstMoto = 0;
	private int numberStepsScooter = 0;
	private int numberStepsCoolMoto = 0;


	public boolean isEmpty() {
		return !monocycleAchievement && !firstMotoAchievement && !scooterAchievement && !coolMotoAchievement && !antarcticaAchievement
				&&numberStepsFirstMoto ==0 && numberStepsScooter ==0 && numberStepsCoolMoto==0;
	}
	
	 public void saveLocal(Context ctx) {
		 // TODO
		 
	 }
	 
	 public void loadLocal(Context ctx) {
		 // TODO
		 
	 }
	 

	public boolean isMonocycleAchievement() {
		return monocycleAchievement;
	}

	public void setMonocycleAchievement(boolean monocycleAchievement) {
		this.monocycleAchievement = monocycleAchievement;
	}

	public boolean isFirstMotoAchievement() {
		return firstMotoAchievement;
	}

	public void setFirstMotoAchievement(boolean firstMotoAchievement) {
		this.firstMotoAchievement = firstMotoAchievement;
	}

	public boolean isScooterAchievement() {
		return scooterAchievement;
	}

	public void setScooterAchievement(boolean scooterAchievement) {
		this.scooterAchievement = scooterAchievement;
	}

	public boolean isCoolMotoAchievement() {
		return coolMotoAchievement;
	}

	public void setCoolMotoAchievement(boolean coolMotoAchievement) {
		this.coolMotoAchievement = coolMotoAchievement;
	}

	public boolean isAntarcticaAchievement() {
		return antarcticaAchievement;
	}

	public void setAntarcticaAchievement(boolean antarcticaAchievement) {
		this.antarcticaAchievement = antarcticaAchievement;
	}
	
	public int getNumberStepsFirstMoto() {
		return numberStepsFirstMoto;
	}

	public void setNumberStepsFirstMoto(int numberStepsFirstMoto) {
		this.numberStepsFirstMoto = numberStepsFirstMoto;
	}
	
	public void increaseNumberStepsFirstMoto() {
		this.numberStepsFirstMoto++;
	}

	public int getNumberStepsScooter() {
		return numberStepsScooter;
	}

	public void setNumberStepsScooter(int numberStepsScooter) {
		this.numberStepsScooter = numberStepsScooter;
	}
	
	public void increaseNumberStepsScooter() {
		this.numberStepsScooter ++;
	}

	public int getNumberStepsCoolMoto() {
		return numberStepsCoolMoto;
	}

	public void setNumberStepsCoolMoto(int numberStepsCoolMoto) {
		this.numberStepsCoolMoto = numberStepsCoolMoto;
	}

	public void increaseNumberStepsCoolMoto() {
		this.numberStepsCoolMoto++;
	}

}
