package com.martaocio.farmergoody;

import org.andengine.opengl.texture.region.ITiledTextureRegion;

public enum Vehicle {
	UNICYCLE(1,Constants.UNICYCLE,50), BICYCLE(2,Constants.BICYCLE,50), SCOOTER(3,Constants.SCOOTER,50), HARLEY(4,Constants.HARLEY,4000),NONE(0,"",0);

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
	
	public static Vehicle getByDescription(String vehicleName){
		if(vehicleName.equals(Constants.UNICYCLE)){
			return Vehicle.UNICYCLE;
		}
		else if(vehicleName.equals(Constants.BICYCLE)){
			return Vehicle.BICYCLE;
		}else if(vehicleName.equals(Constants.SCOOTER)){
			return Vehicle.SCOOTER;
		}else if(vehicleName.equals(Constants.HARLEY)){
			return Vehicle.HARLEY;
		}else{
			return Vehicle.NONE;
		}
	}
	

	

	
	

}
