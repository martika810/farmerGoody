package com.martaocio.farmergoody;

import java.util.ArrayList;
import java.util.List;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.util.HorizontalAlign;

import com.martaocio.farmergoody.SceneManager.SceneType;

public class MainMenu extends BaseScene implements IOnMenuItemClickListener, IOnSceneTouchListener {

	private static final int ORIGIN_X = 200;
	private static final int ORIGIN_SESSIONMENU_X = 215;
	private static final int ORIGIN_Y = 90;
	private static final int PADDING_X = 10;
	private static final int PADDING_Y = 5;
	private static final int ITEM_DIM_X = 190;
	private static final int ITEM_DIM_Y = 190;
	private static final int ITEM_SESSION_DIM_Y = 102;
	private static final int OUT_OF_SCREEN_X = 900;
	private static final int OUT_OF_SCREEN_Y = 900;
	private static final int NUMBER_COLUMNS = 2;
	private MenuScene menuChildScene;// menu that can have buttons
	private IMenuItem shopMenuItem;
	private IMenuItem playMenuItem;
	// private MenuScene shopChildScene;
	private ShopSubMenu bg;
	private SessionSubMenu sessionBg;
	private final int PLAY = 0;// to recognize when the play buttons is gonna be
								// played
	private final int CONTINUE = 1;
	private final int BACK = 2;
	private final int SELECT_SESSION1 = 3;
	private final int SELECT_SESSION2 = 4;
	private final int SELECT_SESSION3 = 5;
	private boolean areMenuItemEnabled = true;
	private boolean isShopMenuOnScreen = false;
	private boolean isSessionMenuOnScreen = false;

	@Override
	public void createScene() {

		createMenuScene();
		createShopScene();
		createSessionMenu();
		// create background
		this.attachChild(new Sprite(0, 0, resourceManager.mainMenuBackground, vbom) {

			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither(); // the avoid pixalation
			}
		});

		// resourceManager.themeMusic.play();

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

	private void createShopScene() {

		bg = new ShopSubMenu(0, 0, resourceManager.shopMenuBackGround, vbom, menuChildScene, this);
		bg.setCullingEnabled(true);
		bg.createMenu();

	}

	private void createSessionMenu() {

		sessionBg = new SessionSubMenu(0, 0, ResourceManager.getInstance().sessionMenuBackground, vbom, menuChildScene, this, camera);
		sessionBg.setCullingEnabled(true);
		sessionBg.createMenu();

	}

	private void showShopMenuScene() {
		// this.setChildScene(shopChildScene, false, true,true);
		isShopMenuOnScreen = true;
		bg.registerEntityModifier(new MoveModifier(0.3f, camera.getWidth(), 0, 0, 0));
		if (!bg.hasParent()) {
			this.menuChildScene.attachChild(bg);
		}
		hideMainMenuControllers();
		// shopMenuItem.setPosition(OUT_OF_SCREEN_X, OUT_OF_SCREEN_Y);

	}

	public void showSessionMenuScene() {
		sessionBg.registerEntityModifier(new MoveModifier(0.3f, 0, 0, camera.getHeight(), 0));
		if (!sessionBg.hasParent()) {
			this.menuChildScene.attachChild(sessionBg);
		}
		hideMainMenuControllers();
		isSessionMenuOnScreen = true;
		// shopMenuItem.setPosition(OUT_OF_SCREEN_X, OUT_OF_SCREEN_Y);
	}

	public void hideShopMenuScene() {
		bg.registerEntityModifier(new MoveModifier(0.3f, 0, camera.getWidth(), 0, 0));
		showMainMenuControllers();
		isShopMenuOnScreen = false;
		// shopMenuItem.setPosition(650, 370);

	}

	public void hideSessionMenuScene() {
		
		sessionBg.registerEntityModifier(new MoveModifier(0.3f, 0, 0, 0, camera.getHeight()));
		showMainMenuControllers();
		isSessionMenuOnScreen = false;
		// shopMenuItem.setPosition(650, 370);

	}
	public void showMainMenuControllers(){
		shopMenuItem.setVisible(true);// move button back to screen
		playMenuItem.setVisible(true);
		areMenuItemEnabled = true;
	}
	
	public void hideMainMenuControllers(){
		shopMenuItem.setVisible(false);
		playMenuItem.setVisible(false);
		areMenuItemEnabled = false;
	}

	private void createMenuScene() {

		this.menuChildScene = new MenuScene(camera);
		this.menuChildScene.setPosition(0, 0);

		// create the menu buttons
		// when the button is clicked , it is scaled it to 1.2
		playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(this.PLAY, resourceManager.playMenuButton, vbom), 1.7f, 1.5f);

		shopMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(this.CONTINUE, resourceManager.continueMenuButton, vbom), 1.2f, 1);

		this.menuChildScene.addMenuItem(playMenuItem);
		this.menuChildScene.addMenuItem(shopMenuItem);

		this.menuChildScene.buildAnimations();
		this.menuChildScene.setBackgroundEnabled(false);

		playMenuItem.setPosition(360, 260);
		shopMenuItem.setPosition(650, 370);
		this.menuChildScene.setOnMenuItemClickListener(this);
		this.menuChildScene.setOnSceneTouchListener(this);

		// attach the play menu to the scene
		this.setChildScene(menuChildScene, false, true, true);
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {
		if (areMenuItemEnabled) {
			switch (pMenuItem.getID()) {

			case PLAY:
				// load game
				// SceneManager.getInstance().createGameScene();
				showSessionMenuScene();
				return true;
			case CONTINUE:
				// UserState.getInstance().clear();
				// SceneManager.getInstance().createGameScene();
				showShopMenuScene();
				return true;
			case BACK:
				hideShopMenuScene();
				return true;
				// case SELECT_SESSION1:
				// SceneManager.getInstance().createGameScene();
				// case SELECT_SESSION2:
				// SceneManager.getInstance().createGameScene();
				// case SELECT_SESSION3:
				// SceneManager.getInstance().createGameScene();
			default:
				return false;
			}
		} else {
			return false;
		}

	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		return true;
	}

	public MenuScene getMenuChildScene() {
		return menuChildScene;
	}

	public boolean isAreMenuItemEnabled() {
		return areMenuItemEnabled;
	}

	public boolean isShopMenuOnScreen() {
		return isShopMenuOnScreen;
	}

	public boolean isSessionMenuOnScreen() {
		return isSessionMenuOnScreen;
	}

}
