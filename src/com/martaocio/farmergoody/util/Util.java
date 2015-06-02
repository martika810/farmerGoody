package com.martaocio.farmergoody.util;

import org.andengine.entity.sprite.Sprite;

public class Util {
	
	public static boolean isPointWithinSprite(final float x,final float y,final Sprite sprite){
		return (x>sprite.getX()+sprite.getParent().getX() && x<sprite.getParent().getX()+sprite.getX()+sprite.getWidth() &&
				y>sprite.getY()+sprite.getParent().getY() && y<sprite.getY()+sprite.getHeight()+sprite.getParent().getY());
	}
	
	public static double calculateDistance(float startX,float startY,float endX,float endY){
		return Math.sqrt(Math.pow(endX-startX, 2)+Math.pow(endY-startY, 2));
	}
	
	public static float calculateMidPointX(float startX,float endX){
		return (startX + endX)/2;
	}
	
	public static float calculateMidPointY(float startY,float endY){
		return (startY + endY)/2;
	}

}
