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

import com.martaocio.farmergoody.scenes.MainMenu;

public class ShopSubMenu extends Sprite {

	private static final int ORIGIN_X = 240;
	private static final int ORIGIN_Y = 110;
	private static final int PADDING_X = 10;
	private static final int PADDING_Y = 5;
	private static final int ITEM_DIM_X = 180;
	private static final int ITEM_DIM_Y = 180;
	private static final int NUMBER_COLUMNS = 2;

	private MenuScene parentScene;
	private MainMenu parentMenu;
	private VertexBufferObjectManager vbom;
	private Sprite backMenuItem;
	private List<Sprite> shopItems = new ArrayList<>();
	private Text creditText;
	private boolean hasBoughtSomething;
	private List<Sprite> buyButtons = new ArrayList<>();

	public ShopSubMenu(float pX, float pY, ITextureRegion texture, VertexBufferObjectManager vbom, MenuScene parentScene, MainMenu parentMenu) {

		super(0, 0, texture, vbom);
		this.vbom = vbom;
		this.parentScene = parentScene;
		this.parentMenu = parentMenu;

	}

	public void createMenu() {

		backMenuItem = new Sprite(600, 50, ResourceManager.getInstance().backBtnTexture, vbom) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
				if (pSceneTouchEvent.isActionDown() && parentMenu.isShopMenuOnScreen()) {
					parentMenu.hideShopMenuScene();
				}
				return true;
			};
		};
		
		backMenuItem.setIgnoreUpdate(true);
		this.attachChild(backMenuItem);
		this.parentScene.registerTouchArea(backMenuItem);

		creditText = new Text(230, 90, ResourceManager.getInstance().font, "Credit: "
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
				
				

				Sprite item = createShopMenuItem(vehicle, alreadyHad, canBuy,indexOfItemToPlace);
				
				shopItems.add(item);

				

			//	item.setPosition(positionXToPlaceItem, positionYToPlaceItem);
				this.attachChild(item);
				indexOfItemToPlace++;

			}
		}
	}

	private Sprite createShopMenuItem(final Vehicle vehicle, boolean alreadyHad, boolean canBuy,final int indexItem) {
		//AnimatedSprite itemBackground = new AnimatedSprite(0, 0, Vehicle.getVehicleShopItem(vehicle), vbom);
		//ShopVehiculeItem shopItem=new ShopVehiculeItem(0, 0, vbom, vehicle);
		
		int colToPlaceItem = indexItem/ NUMBER_COLUMNS;
		int rowToPlaceItem = indexItem % NUMBER_COLUMNS;
		int positionXToPlaceItem = ORIGIN_X + PADDING_X + (ITEM_DIM_X * rowToPlaceItem);
		int positionYToPlaceItem = ORIGIN_Y + PADDING_Y + (ITEM_DIM_Y * colToPlaceItem);
		Sprite shopItem=new Sprite(positionXToPlaceItem,positionYToPlaceItem,Vehicle.getVehicleShopItem(vehicle),vbom);
		
		shopItem.setCullingEnabled(true);
		
		Sprite priceTag=new Sprite(-64,shopItem.getHeight()-25,ResourceManager.getInstance().priceTagIcon,vbom);
		Text priceText = new Text(40, 10, ResourceManager.getInstance().font, new Integer(vehicle.getPrice()).toString() + "$",
				new TextOptions(HorizontalAlign.CENTER), vbom);
		priceTag.attachChild(priceText);
		shopItem.attachChild(priceTag);

		if(!canBuy && !alreadyHad){
			Sprite lockBtn = new Sprite(shopItem.getWidth()-30, shopItem.getHeight()-30, ResourceManager.getInstance().lockIcon, vbom);
			shopItem.attachChild(lockBtn);
		}
		
		if (canBuy && !alreadyHad) {
			Sprite buyBtn = new Sprite(shopItem.getWidth()-30, shopItem.getHeight()-30, ResourceManager.getInstance().buyBtn, vbom);
//			{
//				@Override
//				public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
//					if (pSceneTouchEvent.isActionDown() && parentMenu.isShopMenuOnScreen()) {
//						
//						buyVehiculeAction(vehicle);
//						this.registerEntityModifier(getBuyBtnEntityModifier());
//						hasBoughtSomething=true;
//
//					}
//					return true;
//				};
//			};
			buyBtn.setUserData(vehicle);
			//buyBtn.setCullingEnabled(true);
			buyButtons.add(buyBtn);

			//buyBtn.setPosition(shopItem.getWidth()-30, shopItem.getHeight()-30);
			//this.parentScene.registerTouchArea(buyBtn);
		
			
			
			
			shopItem.attachChild(buyBtn);
		}

		

		

		return shopItem;
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
	
//	public void updateShopItem(int indexItem){
//		shopItems.get(indexItem).setCurrentTileIndex(1);
//	}

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
	
	public Sprite wasBuyButtonTouched(float x,float y){
		for(Sprite buyBtn:buyButtons){
			if(Util.isPointWithinSprite(x, y, buyBtn)){
				return buyBtn;
			}
		}
	
		return null;
	}
	
	public void buyVehicle(Sprite touchedSprite){
		Vehicle vehicleSelected= (Vehicle)touchedSprite.getUserData();
		buyVehiculeAction(vehicleSelected);
		touchedSprite.registerEntityModifier(getBuyBtnEntityModifier());
		hasBoughtSomething=true;
	}

}
