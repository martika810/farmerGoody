package com.martaocio.farmergoody;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

public class SceneManager {

	private static final SceneManager INSTANCE = new SceneManager();

	private BaseScene mainMenu;
	private BaseScene gameScene;
	private BaseScene splashScene;
	private BaseScene loadingScene;

	private BaseScene currentScene;
	private SceneType currentSceneType = SceneType.SCENE_SPLASH;

	private Engine engine = ResourceManager.getInstance().engine;

	public static SceneManager getInstance() {

		return INSTANCE;
	}

	public void setScene(BaseScene scene) {

		engine.setScene(scene);

		currentScene = scene;
		currentSceneType=scene.getSceneType();
	}
	
	public void setScene(SceneType sceneType){
		switch(sceneType){
		
		case SCENE_GAME:
			setScene(gameScene);
			break;
		case SCENE_LOADING:
			setScene(loadingScene);
			break;
		case SCENE_MENU:
			setScene(mainMenu);
			break;
		case SCENE_SPLASH:
			setScene(splashScene);
			break;
		default:
			break;
		}
	}
	
	public void createGameScene(final Engine mEngine){
		
		setScene(loadingScene);
		ResourceManager.getInstance().unloadMenuGraphics();
		mainMenu.disposeScene();
		
		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);
				ResourceManager.getInstance().loadGameResources();
				
				gameScene=new GameScene();
				
				setScene(gameScene);
				
			}
		}));
		
		
	}
	public void loadNextGameScene(){
		gameScene=new GameScene();
		setScene(gameScene);
	}
//	public void updateGameScene(){
//		gameScene=new GameScene();
//		currentScene=gameScene;
//		ResourceManager.getInstance().engine.setScene(gameScene);
//	}
	
	public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback){
		ResourceManager.getInstance().loadSplashResources();
		splashScene=new SplashScene();
		currentScene=splashScene;
		pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
	}
	
	public void disposeSplashScene(){
		ResourceManager.getInstance().unloadSplashResource();
		splashScene.disposeScene();
		splashScene=null;
	}
	
	//to set our main menu back
	public void setMainMenu(){
		ResourceManager.getInstance().engine.setScene(mainMenu);
		
	}
	
	public void createMainMenu(/*OnCreateSceneCallback pOnCreateSceneCallback*/){
		
		ResourceManager.getInstance().loadMenuResources();
		
		mainMenu=new MainMenu();
		loadingScene=new LoadingScene();
		
		setScene(mainMenu);
		
		disposeSplashScene();
		
		//pOnCreateSceneCallback.onCreateSceneFinished(mainMenu);
	}
	
	public void loadMainMenu(final Engine mEngine){
		setScene(loadingScene);
		ResourceManager.getInstance().unloadGameGraphics();
		//gameScene.disposeScene();
		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);
				ResourceManager.getInstance().loadMenuResources();
				mainMenu=new MainMenu();
				setScene(mainMenu);
				
			}
		}));
	}
	
//	public void updateMainMenu(){
//		mainMenu.update();
//		
//		currentScene=mainMenu;
//		
//	}
//	
	

	public enum SceneType {
		SCENE_MENU, SCENE_GAME , SCENE_SPLASH, SCENE_LOADING
	}

	public SceneType getSceneType() {
		return currentSceneType;
	}

}
