package com.mytestproject;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import com.mytestproject.SceneManager.SceneType;

public class MainActivity extends BaseGameActivity {

	private Camera camera;
	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;
	
	SceneManager sceneManager;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), camera);
		return engineOptions;
	}
	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception {
		sceneManager = new SceneManager(this, mEngine, camera);
		sceneManager.loadSplashSceneResources();
		
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}
	
	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
		pOnCreateSceneCallback.onCreateSceneFinished(sceneManager.createSplashScene());	
	}
	
	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		mEngine.registerUpdateHandler(new TimerHandler(3f, new ITimerCallback() {
			public void onTimePassed(final TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);
				sceneManager.loadGameSceneResources();
				sceneManager.createGameScenes();
				
				sceneManager.setCurrentScene(SceneType.MAINGAME);
			}
		}));
		
		pOnPopulateSceneCallback.onPopulateSceneFinished();
		
	}
	

}
