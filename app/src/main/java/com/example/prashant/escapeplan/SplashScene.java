package com.example.prashant.escapeplan;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
<<<<<<< HEAD
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
=======
>>>>>>> f6f9f549ce7812406a6877db3a59086a367c4432
import org.andengine.opengl.util.GLState;

/**
 * Created by prashant on 25/12/14.
 */
public class SplashScene extends BaseScene {
    private Sprite splash;

    @Override
    public void createScene() {
<<<<<<< HEAD

=======
>>>>>>> f6f9f549ce7812406a6877db3a59086a367c4432
        splash = new Sprite(0, 0, resourcesManager.splash_region, vbom)
        {
            @Override
            protected void preDraw(GLState pGLState, Camera pCamera)
            {
                super.preDraw(pGLState, pCamera);
                pGLState.enableDither();
            }
        };

        splash.setScale(1.5f);
<<<<<<< HEAD
        splash.setPosition(camera.getCenterX(), camera.getCenterY());
=======
        splash.setPosition(250, 140);
>>>>>>> f6f9f549ce7812406a6877db3a59086a367c4432
        attachChild(splash);
    }

    @Override
    public void onBackKeyPressed() {

    }

    @Override
    public SceneManager.SceneType getSceneType() {
        return SceneManager.SceneType.SCENE_SPLASH;
    }

    @Override
    public void disposeScene() {

        splash.detachSelf();
        splash.dispose();
        this.detachSelf();
        this.dispose();

    }
}
