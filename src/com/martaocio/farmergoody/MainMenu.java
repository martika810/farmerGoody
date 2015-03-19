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
	private static final int ITEM_SESSION_DIM_Y=102;
	private static final int OUT_OF_SCREEN_X = 900;
	private static final int OUT_OF_SCREEN_Y = 900;
	private static final int NUMBER_COLUMNS = 2;
	private MenuScene menuChildScene;// menu that can have buttons
	private IMenuItem shopMenuItem;
	private IMenuItem playMenuItem;
	// private MenuScene shopChildScene;
	private Sprite bg;
	private Sprite sessionBg;
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
	private List<Sprite> sessionItems=new ArrayList<>();

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

		bg = new Sprite(0, 0, resourceManager.shopMenuBackGround, vbom);
		Sprite backMenuItem = new Sprite(0, 0, resourceManager.leftArrowTexture, vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
				if (pSceneTouchEvent.isActionDown() && isShopMenuOnScreen) {
					hideShopMenuScene();
				}
				return true;
			};
		};
		backMenuItem.setPosition(600, 50);

		bg.attachChild(backMenuItem);

		Text creditText = new Text(230, 80, resourceManager.font, "Credit: "
				+ (UserState.getInstance().getLastModifiedSession() == null ? 0 : UserState.getInstance().getLastModifiedSession()
						.getCurrentMoney()) + "$", new TextOptions(HorizontalAlign.CENTER), vbom);
		bg.attachChild(creditText);

		populateShopMenu();

		this.menuChildScene.registerTouchArea(backMenuItem);

	}

	private void populateShopMenu() {

		int indexOfItemToPlace = 0;

		for (Vehicle vehicle : Vehicle.values()) {
			if (!vehicle.equals(Vehicle.NONE)) {
				boolean alreadyHad = UserState.getInstance().getAvailableVehicles().contains(vehicle);

				boolean haveEnoughtMoneyToBuy = (UserState.getInstance().getLastModifiedSession() == null ? false : UserState.getInstance()
						.getLastModifiedSession().getCurrentMoney() > vehicle.getPrice());
				boolean canBuy = !alreadyHad && haveEnoughtMoneyToBuy;

				Sprite item = createShopMenuItem(vehicle, alreadyHad, canBuy);

				int colToPlaceItem = indexOfItemToPlace / NUMBER_COLUMNS;
				int rowToPlaceItem = indexOfItemToPlace % NUMBER_COLUMNS;
				int positionXToPlaceItem = ORIGIN_X + PADDING_X + (ITEM_DIM_X * rowToPlaceItem);
				int positionYToPlaceItem = ORIGIN_Y + PADDING_Y + (ITEM_DIM_Y * colToPlaceItem);

				item.setPosition(positionXToPlaceItem, positionYToPlaceItem);
				bg.attachChild(item);
				indexOfItemToPlace++;

			}
		}
	}

	private void populateSessionMenu() {
		if (UserState.getInstance().getSessions().isEmpty())
			UserState.getInstance().initSessions();
		int indexItem=0;
		for (GameSession session : UserState.getInstance().getSessions()) {

			Sprite sessionItem = createSessionMenuItem(session,indexItem);
			sessionBg.attachChild(sessionItem);
			if(sessionItems==null ){
				sessionItems=new ArrayList<>();
			}
			sessionItems.add(sessionItem);
			indexItem++;
		}

	}
	
	private void updateSessionItem(GameSession session,int menuIndexToUpdate){
		Sprite sessionMenuItemToUpdate=sessionItems.get(menuIndexToUpdate);
		sessionMenuItemToUpdate.detachSelf();
		sessionMenuItemToUpdate.dispose();
		sessionItems.remove(menuIndexToUpdate);
		Sprite newSessionMenuItem=createSessionMenuItem(session, menuIndexToUpdate);
		sessionBg.attachChild(newSessionMenuItem);
		sessionItems.add(newSessionMenuItem);
	}

	private Sprite createSessionMenuItem(GameSession session,int indexItem) {
		Sprite sessionItem = new Sprite(0,0, resourceManager.sessionMenuItem, vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
				if (pSceneTouchEvent.isActionDown() && isSessionMenuOnScreen) {
					//setScale(1.2f);
					hideSessionMenuScene();
					UserState.getInstance().setSelectedSession(0);
					SceneManager.getInstance().createGameScene();
				}
				return true;
			};
		};
		sessionItem.setPosition(ORIGIN_SESSIONMENU_X, ORIGIN_Y+PADDING_Y+(ITEM_SESSION_DIM_Y*indexItem));
		Sprite levelImage=new Sprite(20,10,resourceManager.levelIcon,vbom);
		sessionItem.attachChild(levelImage);
		
		Text levelText = new Text(120, 30, resourceManager.font, "LEVEL "+session.getCurrentLevel(), new TextOptions(
				HorizontalAlign.CENTER), vbom);
		sessionItem.attachChild(levelText);
		
		Sprite vehicleIcon=createVehiculeIcon(session.getVehicleUsed());
		vehicleIcon.setPosition(200,10);
		sessionItem.attachChild(vehicleIcon);
		
		this.menuChildScene.registerTouchArea(sessionItem);

		return sessionItem;

	}
	
	private Sprite createVehiculeIcon(Vehicle vehicleSelected){
		if(vehicleSelected.equals(Vehicle.UNICYCLE)){
			return new Sprite(0,0,resourceManager.iconUnicycle,vbom);
		}
		return new Sprite(0,0,resourceManager.vehicleNoImage,vbom);
			
		
	}

	private void createSessionMenu() {

		sessionBg = new Sprite(0, 0, ResourceManager.getInstance().sessionMenuBackground, vbom);
		Sprite backMenuItem = new Sprite(0, 0, ResourceManager.getInstance().leftArrowTexture, vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
				if (pSceneTouchEvent.isActionDown() && isSessionMenuOnScreen) {
					setScale(1.2f);
					hideSessionMenuScene();
				}
				return true;
			};
		};
		backMenuItem.setPosition(600, 300);

		sessionBg.attachChild(backMenuItem);
		this.menuChildScene.registerTouchArea(backMenuItem);

		populateSessionMenu();
	}

	private Sprite createShopMenuItem(final Vehicle vehicle, boolean alreadyHad, boolean canBuy) {
		Sprite itemBackground = new Sprite(0, 0, resourceManager.shopItemMenuBackground, vbom);
		Sprite vehicleImage = new Sprite(0, 0, resourceManager.unycleImage, vbom);
		Text priceText = new Text(70, 150, resourceManager.font, new Integer(vehicle.getPrice()).toString() + "$", new TextOptions(
				HorizontalAlign.CENTER), vbom);
		if (canBuy && !alreadyHad) {
			Sprite buyBtn = new Sprite(0, 0, resourceManager.buyBtn, vbom) {
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
					if (pSceneTouchEvent.isActionDown() && isShopMenuOnScreen) {
						//setScale(1.2f);
						UserState.getInstance().setSelectedSession(0);
						UserState.getInstance().getSelectedSession().setVehicleUsed(vehicle);
						int newCurrentCreadit=UserState.getInstance().getSelectedSession().getCurrentMoney()-vehicle.getPrice();
						UserState.getInstance().getSelectedSession().setCurrentMoney(newCurrentCreadit);
						UserState.getInstance().saveToFile();
						updateSessionItem(UserState.getInstance().getSelectedSession(), 0);
						this.registerEntityModifier(getBuyBtnEntityModifier());
					}
					return true;
				};
			};
			
			buyBtn.setPosition(130, -10);
			this.menuChildScene.registerTouchArea(buyBtn);
			itemBackground.attachChild(buyBtn);
		}
		vehicleImage.setPosition(40, 30);
		itemBackground.attachChild(vehicleImage);
		itemBackground.attachChild(priceText);

		return itemBackground;
	}
	private SequenceEntityModifier getBuyBtnEntityModifier(){
		
		return new SequenceEntityModifier(
				new ScaleModifier(0.5f, 1f, 1.2f) ,
				new DelayModifier(0.2f),
				new ScaleModifier(0.2f, 1f, 0f)
				){
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
		                pItem.setVisible(false);
		        }
		};
	}

	private void showShopMenuScene() {
		// this.setChildScene(shopChildScene, false, true,true);
		isShopMenuOnScreen = true;
		bg.registerEntityModifier(new MoveModifier(0.3f, camera.getWidth(), 0, 0, 0));
		if (!bg.hasParent()) {
			this.menuChildScene.attachChild(bg);
		}

		shopMenuItem.setVisible(false);
		areMenuItemEnabled = false;
		// shopMenuItem.setPosition(OUT_OF_SCREEN_X, OUT_OF_SCREEN_Y);

	}

	private void showSessionMenuScene() {
		sessionBg.registerEntityModifier(new MoveModifier(0.3f, 0, 0, camera.getHeight(), 0));
		if (!sessionBg.hasParent()) {
			this.menuChildScene.attachChild(sessionBg);
		}
		shopMenuItem.setVisible(false);
		playMenuItem.setVisible(false);
		areMenuItemEnabled = false;
		isSessionMenuOnScreen = true;
		// shopMenuItem.setPosition(OUT_OF_SCREEN_X, OUT_OF_SCREEN_Y);
	}

	private void hideShopMenuScene() {
		bg.registerEntityModifier(new MoveModifier(0.3f, 0, camera.getWidth(), 0, 0));
		shopMenuItem.setVisible(true);// move button back to screen
		playMenuItem.setVisible(true);
		areMenuItemEnabled = true;
		isShopMenuOnScreen = false;
		// shopMenuItem.setPosition(650, 370);

	}

	private void hideSessionMenuScene() {
		sessionBg.registerEntityModifier(new MoveModifier(0.3f, 0, 0, 0, camera.getHeight()));
		shopMenuItem.setVisible(true);// move button back to screen
		playMenuItem.setVisible(true);
		areMenuItemEnabled = true;
		isSessionMenuOnScreen = false;
		// shopMenuItem.setPosition(650, 370);

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
		return false;
	}

}
