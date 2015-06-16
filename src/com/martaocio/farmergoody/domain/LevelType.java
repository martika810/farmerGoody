package com.martaocio.farmergoody.domain;

import org.andengine.util.color.Color;

import com.martaocio.farmergoody.providers.LevelProvider;

public enum LevelType {
	
	TRAINING_LEVEL(0,Constants.FARM_EVENING,150,Constants.FARM_GROUND_COLOR),
	LEVEL1(1,Constants.FARM_EVENING,385,Constants.FARM_GROUND_COLOR),
	LEVEL2(2,Constants.DESERT,370,Constants.DESERT_COLOR),
	LEVEL3(3,Constants.FOREST,295,Constants.FOREST_GROUND_COLOR),
	LEVEL4(4,Constants.FARM_EVENING,325,Constants.FARM_GROUND_COLOR),
	LEVEL5(5,Constants.DESERT,370,Constants.DESERT_COLOR),
	LEVEL6(6,Constants.FOREST,295,Constants.FOREST_GROUND_COLOR),
	LEVEL7(7,Constants.FARM_EVENING,385,Constants.FARM_GROUND_COLOR),
	LEVEL8(8,Constants.DESERT,370,Constants.DESERT_COLOR),
	LEVEL9(9,Constants.FOREST,295,Constants.FOREST_GROUND_COLOR),
	LEVEL10(10,Constants.FARM_EVENING,325,Constants.FARM_GROUND_COLOR),
	LEVEL11(11,Constants.DESERT,370,Constants.DESERT_COLOR),
	LEVEL12(12,Constants.FOREST,295,Constants.FOREST_GROUND_COLOR),
	LEVEL13(13,Constants.FARM_EVENING,385,Constants.FARM_GROUND_COLOR),
	LEVEL14(14,Constants.DESERT,370,Constants.DESERT_COLOR),
	LEVEL15(15,Constants.FOREST,295,Constants.FOREST_GROUND_COLOR),
	LEVEL16(16,Constants.FARM_EVENING,325,Constants.FARM_GROUND_COLOR),
	LEVEL17(17,Constants.DESERT,370,Constants.DESERT_COLOR),
	LEVEL18(18,Constants.FOREST,295,Constants.FOREST_GROUND_COLOR),
	LEVEL19(19,Constants.FARM_EVENING,385,Constants.FARM_GROUND_COLOR),
	LEVEL20(20,Constants.DESERT,370,Constants.DESERT_COLOR);
	
	
	private int number;
	private String typeLevel;
	private int totalPoints;
	private Color colorGround;
	

	

	LevelType(int number,String typeLevel,int totalPoints,Color colorGround){
		this.number=number;
		this.typeLevel=typeLevel;
		this.totalPoints=totalPoints;
		this.colorGround=colorGround;
	}
	

	public static LevelType getLevelType(int levelNumber){
		int convertedLevelNumber;
		if(levelNumber>=20){
			 convertedLevelNumber=levelNumber%LevelProvider.NUM_TOTAL_LEVELS+1;
		}else{
			convertedLevelNumber=levelNumber;
		}
		switch(convertedLevelNumber){
		case 0:
			return TRAINING_LEVEL;
		case 1:
			return LEVEL1;
		case 2:
			return LEVEL2;
		case 3:
			return LEVEL3;
		case 4:
			return LEVEL4;
		case 5:
			return LEVEL5;
		case 6:
			return LEVEL6;
		case 7:
			return LEVEL7;
		case 8:
			return LEVEL8;
		case 9:
			return LEVEL9;
		case 10:
			return LEVEL10;
		case 11:
			return LEVEL11;
		case 12:
			return LEVEL12;
		case 13:
			return LEVEL13;
		case 14:
			return LEVEL14;
		case 15:
			return LEVEL15;
		case 16:
			return LEVEL16;
		case 17:
			return LEVEL17;
		case 18:
			return LEVEL18;
		case 19:
			return LEVEL19;
		case 20:
			return LEVEL20;
		default:
			return null;
			
		}
		
	}

	public String getTypeLevel() {
		return typeLevel;
	}
	
	public int getTotalPoints() {
		return totalPoints;
	}
	
	public Color getColorGround() {
		return colorGround;
	}

	
	


}
