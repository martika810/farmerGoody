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
import org.andengine.entity.scene.IOnSceneTouchListener;
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

public class GameScene extends BaseScene implements IOnSceneTouchListener,
		IOnMenuItemClickListener {

	private final int RESTART = 0;
	private final int QUIT = 1;

	private TMXTiledMap mTMXTiledMap;
	private TMXLayer tmxLayer;

	private PhysicsWorld mPhysicsWorld;

	private LinkedList<Sprite> vegetables;
	private LinkedList<Body> fencesBodies;
	private LinkedList<Sprite> tomatos;

	public Text textScore;

	private Player player;
	private Bull bull;

	private TimerHandler timerPlayer = null;
	private DelayModifier keepRunningModifier = null;
	private DelayModifier deleteAfterFadeModifier = null;

	private int score = 0;

	// create 2 new menuScene
	private MenuScene levelFailed, levelCleared;

	@Override
	public void createScene() {
		createPhysicsWorld();
		createLevel();
		// createLevelClearedScene();
		createLevelFailedScene();

		// listen when someone touch the screen
		this.setOnSceneTouchListener(this);

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

	private void createLevel() {

		// create the tex for the score
		textScore = new Text(100, 10, this.resourceManager.font, "Coins: 0",
				vbom);

		vegetables = new LinkedList<Sprite>();
		tomatos = new LinkedList<Sprite>();
		fencesBodies = new LinkedList<Body>();

		try {
			final TMXLoader tmxLoader = new TMXLoader(activity.getAssets(),
					activity.getTextureManager(),
					TextureOptions.BILINEAR_PREMULTIPLYALPHA, vbom);

			this.mTMXTiledMap = tmxLoader.loadFromAsset("tmx/level_flat.tmx");
		} catch (TMXLoadException e) {

			Debug.e(e);
			e.printStackTrace();
		}

		// Create all the objects from the TMXLayer
		for (final TMXObjectGroup group : this.mTMXTiledMap
				.getTMXObjectGroups()) {// loop over the objectgroups
			for (final TMXObject object : group.getTMXObjects()) {
				if (object.getName().equals("ground")) {
					Rectangle rect = new Rectangle(object.getX(),
							object.getY(), object.getWidth(),
							object.getHeight(), vbom);

					FixtureDef groundFixtureDef = PhysicsFactory
							.createFixtureDef(0, 0, .2f);
					// BodyType to static if u dont want the body moves
					PhysicsFactory.createBoxBody(mPhysicsWorld, rect,
							BodyType.StaticBody, groundFixtureDef).setUserData(
							"ground");

					// store data in userData so it can be retrieved later on

					rect.setVisible(false);
					attachChild(rect);
				} else if (object.getName().equals("fence")) {
					Rectangle rect = new Rectangle(object.getX(),
							object.getY(), object.getWidth(),
							object.getHeight(), vbom);

					FixtureDef spikeFixtureDef = PhysicsFactory
							.createFixtureDef(0, 0, 0f);
					Body fenceBody = PhysicsFactory.createBoxBody(
							mPhysicsWorld, rect, BodyType.StaticBody,
							spikeFixtureDef);
					fenceBody.setUserData("fence");
					fencesBodies.add(fenceBody);
				}
				else if(object.getName().equals("t1")){
					Sprite tomatoType1=new Sprite(object.getX(),object.getY(),50,50,ResourceManager.getInstance().tomato1Texture,vbom);
					FixtureDef tomatoFixtureDef =PhysicsFactory .createFixtureDef(0, 0, 0f);
					Body body=PhysicsFactory.createBoxBody(mPhysicsWorld, tomatoType1, BodyType.StaticBody, tomatoFixtureDef);
					body.setUserData("t1");
					tomatos.add(tomatoType1);
					tomatoType1.setUserData(body);
				}
				else if(object.getName().equals("t2")){
					Sprite tomatoType2=new Sprite(object.getX(),object.getY(),50,50,ResourceManager.getInstance().tomato2Texture,vbom);
					FixtureDef tomatoFixtureDef =PhysicsFactory .createFixtureDef(0, 0, 0f);
					Body body=PhysicsFactory.createBoxBody(mPhysicsWorld, tomatoType2, BodyType.StaticBody, tomatoFixtureDef);
					body.setUserData("t2");
					tomatos.add(tomatoType2);
					tomatoType2.setUserData(body);
				}
				else if(object.getName().equals("t3")){
					Sprite tomatoType3=new Sprite(object.getX(),object.getY(),50,50,ResourceManager.getInstance().tomato3Texture,vbom);
					FixtureDef tomatoFixtureDef =PhysicsFactory .createFixtureDef(0, 0, 0f);
					Body body=PhysicsFactory.createBoxBody(mPhysicsWorld, tomatoType3, BodyType.StaticBody, tomatoFixtureDef);
					body.setUserData("t3");
					tomatos.add(tomatoType3);
					tomatoType3.setUserData(body);
				}
				else if(object.getName().equals("t4")){
					Sprite tomatoType4=new Sprite(object.getX(),object.getY(),50,50,ResourceManager.getInstance().tomato4Texture,vbom);
					FixtureDef tomatoFixtureDef =PhysicsFactory .createFixtureDef(0, 0, 0f);
					Body body=PhysicsFactory.createBoxBody(mPhysicsWorld, tomatoType4, BodyType.StaticBody, tomatoFixtureDef);
					body.setUserData("t4");
					tomatos.add(tomatoType4);
					tomatoType4.setUserData(body);
				}
				else if(object.getName().equals("t5")){
					Sprite tomatoType5=new Sprite(object.getX(),object.getY(),50,50,ResourceManager.getInstance().tomato5Texture,vbom);
					FixtureDef tomatoFixtureDef =PhysicsFactory .createFixtureDef(0, 0, 0f);
					Body body=PhysicsFactory.createBoxBody(mPhysicsWorld, tomatoType5, BodyType.StaticBody, tomatoFixtureDef);
					body.setUserData("t5");
					tomatos.add(tomatoType5);
					tomatoType5.setUserData(body);
				}
				else if(object.getName().equals("t6")){
					Sprite tomatoType6=new Sprite(object.getX(),object.getY(),50,50,ResourceManager.getInstance().tomato6Texture,vbom);
					FixtureDef tomatoFixtureDef =PhysicsFactory .createFixtureDef(0, 0, 0f);
					Body body=PhysicsFactory.createBoxBody(mPhysicsWorld, tomatoType6, BodyType.StaticBody, tomatoFixtureDef);
					body.setUserData("t6");
					tomatos.add(tomatoType6);
					tomatoType6.setUserData(body);
				}
				else if(object.getName().equals("t8")){
					Sprite tomatoType8=new Sprite(object.getX(),object.getY(),50,50,ResourceManager.getInstance().tomato8Texture,vbom);
					FixtureDef tomatoFixtureDef =PhysicsFactory .createFixtureDef(0, 0, 0f);
					Body body=PhysicsFactory.createBoxBody(mPhysicsWorld, tomatoType8, BodyType.StaticBody, tomatoFixtureDef);
					body.setUserData("t8");
					tomatos.add(tomatoType8);
					tomatoType8.setUserData(body);
				}
				else if(object.getName().equals("t9")){
					Sprite tomatoType9=new Sprite(object.getX(),object.getY(),50,50,ResourceManager.getInstance().tomato9Texture,vbom);
					FixtureDef tomatoFixtureDef =PhysicsFactory .createFixtureDef(0, 0, 0f);
					Body body=PhysicsFactory.createBoxBody(mPhysicsWorld, tomatoType9, BodyType.StaticBody, tomatoFixtureDef);
					body.setUserData("t9");
					tomatos.add(tomatoType9);
					tomatoType9.setUserData(body);
				}
				else if(object.getName().equals("t10")){
					Sprite tomatoType10=new Sprite(object.getX(),object.getY(),50,50,ResourceManager.getInstance().tomato10Texture,vbom);
					FixtureDef tomatoFixtureDef =PhysicsFactory .createFixtureDef(0, 0, 0f);
					Body body=PhysicsFactory.createBoxBody(mPhysicsWorld, tomatoType10, BodyType.StaticBody, tomatoFixtureDef);
					body.setUserData("t10");
					tomatos.add(tomatoType10);
					tomatoType10.setUserData(body);
				}
				/*
				 * else if (object.getName().equals("coin")) { // Sprite is
				 * anobject u can do things with. moving Sprite coin = new
				 * Sprite(object.getX(), object.getY(), object.getWidth(),
				 * object.getHeight(),
				 * ResourceManager.getInstance().coinTexture, vbom); // create
				 * the body for the sprite FixtureDef coinFixtureDef =
				 * PhysicsFactory .createFixtureDef(0, 0, 0f);
				 * 
				 * Body body = PhysicsFactory.createBoxBody(mPhysicsWorld, coin,
				 * BodyType.StaticBody, coinFixtureDef);
				 * 
				 * // for collecting coins body.setUserData("coin");
				 * coins.add(coin); coin.setUserData(body);// pass the body bcos
				 * we will remove // it later
				 * 
				 * } else if (object.getName().equals("spike")) { Rectangle rect
				 * = new Rectangle(object.getX(), object.getY(),
				 * object.getWidth(), object.getHeight(), vbom);
				 * 
				 * FixtureDef spikeFixtureDef = PhysicsFactory
				 * .createFixtureDef(0, 0, 0f);
				 * PhysicsFactory.createBoxBody(mPhysicsWorld, rect,
				 * BodyType.StaticBody, spikeFixtureDef).setUserData( "spike");
				 * 
				 * } else if (object.getName().equals("finishline")) {
				 * 
				 * Rectangle rect = new Rectangle(object.getX(), object.getY(),
				 * object.getWidth(), object.getHeight(), vbom); FixtureDef
				 * finishLineDef = PhysicsFactory.createFixtureDef( 0, 0, 0f);
				 * PhysicsFactory.createBoxBody(mPhysicsWorld, rect,
				 * BodyType.StaticBody, finishLineDef).setUserData(
				 * "finishline");
				 * 
				 * }
				 */
			}

		}

		// /////////////////////////////////////////
		tmxLayer = this.mTMXTiledMap.getTMXLayers().get(0);// to get the first
															// layer

		attachChild(tmxLayer);

		tmxLayer = this.mTMXTiledMap.getTMXLayers().get(1);

		attachChild(tmxLayer);

		// add the coins to the scene
		// for (int i = 0; i < coins.size(); i++) {
		// this.attachChild(coins.get(i));
		// }
		for (Sprite tomato:tomatos){
			this.attachChild(tomato);
		}
		// /////////////////////////////////////////////////////////////////

		// Attach player
		player = new Player(400, 150, 160, 160, vbom, camera, mPhysicsWorld) {

			@Override
			public void onDie() {
				// showLevelFailed();
			}
		};
		// animate the player
		player.setRunning();
		this.attachChild(player);

		// Attach bull
		bull = new Bull(50, 150, 160, 160, vbom, camera, mPhysicsWorld);

		// animate the player
		bull.setRunning();
		this.attachChild(bull);
		// addTomatoObject();

		// for (int i = 0; i < vegetables.size(); i++) {
		// this.attachChild(vegetables.get(i));
		// }

		// that heads up the display
		HUD hud = new HUD();
		hud.attachChild(textScore);

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

	private void createPhysicsWorld() {

		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0,
				SensorManager.GRAVITY_EARTH), false);

		this.registerUpdateHandler(mPhysicsWorld);
		mPhysicsWorld.setContactListener(contactListener());
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// if the action is down , the player should jump
		if (pSceneTouchEvent.isActionDown()) {
			player.jump();
			// open mouth
			// player.eat();
			//
			// if (keepRunningModifier != null) {
			// keepRunningModifier.reset();
			// }
			// keepRunningModifier = new DelayModifier(0.5f);
			//
			// keepRunningModifier
			// .addModifierListener(new IModifierListener<IEntity>() {
			//
			// @Override
			// public void onModifierStarted(
			// IModifier<IEntity> pModifier, IEntity pItem) {
			//
			// }
			//
			// @Override
			// public void onModifierFinished(
			// IModifier<IEntity> pModifier, IEntity pItem) {
			// player.setRunning();
			//
			// }
			//
			// });
			// player.registerEntityModifier(keepRunningModifier);
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

		return false;
	}

	private ContactListener contactListener() {
		ContactListener contactListener = new ContactListener() {

			@Override
			public void beginContact(Contact contact) {
				Body a = contact.getFixtureA().getBody();
				Body b = contact.getFixtureB().getBody();
				// collision between player and ground
				if (a.getUserData() != null && b.getUserData() != null) {
					if (a.getUserData().equals("player")
							&& b.getUserData().equals("ground")
							|| b.getUserData().equals("player")
							&& a.getUserData().equals("ground")) {

						player.canEat = false;
						player.canJump = true;
						player.setRunning();
					}

					// collision between bull and ground
					if (a.getUserData().equals("bull")
							&& b.getUserData().equals("ground")
							|| b.getUserData().equals("bull")
							&& a.getUserData().equals("ground")) {
						bull.setRunning();
					}
					// collision between player and coin
					if (a.getUserData().equals("player")
							&& b.getUserData().equals("bull")
							|| a.getUserData().equals("bull")
							&& b.getUserData().equals("player")) {
						// resourceManager.coinCollect.play();
						// update the score
						/*
						 * if (player.canEat) { score += 5;
						 * textScore.setText("Coins: " + score);
						 * 
						 * // detect which body was colaided
						 * if(a.getUserData().equals("tomato")) { removeBody(a);
						 * } else if (b.getUserData().equals("tomato")) {
						 * removeBody(b); } } else { if
						 * (a.getUserData().equals("tomato")) {
						 * 
						 * fadeOutBody(a); } else if
						 * (b.getUserData().equals("tomato")) { fadeOutBody(b);
						 * }
						 * 
						 * }
						 */
						showLevelFailed();
					}
					if (a.getUserData().equals("player")
							&& b.getUserData().equals("fence")
							|| a.getUserData().equals("fence")
							&& b.getUserData().equals("player")) {
						//
						// // call level failed
						
						//
					}
					if (a.getUserData().equals("bull")
							&& b.getUserData().equals("fence")
							|| a.getUserData().equals("fence")
							&& b.getUserData().equals("bull")) {
						if (a.getUserData().equals("fence")) {
							removeFence(a);
						}else if(b.getUserData().equals("fence")){
							removeFence(b);
							
						}

					}
					//
					// if (a.getUserData().equals("player")
					// && b.getUserData().equals("finishline")
					// || a.getUserData().equals("finishline")
					// && b.getUserData().equals("player")) {
					//
					// // call level passed
					// showLevelCleared();
					//
					// }

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

		for (int i = 0; i < vegetables.size(); i++) {
			if (vegetables.get(i).getUserData() == body) { // if the coin is the
															// coin
				// the player touchs,
				// then delete it
				detachChild(vegetables.get(i));

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
		// // Add menu item
		// final IMenuItem restartButton = new ScaleMenuItemDecorator(
		// new SpriteMenuItem(RESTART, resourceManager.restartButton, vbom),
		// 1.2f, 1);
		// final IMenuItem quitButton = new ScaleMenuItemDecorator(
		// new SpriteMenuItem(QUIT, resourceManager.quitButton, vbom),
		// 1.2f, 1);
		//
		// create the background
		Sprite bg = new Sprite(0, 0, resourceManager.failedBG, vbom);
		//
		levelFailed.attachChild(bg);
		levelFailed.setBackgroundEnabled(false);// to see our scena throught the
		// background

		// levelFailed.addMenuItem(restartButton);
		// levelFailed.addMenuItem(quitButton);
		//
		// levelFailed.buildAnimations();
		// levelFailed.setOnMenuItemClickListener(this);
	}

	//
	// private void createLevelClearedScene() {
	// levelCleared = new MenuScene(camera);
	// levelCleared.setPosition(0, 0);
	//
	// final IMenuItem restartButton = new ScaleMenuItemDecorator(
	// new SpriteMenuItem(RESTART, resourceManager.restartButton, vbom),
	// 1.2f, 1);
	// final IMenuItem quitButton = new ScaleMenuItemDecorator(
	// new SpriteMenuItem(QUIT, resourceManager.quitButton, vbom),
	// 1.2f, 1);
	//
	// Sprite bg = new Sprite(0, 0, resourceManager.passedBG, vbom);
	//
	// levelCleared.addMenuItem(restartButton);
	// levelCleared.addMenuItem(quitButton);
	//
	// levelCleared.buildAnimations();
	//
	// levelCleared.setOnMenuItemClickListener(this);
	// }
	//
	private void showLevelFailed() {

		this.setChildScene(levelFailed, false, true, true);
	}

	//
	// private void showLevelCleared() {
	// this.setChildScene(levelCleared, false, true, true);
	// }

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {

		switch (pMenuItem.getID()) {

		case RESTART:

			// restart
			restartLevel();
			break;
		case QUIT:

			// go to main menu
			quit();
			break;
		}

		return false;
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
		this.dispose();

		// reset the camera
		resourceManager.camera.setCenter(0, 0);

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

	private void clearVegetables() {
		for (int i = 0; i < vegetables.size(); i++) {
			if (!vegetables.get(i).hasParent()) {
				vegetables.remove(i);
			}
		}
	}

	private void addTomatoObject() {

		if (vegetables.size() < 2) {
			Tomato tomato = new Tomato(700, 50, 50, 50, vbom, camera,
					mPhysicsWorld) {

				@Override
				public void onDie() {
					this.setVisible(false);
					getBody().setActive(false);
					this.setIgnoreUpdate(true);
					removeBody((Body) this.getUserData());

					// for (int i = 0; i < vegetables.size(); i++) {
					// if (vegetables.get(i).getUserData() ==
					// this.getUserData()) { // if the coin is the
					// // coin
					// // the player touchs,
					// // then delete it
					// detachChild(vegetables.get(i));
					// vegetables.remove(i);
					// }
					// }
				}
			};

			FixtureDef tomatoFixtureDef = PhysicsFactory.createFixtureDef(0, 0,
					0f);
			Body body = PhysicsFactory.createBoxBody(mPhysicsWorld, tomato,
					BodyType.DynamicBody, tomatoFixtureDef);
			body.setUserData("tomato");

			// animate the player
			vegetables.add(tomato);
			tomato.setUserData(tomato.getBody());
			tomato.setRunning();
			for (int i = 0; i < vegetables.size(); i++) {
				if (!vegetables.get(i).hasParent()) {
					this.attachChild(vegetables.get(i));
				}
			}
		}

	}

}
