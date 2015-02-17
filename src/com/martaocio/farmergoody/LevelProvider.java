package com.martaocio.farmergoody;

public class LevelProvider {
	
	public static String getTXMLevel(int levelNumber){
		
		switch (levelNumber){
			case 1:
				return "tmx/level_flat.tmx";
			
			case 2:
				return "tmx/level_flat2.tmx";
			case 3:
				return "tmx/level_flat3.tmx";
			case 4:
				return "tmx/level_flat4.tmx";
			case 5:
				return "tmx/level_flat4_2.tmx";
			case 6:
				return "tmx/level_flat4_3.tmx";
			case 7:
				return "tmx/level_flat5.tmx";
			case 8:
				return "tmx/level_london_2.tmx";
			case 9:
				return "tmx/level_london_3.tmx";
			default:
				return "";
		}
				
		
				
	}

}
