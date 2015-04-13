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
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
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
	public Sound goodSound;
	public Sound badSound;
	public Sound jumpSound;

	// TEXTURES
	// this class is like a canvas to paint over
	private BuildableBitmapTextureAtlas mainMenuTexturesAtlas;
	
	private BuildableBitmapTextureAtlas subMenuShopTextureAtlas;
	private BuildableBitmapTextureAtlas subMenuSessionTextureAtlas;
	private BuildableBitmapTextureAtlas subMenuSelectVehicleTextureAtlas;
	private BuildableBitmapTextureAtlas splashTexturesAtlas;

	private BuildableBitmapTextureAtlas gameTexturesAtlas,playerTexturesAtlas,tomatoScorerAtlas;

	private BuildableBitmapTextureAtlas levelFailedAtlas, levelPassedAtlas, pauseAtlas,levelNoMoneyAtlas;

	public ITextureRegion playMenuButton, continueMenuButton,shopMenuButton, mainMenuBackground, shopMenuBackGround;
	// public ITextureRegion restartButton,quitButton,failedBG,passedBG;
	public ITextureRegion restartButton, quitButton, playButton, pauseButton, failedBG, passedBG, pauseBG,noMoneyBG;

	// Create coin texture
	// public ITextureRegion coinTexture;

	public ITiledTextureRegion playerTexture;// this texture is gonna be tiled
	public ITiledTextureRegion playerRidingUnicycleTexture,playerRidingBicycleTexture/*,playerRidingScooterTexture*/;
	//public ITiledTextureRegion tomatoTexture;
	public ITiledTextureRegion bullTexture;


	public ITextureRegion upArrowTexture,rightArrowTexture;

	public ITiledTextureRegion unicycleShopItem,bicycleShopItem,hardleyShopItem,tomatoScorer;
	
	public ITextureRegion unicycleSessionMenuItem,bicycleSessionMenuItem,scooterSessionMenuItem,harleySessionMenuItem;
	
	public ITextureRegion sessionMenuBackground;
	public ITextureRegion selectVehiculeMenuBackground;
	public ITextureRegion lockIcon;
	public ITextureRegion sessionMenuItem;
	public ITextureRegion iconUnicycle;
	public ITextureRegion vehicleNoImage;
	public ITextureRegion levelIcon,levelIconForest,levelIconFarmEvening;
	public ITextureRegion unycleImage;
	public ITextureRegion buyBtn;
	public ITextureRegion pauseBtnTexture,jumpBtnTextute,backBtnTexture,deleteSmallBtnTexture;

	//SPLASH
	public ITextureRegion splashMenuBackground;
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
		font = FontFactory.createFromAsset(activity.getFontManager(), fontTexture, activity.getAssets(), "Dion.otf", 28, true,
				Color.BLACK);
		font.load();

		this.levelFailedAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024,BitmapTextureFormat.RGBA_8888,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		this.levelNoMoneyAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024,BitmapTextureFormat.RGBA_8888,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.levelPassedAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024,BitmapTextureFormat.RGBA_8888,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		this.pauseAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024,BitmapTextureFormat.RGBA_4444,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		this.gameTexturesAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024,BitmapTextureFormat.RGBA_4444,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		this.playerTexturesAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024,BitmapTextureFormat.RGBA_4444,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		this.tomatoScorerAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024,BitmapTextureFormat.RGBA_4444,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);


		this.playerTexture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(playerTexturesAtlas, activity.getAssets(),
				"player.png", 6, 1);
		this.playerRidingUnicycleTexture=BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(playerTexturesAtlas, activity.getAssets(),
				"riding_unicycle.png", 6, 1);
		this.playerRidingBicycleTexture=BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(playerTexturesAtlas, activity.getAssets(),
				"player_riding.png", 6, 2);
		

		this.bullTexture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTexturesAtlas, activity.getAssets(), "bull.png",
				2, 1);
		
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

			this.goodSound = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "sfx/good_bell.wav");
			this.jumpSound = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "sfx/jump.wav");
			this.badSound = SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "sfx/bad.wav");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void loadMenuResources() {
		loadMenuSounds();
		loadMenuGraphics();

	}
	
	public void loadSplashResources(){
		loadSplashGraphics();
	}
	
	private void loadSplashGraphics() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.splashTexturesAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024,BitmapTextureFormat.RGBA_4444,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		this.splashMenuBackground=BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTexturesAtlas , activity.getAssets(),
				"splash_screen.png");
		
		try {
			this.splashTexturesAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
		} catch (TextureAtlasBuilderException e) {
			
			e.printStackTrace();
		}
		this.splashTexturesAtlas.load();
		
		
	}

	private void loadMenuGraphics() {
		// Indicate the folder of the assetsf
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		// create font
		FontFactory.setAssetBasePath("fonts/");
		final ITexture fontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		font = FontFactory.createFromAsset(activity.getFontManager(), fontTexture, activity.getAssets(), "Dion.otf", 29, true,
				Color.BLACK);
		font.load();

		this.mainMenuTexturesAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024,BitmapTextureFormat.RGBA_4444,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		this.subMenuShopTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024,BitmapTextureFormat.RGBA_4444,
				TextureOptions.NEAREST_PREMULTIPLYALPHA);
		
		this.subMenuSessionTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024,BitmapTextureFormat.RGBA_4444,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.subMenuSelectVehicleTextureAtlas=new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024,BitmapTextureFormat.RGBA_4444,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);


		// add the background to the canvas
		this.mainMenuBackground = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mainMenuTexturesAtlas, activity.getAssets(),
				"menubackground3.png");
		this.shopMenuBackGround = BitmapTextureAtlasTextureRegionFactory.createFromAsset(subMenuShopTextureAtlas, activity.getAssets(),
				"shopPanel.png");
		this.unicycleShopItem=BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(subMenuShopTextureAtlas, activity.getAssets(),
				"unicycleShopItem.png", 2, 1);
		this.bicycleShopItem=BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(subMenuShopTextureAtlas, activity.getAssets(),
				"bicycleShopIconSheet.png", 2, 2);
//		this.scooterShopItem=BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(subMenuShopTextureAtlas, activity.getAssets(),
//				"ScooterShopItem.png", 2, 1);
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
		
		
		this.deleteSmallBtnTexture=BitmapTextureAtlasTextureRegionFactory.createFromAsset(subMenuSessionTextureAtlas, activity.getAssets(),
				"deletebtn.png");
		this.levelIcon=BitmapTextureAtlasTextureRegionFactory.createFromAsset(subMenuSessionTextureAtlas, activity.getAssets(),
				"level_noimage.png");
		this.levelIconForest=BitmapTextureAtlasTextureRegionFactory.createFromAsset(subMenuSessionTextureAtlas, activity.getAssets(),
				"levelIcon_forest.png");
		
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
	public void unloadMenuGraphics(){
		this.mainMenuTexturesAtlas.unload();
		this.subMenuShopTextureAtlas.unload();
		this.subMenuSessionTextureAtlas.unload();
		this.subMenuSelectVehicleTextureAtlas.unload();
		setMenuResourcesToNull();
		
	}
	
	public void unloadGameGraphics(){
		this.gameTexturesAtlas.unload();
		this.playerTexturesAtlas.unload();
		this.tomatoScorerAtlas.unload();
		this.levelFailedAtlas.unload();
		this.levelPassedAtlas.unload();
		this.levelNoMoneyAtlas.unload();
		setGameResourcesToNull();
		
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
	
	public void unloadSplashResource(){
		this.splashTexturesAtlas.unload();
		this.splashMenuBackground=null;
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
	
	private void setMenuResourcesToNull(){
		this.mainMenuTexturesAtlas = null;
		this.subMenuShopTextureAtlas = null;
		this.subMenuSessionTextureAtlas = null;
		this.subMenuSelectVehicleTextureAtlas=null;
		this.mainMenuBackground = null;
		this.shopMenuBackGround =null;
		this.unicycleShopItem=null;
		this.bicycleShopItem=null;
		this.hardleyShopItem=null;
		this.unycleImage = null;
		this.buyBtn=null;
		this.sessionMenuBackground=null;
		this.selectVehiculeMenuBackground=null;
		this.lockIcon = null;
		this.sessionMenuItem=null;
		this.deleteSmallBtnTexture=null;
		this.levelIcon=null;
		this.levelIconForest=null;
		this.levelIconFarmEvening=null;
		this.iconUnicycle=null;
		this.vehicleNoImage=null;
		this.unicycleSessionMenuItem=null;
		this.bicycleSessionMenuItem=null;
		this.scooterSessionMenuItem=null;
		this.harleySessionMenuItem=null;
		this.playMenuButton = null;
		this.backBtnTexture = null;
		this.continueMenuButton = null;
		this.shopMenuButton = null;
	}

	private void setGameResourcesToNull(){
		
		this.playerTexture = null;
		this.playerRidingUnicycleTexture=null;
		this.playerRidingBicycleTexture=null;
		this.bullTexture = null;
		this.tomatoScorer = null;
		this.pauseBtnTexture = null;
		this.jumpBtnTextute = null;
		this.tomatoIconTexture = null;
		this.levelIcon = null;
		this.point5Texture = null;
		this.point10Texture = null;
		this.minusPoint5Texture = null;
		this.minusPoint10Texture = null;
		this.minusPoint20Texture = null;
		this.failedBG = null;
		this.passedBG = null;
		this.pauseBG = null;
		this.noMoneyBG = null;
		this.restartButton = null;
		this.quitButton = null;
		this.playButton = null;
		this.pauseButton = null;

		
	}
	
	
	

}
