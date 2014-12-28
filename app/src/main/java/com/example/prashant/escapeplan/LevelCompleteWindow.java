package com.example.prashant.escapeplan;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by prashant on 28/12/14.
 */
public class LevelCompleteWindow extends Sprite {
    public LevelCompleteWindow(VertexBufferObjectManager pSpriteVertexBufferObject)
    {
        super(0, 0, 650, 400, ResourcesManager.getInstance().complete_window_region, pSpriteVertexBufferObject);

    }

    public void display(Scene scene, Camera camera)
    {
        // Hide HUD
        camera.getHUD().setVisible(false);

        // Disable camera chase entity
        camera.setChaseEntity(null);

        // Attach our level complete panel in the middle of camera
        setPosition(camera.getCenterX(), camera.getCenterY());
        scene.attachChild(this);
    }
}
