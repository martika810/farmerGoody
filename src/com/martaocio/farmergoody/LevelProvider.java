package com.martaocio.farmergoody;

public class LevelProvider {
	
	public static String getTXMLevel(int levelNumber){
		
		switch (levelNumber){
			case 1:
				return "tmx/level_flat.tmx";
			
			case 2:
				return "tmx/level_flat2.tmx";
			default:
				return "tmx/level_flat.tmx";
		}
				
		
				
	}

}
