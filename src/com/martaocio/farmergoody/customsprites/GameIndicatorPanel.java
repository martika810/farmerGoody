package com.martaocio.farmergoody.customsprites;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;

import com.martaocio.farmergoody.ResourceManager;
import com.martaocio.farmergoody.domain.GameSession;
import com.martaocio.farmergoody.domain.LevelType;
import com.martaocio.farmergoody.domain.UserState;

public class GameIndicatorPanel extends BasicGameIndicatorPanel {

	private final int TAG_REWARD_ICON = 999;
	protected final int TAG_TOMAT_ICON = 998;

	protected final int SCREEN_WIDTH = 800;
	protected final int SCREEN_HEIGHT = 480;

	// /////////////////SPRITES///////////////////////////

	public FuelDeposit fuelDepositIcon;
	public Sprite levelIcon, lifeIndicator, moneyIndicator;
	public Sprite rightPathScoreIcon;

	public Sprite restartButton;
	public Sprite leftButton;
	public Sprite rightButton;

	public Text textNumberLife, textMoney;
	public Text textLevel;


	// ///////////////VALUES//////////////////////////////

	public void createGameIndicator(VertexBufferObjectManager vbom) {
		GameSession selectSession = UserState.getInstance().getSelectedSession();
		int currentLevel = selectSession.getCurrentLevel();
		int currentNumberLifes = selectSession.getNumberLifes();
		int currentMoney = selectSession.getCurrentMoney();

		super.createGameIndicator(vbom);

		fuelDepositIcon = new FuelDeposit(20, 280, 70, 70, vbom);

		fuelDepositIcon.update(UserState.getInstance().getSelectedSession().getFuelpoints());
		lifeIndicator = new Sprite(20, 110, 80, 80, ResourceManager.getInstance().lifeIndicatorTexture, vbom);

		textNumberLife = new Text(lifeIndicator.getX() + 30, lifeIndicator.getY() + 20, ResourceManager.getInstance().font,
				currentNumberLifes + "    ", new TextOptions(HorizontalAlign.CENTER), vbom);

		moneyIndicator = new Sprite(20, 190, 80, 80, ResourceManager.getInstance().moneyIndicatorTexture, vbom);
		textMoney = new Text(moneyIndicator.getX() + 10, moneyIndicator.getY() + 20, ResourceManager.getInstance().font, currentMoney + "$"
				+ "    ", new TextOptions(HorizontalAlign.CENTER), vbom);
		levelIcon = new Sprite(100, 280, 70, 70, ResourceManager.getInstance().levelIcon, vbom);

		textLevel = new Text(levelIcon.getX() + 20, levelIcon.getY() + 20, ResourceManager.getInstance().font, "#" + currentLevel + "    ",
				new TextOptions(HorizontalAlign.CENTER), vbom);
		tomatoScoreIcon.setTag(TAG_TOMAT_ICON);

	}

	public void attachGameIndicatorToHud(final HUD hud) {
		
		super.attachGameIndicatorToHud(hud);
		hud.attachChild(levelIcon);
		hud.attachChild(textLevel);
		hud.attachChild(fuelDepositIcon);
		hud.attachChild(lifeIndicator);
		hud.attachChild(moneyIndicator);
		hud.attachChild(textNumberLife);
		hud.attachChild(textMoney);

	}
	
	public void show(boolean shouldShow){
		super.show(shouldShow);
		levelIcon.setVisible(shouldShow);
		textLevel.setVisible(shouldShow);
		fuelDepositIcon.setVisible(shouldShow);
		lifeIndicator.setVisible(shouldShow);
		moneyIndicator.setVisible(shouldShow);
		textNumberLife.setVisible(shouldShow);
		textMoney.setVisible(shouldShow);
	}

	

	public FuelDeposit getFuelDepositIcon() {
		return fuelDepositIcon;
	}

	public Sprite getLevelIcon() {
		return levelIcon;
	}

	public Sprite getLifeIndicator() {
		return lifeIndicator;
	}

	public Sprite getMoneyIndicator() {
		return moneyIndicator;
	}

	public Sprite getRightPathScoreIcon() {
		return rightPathScoreIcon;
	}

	public Sprite getRestartButton() {
		return restartButton;
	}

	public Sprite getLeftButton() {
		return leftButton;
	}

	public Sprite getRightButton() {
		return rightButton;
	}

	public Text getTextNumberLife() {
		return textNumberLife;
	}

	public Text getTextMoney() {
		return textMoney;
	}

	public Text getTextLevel() {
		return textLevel;
	}



}
