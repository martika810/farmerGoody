package com.martaocio.farmergoody;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

public enum Vehicle {
	UNICYCLE(1,Constants.UNICYCLE,50), BICYCLE(2,Constants.BICYCLE,100), SCOOTER(3,Constants.SCOOTER,600), HARLEY(4,Constants.HARLEY,4000),NONE(0,"",0);

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
	
	public static ITextureRegion getVehicleShopItem(Vehicle vehicle) {

		if (vehicle.equals(Vehicle.UNICYCLE)) {
			//return ResourceManager.getInstance().unicycleShopItem;
			return ResourceManager.getInstance().unicycleSessionMenuItem;
		} else if (vehicle.equals(Vehicle.BICYCLE)) {
			//return ResourceManager.getInstance().bicycleShopItem;
			return ResourceManager.getInstance().bicycleSessionMenuItem;
		} else if (vehicle.equals(Vehicle.SCOOTER)) {
			//return ResourceManager.getInstance().bicycleShopItem;
			return ResourceManager.getInstance().scooterSessionMenuItem;
		} else if (vehicle.equals(Vehicle.HARLEY)) {
			//return ResourceManager.getInstance().hardleyShopItem;
			return ResourceManager.getInstance().harleySessionMenuItem;
		}
		return ResourceManager.getInstance().unicycleSessionMenuItem;
	}
	
	public static ITextureRegion getVehicleSelectVehicleItem(Vehicle vehicle) {

		if (vehicle.equals(Vehicle.UNICYCLE)) {
			return ResourceManager.getInstance().unicycleSessionMenuItem;
		} else if (vehicle.equals(Vehicle.BICYCLE)) {
		return ResourceManager.getInstance().bicycleSessionMenuItem;
		} else if (vehicle.equals(Vehicle.SCOOTER)) {
			return ResourceManager.getInstance().scooterSessionMenuItem;
		} else if (vehicle.equals(Vehicle.HARLEY)) {
			return ResourceManager.getInstance().harleySessionMenuItem;
		}
		return ResourceManager.getInstance().vehicleNoImage;
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
