package com.martaocio.farmergoody;

public class SpriteTag {

	public static final String BULL = "bull";
	public static final String STAIR = "stair";
	public static final String PLAYER = "player";
	public static final String LINE="line";
	public static final String TOMATO5 = "t5";
	public static final String TOMATO10 = "t10";
	public static final String MINUSTOMATO5 = "mt5";
	public static final String MINUSTOMATO10 = "mt10";
	public static final String MINUSTOMATO20 = "mt20";
	public static final String GROUND = "ground";
	public static final String FENCE = "fence";
	public static final String CORRECT = "correct";
	public static final String WRONG = "wrong";
	public static final String LIFE = "life";
	public static final String END = "end";

	public static boolean isTomatoTag(String objectName){
		return objectName.equals(TOMATO5)||objectName.equals(TOMATO10);
		
	}
	
	public static boolean isMinusTomatoTag(String objectName){
		return objectName.equals(MINUSTOMATO5)||objectName.equals(MINUSTOMATO10)||objectName.equals(MINUSTOMATO20);
		
	}
}
