package com.martaocio.farmergoody;

import java.util.ArrayList;
import java.util.List;


import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;

public class ShopSubMenu extends Sprite {

	private static final int ORIGIN_X = 200;
	private static final int ORIGIN_Y = 90;
	private static final int PADDING_X = 10;
	private static final int PADDING_Y = 5;
	private static final int ITEM_DIM_X = 190;
	private static final int ITEM_DIM_Y = 190;
	private static final int NUMBER_COLUMNS = 2;

	private MenuScene parentScene;
	private MainMenu parentMenu;
	private VertexBufferObjectManager vbom;
	private Sprite backMenuItem;
	private List<AnimatedSprite> shopItems = new ArrayList<>();
	private Text creditText;
	private boolean hasBoughtSomething;

	public ShopSubMenu(float pX, float pY, ITextureRegion texture, VertexBufferObjectManager vbom, MenuScene parentScene, MainMenu parentMenu) {

		super(0, 0, texture, vbom);
		this.vbom = vbom;
		this.parentScene = parentScene;
		this.parentMenu = parentMenu;

	}

	public void createMenu() {

		backMenuItem = new Sprite(0, 0, ResourceManager.getInstance().backBtnTexture, vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
				if (pSceneTouchEvent.isActionDown() && parentMenu.isShopMenuOnScreen()) {
					parentMenu.hideShopMenuScene();
				}
				return true;
			};
		};
		backMenuItem.setPosition(600, 50);
		backMenuItem.setCullingEnabled(true);
		this.attachChild(backMenuItem);
		this.parentScene.registerTouchArea(backMenuItem);

		creditText = new Text(230, 80, ResourceManager.getInstance().font, "Credit: "
				+ (UserState.getInstance().getLastModifiedSession() == null ? 0 : UserState.getInstance().getLastModifiedSession()
						.getCurrentMoney()) + "$", new TextOptions(HorizontalAlign.CENTER), vbom);

		populateShopMenu();
		

		this.attachChild(creditText);

	}

	private void populateShopMenu() {

		int indexOfItemToPlace = 0;

		for (Vehicle vehicle : Vehicle.values()) {
			if (!vehicle.equals(Vehicle.NONE)) {
				boolean alreadyHad = UserState.getInstance().getAvailableVehicles().contains(vehicle);

				boolean haveEnoughtMoneyToBuy = (UserState.getInstance().getLastModifiedSession() == null ? false : UserState.getInstance()
						.getLastModifiedSession().getCurrentMoney() > vehicle.getPrice());
				boolean canBuy = !alreadyHad && haveEnoughtMoneyToBuy;

				AnimatedSprite item = createShopMenuItem(vehicle, alreadyHad, canBuy,indexOfItemToPlace);
				item.setCullingEnabled(true);
				shopItems.add(item);

				int colToPlaceItem = indexOfItemToPlace / NUMBER_COLUMNS;
				int rowToPlaceItem = indexOfItemToPlace % NUMBER_COLUMNS;
				int positionXToPlaceItem = ORIGIN_X + PADDING_X + (ITEM_DIM_X * rowToPlaceItem);
				int positionYToPlaceItem = ORIGIN_Y + PADDING_Y + (ITEM_DIM_Y * colToPlaceItem);

				item.setPosition(positionXToPlaceItem, positionYToPlaceItem);
				this.attachChild(item);
				indexOfItemToPlace++;

			}
		}
	}

	private AnimatedSprite createShopMenuItem(final Vehicle vehicle, boolean alreadyHad, boolean canBuy,final int indexItem) {
		AnimatedSprite itemBackground = new AnimatedSprite(0, 0, Vehicle.getVehicleShopItem(vehicle), vbom);
		itemBackground.setCullingEnabled(true);
		if(alreadyHad){
			itemBackground.setCurrentTileIndex(1);
		}else{
			itemBackground.setCurrentTileIndex(0);
		}
		
		if (canBuy && !alreadyHad) {
			Sprite buyBtn = new Sprite(0, 0, ResourceManager.getInstance().buyBtn, vbom) {
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
					if (pSceneTouchEvent.isActionDown() && parentMenu.isShopMenuOnScreen()) {
						// setScale(1.2f);
						buyVehiculeAction(vehicle);
						int itemTouched=(int)this.getUserData();
						updateShopItem(itemTouched);
						this.registerEntityModifier(getBuyBtnEntityModifier());
						hasBoughtSomething=true;

					}
					return true;
				};
			};
			buyBtn.setUserData(indexItem);
			buyBtn.setCullingEnabled(true);

			buyBtn.setPosition(itemBackground.getWidth()-30, itemBackground.getHeight()-30);
			this.parentScene.registerTouchArea(buyBtn);
		
			Text priceText = new Text(20, 25, ResourceManager.getInstance().font, new Integer(vehicle.getPrice()).toString() + "$",
					new TextOptions(HorizontalAlign.CENTER), vbom);
			buyBtn.attachChild(priceText);
			itemBackground.attachChild(buyBtn);
		}

		

		

		return itemBackground;
	}

//	private static ITiledTextureRegion getVehicleShopItem(Vehicle vehicle) {
//
//		if (vehicle.equals(Vehicle.UNICYCLE)) {
//			return ResourceManager.getInstance().unicycleShopItem;
//		} else if (vehicle.equals(Vehicle.BICYCLE)) {
//			return ResourceManager.getInstance().bicycleShopItem;
//		} else if (vehicle.equals(Vehicle.SCOOTER)) {
//			return ResourceManager.getInstance().scooterShopItem;
//		} else if (vehicle.equals(Vehicle.HARLEY)) {
//			return ResourceManager.getInstance().hardleyShopItem;
//		}
//		return ResourceManager.getInstance().unicycleShopItem;
//	}

	private SequenceEntityModifier getBuyBtnEntityModifier() {

		return new SequenceEntityModifier(new ScaleModifier(0.5f, 1f, 1.2f), new DelayModifier(0.2f), new ScaleModifier(0.2f, 1f, 0f)) {
			@Override
			protected void onModifierStarted(IEntity pItem) {
				super.onModifierStarted(pItem);
				// Your action after starting modifier

			}

			@Override
			protected void onModifierFinished(IEntity pItem) {
				super.onModifierFinished(pItem);
				pItem.setVisible(false);
			}
		};
	}
	
	public void updateShopItem(int indexItem){
		shopItems.get(indexItem).setCurrentTileIndex(1);
	}

	public void buyVehiculeAction(Vehicle vehicle) {
		int newCurrentCreadit = UserState.getInstance().getSelectedSession().getCurrentMoney() - vehicle.getPrice();
		UserState.getInstance().getSelectedSession().setCurrentMoney(newCurrentCreadit);
		UserState.getInstance().getAvailableVehicles().add(vehicle);
		UserState.getInstance().saveToFile();
		this.creditText.setText("Credit: " + newCurrentCreadit);
		// updateSessionItem(UserState.getInstance().getSelectedSession(), 0);
		

	}

	public boolean isHasBoughtSomething() {
		return hasBoughtSomething;
	}

	public void setHasBoughtSomething(boolean hasBoughtSomething) {
		this.hasBoughtSomething = hasBoughtSomething;
	}

}
