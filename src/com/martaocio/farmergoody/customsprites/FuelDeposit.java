package com.martaocio.farmergoody.customsprites;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.martaocio.farmergoody.ResourceManager;

public class FuelDeposit extends AnimatedSprite{
	
	public static final float PORTION=20f;
	public static final float EMPTY=0f;
	public static final float FULL=100f;
	
	public FuelDeposit(float pX, float pY, float width, float height, VertexBufferObjectManager vbom){
		super(pX, pY, width, height, ResourceManager.getInstance().fuelDepositTexture, vbom);
		this.setIgnoreUpdate(true);
		this.setCurrentTileIndex(0);
	}
	
	public void update(int percentage){
		if(percentage==FULL){
			this.setCurrentTileIndex(0);
		}else if(percentage<PORTION*5 && percentage>=PORTION*4){
			this.setCurrentTileIndex(1);
		}else if(percentage<PORTION*4 && percentage>=PORTION*3){
			this.setCurrentTileIndex(2);
		}else if(percentage<PORTION*3 && percentage>=PORTION*2){
			this.setCurrentTileIndex(3);
		}else if(percentage<PORTION*2 && percentage>=PORTION){
			this.setCurrentTileIndex(4);
		}else if(percentage<PORTION && percentage>0){
			this.setCurrentTileIndex(5);
		}else if(percentage==EMPTY){
			this.setCurrentTileIndex(6);
		}
		
	}
	
	public void fill(){
		this.setCurrentTileIndex(0);
		
	}

}
