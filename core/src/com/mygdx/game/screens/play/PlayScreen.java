package com.mygdx.game.screens.play;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.InversePacman;
import com.mygdx.game.components.AnimationComponent;
import com.mygdx.game.components.ButtonComponent;
import com.mygdx.game.components.CollisionComponent;
import com.mygdx.game.components.MusicComponent;
import com.mygdx.game.components.PillComponent;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.StateComponent;
import com.mygdx.game.components.TableComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.components.VelocityComponent;
import com.mygdx.game.managers.EntityManager;
import com.mygdx.game.managers.GameScreenManager;
import com.mygdx.game.managers.NetworkManager;
import com.mygdx.game.multiplayermessage.MultiplayerMessage;
import com.mygdx.game.scenes.Hud;
import com.mygdx.game.screens.AbstractScreen;
import com.mygdx.game.systems.AISystem;
import com.mygdx.game.systems.AnimationSystem;
import com.mygdx.game.systems.ButtonSystem;
import com.mygdx.game.systems.CollisionEventSystem;
import com.mygdx.game.systems.CollisionSystem;
import com.mygdx.game.systems.MovementSystem;
import com.mygdx.game.systems.MusicSystem;
import com.mygdx.game.systems.PillSystem;
import com.mygdx.game.systems.PlayerContactListener;
import com.mygdx.game.systems.PlayerInputSystem;
import com.mygdx.game.systems.RenderingSystem;
import com.mygdx.game.systems.StateSystem;
import com.mygdx.game.worldbuilder.WorldBuilder;

import java.text.DecimalFormat;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.concurrent.Future;


import java.util.concurrent.ThreadLocalRandom;


public final class PlayScreen extends AbstractScreen {

    public static boolean pause;
    //camera for the view
    private OrthographicCamera camera;
    //camera for the buttonsystem
    private OrthographicCamera camera2;


    private Hud hud;


    //private SpriteBatch batch;
    private EntityManager entityManager;

    private Texture pacmansprite;
    private Texture ghostsheet;
    private Texture littlePillSprite;
    private Texture powerPillSprite;

    private TextureRegion pausescreen;
    private TextureRegion pausetexture;
    private TextureRegion back;
    private TextureRegion ellipse;

    private TextureRegion pause_ellipse;
    private TextureRegion front_ellipse;
    private TextureRegion pauseicon;


    private Sprite pauseSprite;
    private Sprite ellipseSprite;


    private Sprite pauseButtonSprite;
    private Sprite pacmanSpritus;
    private Sprite backSprite;

    private Sprite pause_ellipseSprite;
    private Sprite front_ellipseSprite;
    private Sprite pauseiconSprite;

    private Entity pacman;
    private Entity pauseEntity;
    private Entity pauseButton;
    private Entity backButton;
    private Entity ghost;
    private Entity pill;
    private Entity ellipseEntity;

    private Entity pause_ellipseEntity;
    private Entity front_ellipseEntity;
    private Entity pauseiconEntity;



    public static boolean MULTIPLAYER;
    private MultiplayerMessage connection = MultiplayerMessage.getInstance();
    private Entity musicEntity;
    private Entity musicPauseEntity;

    //World building
    public World world;
    public Body player;
    public Box2DDebugRenderer b2dr;

    //Box2d
    public OrthogonalTiledMapRenderer tmr;
    public TiledMap map;

    public boolean destroyAllBodies;
    public boolean pacmanGotHit;

    private Engine engine;

    //public boolean pause = false;
    private boolean resume = false;

    private CollisionEventSystem collisionEventSystem;
    private MovementSystem movementSystem;
    private PlayerInputSystem playerInputSystem;
    private AISystem aiSystem;
    private RenderingSystem renderingSystem;
    private StateSystem stateSystem;
    private AnimationSystem animationSystem;
    private MusicSystem musicSystem;
    private ButtonSystem buttonSystem;
    private PillSystem pillSystem;


    public static float scaleX;
    private float scaleY;

    private static final Integer[] powerPillIndices = {4, 30, 105, 120};


    public PlayScreen(final InversePacman app, Engine engine) {
        super(app, engine);

        this.engine = engine;
        ellipse = new TextureRegion(new Texture("menuscreen/ellipse_color_change_correct.png"));
        pausetexture = new TextureRegion(new Texture("playscreen/pause2x.png"));
        back = new TextureRegion(new Texture("playscreen/back2x.png"));

        pause_ellipse = new TextureRegion(new Texture("playscreen/ellipse.png"));
        front_ellipse = new TextureRegion(new Texture("playscreen/front_ellipse.png"));
        pauseicon = new TextureRegion(new Texture("playscreen/pauseicon.png"));

//        this.engine = engine;
//         Sets the camera; width and height.
        this.camera = new OrthographicCamera();

        this.camera2 = new OrthographicCamera();

        //I/System.out: camera  (415.38464,690.0,0.0)


        scaleX = Gdx.graphics.getWidth() / (float)app.APP_WIDTH_MOBILE;
        scaleY = Gdx.graphics.getHeight() / (float)app.APP_HEIGHT_MOBILE;
        this.camera.setToOrtho(false, Gdx.graphics.getWidth() / (scaleX *1.32f), Gdx.graphics.getHeight() / (scaleX*1.32f));
        this.camera2.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //camera.translate(415.38464f,690.0f,0);

        scaleX = Gdx.graphics.getWidth() / (float)app.APP_WIDTH_MOBILE;
        scaleY = Gdx.graphics.getHeight() / (float)app.APP_HEIGHT_MOBILE;
        this.camera.setToOrtho(false, Gdx.graphics.getWidth() / (scaleX *1.32f), Gdx.graphics.getHeight() / (scaleX*1.32f));


        hud = new Hud(app.batch);
    }

    public void handleInput(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
            //Important to call both if you want to remove the music from the previous screen
            for (EntitySystem system : engine.getSystems()){
                engine.removeSystem(system);
            }
            engine.removeAllEntities();

            destroyAllBodies = true;
            if(MULTIPLAYER){
                connection.leaveLobby();
                LobbyScreen.LOBBY_JOINED = null;
                MULTIPLAYER = false;
            }
            app.gsm.setScreen(GameScreenManager.STATE.MAIN_MENU_SCREEN);

        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            pause = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            pause = false;
        }
        if(pauseButton.flags == 1) {
            pause = true;
            pauseButton.flags = 0;
        }

        if(backButton.flags == 1) {
            for (EntitySystem system : engine.getSystems()){
                engine.removeSystem(system);
            }
            engine.removeAllEntities();
            destroyAllBodies = true;

            if(MULTIPLAYER){
                connection.leaveLobby();
                LobbyScreen.LOBBY_JOINED = null;
                MULTIPLAYER = false;
            }

            app.gsm.setScreen(GameScreenManager.STATE.MAIN_MENU_SCREEN);
        }

        if(pause_ellipseEntity.flags == 1) {
            pause_ellipseEntity.flags = 0;
            pause = false;
        }

    }

    @Override
    public void update(float delta) {
        handleInput();

        // Chooses the next song to play if the song has finished
        // Had to add the second condition since it chose to play a new song as I switched screens

        if (pillSystem.allPillsCollected()) {
            if (PlayScreen.MULTIPLAYER) {
                app.NETWORKMANAGER.updateMultiplayerScore(app.NETWORKMANAGER.getPlayerId(), TableComponent.PLAYERTYPE);
            } else {
                app.NETWORKMANAGER.updateSingleplayerScore(app.NETWORKMANAGER.getPlayerId(), hud.getScore());
            }
            engine.removeAllEntities();

            musicSystem.dispose();
            engine.removeSystem(musicSystem);
            engine.removeSystem(collisionEventSystem);
            engine.removeSystem(animationSystem);
            ghostsheet.dispose();
            destroyAllBodies = true;
            app.gsm.setScreen(GameScreenManager.STATE.GAME_OVER_SCREEN);
        }

        if(hud.remainingLives < 1){
            if (PlayScreen.MULTIPLAYER) {
                app.NETWORKMANAGER.updateMultiplayerScore(app.NETWORKMANAGER.getPlayerId(), TableComponent.PLAYERTYPE);
            }
            engine.removeAllEntities();

            musicSystem.dispose();
            engine.removeSystem(collisionEventSystem);
            ghostsheet.dispose();
            destroyAllBodies = true;
            app.gsm.setScreen(GameScreenManager.STATE.GAME_OVER_SCREEN);
        }

        hud.update(delta);
    }


    @Override
    public void show() {
        //world
        world = new World(new Vector2(0f, 0), false);
        world.setContactListener(new PlayerContactListener());
        b2dr = new Box2DDebugRenderer();

        //Tiled map creation and WorldBuilder call
        map = new TmxMapLoader().load("World/InvPac_Maze2.tmx");
        //map.getLayers().get("BackgroundLayer").setOffsetX(100);
        //map.getLayers().get("Players").setOffsetX(100);
        //map.getLayers().get("Pills").setOffsetX(100);
       // Gdx.graphics.getWidth()/(map.getProperties().get("width",Integer.class)*32f)
        tmr = new OrthogonalTiledMapRenderer(map);
        WorldBuilder.parseTiledObjectLayer(world, map.getLayers().get("Collision").getObjects()
                ,map.getLayers().get("BackgroundLayer")
                ,map.getLayers().get("Players").getObjects()
                ,map.getLayers().get("Pills").getObjects()
                , map.getLayers().get("AiRoute"));
        WorldBuilder.createPlayers(world);
        WorldBuilder.createPills(world);
        //this.camera.setToOrtho(false, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);

        // To add a new songs, place the file under the folder assets/music/play
        //batch = new SpriteBatch();
        playerInputSystem = new PlayerInputSystem();
        aiSystem = new AISystem();
        movementSystem = new MovementSystem();
        collisionEventSystem = new CollisionEventSystem();

        renderingSystem = new RenderingSystem(app.batch);
        buttonSystem = new ButtonSystem(camera2);
        stateSystem = new StateSystem();
        animationSystem = new AnimationSystem();
        musicSystem = new MusicSystem();
        pillSystem = new PillSystem();

        engine.addSystem(playerInputSystem);
        if(!MULTIPLAYER){
            engine.addSystem(aiSystem);
        }
        engine.addSystem(movementSystem);
        engine.addSystem(collisionEventSystem);
        engine.addSystem(renderingSystem);
        engine.addSystem(stateSystem);
        engine.addSystem(animationSystem);
        engine.addSystem(musicSystem);
        engine.addSystem(buttonSystem);
        engine.addSystem(pillSystem);

        // Pill logic
        littlePillSprite = new Texture("white_pill.png");
        powerPillSprite = new Texture("orange_pill.png");

        ellipseSprite = new Sprite(ellipse);
        ellipseEntity = new Entity();
        app.addSpriteEntity(ellipseSprite, ellipseEntity, engine, (Gdx.graphics.getWidth() / 2 - (ellipse.getRegionWidth() / 2 * (scaleX))), (Gdx.graphics.getHeight() - (ellipse.getRegionHeight() * (scaleY))), (ellipse.getRegionWidth() * (scaleX)), (ellipse.getRegionHeight() * (scaleY)), false, true, true, false);

        Vector2 scale = new Vector2();
        for (int i = 0; i < WorldBuilder.getPillList().size(); i++) {
            PillComponent pillComponent = WorldBuilder.getPillList().get(i);
            Vector2 vector = pillComponent.body.getPosition();

            TextureRegion pillTextureRegion;
            // If the current index has been identified as a power pill index,
            // then the pill is a power pill and the size of its texture is increased
            if ((Arrays.asList(powerPillIndices)).contains(i)) {
                pillComponent.setPowerPill(true);
                scale.set(.10f*(scaleX*1.32f), .10f*(scaleX * 1.32f));
                pillTextureRegion = new TextureRegion(powerPillSprite);
            } else {
                pillComponent.setPowerPill(false);
                scale.set(0.07f*(scaleX *1.32f), 0.07f*(scaleX *1.32f));
                pillTextureRegion = new TextureRegion(littlePillSprite);
            }

            pill = new Entity();
            pill.add(WorldBuilder.getPillList().get(i))
                    .add(new TextureComponent(new TextureRegion(pillTextureRegion)))
                    .add(new TransformComponent((scaleX *1.32f)*vector.x / RenderingSystem.PPM,
                            (scaleX *1.32f)*vector.y / RenderingSystem.PPM, scale.x, scale.y, 0f));

            engine.addEntity(pill);
        }

        //splitting up the different frames in the ghost sheet and adding them to an animation
        ghostsheet = new Texture("ghosts.png");
        TextureRegion[][] temp = TextureRegion.split(ghostsheet,ghostsheet.getWidth()/10, ghostsheet.getHeight());
        TextureRegion[] walkFrames = new TextureRegion[10];

        for (int i = 0; i < 10; i++) {
                walkFrames[i] = temp[0][i];
        }

        Animation walkAnimation = new Animation<>(0.1f,walkFrames);
        //adding animation to each direction state and idle
        AnimationComponent animcomponent = new AnimationComponent(0,walkAnimation);
        animcomponent.animations.put(0,walkAnimation);
        animcomponent.animations.put(1,walkAnimation);
        animcomponent.animations.put(2,walkAnimation);
        animcomponent.animations.put(3,walkAnimation);
        animcomponent.animations.put(4,walkAnimation);


        //Vector2 scale = new Vector2(1f, 1f);

        for (int i = 0; i<4; i++){

            PlayerComponent playerComponent = WorldBuilder.getPlayerList().get(i);
            Vector2 vector = playerComponent.body.getPosition();


            scale = new Vector2(0.9f*(scaleX *1.32f), 0.9f*(scaleX *1.32f));

            ghost = new Entity();
            ghost.add(new VelocityComponent())
                    .add(WorldBuilder.getPlayerList().get(i))
//                    .add(new GhostComponent())
                    .add(new TextureComponent())
                    .add(animcomponent)
                    .add(new StateComponent(0))
                    .add(new TransformComponent(20,20, scale.x, scale.y,0))

                    .add(new CollisionComponent());
            engine.addEntity(ghost);
        }

        // Pac-Man
        PlayerComponent playerComponent = WorldBuilder.getPlayerList().get(4);
        Vector2 vector = playerComponent.body.getPosition();
        System.out.println("pacman is here: " + playerComponent.body.getPosition());


        pacmansprite = new Texture(app.skin);




        pacmanSpritus = new Sprite(pacmansprite);
        Vector2 position = new Vector2(20,20);

        scale = new Vector2(0.15f,0.15f);
        pacman = new Entity();
        pacman.add(new VelocityComponent())
//               .add(new PacmanComponent())
                .add(WorldBuilder.getPlayerList().get(4))
                .add(new TextureComponent(new TextureRegion(pacmansprite)))
                .add(new StateComponent(0))
                .add(new TransformComponent(2*vector.x / RenderingSystem.PPM, 2* vector.y / RenderingSystem.PPM, (scaleX *1.32f)*scale.x, (scaleX *1.32f)*scale.y, 0))
                .add(new CollisionComponent());
        engine.addEntity(pacman);


        pausescreen = new TextureRegion(new Texture("playscreen/pausescreen.png"));
        pauseSprite = new Sprite(pausescreen);
        pauseEntity = new Entity();
        pauseEntity.add(new TextureComponent(pauseSprite, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),false, false, false))
            .add(new TransformComponent(0,0));
        //engine.addEntity(pauseEntity);

        pauseButtonSprite = new Sprite(pausetexture);
        pauseButton = new Entity();
        app.addSpriteEntity(pauseButtonSprite, pauseButton, engine,  Gdx.graphics.getWidth() - (pauseButtonSprite.getRegionWidth() * scaleX), 50 * 32 * scaleX/ 1.5f, pauseButtonSprite.getRegionWidth() * scaleX, pauseButtonSprite.getRegionHeight() * scaleX, true,false, false, false);


        backSprite = new Sprite(back);
        backButton = new Entity();
        app.addSpriteEntity(backSprite, backButton, engine, 0,  50 * 32 * scaleX/ 1.5f, backSprite.getRegionWidth() * scaleX, backSprite.getRegionHeight() * scaleX, true,false, false, false);

        musicEntity = new Entity();
        musicEntity.add(new MusicComponent(Gdx.files.internal("music/play")));
        engine.addEntity(musicEntity);

        musicPauseEntity = new Entity();
        musicPauseEntity.add(new MusicComponent(Gdx.files.internal("music/pause")));
        //engine.addEntity(musicPauseEntity);

        aiSystem.setDifficulty(app.ai_difficulty);


        // PauseEntities

        pause_ellipseSprite = new Sprite(pause_ellipse);
        pause_ellipseEntity = new Entity();
        app.addSpriteEntity(pause_ellipseSprite, pause_ellipseEntity, engine, (Gdx.graphics.getWidth() / 2 - (pause_ellipseSprite.getRegionWidth() / 2 * (scaleX))),  Gdx.graphics.getHeight() / 3, pause_ellipseSprite.getRegionWidth() * scaleX, pause_ellipseSprite.getRegionHeight() * scaleY, true,true, true, false);
        engine.removeEntity(pause_ellipseEntity);

        front_ellipseSprite = new Sprite(front_ellipse);
        front_ellipseEntity = new Entity();
        app.addSpriteEntity(front_ellipseSprite, front_ellipseEntity, engine, (Gdx.graphics.getWidth() / 2 - (front_ellipseSprite.getRegionWidth() / 2 * (scaleX))), (Gdx.graphics.getHeight() / 3) + (pause_ellipseSprite.getRegionHeight() * scaleY - front_ellipseSprite.getRegionHeight() * scaleY) / 2, front_ellipseSprite.getRegionWidth() * scaleX, front_ellipseSprite.getRegionHeight() * scaleY, false,false, false, false);
        engine.removeEntity(front_ellipseEntity);

        pauseiconSprite = new Sprite(pauseicon);
        pauseiconEntity = new Entity();
        app.addSpriteEntity(pauseiconSprite, pauseiconEntity, engine, (Gdx.graphics.getWidth() / 2 - (pauseiconSprite.getRegionWidth() / 2 * (scaleX))),  (Gdx.graphics.getHeight() / 3) + (pause_ellipseSprite.getRegionHeight() * scaleY - pauseiconSprite.getRegionHeight() * scaleY) / 2, pauseiconSprite.getRegionWidth() * scaleX, pauseiconSprite.getRegionHeight() * scaleY, false,true, true, false);
        engine.removeEntity(pauseiconEntity);


    }

    // Render the PlayScreen, for now only a picture with green background. This method i
    // needed in every screen but can be changed to show different data.
    @Override
    public void render(float delta) {

        super.render(delta);
        // draws the background to our style
        Gdx.gl.glClearColor(33/255f, 32/255f, 49/255f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //System.out.println("camera  " + camera.position);
        tmr.setView(camera);
        tmr.render();

        //rendering the box debugger lags the game
        //b2dr.render(world, camera.combined.scl(1f));

//        engine.update(delta);

        // when paused engine stops updating, and textures "disappear"
        if(!pause) {
            if(resume) {
                //engine.removeEntity(pauseEntity);
                //engine.addEntity(pacman);
                musicSystem.resume();
                engine.removeEntity(pauseEntity);
                engine.removeEntity(pause_ellipseEntity);
                engine.removeEntity(front_ellipseEntity);
                engine.removeEntity(pauseiconEntity);
                engine.addEntity(pauseButton);
                engine.addEntity(backButton);
                resume = false;
            }
            engine.update(delta);

            if (destroyAllBodies){
                Array<Body> bodies = new Array<Body>();
                world.getBodies(bodies);
                for (Body body: bodies){
                    world.destroyBody(body);
                }
                world.dispose();
                destroyAllBodies = false;
            }



            world.step(1/60f,6,2);

        }
        if(pause){

            //engine.addEntity(pauseEntity);
            //engine.removeEntity(pacman);
            musicSystem.pause();
            engine.getSystem(RenderingSystem.class).update(delta);
            engine.removeEntity(pauseButton);
            engine.removeEntity(backButton);
            engine.getSystem(ButtonSystem.class).update(delta);
            //engine.getEntitiesFor(Family.one())
            //engine.getSystem(ButtonSystem.class).update(delta);
            if(!resume) {
                engine.addEntity(pauseEntity);
                engine.addEntity(pause_ellipseEntity);
                engine.addEntity(front_ellipseEntity);
                engine.addEntity(pauseiconEntity);

            }
            resume = true;
            /*if(Gdx.input.justTouched()) {
                pause = false;
            }*/
        }
        //engine.update(delta);

        hud.stage.getViewport().apply();
        hud.stage.draw();

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);

    }

    @Override
    public void resize(int width, int height) {
        hud.stage.getViewport().update(width, height);
    }

    @Override
    public void dispose(){
        super.dispose();
        engine.removeAllEntities();
        tmr.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
