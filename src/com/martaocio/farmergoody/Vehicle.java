package com.martaocio.farmergoody;

import org.andengine.opengl.texture.region.ITiledTextureRegion;

public enum Vehicle {
	UNICYCLE(1,"Unicyle",50), BICYCLE(2,"Bicyle",100), SCOOTER(3,"Scooter",100), HARLEY(4,"Hardley",1000),NONE(0,"",0);

	private int id;
	private String description;
	private int price;

	Vehicle(int id,String description,int price) {
		this.id=id;
		this.description=description;
		this.price=price;
	}

	public int getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public int getPrice() {
		return price;
	}
	
	public static ITiledTextureRegion getVehicleShopItem(Vehicle vehicle) {

		if (vehicle.equals(Vehicle.UNICYCLE)) {
			return ResourceManager.getInstance().unicycleShopItem;
		} else if (vehicle.equals(Vehicle.BICYCLE)) {
			return ResourceManager.getInstance().bicycleShopItem;
		} else if (vehicle.equals(Vehicle.SCOOTER)) {
			return ResourceManager.getInstance().scooterShopItem;
		} else if (vehicle.equals(Vehicle.HARLEY)) {
			return ResourceManager.getInstance().hardleyShopItem;
		}
		return ResourceManager.getInstance().unicycleShopItem;
	}
	

	

	
	

}
