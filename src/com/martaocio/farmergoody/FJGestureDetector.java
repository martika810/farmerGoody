package com.martaocio.farmergoody;

import org.andengine.input.touch.detector.SurfaceGestureDetector;

import com.martaocio.farmergoody.scenes.BaseScene;
import com.martaocio.farmergoody.scenes.GameScene;

import android.content.Context;

public class FJGestureDetector extends SurfaceGestureDetector{
	
	private boolean wasSingleTapMovement;
	private boolean wasSwipeMovement;
	
	public FJGestureDetector(Context pContext) {
		super(pContext);
		
	}
	
	

	@Override
	protected boolean onSingleTap() {
		setWasSwipeMovement(true);
		return true;
	}

	@Override
	protected boolean onDoubleTap() {
		
		return false;
	}

	@Override
	protected boolean onSwipeUp() {
		setWasSwipeMovement(true);
		return true;
	}

	@Override
	protected boolean onSwipeDown() {
		setWasSwipeMovement(true);
		return true;
	}

	@Override
	protected boolean onSwipeLeft() {
		setWasSwipeMovement(true);
		return true;
	}

	@Override
	protected boolean onSwipeRight() {
		setWasSwipeMovement(true);
		return true;
	}



	@Override
	public void reset() {
		super.reset();
		setWasSingleTapMovement(false);
		setWasSwipeMovement(false);
	}



	public boolean isWasSingleTapMovement() {
		return wasSingleTapMovement;
	}



	private void setWasSingleTapMovement(boolean wasSingleTapMovement) {
		this.wasSingleTapMovement = wasSingleTapMovement;
	}



	public boolean isWasSwipeMovement() {
		return wasSwipeMovement;
	}



	private void setWasSwipeMovement(boolean wasSwipeMovement) {
		this.wasSwipeMovement = wasSwipeMovement;
	}
	
	
	
//	private boolean isPlayerArea(Player player, float f, float g) {
//		if ((f > player.getX() + 3 && f < (player.getX() + player.getWidth() - 3))
//				&& (g > player.getY() + 3 && (g < player.getY() + player.getHeight() - 3))) {
//			return true;
//		}
//
//		return false;
//	}

}
