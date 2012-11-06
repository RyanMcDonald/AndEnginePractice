package com.mytestproject;

import org.andengine.audio.music.Music;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSCounter;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.ui.activity.BaseGameActivity;

import android.graphics.Typeface;

/**
 *
 * @author Ryan McDonald
 * @since 09:37:08 - 11.03.2012
 */
public class SplashActivity extends BaseGameActivity {

	private FPSCounter fpsCounter;
	
	private Font mFont;
	
	private Text fpsText;
	
	private Camera camera;
	private final static int CAMERA_WIDTH = 720;
	private final static int CAMERA_HEIGHT = 480;
	
	private Music backgroundMusic;
	
	private Scene splashScene;
	private Scene mainScene;
	
	private BitmapTextureAtlas splashTextureAtlas;
	private ITextureRegion splashTextureRegion;
	private Sprite splash;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), camera);
		return engineOptions;
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception {		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		splashTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 256, 256, TextureOptions.DEFAULT);
		splashTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, this, "splash.png", 0, 0);
		splashTextureAtlas.load();
		pOnCreateResourcesCallback.onCreateResourcesFinished();
		
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
		initSplashScene();
		pOnCreateSceneCallback.onCreateSceneFinished(this.splashScene);
		
	}
	
	@Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		mEngine.registerUpdateHandler(new TimerHandler(3f, new ITimerCallback() {
			public void onTimePassed(final TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);
				loadResources();
				loadScenes();
				splash.detachSelf();
				mEngine.setScene(mainScene);
			}
		}));
		
		pOnPopulateSceneCallback.onPopulateSceneFinished();
    }
	
	private void initSplashScene() {
		splashScene = new Scene();
		splash = new Sprite(0, 0, splashTextureRegion, mEngine.getVertexBufferObjectManager()) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};
		
		splash.setScale(1.5f);
		splash.setPosition((CAMERA_WIDTH - splash.getWidth()) * 0.5f, (CAMERA_HEIGHT - splash.getHeight()) * 0.5f);
		splashScene.attachChild(splash);
	}
	
	public void loadResources() {
		fpsCounter = new FPSCounter();
		this.mEngine.registerUpdateHandler(fpsCounter);
		
		this.mFont = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32);
		this.mFont.load();

		fpsText = new Text(0, 0, mFont, "FPS:", "FPS: XXXXX".length(), this.getVertexBufferObjectManager());
	}
	
	public void loadScenes() {
		mainScene = new Scene();
		mainScene.setBackground(new Background(0, 50, 255));
		
		HUD hud = new HUD();
		hud.attachChild(fpsText);
		hud.registerUpdateHandler(new TimerHandler(1 / 20.0f, true, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				fpsText.setText("FPS: " + String.format("%.2f", fpsCounter.getFPS()));
			}
		}));
		
		camera.setHUD(hud);
		
		Entity playerEntity = new Entity();
		playerEntity.setPosition(0, 0);
		camera.setChaseEntity(playerEntity);
	}
	
}
