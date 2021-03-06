package com.martaocio.farmergoody;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.ui.IGameInterface.OnCreateSceneCallback;

import com.martaocio.farmergoody.domain.UserState;
import com.martaocio.farmergoody.scenes.BaseScene;
import com.martaocio.farmergoody.scenes.GameScene;
import com.martaocio.farmergoody.scenes.InstructionScene;
import com.martaocio.farmergoody.scenes.LoadingScene;
import com.martaocio.farmergoody.scenes.MainMenu;
import com.martaocio.farmergoody.scenes.SplashScene;
import com.martaocio.farmergoody.scenes.StoryScene;
import com.martaocio.farmergoody.scenes.TrainingGame;

public class SceneManager {

	private static final SceneManager INSTANCE = new SceneManager();

	private BaseScene mainMenu;
	private BaseScene gameScene;
	private BaseScene splashScene;
	private BaseScene loadingScene;
	private BaseScene instructionScene;
	private BaseScene storyScene;
	private BaseScene trainingScene;

	private BaseScene currentScene;
	private SceneType currentSceneType = SceneType.SCENE_SPLASH;

	private Engine engine = ResourceManager.getInstance().engine;

	public static SceneManager getInstance() {

		return INSTANCE;
	}

	public void setScene(BaseScene scene) {

		engine.setScene(scene);

		currentScene = scene;
		currentSceneType = scene.getSceneType();
	}

	public void setScene(SceneType sceneType) {
		switch (sceneType) {

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
		case SCENE_INSTRUCTION:
			setScene(instructionScene);
			break;
		case SCENE_STORY:
			setScene(storyScene);
			break;
		case SCENE_TRAINING:
			setScene(trainingScene);
			break;
		default:
			break;
		}
	}

	public void createGameScene(final Engine mEngine) {

		setScene(loadingScene);
		ResourceManager.getInstance().unloadMenuGraphics();
		mainMenu.disposeScene();

		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);
				ResourceManager.getInstance().loadGameResources();

				gameScene = new GameScene();

				setScene(gameScene);

			}
		}));

	}
	
	public void createTrainingGameScene(final Engine mEngine) {

		setScene(loadingScene);
		ResourceManager.getInstance().unloadStoryResource();
		//storyScene.disposeScene();

		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);
				ResourceManager.getInstance().loadTrainingGameResources();

				trainingScene = new TrainingGame();

				setScene(trainingScene);

			}
		}));

	}
	
	
	
	public void createGameFromStoryScene(final Engine mEngine) {

		setScene(loadingScene);
		ResourceManager.getInstance().unloadStoryResource();
		storyScene.disposeScene();

		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);
				ResourceManager.getInstance().loadGameResources();

				gameScene = new GameScene();

				setScene(gameScene);

			}
		}));

	}
	
	public void createGameFromTraining(final Engine mEngine) {

		setScene(loadingScene);
		ResourceManager.getInstance().unloadTrainingGameGraphics();
		//trainingScene.disposeScene();

		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);
				ResourceManager.getInstance().loadGameResources();

				gameScene = new GameScene();

				setScene(gameScene);

			}
		}));

	}

	public void loadNextGameScene(final Engine mEngine) {

		setScene(loadingScene);
		ResourceManager.getInstance().unloadGameGraphics();

		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);
				ResourceManager.getInstance().loadGameResources();
				
				gameScene = new GameScene();
				setScene(gameScene);
			}
		}));
		
	}
	
	public void loadNextGameSceneFromTraining(final Engine mEngine) {

		setScene(loadingScene);
		ResourceManager.getInstance().unloadTrainingGameGraphics();

		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);
				ResourceManager.getInstance().loadGameResources();
				
				gameScene = new GameScene();
				setScene(gameScene);
			}
		}));
		
	}
	
	public void reloadTrainingGameScene(final Engine mEngine) {

		setScene(loadingScene);

		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);
				
				trainingScene = new TrainingGame();
				setScene(trainingScene);
			}
		}));
		
	}

	// public void updateGameScene(){
	// gameScene=new GameScene();
	// currentScene=gameScene;
	// ResourceManager.getInstance().engine.setScene(gameScene);
	// }

	public void createSplashScene(OnCreateSceneCallback pOnCreateSceneCallback) {
		ResourceManager.getInstance().loadSplashResources();
		splashScene = new SplashScene();
		currentScene = splashScene;
		pOnCreateSceneCallback.onCreateSceneFinished(splashScene);
	}

	public void disposeSplashScene() {
		ResourceManager.getInstance().unloadSplashResource();
		splashScene.disposeScene();
		splashScene = null;
	}

	
	// to set our main menu back
	public void setMainMenu() {
		ResourceManager.getInstance().engine.setScene(mainMenu);

	}

	public void createMainMenu(/* OnCreateSceneCallback pOnCreateSceneCallback */) {

		ResourceManager.getInstance().loadMenuResources();

		mainMenu = new MainMenu();
		loadingScene = new LoadingScene();

		setScene(mainMenu);

		disposeSplashScene();

		// pOnCreateSceneCallback.onCreateSceneFinished(mainMenu);
	}

	public void createInstructions(final Engine mEngine) {

		setScene(loadingScene);
		ResourceManager.getInstance().unloadMenuGraphics();
		mainMenu.disposeScene();

		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);

				ResourceManager.getInstance().loadInstructionsResources();
				instructionScene = new InstructionScene();
				setScene(instructionScene);

			}
		}));

	}
	
	public void disposeInstructionScene() {
		ResourceManager.getInstance().unloadInstructionsResource();
		instructionScene.disposeScene();
		instructionScene = null;
	}
	
	public void createStory(final Engine mEngine) {

		setScene(loadingScene);
		ResourceManager.getInstance().unloadMenuGraphics();
		mainMenu.disposeScene();

		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);

				ResourceManager.getInstance().loadStoryResources();
				storyScene = new StoryScene();
				setScene(storyScene);

			}
		}));

	}
	
	public void disposeStoryScene() {
		ResourceManager.getInstance().unloadStoryResource();
		storyScene.disposeScene();
		storyScene = null;
	}


	public void loadMainMenu(final Engine mEngine) {
		setScene(loadingScene);
		boolean isTrainingLevel=(UserState.getInstance().getSelectedSession().getCurrentLevel()==0);
		if(isTrainingLevel){
			ResourceManager.getInstance().unloadTrainingGameGraphics();
		}else{
			ResourceManager.getInstance().unloadGameGraphics();
		}
		
		// gameScene.disposeScene();
		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);
				ResourceManager.getInstance().loadMenuResources();
				mainMenu = new MainMenu();
				setScene(mainMenu);

			}
		}));
	}
	
	public void loadMainMenuFromTraining(final Engine mEngine) {
		setScene(loadingScene);
		ResourceManager.getInstance().unloadTrainingGameGraphics();
		// gameScene.disposeScene();
		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);
				ResourceManager.getInstance().loadMenuResources();
				mainMenu = new MainMenu();
				setScene(mainMenu);

			}
		}));
	}

	public void loadMainMenuFromInstruction(final Engine mEngine) {
		setScene(loadingScene);
		ResourceManager.getInstance().unloadInstructionsResource();
		// gameScene.disposeScene();
		mEngine.registerUpdateHandler(new TimerHandler(0.1f, new ITimerCallback() {

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);
				ResourceManager.getInstance().loadMenuResources();
				mainMenu = new MainMenu();
				setScene(mainMenu);

			}
		}));
	}

	// public void updateMainMenu(){
	// mainMenu.update();
	//
	// currentScene=mainMenu;
	//
	// }
	//

	public enum SceneType {
		SCENE_MENU, SCENE_GAME, SCENE_SPLASH, SCENE_LOADING, SCENE_INSTRUCTION,SCENE_STORY,SCENE_TRAINING
	}

	public SceneType getSceneType() {
		return currentSceneType;
	}

}
