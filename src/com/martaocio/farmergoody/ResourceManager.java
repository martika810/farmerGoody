package com.martaocio.farmergoody;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.BoundCamera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import android.content.Context;
import android.graphics.Color;
import android.os.Vibrator;
import android.util.Log;

public class ResourceManager {

	public Engine engine;
	public MainGameActivity activity;
	public BoundCamera camera;
	public VertexBufferObjectManager vbom;
	public Font font;
	public Vibrator vibrator;

	// load sounds
	public Music themeMusic;
	public Sound eatTomato;
	public Sound jumpSound;

	// TEXTURES
	// this class is like a canvas to paint over
	private BuildableBitmapTextureAtlas mainMenuTexturesAtlas;
	
	private BuildableBitmapTextureAtlas subMenuShopTextureAtlas;
	private BuildableBitmapTextureAtlas subMenuSessionTextureAtlas;
	private BuildableBitmapTextureAtlas subMenuSelectVehicleTextureAtlas;

	private BuildableBitmapTextureAtlas gameTexturesAtlas,playerTexturesAtlas,tomatoScorerAtlas;

	private BuildableBitmapTextureAtlas levelFailedAtlas, levelPassedAtlas, pauseAtlas,levelNoMoneyAtlas;

	public ITextureRegion playMenuButton, continueMenuButton,shopMenuButton, mainMenuBackground, shopMenuBackGround;
	// public ITextureRegion restartButton,quitButton,failedBG,passedBG;
	public ITextureRegion restartButton, quitButton, playButton, pauseButton, failedBG, passedBG, pauseBG,noMoneyBG;

	// Create coin texture
	// public ITextureRegion coinTexture;

	public ITiledTextureRegion playerTexture;// this texture is gonna be tiled
	public ITiledTextureRegion playerRidingUnicycleTexture,playerRidingBicycleTexture,playerRidingScooterTexture;
	public ITiledTextureRegion tomatoTexture;
	public ITiledTextureRegion bullTexture;


	public ITextureRegion upArrowTexture,rightArrowTexture,leftArrowTexture;

	public ITiledTextureRegion unicycleShopItem,bicycleShopItem,scooterShopItem,hardleyShopItem,tomatoScorer;
	
	public ITextureRegion unicycleSessionMenuItem,bicycleSessionMenuItem,scooterSessionMenuItem,harleySessionMenuItem;
	
	public ITextureRegion sessionMenuBackground;
	public ITextureRegion selectVehiculeMenuBackground;
	public ITextureRegion lockIcon;
	public ITextureRegion sessionMenuItem;
	public ITextureRegion iconUnicycle;
	public ITextureRegion vehicleNoImage;
	public ITextureRegion levelIcon,levelIconForest,levelIconFarmDay,levelIconFarmEvening;
	public ITextureRegion unycleImage;
	public ITextureRegion buyBtn;
	public ITextureRegion pauseBtnTexture,jumpBtnTextute,backBtnTexture,playSmallBtnTexture,deleteSmallBtnTexture;

	public ITextureRegion tomatoIconTexture;
		
	public ITextureRegion minusPoint5Texture,minusPoint10Texture,minusPoint20Texture,point5Texture,point10Texture;
	

	public ITextureRegion title;
	

	// Singleton pattern so all scenes use the same Resouce Manager
	private static final ResourceManager INSTANCE = new ResourceManager();

	public void loadGameResources() {
		loadGameSounds();
		loadGameGraphics();

	}

	private void loadGameGraphics() {

		// create font
		FontFactory.setAssetBasePath("fonts/");
		final ITexture fontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		font = FontFactory.createFromAsset(activity.getFontManager(), fontTexture, activity.getAssets(), "BebasNeue.otf", 26, true,
				Color.BLACK);
		font.load();

		this.levelFailedAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		this.levelNoMoneyAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.levelPassedAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		this.pauseAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		this.gameTexturesAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		this.playerTexturesAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		this.tomatoScorerAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		// this.coinTexture=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas,
		// activity.getAssets(), "coin.png");
		this.playerTexture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(playerTexturesAtlas, activity.getAssets(),
				"player.png", 6, 1);
		this.playerRidingUnicycleTexture=BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(playerTexturesAtlas, activity.getAssets(),
				"riding_unicycle.png", 6, 1);
		this.playerRidingBicycleTexture=BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(playerTexturesAtlas, activity.getAssets(),
				"player_riding_bicycle.png", 6, 1);
		
		this.playerRidingScooterTexture=BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(playerTexturesAtlas, activity.getAssets(),
				"player_riding_scooter.png", 6, 1);
		this.bullTexture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTexturesAtlas, activity.getAssets(), "bull.png",
				4, 1);
		this.tomatoTexture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTexturesAtlas, activity.getAssets(),
				"tomato.png", 5, 1);
		
		this.tomatoScorer = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(tomatoScorerAtlas, activity.getAssets(),
				"tomatoScorerSheet.png", 3, 5);

		
		this.pauseBtnTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(),
				"pausebtn.png");
		
		this.jumpBtnTextute = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(),
				"jump.png");
		
		

		String levelType = LevelType.getLevelType(UserState.getInstance().getCurrentLevel()).getTypeLevel();
		this.tomatoIconTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(),
				"tomato_icon.png");
		this.levelIcon = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(), "level70x70.png");

		this.point5Texture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(),
				ImageProvider.getPointImage(Constants.POINT5));

		this.point10Texture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(),
				ImageProvider.getPointImage(Constants.POINT10));

		minusPoint5Texture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(),
				ImageProvider.getPointImage(Constants.MINUSPOINT5));

		minusPoint10Texture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(),
				ImageProvider.getPointImage(Constants.MINUSPOINT10));
		minusPoint20Texture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(),
				ImageProvider.getPointImage(Constants.MINUSPOINT20));
		// this.passedBG=BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.levelPassedAtlas,activity.getAssets(),"level_cleared_background.png");

		this.failedBG = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.levelFailedAtlas, activity.getAssets(),
				"level_failed.png");
		this.passedBG = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.levelPassedAtlas, activity.getAssets(),
				"level_passed.png");
		this.pauseBG = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.levelPassedAtlas, activity.getAssets(),
				"pause_screen.png");
		
		this.noMoneyBG = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.levelNoMoneyAtlas, activity.getAssets(),
				"level_no_money.png");
		this.restartButton = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.levelPassedAtlas, activity.getAssets(),
				"restartbtn.png");

		this.quitButton = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.levelPassedAtlas, activity.getAssets(), "homebtn.png");
		this.playButton = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.levelPassedAtlas, activity.getAssets(), "playbtn.png");
		this.pauseButton = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.levelPassedAtlas, activity.getAssets(),
				"pausebtn.png");

		this.title = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(), "title.png");

		try {
			this.gameTexturesAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
			this.gameTexturesAtlas.load();
			
			this.playerTexturesAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
			this.playerTexturesAtlas.load();
			
			this.tomatoScorerAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
			this.tomatoScorerAtlas.load();

			this.levelFailedAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
			this.levelFailedAtlas.load();

			this.levelPassedAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
			this.levelPassedAtlas.load();
			
			this.levelNoMoneyAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
			this.levelNoMoneyAtlas.load();

		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	private void loadGameSounds() {
		try {

			this.eatTomato = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "sfx/nom.wav");
			this.jumpSound = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "sfx/jump.wav");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void loadMenuResources() {
		loadMenuSounds();
		loadMenuGraphics();

	}

	private void loadMenuGraphics() {
		// Indicate the folder of the assetsf
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		// create font
		FontFactory.setAssetBasePath("fonts/");
		final ITexture fontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		font = FontFactory.createFromAsset(activity.getFontManager(), fontTexture, activity.getAssets(), "BebasNeue.otf", 26, true,
				Color.BLACK);
		font.load();

		this.mainMenuTexturesAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		this.subMenuShopTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024,
				TextureOptions.NEAREST_PREMULTIPLYALPHA);
		
		this.subMenuSessionTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024,
				TextureOptions.NEAREST_PREMULTIPLYALPHA);
		this.subMenuSelectVehicleTextureAtlas=new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024,
				TextureOptions.NEAREST_PREMULTIPLYALPHA);


		// add the background to the canvas
		this.mainMenuBackground = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mainMenuTexturesAtlas, activity.getAssets(),
				"menubackground3.png");
		this.shopMenuBackGround = BitmapTextureAtlasTextureRegionFactory.createFromAsset(subMenuShopTextureAtlas, activity.getAssets(),
				"shopPanel.png");
		this.unicycleShopItem=BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(subMenuShopTextureAtlas, activity.getAssets(),
				"unicycleShopItem.png", 2, 1);
		this.bicycleShopItem=BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(subMenuShopTextureAtlas, activity.getAssets(),
				"bicycleShopIconSheet.png", 2, 1);
		this.scooterShopItem=BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(subMenuShopTextureAtlas, activity.getAssets(),
				"ScooterShopItem.png", 2, 1);
		this.hardleyShopItem=BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(subMenuShopTextureAtlas, activity.getAssets(),
				"harleyShopIcon.png", 2, 1);
		this.unycleImage = BitmapTextureAtlasTextureRegionFactory.createFromAsset(subMenuShopTextureAtlas, activity.getAssets(),
				"unicycle.png");
		this.buyBtn=BitmapTextureAtlasTextureRegionFactory.createFromAsset(subMenuShopTextureAtlas, activity.getAssets(),
				"bagShopIcon.png");
		
		this.sessionMenuBackground=BitmapTextureAtlasTextureRegionFactory.createFromAsset(subMenuSessionTextureAtlas, activity.getAssets(),
			"sessionPanel.png");
		
		this.selectVehiculeMenuBackground=BitmapTextureAtlasTextureRegionFactory.createFromAsset(subMenuSelectVehicleTextureAtlas, activity.getAssets(),
				"selectVehiclePanel.png");
		
		this.lockIcon = BitmapTextureAtlasTextureRegionFactory.createFromAsset(subMenuSelectVehicleTextureAtlas, activity.getAssets(),
				"lock.png");
		
		this.sessionMenuItem=BitmapTextureAtlasTextureRegionFactory.createFromAsset(subMenuSessionTextureAtlas, activity.getAssets(),
				"session_item.png");
		
		this.playSmallBtnTexture=BitmapTextureAtlasTextureRegionFactory.createFromAsset(subMenuSessionTextureAtlas, activity.getAssets(),
				"play_50x50.png");
		this.deleteSmallBtnTexture=BitmapTextureAtlasTextureRegionFactory.createFromAsset(subMenuSessionTextureAtlas, activity.getAssets(),
				"deletebtn.png");
		this.levelIcon=BitmapTextureAtlasTextureRegionFactory.createFromAsset(subMenuSessionTextureAtlas, activity.getAssets(),
				"level_noimage.png");
		this.levelIconForest=BitmapTextureAtlasTextureRegionFactory.createFromAsset(subMenuSessionTextureAtlas, activity.getAssets(),
				"levelIcon_forest.png");
		this.levelIconFarmDay=BitmapTextureAtlasTextureRegionFactory.createFromAsset(subMenuSessionTextureAtlas, activity.getAssets(),
				"levelIcon_farm_day.png");
		this.levelIconFarmEvening=BitmapTextureAtlasTextureRegionFactory.createFromAsset(subMenuSessionTextureAtlas, activity.getAssets(),
				"levelIcon_farm_even.png");
		this.iconUnicycle=BitmapTextureAtlasTextureRegionFactory.createFromAsset(subMenuSessionTextureAtlas, activity.getAssets(),
				"icon_unicycle.png");
		this.vehicleNoImage=BitmapTextureAtlasTextureRegionFactory.createFromAsset(subMenuSessionTextureAtlas, activity.getAssets(),
				"vehicle_noimage.png");
		this.unicycleSessionMenuItem=BitmapTextureAtlasTextureRegionFactory.createFromAsset(subMenuSessionTextureAtlas, activity.getAssets(),
				"unicycleSessionItem.png");

		this.bicycleSessionMenuItem=BitmapTextureAtlasTextureRegionFactory.createFromAsset(subMenuSessionTextureAtlas, activity.getAssets(),
				"bycicleSessionIcon.png");

		this.scooterSessionMenuItem=BitmapTextureAtlasTextureRegionFactory.createFromAsset(subMenuSessionTextureAtlas, activity.getAssets(),
				"scooterSessionItem.png");

		this.harleySessionMenuItem=BitmapTextureAtlasTextureRegionFactory.createFromAsset(subMenuSessionTextureAtlas, activity.getAssets(),
				"harleySessionItem.png");


		// add the play button
		this.leftArrowTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mainMenuTexturesAtlas, activity.getAssets(),
				"leftbtn.png");
		this.playMenuButton = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mainMenuTexturesAtlas, activity.getAssets(),
				"playbtn.png");
		this.backBtnTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mainMenuTexturesAtlas, activity.getAssets(),
				"backbtn.png");
		this.continueMenuButton = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mainMenuTexturesAtlas, activity.getAssets(),
				"restartbtn.png");
		this.shopMenuButton = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mainMenuTexturesAtlas, activity.getAssets(),
				"shopbtn.png");

		try {
			this.mainMenuTexturesAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
			this.mainMenuTexturesAtlas.load();
			
			this.subMenuShopTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
			this.subMenuShopTextureAtlas.load();
			
			this.subMenuSessionTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
			this.subMenuSessionTextureAtlas.load();
			
			this.subMenuSelectVehicleTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
			this.subMenuSelectVehicleTextureAtlas.load();

		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	private void loadMenuSounds() {
		// try {
		// themeMusic=MusicFactory.createMusicFromAsset(activity.getMusicManager(),
		// activity, "sfx/theme_music.ogg");
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	// method to prepare out manager
	public static void prepareManager(Engine engine, MainGameActivity activity, BoundCamera camera, VertexBufferObjectManager vbom,
			Object vibrator) {
		getInstance().activity = activity;
		getInstance().engine = engine;
		getInstance().camera = camera;
		getInstance().vbom = vbom;
		getInstance().vibrator = (Vibrator) vibrator;

	}

	public static ResourceManager getInstance() {
		return INSTANCE;
	}

}
