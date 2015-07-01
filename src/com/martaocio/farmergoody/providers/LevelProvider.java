package com.martaocio.farmergoody.providers;

import com.martaocio.farmergoody.domain.AchievementBox;
import com.martaocio.farmergoody.domain.UserState;

public class LevelProvider {
	public static int NUM_TOTAL_LEVELS=20;
	
	public static String getTXMLevel(int levelNumber){
		int convertedLevelNumber;
		if(levelNumber>=20){
			 convertedLevelNumber=levelNumber%LevelProvider.NUM_TOTAL_LEVELS+1;
		}else{
			convertedLevelNumber=levelNumber;
		}
		switch (convertedLevelNumber){
			case 0: 
				
				
				return "tmx/training_x60.tmx";
				
			case 1:
				
				return "tmx/farm1_x60.tmx";
			case 2:
				
				return "tmx/dessert1_x60.tmx";
			case 3:
				
				return "tmx/forest1_x60.tmx";
			case 4:
				return "tmx/farm2.tmx";
			case 5:
				if(UserState.getInstance().isCanGoToAntarctica()){
					return "tmx/antarctica1_x60.tmx";
				}
				else{
					return "tmx/dessert1_x60.tmx";
				}
			case 6:
				
				return "tmx/forest1_x60.tmx";
			case 7:
				
				return "tmx/farm1_x60.tmx";
			case 8:
				//return "tmx/dessert1.tmx";
				return "tmx/dessert1_x60.tmx";
			case 9:
				//return "tmx/level3.tmx";
				return "tmx/forest1_x60.tmx";
			case 10:
				return "tmx/farm2.tmx";
			case 11:
				if(UserState.getInstance().isCanGoToAntarctica()){
					return "tmx/antarctica1_x60.tmx";
				}else{
					return "tmx/dessert1_x60.tmx";
				}
			case 12:
				//return "tmx/level3.tmx";
				return "tmx/forest1_x60.tmx";
			case 13:
				//return "tmx/level2.tmx";
				return "tmx/farm1_x60.tmx";
			case 14:
				//return "tmx/dessert1.tmx";
				return "tmx/dessert1_x60.tmx";
			case 15:
				//return "tmx/level3.tmx";
				return "tmx/forest1_x60.tmx";
			case 16:
				return "tmx/farm2.tmx";
			case 17:
				if(UserState.getInstance().isCanGoToAntarctica()){
					return "tmx/antarctica1_x60.tmx";
				}else{
					return "tmx/dessert1_x60.tmx";
				}
			case 18:
				//return "tmx/level3.tmx";
				return "tmx/forest1_x60.tmx";
			case 19:
				//return "tmx/level2.tmx";
				return "tmx/farm1_x60.tmx";
			case 20:
				//return "tmx/dessert1.tmx";
				return "tmx/dessert1_x60.tmx";
			default:
				return "";
		}
				
		
				
	}

}
