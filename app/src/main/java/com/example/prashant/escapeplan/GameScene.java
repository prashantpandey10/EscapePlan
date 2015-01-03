package com.example.prashant.escapeplan;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.SAXUtils;

import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;
import org.andengine.util.level.EntityLoader;
import org.andengine.util.level.constants.LevelConstants;
import org.andengine.util.level.simple.SimpleLevelEntityLoaderData;
import org.andengine.util.level.simple.SimpleLevelLoader;
import org.xml.sax.Attributes;

import java.io.IOException;

/**
 * Created by prashant on 25/12/14.
 */
public class GameScene extends BaseScene implements IOnSceneTouchListener {

    private static final String TAG_ENTITY = "entity";
    private static final String TAG_ENTITY_ATTRIBUTE_X = "x";
    private static final String TAG_ENTITY_ATTRIBUTE_Y = "y";
    private static final String TAG_ENTITY_ATTRIBUTE_TYPE = "type";

    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1 = "platform1";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM4 = "platform4";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM5 = "platform5";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM6 = "platform6";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM7 = "platform7";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2 = "platform2";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3 = "platform3";

    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN = "coin";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LEVEL_COMPLETE = "levelComplete";
    private static final Object TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER = "player";

    private Player player;
    private LevelCompleteWindow levelCompleteWindow;
    private HUD gameHUD;

    private int score = 0;
    private Text scoreText;
    private PhysicsWorld physicsWorld;
    private boolean firstTouch = false;
    private Text gameOverText;
    private boolean gameOverDisplayed = false;



    private void createPhysics()
    {
        physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, -17), false);
        physicsWorld.setContactListener(contactListener());
        registerUpdateHandler(physicsWorld);
    }

    private void addToScore(int i)
    {
        score += i;
        scoreText.setText("Score: " + score);
    }

    private void createHUD()
    {
        gameHUD = new HUD();

        scoreText = new Text(150, 420, resourcesManager.font, "Score: 0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);


        //scoreText.setAnchorCenter(0, 0);
        scoreText.setText("Score: 0");
        gameHUD.attachChild(scoreText);
        camera.setHUD(gameHUD);
    }

    @Override
    public void createScene() {
        setBackground(new Background(Color.BLACK));
        createHUD();
        setOnSceneTouchListener(this);
        createPhysics();
        createGameOverText();
        loadLevel(1);
//        levelCompleteWindow = new LevelCompleteWindow(vbom);

    }

    @Override
    public void onBackKeyPressed() {
        SceneManager.getInstance().loadMenuScene(engine);
    }

    @Override
    public SceneManager.SceneType getSceneType() {
        return SceneManager.SceneType.SCENE_GAME;
    }

    @Override
    public void disposeScene() {
        camera.setHUD(null);
        camera.setCenter(400, 240);
        camera.setChaseEntity(null);
        // TODO code responsible for disposing scene
        // removing all game scene objects.
    }

    private void loadLevel(int levelID)
    {
        final SimpleLevelLoader levelLoader = new SimpleLevelLoader(vbom);

        final FixtureDef FIXTURE_DEF = PhysicsFactory.createFixtureDef(0, 0.01f, 0.5f);

        levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(LevelConstants.TAG_LEVEL)
        {
            public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException
            {
                //final int width = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_WIDTH);
                //final int height = SAXUtils.getIntAttributeOrThrow(pAttributes, LevelConstants.TAG_LEVEL_ATTRIBUTE_HEIGHT);
                // TODO later we will specify camera BOUNDS and create invisible walls
                // on the beginning and on the end of the level.


                camera.setBounds(0, 0, 30000, 1080); // here we set camera bounds
                camera.setBoundsEnabled(true);

                return GameScene.this;
            }
        });

        levelLoader.registerEntityLoader(new EntityLoader<SimpleLevelEntityLoaderData>(TAG_ENTITY)
        {
            public IEntity onLoadEntity(final String pEntityName, final IEntity pParent, final Attributes pAttributes, final SimpleLevelEntityLoaderData pSimpleLevelEntityLoaderData) throws IOException
            {
                final int x = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_X);
                final int y = SAXUtils.getIntAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_Y);
                final String type = SAXUtils.getAttributeOrThrow(pAttributes, TAG_ENTITY_ATTRIBUTE_TYPE);

                final Sprite levelObject;

                if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM1))
                {
                    levelObject = new Sprite(x, y, resourcesManager.platform1_region, vbom);
                    PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyDef.BodyType.StaticBody, FIXTURE_DEF).setUserData("platform1");
                }
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM2))
                {
                    levelObject = new Sprite(x, y, resourcesManager.platform2_region, vbom);
                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyDef.BodyType.StaticBody, FIXTURE_DEF);
                    body.setUserData("platform");
                    levelObject.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier( // Forward
                            new MoveYModifier(1.5f, levelObject.getY(),
                                    levelObject.getY() - 80),
                            // Backward
                            new MoveYModifier(1.5f, levelObject.getY() - 80,
                                    levelObject.getY()))));
                    body.setUserData("platform2");
                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
                }
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM3))
                {
                    levelObject = new Sprite(x, y, resourcesManager.platform3_region, vbom);
                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyDef.BodyType.StaticBody, FIXTURE_DEF);

                    levelObject.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier( // Forward
                            new MoveYModifier(1.5f, levelObject.getY(),
                                    levelObject.getY() + 80),
                            // Backward
                            new MoveYModifier(1.5f, levelObject.getY()+80,
                                    levelObject.getY()))));

                    body.setUserData("platform");
                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
                }




                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM4))
                {
                    levelObject = new Sprite(x, y, resourcesManager.platform2_region, vbom);
                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyDef.BodyType.StaticBody, FIXTURE_DEF);
                    body.setUserData("platform");
                    levelObject.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier( // Forward
                            new MoveYModifier(1.0f, levelObject.getY(),
                                    levelObject.getY() - 80),
                            // Backward
                            new MoveYModifier(1.0f, levelObject.getY() - 80,
                                    levelObject.getY()))));
                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
                }
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM5))
                {
                    levelObject = new Sprite(x, y, resourcesManager.platform3_region, vbom);
                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyDef.BodyType.StaticBody, FIXTURE_DEF);
                    levelObject.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier( // Forward
                            new MoveYModifier(1.0f, levelObject.getY(),
                                    levelObject.getY() + 80),
                            // Backward
                            new MoveYModifier(1.0f, levelObject.getY()+80,
                                    levelObject.getY()))));

                    body.setUserData("platform");
                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
                }


                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM6))
                {
                    levelObject = new Sprite(x, y, resourcesManager.platform2_region, vbom);
                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyDef.BodyType.StaticBody, FIXTURE_DEF);
                    body.setUserData("platform");
                    levelObject.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier( // Forward
                            new MoveYModifier(.8f, levelObject.getY(),
                                    levelObject.getY() - 80),
                            // Backward
                            new MoveYModifier(.8f, levelObject.getY() - 80,
                                    levelObject.getY()))));
                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
                }

                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLATFORM7))
                {
                    levelObject = new Sprite(x, y, resourcesManager.platform3_region, vbom);
                    final Body body = PhysicsFactory.createBoxBody(physicsWorld, levelObject, BodyDef.BodyType.StaticBody, FIXTURE_DEF);
                    levelObject.registerEntityModifier(new LoopEntityModifier(new SequenceEntityModifier( // Forward
                            new MoveYModifier(0.8f, levelObject.getY(),
                                    levelObject.getY() +80),
                            // Backward
                            new MoveYModifier(.8f, levelObject.getY()+80,
                                    levelObject.getY()))));

                    body.setUserData("platform");
                    physicsWorld.registerPhysicsConnector(new PhysicsConnector(levelObject, body, true, false));
                }

                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_COIN))
                {
                    levelObject = new Sprite(x, y, resourcesManager.coin_region, vbom)
                    {
                        @Override
                        protected void onManagedUpdate(float pSecondsElapsed)
                        {
                            super.onManagedUpdate(pSecondsElapsed);

                            /**
                             * TODO
                             * we will later check if player collide with this (coin)
                             * and if it does, we will increase score and hide coin
                             * it will be completed in next articles (after creating player code)
                             */
                            if (player.collidesWith(this))
                            {
                                addToScore(10);
                                this.setVisible(false);
                                this.setIgnoreUpdate(true);
                            }
                        }
                    };
                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
                }
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_PLAYER))
                {
                    player = new Player(x, y, vbom, camera, physicsWorld)
                    {
                        @Override
                        public void onDie()
                        {
                            if (!gameOverDisplayed)
                            {
                                displayGameOverText();
                            }
                        }
                    };
                    levelObject = player;
                }
                else if (type.equals(TAG_ENTITY_ATTRIBUTE_TYPE_VALUE_LEVEL_COMPLETE))
                {
                    levelObject = new Sprite(x, y, resourcesManager.complete_window_region, vbom)
                    {
                        @Override
                        protected void onManagedUpdate(float pSecondsElapsed)
                        {
                            super.onManagedUpdate(pSecondsElapsed);

                            if (player.collidesWith(this))
                            {

                                this.setVisible(false);
                                this.setIgnoreUpdate(true);
                            }
                        }
                    };
                    levelObject.registerEntityModifier(new LoopEntityModifier(new ScaleModifier(1, 1, 1.3f)));
                }
                else
                {
                    throw new IllegalArgumentException();
                }

                levelObject.setCullingEnabled(true);

                return levelObject;
            }
        });

        levelLoader.loadLevelFromAsset(activity.getAssets(), "level/" + levelID + ".lvl");
    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        if (pSceneTouchEvent.isActionDown())
        {
            if (!firstTouch)
            {
                player.setRunning();
                firstTouch = true;
            }
            else
            {

                if(player.getY()<camera.getHeight())
                player.jump();
            }
        }
        return false;
    }
    private void createGameOverText()
    {
        gameOverText = new Text(0, 0, resourcesManager.font, "Game Over!", vbom);
    }

    private void displayGameOverText()
    {
        camera.setChaseEntity(null);
        gameOverText.setPosition(camera.getCenterX(), camera.getCenterY());
        attachChild(gameOverText);
        gameOverDisplayed = true;
    }

    private ContactListener contactListener()
    {
        ContactListener contactListener = new ContactListener()
        {
            public void beginContact(Contact contact)
            {
                final Fixture x1 = contact.getFixtureA();
                final Fixture x2 = contact.getFixtureB();

                if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null)
                {
                    if (x2.getBody().getUserData().equals("player"))
                    {
                        //if(x2.getBody().getPosition().y*PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT<780)
                        player.increaseFootContacts();
                    }
                }
                if (x1.getBody().getUserData().equals("platform") && x2.getBody().getUserData().equals("player"))
                {
                    //x1.getBody().setType(BodyDef.BodyType.DynamicBody);
                    camera.setChaseEntity(null);
                    player.setVisible(false);

                    player.onDie();

                }
                /*if (x1.getBody().getUserData().equals("platform2") && x2.getBody().getUserData().equals("player"))
                {
                    camera.setChaseEntity(null);
                    player.onDie();


                        player.increaseFootContacts();
                    }
                }
                if (x1.getBody().getUserData().equals("platform3") && x2.getBody().getUserData().equals("player"))
                {
                    x1.getBody().setType(BodyDef.BodyType.DynamicBody);
                }
                if (x1.getBody().getUserData().equals("platform2") && x2.getBody().getUserData().equals("player"))
                {

                    engine.registerUpdateHandler(new TimerHandler(0.2f, new ITimerCallback()
                    {
                        public void onTimePassed(final TimerHandler pTimerHandler)
                        {
                            pTimerHandler.reset();
                            engine.unregisterUpdateHandler(pTimerHandler);
                            x1.getBody().setType(BodyDef.BodyType.DynamicBody);
                        }
                    }));

                }*/
                }


            public void endContact(Contact contact)
            {
                final Fixture x1 = contact.getFixtureA();
                final Fixture x2 = contact.getFixtureB();

                if (x1.getBody().getUserData() != null && x2.getBody().getUserData() != null)
                {
                    if (x2.getBody().getUserData().equals("player"))
                    {
                     //   player.decreaseFootContacts();

                    }
                }
            }

            public void preSolve(Contact contact, Manifold oldManifold)
            {

            }

            public void postSolve(Contact contact, ContactImpulse impulse)
            {

            }
        };
        return contactListener;
    }
}
