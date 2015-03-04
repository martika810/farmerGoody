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
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import android.graphics.Color;
import android.util.Log;

public class ResourceManager {

	public Engine engine;
	public MainGameActivity activity;
	public BoundCamera camera;
	public VertexBufferObjectManager vbom;
	public Font font;
	
	//load sounds
	public Music themeMusic;
	public Sound eatTomato;
	public Sound jumpSound;
	
	//TEXTURES
	//this class is like a canvas to paint over
	private BuildableBitmapTextureAtlas mainMenuTexturesAtlas;
	
	private BuildableBitmapTextureAtlas gameTexturesAtlas;
	
	private BuildableBitmapTextureAtlas levelFailedAtlas,levelPassedAtlas,pauseAtlas;
	
	public ITextureRegion playMenuButton,continueMenuButton,mainMenuBackground;
	//public ITextureRegion restartButton,quitButton,failedBG,passedBG;
	public ITextureRegion restartButton,quitButton,playButton,pauseButton,failedBG,passedBG,pauseBG;
	
	//Create coin texture
//	public ITextureRegion coinTexture; 
	
	public ITiledTextureRegion playerTexture;//this texture is gonna be tiled
	public ITextureRegion stairTexture;
	public ITiledTextureRegion tomatoTexture;
	public ITiledTextureRegion bullTexture;
	
	public ITextureRegion correctIconTexture,wrongIconTexture;
	
	public ITextureRegion upArrowTexture;
	public ITextureRegion rightArrowTexture;
	public ITextureRegion leftArrowTexture;
	public ITextureRegion pauseBtnTexture;
	
	public ITextureRegion tomatoIconTexture;
	public ITextureRegion bestScoreIcon;
	public ITextureRegion point5Texture;
	public ITextureRegion point10Texture;
	

	public ITextureRegion minusPoint5Texture;
	public ITextureRegion minusPoint10Texture;
	public ITextureRegion minusPoint20Texture;
	
	public ITextureRegion title;
	

	// Singleton pattern so all scenes use the same Resouce Manager
	private static final ResourceManager INSTANCE = new ResourceManager();
	
	public void loadGameResources(){
		loadGameSounds();
		loadGameGraphics();
		
		
	}
	
	private void loadGameGraphics(){
		
		//create font
		FontFactory.setAssetBasePath("fonts/");
		final ITexture fontTexture=new BitmapTextureAtlas(activity.getTextureManager(), 256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		font=FontFactory.createFromAsset(activity.getFontManager(),fontTexture, activity.getAssets(),"BebasNeue.otf",26,true,Color.BLACK);
		font.load();
		
		
		this.levelFailedAtlas=new BuildableBitmapTextureAtlas(activity.getTextureManager(),1024,1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		this.levelPassedAtlas=new BuildableBitmapTextureAtlas(activity.getTextureManager(),1024,1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		this.pauseAtlas=new BuildableBitmapTextureAtlas(activity.getTextureManager(),1024,1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		this.gameTexturesAtlas=new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
	//	this.coinTexture=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(), "coin.png");
		this.playerTexture=BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTexturesAtlas, activity.getAssets(), "player1.png", 6, 2);
		this.bullTexture=BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTexturesAtlas, activity.getAssets(), "bull.png", 4, 1);
		this.tomatoTexture=BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTexturesAtlas, activity.getAssets(), "tomato.png", 5, 1);
		
		this.correctIconTexture=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(), "correct.png");
		this.wrongIconTexture=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(), "error.png");
		
		this.upArrowTexture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(), "upbtn.png");
		this.rightArrowTexture=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(), "rightbtn.png");
		this.leftArrowTexture=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(), "leftbtn.png");
		this.pauseBtnTexture=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(), "pausebtn.png");
		
		this.stairTexture=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(), "ladder.png");
		
		String levelType=LevelType.getLevelType(UserState.getInstance().getCurrentLevel()).getTypeLevel();
		this.tomatoIconTexture=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(), "tomato_icon.png");
		this.bestScoreIcon=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(), "star.png");
		
		this.point5Texture=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(), ImageProvider.getPointImage(Constants.POINT5, levelType));

		this.point10Texture=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(), ImageProvider.getPointImage(Constants.POINT10, levelType));
		
	
		minusPoint5Texture=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(), ImageProvider.getPointImage(Constants.MINUSPOINT5, levelType));
	
		minusPoint10Texture=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(), ImageProvider.getPointImage(Constants.MINUSPOINT10, levelType));
		minusPoint20Texture=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(), ImageProvider.getPointImage(Constants.MINUSPOINT20, levelType));
	//	this.passedBG=BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.levelPassedAtlas,activity.getAssets(),"level_cleared_background.png");
		
		this.failedBG=BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.levelFailedAtlas, activity.getAssets(), "level_failed.png");
		this.passedBG=BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.levelPassedAtlas, activity.getAssets(), "level_passed.png");
		this.pauseBG=BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.levelPassedAtlas, activity.getAssets(), "pause_screen.png");
		this.restartButton=BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.levelPassedAtlas, activity.getAssets(), "restartbtn.png");
		
		this.quitButton=BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.levelPassedAtlas, activity.getAssets(), "homebtn.png");
		this.playButton=BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.levelPassedAtlas, activity.getAssets(), "playbtn.png");
		this.pauseButton=BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.levelPassedAtlas, activity.getAssets(), "pausebtn.png");
		
		this.title=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(), "title.png");
		
		try{
			this.gameTexturesAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource,BitmapTextureAtlas>(0, 0,1));
			this.gameTexturesAtlas.load();
			
			this.levelFailedAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource,BitmapTextureAtlas>(0,0,1));
			this.levelFailedAtlas.load();
			
			this.levelPassedAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource,BitmapTextureAtlas>(0,0,1));
			this.levelPassedAtlas.load();
			
		}catch(TextureAtlasBuilderException e){
			Debug.e(e);
		}
	}
	
	private void loadGameSounds(){
		try {
			
			this.eatTomato=SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "sfx/nom.wav");
			this.jumpSound=SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "sfx/jump.wav");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadMenuResources(){
		loadMenuSounds();
		loadMenuGraphics();
		
	}
	
	private void loadMenuGraphics(){
		//Indicate the folder of the assetsf
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		
		this.mainMenuTexturesAtlas=new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		//add the background to the canvas
		this.mainMenuBackground =BitmapTextureAtlasTextureRegionFactory.createFromAsset(mainMenuTexturesAtlas, activity.getAssets(), "menubackground.png");
		
		
		//add the play button
		this.playMenuButton=BitmapTextureAtlasTextureRegionFactory.createFromAsset(mainMenuTexturesAtlas,activity.getAssets(),"playbtn.png");
		this.continueMenuButton=BitmapTextureAtlasTextureRegionFactory.createFromAsset(mainMenuTexturesAtlas,activity.getAssets(),"restartbtn.png");
		
		try{
			this.mainMenuTexturesAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource,BitmapTextureAtlas>(0, 0,1));
			this.mainMenuTexturesAtlas.load();
			
		}catch(TextureAtlasBuilderException e){
			Debug.e(e);
		}
	}
	
	private void loadMenuSounds(){
//		try {
//			themeMusic=MusicFactory.createMusicFromAsset(activity.getMusicManager(), activity, "sfx/theme_music.ogg");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}

	// method to prepare out manager
	public static void prepareManager(Engine engine, MainGameActivity activity,
			BoundCamera camera, VertexBufferObjectManager vbom) {
		getInstance().activity = activity;
		getInstance().engine = engine;
		getInstance().camera = camera;
		getInstance().vbom = vbom;

	}

	public static ResourceManager getInstance() {
		return INSTANCE;
	}

}
