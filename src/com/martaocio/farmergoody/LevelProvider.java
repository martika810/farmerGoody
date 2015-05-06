package com.martaocio.farmergoody;

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
				
				return "tmx/training.tmx";
				
			case 1:
				return "tmx/level2.tmx";
			case 2:
				return "tmx/dessert1.tmx";
			case 3:
				return "tmx/level3.tmx";
			case 4:
				return "tmx/level2.tmx";
			case 5:
				return "tmx/dessert1.tmx";
			case 6:
				return "tmx/level3.tmx";
			case 7:
				return "tmx/level2.tmx";
			case 8:
				return "tmx/dessert1.tmx";
			case 9:
				return "tmx/level3.tmx";
			case 10:
				return "tmx/level2.tmx";
			case 11:
				return "tmx/dessert1.tmx";
			case 12:
				return "tmx/level3.tmx";
			case 13:
				return "tmx/level2.tmx";
			case 14:
				return "tmx/dessert1.tmx";
			case 15:
				return "tmx/level3.tmx";
			case 16:
				return "tmx/level2.tmx";
			case 17:
				return "tmx/dessert1.tmx";
			case 18:
				return "tmx/level3.tmx";
			case 19:
				return "tmx/level2.tmx";
			case 20:
				return "tmx/dessert1.tmx";
			default:
				return "";
		}
				
		
				
	}

}
