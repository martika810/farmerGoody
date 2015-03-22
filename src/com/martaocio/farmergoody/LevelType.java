package com.martaocio.farmergoody;

public enum LevelType {
	

	LEVEL1(1,Constants.FARM_DAY,270),
	LEVEL2(2,Constants.FARM_EVENING,385),
	LEVEL3(3,Constants.FOREST,295),
	LEVEL4(4,Constants.FARM_DAY,270),
	LEVEL5(5,Constants.FARM_EVENING,385),
	LEVEL6(6,Constants.FOREST,295),
	LEVEL7(7,Constants.FARM_DAY,270),
	LEVEL8(8,Constants.FARM_EVENING,385),
	LEVEL9(9,Constants.FOREST,295),
	LEVEL10(9,Constants.FARM_DAY,270);
	
	private int number;
	private String typeLevel;
	private int totalPoints;
	

	LevelType(int number,String typeLevel,int totalPoints){
		this.number=number;
		this.typeLevel=typeLevel;
		this.totalPoints=totalPoints;
	}
	
	public static LevelType getLevelType(int levelNumber){
		switch(levelNumber){
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


}
