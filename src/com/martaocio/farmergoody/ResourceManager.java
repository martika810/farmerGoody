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
	public Sound coinCollect;
	public Sound jumpSound;
	
	//TEXTURES
	//this class is like a canvas to paint over
	private BuildableBitmapTextureAtlas mainMenuTexturesAtlas;
	
	private BuildableBitmapTextureAtlas gameTexturesAtlas;
	
	private BuildableBitmapTextureAtlas levelFailedAtlas;//,levelPassedAtlas;
	
	public ITextureRegion playButton,mainMenuBackground;
	//public ITextureRegion restartButton,quitButton,failedBG,passedBG;
	public ITextureRegion failedBG;
	
	//Create coin texture
//	public ITextureRegion coinTexture; 
	
	public ITiledTextureRegion playerTexture;//this texture is gonna be tiled
	public ITiledTextureRegion tomatoTexture;
	public ITiledTextureRegion bullTexture;
	
	public ITextureRegion tomato1Texture;
	public ITextureRegion tomato2Texture;
	public ITextureRegion tomato3Texture;
	public ITextureRegion tomato4Texture;
	public ITextureRegion tomato5Texture;
	public ITextureRegion tomato6Texture;
	public ITextureRegion tomato8Texture;
	public ITextureRegion tomato9Texture;
	public ITextureRegion tomato10Texture;
	

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
		font=FontFactory.createFromAsset(activity.getFontManager(),fontTexture, activity.getAssets(),"BebasNeue.otf",30,true,Color.BLACK);
		font.load();
		
		
		this.levelFailedAtlas=new BuildableBitmapTextureAtlas(activity.getTextureManager(),1024,1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
	//	this.levelPassedAtlas=new BuildableBitmapTextureAtlas(activity.getTextureManager(),1024,1024,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
		this.gameTexturesAtlas=new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		
	//	this.coinTexture=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(), "coin.png");
		this.playerTexture=BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTexturesAtlas, activity.getAssets(), "player_farmer.png", 6, 1);
		this.bullTexture=BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTexturesAtlas, activity.getAssets(), "bull.png", 4, 1);
		this.tomatoTexture=BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(gameTexturesAtlas, activity.getAssets(), "tomato.png", 5, 1);
		
		this.tomato1Texture=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(), "tomato_point1.png");
		this.tomato2Texture=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(), "tomato_point2.png");
		this.tomato3Texture=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(), "tomato_point3.png");
		this.tomato4Texture=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(), "tomato_point4.png");
		this.tomato5Texture=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(), "tomato_point5.png");
		this.tomato6Texture=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(), "tomato_point6.png");
		this.tomato8Texture=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(), "tomato_point8.png");
		this.tomato9Texture=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(), "tomato_point9.png");
		this.tomato10Texture=BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTexturesAtlas, activity.getAssets(), "tomato_point10.png");
	//	this.passedBG=BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.levelPassedAtlas,activity.getAssets(),"level_cleared_background.png");
		
		this.failedBG=BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.levelFailedAtlas, activity.getAssets(), "level_failed.png");
		
	//	this.restartButton=BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.levelPassedAtlas, activity.getAssets(), "restart.png");
		
	//	this.quitButton=BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.levelPassedAtlas, activity.getAssets(), "quit.png");
		
		try{
			this.gameTexturesAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource,BitmapTextureAtlas>(0, 0,1));
			this.gameTexturesAtlas.load();
			
			this.levelFailedAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource,BitmapTextureAtlas>(0,0,1));
			this.levelFailedAtlas.load();
//			
//			this.levelPassedAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource,BitmapTextureAtlas>(0,0,1));
//			this.levelPassedAtlas.load();
			
		}catch(TextureAtlasBuilderException e){
			Debug.e(e);
		}
	}
	
	private void loadGameSounds(){
	//	try {
			
//			this.coinCollect=SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "sfx/coin_pickup.ogg");
//			this.jumpSound=SoundFactory.createSoundFromAsset(activity.getSoundManager(), activity, "sfx/jumping.ogg");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
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
		this.playButton=BitmapTextureAtlasTextureRegionFactory.createFromAsset(mainMenuTexturesAtlas,activity.getAssets(),"play_btn.png");
		
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
