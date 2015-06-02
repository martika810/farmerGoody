package com.martaocio.farmergoody.domain;

import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import com.martaocio.farmergoody.ResourceManager;

public enum Vehicle {
	UNICYCLE(1,Constants.UNICYCLE,100), BICYCLE(2,Constants.BICYCLE,600), SCOOTER(3,Constants.SCOOTER,1200), HARLEY(4,Constants.HARLEY,3000),NONE(0,"",0);

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
		ResourceManager resourceManagerInstance=ResourceManager.getInstance();
		if (vehicle.equals(Vehicle.UNICYCLE)) {
			
			return resourceManagerInstance.unicycleSessionMenuItem;
		} else if (vehicle.equals(Vehicle.BICYCLE)) {
			
			return resourceManagerInstance.bicycleSessionMenuItem;
		} else if (vehicle.equals(Vehicle.SCOOTER)) {
			
			return resourceManagerInstance.scooterSessionMenuItem;
		} else if (vehicle.equals(Vehicle.HARLEY)) {
			
			return resourceManagerInstance.harleySessionMenuItem;
		}
		return resourceManagerInstance.unicycleSessionMenuItem;
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
