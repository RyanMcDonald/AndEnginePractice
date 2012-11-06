package com.mytestproject;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.opengl.GLES20;

public class SceneManager {

	private SceneType currentScene;
	private BaseGameActivity activity;
	private Engine engine;
	private Camera camera;

	private Scene splashScene;
	private BitmapTextureAtlas splashTextureAtlas;
	private ITextureRegion splashTextureRegion;
	private Sprite splash;
	
	private Scene titleScene;
	
	private Scene mainGameScene;
	
	private AnimatedSprite player;
	private PlayerDirection currentPlayerDirection;
	private static final int ANIMATE_DURATION = 20;
	
	private BitmapTextureAtlas analogControlBitmapTextureAtlas;
	
	private BitmapTextureAtlas analogControlTexture;
	private ITextureRegion analogControlBaseTextureRegion;
	private ITextureRegion analogControlKnobTextureRegion;
	private AnalogOnScreenControl analogOnScreenControl;
	
	private PhysicsHandler physicsHandler;
	
	private BitmapTextureAtlas maleRogueTextureAtlas;
	private TiledTextureRegion maleRogueTextureRegion;
	private AnimatedSprite maleRogueSprite;
	
	public enum SceneType {
		SPLASH,
		TITLE,
		MAINGAME
	}
	
	public enum PlayerDirection {
		NONE,
		UP,
		DOWN,
		LEFT,
		RIGHT,
		RIGHT_UP,
		RIGHT_DOWN,
		LEFT_UP,
		LEFT_DOWN,
	}
	
	public SceneManager(BaseGameActivity activity, Engine engine, Camera camera) {
		this.activity = activity;
		this.engine = engine;
		this.camera = camera;
	}
	
	public void loadSplashSceneResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.DEFAULT);
		splashTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "splash.png", 0, 0);
		splashTextureAtlas.load();
	}
	
	public void loadGameSceneResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		// Analog Control
		analogControlBitmapTextureAtlas = new BitmapTextureAtlas(engine.getTextureManager(), 32, 32, TextureOptions.BILINEAR);
		analogControlBitmapTextureAtlas.load();

		analogControlTexture = new BitmapTextureAtlas(engine.getTextureManager(), 256, 128, TextureOptions.BILINEAR);
		analogControlBaseTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(analogControlTexture, activity, "onscreen_control_base.png", 0, 0);
		analogControlKnobTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(analogControlTexture, activity, "onscreen_control_knob.png", 128, 0);
		analogControlTexture.load();
		
		// Male Rogue
		maleRogueTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 192, 256, TextureOptions.DEFAULT);
		maleRogueTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(maleRogueTextureAtlas, activity, "male_rogue.png", 0, 0, 6, 8);
		maleRogueTextureAtlas.load();
		
	}
	
	public Scene createSplashScene() {
		splashScene = new Scene();
		splashScene.setBackground(new Background(1, 0, 0));
		splash = new Sprite(0, 0, splashTextureRegion, activity.getVertexBufferObjectManager()) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};
		
		splash.setScale(1.5f);
		splash.setPosition((camera.getWidth() - splash.getWidth()) * 0.5f, (camera.getHeight() - splash.getHeight()) * 0.5f);
		splashScene.attachChild(splash);
		
		return splashScene;
	}
	
	public void createGameScenes() {
		titleScene = new Scene();
		titleScene.setBackground(new Background(0, 0, 1));
		
		mainGameScene = new Scene();
		mainGameScene.setBackground(new Background(0, 1, 0));

		// Female Rogue Sprite
		maleRogueSprite = new AnimatedSprite(0, 0, maleRogueTextureRegion, activity.getVertexBufferObjectManager());
		physicsHandler = new PhysicsHandler(maleRogueSprite);
		maleRogueSprite.registerUpdateHandler(physicsHandler);
		mainGameScene.attachChild(maleRogueSprite);
		
		player = maleRogueSprite;
		createAnalogControl();
		
		
		mainGameScene.setChildScene(analogOnScreenControl);
		
	}
	
	public SceneType getCurrentScene() {
		return currentScene;
	}
	
	public void setCurrentScene(SceneType scene) {
		currentScene = scene;
		
		switch(currentScene) {
		case SPLASH:
			break;
		case TITLE:
			engine.setScene(titleScene);
			break;
		case MAINGAME:
			engine.setScene(mainGameScene);
			break;
		}
	}
	
	private void createAnalogControl() {
		// Analog Control
		analogOnScreenControl = new AnalogOnScreenControl(0, camera.getHeight() - this.analogControlBaseTextureRegion.getHeight(), camera, this.analogControlBaseTextureRegion, this.analogControlKnobTextureRegion, 0.1f, 200, engine.getVertexBufferObjectManager(), new IAnalogOnScreenControlListener() {
			@Override
			public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {
				physicsHandler.setVelocity(pValueX * 100, pValueY * 100);

				PlayerDirection direction = getPlayerDirection(pValueX, pValueY);

				// If the player is walking in the same direction as the last check we just return
				if (direction == currentPlayerDirection) {
					return;
				}

				currentPlayerDirection = direction;
				long[] animationDurations = new long[] {200, 200, 200, 200, 200, 200};
				switch (currentPlayerDirection) {
				case UP:
					player.animate(animationDurations, 18, 23, true);
					break;

				case DOWN:
					player.animate(animationDurations, 0, 5, true);
					break;

				case LEFT:
					player.animate(animationDurations, 6, 11, true);
					break;

				case RIGHT:
					player.animate(animationDurations, 12, 17, true);
					break;

				case RIGHT_UP:
					player.animate(animationDurations, 18, 23, true);
					break;

				case RIGHT_DOWN:
					player.animate(animationDurations, 30, 35, true);
					break;

				case LEFT_UP:
					player.animate(animationDurations, 36, 41, true);
					break;

				case LEFT_DOWN:
					player.animate(animationDurations, 24, 29, true);
					break;

				case NONE:
					player.stopAnimation();
					break;

				default:
					player.stopAnimation();
					break;
				}

			}

			@Override
			public void onControlClick(final AnalogOnScreenControl pAnalogOnScreenControl) {
				
			}
		});
		
		analogOnScreenControl.getControlBase().setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		analogOnScreenControl.getControlBase().setAlpha(0.5f);
		analogOnScreenControl.getControlBase().setScaleCenter(0, 128);
		analogOnScreenControl.getControlBase().setScale(1.25f);
		analogOnScreenControl.getControlKnob().setScale(1.25f);
		analogOnScreenControl.refreshControlKnobPosition();
	}
	
	private PlayerDirection getPlayerDirection(final float pValueX, final float pValueY) {
	    
	    if(pValueX == 0 && pValueY == 0) {
	    	return PlayerDirection.NONE;
	    }
	   
	    double angle = getAngle(pValueX, pValueY);
	   
	    if(isBetween(68, 113, angle)){
	            return PlayerDirection.UP;
	    }
	   
	    if(isBetween(248, 293, angle)){
	            return PlayerDirection.DOWN;
	    }
	   
	    if(isBetween(158, 203, angle)){
	            return PlayerDirection.LEFT;
	    }
	   
	    if(angle < 23 || angle > 338){
	            return PlayerDirection.RIGHT;
	    }
	   
	    if(isBetween(23, 68, angle)){
	            return PlayerDirection.RIGHT_UP;
	    }
	   
	    if(isBetween(293, 338, angle)){
	            return PlayerDirection.RIGHT_DOWN;
	    }
	   
	    if(isBetween(113, 158, angle)){
	            return PlayerDirection.LEFT_UP;
	    }
	   
	    if(isBetween(203, 248, angle)){
	            return PlayerDirection.LEFT_DOWN;
	    }
	   
	    return PlayerDirection.NONE;
	}
	
	// Return true if c is between a and b.
    public boolean isBetween(int a, int b, double c) {
        return b > a ? c > a && c < b : c > b && c < a;
    }
   
	/*
	 * This method accepts x, y cartesian coordinates and converts them into an angle (degree)
	 */
    private double getAngle(float x, float y) {
        double inRads = Math.atan2(y,x);

        // We need to map to coord system when 0 degree is at 3 O'clock, 270 at 12 O'clock
        if (inRads < 0) {
            inRads = Math.abs(inRads);
        } else {
            inRads = 2*Math.PI - inRads;
        }

        return Math.toDegrees(inRads);
    }

}
