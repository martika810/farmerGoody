package com.martaocio.farmergoody.domain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.martaocio.farmergoody.ResourceManager;

public class UserState {

	private int currentLevel = 0;
	private int currentAcumalatedPoints = 0;
	private int bestScore = 0;
	private boolean canGoToAntarctica = false;
	private static UserState INSTANCE = null;
	private static Gson gson = null;
	private static String DATA_FILE = "fj.txt";
	private List<Vehicle> availableVehicles = new ArrayList<>();
	private List<GameSession> sessions = Arrays.asList(new GameSession[3]);
	private int indexSelectedSession = 0;
	
	public UserState(){
		this.currentLevel = 0;
		this.currentAcumalatedPoints = 0;
		this.bestScore = 0;
		this.availableVehicles = new ArrayList<>();
		this.sessions = new ArrayList<>();
		this.sessions.add(new GameSession());
		this.sessions.add(new GameSession());
		this.sessions.add(new GameSession());
		
		this.indexSelectedSession = 0;
		
	}
	

	public List<Vehicle> getAvailableVehicles() {
		return availableVehicles;
	}

	public List<GameSession> getSessions() {
		return sessions;
	}
	
	public void initSessions(){
		sessions=Arrays.asList(new GameSession[3]);
	}

	public static UserState getInstance() {
		if (INSTANCE == null) {

			INSTANCE = load();
		}
		return INSTANCE;
	}

	public int getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(int level) {
		this.currentLevel = level;
	}
	
	public void syncWithGoogleAchievements(AchievementBox achievementBox){
		availableVehicles.clear();
		if(achievementBox.isMonocycleAchievement()){
			availableVehicles.add(Vehicle.UNICYCLE);
		}
		if(achievementBox.isFirstMotoAchievement()){
			availableVehicles.add(Vehicle.BICYCLE);
		}
		if(achievementBox.isScooterAchievement()){
			availableVehicles.add(Vehicle.SCOOTER);
		}
		if(achievementBox.isCoolMotoAchievement()){
			availableVehicles.add(Vehicle.HARLEY);
		}
		if(achievementBox.isAntarcticaAchievement()){
			UserState.getInstance().setCanGoToAntarctica(true);
		}
		
		
	}

	public GameSession getLastModifiedSession() {
		GameSession lastModifiedSession = null;
		GregorianCalendar lastDateCalendar = null;
		if (sessions.isEmpty()) {
			return lastModifiedSession;// that returns NULL
		}
		for (GameSession session : sessions) {
			if (lastModifiedSession == null) {
				lastDateCalendar = session.getLastModified();
				lastModifiedSession = session;
			}
			if (lastDateCalendar.before(session.getLastModified())) {
				lastDateCalendar = session.getLastModified();
				lastModifiedSession = session;
			}
		}
		return lastModifiedSession;

	}
	
	public Vehicle getBestVehicle(){
		return Vehicle.getBestVehicle(availableVehicles);
	}

	public GameSession getSelectedSession() {
		if (sessions.isEmpty()) {
			sessions.add(new GameSession());
			indexSelectedSession=0;
		}
		
		return sessions.get(indexSelectedSession);
	}
	public void setSelectedSession(final int indexSelectedSession){
		this.indexSelectedSession=indexSelectedSession;
	}
	
	public void loadAchievementBox(AchievementBox achievementBox){
		// TODO
	}

	public void saveToFile() {

		
		FileOutputStream fos;
		try {
			fos = ResourceManager.getInstance().activity.openFileOutput(DATA_FILE, ResourceManager.getInstance().activity.MODE_PRIVATE);

			PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(fos)));
			String strUserState = gson.toJson(this);
			pw.print(strUserState);
			pw.close();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}

	}

	private static UserState load() {
		if (gson == null) {
			gson = new GsonBuilder().create();
		}
		FileInputStream fis;
		String strJson = "";
		UserState userState = new UserState();
		try {
			fis = ResourceManager.getInstance().activity.openFileInput(DATA_FILE);

			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			strJson = br.readLine();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!strJson.isEmpty()) {
			userState = gson.fromJson(strJson, UserState.class);
		}

		return userState;

	}

	public void clear() {
		currentLevel = 0;
		currentAcumalatedPoints = 0;
	}

	public int getBestScore() {
		return bestScore;
	}

	public void setBestScore(int bestScore) {
		this.bestScore = bestScore;
	}

	public int getCurrentAcumalatedPoints() {
		return currentAcumalatedPoints;
	}

	public void setCurrentAcumalatedPoints(int currentAcumalatedPoints) {
		this.currentAcumalatedPoints = currentAcumalatedPoints;
	}


	public boolean isCanGoToAntarctica() {
		return canGoToAntarctica;
	}


	public void setCanGoToAntarctica(boolean canGoToAntarctica) {
		this.canGoToAntarctica = canGoToAntarctica;
	}

}
