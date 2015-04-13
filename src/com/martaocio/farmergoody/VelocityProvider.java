package com.martaocio.farmergoody;

public class VelocityProvider {
	
public static float getVelocityByLevel(Vehicle vehicle){
		
	if(vehicle.equals(Vehicle.UNICYCLE)){
		return 5;
	}else if(vehicle.equals(Vehicle.BICYCLE)){
		return 6;
	}
	else if(vehicle.equals(Vehicle.SCOOTER)){
		return 7;
	}
	else if(vehicle.equals(Vehicle.SCOOTER)){
		return 8;
	}
	else{
		return 4;
	}
		
		
				
		
				
	}

public static float getBullVelocityByLevel(int currentLevel){
	if (currentLevel==1 || currentLevel ==2 ||currentLevel==3)return 4;
	else return 4+(currentLevel*0.2f);
						
	}

}
