package com.martaocio.farmergoody;

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
import java.util.GregorianCalendar;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UserState {

	private int currentLevel = 1;
	private int currentAcumalatedPoints =0;
	private int bestScore=0;
	private static UserState INSTANCE = null;
	private static  Gson gson = null;
	private static String DATA_FILE = "fj.txt";
	private List<Vehicle> availableVehicles=new ArrayList<>();
	private List<GameSession> sessions=new ArrayList<>();

	public List<Vehicle> getAvailableVehicles() {
		return availableVehicles;
	}

	public List<GameSession> getSessions() {
		return sessions;
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
		this.currentLevel=level;
	}
	
	public GameSession getLastModifiedSession(){
		GameSession lastModifiedSession=null;
		GregorianCalendar lastDateCalendar=null;
		if(sessions.isEmpty()){
			return lastModifiedSession;//that returns NULL
		}
		for(GameSession session:sessions){
			if(lastModifiedSession==null) lastDateCalendar=session.getLastModified();
			if(lastDateCalendar.before(session.getLastModified())){
				lastDateCalendar=session.getLastModified();
				lastModifiedSession=session;
			}
		}
		return lastModifiedSession;
		
	}

	public void saveToFile() {

	
		File data_file = new File(DATA_FILE);
		FileOutputStream fos;
		try {
			fos = ResourceManager.getInstance().activity.openFileOutput(DATA_FILE, ResourceManager.getInstance().activity.MODE_PRIVATE);

			PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(fos)));
			String strUserState = gson.toJson(this);
			pw.print(strUserState);
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	private static UserState load() {
		if(gson==null){
			gson=new GsonBuilder().create();
		}
		FileInputStream fis;
		String strJson="";
		UserState userState=new UserState();
		try {
			fis = ResourceManager.getInstance().activity.openFileInput(DATA_FILE);

			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			strJson = br.readLine();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!strJson.isEmpty()){
			userState = gson.fromJson(strJson, UserState.class);
		}
		 
		return userState;

	}
	
	public void clear(){
		currentLevel=1;
		currentAcumalatedPoints=0;
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

}
