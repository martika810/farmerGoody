package com.martaocio.farmergoody;

public class VelocityProvider {
	
public static float getVelocityByLevel(int levelNumber){
		
		if(levelNumber>=1 && levelNumber<2){
			return 4f;
		}
		else{
			return 4.5f;
		}
				
		
				
	}

}
