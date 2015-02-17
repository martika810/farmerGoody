package com.martaocio.farmergoody;

import java.util.LinkedList;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
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
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;

import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
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
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.IModifier.IModifierListener;

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
	private final int TAG_REWARD_ICON=999;
	private final int TAG_TOMAT_ICON=998;
	private final int TAG_SCORE_TEXT=997;

	private TMXTiledMap mTMXTiledMap;
	private TMXLayer tmxLayer;

	private PhysicsWorld mPhysicsWorld;

	private LinkedList<Sprite> vegetables;
	private LinkedList<Body> fencesBodies;
	private LinkedList<Sprite> tomatos;
	
	private LinkedList<Sprite> pathScoreIndicators;

	public Text textScore;
	public Text textBestScore;
	public Text textLevel;

	public Sprite tomatoScoreIcon;
	public Sprite rightPathScoreIcon;
	public Sprite upButton;
	public Sprite pauseButton;
	public Sprite restartButton;
	public Sprite leftButton;
	public Sprite rightButton;

	private Player player;
	private Bull bull;

	private TimerHandler timerPlayer = null;
	private DelayModifier keepRunningModifier = null;
	private DelayModifier deleteAfterFadeModifier = null;
	private final static int MAX_NUMBER_ERROR=3;

	private int score = 0;
	private int rightPathScore = 0;
	private int wrongPathScore = 0;

	// create 2 new menuScene
	private MenuScene levelFailed, levelCleared,levelPause;

	@Override
	public void createScene() {
		createPhysicsWorld();
		createLevel(UserState.getInstance().getCurrentLevel());
		createLevelClearedScene();
		createLevelFailedScene();
	//	createLevelPauseScene();

		// listen when someone touch the screen
		// this.setOnSceneTouchListener(this);

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

		tomatoScoreIcon = new Sprite(40, 10, 35, 35, ResourceManager.getInstance().tomatoIconTexture, vbom);
		textScore = new Text(80, 10, this.resourceManager.font, ":"+UserState.getInstance().getCurrentAcumalatedPoints()+"   " , vbom);
		textBestScore = new Text(40, 50, this.resourceManager.font, "Best Score:"+UserState.getInstance().getBestScore()+"   " , vbom);
		textLevel= new Text(40, 90, this.resourceManager.font, "#"+UserState.getInstance().getCurrentLevel()+"   " , vbom);
		tomatoScoreIcon.setTag(TAG_TOMAT_ICON);
		textScore.setTag(TAG_SCORE_TEXT);

		createButtons();

		// Sprite tomatoType2 = new Sprite(object.getX(), object.getY(), 50, 50,
		// ResourceManager.getInstance().tomato2Texture,
		// vbom);

		tomatos = new LinkedList<Sprite>();
		fencesBodies = new LinkedList<Body>();
		pathScoreIndicators=new LinkedList<Sprite>();
		try {
			final TMXLoader tmxLoader = new TMXLoader(activity.getAssets(), activity.getTextureManager(),
					TextureOptions.BILINEAR_PREMULTIPLYALPHA, vbom);

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
				} else if (object.getName().equals(SpriteTag.FENCE)) {
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

				else if (object.getName().equals(SpriteTag.TOMATO1)) {
					Sprite tomatoType1 = new Sprite(object.getX(), object.getY(), 50, 50, ResourceManager.getInstance().point1Texture,
							vbom);
					FixtureDef tomatoFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0f);
					Body body = PhysicsFactory.createBoxBody(mPhysicsWorld, tomatoType1, BodyType.StaticBody, tomatoFixtureDef);
					body.setUserData(SpriteTag.TOMATO1);
					tomatos.add(tomatoType1);
					tomatoType1.setUserData(body);
				} else if (object.getName().equals(SpriteTag.TOMATO2)) {
					Sprite tomatoType2 = new Sprite(object.getX(), object.getY(), 50, 50, ResourceManager.getInstance().point2Texture,
							vbom);
					FixtureDef tomatoFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0f);
					Body body = PhysicsFactory.createBoxBody(mPhysicsWorld, tomatoType2, BodyType.StaticBody, tomatoFixtureDef);
					body.setUserData(SpriteTag.TOMATO2);
					tomatos.add(tomatoType2);
					tomatoType2.setUserData(body);
				} else if (object.getName().equals(SpriteTag.TOMATO3)) {
					Sprite tomatoType3 = new Sprite(object.getX(), object.getY(), 50, 50, ResourceManager.getInstance().point3Texture,
							vbom);
					FixtureDef tomatoFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0f);
					Body body = PhysicsFactory.createBoxBody(mPhysicsWorld, tomatoType3, BodyType.StaticBody, tomatoFixtureDef);
					body.setUserData(SpriteTag.TOMATO3);
					tomatos.add(tomatoType3);
					tomatoType3.setUserData(body);
				} else if (object.getName().equals(SpriteTag.TOMATO4)) {
					Sprite tomatoType4 = new Sprite(object.getX(), object.getY(), 50, 50, ResourceManager.getInstance().point4Texture,
							vbom);
					FixtureDef tomatoFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0f);
					Body body = PhysicsFactory.createBoxBody(mPhysicsWorld, tomatoType4, BodyType.StaticBody, tomatoFixtureDef);
					body.setUserData(SpriteTag.TOMATO4);
					tomatos.add(tomatoType4);
					tomatoType4.setUserData(body);
				} else if (object.getName().equals(SpriteTag.TOMATO5)) {
					Sprite tomatoType5 = new Sprite(object.getX(), object.getY(), 50, 50, ResourceManager.getInstance().point5Texture,
							vbom);
					FixtureDef tomatoFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0f);
					Body body = PhysicsFactory.createBoxBody(mPhysicsWorld, tomatoType5, BodyType.StaticBody, tomatoFixtureDef);
					body.setUserData(SpriteTag.TOMATO5);
					tomatos.add(tomatoType5);
					tomatoType5.setUserData(body);
				} else if (object.getName().equals(SpriteTag.TOMATO6)) {
					Sprite tomatoType6 = new Sprite(object.getX(), object.getY(), 50, 50, ResourceManager.getInstance().point6Texture,
							vbom);
					FixtureDef tomatoFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0f);
					Body body = PhysicsFactory.createBoxBody(mPhysicsWorld, tomatoType6, BodyType.StaticBody, tomatoFixtureDef);
					body.setUserData(SpriteTag.TOMATO6);
					tomatos.add(tomatoType6);
					tomatoType6.setUserData(body);
				} else if (object.getName().equals(SpriteTag.TOMATO8)) {
					Sprite tomatoType8 = new Sprite(object.getX(), object.getY(), 50, 50, ResourceManager.getInstance().point8Texture,
							vbom);
					FixtureDef tomatoFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0f);
					Body body = PhysicsFactory.createBoxBody(mPhysicsWorld, tomatoType8, BodyType.StaticBody, tomatoFixtureDef);
					body.setUserData(SpriteTag.TOMATO8);
					tomatos.add(tomatoType8);
					tomatoType8.setUserData(body);
				} else if (object.getName().equals(SpriteTag.TOMATO9)) {
					Sprite tomatoType9 = new Sprite(object.getX(), object.getY(), 50, 50, ResourceManager.getInstance().point9Texture,
							vbom);
					FixtureDef tomatoFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0f);
					Body body = PhysicsFactory.createBoxBody(mPhysicsWorld, tomatoType9, BodyType.StaticBody, tomatoFixtureDef);
					body.setUserData(SpriteTag.TOMATO9);
					tomatos.add(tomatoType9);
					tomatoType9.setUserData(body);
				} else if (object.getName().equals(SpriteTag.TOMATO10)) {
					Sprite tomatoType10 = new Sprite(object.getX(), object.getY(), 50, 50, ResourceManager.getInstance().point10Texture,
							vbom);
					FixtureDef tomatoFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0f);
					Body body = PhysicsFactory.createBoxBody(mPhysicsWorld, tomatoType10, BodyType.StaticBody, tomatoFixtureDef);
					body.setUserData(SpriteTag.TOMATO10);
					tomatos.add(tomatoType10);
					tomatoType10.setUserData(body);
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
		if(this.mTMXTiledMap.getTMXLayers().size()>2){
			tmxLayer = this.mTMXTiledMap.getTMXLayers().get(2);
			attachChild(tmxLayer);
		}
		
		
		// add the coins to the scene
		// for (int i = 0; i < coins.size(); i++) {
		// this.attachChild(coins.get(i));
		// }
		for (Sprite tomato : tomatos) {
			this.attachChild(tomato);
		}
		// /////////////////////////////////////////////////////////////////

		// Attach player
		player = new Player(400, 150, 150, 150, vbom, camera, mPhysicsWorld) {

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
		// addTomatoObject();

		// for (int i = 0; i < vegetables.size(); i++) {
		// this.attachChild(vegetables.get(i));
		// }

		// that heads up the display
		HUD hud = new HUD();
		hud.attachChild(textBestScore);
		hud.attachChild(textLevel);
		hud.attachChild(textScore);
		hud.attachChild(tomatoScoreIcon);

		hud.attachChild(rightButton);
		hud.attachChild(leftButton);
		hud.attachChild(upButton);
		hud.attachChild(pauseButton);
		hud.attachChild(restartButton);

		hud.registerTouchArea(upButton);
		hud.registerTouchArea(rightButton);
		hud.registerTouchArea(leftButton);
		hud.registerTouchArea(pauseButton);
		hud.registerTouchArea(restartButton);

		setOnSceneTouchListener(this);
		this.setTouchAreaBindingOnActionMoveEnabled(true);
		this.setTouchAreaBindingOnActionDownEnabled(true);

		this.camera.setHUD(hud);

		// this will be trigger when the scene is updated
		// TimerHandler updateModifier = new TimerHandler(5f, true,
		// new ITimerCallback() {
		//
		// @Override
		// public void onTimePassed(TimerHandler pTimerHandler) {
		// clearVegetables();
		// addTomatoObject();
		//
		// }
		// });
		//
		// this.registerUpdateHandler(updateModifier);
	}

	private void createButtons() {
		upButton = new Sprite(360, 400, 80, 80, ResourceManager.getInstance().upArrowTexture, vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
				if (pSceneTouchEvent.isActionUp()) {
					player.jump();
				}
				return true;
			};
		};

		leftButton = new Sprite(270, 400, 80, 80, ResourceManager.getInstance().leftArrowTexture, vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
				if (pSceneTouchEvent.isActionUp()) {
					player.runSlower();
				}
				return true;
			};
		};
		rightButton = new Sprite(450, 400, 80, 80, ResourceManager.getInstance().rightArrowTexture, vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
				if (pSceneTouchEvent.isActionUp()) {
					player.runFaster(camera);
				}
				return true;
			};
		};
		
		pauseButton = new Sprite(700, 400, 80, 64, ResourceManager.getInstance().pauseBtnTexture, vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
				restartButton.setVisible(true);
				pauseButton.setVisible(false);
				pause(true);
				
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
	
	private void pause(boolean shouldPause){
		this.setIgnoreUpdate(shouldPause);
		
	}

	private void createPhysicsWorld() {

		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0, SensorManager.GRAVITY_EARTH), false);

		this.registerUpdateHandler(mPhysicsWorld);
		mPhysicsWorld.setContactListener(contactListener());
	}

	

	// if(timerPlayer==null){
	// timerPlayer=new TimerHandler(300f,false,new ITimerCallback(){
	//
	// @Override
	// public void onTimePassed(final TimerHandler pTimerHandler){
	// player.setRunning();
	//
	// }
	// });
	// }else{
	//
	// timerPlayer.setTimerSeconds(300f);
	//
	// }
	// player.registerUpdateHandler(timerPlayer);

	// return false;
	// }

	private ContactListener contactListener() {
		ContactListener contactListener = new ContactListener() {

			@Override
			public void beginContact(Contact contact) {
				Body a = contact.getFixtureA().getBody();
				Body b = contact.getFixtureB().getBody();
				UserState currentUserState=UserState.getInstance();
				// collision between player and ground
				if (a.getUserData() != null && b.getUserData() != null) {
					if (GameUtils.isCollisionBetween(a, b, SpriteTag.PLAYER, SpriteTag.GROUND)) {

						player.canEat = false;
						player.canJump = true;
						player.setRunning();
					}

					if (GameUtils.isCollisionBetween(a, b, SpriteTag.GROUND, SpriteTag.BULL)) {
						bull.setRunning();
					}

					if (GameUtils.isCollisionBetween(a, b, SpriteTag.PLAYER, SpriteTag.BULL)) {
						if(currentUserState.getBestScore()<score){
							currentUserState.setBestScore(score);
						}
						currentUserState.setCurrentLevel(currentUserState.getCurrentLevel());
						currentUserState.saveToFile();
						

						showLevelFailed();
					}
					if (GameUtils.isCollisionBetween(a, b, SpriteTag.PLAYER, SpriteTag.FENCE)) {

						player.setRunning();

					}
					if (GameUtils.isCollisionBetween(a, b, SpriteTag.BULL, SpriteTag.FENCE)) {

						if (a.getUserData().equals(SpriteTag.FENCE)) {
							removeFence(a);
						} else if (b.getUserData().equals(SpriteTag.FENCE)) {
							removeFence(b);

						}
					}
					if (GameUtils.isCollisionBetween(a, b, SpriteTag.PLAYER, SpriteTag.TOMATO1)) {
						// resourceManager.coinCollect.play();
						// update the score
						score += 1;
						textScore.setText(": " + score);
						resourceManager.eatTomato.play();

						// detect which body was colaided
						if (a.getUserData().equals(SpriteTag.TOMATO1)) {
							removeBody(a);
						} else if (b.getUserData().equals(SpriteTag.TOMATO1)) {
							removeBody(b);
						}
					}
					if (GameUtils.isCollisionBetween(a, b, SpriteTag.PLAYER, SpriteTag.TOMATO2)) {
						// resourceManager.coinCollect.play();
						// update the score
						score += 2;
						textScore.setText(": " + score);
						resourceManager.eatTomato.play();

						// detect which body was colaided
						if (a.getUserData().equals(SpriteTag.TOMATO2)) {
							removeBody(a);
						} else if (b.getUserData().equals(SpriteTag.TOMATO2)) {
							removeBody(b);
						}
					}
					if (GameUtils.isCollisionBetween(a, b, SpriteTag.PLAYER, SpriteTag.TOMATO3)) {
						// resourceManager.coinCollect.play();
						// update the score
						score += 3;
						textScore.setText(": " + score);
						resourceManager.eatTomato.play();

						// detect which body was colaided
						if (a.getUserData().equals(SpriteTag.TOMATO3)) {
							removeBody(a);
						} else if (b.getUserData().equals(SpriteTag.TOMATO3)) {
							removeBody(b);
						}
					}
					if (GameUtils.isCollisionBetween(a, b, SpriteTag.PLAYER, SpriteTag.TOMATO4)) {
						// resourceManager.coinCollect.play();
						// update the score
						score += 4;
						textScore.setText(": " + score);
						resourceManager.eatTomato.play();

						// detect which body was colaided
						if (a.getUserData().equals(SpriteTag.TOMATO4)) {
							removeBody(a);
						} else if (b.getUserData().equals(SpriteTag.TOMATO4)) {
							removeBody(b);
						}
					}
					if (GameUtils.isCollisionBetween(a, b, SpriteTag.PLAYER, SpriteTag.TOMATO5)) {
						// resourceManager.coinCollect.play();
						// update the score
						score += 5;
						textScore.setText(": " + score);
						resourceManager.eatTomato.play();

						// detect which body was colaided
						if (a.getUserData().equals(SpriteTag.TOMATO5)) {
							removeBody(a);
						} else if (b.getUserData().equals(SpriteTag.TOMATO5)) {
							removeBody(b);
						}
					}
					if (GameUtils.isCollisionBetween(a, b, SpriteTag.PLAYER, SpriteTag.TOMATO6)) {
						// resourceManager.coinCollect.play();
						// update the score
						score += 6;
						textScore.setText(": " + score);
						resourceManager.eatTomato.play();

						// detect which body was colaided
						if (a.getUserData().equals(SpriteTag.TOMATO6)) {
							removeBody(a);
						} else if (b.getUserData().equals(SpriteTag.TOMATO6)) {
							removeBody(b);
						}
					}
					if (GameUtils.isCollisionBetween(a, b, SpriteTag.PLAYER, SpriteTag.TOMATO8)) {
						// resourceManager.coinCollect.play();
						// update the score
						score += 8;
						textScore.setText(": " + score);
						resourceManager.eatTomato.play();

						// detect which body was colaided
						if (a.getUserData().equals(SpriteTag.TOMATO8)) {
							removeBody(a);
						} else if (b.getUserData().equals(SpriteTag.TOMATO8)) {
							removeBody(b);
						}
					}
					if (GameUtils.isCollisionBetween(a, b, SpriteTag.PLAYER, SpriteTag.TOMATO9)) {
						// resourceManager.coinCollect.play();
						// update the score
						score += 9;
						textScore.setText(": " + score);
						resourceManager.eatTomato.play();

						// detect which body was colaided
						if (a.getUserData().equals(SpriteTag.TOMATO9)) {
							removeBody(a);
						} else if (b.getUserData().equals(SpriteTag.TOMATO9)) {
							removeBody(b);
						}
					}
					if (GameUtils.isCollisionBetween(a, b, SpriteTag.PLAYER, SpriteTag.TOMATO10)) {
						// resourceManager.coinCollect.play();
						// update the score
						score += 10;
						textScore.setText(": " + score);
						resourceManager.eatTomato.play();
						// detect which body was colaided
						if (a.getUserData().equals(SpriteTag.TOMATO10)) {
							removeBody(a);
						} else if (b.getUserData().equals(SpriteTag.TOMATO10)) {
							removeBody(b);
						}
					}
					if (GameUtils.isCollisionBetween(a, b, SpriteTag.BULL, SpriteTag.TOMATO1)
							|| GameUtils.isCollisionBetween(a, b, SpriteTag.BULL, SpriteTag.TOMATO2)
							|| GameUtils.isCollisionBetween(a, b, SpriteTag.BULL, SpriteTag.TOMATO3)
							|| GameUtils.isCollisionBetween(a, b, SpriteTag.BULL, SpriteTag.TOMATO4)
							|| GameUtils.isCollisionBetween(a, b, SpriteTag.BULL, SpriteTag.TOMATO5)
							|| GameUtils.isCollisionBetween(a, b, SpriteTag.BULL, SpriteTag.TOMATO6)
							|| GameUtils.isCollisionBetween(a, b, SpriteTag.BULL, SpriteTag.TOMATO8)
							|| GameUtils.isCollisionBetween(a, b, SpriteTag.BULL, SpriteTag.TOMATO9)
							|| GameUtils.isCollisionBetween(a, b, SpriteTag.BULL, SpriteTag.TOMATO10)) {

					}
					if (a.getUserData().equals(SpriteTag.BULL) && GameUtils.isBodyTomato(b) || b.getUserData().equals(SpriteTag.BULL)
							&& GameUtils.isBodyTomato(a)) {
						String tomatoType = "";
						if (GameUtils.isBodyTomato(a)) {
							// tomatoType=GameUtils.extractTomatoType(a);
							removeBody(a);
						} else if (GameUtils.isBodyTomato(b)) {
							removeBody(b);
						}
					}
					if (GameUtils.isCollisionBetween(a, b, SpriteTag.PLAYER, SpriteTag.CORRECT)) {
						rightPathScore++;
						addRewardIcon(true);
						// textRightPathScore.setText(": " + rightPathScore);
						if (a.getUserData().equals(SpriteTag.CORRECT)) {
							removeBody(a);
						} else if (b.getUserData().equals(SpriteTag.CORRECT)) {
							removeBody(b);
						}

					}
					if (GameUtils.isCollisionBetween(a, b, SpriteTag.PLAYER, SpriteTag.WRONG)) {
						wrongPathScore++;
						addRewardIcon(false);
						
						// textRightPathScore.setText(": " + rightPathScore);
						if (a.getUserData().equals(SpriteTag.WRONG)) {
							removeBody(a);
						} else if (b.getUserData().equals(SpriteTag.WRONG)) {
							removeBody(b);
						}
						if(wrongPathScore >= MAX_NUMBER_ERROR){
							if(currentUserState.getBestScore()<score){
								currentUserState.setBestScore(score);
							}
							currentUserState.setCurrentLevel(currentUserState.getCurrentLevel());
							currentUserState.saveToFile();
							showLevelFailed();
						}

					}
					if (GameUtils.isCollisionBetween(a, b, SpriteTag.BULL, SpriteTag.CORRECT)) {

						if (a.getUserData().equals(SpriteTag.CORRECT)) {
							removeBody(a);
						} else if (b.getUserData().equals(SpriteTag.CORRECT)) {
							removeBody(b);
						}

					}
					if (GameUtils.isCollisionBetween(a, b, SpriteTag.BULL, SpriteTag.WRONG)) {

						if (a.getUserData().equals(SpriteTag.WRONG)) {
							removeBody(a);
						} else if (b.getUserData().equals(SpriteTag.WRONG)) {
							removeBody(b);
						}

					}
					if (GameUtils.isCollisionBetween(a, b, SpriteTag.PLAYER, SpriteTag.END)) {
						// show success screen
						if(currentUserState.getBestScore()<score){
							currentUserState.setBestScore(score);
						}
						currentUserState.setCurrentAcumalatedPoints(score);
						currentUserState.setCurrentLevel(currentUserState.getCurrentLevel() + 1);
						currentUserState.saveToFile();
						showLevelCleared();
						
						
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
		IMenuItem restartButton = new ScaleMenuItemDecorator(new SpriteMenuItem(RESTART, resourceManager.restartButton, vbom), 1.3f,
				1.1f);
		IMenuItem quitButton = new ScaleMenuItemDecorator(new SpriteMenuItem(QUIT, resourceManager.quitButton, vbom), 1.3f, 1.1f);
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
		restartButton.setPosition(470, 250);
		quitButton.setPosition(470, 360);

		levelFailed.setOnMenuItemClickListener(this);
	}

	private void createLevelClearedScene() {
		levelCleared = new MenuScene(camera);
		levelCleared.setPosition(0, 0);

		IMenuItem playButton = new ScaleMenuItemDecorator(new SpriteMenuItem(KEEP_PLAYING, resourceManager.playButton, vbom), 1.3f,
				1.1f);
		IMenuItem quitButton = new ScaleMenuItemDecorator(new SpriteMenuItem(QUIT, resourceManager.quitButton, vbom), 1.2f, 1);

		Sprite bg = new Sprite(0, 0, resourceManager.passedBG, vbom);

		levelCleared.attachChild(bg);
		levelCleared.setBackgroundEnabled(false);
		
		
		levelCleared.addMenuItem(playButton);
		levelCleared.addMenuItem(quitButton);

		levelCleared.buildAnimations();
		
		playButton.setPosition(470, 250);
		quitButton.setPosition(470, 360);
		
		levelCleared.setOnMenuItemClickListener(this);
	}
	
//	private void createLevelPauseScene() {
//		levelPause = new MenuScene(camera);
//		levelPause.setPosition(0, 0);
//
//		final IMenuItem playButton = new ScaleMenuItemDecorator(new SpriteMenuItem(UNPAUSE, resourceManager.playButton, vbom), 1.3f,
//				1.1f);
//		final IMenuItem quitButton = new ScaleMenuItemDecorator(new SpriteMenuItem(QUIT, resourceManager.quitButton, vbom), 1.2f, 1);
//
//		Sprite bg = new Sprite(0, 0, resourceManager.passedBG, vbom);
//
//		levelPause.attachChild(bg);
//		levelPause.setBackgroundEnabled(false);
//		
//		levelPause.addMenuItem(playButton);
//		levelPause.addMenuItem(quitButton);
//
//		levelPause.buildAnimations();
//		//
//		levelPause.setOnMenuItemClickListener(this);
//	}


	private void showLevelFailed() {
		showGameIndicators(false);
		this.hideControlButtons();
		this.setChildScene(levelFailed, false, true, true);
	}

	private void showLevelCleared() {
		showGameIndicators(false);
		this.hideControlButtons();
		this.setChildScene(levelCleared, false, true, true);
	}
	
//	private void showLevelPaused() {
//		showGameIndicators(false);
//		this.hideControlButtons();
//		this.setChildScene(levelPause, false, true, true);
//	}

	private void hideControlButtons() {
		this.leftButton.setVisible(false);
		this.rightButton.setVisible(false);
		this.upButton.setVisible(false);
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {

		switch (pMenuItem.getID()) {

		case RESTART:

			// restart
			restartLevel();
			break;
		case QUIT:

			// go to main menu
			quit();
			break;
			
		case KEEP_PLAYING:
			restartLevel();
			break;
		
			
		}
		

		return false;
	}

	private void addRewardIcon(boolean wasGoodPath) {
		Sprite icon = null;
		int totalIconAlreadyAdded = rightPathScore + wrongPathScore;
		int rowToPlaceIcon = totalIconAlreadyAdded % 10 + 1;
		float positionX = 760 - 35 * totalIconAlreadyAdded;
		if (wasGoodPath) {
			icon = new Sprite(positionX, 10, 35, 35, ResourceManager.getInstance().correctIconTexture, vbom);

		} else {
			icon = new Sprite(positionX, 10, 35, 35, ResourceManager.getInstance().wrongIconTexture, vbom);
		}
		icon.setTag(TAG_REWARD_ICON);
		this.camera.getHUD().attachChild(icon);
		this.pathScoreIndicators.add(icon);
		
	}
	private void showGameIndicators(boolean shouldShow){
		upButton.setVisible(shouldShow);
		leftButton.setVisible(shouldShow);
		rightButton.setVisible(shouldShow);
		pauseButton.setVisible(shouldShow);
		tomatoScoreIcon.setVisible(shouldShow);
		textScore.setVisible(shouldShow);
		textLevel.setVisible(shouldShow);
		textBestScore.setVisible(shouldShow);
		for(Sprite rightWrongPathIcon:pathScoreIndicators){
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
	//	resourceManager.camera.setCenter(0, 0);
		int widthTiledBackgroung=this.mTMXTiledMap.getTileColumns()*this.mTMXTiledMap.getTileHeight();
		int heightTiledBackgroung=this.mTMXTiledMap.getTileRows()*this.mTMXTiledMap.getTileWidth();
		
		player.setX(0);
		player.setY(-10);
		this.camera.setChaseEntity(null);
		resourceManager.camera.setCenter(resourceManager.camera.getWidth() / 2, resourceManager.camera.getHeight() / 2);
		
		
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
		if(pSceneTouchEvent.isActionDown() && this.isIgnoreUpdate()){
			pause(false);
		}
		return false;
	}

}
