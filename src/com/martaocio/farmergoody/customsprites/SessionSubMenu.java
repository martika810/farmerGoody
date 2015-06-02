package com.martaocio.farmergoody.customsprites;

import java.util.ArrayList;
import java.util.List;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;

import com.martaocio.farmergoody.ResourceManager;
import com.martaocio.farmergoody.SceneManager;
import com.martaocio.farmergoody.domain.GameSession;
import com.martaocio.farmergoody.domain.LevelType;
import com.martaocio.farmergoody.domain.UserState;
import com.martaocio.farmergoody.domain.Vehicle;
import com.martaocio.farmergoody.providers.ImageProvider;
import com.martaocio.farmergoody.scenes.MainMenu;
import com.martaocio.farmergoody.util.Util;

import android.app.Activity;

public class SessionSubMenu extends Sprite {

	private static final int ORIGIN_SESSIONMENU_X = 215;
	private static final int ORIGIN_Y = 50;
	private static final int PADDING_Y = 10;
	private static final int ITEM_SESSION_DIM_Y = 102;
	private static final int ORIGIN_SELECTVEHICLEMENU_X = 280;
	private static final int ORIGIN_SELECTVEHICLEMENU_Y = 30;
	private static final int DIM_HEIGHT_SELECTVEHICLEITEM = 100;
	private static final int PADDING_SELECTVEHICLEITEM = 9;
	private static final int DIM_WIDTH_UNICYCLE_SESSION_ITEM = 70;
	private static final int DIM_HEIGHT_UNICYCLE_SESSION_ITEM = 70;
	private static final int DIM_WIDTH_OLD_MOTO_SESSION_ITEM = 105;
	private static final int DIM_HEIGHT_OLD_MOTO_SESSION_ITEM = 70;
	private static final int DIM_WIDTH_SCOOTER_SESSION_ITEM = 105;
	private static final int DIM_HEIGHT_SCOOTER_SESSION_ITEM = 70;
	private static final int DIM_WIDTH_HARDLEY_SESSION_ITEM = 105;
	private static final int DIM_HEIGHT_HARDLEY_SESSION_ITEM = 70;

	private MenuScene parentScene;

	private MainMenu parentMenu;
	private Sprite selectVehicleMenu;
	private VertexBufferObjectManager vbom;
	private BoundCamera camera;
	private Engine engine;
	private Activity activity;
	private Sprite backMenuItem;
	private List<Sprite> sessionItems = new ArrayList<>();
	private List<Sprite> deleteBtns = new ArrayList<>();
	private List<Sprite> vehicleBtns = new ArrayList<>();
	private List<Sprite> playBtns = new ArrayList<>();
	private List<Sprite> vehicleToSelectBtns= new ArrayList<>();
	private boolean isSelectVehicleMenuOnScreen = false;
	private int indexSessionToUpdateVehicule;
	
	

	
	public SessionSubMenu(float pX, float pY, ITextureRegion texture, VertexBufferObjectManager vbom, MenuScene parentScene,
			MainMenu parentMenu, BoundCamera camera, Engine engine,Activity activity) {

		super(0, 0, texture, vbom);
		
		this.vbom = vbom;
		this.parentScene = parentScene;
		this.parentMenu = parentMenu;
		this.camera = camera;
		this.engine = engine;
		this.activity = activity;

	}

	public void createMenu() {
		backMenuItem = new Sprite(0, 0, ResourceManager.getInstance().backBtnTexture, vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
				if (pSceneTouchEvent.isActionDown() && parentMenu.isSessionMenuOnScreen()) {
					setScale(1.2f);
					parentMenu.hideSessionMenuScene();
				}
				return true;
			};
		};
		backMenuItem.setPosition(600, 300);
		
		// backMenuItem.setCullingEnabled(true);

		populateSessionMenu();
		this.attachChild(backMenuItem);
		parentScene.registerTouchArea(backMenuItem);
		createSelectVehiculeMenu();

	}

	private void createSelectVehiculeMenu() {

		selectVehicleMenu = new Sprite(0, 0, ResourceManager.getInstance().selectVehiculeMenuBackground, vbom);
		//selectVehicleMenu.setIgnoreUpdate(true);

		Sprite backSelectVehicleBtn = new Sprite(400, 400, ResourceManager.getInstance().backBtnTexture, vbom) 
		{
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
				if (pSceneTouchEvent.isActionDown() && isSelectVehicleMenuOnScreen) {
					this.registerEntityModifier(new SequenceEntityModifier(new ScaleModifier(0.2f, 1f, 1.2f), new ScaleModifier(0.2f, 1f,
							0.8f)));
					hideSelectVehicleMenu();

				}
				return true;
			};
		};
		//backSelectVehicleBtn.setIgnoreUpdate(true);
		selectVehicleMenu.attachChild(backSelectVehicleBtn);
		this.parentScene.registerTouchArea(backSelectVehicleBtn);
		
		populateSelectVehicleMenu();
	}

	private void populateSelectVehicleMenu() {
		int indexItemToPlace = 0;
		for (Vehicle vehicle : Vehicle.values()) {
			if (!vehicle.equals(Vehicle.NONE)) {
				boolean isVehicleAvailable = UserState.getInstance().getAvailableVehicles().contains(vehicle);
				Sprite vehiculeItem = null;
				int originY = ORIGIN_SELECTVEHICLEMENU_Y + (PADDING_SELECTVEHICLEITEM + (DIM_HEIGHT_SELECTVEHICLEITEM * indexItemToPlace));
				if (isVehicleAvailable) {
					vehiculeItem = new Sprite(ORIGIN_SELECTVEHICLEMENU_X, originY, Vehicle.getVehicleSelectVehicleItem(vehicle), vbom);
//					{
//						@Override
//						public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
//							if (pSceneTouchEvent.isActionDown() && isSelectVehicleMenuOnScreen) {
//								this.registerEntityModifier(getScaleBtnEntityModifier());
//								String descriptionVehicleSelected = (String) this.getUserData();
//								Vehicle vehicleSelected = Vehicle.getByDescription(descriptionVehicleSelected);
//								UserState.getInstance().getSessions().get(indexSessionToUpdateVehicule).setVehicleUsed(vehicleSelected);
//								UserState.getInstance().saveToFile();
//								refreshSessionMenu();
//								hideSelectVehicleMenu();
//
//							}
//							return true;
//						};
//					};
					vehicleToSelectBtns.add(vehiculeItem);
					//this.parentScene.registerTouchArea(vehiculeItem);
				} else {
					vehiculeItem = new Sprite(ORIGIN_SELECTVEHICLEMENU_X, originY, Vehicle.getVehicleShopItem(vehicle), vbom);
					Sprite lockSprite = new Sprite(vehiculeItem.getWidth() - 30, vehiculeItem.getHeight() - 30,
							ResourceManager.getInstance().lockIcon, vbom);
					lockSprite.setCullingEnabled(true);
					vehiculeItem.attachChild(lockSprite);
				}

				vehiculeItem.setUserData(vehicle.getDescription());
			
				

				selectVehicleMenu.attachChild(vehiculeItem);
				indexItemToPlace++;
			}
		}

	}

	public void updateSelectVehiculeMenu() {
		for (int i = 0; i < selectVehicleMenu.getChildCount(); i++) {
//			activity.runOnUiThread(new Runnable() {
//				
//				@Override
//				public void run() {
//					selectVehicleMenu.detachChild(selectVehicleMenu.getChildByIndex(0));
//					
//				}
//			});
			selectVehicleMenu.getChildByIndex(i).setVisible(false);
			
		}
		vehicleToSelectBtns.clear();
		populateSelectVehicleMenu();
	}

	private void showSelectVehicleMenu() {
		selectVehicleMenu.registerEntityModifier(new MoveModifier(0.3f, camera.getWidth(), 0, 0, 0));
		if (!selectVehicleMenu.hasParent()) {
			this.parentScene.attachChild(selectVehicleMenu);
		}
		this.parentMenu.hideSessionMenuScene();
		this.parentMenu.hideMainMenuControllers();
		this.isSelectVehicleMenuOnScreen = true;
	}

	private void hideSelectVehicleMenu() {
		this.selectVehicleMenu.registerEntityModifier(new MoveModifier(0.3f, 0, camera.getWidth(), 0, 0));
		this.parentMenu.showSessionMenuScene();
		this.isSelectVehicleMenuOnScreen = false;
	}

	private void clearPlayButtons() {
		for (Sprite playBtn : vehicleBtns) {
			this.parentScene.unregisterTouchArea(playBtn);
		}
		vehicleBtns.clear();
	}

	private void clearDeleteButtons() {
		for (Sprite deleteBtn : deleteBtns) {
			this.parentScene.unregisterTouchArea(deleteBtn);
		}
		deleteBtns.clear();
	}

	private void clearVehicleButtons() {
		for (Sprite vehicleBtn : vehicleBtns) {
			this.parentScene.unregisterTouchArea(vehicleBtn);
		}
		vehicleBtns.clear();
	}

	private void clearSessionMenu() {
//		clearDeleteButtons();
//		clearPlayButtons();
//		clearVehicleButtons();
		
		for (final Sprite sessionItem : sessionItems) {
			sessionItem.dispose();
			sessionItem.setVisible(false);
			

		}
		
		
		
		sessionItems.clear();
		playBtns.clear();
		deleteBtns.clear();
		vehicleBtns.clear();
	}

	public void refreshSessionMenu() {
		clearSessionMenu();
		populateSessionMenu();
		
	}

	private void populateSessionMenu() {
		// if (UserState.getInstance().getSessions().isEmpty())
		// UserState.getInstance().initSessions();
		int indexItem = 0;
		for (GameSession session : UserState.getInstance().getSessions()) {
			Sprite sessionItem = null;
			if (session.isEmptySession()) {
				sessionItem = createEmptySessionMenuItem(session, indexItem);
			} else {
				sessionItem = createSessionMenuItem(session, indexItem);
			}

			this.attachChild(sessionItem);

			if (sessionItems == null) {
				sessionItems = new ArrayList<>();
			}

			sessionItems.add(sessionItem);
			indexItem++;
		}

	}

	private Sprite createSessionMenuItem(GameSession session, final int indexItem) {
		Sprite sessionItem = new Sprite(ORIGIN_SESSIONMENU_X, ORIGIN_Y + PADDING_Y + (ITEM_SESSION_DIM_Y * indexItem),
				ResourceManager.getInstance().sessionMenuItem, vbom);
		// sessionItem.setIgnoreUpdate(true);

		// create level icon
		String levelType = LevelType.getLevelType(session.getCurrentLevel()).getTypeLevel();
		Sprite levelImage = new Sprite(20, 20, ImageProvider.getLevelIcon(levelType), vbom);
		// levelImage.setIgnoreUpdate(true);
		sessionItem.attachChild(levelImage);

		// create delete btn
		Sprite deleteBtn = new Sprite(sessionItem.getWidth() - 50, sessionItem.getHeight() - 50,
				ResourceManager.getInstance().deleteSmallBtnTexture, vbom);

		deleteBtn.setUserData(indexItem);
		sessionItem.attachChild(deleteBtn);
		deleteBtns.add(deleteBtn);

		Sprite vehicleIcon = createVehiculeIcon(session.getVehicleUsed());

		vehicleIcon.setUserData(indexItem);
		vehicleIcon.setIgnoreUpdate(true);
		sessionItem.attachChild(vehicleIcon);
		vehicleBtns.add(vehicleIcon);

		// create play btn
		Sprite playBtn = new Sprite(sessionItem.getWidth() - 50, 0, 50, 50, ResourceManager.getInstance().playMenuButton, vbom); 

		playBtn.setUserData(indexItem);
		sessionItem.attachChild(playBtn);

		playBtns.add(playBtn);

		Text levelText = new Text(95, 20, ResourceManager.getInstance().font, "LEVEL " + session.getCurrentLevel(), new TextOptions(
				HorizontalAlign.CENTER), vbom);

		sessionItem.attachChild(levelText);
		Sprite moneyBagIcon = new Sprite(95, 50, 30, 30, ResourceManager.getInstance().buyBtn, vbom);
		// moneyBagIcon.setIgnoreUpdate(true);
		Text moneyText = new Text(moneyBagIcon.getX() + moneyBagIcon.getWidth() + 10, 50, ResourceManager.getInstance().font, ""
				+ session.getCurrentMoney() + "$", new TextOptions(HorizontalAlign.CENTER), vbom);
		sessionItem.attachChild(moneyBagIcon);
		sessionItem.attachChild(moneyText);

		

		return sessionItem;

	}

	private Sprite createEmptySessionMenuItem(GameSession session, final int indexItem) {
		Sprite sessionItem = new Sprite(0, 0, ResourceManager.getInstance().sessionMenuItem, vbom);
		sessionItem.setCullingEnabled(true);
		sessionItem.setUserData(indexItem);
		sessionItem.setPosition(ORIGIN_SESSIONMENU_X, ORIGIN_Y + PADDING_Y + (ITEM_SESSION_DIM_Y * indexItem));

		Sprite playBtn = new Sprite(sessionItem.getWidth() - 50, sessionItem.getHeight() / 2 - 25, 50, 50,
				ResourceManager.getInstance().playMenuButton, vbom) ;
//		{
//			@Override
//			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
//				if (pSceneTouchEvent.isActionDown() && parentMenu.isSessionMenuOnScreen()) {
//					int indexItemSelected = (int) this.getUserData();
//					this.registerEntityModifier(getPlayBtnEntityModifier(indexItemSelected));
//
//				}
//				return true;
//			};
//
//		};
		playBtn.setCullingEnabled(true);
		playBtn.setUserData(indexItem);
		sessionItem.attachChild(playBtn);
		playBtns.add(playBtn);

		Text newSessionText = new Text(90, 30, ResourceManager.getInstance().font, "NEW GAME", new TextOptions(HorizontalAlign.CENTER),
				vbom);
		sessionItem.attachChild(newSessionText);
		
		return sessionItem;
	}

	private Sprite createVehiculeIcon(Vehicle vehicleSelected) {
		Sprite vehiculeSprite = null;
		if (vehicleSelected.equals(Vehicle.UNICYCLE)) {
			vehiculeSprite = new Sprite(200, 10, DIM_WIDTH_UNICYCLE_SESSION_ITEM, DIM_HEIGHT_UNICYCLE_SESSION_ITEM,
					ResourceManager.getInstance().unicycleSessionMenuItem, vbom);

		} else if (vehicleSelected.equals(Vehicle.BICYCLE)) {
			vehiculeSprite = new Sprite(200, 10, DIM_WIDTH_OLD_MOTO_SESSION_ITEM, DIM_HEIGHT_OLD_MOTO_SESSION_ITEM,
					ResourceManager.getInstance().bicycleSessionMenuItem, vbom) ;

		} else if (vehicleSelected.equals(Vehicle.SCOOTER)) {
			vehiculeSprite = new Sprite(200, 10, DIM_WIDTH_SCOOTER_SESSION_ITEM, DIM_HEIGHT_SCOOTER_SESSION_ITEM,
					ResourceManager.getInstance().scooterSessionMenuItem, vbom); 

		} else if (vehicleSelected.equals(Vehicle.HARLEY)) {
			vehiculeSprite = new Sprite(200, 10, DIM_WIDTH_HARDLEY_SESSION_ITEM, DIM_HEIGHT_HARDLEY_SESSION_ITEM,
					ResourceManager.getInstance().harleySessionMenuItem, vbom);

		} else {
			vehiculeSprite = new Sprite(200, 10, 80, 80, ResourceManager.getInstance().vehicleNoImage, vbom) ;

		}

		return vehiculeSprite;

	}

	public boolean isSelectVehicleMenuOnScreen() {
		return isSelectVehicleMenuOnScreen;
	}

	private SequenceEntityModifier getScaleBtnEntityModifier() {

		return new SequenceEntityModifier(new ScaleModifier(0.1f, 1f, 1.2f), new ScaleModifier(0.1f, 1.2f, 1f));
	}

	private SequenceEntityModifier getDeleteBtnEntityModifier(final int indexSelected) {

		return new SequenceEntityModifier(new ScaleModifier(0.1f, 1f, 1.2f), new ScaleModifier(0.1f, 1.2f, 1f)) {

			@Override
			protected void onModifierStarted(IEntity pItem) {
				super.onModifierStarted(pItem);
				// Your action after starting modifier

			}

			@Override
			protected void onModifierFinished(IEntity pItem) {
				super.onModifierFinished(pItem);
				UserState.getInstance().getSessions().get(indexSelected).flush();
				// updateSessionItem(indexSelected);
				refreshSessionMenu();

			}

		};
	}

	private SequenceEntityModifier getPlayBtnEntityModifier(final int indexSelected) {

		return new SequenceEntityModifier(new ScaleModifier(0.1f, 1f, 1.2f), new ScaleModifier(0.1f, 1.2f, 1f)) {

			@Override
			protected void onModifierStarted(IEntity pItem) {
				super.onModifierStarted(pItem);
				// Your action after starting modifier

			}

			@Override
			protected void onModifierFinished(IEntity pItem) {
				super.onModifierFinished(pItem);

				//parentMenu.hideSessionMenuScene();
				UserState.getInstance().setSelectedSession(indexSelected);
				GameSession selectedSession = UserState.getInstance().getSelectedSession();
				boolean isNewGame = selectedSession.isEmptySession();
				if (isNewGame) {
					SceneManager.getInstance().createStory(engine);
				} else {
					SceneManager.getInstance().createGameScene(engine);
				}

			}

		};
	}
	
	public Sprite wasDeleteButtonTouched(float x,float y){
		for(Sprite deleteBtn:deleteBtns){
			if(Util.isPointWithinSprite(x, y, deleteBtn)){
				return deleteBtn;
			}
		}
	
		return null;
	}
	
	public Sprite wasPlayButtonTouched(float x,float y){
		for(Sprite playBtn:playBtns){
			if(Util.isPointWithinSprite(x, y, playBtn)){
				return playBtn;
			}
		}
	
		return null;
	}
	
	public Sprite wasVehiculeButtonTouched(float x,float y){
		for(Sprite vehiculeBtn:vehicleBtns){
			if(Util.isPointWithinSprite(x, y, vehiculeBtn)){
				return vehiculeBtn;
			}
		}
	
		return null;
	}
	
	public Sprite wasVehiculeToSelectButtonTouched(float x,float y){
		for(Sprite vehiculeToSelectBtn:vehicleToSelectBtns){
			
			if(Util.isPointWithinSprite(x, y, vehiculeToSelectBtn)){
				return vehiculeToSelectBtn;
			}
		}
	
		return null;
	}
	public void displaySelectVehicleMenu(Sprite touchedSprite){
		showSelectVehicleMenu();
		indexSessionToUpdateVehicule = Integer.parseInt(touchedSprite.getUserData().toString());
		
	}

	
	public void playGame(Sprite touchedSprite){
		int indexItemSelected = Integer.parseInt(touchedSprite.getUserData().toString());
		
		touchedSprite.registerEntityModifier(getPlayBtnEntityModifier(indexItemSelected));
	}
	
	public void deleteGameSession(Sprite touchedSprite){
		int indexItemSelected = Integer.parseInt(touchedSprite.getUserData().toString());
		touchedSprite.registerEntityModifier(getDeleteBtnEntityModifier(indexItemSelected));
	}
	
	public void selectVehicle(Sprite touchedSprite){
		touchedSprite.registerEntityModifier(getScaleBtnEntityModifier());
		String descriptionVehicleSelected = (String) touchedSprite.getUserData();
		Vehicle vehicleSelected = Vehicle.getByDescription(descriptionVehicleSelected);
		UserState.getInstance().getSessions().get(indexSessionToUpdateVehicule).setVehicleUsed(vehicleSelected);
		UserState.getInstance().saveToFile();
		refreshSessionMenu();
		hideSelectVehicleMenu();
		
	}
	
	

}
