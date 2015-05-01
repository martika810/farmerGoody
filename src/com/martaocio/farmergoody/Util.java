package com.martaocio.farmergoody;

import org.andengine.entity.sprite.Sprite;

public class Util {
	
	public static boolean isPointWithinSprite(final float x,final float y,final Sprite sprite){
		return (x>sprite.getX()+sprite.getParent().getX() && x<sprite.getParent().getX()+sprite.getX()+sprite.getWidth() &&
				y>sprite.getY()+sprite.getParent().getY() && y<sprite.getY()+sprite.getHeight()+sprite.getParent().getY());
	}

}
