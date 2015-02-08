package com.martaocio.farmergoody;

public enum LevelType {
	

	LEVEL1(1,Constants.FARM),
	LEVEL2(2,Constants.FARM),
	LEVEL3(3,Constants.FARM),
	LEVEL4(4,Constants.FOREST),
	LEVEL5(5,Constants.LONDON);
	
	private int number;
	private String typeLevel;
	LevelType(int number,String typeLevel){
		this.number=number;
		this.typeLevel=typeLevel;
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
//		case 6:
//			return LEVEL6;
//		case 7:
//			return LEVEL7;
		default:
			return null;
			
		}
		
	}

	public String getTypeLevel() {
		return typeLevel;
	}


}
