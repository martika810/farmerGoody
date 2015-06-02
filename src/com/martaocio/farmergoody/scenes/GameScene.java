package com.martaocio.farmergoody.scenes;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.MoveXModifier;
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
import org.andengine.entity.sprite.batch.SpriteBatch;
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
import org.andengine.input.touch.detector.ContinuousHoldDetector;
import org.andengine.input.touch.detector.HoldDetector;
import org.andengine.input.touch.detector.HoldDetector.IHoldDetectorListener;
import org.andengine.input.touch.detector.SurfaceGestureDetector;
import org.andengine.input.touch.detector.SurfaceGestureDetectorAdapter;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.entity.modifier.PathModifier.Path;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.debug.Debug;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.IModifier.IModifierListener;

import android.content.Context;
import android.hardware.SensorManager;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.martaocio.farmergoody.BodyGroups;
import com.martaocio.farmergoody.FJGestureDetector;
import com.martaocio.farmergoody.MainGameActivity;
import com.martaocio.farmergoody.ResourceManager;
import com.martaocio.farmergoody.RockPool;
import com.martaocio.farmergoody.SceneManager;
import com.martaocio.farmergoody.SpriteTag;
import com.martaocio.farmergoody.SceneManager.SceneType;
import com.martaocio.farmergoody.customsprites.Bull;
import com.martaocio.farmergoody.customsprites.Player;
import com.martaocio.farmergoody.customsprites.RockSprite;
import com.martaocio.farmergoody.customsprites.TomatoScorer;
import com.martaocio.farmergoody.domain.GameSession;
import com.martaocio.farmergoody.domain.LevelType;
import com.martaocio.farmergoody.domain.UserState;
import com.martaocio.farmergoody.domain.Vehicle;
import com.martaocio.farmergoody.providers.AchievementHelper;
import com.martaocio.farmergoody.providers.LevelProvider;
import com.martaocio.farmergoody.providers.TomatoResourceHelper;
import com.martaocio.farmergoody.util.GameUtils;
import com.martaocio.farmergoody.util.Util;

public class GameScene extends BaseScene implements IOnSceneTouchListener, IOnMenuItemClickListener {

	protected final int RESTART = 0;
	protected final int QUIT = 1;
	protected final int KEEP_PLAYING = 2;
	protected final int UNPAUSE = 3;
	private final int TAG_REWARD_ICON = 999;
	protected final int TAG_TOMAT_ICON = 998;
	protected final int TAG_SCORE_TEXT = 997;
	protected final int SCREEN_WIDTH = 800;
	protected final int SCREEN_HEIGHT = 480;

	protected TMXTiledMap mTMXTiledMap;
	protected TMXLayer tmxLayer;

	protected PhysicsWorld mPhysicsWorld;

	protected LinkedList<Body> fencesBodies;
	protected LinkedList<Sprite> tomatos;
	protected LinkedList<Sprite> lifes;
	protected LinkedList<Sprite> pathScoreIndicators;

	Rectangle[] rec = new Rectangle[250];
	private int i = 0;

	protected boolean isGameVisible = true;

	private SurfaceGestureDetector gestureDetector;

	protected RockPool rockPool;

	// private LinkedList<Stair> stairs;

	public Text textScore, textNumberLife, textMoney;

	public Text textLevel;
	public boolean isDrawing = false;
	public boolean wasPlayerTap = false;
	public boolean wasSingleTap = false;
	public boolean wasSwiping = false;

	public TomatoScorer tomatoScoreIcon;
	public Sprite levelIcon, lifeIndicator, moneyIndicator;
	public Sprite rightPathScoreIcon;
	public Sprite jumpButton,turboButton;
	public Sprite pauseButton;
	public Sprite restartButton;
	public Sprite leftButton;
	public Sprite rightButton;
	public Sprite moneyBag100Indicator;
	// public final SpriteBatch rockLine=new
	// SpriteBatch(resourceManager.gameTexturesAtlas, 1000, vbom);
	public SpriteBatch tomatoSprite = new SpriteBatch(resourceManager.gameTexturesAtlas, 100, vbom);

	public boolean wasMoneyBag100IndicatorsShown, wasMoneyBag200IndicatorsShown = false;

	public int percentage = 0;

	public Sprite title;
	protected HUD hud;

	protected Player player;
	protected Bull bull;

	protected int score = 10;
	protected int levelTotalPoints;
	// create 2 new menuScene
	protected MenuScene levelFailed, levelCleared, levelPause, levelNoMoney;

	@Override
	public void createScene() {
		
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				((MainGameActivity)activity).removeSignInGoogleButton();
				
			}});

		createPhysicsWorld();
		createLevel(UserState.getInstance().getSelectedSession().getCurrentLevel());
		createLevelFailedScene();
		// createLevelClearedScene();
		// createLevelNoMoneyScene();
		createPauseScene();

	}

	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().loadMainMenu(engine);

	}

	@Override
	public SceneType getSceneType() {

		return SceneType.SCENE_GAME;
	}

	@Override
	public void disposeScene() {
		quit();

	}

	private void createLevel(int levelNumber) {

		RockPool.prepareRockPool(resourceManager.rockLineTexture, vbom, camera, this);
		rockPool = RockPool.getInstance();

		// create the text for the score

		createGameIndicators(levelNumber);

		createButtons();

		tomatos = new LinkedList<Sprite>();
		lifes = new LinkedList<Sprite>();
		fencesBodies = new LinkedList<Body>();
		pathScoreIndicators = new LinkedList<Sprite>();
		try {
			TMXLoader tmxLoader = new TMXLoader(activity.getAssets(), activity.getTextureManager(),
					TextureOptions.BILINEAR_PREMULTIPLYALPHA, vbom);

			this.mTMXTiledMap = tmxLoader.loadFromAsset(LevelProvider.getTXMLevel(levelNumber));

		} catch (TMXLoadException e) {

			Debug.e(e);
			e.printStackTrace();
		}

		// Create all the objects from the TMXLayer
		createObjectTMXLayer();

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

		for (Sprite life : lifes) {
			this.attachChild(life);
		}

		kickoffPlayer();

		kickoffBull();

		// that heads up the display
		hud = new HUD();

		hud.attachChild(levelIcon);
		hud.attachChild(textLevel);
		hud.attachChild(tomatoScoreIcon);
		hud.attachChild(textScore);
		hud.attachChild(turboButton);

		hud.attachChild(lifeIndicator);
		hud.attachChild(moneyIndicator);
		hud.attachChild(textNumberLife);
		hud.attachChild(textMoney);

		hud.attachChild(title);
		hud.attachChild(jumpButton);
		hud.attachChild(pauseButton);
		hud.attachChild(restartButton);
		hud.attachChild(moneyBag100Indicator);

		hud.registerTouchArea(turboButton);
		hud.registerTouchArea(jumpButton);
		hud.registerTouchArea(pauseButton);
		hud.registerTouchArea(restartButton);

		// setSpriteCulling();

		setOnSceneTouchListener(this);
		this.setTouchAreaBindingOnActionMoveEnabled(true);
		this.setTouchAreaBindingOnActionDownEnabled(true);
		// activity.runOnUiThread(new Runnable() {
		// public void run() {
		// setupGestureDetection();
		// }
		// });

		// this.setOnSceneTouchListener(this.gestureDetector);

		this.camera.setHUD(hud);

		createUpdateHandler();

	}

	private void setupGestureDetection() {
		gestureDetector = new SurfaceGestureDetectorAdapter(activity.getApplicationContext()) {
			@Override
			protected boolean onSingleTap() {
				wasSingleTap = true;
				return false;
			}

			@Override
			protected boolean onDoubleTap() {

				return false;
			}

			@Override
			protected boolean onSwipeUp() {
				wasSwiping = true;
				return false;
			}

			@Override
			protected boolean onSwipeDown() {
				wasSwiping = true;
				return false;
			}

			@Override
			protected boolean onSwipeLeft() {
				wasSwiping = true;
				return false;
			}

			@Override
			protected boolean onSwipeRight() {
				wasSwiping = true;
				return false;
			}
		};
	}

	protected void createUpdateHandler() {
		this.registerUpdateHandler(new IUpdateHandler() {

			@Override
			public void onUpdate(float pSecondsElapsed) {
				boolean isBullAheadPlayer = bull.getX() > player.getX();
				if (isBullAheadPlayer) {
					if (UserState.getInstance().getSelectedSession().getNumberLifes() > 0) {
						updatePlayerNumberLifes(-1);
						bull.moveBack();
					} else {
						endGameByBullCatch();
					}

				}

			}

			@Override
			public void reset() {
				// TODO Auto-generated method stub

			}

		});

	}

	protected void kickoffPlayer() {

		player = createPlayer();
		// animate the player

		player.setRunning();

		this.attachChild(player);

	}

	protected void kickoffBull() {
		// Attach bull
		bull = new Bull(50, 150, 150, 150, vbom, camera, mPhysicsWorld);

		// animate the player
		bull.setRunning();
		this.attachChild(bull);
	}

	protected void createButtons() {
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
		
		
		turboButton = new Sprite(450, 380, 70, 70, ResourceManager.getInstance().turboButtonTexture, vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
				if (pSceneTouchEvent.isActionUp() && isGameVisible) {
					makePlayerTurbo();
				}
				return true;
			}
			
			
		};

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

	private void createGameIndicators(int levelNumber) {

		score = 10;
		GameSession selectSession = UserState.getInstance().getSelectedSession();
		int currentLevel = selectSession.getCurrentLevel();
		int currentNumberLifes = selectSession.getNumberLifes();
		int currentMoney = selectSession.getCurrentMoney();
		levelTotalPoints = LevelType.getLevelType(currentLevel).getTotalPoints();
		tomatoScoreIcon = new TomatoScorer(20, 20, 200, 80, vbom);
		lifeIndicator = new Sprite(20, 110, 80, 80, this.resourceManager.lifeIndicatorTexture, vbom);

		textNumberLife = new Text(lifeIndicator.getX() + 30, lifeIndicator.getY() + 20, this.resourceManager.font, currentNumberLifes
				+ "    ", new TextOptions(HorizontalAlign.CENTER), vbom);

		moneyIndicator = new Sprite(20, 190, 80, 80, this.resourceManager.moneyIndicatorTexture, vbom);
		textMoney = new Text(moneyIndicator.getX() + 10, moneyIndicator.getY() + 20, this.resourceManager.font,
				currentMoney + "$" + "    ", new TextOptions(HorizontalAlign.CENTER), vbom);
		levelIcon = new Sprite(20, 280, 70, 70, this.resourceManager.levelIcon, vbom);

		title = new Sprite(693, 5, 107, 50, this.resourceManager.title, vbom);

		textScore = new Text(40, 50, this.resourceManager.font, score + "    ", new TextOptions(HorizontalAlign.CENTER), vbom);
		textLevel = new Text(levelIcon.getX() + 20, levelIcon.getY() + 20, this.resourceManager.font, "#" + levelNumber + "    ",
				new TextOptions(HorizontalAlign.CENTER), vbom);
		tomatoScoreIcon.setTag(TAG_TOMAT_ICON);
		textScore.setTag(TAG_SCORE_TEXT);
		moneyBag100Indicator = new Sprite(SCREEN_WIDTH / 2 - 128, SCREEN_HEIGHT / 2 - 128, 256, 256, this.resourceManager.moneyBag100, vbom);
		moneyBag100Indicator.setVisible(false);

	}

	private void pause(boolean shouldPause) {
		this.setIgnoreUpdate(shouldPause);

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
					if (GameUtils.isCollisionBetween(a, b, SpriteTag.BULL, SpriteTag.LIFE)) {

						if (a.getUserData().equals(SpriteTag.LIFE)) {
							removeFence(a);
						} else if (b.getUserData().equals(SpriteTag.LIFE)) {
							removeFence(b);

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

					if (a.getUserData().equals(SpriteTag.BULL) && GameUtils.isBodyTomato(b) || b.getUserData().equals(SpriteTag.BULL)
							&& GameUtils.isBodyTomato(a)) {

						if (GameUtils.isBodyTomato(a)) {

							removeBody(a);
						} else if (GameUtils.isBodyTomato(b)) {
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

	private void updatePlayerNumberLifes(int pointToUpdate) {
		GameSession selectedSession = UserState.getInstance().getSelectedSession();
		int currentNumberLife = selectedSession.getNumberLifes() + pointToUpdate;
		selectedSession.setNumberLifes(currentNumberLife);
		textNumberLife.setText(currentNumberLife + "  ");

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

	private void removeLife(final Body body) {

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

	protected void createLevelNoMoneyScene() {

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
		// bg.setCullingEnabled(true);
		levelNoMoney.setBackgroundEnabled(false);// to see our scena throught
													// the
		// background

		levelNoMoney.addMenuItem(restartButton);
		levelNoMoney.addMenuItem(quitButton);

		levelNoMoney.buildAnimations();
		restartButton.setPosition(300, 270);
		quitButton.setPosition(400, 270);

		levelNoMoney.setOnMenuItemClickListener(this);
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

	protected void createLevelClearedScene() {
		levelCleared = new MenuScene(camera);
		levelCleared.setPosition(0, 0);

		IMenuItem playButton = new ScaleMenuItemDecorator(new SpriteMenuItem(KEEP_PLAYING, resourceManager.playButton, vbom), 1.2f, 0.9f);
		IMenuItem quitButton = new ScaleMenuItemDecorator(new SpriteMenuItem(QUIT, resourceManager.quitButton, vbom), 1.2f, 0.9f);

		Sprite bg = new Sprite(0, 0, resourceManager.passedBG, vbom);

		levelCleared.attachChild(bg);
		// bg.setCullingEnabled(true);
		levelCleared.setBackgroundEnabled(false);

		levelCleared.addMenuItem(playButton);
		levelCleared.addMenuItem(quitButton);

		levelCleared.buildAnimations();

		playButton.setPosition(300, 270);
		quitButton.setPosition(400, 270);

		levelCleared.setOnMenuItemClickListener(this);
	}

	private void showLevelFailed() {
		if (!activity.FREE_ADS) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					activity.addShareGoogleButton(UserState.getInstance().getSelectedSession());
					activity.showAdvert(true);
				}
			});
		}
		isGameVisible = false;
		showGameIndicators(false);
		this.hideControlButtons();
		this.setChildScene(levelFailed, false, true, true);

	}

	public void updateGameIndicators(int points) {
		score += points;
		textScore.setText("" + score);
		if (points > 0) {
			resourceManager.goodSound.play();
		} else {
			resourceManager.badSound.play();
		}
		percentage = (score * 100) / levelTotalPoints;

		if (percentage > 50 && percentage < 80 && !wasMoneyBag100IndicatorsShown) {
			showMoneyBag100Indicator();
			updateCurrentMoney(100);
		}
		if (percentage > 80 && !wasMoneyBag200IndicatorsShown) {
			showMoneyBag100Indicator();
			updateCurrentMoney(100);
		}

		tomatoScoreIcon.update(percentage);

	}

	private void updateCurrentMoney(int money) {
		UserState currentUserState = UserState.getInstance();

		int currentMoney = currentUserState.getSelectedSession().getCurrentMoney()+100;
		textMoney.setText(currentMoney + "$");
		currentUserState.getSelectedSession().setCurrentMoney(currentMoney);
		AchievementHelper.getInstance(activity).checkAchievements(currentMoney);
		AchievementHelper.getInstance(activity).pushAchievements(activity);
		currentUserState.saveToFile();
	}

	// private void showLevelNoMoney() {
	// if (!activity.FREE_ADS) {
	// activity.runOnUiThread(new Runnable() {
	// @Override
	// public void run() {
	// activity.showAdvert(true);
	// }
	// });
	// }
	// isGameVisible = false;
	// createNoMonetMessages();
	// showGameIndicators(false);
	// this.hideControlButtons();
	// this.setChildScene(levelNoMoney, false, true, true);
	// }

	// private void showLevelCleared(final int moneyWon) {
	// if (!activity.FREE_ADS) {
	// activity.runOnUiThread(new Runnable() {
	// @Override
	// public void run() {
	// activity.showAdvert(true);
	// }
	// });
	// }
	// isGameVisible = false;
	// createWinningMesages(moneyWon);
	// showGameIndicators(false);
	// this.hideControlButtons();
	// this.setChildScene(levelCleared, false, true, true);
	// }

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

	protected void hideControlButtons() {
		// this.leftButton.setVisible(false);
		// this.rightButton.setVisible(false);
		this.jumpButton.setVisible(false);

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
			if(UserState.getInstance().getSelectedSession().getCurrentLevel()==1){
				
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

	protected void showGameIndicators(boolean shouldShow) {
		jumpButton.setVisible(shouldShow);
		title.setVisible(shouldShow);
		tomatoScoreIcon.setVisible(shouldShow);

		if (pauseButton != null) {
			lifeIndicator.setVisible(shouldShow);
			textNumberLife.setVisible(shouldShow);

		}
		if(turboButton !=null){
			turboButton.setVisible(shouldShow);
		}
		if (moneyIndicator != null) {
			moneyIndicator.setVisible(shouldShow);
			textMoney.setVisible(shouldShow);
		}

		textScore.setVisible(shouldShow);
		if (pauseButton != null)
			pauseButton.setVisible(shouldShow);
		if (levelIcon != null)
			levelIcon.setVisible(shouldShow);
		if (textLevel != null)
			textLevel.setVisible(shouldShow);

	}

	protected void quit() {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				activity.removeShareGoogleButton();
			}
		});
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

	public void restartLevel() {
		int currentPoints = UserState.getInstance().getSelectedSession().getCurrentMoney();
		//AchievementHelper.getInstance(activity).checkAchievements(activity, currentPoints);
		activity.submitLeaderBoard(currentPoints);
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				activity.removeShareGoogleButton();
			}
		});
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

	// private void setSpriteCulling() {
	// levelIcon.setCullingEnabled(true);
	// title.setCullingEnabled(true);
	//
	// }

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

	@Override
	public void update() {
		// TODO Auto-generated method stub

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
					PhysicsFactory.createBoxBody(mPhysicsWorld, rect, BodyType.StaticBody, BodyGroups.GROUND_FIXTURE_DEF).setUserData(SpriteTag.GROUND);

					// store data in userData so it can be retrieved later on

					rect.setVisible(false);
					attachChild(rect);
				} else if (object.getName().equals(SpriteTag.FENCE)) {
					Rectangle rect = new Rectangle(object.getX(), object.getY(), object.getWidth(), object.getHeight(), vbom);

					FixtureDef spikeFixtureDef = PhysicsFactory.createFixtureDef(0, 0, 0f);
					Body fenceBody = PhysicsFactory.createBoxBody(mPhysicsWorld, rect, BodyType.StaticBody, BodyGroups.FENCE_FIXTURE_DEF);
					fenceBody.setUserData(SpriteTag.FENCE);
					fencesBodies.add(fenceBody);

				} else if (object.getName().equals(SpriteTag.LIFE)) {
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
					PhysicsFactory.createBoxBody(mPhysicsWorld, rect, BodyType.StaticBody, BodyGroups.END_FIXTURE_DEF).setUserData(SpriteTag.END);

				}

			}

		}

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

	// private void createWinningMesages(final int moneyWon) {
	//
	// int nextLevel =
	// UserState.getInstance().getSelectedSession().getCurrentLevel();
	// boolean isTrainingLevel = nextLevel == 1;
	// if (isTrainingLevel) {
	// Text textYouAreReady = new Text(340, 370, this.resourceManager.font,
	// "Let's play!", new TextOptions(HorizontalAlign.CENTER),
	// vbom);
	//
	// levelCleared.attachChild(textYouAreReady);
	// } else {
	// Text textYouWon = new Text(340, 370, this.resourceManager.font,
	// "You won " + moneyWon + " $  ", new TextOptions(
	// HorizontalAlign.CENTER), vbom);
	// Text textNextLevel = new Text(340, 400, this.resourceManager.font,
	// "Next Level: " + nextLevel + " ", new TextOptions(
	// HorizontalAlign.CENTER), vbom);
	//
	// levelCleared.attachChild(textNextLevel);
	// levelCleared.attachChild(textYouWon);
	// }
	//
	// }

	// private void createNoMonetMessages() {
	// Text textNotEnoughTomatos = new Text(300, 370, this.resourceManager.font,
	// "Just " + percentage + " % of tomatos!", new TextOptions(
	// HorizontalAlign.CENTER), vbom);
	// Text textComeBack = new Text(300, 400, this.resourceManager.font,
	// "Come back for more!", new TextOptions(HorizontalAlign.CENTER),
	// vbom);
	// levelNoMoney.attachChild(textNotEnoughTomatos);
	// levelNoMoney.attachChild(textComeBack);
	// }

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
		
		player.registerEntityModifier(
				new SequenceEntityModifier(
						//new MoveXModifier(1f, player.getX(), player.getX()+400)
						new DelayModifier(1f)) {
			@Override
			protected void onModifierStarted(IEntity pItem) {
				super.onModifierStarted(pItem);
				player.runTurbo();
				player.isTurbo=true;

			}
			
			
			@Override
			protected void onModifierFinished(IEntity pItem) {
				super.onModifierFinished(pItem);
			
				player.isTurbo=false;
				player.setRunning();

			}
		});

	}


	private boolean isPlayerArea(float f, float g) {
		if ((f > player.getX() + 3 && f < (player.getX() + player.getWidth() - 3))
				&& (g > player.getY() + 3 && (g < player.getY() + player.getHeight() - 3))) {
			return true;
		}

		return false;
	}

	public Player getPlayer() {
		return this.player;
	}

}
