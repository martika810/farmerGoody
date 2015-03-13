package com.martaocio.farmergoody;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.modifier.MoveModifier;
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
	private static final int ORIGIN_Y = 70;
	private static final int PADDING_X = 10;
	private static final int PADDING_Y = 10;
	private static final int ITEM_DIM_X = 190;
	private static final int ITEM_DIM_Y = 190;
	private static final int NUMBER_COLUMNS = 2;
	private MenuScene menuChildScene;// menu that can have buttons
	private MenuScene shopChildScene;
	Sprite bg;
	private final int PLAY = 0;// to recognize when the play buttons is gonna be
								// played
	private final int CONTINUE = 1;
	private final int BACK = 2;

	@Override
	public void createScene() {

		createMenuScene();
		createShopScene();
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
		// this.shopChildScene=new MenuScene(camera);
		// this.shopChildScene.setPosition(0, 0);

		bg = new Sprite(0, 0, resourceManager.shopMenuBackGround, vbom);
		Sprite backMenuItem = new Sprite(0, 0, resourceManager.leftArrowTexture, vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
				if (pSceneTouchEvent.isActionDown()) {
					hideShopMenuScene();
				}
				return true;
			};
		};
		backMenuItem.setPosition(600, 50);

		bg.attachChild(backMenuItem);
		int indexOfItemToPlace = 0;

		for (Vehicle vehicle : Vehicle.values()) {
			if (!vehicle.equals(Vehicle.NONE)) {
				boolean alreadyHad = UserState.getInstance().getAvailableVehicles().contains(vehicle);
				GameSession lastModifiedSession = UserState.getInstance().getLastModifiedSession();

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

		this.menuChildScene.registerTouchArea(backMenuItem);

	}

	private Sprite createShopMenuItem(final Vehicle vehicle, boolean alreadyHad, boolean canBuy) {
		Sprite itemBackground = new Sprite(0, 0, resourceManager.shopItemMenuBackground, vbom);
		Sprite vehicleImage = new Sprite(0, 0, resourceManager.unycleImage, vbom);
		Text priceText = new Text(70, 140, resourceManager.font, new Integer(vehicle.getPrice()).toString() + "$", new TextOptions(
				HorizontalAlign.CENTER), vbom);
		if (canBuy && !alreadyHad) {
			Sprite buyBtn = new Sprite(0, 0, resourceManager.buyBtn, vbom) {
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
					if (pSceneTouchEvent.isActionDown()) {
						UserState.getInstance().getAvailableVehicles().add(vehicle);
					}
					return true;
				};
			};
			buyBtn.setPosition(130, -10);
			itemBackground.attachChild(buyBtn);
		}
		vehicleImage.setPosition(50, 30);
		itemBackground.attachChild(vehicleImage);
		itemBackground.attachChild(priceText);

		return itemBackground;
	}

	private void showShopMenuScene() {
		// this.setChildScene(shopChildScene, false, true,true);
		bg.registerEntityModifier(new MoveModifier(0.3f, camera.getWidth(), 0, 0, 0));
		if (!bg.hasParent()) {
			this.menuChildScene.attachChild(bg);
		}

	}

	private void hideShopMenuScene() {
		bg.registerEntityModifier(new MoveModifier(0.3f, 0, camera.getWidth(), 0, 0));
	}

	private void createMenuScene() {

		this.menuChildScene = new MenuScene(camera);
		this.menuChildScene.setPosition(0, 0);

		// create the menu buttons
		// when the button is clicked , it is scaled it to 1.2
		final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(this.PLAY, resourceManager.playMenuButton, vbom),
				1.7f, 1.5f);

		final IMenuItem continueMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(this.CONTINUE, resourceManager.continueMenuButton,
				vbom), 1.2f, 1);

		this.menuChildScene.addMenuItem(playMenuItem);
		this.menuChildScene.addMenuItem(continueMenuItem);

		this.menuChildScene.buildAnimations();
		this.menuChildScene.setBackgroundEnabled(false);

		playMenuItem.setPosition(360, 260);
		continueMenuItem.setPosition(650, 370);
		this.menuChildScene.setOnMenuItemClickListener(this);
		this.menuChildScene.setOnSceneTouchListener(this);

		// attach the play menu to the scene
		this.setChildScene(menuChildScene, false, true, true);
	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY) {
		switch (pMenuItem.getID()) {

		case PLAY:
			// load game
			SceneManager.getInstance().createGameScene();
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

	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		return false;
	}

}
