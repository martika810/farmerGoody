package com.martaocio.farmergoody.scenes;

import java.util.LinkedList;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.tmx.TMXLayer;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.TMXObject;
import org.andengine.extension.tmx.TMXObjectGroup;
import org.andengine.extension.tmx.TMXTiledMap;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.util.debug.Debug;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.martaocio.farmergoody.BodyGroups;
import com.martaocio.farmergoody.MainGameActivity;
import com.martaocio.farmergoody.RockPool;
import com.martaocio.farmergoody.SpriteTag;
import com.martaocio.farmergoody.SceneManager.SceneType;
import com.martaocio.farmergoody.customsprites.BasicGameIndicatorPanel;
import com.martaocio.farmergoody.customsprites.Bull;
import com.martaocio.farmergoody.customsprites.GameIndicatorPanel;
import com.martaocio.farmergoody.customsprites.Player;
import com.martaocio.farmergoody.customsprites.RockSprite;
import com.martaocio.farmergoody.domain.LevelType;
import com.martaocio.farmergoody.domain.UserState;
import com.martaocio.farmergoody.domain.Vehicle;
import com.martaocio.farmergoody.providers.LevelProvider;
import com.martaocio.farmergoody.providers.TomatoResourceHelper;
import com.martaocio.farmergoody.util.GameUtils;
import com.martaocio.farmergoody.util.Util;

public abstract class AbstractGameScene extends BaseScene implements IOnSceneTouchListener, IOnMenuItemClickListener{
	
	protected PhysicsWorld mPhysicsWorld;
	protected HUD hud;
	
	protected RockPool rockPool;
	
	/////////MENU ITEM OPTIONS///////////////
	protected final int RESTART = 0;
	protected final int QUIT = 1;
	protected final int KEEP_PLAYING = 2;


	/////////CHARACTERS////////////
	protected Player player;
	protected Bull bull;
	
	///////LEVEL STATE VARIABLES//////
	protected int score = 10;
	protected int levelTotalPoints;
	public int percentage = 0;
	protected LinkedList<Sprite> fencesBodies;
	protected LinkedList<Sprite> tomatos;
	protected LinkedList<Sprite> lifes;
	protected boolean isGameVisible = true;
	
	///////GAME INDICATOR AND CONTROLS//////////
	public BasicGameIndicatorPanel gameIndicatorController;
	public Sprite jumpButton;
	
	///////////////TMX OBJECTS///////////////////////
	protected TMXTiledMap mTMXTiledMap;
	protected TMXLayer tmxLayer;
	
	///////PANELS/////////////////
	protected MenuScene levelFailed;
	
	///VARIABLE TO CONTROL TOUCH EVENTS/////////////
	public boolean isDrawing = false;
	public boolean wasPlayerTap = false;
	public boolean wasSingleTap = false;
	public boolean wasSwiping = false;
	
	Rectangle[] rec = new Rectangle[250];
	private int i = 0;

	@Override
	public void createScene() {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				((MainGameActivity) activity).removeSignInGoogleButton();

			}
		});

		createPhysicsWorld();
		
		createLevelFailedScene();
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_GAME;
	}

	@Override
	public void disposeScene() {
		quit();
		
	}
	
	protected void createPhysicsWorld() {

		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);
		this.registerUpdateHandler(mPhysicsWorld);
		mPhysicsWorld.setContactListener(contactListener());
	}
	
	private ContactListener contactListener() {
		ContactListener contactListener = new ContactListener() {

			@Override
			public void beginContact(Contact contact) {
				Body a = contact.getFixtureA().getBody();
				Body b = contact.getFixtureB().getBody();
				UserState currentUserState = UserState.getInstance();
				// collision between player and ground
				if (a.getUserData() != null && b.getUserData() != null) {
					if (GameUtils.isCollisionBetween(a, b, SpriteTag.PLAYER, SpriteTag.GROUND)
							|| GameUtils.isCollisionBetween(a, b, SpriteTag.PLAYER, SpriteTag.LINE)) {

						player.canEat = false;
						player.canJump = true;
						player.setRunning();

					}

					if (GameUtils.isCollisionBetween(a, b, SpriteTag.GROUND, SpriteTag.BULL)) {
						bull.setRunning();
					}

					if (GameUtils.isCollisionBetween(a, b, SpriteTag.PLAYER, SpriteTag.BULL)) {

						int currentNumberLifes = currentUserState.getSelectedSession().getNumberLifes();
						if (currentNumberLifes > 0) {
							resourceManager.badSound.play();
							updatePlayerNumberLifes(-1);
							activity.runOnUpdateThread(new Runnable() {

								@Override
								public void run() {
									bull.moveBack();
								}
							});
							bull.setRunning();
						} else {
							currentUserState.setCurrentLevel(currentUserState.getCurrentLevel());
							currentUserState.saveToFile();
							endGameByBullCatch();
						}

					}
					if (GameUtils.isCollisionBetween(a, b, SpriteTag.PLAYER, SpriteTag.FENCE)) {
						player.setRunning();

					}
					if (GameUtils.isCollisionBetween(a, b, SpriteTag.PLAYER, SpriteTag.LINE)) {
						player.setRunning();

					}
					if (GameUtils.isCollisionBetween(a, b, SpriteTag.PLAYER, SpriteTag.LIFE)) {
						resourceManager.goodSound.play();
						updatePlayerNumberLifes(+1);
						if (a.getUserData().equals(SpriteTag.LIFE)) {
							removeLife(a);
						} else if (b.getUserData().equals(SpriteTag.LIFE)) {
							removeLife(b);
						}

					}
			
					if (GameUtils.isCollisionBetween(a, b, SpriteTag.PLAYER, SpriteTag.TOMATO5)) {
						// resourceManager.coinCollect.play();
						// update the score
						updateGameIndicators(5);

						// detect which body was colaided
						if (a.getUserData().equals(SpriteTag.TOMATO5)) {
							removeBody(a);
						} else if (b.getUserData().equals(SpriteTag.TOMATO5)) {
							removeBody(b);
						}
					}
					if (GameUtils.isCollisionBetween(a, b, SpriteTag.PLAYER, SpriteTag.TOMATO10)) {
						// resourceManager.coinCollect.play();
						// update the score
						updateGameIndicators(10);
						// detect which body was colaided
						if (a.getUserData().equals(SpriteTag.TOMATO10)) {
							removeBody(a);
						} else if (b.getUserData().equals(SpriteTag.TOMATO10)) {
							removeBody(b);
						}
					}
					if (GameUtils.isCollisionBetween(a, b, SpriteTag.PLAYER, SpriteTag.MINUSTOMATO5)) {
						// resourceManager.coinCollect.play();
						// update the score
						updateGameIndicators(-5);

						// detect which body was colaided
						if (a.getUserData().equals(SpriteTag.MINUSTOMATO5)) {
							removeBody(a);
						} else if (b.getUserData().equals(SpriteTag.MINUSTOMATO5)) {
							removeBody(b);
						}
					}
					if (GameUtils.isCollisionBetween(a, b, SpriteTag.PLAYER, SpriteTag.MINUSTOMATO10)) {
						// resourceManager.coinCollect.play();
						// update the score
						updateGameIndicators(-10);

						// detect which body was colaided
						if (a.getUserData().equals(SpriteTag.MINUSTOMATO10)) {
							removeBody(a);
						} else if (b.getUserData().equals(SpriteTag.MINUSTOMATO10)) {
							removeBody(b);
						}
					}
					if (GameUtils.isCollisionBetween(a, b, SpriteTag.PLAYER, SpriteTag.MINUSTOMATO20)) {
						// resourceManager.coinCollect.play();
						// update the score
						updateGameIndicators(-20);

						// detect which body was colaided
						if (a.getUserData().equals(SpriteTag.MINUSTOMATO20)) {
							removeBody(a);
						} else if (b.getUserData().equals(SpriteTag.MINUSTOMATO20)) {
							removeBody(b);
						}
					}

					

					if (GameUtils.isCollisionBetween(a, b, SpriteTag.PLAYER, SpriteTag.END)) {
						currentUserState.getSelectedSession().setCurrentLevel(currentUserState.getSelectedSession().getCurrentLevel() + 1);
						activity.runOnUpdateThread(new Runnable() {

							@Override
							public void run() {
								restartLevel();

							}
						});

					}

					if (score <= 0) {
						// currentUserState.setCurrentLevel(currentUserState.getCurrentLevel());
						currentUserState.saveToFile();
						endGameByRunOutPoints();
					}

				}

			}

			@Override
			public void endContact(Contact contact) {
				// TODO Auto-generated method stub

			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				// TODO Auto-generated method stub

			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				// TODO Auto-generated method stub

			}

		};

		return contactListener;

	}
	
	private void removeBody(final Body body) {

		for (int i = 0; i < tomatos.size(); i++) {
			if (tomatos.get(i).getUserData() == body) { // if the coin is the
														// coin
				// the player touchs,
				// then delete it
				detachChild(tomatos.get(i));

			}
		}

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
	
	public void endGameByBullCatch() {
		// player.setDead();
		resourceManager.vibrator.vibrate(250);
		SequenceEntityModifier rotationModifier = new SequenceEntityModifier(new MoveYModifier(0.5f, player.getY(), player.getY() - 200),
				new RotationModifier(0.5f, 0, 360), new MoveYModifier(1f, player.getY(), 1000)) {
			@Override
			protected void onModifierStarted(IEntity pItem) {
				super.onModifierStarted(pItem);
				// Your action after starting modifier
				player.setDead();
			}

			@Override
			protected void onModifierFinished(IEntity pItem) {
				super.onModifierFinished(pItem);
				showLevelFailed();
			}
		};
		player.registerEntityModifier(rotationModifier);

	}
	
	public void endGameByRunOutPoints() {
		player.setDead();
		resourceManager.vibrator.vibrate(250);
		SequenceEntityModifier rotationModifier = new SequenceEntityModifier(new MoveYModifier(0.5f, player.getY(), player.getY() - 200),
				new RotationModifier(0.5f, 0, 360), new MoveYModifier(1f, player.getY(), 1000)) {
			@Override
			protected void onModifierStarted(IEntity pItem) {
				super.onModifierStarted(pItem);
				// Your action after starting modifier
			}

			@Override
			protected void onModifierFinished(IEntity pItem) {
				super.onModifierFinished(pItem);
				showLevelFailed();
			}
		};
		player.registerEntityModifier(rotationModifier);

	}
	
	public void updateGameIndicators(int points) {
		score += points;
		gameIndicatorController.getTextScore().setText("" + score);
		if (points > 0) {
			resourceManager.goodSound.play();
		} else {
			resourceManager.badSound.play();
		}
		if(levelTotalPoints>0){
			percentage = (score * 100) / levelTotalPoints;
		}
		

		gameIndicatorController.getTomatoScoreIcon().update(percentage);

	}
	
	public void showLevelFailed() {
		if (!activity.FREE_ADS) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					//activity.addShareGoogleButton(UserState.getInstance().getSelectedSession());
					activity.showAdvert(true);
				}
			});
		}
		isGameVisible = false;
		showGameIndicators(false);
		hideControlButtons();
		this.setChildScene(levelFailed, false, true, true);

	}
	
	public void hideControlButtons() {
		
		this.jumpButton.setVisible(false);

	}
	
	public void createLevel() {
		RockPool.prepareRockPool(resourceManager.rockLineTexture, vbom, camera, this);
		rockPool = RockPool.getInstance();
		tomatos = new LinkedList<Sprite>();
		lifes = new LinkedList<Sprite>();
		fencesBodies = new LinkedList<Sprite>();
		
		try {
			TMXLoader tmxLoader = new TMXLoader(activity.getAssets(), activity.getTextureManager(),
					TextureOptions.BILINEAR_PREMULTIPLYALPHA, vbom);

			this.mTMXTiledMap = tmxLoader.loadFromAsset(LevelProvider.getTXMLevel(0));

		} catch (TMXLoadException e) {

			Debug.e(e);
			e.printStackTrace();
		}

		// Create all the objects from the TMXLayer
		createObjectTMXLayer();

		tmxLayer = this.mTMXTiledMap.getTMXLayers().get(0);// to get the first
		// layer

		attachChild(tmxLayer);

		tmxLayer = this.mTMXTiledMap.getTMXLayers().get(1);

		attachChild(tmxLayer);
		if (this.mTMXTiledMap.getTMXLayers().size() > 2) {
			tmxLayer = this.mTMXTiledMap.getTMXLayers().get(2);
			attachChild(tmxLayer);
		}
		int currentLevel = UserState.getInstance().getSelectedSession().getCurrentLevel();
		levelTotalPoints = LevelType.getLevelType(currentLevel).getTotalPoints();
		
		for (Sprite tomato : tomatos) {
			this.attachChild(tomato);

		}

		for (Sprite life : lifes) {
			this.attachChild(life);
		}
		
		kickoffPlayer();

		kickoffBull();
		
		createButtons();
		
//		setOnSceneTouchListener(this);
//		this.setTouchAreaBindingOnActionMoveEnabled(true);
//		this.setTouchAreaBindingOnActionDownEnabled(true);

	}
	
	protected void createObjectTMXLayer() {
		for (final TMXObjectGroup group : this.mTMXTiledMap.getTMXObjectGroups()) {// loop
			// over
			// the
			// objectgroups
			for (final TMXObject object : group.getTMXObjects()) {
				if (object.getName().equals(SpriteTag.GROUND)) {
					Rectangle rect = new Rectangle(object.getX(), object.getY(), object.getWidth(), object.getHeight(), vbom);

					FixtureDef groundFixtureDef = PhysicsFactory.createFixtureDef(0, 0, .2f);
					// BodyType to static if u dont want the body moves
					PhysicsFactory.createBoxBody(mPhysicsWorld, rect, BodyType.StaticBody, BodyGroups.GROUND_FIXTURE_DEF).setUserData(
							SpriteTag.GROUND);

					// store data in userData so it can be retrieved later on

					rect.setVisible(false);
					attachChild(rect);
				} else if (object.getName().equals(SpriteTag.FENCE)) {
					Rectangle rect = new Rectangle(object.getX(), object.getY(), object.getWidth(), object.getHeight(), vbom);

					FixtureDef spikeFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0f);
					Body fenceBody = PhysicsFactory.createBoxBody(mPhysicsWorld, rect, BodyType.StaticBody, BodyGroups.FENCE_FIXTURE_DEF);
					fenceBody.setUserData(SpriteTag.FENCE);
					//fencesBodies.add(fenceBody);

				}
				else if (object.getName().equals(SpriteTag.LIFE)) {
					Sprite lifeSprite = new Sprite(object.getX(), object.getY(), 50, 53, resourceManager.lifeIndicatorTexture, vbom);
					lifeSprite.setIgnoreUpdate(true);
					FixtureDef lifeFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0f);
					Body body = PhysicsFactory.createBoxBody(mPhysicsWorld, lifeSprite, BodyType.StaticBody, BodyGroups.LIFE_FIXTURE_DEF);
					body.setUserData(object.getName());
					lifes.add(lifeSprite);
					lifeSprite.setUserData(body);

				} else if (SpriteTag.isTomatoTag(object.getName()) || SpriteTag.isMinusTomatoTag(object.getName())) {
					Sprite tomatoType = new Sprite(object.getX(), object.getY(), 50, 50, TomatoResourceHelper.getTomatoResource(object
							.getName()), vbom);

					tomatoType.setCullingEnabled(true);
					tomatoType.setIgnoreUpdate(true);
					FixtureDef tomatoFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0f);
					Body body = PhysicsFactory.createBoxBody(mPhysicsWorld, tomatoType, BodyType.StaticBody, BodyGroups.TOMATO_FIXTURE_DEF);
					body.setUserData(object.getName());
					tomatos.add(tomatoType);
					tomatoType.setUserData(body);

				} else if (object.getName().equals(SpriteTag.END)) {

					Rectangle rect = new Rectangle(object.getX(), object.getY(), object.getWidth(), object.getHeight(), vbom);
					FixtureDef endLineDef = PhysicsFactory.createFixtureDef(0, 0, 0f);
					PhysicsFactory.createBoxBody(mPhysicsWorld, rect, BodyType.StaticBody, BodyGroups.END_FIXTURE_DEF).setUserData(
							SpriteTag.END);

				}

			}

		}

	}

	
	protected void kickoffPlayer() {

		player = createPlayer();
		// animate the player

		player.setRunning();

		this.attachChild(player);

	}
	
	protected Player createPlayer() {
		Player player = null;
		Vehicle vehicleSelected = UserState.getInstance().getSelectedSession().getVehicleUsed();
		if (vehicleSelected.equals(Vehicle.NONE)) {
			player = new Player(400, 150, 100, 110, vbom, camera, mPhysicsWorld) {

				@Override
				public void onDie() {
					// showLevelFailed();
				}
			};
		} else {
			player = new Player(400, 150, 130, 140, vbom, camera, mPhysicsWorld) {

				@Override
				public void onDie() {
					// showLevelFailed();
				}
			};
		}
		return player;
	}
	
	protected void kickoffBull() {
		// Attach bull
		bull = new Bull(50, 150, 150, 150, vbom, camera, mPhysicsWorld);

		// animate the player
		bull.setRunning();
		this.attachChild(bull);
	}
	
	protected void quit() {
		
		// do some clean up
		this.mPhysicsWorld.clearForces();
		this.mPhysicsWorld.clearPhysicsConnectors();
		this.mPhysicsWorld.dispose();

		// some work with the scene
		this.clearTouchAreas();
		this.clearEntityModifiers();
		this.clearUpdateHandlers();
		this.camera.getHUD().clearEntityModifiers();
		this.camera.getHUD().clearUpdateHandlers();
		// this.camera.getHUD().detachSelf();
		this.camera.setHUD(null);
		this.dispose();

		centerCamera();

	}

	protected void centerCamera() {
		this.camera.setChaseEntity(null);
		this.camera.setBoundsEnabled(false);
		resourceManager.camera.setCenter(400, 240);
	}
	
	protected void createLevelFailedScene() {

		levelFailed = new MenuScene(camera);
		levelFailed.setPosition(0, 0);
		//
		// Add menu item
		IMenuItem restartButton = new ScaleMenuItemDecorator(new SpriteMenuItem(RESTART, resourceManager.restartButton, vbom), 1.2f, 0.9f);
		IMenuItem quitButton = new ScaleMenuItemDecorator(new SpriteMenuItem(QUIT, resourceManager.quitButton, vbom), 1.2f, 0.9f);
		//
		// create the background
		Sprite bg = new Sprite(0, 0, resourceManager.failedBG, vbom);

		//
		levelFailed.attachChild(bg);
		// bg.setCullingEnabled(true);
		levelFailed.setBackgroundEnabled(false);// to see our scena throught the
		// background

		levelFailed.addMenuItem(restartButton);
		levelFailed.addMenuItem(quitButton);

		levelFailed.buildAnimations();
		restartButton.setPosition(300, 270);
		quitButton.setPosition(400, 270);

		levelFailed.setOnMenuItemClickListener(this);
	}
	
	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {

		boolean isPlayerArea = isPlayerArea(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
		if (!isDrawing && pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN && isPlayerArea) {
			player.registerEntityModifier(new SequenceEntityModifier(new ScaleModifier(0.2f, 1f, 1.2f), new ScaleModifier(0.2f, 1.2f, 1f)));
			player.runFaster(camera);
			wasPlayerTap = true;
			return true;
		}
		if (!wasPlayerTap && pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN && !isPlayerArea) {
			isDrawing = true;
			i = 0;
		}
		if (wasPlayerTap && pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
			wasPlayerTap = false;
			return true;
		}

		if (!wasPlayerTap && pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
			isDrawing = false;
		}
		if (isDrawing && !wasPlayerTap) {
			rec[i] = new Rectangle(pSceneTouchEvent.getX(), pSceneTouchEvent.getY(), 6f, 6f, vbom);
			if (i != 0) {

				drawLine(rec[i - 1].getX(), rec[i - 1].getY(), rec[i].getX(), rec[i].getY());

			}
			// this.attachChild(rec[i]);
			i++;
		}
		return true;
	}
	
	private boolean isPlayerArea(float f, float g) {
		if ((f > player.getX() + 3 && f < (player.getX() + player.getWidth() - 3))
				&& (g > player.getY() + 3 && (g < player.getY() + player.getHeight() - 3))) {
			return true;
		}

		return false;
	}
	
	private void drawLine(final float startX, final float startY, final float endX, final float endY) {

		RockSprite rockLine = rockPool.obtainPoolItem();
		rockLine.setPosition(startX, startY);
		rockLine.setIgnoreUpdate(true);

		Line bodyLine = new Line(startX, startY, endX, endY, vbom);
		bodyLine.setLineWidth(RockPool.LINE_BODY_WIDTH);

		FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(0.0f, 0.5f, 0.5f);
		Body physicBodyLine = PhysicsFactory.createLineBody(mPhysicsWorld, bodyLine, BodyGroups.LINE_FIXTURE_DEF);
		physicBodyLine.setUserData(SpriteTag.LINE);
		bodyLine.setVisible(false);
		rockLine.setBody(physicBodyLine);

		// LevelType
		// currentLevelType=LevelType.getLevelType(UserState.getInstance().getSelectedSession().getCurrentLevel());

		this.attachChild(bodyLine);
		double distance = Util.calculateDistance(startX, startY, endX, endY);
		if (distance > 25) {
			createRocksToFillGap(startX, endX, startY, endY);
		}

		this.attachChild(rockLine);

	}

	private void createRocksToFillGap(float startX, float endX, float startY, float endY) {
		double distance = Util.calculateDistance(startX, startY, endX, endY);
		float middlePointX = Util.calculateMidPointX(startX, endX);
		float middlePointY = Util.calculateMidPointY(startY, endY);
		if (distance > 30) {
			createRocksToFillGap(startX, middlePointX, startY, middlePointY);
			createRocksToFillGap(middlePointX, endX, middlePointY, endY);
		} else {

			RockSprite extraRock = rockPool.obtainPoolItem();
			extraRock.setPosition(middlePointX, middlePointY);
			extraRock.setIgnoreUpdate(true);
			this.attachChild(extraRock);
		}

	}
	
	public abstract void showGameIndicators(boolean shouldShow);
	public abstract void updatePlayerNumberLifes(int pointToUpdate);
	public abstract void removeLife(final Body body); 
	public abstract void restartLevel();
	public abstract  void attachButtonsToHud();
	public abstract void createUpdateHandler();
	public abstract void createButtons();

}
