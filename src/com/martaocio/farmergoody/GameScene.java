package com.martaocio.farmergoody;

import java.util.LinkedList;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.PathModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Polygon;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnAreaTouchListener;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.ITouchArea;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.TextMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;

import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.PhysicsConnector;
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
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.debug.Debug;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.IModifier.IModifierListener;

import android.content.Context;
import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.martaocio.farmergoody.SceneManager.SceneType;

public class GameScene extends BaseScene implements IOnSceneTouchListener, IOnMenuItemClickListener {

	private final int RESTART = 0;
	private final int QUIT = 1;
	private final int KEEP_PLAYING = 2;
	private final int UNPAUSE = 3;
	private final int TAG_REWARD_ICON = 999;
	private final int TAG_TOMAT_ICON = 998;
	private final int TAG_SCORE_TEXT = 997;

	private TMXTiledMap mTMXTiledMap;
	private TMXLayer tmxLayer;

	private PhysicsWorld mPhysicsWorld;

	private LinkedList<Sprite> vegetables;
	private LinkedList<Body> fencesBodies;
	private LinkedList<Sprite> tomatos;
	Rectangle[] rec = new Rectangle[250];
	private int i = 0;
	// private LinkedList<Stair> stairs;

	private LinkedList<Sprite> pathScoreIndicators;

	public Text textScore;

	public Text textLevel;
	public boolean isDrawing = false;

	public TomatoScorer tomatoScoreIcon;
	public Sprite levelIcon;
	public Sprite rightPathScoreIcon;
	public Sprite jumpButton;
	public Sprite pauseButton;
	public Sprite restartButton;
	public Sprite leftButton;
	public Sprite rightButton;
	public int percentage=0;

	public Sprite title;

	private Player player;
	private Bull bull;

	private int score = 10;
	private int levelTotalPoints=300;
	// create 2 new menuScene
	private MenuScene levelFailed, levelCleared, levelPause,levelNoMoney;

	@Override
	public void createScene() {
		createPhysicsWorld();
		createLevel(UserState.getInstance().getSelectedSession().getCurrentLevel());
		createLevelClearedScene();
		createLevelFailedScene();
		createLevelNoMoneyScene();
		createPauseScene();



	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub

	}

	@Override
	public SceneType getSceneType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub

	}

	private void createLevel(int levelNumber) {

		// create the tex for the score
		score = 10;
		int currentLevel=UserState.getInstance().getSelectedSession().getCurrentLevel();
		levelTotalPoints=LevelType.getLevelType(currentLevel).getTotalPoints();
		tomatoScoreIcon =new TomatoScorer(20, 100, 200, 80, vbom); 
		levelIcon = new Sprite(20, 100, 70, 70, ResourceManager.getInstance().levelIcon, vbom);
		levelIcon.setCullingEnabled(true);
		title = new Sprite(693, 5, 107, 50, ResourceManager.getInstance().title, vbom);
		title.setCullingEnabled(true);
		tomatoScoreIcon.setPosition(20, 20);
		textScore = new Text(40, 50, this.resourceManager.font, score + "    ", new TextOptions(HorizontalAlign.CENTER), vbom);
	//	textBestScore = new Text(20, 125, this.resourceManager.font, "    " + UserState.getInstance().getSelectedSession().getScore() + "   ",
		//		new TextOptions(HorizontalAlign.CENTER), vbom);
		textLevel = new Text(20, 120, this.resourceManager.font, "#" + levelNumber + "    ",new TextOptions(HorizontalAlign.CENTER), vbom);
		tomatoScoreIcon.setTag(TAG_TOMAT_ICON);
		textScore.setTag(TAG_SCORE_TEXT);

		createButtons();

		tomatos = new LinkedList<Sprite>();
		fencesBodies = new LinkedList<Body>();
		pathScoreIndicators = new LinkedList<Sprite>();
		try {
			final TMXLoader tmxLoader = new TMXLoader(activity.getAssets(), activity.getTextureManager(),
					TextureOptions.NEAREST_PREMULTIPLYALPHA, vbom);

			this.mTMXTiledMap = tmxLoader.loadFromAsset(LevelProvider.getTXMLevel(levelNumber));
		} catch (TMXLoadException e) {

			Debug.e(e);
			e.printStackTrace();
		}

		// Create all the objects from the TMXLayer
		for (final TMXObjectGroup group : this.mTMXTiledMap.getTMXObjectGroups()) {// loop
																					// over
																					// the
																					// objectgroups
			for (final TMXObject object : group.getTMXObjects()) {
				if (object.getName().equals(SpriteTag.GROUND)) {
					Rectangle rect = new Rectangle(object.getX(), object.getY(), object.getWidth(), object.getHeight(), vbom);

					FixtureDef groundFixtureDef = PhysicsFactory.createFixtureDef(0, 0, .2f);
					// BodyType to static if u dont want the body moves
					PhysicsFactory.createBoxBody(mPhysicsWorld, rect, BodyType.StaticBody, groundFixtureDef).setUserData(SpriteTag.GROUND);

					// store data in userData so it can be retrieved later on

					rect.setVisible(false);
					attachChild(rect);
				}else if (object.getName().equals(SpriteTag.FENCE)) {
					Rectangle rect = new Rectangle(object.getX(), object.getY(), object.getWidth(), object.getHeight(), vbom);

					FixtureDef spikeFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0f);
					Body fenceBody = PhysicsFactory.createBoxBody(mPhysicsWorld, rect, BodyType.StaticBody, spikeFixtureDef);
					fenceBody.setUserData(SpriteTag.FENCE);
					fencesBodies.add(fenceBody);
				} else if (object.getName().equals(SpriteTag.CORRECT)) {
					Rectangle rect = new Rectangle(object.getX(), object.getY(), object.getWidth(), object.getHeight(), vbom);
					FixtureDef correctFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0f);
					Body correctBody = PhysicsFactory.createBoxBody(mPhysicsWorld, rect, BodyType.StaticBody, correctFixtureDef);
					correctBody.setUserData(SpriteTag.CORRECT);

				} else if (object.getName().equals(SpriteTag.WRONG)) {
					Rectangle rect = new Rectangle(object.getX(), object.getY(), object.getWidth(), object.getHeight(), vbom);
					FixtureDef wrongFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0f);
					Body wrongBody = PhysicsFactory.createBoxBody(mPhysicsWorld, rect, BodyType.StaticBody, wrongFixtureDef);
					wrongBody.setUserData(SpriteTag.WRONG);

				}

				else if (SpriteTag.isTomatoTag(object.getName()) || SpriteTag.isMinusTomatoTag(object.getName())) {
					Sprite tomatoType = new Sprite(object.getX(), object.getY(), 50, 50, TomatoResourceHelper.getTomatoResource(object
							.getName()), vbom);
					tomatoType.setCullingEnabled(true);
					FixtureDef tomatoFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0f);
					Body body = PhysicsFactory.createBoxBody(mPhysicsWorld, tomatoType, BodyType.StaticBody, tomatoFixtureDef);
					body.setUserData(object.getName());
					tomatos.add(tomatoType);
					tomatoType.setUserData(body);
				} else if (object.getName().equals(SpriteTag.END)) {

					Rectangle rect = new Rectangle(object.getX(), object.getY(), object.getWidth(), object.getHeight(), vbom);
					FixtureDef endLineDef = PhysicsFactory.createFixtureDef(0, 0, 0f);
					PhysicsFactory.createBoxBody(mPhysicsWorld, rect, BodyType.StaticBody, endLineDef).setUserData(SpriteTag.END);

				}

			}

		}

		// /////////////////////////////////////////
		tmxLayer = this.mTMXTiledMap.getTMXLayers().get(0);// to get the first
															// layer

		attachChild(tmxLayer);

		tmxLayer = this.mTMXTiledMap.getTMXLayers().get(1);

		attachChild(tmxLayer);
		if (this.mTMXTiledMap.getTMXLayers().size() > 2) {
			tmxLayer = this.mTMXTiledMap.getTMXLayers().get(2);
			attachChild(tmxLayer);
		}

		for (Sprite tomato : tomatos) {
			this.attachChild(tomato);

		}

		// Attach player
		player = new Player(400, 150, 130, 140, vbom, camera, mPhysicsWorld) {

			@Override
			public void onDie() {
				// showLevelFailed();
			}
		};
		// animate the player
		
		player.setRunning();
		
		this.attachChild(player);

		// Attach bull
		bull = new Bull(50, 150, 150, 150, vbom, camera, mPhysicsWorld);

		// animate the player
		bull.setRunning();
		this.attachChild(bull);

		// that heads up the display
		HUD hud = new HUD();

		hud.attachChild(levelIcon);
		hud.attachChild(textLevel);
		hud.attachChild(tomatoScoreIcon);
		hud.attachChild(textScore);
		
//		hud.attachChild(textBestScore);
		hud.attachChild(title);
		hud.attachChild(jumpButton);
		hud.attachChild(pauseButton);
		hud.attachChild(restartButton);

		hud.registerTouchArea(jumpButton);
		hud.registerTouchArea(pauseButton);
		hud.registerTouchArea(restartButton);

		setOnSceneTouchListener(this);
		this.setTouchAreaBindingOnActionMoveEnabled(true);
		this.setTouchAreaBindingOnActionDownEnabled(true);

		this.camera.setHUD(hud);

		
		this.registerUpdateHandler(new IUpdateHandler() {

			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(bull.getX()>player.getX()){
					endGameByBullCatch();
				}

			}

			@Override
			public void reset() {
				// TODO Auto-generated method stub

			}

		});
	}

	private void createButtons() {
		jumpButton = new Sprite(320, 380, 164, 70, ResourceManager.getInstance().jumpBtnTextute, vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
				if (pSceneTouchEvent.isActionUp()) {
					this.registerEntityModifier(new SequenceEntityModifier(new ScaleModifier(0.3f, 1f, 1.2f),
							new ScaleModifier(0.3f, 1f, 0.8f)));
					player.jump();
				}
				return true;
			};
		};

		
		pauseButton = new Sprite(700, 400, 80, 64, ResourceManager.getInstance().pauseBtnTexture, vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
				this.registerEntityModifier(new ScaleModifier(0.5f, 1f, 1.2f));
				showPause();

				return true;
			};
		};

		restartButton = new Sprite(700, 400, 80, 64, ResourceManager.getInstance().pauseBtnTexture, vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
				pause(false);
				restartButton.setVisible(false);
				pauseButton.setVisible(true);
				return true;
			};
		};
		restartButton.setVisible(false);

	}

	private void pause(boolean shouldPause) {
		this.setIgnoreUpdate(shouldPause);

	}

	private void createPhysicsWorld() {

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
						if (currentUserState.getBestScore() < score) {
							currentUserState.setBestScore(score);
						}
						currentUserState.setCurrentLevel(currentUserState.getCurrentLevel());
						currentUserState.saveToFile();
						endGameByBullCatch();
						
					}
					if (GameUtils.isCollisionBetween(a, b, SpriteTag.PLAYER, SpriteTag.FENCE)) {
							player.setRunning();
						

					}
					if (GameUtils.isCollisionBetween(a, b, SpriteTag.PLAYER, SpriteTag.LINE)) {
						player.setRunning();
					

				}
					if (GameUtils.isCollisionBetween(a, b, SpriteTag.BULL, SpriteTag.FENCE)) {

						if (a.getUserData().equals(SpriteTag.FENCE)) {
							removeFence(a);
						} else if (b.getUserData().equals(SpriteTag.FENCE)) {
							removeFence(b);

						}
					}
					if (GameUtils.isCollisionBetween(a, b, SpriteTag.BULL, SpriteTag.LINE)) {

						if (a.getUserData().equals(SpriteTag.LINE)) {
							removeFence(a);
						} else if (b.getUserData().equals(SpriteTag.LINE)) {
							removeFence(b);

						}
					}

					if (GameUtils.isCollisionBetween(a, b, SpriteTag.PLAYER, SpriteTag.TOMATO5)) {
						// resourceManager.coinCollect.play();
						// update the score
						score += 5;
						textScore.setText("" + score);
						resourceManager.eatTomato.play();
						percentage=(score*100)/levelTotalPoints;
						tomatoScoreIcon.update(percentage);

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
						score += 10;
						textScore.setText("" + score);
						resourceManager.eatTomato.play();
						percentage=(score/levelTotalPoints)*100;
						tomatoScoreIcon.update(percentage);
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
						score -= 5;
						textScore.setText("" + score);
						percentage=(score/levelTotalPoints)*100;
						tomatoScoreIcon.update(percentage);

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
						score -= 10;
						textScore.setText("" + score);
						percentage=(score/levelTotalPoints)*100;
						tomatoScoreIcon.update(percentage);

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
						score -= 20;
						textScore.setText("" + score);
						percentage=(score/levelTotalPoints)*100;
						tomatoScoreIcon.update(percentage);

						// detect which body was colaided
						if (a.getUserData().equals(SpriteTag.MINUSTOMATO20)) {
							removeBody(a);
						} else if (b.getUserData().equals(SpriteTag.MINUSTOMATO20)) {
							removeBody(b);
						}
					}

					if (a.getUserData().equals(SpriteTag.BULL) && GameUtils.isBodyTomato(b) || b.getUserData().equals(SpriteTag.BULL)
							&& GameUtils.isBodyTomato(a)) {
						
						if (GameUtils.isBodyTomato(a)) {

							removeBody(a);
						} else if (GameUtils.isBodyTomato(b)) {
							removeBody(b);
						}
					}

					if (GameUtils.isCollisionBetween(a, b, SpriteTag.PLAYER, SpriteTag.END)) {
						// show success screen
//						if (currentUserState.getBestScore() < score) {
//							currentUserState.setBestScore(score);
//						}
						if(percentage<50){
							showLevelNoMoney();
							
						}else{
							currentUserState.getSelectedSession().setCurrentLevel(currentUserState.getSelectedSession().getCurrentLevel()+1);
							int currentMoney=currentUserState.getSelectedSession().getCurrentMoney();
							currentUserState.getSelectedSession().setCurrentMoney(currentMoney+100);
							currentUserState.saveToFile();
							showLevelCleared();
						}
						
						

					}

					if (score <= 0) {
						//currentUserState.setCurrentLevel(currentUserState.getCurrentLevel());
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

	private void removeFence(final Body body) {
		activity.runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				mPhysicsWorld.destroyBody(body);

			}

		});
	}
	
	public void endGameByBullCatch() {
	//	player.setDead();
		resourceManager.vibrator.vibrate(250);
		SequenceEntityModifier rotationModifier=new SequenceEntityModifier(
				new MoveYModifier(0.5f, player.getY(), player.getY()-200) ,
				new RotationModifier(0.5f,0,360),
				new MoveYModifier(1f, player.getY(), 1000)
				){
			 @Override
		        protected void onModifierStarted(IEntity pItem)
		        {
		                super.onModifierStarted(pItem);
		                // Your action after starting modifier
		                player.setDead();
		        }
		       
		        @Override
		        protected void onModifierFinished(IEntity pItem)
		        {
		                super.onModifierFinished(pItem);
		                showLevelFailed();
		        }
		};
		player.registerEntityModifier(rotationModifier);
		
		
	}
	
	public void endGameByRunOutPoints() {
		player.setDead();
		resourceManager.vibrator.vibrate(250);
		SequenceEntityModifier rotationModifier=new SequenceEntityModifier(
				new MoveYModifier(0.5f, player.getY(), player.getY()-200) ,
				new RotationModifier(0.5f,0,360),
				new MoveYModifier(1f, player.getY(), 1000)){
			 @Override
		        protected void onModifierStarted(IEntity pItem)
		        {
		                super.onModifierStarted(pItem);
		                // Your action after starting modifier
		        }
		       
		        @Override
		        protected void onModifierFinished(IEntity pItem)
		        {
		                super.onModifierFinished(pItem);
		                showLevelFailed();
		        }
		};
		player.registerEntityModifier(rotationModifier);
		
		
	}

	private void fadeOutBody(final Body body) {

		for (int i = 0; i < vegetables.size(); i++) {
			if (vegetables.get(i).getUserData() == body) { // if the coin is the
															// coin
				// the player touchs,
				// then delete it
				// detachChild(vegetables.get(i));
				((Tomato) vegetables.get(i)).smash();
				vegetables.remove(i);

				// detachChild(vegetables.get(i));

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

	private void createLevelFailedScene() {

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
		levelFailed.setBackgroundEnabled(false);// to see our scena throught the
		// background

		levelFailed.addMenuItem(restartButton);
		levelFailed.addMenuItem(quitButton);

		levelFailed.buildAnimations();
		restartButton.setPosition(470, 270);
		quitButton.setPosition(470, 360);

		levelFailed.setOnMenuItemClickListener(this);
	}
	
	private void createLevelNoMoneyScene() {

		levelNoMoney = new MenuScene(camera);
		levelNoMoney.setPosition(0, 0);
		//
		// Add menu item
		IMenuItem restartButton = new ScaleMenuItemDecorator(new SpriteMenuItem(RESTART, resourceManager.restartButton, vbom), 1.2f, 0.9f);
		IMenuItem quitButton = new ScaleMenuItemDecorator(new SpriteMenuItem(QUIT, resourceManager.quitButton, vbom), 1.2f, 0.9f);
		//
		// create the background
		Sprite bg = new Sprite(0, 0, resourceManager.noMoneyBG, vbom);
		
		//
		levelNoMoney.attachChild(bg);
		levelNoMoney.setBackgroundEnabled(false);// to see our scena throught the
		// background

		levelNoMoney.addMenuItem(restartButton);
		levelNoMoney.addMenuItem(quitButton);

		levelNoMoney.buildAnimations();
		restartButton.setPosition(470, 270);
		quitButton.setPosition(470, 360);

		levelNoMoney.setOnMenuItemClickListener(this);
	}

	private void createPauseScene() {

		levelPause = new MenuScene(camera);
		levelPause.setPosition(0, 0);
		//
		// Add menu item
		IMenuItem unpauseButton = new ScaleMenuItemDecorator(new SpriteMenuItem(UNPAUSE, resourceManager.playButton, vbom), 1.7f, 1.5f);
		IMenuItem homeButton = new ScaleMenuItemDecorator(new SpriteMenuItem(QUIT, resourceManager.quitButton , vbom), 0.6f, 0.8f);
		
		// create the background
		Sprite bg = new Sprite(0, 0, resourceManager.pauseBG, vbom);
		bg.setCullingEnabled(true);
		//
		levelPause.attachChild(bg);
		levelPause.setBackgroundEnabled(false);// to see our scena throught the
		// background

		levelPause.addMenuItem(unpauseButton);
		levelPause.addMenuItem(homeButton);
		homeButton.setPosition(700, 400);
		
		levelPause.buildAnimations();
		// restartButton.setPosition(470, 250);

		levelPause.setOnMenuItemClickListener(this);
	}

	private void createLevelClearedScene() {
		levelCleared = new MenuScene(camera);
		levelCleared.setPosition(0, 0);

		IMenuItem playButton = new ScaleMenuItemDecorator(new SpriteMenuItem(KEEP_PLAYING, resourceManager.playButton, vbom), 1.2f, 0.9f);
		IMenuItem quitButton = new ScaleMenuItemDecorator(new SpriteMenuItem(QUIT, resourceManager.quitButton, vbom), 1.2f, 0.9f);

		Sprite bg = new Sprite(0, 0, resourceManager.passedBG, vbom);
	//	bg.setCullingEnabled(true);

		levelCleared.attachChild(bg);
		levelCleared.setBackgroundEnabled(false);

		levelCleared.addMenuItem(playButton);
		levelCleared.addMenuItem(quitButton);

		levelCleared.buildAnimations();

		playButton.setPosition(470, 270);
		quitButton.setPosition(470, 360);

		levelCleared.setOnMenuItemClickListener(this);
	}

	private void showLevelFailed() {
		showGameIndicators(false);
		this.hideControlButtons();
		this.setChildScene(levelFailed, false, true, true);
	}
	
	private void showLevelNoMoney() {
		showGameIndicators(false);
		this.hideControlButtons();
		this.setChildScene(levelNoMoney, false, true, true);
	}

	private void showLevelCleared() {
		showGameIndicators(false);
		this.hideControlButtons();
		this.setChildScene(levelCleared, false, true, true);
	}

	private void showPause() {
		showGameIndicators(false);
		this.hideControlButtons();
		this.setChildScene(levelPause, false, true, true);
	}

	private void hideControlButtons() {
		// this.leftButton.setVisible(false);
		// this.rightButton.setVisible(false);
		this.jumpButton.setVisible(false);

	}

	@Override
	public void reset() {
		super.reset();

		showGameIndicators(true);
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {

		switch (pMenuItem.getID()) {

		case RESTART:

			// restart
			restartLevel();
			System.gc();
			break;
		case QUIT:

			// go to main menu
			quit();
			System.gc();
			break;

		case KEEP_PLAYING:
			restartLevel();
			System.gc();

			break;

		case UNPAUSE:
			
			reset();

			break;
		}

		return false;
	}

//	private void addRewardIcon(boolean wasGoodPath) {
//		Sprite icon = null;
//		int totalIconAlreadyAdded = rightPathScore + wrongPathScore;
//		int rowToPlaceIcon = totalIconAlreadyAdded % 10 + 1;
//		float positionX = 760 - 35 * totalIconAlreadyAdded;
//		if (wasGoodPath) {
//			icon = new Sprite(positionX, 10, 35, 35, ResourceManager.getInstance().correctIconTexture, vbom);
//
//		} else {
//			icon = new Sprite(positionX, 10, 35, 35, ResourceManager.getInstance().wrongIconTexture, vbom);
//		}
//		icon.setTag(TAG_REWARD_ICON);
//		this.camera.getHUD().attachChild(icon);
//		this.pathScoreIndicators.add(icon);
//
//	}

	private void showGameIndicators(boolean shouldShow) {
		jumpButton.setVisible(shouldShow);
		pauseButton.setVisible(shouldShow);
		title.setVisible(shouldShow);
		tomatoScoreIcon.setVisible(shouldShow);
	
		textScore.setVisible(shouldShow);
		levelIcon.setVisible(shouldShow);
		textLevel.setVisible(shouldShow);
	
		for (Sprite rightWrongPathIcon : pathScoreIndicators) {
			rightWrongPathIcon.setVisible(shouldShow);
		}

	}

	private void quit() {
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
		this.camera.getHUD().detachSelf();
		this.dispose();

		// reset the camera
		// resourceManager.camera.setCenter(0, 0);
		int widthTiledBackgroung = this.mTMXTiledMap.getTileColumns() * this.mTMXTiledMap.getTileHeight();
		int heightTiledBackgroung = this.mTMXTiledMap.getTileRows() * this.mTMXTiledMap.getTileWidth();

		// center the camera
		this.camera.setChaseEntity(null);
		this.camera.setBoundsEnabled(false);
		resourceManager.camera.setCenter(400, 240);

		SceneManager.getInstance().updateMainMenu();
		System.gc();
		SceneManager.getInstance().setMainMenu();

	}

	private void restartLevel() {

		// do some clean up
		this.mPhysicsWorld.clearForces();
		this.mPhysicsWorld.clearPhysicsConnectors();
		this.mPhysicsWorld.dispose();

		// some work with the scene
		this.clearTouchAreas();
		this.clearEntityModifiers();
		this.clearUpdateHandlers();
		this.dispose();

		SceneManager.getInstance().createGameScene();
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
			isDrawing = true;
			i = 0;
		}
		if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
			isDrawing = false;
		}
		if (isDrawing = true) {
			rec[i] = new Rectangle(pSceneTouchEvent.getX(), pSceneTouchEvent.getY(), 1, 1, vbom);
			if (i != 0) {

				drawLine(rec[i - 1].getX(), rec[i - 1].getY(), rec[i].getX(), rec[i].getY());

			}
			// this.attachChild(rec[i]);
			i++;
		}
		return true;
	}

	private void drawLine(final float startX, final float startY, final float endX, final float endY) {
		Line bodyLine = new Line(startX, startY, endX, endY, vbom);

		Body physicBodyLine;
		Line line = new Line(startX, startY, endX, endY, vbom);
		Line lineTicker = new Line(startX, startY, endX, endY, vbom);

		FixtureDef objectFixtureDef = PhysicsFactory.createFixtureDef(0.0f, 0.5f, 0.5f);

		physicBodyLine = PhysicsFactory.createLineBody(mPhysicsWorld, bodyLine, objectFixtureDef);
		physicBodyLine.setUserData(SpriteTag.LINE);
		bodyLine.setVisible(false);
		line.setLineWidth(2);
		lineTicker.setLineWidth(6);
		line.setColor(0f, 102f, 0f);
		lineTicker.setColor(102f,204f,0f);

		this.attachChild(bodyLine);
		this.attachChild(lineTicker);
		this.attachChild(line);

	}

}
