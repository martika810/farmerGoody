package com.martaocio.farmergoody.scenes;

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

import com.martaocio.farmergoody.ResourceManager;
import com.martaocio.farmergoody.SceneManager;
import com.martaocio.farmergoody.SessionSubMenu;
import com.martaocio.farmergoody.ShopSubMenu;
import com.martaocio.farmergoody.Util;
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
	private IMenuItem shopMenuItem,helpMenuItem;
	//private IMenuItem playMenuItem;
	private Sprite playMenuItem;
	// private MenuScene shopChildScene;
	private ShopSubMenu shopBg;
	private SessionSubMenu sessionBg;
	private final int PLAY = 0;// to recognize when the play buttons is gonna be
								// played
	private final int CONTINUE = 1;
	private final int BACK = 2;
	private final int HELP = 3;
	private final int SELECT_SESSION1 = 3;
	private final int SELECT_SESSION2 = 4;
	private final int SELECT_SESSION3 = 5;
	private boolean areMenuItemEnabled = true;
	private boolean isShopMenuOnScreen = false;
	private boolean isSessionMenuOnScreen = false;
	private boolean hasBoughtSomething;

	@Override
	public void createScene() {
		activity.runOnUiThread(new Runnable(){
			@Override
			public void run(){
				activity.showFatJackAdvert();
			}
		});
	
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
	public void update(){
		createShopScene();
		createSessionMenu();
	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub

	}

	@Override
	public SceneType getSceneType() {
		
		return SceneType.SCENE_MENU;
	}

	@Override
	public void disposeScene() {
		
		this.clearTouchAreas();
		this.clearEntityModifiers();
		this.clearUpdateHandlers();
		shopBg.dispose();
		sessionBg.dispose();
		this.dispose();
		

	}

	private void createShopScene() {

		shopBg = new ShopSubMenu(0, 0, resourceManager.shopMenuBackGround, vbom, menuChildScene, this);
		shopBg.setCullingEnabled(true);
		shopBg.createMenu();

	}

	private void createSessionMenu() {

		sessionBg = new SessionSubMenu(0, 0, ResourceManager.getInstance().sessionMenuBackground, vbom, menuChildScene, this, camera,engine,activity);
		//sessionBg = SessionSubMenu.getInstance(camera, vbom, menuChildScene, this);
		//sessionBg.setCullingEnabled(true);
		sessionBg.createMenu();

	}

	private void showShopMenuScene() {
		// this.setChildScene(shopChildScene, false, true,true);
		isShopMenuOnScreen = true;
		shopBg.setHasBoughtSomething(false);
		shopBg.registerEntityModifier(new MoveModifier(0.3f, camera.getWidth(), 0, 0, 0));
		if (!shopBg.hasParent()) {
			this.menuChildScene.attachChild(shopBg);
		}
		hideMainMenuControllers();
		// shopMenuItem.setPosition(OUT_OF_SCREEN_X, OUT_OF_SCREEN_Y);

	}

	public void showSessionMenuScene() {
		//sessionBg=SessionSubMenu.getInstance(camera, vbom, menuChildScene, this);
		this.menuChildScene.setOnSceneTouchListenerBindingOnActionDownEnabled(false);
		sessionBg.registerEntityModifier(new MoveModifier(0.3f, 0, 0, camera.getHeight(), 0));
		
		if (!sessionBg.hasParent()) {
			this.menuChildScene.attachChild(sessionBg);
		}
		hideMainMenuControllers();
		isSessionMenuOnScreen = true;
		// shopMenuItem.setPosition(OUT_OF_SCREEN_X, OUT_OF_SCREEN_Y);
	}

	public void hideShopMenuScene() {
		shopBg.registerEntityModifier(new MoveModifier(0.3f, 0, camera.getWidth(), 0, 0));
		showMainMenuControllers();
		isShopMenuOnScreen = false;
		if(shopBg.isHasBoughtSomething()){
			sessionBg.updateSelectVehiculeMenu();
		}
		// shopMenuItem.setPosition(650, 370);

	}

	public void hideSessionMenuScene() {
		//sessionBg=SessionSubMenu.getInstance(camera, vbom, menuChildScene, this);
		this.menuChildScene.setOnSceneTouchListenerBindingOnActionDownEnabled(true);
		sessionBg.registerEntityModifier(new MoveModifier(0.3f, 0, 0, 0, camera.getHeight()));
		showMainMenuControllers();
		isSessionMenuOnScreen = false;
		// shopMenuItem.setPosition(650, 370);

	}
	public void showMainMenuControllers(){
		shopMenuItem.setVisible(true);// move button back to screen
		playMenuItem.setVisible(true);
		helpMenuItem.setVisible(true);
		areMenuItemEnabled = true;
	}
	
	public void hideMainMenuControllers(){
		shopMenuItem.setVisible(false);
		playMenuItem.setVisible(false);
		helpMenuItem.setVisible(false);
		areMenuItemEnabled = false;
	}

	private void createMenuScene() {

		this.menuChildScene = new MenuScene(camera);
		this.menuChildScene.setPosition(0, 0);

		// create the menu buttons
		// when the button is clicked , it is scaled it to 1.2
		//playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(this.PLAY, resourceManager.playMenuButton, vbom), 1.7f, 1.5f);
		playMenuItem = new Sprite(325,165,150,150,resourceManager.playMenuButton,vbom);
//		{
//			@Override
//			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, final float X, final float Y) {
//				if(!isSessionMenuOnScreen && !isShopMenuOnScreen && !sessionBg.isSelectVehicleMenuOnScreen() ){
//					showSessionMenuScene();
//					
//				}
//				return true;
//			}
//			
//		};
		playMenuItem.setVisible(true);
		shopMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(this.CONTINUE, resourceManager.shopMenuButton, vbom), 1.2f, 1);
		
		helpMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(this.HELP, resourceManager.helpButton, vbom), 1.2f, 1);

		

		this.menuChildScene.buildAnimations();
		this.menuChildScene.setBackgroundEnabled(false);
		
		//this.menuChildScene.addMenuItem(playMenuItem);
		this.menuChildScene.attachChild(playMenuItem);
		this.menuChildScene.addMenuItem(shopMenuItem);
		this.menuChildScene.addMenuItem(helpMenuItem);

		//playMenuItem.setPosition(325,165);
		shopMenuItem.setPosition(530, 370);
		helpMenuItem.setPosition(650, 370);
		this.menuChildScene.setOnMenuItemClickListener(this);
		this.menuChildScene.setOnSceneTouchListener(this);
		//this.menuChildScene.registerTouchArea(playMenuItem);

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
				
			case HELP:
				// load game
				// SceneManager.getInstance().createGameScene();
				SceneManager.getInstance().createInstructions(engine);
				return true;
			case CONTINUE:
				// UserState.getInstance().clear();
				// SceneManager.getInstance().createGameScene();
				showShopMenuScene();
				return true;
			case BACK:
				hideShopMenuScene();
				return true;
				
			default:
				return false;
			}
		} else {
			return false;
		}

	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		float x = pSceneTouchEvent.getX();
		float y = pSceneTouchEvent.getY();
		
		boolean isMainMenuOnScreen = !isSessionMenuOnScreen && !isShopMenuOnScreen && ! sessionBg.isSelectVehicleMenuOnScreen();
		
		if(isMainMenuOnScreen){
			if(isPlayButton(x, y)){
				showSessionMenuScene();
			}
			return true;
		}
		if(isSessionMenuOnScreen){
			if(sessionBg.wasDeleteButtonTouched(x, y)!=null){
				sessionBg.deleteGameSession(sessionBg.wasDeleteButtonTouched(x, y));
			}
			if(sessionBg.wasPlayButtonTouched(x, y)!=null){
				sessionBg.playGame(sessionBg.wasPlayButtonTouched(x, y));
			}
			if(sessionBg.wasVehiculeButtonTouched(x, y)!=null){
				sessionBg.displaySelectVehicleMenu(sessionBg.wasVehiculeButtonTouched(x, y));
			}
			
			return true;
		}
		if(isShopMenuOnScreen){
			if(shopBg.wasBuyButtonTouched(x, y)!=null){
				shopBg.buyVehicle(shopBg.wasBuyButtonTouched(x, y));
			}
			return true;
		}
		if(sessionBg.isSelectVehicleMenuOnScreen()){
			if(sessionBg.wasVehiculeToSelectButtonTouched(x, y)!=null){
				sessionBg.selectVehicle(sessionBg.wasVehiculeToSelectButtonTouched(x, y));
			}
			return true;
		}
		
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
	
	private boolean isPlayButton(float x,float y){
		return Util.isPointWithinSprite(x, y, playMenuItem);
	}
	
	

}
