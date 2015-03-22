package com.martaocio.farmergoody;

import java.util.ArrayList;
import java.util.List;

import org.andengine.engine.camera.BoundCamera;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;


public class SessionSubMenu extends Sprite {

	private static final int ORIGIN_SESSIONMENU_X = 215;
	private static final int ORIGIN_Y = 50;
	private static final int PADDING_Y = 10;
	private static final int ITEM_SESSION_DIM_Y = 102;
	private static final int ORIGIN_SELECTVEHICLEMENU_X = 280;
	private static final int ORIGIN_SELECTVEHICLEMENU_Y = 10;
	private static final int DIM_HEIGHT_SELECTVEHICLEITEM = 100;
	private static final int PADDING_SELECTVEHICLEITEM = 9;

	private MenuScene parentScene;
	
	private MainMenu parentMenu;
	private Sprite selectVehicleMenu;
	private VertexBufferObjectManager vbom;
	private BoundCamera camera;
	private Sprite backMenuItem;
	private List<Sprite> sessionItems = new ArrayList<>();
	private boolean isSelectVehicleMenuOnScreen=false;
	private int indexSessionToUpdateVehicule;

	public SessionSubMenu(float pX, float pY, ITextureRegion texture, VertexBufferObjectManager vbom, MenuScene parentScene,
			MainMenu parentMenu, BoundCamera camera) {

		super(0, 0, texture, vbom);
		this.vbom = vbom;
		this.parentScene = parentScene;
		this.parentMenu = parentMenu;
		this.camera = camera;

	}

	public void createMenu() {
		backMenuItem = new Sprite(0, 0, ResourceManager.getInstance().leftArrowTexture, vbom) {
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
		backMenuItem.setCullingEnabled(true);

		populateSessionMenu();
		this.attachChild(backMenuItem);
		parentScene.registerTouchArea(backMenuItem);
		createSelectVehiculeMenu();

	}

	private void createSelectVehiculeMenu() {

		selectVehicleMenu = new Sprite(0, 0, ResourceManager.getInstance().selectVehiculeMenuBackground, vbom);
		selectVehicleMenu.setCullingEnabled(true);
		populateSelectVehicleMenu();
	}

	private void populateSelectVehicleMenu() {
		int indexItemToPlace = 0;
		for (Vehicle vehicle : Vehicle.values()) {
			if (!vehicle.equals(Vehicle.NONE)) {
				boolean isVehicleAvailable = UserState.getInstance().getAvailableVehicles().contains(vehicle);
				Sprite vehiculeItem = null;
				if (isVehicleAvailable) {
					vehiculeItem = new Sprite(0, 0, Vehicle.getVehicleShopItem(vehicle), vbom) {
						@Override
						public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
							if (pSceneTouchEvent.isActionDown() && isSelectVehicleMenuOnScreen) {
								UserState.getInstance().getSessions().get(indexSessionToUpdateVehicule).setVehicleUsed(Vehicle.BICYCLE);
								updateSessionItem(indexSessionToUpdateVehicule);
								hideSelectVehicleMenu();
								
							}
							return true;
						};
					};
					this.parentScene.registerTouchArea(vehiculeItem);
				} else {
					vehiculeItem = new Sprite(0, 0, Vehicle.getVehicleShopItem(vehicle), vbom);
					Sprite lockSprite = new Sprite(vehiculeItem.getWidth() - 30, vehiculeItem.getHeight() - 30,
							ResourceManager.getInstance().lockIcon, vbom);
					lockSprite.setCullingEnabled(true);
					vehiculeItem.attachChild(lockSprite);
				}
				vehiculeItem.setPosition(ORIGIN_SELECTVEHICLEMENU_X, ORIGIN_SELECTVEHICLEMENU_Y
						+ (PADDING_SELECTVEHICLEITEM + (DIM_HEIGHT_SELECTVEHICLEITEM * indexItemToPlace)));
				vehiculeItem.setCullingEnabled(true);
				
				selectVehicleMenu.attachChild(vehiculeItem);
				indexItemToPlace++;
			}
		}

	}

	private void showSelectVehicleMenu() {
		selectVehicleMenu.registerEntityModifier(new MoveModifier(0.3f, camera.getWidth(), 0, 0, 0));
		if (!selectVehicleMenu.hasParent()) {
			this.parentScene.attachChild(selectVehicleMenu);
		}
		this.parentMenu.hideSessionMenuScene();
		this.parentMenu.hideMainMenuControllers();
		isSelectVehicleMenuOnScreen=true;
	}

	private void hideSelectVehicleMenu() {
		selectVehicleMenu.registerEntityModifier(new MoveModifier(0.3f, 0, camera.getWidth(), 0, 0));
		this.parentMenu.showSessionMenuScene();
		isSelectVehicleMenuOnScreen=false;
	}

	private void populateSessionMenu() {
		if (UserState.getInstance().getSessions().isEmpty())
			UserState.getInstance().initSessions();
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

	private Sprite createSessionMenuItem(GameSession session, int indexItem) {
		Sprite sessionItem = new Sprite(0, 0, ResourceManager.getInstance().sessionMenuItem, vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
				if (pSceneTouchEvent.isActionDown() && parentMenu.isSessionMenuOnScreen()) {
					this.registerEntityModifier(new ScaleModifier(1f, 1f, 1.2f));
					parentMenu.hideSessionMenuScene();
					int indexItemSelected = (int) this.getUserData();
					UserState.getInstance().setSelectedSession(indexItemSelected);
					SceneManager.getInstance().createGameScene();
				}
				return true;
			};
		};
		sessionItem.setCullingEnabled(true);
		sessionItem.setUserData(indexItem);
		sessionItem.setPosition(ORIGIN_SESSIONMENU_X, ORIGIN_Y + PADDING_Y + (ITEM_SESSION_DIM_Y * indexItem));
		Sprite levelImage = new Sprite(20, 10, ResourceManager.getInstance().levelIcon, vbom);
		sessionItem.attachChild(levelImage);

		Text levelText = new Text(120, 30, ResourceManager.getInstance().font, "LEVEL " + session.getCurrentLevel(), new TextOptions(
				HorizontalAlign.CENTER), vbom);
		sessionItem.attachChild(levelText);

		Text moneyText = new Text(120, 60, ResourceManager.getInstance().font, "" + session.getCurrentMoney(), new TextOptions(
				HorizontalAlign.CENTER), vbom);
		sessionItem.attachChild(moneyText);

		Sprite vehicleIcon = createVehiculeIcon(session.getVehicleUsed());
		vehicleIcon.setPosition(200, 10);
		vehicleIcon.setUserData(indexItem);
		sessionItem.attachChild(vehicleIcon);

		this.parentScene.registerTouchArea(vehicleIcon);
		this.parentScene.registerTouchArea(sessionItem);

		return sessionItem;

	}

	private Sprite createEmptySessionMenuItem(GameSession session, int indexItem) {
		Sprite sessionItem = new Sprite(0, 0, ResourceManager.getInstance().sessionMenuItem, vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
				if (pSceneTouchEvent.isActionDown() && parentMenu.isSessionMenuOnScreen()) {
					this.registerEntityModifier(new ScaleModifier(1f, 1f, 1.2f));
					parentMenu.hideSessionMenuScene();
					int indexItemSelected = (int) this.getUserData();
					UserState.getInstance().setSelectedSession(indexItemSelected);
					SceneManager.getInstance().createGameScene();
				}
				return true;
			};
		};
		sessionItem.setCullingEnabled(true);
		sessionItem.setUserData(indexItem);
		sessionItem.setPosition(ORIGIN_SESSIONMENU_X, ORIGIN_Y + PADDING_Y + (ITEM_SESSION_DIM_Y * indexItem));

		Text newSessionText = new Text(90, 30, ResourceManager.getInstance().font, "NEW GAME", new TextOptions(HorizontalAlign.CENTER),
				vbom);
		sessionItem.attachChild(newSessionText);
		this.parentScene.registerTouchArea(sessionItem);
		return sessionItem;
	}

	private Sprite createVehiculeIcon(Vehicle vehicleSelected) {
		Sprite vehiculeSprite = null;
		if (vehicleSelected.equals(Vehicle.UNICYCLE)) {
			vehiculeSprite = new Sprite(0, 0, ResourceManager.getInstance().unicycleSessionMenuItem, vbom) {
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
					if (pSceneTouchEvent.isActionDown() && parentMenu.isSessionMenuOnScreen()) {
						showSelectVehicleMenu();
						indexSessionToUpdateVehicule=(int)this.getUserData();
					}
					return true;
				};

			};
		} else if (vehicleSelected.equals(Vehicle.BICYCLE)) {
			vehiculeSprite = new Sprite(0, 0, ResourceManager.getInstance().bicycleSessionMenuItem, vbom) {
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
					if (pSceneTouchEvent.isActionDown() && parentMenu.isSessionMenuOnScreen()) {
						showSelectVehicleMenu();
						indexSessionToUpdateVehicule=(int)this.getUserData();
					}
					return true;
				};
			};
		} else if (vehicleSelected.equals(Vehicle.SCOOTER)) {
			vehiculeSprite = new Sprite(0, 0, ResourceManager.getInstance().scooterSessionMenuItem, vbom) {
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
					if (pSceneTouchEvent.isActionDown() && parentMenu.isSessionMenuOnScreen()) {
						showSelectVehicleMenu();
						indexSessionToUpdateVehicule=(int)this.getUserData();
					}
					return true;
				};
			};
		} else if (vehicleSelected.equals(Vehicle.HARLEY)) {
			vehiculeSprite = new Sprite(0, 0, ResourceManager.getInstance().harleySessionMenuItem, vbom) {
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
					if (pSceneTouchEvent.isActionDown() && parentMenu.isSessionMenuOnScreen()) {
						showSelectVehicleMenu();
						indexSessionToUpdateVehicule=(int)this.getUserData();
					}
					return true;
				};
			};
		} else {
			vehiculeSprite = new Sprite(0, 0, ResourceManager.getInstance().vehicleNoImage, vbom) {
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
					if (pSceneTouchEvent.isActionDown() && parentMenu.isSessionMenuOnScreen()) {
						showSelectVehicleMenu();
						indexSessionToUpdateVehicule=(int)this.getUserData();
					}
					return true;
				};
			};
		}

		vehiculeSprite.setCullingEnabled(true);
		return vehiculeSprite;

	}

	private void updateSessionItem(int indexMenuItemToUpdate) {
		GameSession dataSession=UserState.getInstance().getSessions().get(indexMenuItemToUpdate);
		Sprite sessionMenuItemToUpdate = sessionItems.get(indexMenuItemToUpdate);
		sessionMenuItemToUpdate.detachSelf();
		sessionMenuItemToUpdate.dispose();
		sessionItems.remove(indexMenuItemToUpdate);
		Sprite newSessionMenuItem = createSessionMenuItem(dataSession, indexMenuItemToUpdate);
		newSessionMenuItem.setPosition(ORIGIN_SESSIONMENU_X, ORIGIN_Y + PADDING_Y + (ITEM_SESSION_DIM_Y * indexMenuItemToUpdate));
		this.attachChild(newSessionMenuItem);
		sessionItems.add(newSessionMenuItem);
	}

	public boolean isSelectVehicleMenuOnScreen() {
		return isSelectVehicleMenuOnScreen;
	}

}
