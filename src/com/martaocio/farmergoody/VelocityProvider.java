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
	else if(vehicle.equals(Vehicle.HARLEY)){
		return 9;
	}
	else{
		return 4;
	}
		
		
				
		
				
	}

public static float getBullVelocityByLevel(int currentLevel){
	if (currentLevel==1 || currentLevel ==2 ||currentLevel==3)return 4;
	if(currentLevel>=45) return 4+(17*0.2f)+((28)*0.05f);
	if(currentLevel>17 && currentLevel<45) return 4+(17*0.2f)+((currentLevel-17)*0.05f);
	else return 4+(currentLevel*0.2f);
						
	}

}
