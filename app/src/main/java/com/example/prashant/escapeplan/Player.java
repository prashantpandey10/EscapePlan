package com.example.prashant.escapeplan;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.Random;

/**
 * Created by prashant on 28/12/14.
 */
public abstract class Player extends AnimatedSprite {
    private Body body;
    private boolean canRun = false;
    private int footContacts = 0;

    private float ybound;

    Random r=new Random();
    public Player(float pX, float pY, VertexBufferObjectManager vbo, Camera camera, PhysicsWorld physicsWorld)
    {
        super(pX, pY, ResourcesManager.getInstance().player_region, vbo);
        createPhysics(camera, physicsWorld);
        camera.setChaseEntity(this);
    }
    public abstract void onDie();

    private void createPhysics(final Camera camera, PhysicsWorld physicsWorld)
    {
        body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyDef.BodyType.DynamicBody, PhysicsFactory.createFixtureDef(1, 0, 0));

        body.setUserData("player");
        body.setFixedRotation(true);

        ybound=camera.getHeight();
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false)
        {
            @Override
            public void onUpdate(float pSecondsElapsed)
            {
                super.onUpdate(pSecondsElapsed);
                camera.onUpdate(0.1f);

                if (getY() <= 0)
                {
                    onDie();
                }

                if (canRun)
                {
                    body.setLinearVelocity(new Vector2(5, body.getLinearVelocity().y));
                }
            }
        });
    }
    public void setRunning()
    {
        canRun = true;

        final long[] PLAYER_ANIMATE = new long[] { 100, 100, 100 };

        animate(PLAYER_ANIMATE, 0, 2, true);
    }
    public void jump()
    {
        if (footContacts < 1)
        {
            return;
        }
        float temp=GameScene.time1-GameScene.time2;
        float val=0;
        if(temp<500) {
            Log.d("HERe", "val1 is" + temp);
            Log.d("HERe", "val1");
            body.setLinearVelocity(new Vector2(body.getLinearVelocity().x,7));
        }
        else if(temp<1000 && temp>500) {
          Log.d("HERe", "val2 is" + temp);
            Log.d("HERe", "val2");
          body.setLinearVelocity(new Vector2(body.getLinearVelocity().x,9));
        }
        else if(temp>1000 && temp<1500) {
            Log.d("HERe", "val3 is" + temp);
            Log.d("HERe", "val3");
          body.setLinearVelocity(new Vector2(body.getLinearVelocity().x,11));
        }
        else {
            Log.d("HERe", "val4 is" + temp);
            Log.d("HERe", "val4");
             body.setLinearVelocity(new Vector2(body.getLinearVelocity().x,9));
        }
}

    public void increaseFootContacts()
    {
        footContacts++;
    }

    public void decreaseFootContacts()
    {
        footContacts--;
    }


}
