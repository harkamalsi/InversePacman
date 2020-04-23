package com.mygdx.game.screens.play;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.InversePacman;
import com.mygdx.game.components.AnimationComponent;
import com.mygdx.game.components.CollisionComponent;
import com.mygdx.game.components.PillComponent;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.StateComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.components.VelocityComponent;
import com.mygdx.game.managers.EntityManager;
import com.mygdx.game.managers.GameScreenManager;
import com.mygdx.game.screens.AbstractScreen;
import com.mygdx.game.systems.AISystem;
import com.mygdx.game.systems.AnimationSystem;
import com.mygdx.game.systems.ButtonSystem;
import com.mygdx.game.systems.CollisionSystem;
import com.mygdx.game.systems.MovementSystem;
import com.mygdx.game.systems.MusicSystem;
import com.mygdx.game.systems.PillSystem;
import com.mygdx.game.systems.PlayerContactListener;
import com.mygdx.game.systems.PlayerInputSystem;
import com.mygdx.game.systems.RenderingSystem;
import com.mygdx.game.systems.StateSystem;
import com.mygdx.game.worldbuilder.WorldBuilder;

import sun.security.jgss.GSSCaller;

public final class PlayScreen extends AbstractScreen {

    private OrthographicCamera camera;

    private SpriteBatch batch;
    private EntityManager entityManager;

    private Texture pacmansprite;
    private Texture ghostsheet;
    private Texture pillSprite;

    private TextureRegion pausescreen;
    private TextureRegion back;

    private Sprite pauseSprite;
    private Sprite backSprite;

    private Entity pacman;
    private Entity pauseEntity;
    private Entity backButton;
    private Entity ghost;
    private Entity pill;

    //World building
    public World world;
    public Body player;
    public Box2DDebugRenderer b2dr;

    //Box2d
    public OrthogonalTiledMapRenderer tmr;
    public TiledMap map;
    public boolean destroyAllBodies;

    private Engine engine;

    private boolean pause = false;
    private boolean resume = false;

    private CollisionSystem collisionSystem;
    private MovementSystem movementSystem;
    private PlayerInputSystem playerInputSystem;
    private AISystem aiSystem;
    private RenderingSystem renderingSystem;
    private StateSystem stateSystem;
    private AnimationSystem animationSystem;
    private MusicSystem musicSystem;
    private ButtonSystem buttonSystem;
    private PillSystem pillSystem;


    public PlayScreen(final InversePacman app, Engine engine) {
        super(app, engine);
        this.engine = engine;
        back = new TextureRegion(new Texture("back.png"));
//        this.engine = engine;
//         Sets the camera; width and height.
        this.camera = new OrthographicCamera();

    }

    public void handleInput(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
            //Important to call both if you want to remove the music from the previous screen
            musicSystem.dispose();
            engine.removeSystem(musicSystem);
            engine.removeAllEntities();


            destroyAllBodies = true;
            app.gsm.setScreen(GameScreenManager.STATE.MAIN_MENU_SCREEN);

        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            pause = true;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            pause = false;
        }
        if(backButton.flags == 1) {
            pause = true;
            backButton.flags = 0;
        }
    }

    @Override
    public void update(float delta) {
        handleInput();
        // Chooses the next song to play if the song has finished
        // Had to add the second condition since it chose to play a new song as I switched screens
        if (pillSystem.allPillsCollected()) {
            engine.removeAllEntities();

            musicSystem.dispose();
            engine.removeSystem(musicSystem);
            engine.removeSystem(collisionSystem);
            //engine.removeSystem(renderingSystem);
            //engine.removeSystem(pillSystem);
            engine.removeSystem(animationSystem);
            ghostsheet.dispose();
            System.out.println("pill list: " + WorldBuilder.getPillList());
            System.out.println("pill size list  " + WorldBuilder.getPillList().size());
            /*int count = 0;
            for(int i = 0; i <= WorldBuilder.getPillList().size(); i++ ) {
                count += 1;
                WorldBuilder.getPillList().remove(i);
            }
            for(int i = 0; i <= WorldBuilder.getPlayerList().size(); i++ ) {
                count += 1;
                WorldBuilder.getPlayerList().remove(i);
            }
            System.out.println(count);
            System.out.println("my medication gone: " + WorldBuilder.getPillList());*/

            //WorldBuilder.getPillList().remove(0);
            app.gsm.setScreen(GameScreenManager.STATE.GAME_OVER_SCREEN);



        }
    }


    @Override
    public void show() {
        //this.camera = new OrthographicCamera();
        //this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //this.camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        //this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // We should probably change the above to below
        //this.camera.setToOrtho(false, InversePacman.V_WIDTH, InversePacman.V_HEIGHT);

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
                ,map.getLayers().get("Pills").getObjects());
        WorldBuilder.createPlayers(world);
        WorldBuilder.createPills(world);
        this.camera.setToOrtho(false, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
        MapLayer layer = map.getLayers().get("Pills");
        MapObjects objects = layer.getObjects();
        System.out.println("object pills: " + objects.get(100).getName());






        // To add a new songs, place the file under the folder assets/music/play

        batch = new SpriteBatch();
        playerInputSystem = new PlayerInputSystem();
        aiSystem = new AISystem();
        movementSystem = new MovementSystem();
        collisionSystem = new CollisionSystem();
        renderingSystem = new RenderingSystem(batch);
        buttonSystem = new ButtonSystem(camera);
        stateSystem = new StateSystem();
        animationSystem = new AnimationSystem();
        musicSystem = new MusicSystem(Gdx.files.internal("music/play"));
        pillSystem = new PillSystem();

        engine.addSystem(playerInputSystem);
        engine.addSystem(aiSystem);
        engine.addSystem(movementSystem);
        engine.addSystem(collisionSystem);
        engine.addSystem(renderingSystem);
        engine.addSystem(stateSystem);
        engine.addSystem(animationSystem);
        engine.addSystem(musicSystem);
        engine.addSystem(buttonSystem);
        engine.addSystem(pillSystem);

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

        Vector2 scale = new Vector2(1f, 1f);

        for (int i = 0; i<4; i++){
            PlayerComponent playerComponent = WorldBuilder.getPlayerList().get(i);
            Vector2 vector = playerComponent.body.getPosition();

            ghost = new Entity();
            ghost.add(new VelocityComponent())
                    .add(WorldBuilder.getPlayerList().get(i))
//                    .add(new GhostComponent())
                    .add(new TextureComponent())
                    .add(animcomponent)
                    .add(new StateComponent(0))
                    .add(new TransformComponent(2*vector.x / RenderingSystem.PPM,
                            2* vector.y / RenderingSystem.PPM, scale.x, scale.y, 0f))
                    .add(new CollisionComponent());
            engine.addEntity(ghost);
        }

        pillSprite = new Texture("white_pill.png");
        scale = new Vector2(0.05f, 0.05f);
        for (int i = 0; i < WorldBuilder.getPillList().size(); i++) {
            PillComponent pillComponent = WorldBuilder.getPillList().get(i);
            Vector2 vector = pillComponent.body.getPosition();

            pill = new Entity();
            pill.add(WorldBuilder.getPillList().get(i))
                    .add(new TextureComponent(new TextureRegion(pillSprite)))
                    .add(new TransformComponent(2*vector.x / RenderingSystem.PPM,
                            2* vector.y / RenderingSystem.PPM, scale.x, scale.y, 0f));

            engine.addEntity(pill);
        }
        PlayerComponent playerComponent = WorldBuilder.getPlayerList().get(4);
        Vector2 vector = playerComponent.body.getPosition();
        System.out.println("pacman is here: " + playerComponent.body.getPosition());
        pacmansprite = new Texture("pacman.png");
        //Vector2 position = new Vector2(20,20);
        scale = new Vector2(0.15f,0.15f);
        pacman = new Entity();
        pacman.add(new VelocityComponent())
//                .add(new PacmanComponent())
                .add(WorldBuilder.getPlayerList().get(4))
                .add(new TextureComponent(new TextureRegion(pacmansprite)))
                .add(new StateComponent(0))
                .add(new TransformComponent(2*vector.x / RenderingSystem.PPM,
                        2* vector.y / RenderingSystem.PPM, scale.x, scale.y, 0f))
                .add(new CollisionComponent());
        engine.addEntity(pacman);

        pausescreen = new TextureRegion(new Texture("playscreen/pausescreen.png"));
        pauseSprite = new Sprite(pausescreen);
        pauseEntity = new Entity();
        pauseEntity.add(new TextureComponent(pauseSprite, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),false, false, false))
            .add(new TransformComponent(0,0));
        //engine.addEntity(pauseEntity);

        backSprite = new Sprite(back);
        backButton = new Entity();
        app.addSpriteEntity(backSprite, backButton, engine, 0, 0, backSprite.getRegionWidth(), backSprite.getRegionHeight(), true,false, false, false);

    }

    // Render the PlayScreen, for now only a picture with green background. This method i
    // needed in every screen but can be changed to show different data.
    @Override
    public void render(float delta) {

        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tmr.setView(camera);
        tmr.render();
        b2dr.render(world, camera.combined.scl(1f));
//        engine.update(delta);


        // when paused engine stops updating, and textures "disappear"
        if(!pause) {
            if(resume) {
                //engine.removeEntity(pauseEntity);
                //engine.addEntity(pacman);
                musicSystem.resume();
                resume = false;
            }
            engine.update(delta);

            if (destroyAllBodies){
                Array<Body> bodies = new Array<Body>();
                world.getBodies(bodies);
                for (Body body: bodies){
                    world.destroyBody(body);
                }
                destroyAllBodies = false;
            }

            world.step(1/60f,6,2);

        }
        if(pause){
            batch.begin();
            batch.draw(pausescreen, 0,0, Gdx.graphics.getWidth() / 32f, Gdx.graphics.getHeight()/ 32f);
            batch.end();
            //engine.addEntity(pauseEntity);
            //engine.removeEntity(pacman);
            musicSystem.pause();
            resume = true;
            if(Gdx.input.justTouched()) {
                pause = false;
            }
        }
        //engine.update(delta);


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
    public void dispose(){
        super.dispose();
        engine.removeAllEntities();
        tmr.dispose();
        b2dr.dispose();

    }
}
