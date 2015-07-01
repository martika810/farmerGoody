package com.martaocio.farmergoody.scenes;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.batch.SpriteBatch;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.SurfaceGestureDetector;
import org.andengine.opengl.texture.TextureOptions;

import com.badlogic.gdx.physics.box2d.Body;
import com.martaocio.farmergoody.MainGameActivity;
import com.martaocio.farmergoody.ResourceManager;
import com.martaocio.farmergoody.SceneManager;
import com.martaocio.farmergoody.SceneManager.SceneType;
import com.martaocio.farmergoody.customsprites.FuelDeposit;
import com.martaocio.farmergoody.customsprites.GameIndicatorPanel;
import com.martaocio.farmergoody.customsprites.Player;
import com.martaocio.farmergoody.domain.GameSession;
import com.martaocio.farmergoody.domain.UserState;
import com.martaocio.farmergoody.domain.Vehicle;
import com.martaocio.farmergoody.providers.AchievementHelper;
import com.martaocio.farmergoody.providers.LevelProvider;

public class GameScene extends AbstractGameScene {

	protected final int UNPAUSE = 3;
	private final int TAG_REWARD_ICON = 999;
	protected final int TAG_TOMAT_ICON = 998;
	protected final int TAG_SCORE_TEXT = 997;
	protected final int SCREEN_WIDTH = 800;
	protected final int SCREEN_HEIGHT = 480;
	public final static int PRICE_FILL_FUEL_DEPOSIT = -25;

	// private boolean isTurboRunning=false;

	private SurfaceGestureDetector gestureDetector;

	public Sprite turboButton;
	public Sprite pauseButton;
	public Sprite restartButton;

	public Sprite moneyBag100Indicator;

	public SpriteBatch tomatoSprite = new SpriteBatch(resourceManager.gameTexturesAtlas, 100, vbom);

	public boolean wasMoneyBag100IndicatorsShown, wasMoneyBag200IndicatorsShown = false;

	private boolean isTurboRunning = false;

	// create 2 new menuScene
	protected MenuScene levelPause;

	@Override
	public void createScene() {

		super.createScene();
		createLevel();

		createPauseScene();

	}

	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().loadMainMenu(engine);

	}

	public void createLevel() {

		super.createLevel();
		loadNextLevel();

		gameIndicatorController = new GameIndicatorPanel();
		gameIndicatorController.createGameIndicator(vbom);

		hud = new HUD();
		gameIndicatorController.attachGameIndicatorToHud(hud);
		attachButtonsToHud();

		this.setOnSceneTouchListener(this);
		this.setTouchAreaBindingOnActionMoveEnabled(true);
		this.setTouchAreaBindingOnActionDownEnabled(true);

		this.camera.setHUD(hud);

		createUpdateHandler();

	}

	public void createUpdateHandler() {
		this.registerUpdateHandler(new IUpdateHandler() {

			@Override
			public void onUpdate(float pSecondsElapsed) {
				boolean isBullAheadPlayer = bull.getX() > player.getX();
				boolean isPlayerAheadMiddleLevel = player.getX() > camera.getBoundsWidth();
				boolean wasNextLevelLoaded = mNextTMXTiledMap !=null;
				//CHECK END GAME
				if (isBullAheadPlayer) {
					if (UserState.getInstance().getSelectedSession().getNumberLifes() > 0) {
						updatePlayerNumberLifes(-1);
						bull.moveBack();
					} else {
						endGameByBullCatch();
					}

				}
				//CHECK IF NEXT LEVEL NEED TO BE LOADED
//				if((isPlayerAheadMiddleLevel) &&(!wasNextLevelLoaded)){
//					loadNextLevel();
//				}

			}

			@Override
			public void reset() {
				// TODO Auto-generated method stub

			}

		});

	}

	public void attachButtonsToHud() {

		hud.attachChild(jumpButton);

		hud.registerTouchArea(jumpButton);

		hud.attachChild(pauseButton);
		if (turboButton != null) {
			hud.attachChild(turboButton);
			hud.registerTouchArea(turboButton);
		}
		hud.attachChild(moneyBag100Indicator);
		hud.registerTouchArea(pauseButton);
		

	}

	public void createButtons() {
		moneyBag100Indicator = new Sprite(SCREEN_WIDTH / 2 - 128, SCREEN_HEIGHT / 2 - 128, 256, 256,
				ResourceManager.getInstance().moneyBag100, vbom);
		moneyBag100Indicator.setVisible(false);

		jumpButton = new Sprite(280, 380, 164, 70, ResourceManager.getInstance().jumpBtnTextute, vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
				if (pSceneTouchEvent.isActionUp() && isGameVisible) {
					this.registerEntityModifier(new SequenceEntityModifier(new ScaleModifier(0.3f, 1f, 1.2f), new ScaleModifier(0.3f, 1f,
							0.8f)));
					player.jump();
				}
				return true;
			};
		};
		Vehicle currentVehicle = UserState.getInstance().getSelectedSession().getVehicleUsed();
		boolean isPlayerWalking = currentVehicle.equals(Vehicle.NONE);
		if (!isPlayerWalking) {
			turboButton = new Sprite(450, 380, 70, 70, ResourceManager.getInstance().turboButtonTexture, vbom) {
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
					if (pSceneTouchEvent.isActionUp() && isGameVisible) {
						if (!isTurboRunning) {
							runTurbo();
						}

					}
					return true;
				}

			};
		}

		pauseButton = new Sprite(700, 400, 80, 64, ResourceManager.getInstance().pauseBtnTexture, vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
				if (pSceneTouchEvent.isActionUp() && isGameVisible) {
					this.registerEntityModifier(new ScaleModifier(0.5f, 1f, 1.2f));
					showPause();
				}

				return true;
			};
		};

		restartButton = new Sprite(700, 400, 80, 64, ResourceManager.getInstance().pauseBtnTexture, vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
				if (pSceneTouchEvent.isActionUp() && isGameVisible) {
					pause(false);
					restartButton.setVisible(false);
					pauseButton.setVisible(true);
				}
				return true;
			};
		};
		restartButton.setVisible(false);

	}

	private void pause(boolean shouldPause) {
		this.setIgnoreUpdate(shouldPause);

	}

	public void updatePlayerNumberLifes(int pointToUpdate) {
		GameSession selectedSession = UserState.getInstance().getSelectedSession();
		int currentNumberLife = selectedSession.getNumberLifes() + pointToUpdate;
		selectedSession.setNumberLifes(currentNumberLife);
		((GameIndicatorPanel) gameIndicatorController).getTextNumberLife().setText(currentNumberLife + "  ");

	}

	private void updateTurbo() {
		int currentUserPoints = UserState.getInstance().getSelectedSession().getFuelpoints();
		if (currentUserPoints > 10) {
			resourceManager.motoEngineSound.play();
			makePlayerTurbo();
			currentUserPoints = currentUserPoints - 10;
			UserState.getInstance().getSelectedSession().setFuelpoints(currentUserPoints);
			((GameIndicatorPanel) gameIndicatorController).getFuelDepositIcon().update(currentUserPoints);
			return;
		} else {
			int currentMoney = UserState.getInstance().getSelectedSession().getCurrentMoney();
			if (currentMoney >= (-PRICE_FILL_FUEL_DEPOSIT)) {
				UserState.getInstance().getSelectedSession().setFuelpoints((int) FuelDeposit.FULL);
				((GameIndicatorPanel) gameIndicatorController).getFuelDepositIcon().fill();
				updateCurrentMoney(PRICE_FILL_FUEL_DEPOSIT);

			}
		}

	}

	public void removeLife(final Body body) {

		activity.runOnUpdateThread(new Runnable() {

			@Override
			public void run() {

				for (int i = 0; i < lifes.size(); i++) {
					if (lifes.get(i).getUserData() == body) { // if the coin is
																// the
																// coin

						detachChild(lifes.get(i));

					}
				}
			}
		});

		// this allow us to do update in our game in the update thread
		// for removing body we have to do this bcos we dont want our game to
		// refers the
		// stuff that have been removed, so the body has to be removed from the
		// physic world
		activity.runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				mPhysicsWorld.destroyBody(body);

			}

		});
	}

	private void removeFence(final Body body) {
		activity.runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				mPhysicsWorld.destroyBody(body);

			}

		});
	}

	protected void createPauseScene() {

		levelPause = new MenuScene(camera);
		levelPause.setPosition(0, 0);
		//
		// Add menu item
		IMenuItem unpauseButton = new ScaleMenuItemDecorator(new SpriteMenuItem(UNPAUSE, resourceManager.playButton, vbom), 1.7f, 1.5f);
		IMenuItem homeButton = new ScaleMenuItemDecorator(new SpriteMenuItem(QUIT, resourceManager.quitButton, vbom), 0.6f, 0.8f);

		// create the background
		Sprite bg = new Sprite(0, 0, resourceManager.pauseBG, vbom);
		// bg.setCullingEnabled(true);
		//
		levelPause.attachChild(bg);
		levelPause.setBackgroundEnabled(false);// to see our scena throught the
		// background

		levelPause.addMenuItem(unpauseButton);
		levelPause.addMenuItem(homeButton);

		levelPause.buildAnimations();
		unpauseButton.setPosition(350, 190);
		homeButton.setPosition(650, 350);

		levelPause.setOnMenuItemClickListener(this);
	}

	public void updateGameIndicators(int points) {
		super.updateGameIndicators(points);

		if (percentage > 50 && percentage < 80 && !wasMoneyBag100IndicatorsShown) {
			showMoneyBag100Indicator();
			updateCurrentMoney(100);
		}
		if (percentage > 80 && !wasMoneyBag200IndicatorsShown) {
			showMoneyBag100Indicator();
			updateCurrentMoney(100);
		}

	}

	private void updateCurrentMoney(final int money) {
		UserState currentUserState = UserState.getInstance();

		int currentMoney = currentUserState.getSelectedSession().getCurrentMoney() + money;
		((GameIndicatorPanel) gameIndicatorController).getTextMoney().setText(currentMoney + "$");
		currentUserState.getSelectedSession().setCurrentMoney(currentMoney);
		if(((MainGameActivity)activity).isSignedIn()){
			AchievementHelper.getInstance(activity).checkAchievements(currentMoney);
			AchievementHelper.getInstance(activity).pushAchievements(activity);
		}
		currentUserState.saveToFile();
	}

	private void showPause() {
		// activity.runOnUiThread(new Runnable(){
		// @Override
		// public void run(){
		// activity.showAdvert(true);
		// }
		// });
		isGameVisible = false;
		showGameIndicators(false);
		this.hideControlButtons();
		this.setChildScene(levelPause, false, true, true);
	}

	@Override
	public void reset() {
		isGameVisible = true;
		super.reset();

		showGameIndicators(true);
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {

		switch (pMenuItem.getID()) {

		case RESTART:

			// restart
			centerCamera();
			if (UserState.getInstance().getSelectedSession().getCurrentLevel() == 1) {

			}
			restartLevel();

			break;
		case QUIT:
			// go to main menu
			quit();
			SceneManager.getInstance().loadMainMenu(engine);
			break;

		case KEEP_PLAYING:
			centerCamera();
			restartLevel();

			break;

		case UNPAUSE:

			reset();

			break;
		}

		return false;
	}

	public void showGameIndicators(boolean shouldShow) {
		jumpButton.setVisible(shouldShow);
		pauseButton.setVisible(shouldShow);
		if (turboButton != null) {
			turboButton.setVisible(shouldShow);
		}
		((GameIndicatorPanel) gameIndicatorController).show(shouldShow);

	}

	public void restartLevel() {
		int currentPoints = UserState.getInstance().getSelectedSession().getCurrentMoney();
		// AchievementHelper.getInstance(activity).checkAchievements(activity,
		// currentPoints);
		activity.submitLeaderBoard(currentPoints);

		centerCamera();
		showGameIndicators(false);
		this.setChildScene(levelFailed);

		// do some clean up
		this.mPhysicsWorld.clearForces();
		this.mPhysicsWorld.clearPhysicsConnectors();

		this.mPhysicsWorld.dispose();

		// some work with the scene
		this.clearTouchAreas();
		this.clearEntityModifiers();
		this.clearUpdateHandlers();
		this.dispose();

		SceneManager.getInstance().loadNextGameScene(engine);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	private void showMoneyBag100Indicator() {
		moneyBag100Indicator.registerEntityModifier(new SequenceEntityModifier(new DelayModifier(2f), new ScaleModifier(3, 1f, 1.2f),
				new ScaleModifier(3, 1.2f, 1f), new ScaleModifier(3, 1f, 1.2f), new ScaleModifier(3, 1.2f, 1f)) {
			@Override
			protected void onModifierStarted(IEntity pItem) {
				super.onModifierStarted(pItem);
				moneyBag100Indicator.setVisible(true);

			}

			@Override
			protected void onModifierFinished(IEntity pItem) {
				super.onModifierFinished(pItem);
				// tapPlayerExplanation.setBlendFunction(GL10.GL_SRC_ALPHA,
				// GL10.GL_ONE_MINUS_SRC_ALPHA);

				moneyBag100Indicator.setVisible(false);
				if (wasMoneyBag100IndicatorsShown) {
					wasMoneyBag200IndicatorsShown = true;
					moneyBag100Indicator.setIgnoreUpdate(true);
				}
				wasMoneyBag100IndicatorsShown = true;

			}
		});

	}

	private void makePlayerTurbo() {

		player.registerEntityModifier(new SequenceEntityModifier(
		// new MoveXModifier(1f, player.getX(), player.getX()+400)
				new DelayModifier(2f)) {
			@Override
			protected void onModifierStarted(IEntity pItem) {
				super.onModifierStarted(pItem);
				player.runTurbo();
				player.isTurbo = true;

			}

			@Override
			protected void onModifierFinished(IEntity pItem) {
				super.onModifierFinished(pItem);

				player.isTurbo = false;
				player.setRunning();

			}
		});

	}

	private void runTurbo() {
		Vehicle currentVehicle = UserState.getInstance().getSelectedSession().getVehicleUsed();
		boolean isPlayerWalking = currentVehicle.equals(Vehicle.NONE);
		if (!isPlayerWalking) {
			turboButton.registerEntityModifier(new SequenceEntityModifier(new ScaleModifier(0.1f, 1f, 1.2f), new ScaleModifier(0.1f, 1.2f,
					1f)) {
				@Override
				protected void onModifierStarted(IEntity pItem) {
					super.onModifierStarted(pItem);
					isTurboRunning = true;

				}

				@Override
				protected void onModifierFinished(IEntity pItem) {
					super.onModifierFinished(pItem);
					isTurboRunning = false;
				}
			});
			updateTurbo();
		}

	}

	public Player getPlayer() {
		return this.player;
	}
	
	private void loadNextLevel(){
		Runnable loadNextLevel=new Runnable(){

			@Override
			public void run() {
				TMXLoader tmxLoader = new TMXLoader(activity.getAssets(), activity.getTextureManager(),
						TextureOptions.BILINEAR_PREMULTIPLYALPHA, vbom);
				int nextLevel=UserState.getInstance().getSelectedSession().getCurrentLevel()+1;
				try {
					mNextTMXTiledMap=tmxLoader.loadFromAsset(LevelProvider.getTXMLevel(nextLevel));
				} catch (TMXLoadException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				tmxLayer=mNextTMXTiledMap.getTMXLayers().get(0);
				tmxLayer.setPosition(Player.CAMERA_WIDE_PER_LEVEL, 0);
				attachChild(tmxLayer);
				tmxLayer=mNextTMXTiledMap.getTMXLayers().get(1);
				tmxLayer.setPosition(Player.CAMERA_WIDE_PER_LEVEL, 0);
				attachChild(tmxLayer);
				createObjectTMXLayer(mNextTMXTiledMap,Player.CAMERA_WIDE_PER_LEVEL);
				camera.setBounds(0, 190, Player.CAMERA_WIDE_PER_LEVEL*2, 290);
			}};
		activity.runOnUiThread(loadNextLevel);
		
	}

}
