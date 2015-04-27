package com.martaocio.farmergoody;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class ShopVehiculeItem  extends AnimatedSprite {
	public ShopVehiculeItem(int pX,int pY, VertexBufferObjectManager vbom,Vehicle vehicleSelected){
		super(0, 0, null, vbom);
		
	}
	
	public void setNormalState(Vehicle currentVehicle){
		if(currentVehicle.equals(Vehicle.BICYCLE)){
			this.setCurrentTileIndex(0);
		}else if(currentVehicle.equals(Vehicle.SCOOTER)){
			this.setCurrentTileIndex(2);
		}else{
			this.setCurrentTileIndex(0);
		}
		
		
	}
	
	public void setSelectedState(Vehicle currentVehicle){
		if(currentVehicle.equals(Vehicle.BICYCLE)){
			this.setCurrentTileIndex(1);
		}else if(currentVehicle.equals(Vehicle.SCOOTER)){
			this.setCurrentTileIndex(3);
		}else{
			this.setCurrentTileIndex(0);
		}
		
		
	}
	
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) {
		
		return true;
	}

}
