package com.martaocio.farmergoody;

import java.util.LinkedList;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.tmx.TMXLoader;
import org.andengine.extension.tmx.util.exception.TMXLoadException;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.debug.Debug;

import com.badlogic.gdx.physics.box2d.Body;
import com.martaocio.farmergoody.SceneManager.SceneType;

public class TrainingGame extends GameScene {

	private static final float POINT_FINGER_ANIMATION_START = 1280;
	private static final float POINT_DISPLAY_TAP_ON_PLAYER_TIP = 2500;
	private boolean wasTapPlayerExplanationShown = false;
	private boolean wasFingerExplanationShown = false;
	private Sprite finger;
	private Sprite tapPlayerExplanation;

	@Override
	public void createScene() {
		super.createPhysicsWorld();
		createLevel();
		super.createLevelFailedScene();
		super.createLevelNoMoneyScene();
		createLevelClearedScene();

	}

	private void createLevel() {

		RockPool.prepareRockPool(resourceManager.rockLineTexture, vbom, camera, this);
		rockPool = RockPool.getInstance();
		createGameIndicators();

		createButtons();

		tomatos = new LinkedList<Sprite>();
		fencesBodies = new LinkedList<Body>();
		pathScoreIndicators = new LinkedList<Sprite>();

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

		for (Sprite tomato : tomatos) {
			this.attachChild(tomato);

		}

		kickoffPlayer();

		kickoffBull();

		// that heads up the display
		HUD hud = new HUD();

		hud.attachChild(tomatoScoreIcon);
		hud.attachChild(textScore);

		hud.attachChild(title);
		hud.attachChild(jumpButton);

		hud.registerTouchArea(jumpButton);

		setOnSceneTouchListener(this);
		this.setTouchAreaBindingOnActionMoveEnabled(true);
		this.setTouchAreaBindingOnActionDownEnabled(true);

		this.camera.setHUD(hud);

		createUpdateHandler();

	}

	private void createGameIndicators() {
		score = 10;

		levelTotalPoints = LevelType.getLevelType(0).getTotalPoints();
		tomatoScoreIcon = new TomatoScorer(20, 100, 200, 80, vbom);
		title = new Sprite(693, 5, 107, 50, ResourceManager.getInstance().title, vbom);

		tomatoScoreIcon.setPosition(20, 20);
		textScore = new Text(40, 50, this.resourceManager.font, score + "    ", new TextOptions(HorizontalAlign.CENTER), vbom);

		tomatoScoreIcon.setTag(TAG_TOMAT_ICON);
		textScore.setTag(TAG_SCORE_TEXT);

	}

	protected void createUpdateHandler() {
		this.registerUpdateHandler(new IUpdateHandler() {

			@Override
			public void onUpdate(float pSecondsElapsed) {
				boolean isBullAheadPlayer = bull.getX() > player.getX();
				if (isBullAheadPlayer) {
					endGameByBullCatch();
				}
				if (player.getX() > POINT_FINGER_ANIMATION_START && !wasFingerExplanationShown) {
					createFingerAnimation();
				}
				if (player.getX() > POINT_DISPLAY_TAP_ON_PLAYER_TIP && player.getX() < POINT_DISPLAY_TAP_ON_PLAYER_TIP + 100
						&& !wasTapPlayerExplanationShown) {
					createTapPlayerExplanation();
				}

			}

			@Override
			public void reset() {
				// TODO Auto-generated method stub

			}

		});

	}

	@Override
	public SceneType getSceneType() {

		return SceneType.SCENE_TRAINING;
	}

	@Override
	public void disposeScene() {
		super.quit();

	}

	private void createFingerAnimation() {
		finger = new Sprite(player.getX()+200, 240, resourceManager.fingerTexture, vbom);
		this.attachChild(finger);

		finger.registerEntityModifier(createFingerSequenceModifier());
		wasFingerExplanationShown =true;
	}
	
	private void createTapPlayerExplanation(){
		tapPlayerExplanation=new Sprite(player.getX(),100,resourceManager.tapPlayerExplanation,vbom);
		this.attachChild(tapPlayerExplanation);
		
		tapPlayerExplanation.registerEntityModifier(new SequenceEntityModifier(
				new DelayModifier(4f)) {
			@Override
			protected void onModifierStarted(IEntity pItem) {
				super.onModifierStarted(pItem);
				
			}

			@Override
			protected void onModifierFinished(IEntity pItem) {
				super.onModifierFinished(pItem);
				tapPlayerExplanation.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
				tapPlayerExplanation.setVisible(false);
				wasTapPlayerExplanationShown=true;
				
			}
		});
	}

	protected void createButtons() {
		jumpButton = new Sprite(320, 380, 164, 70, ResourceManager.getInstance().jumpBtnTextute, vbom) {
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

	private void startGame() {

		// do some clean up
		super.mPhysicsWorld.clearForces();
		this.mPhysicsWorld.clearPhysicsConnectors();
		this.mPhysicsWorld.dispose();

		// some work with the scene
		this.clearTouchAreas();
		this.clearEntityModifiers();
		this.clearUpdateHandlers();
		this.dispose();

		SceneManager.getInstance().createGameFromTraining(engine);
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {

		switch (pMenuItem.getID()) {

		case RESTART:

			// restart
			startGame();

			break;
		case QUIT:
			// go to main menu
			quit();
			SceneManager.getInstance().loadMainMenu(engine);
			break;

		case KEEP_PLAYING:
			startGame();

			break;

		case UNPAUSE:

			reset();

			break;
		}

		return false;
	}

	private SequenceEntityModifier createFingerSequenceModifier() {

		SequenceEntityModifier fingerSequenceModifier = new SequenceEntityModifier(
				new MoveXModifier(4f, finger.getX(), finger.getX()+400), new MoveXModifier(1f,
				340, 240)) {
			@Override
			protected void onModifierStarted(IEntity pItem) {
				super.onModifierStarted(pItem);

			}

			@Override
			protected void onModifierFinished(IEntity pItem) {
				super.onModifierFinished(pItem);
				finger.dispose();
				finger.setVisible(false);
				
			}
		};
		return fingerSequenceModifier;
	}

}
