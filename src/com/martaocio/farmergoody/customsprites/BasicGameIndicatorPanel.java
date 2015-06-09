package com.martaocio.farmergoody.customsprites;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;

import com.martaocio.farmergoody.ResourceManager;

public class BasicGameIndicatorPanel {
	
	protected final int TAG_SCORE_TEXT = 997;
	
	public TomatoScorer tomatoScoreIcon;
	public Sprite title;
	public Text textScore;
	//protected HUD hud;
	
	public void createGameIndicator(VertexBufferObjectManager vbom){
		
		tomatoScoreIcon = new TomatoScorer(20, 20, 200, 80, vbom);
		textScore = new Text(40, 50, ResourceManager.getInstance().font, "    ", new TextOptions(HorizontalAlign.CENTER), vbom);
		textScore.setTag(TAG_SCORE_TEXT);
		title = new Sprite(693, 5, 107, 50, ResourceManager.getInstance().title, vbom);
		
		
	}
	
	public void attachGameIndicatorToHud(final HUD hud){
		hud.attachChild(tomatoScoreIcon);
		hud.attachChild(textScore);
		hud.attachChild(title);
				
	}
	
	public void show(boolean shouldShow){
		tomatoScoreIcon.setVisible(shouldShow);
		textScore.setVisible(shouldShow);
		title.setVisible(shouldShow);
	}
	
	public TomatoScorer getTomatoScoreIcon() {
		return tomatoScoreIcon;
	}
	
	public Text getTextScore() {
		return textScore;
	}
	
	public Sprite getTitle() {
		return title;
	}


}
