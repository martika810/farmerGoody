package com.martaocio.farmergoody;

public class LevelProvider {
	
	public static String getTXMLevel(int levelNumber){
		
		switch (levelNumber){
			case 1:
				return "tmx/level2.tmx";
			case 2:
				return "tmx/level2.tmx";
			case 3:
				return "tmx/level3.tmx";
			case 4:
				return "tmx/level2.tmx";
			case 5:
				return "tmx/level2.tmx";
			case 6:
				return "tmx/level3.tmx";
			case 7:
				return "tmx/level2.tmx";
			case 8:
				return "tmx/level2.tmx";
			case 9:
				return "tmx/level3.tmx";
			case 10:
				return "tmx/level2.tmx";
			default:
				return "";
		}
				
		
				
	}

}
