package com.martaocio.farmergoody;

import org.andengine.engine.Engine;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

public class SceneManager {

	private static final SceneManager INSTANCE = new SceneManager();

	private BaseScene mainMenu;
	private BaseScene gameScene;

	private BaseScene currentScene;
	private SceneType currentSceneType = SceneType.SCENE_MENU;

	private Engine engine = ResourceManager.getInstance().engine;

	public static SceneManager getInstance() {

		return INSTANCE;
	}

	public void setScene(BaseScene scene) {

		engine.setScene(scene);

		currentScene = scene;
		currentSceneType=scene.getSceneType();
	}
	
	public void createGameScene(){
		ResourceManager.getInstance().loadGameResources();
		
		gameScene=new GameScene();
		
		currentScene=gameScene;
		
		ResourceManager.getInstance().engine.setScene(gameScene);
	}
	
	//to set our main menu back
	public void setMainMenu(){
		ResourceManager.getInstance().engine.setScene(mainMenu);
		
	}
	
	public void createMainMenu(OnCreateSceneCallback pOnCreateSceneCallback){
		ResourceManager.getInstance().loadMenuResources();
		
		mainMenu=new MainMenu();
		
		currentScene=mainMenu;
		
		pOnCreateSceneCallback.onCreateSceneFinished(mainMenu);
	}
	
	public void updateMainMenu(){
		mainMenu=new MainMenu();
		
		currentScene=mainMenu;
		
	}
	
	

	public enum SceneType {
		SCENE_MENU, SCENE_GAME
	}

	public SceneType getSceneType() {
		return currentSceneType;
	}

}
