package com.mygdx.game.screens.play;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.InversePacman;
import com.mygdx.game.components.AnimationComponent;
import com.mygdx.game.components.CollisionComponent;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.StateComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.components.VelocityComponent;
import com.mygdx.game.managers.EntityManager;
import com.mygdx.game.screens.AbstractScreen;
import com.mygdx.game.systems.AnimationSystem;
import com.mygdx.game.systems.CollisionSystem;
import com.mygdx.game.systems.MovementSystem;
import com.mygdx.game.systems.PlayerInputSystem;
import com.mygdx.game.systems.RenderingSystem;
import com.mygdx.game.systems.StateSystem;


public final class PlayScreen extends AbstractScreen {

    private OrthographicCamera camera;

    private SpriteBatch batch;
    private EntityManager entityManager;

    private Texture pacmansprite;
    private Texture ghostsheet;

    private Entity pacman;
    private Entity ghost;

    private Engine engine;

    private CollisionSystem collisionSystem;
    private MovementSystem movementSystem;
    private PlayerInputSystem playerInputSystem;
    private RenderingSystem renderingSystem;
    private StateSystem stateSystem;
    private AnimationSystem animationSystem;


    public BitmapFont font = new BitmapFont(); //or use alex answer to use custom font

    public PlayScreen(final InversePacman app) {
        super(app);

        // Sets the camera; width and height.
//        this.camera = new OrthographicCamera();
//        this.camera.setToOrtho(false, InversePacman.V_WIDTH, InversePacman.V_HEIGHT);


    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void show() {

        batch = new SpriteBatch();
        playerInputSystem = new PlayerInputSystem();
        movementSystem = new MovementSystem();
        collisionSystem = new CollisionSystem();
        renderingSystem = new RenderingSystem(batch);
        stateSystem = new StateSystem();
        animationSystem = new AnimationSystem();

        engine = new Engine();
        engine.addSystem(playerInputSystem);
        engine.addSystem(movementSystem);
        engine.addSystem(collisionSystem);
        engine.addSystem(renderingSystem);
        engine.addSystem(stateSystem);
        engine.addSystem(animationSystem);


        //splitting up the different frames in the ghost sheet and adding them to an animation
        ghostsheet = new Texture("ghosts.png");
        TextureRegion[][] temp = TextureRegion.split(ghostsheet,ghostsheet.getWidth()/10, ghostsheet.getHeight());
        TextureRegion[] walkFrames = new TextureRegion[10];

        for (int i = 0; i < 10; i++) {
                walkFrames[i] = temp[0][i];
        }

        Animation walkAnimation = new Animation<>(0.5f,walkFrames);
        //adding animation to each direction state and idle
        AnimationComponent animcomponent = new AnimationComponent(0,walkAnimation);
        animcomponent.animations.put(0,walkAnimation);
        animcomponent.animations.put(1,walkAnimation);
        animcomponent.animations.put(2,walkAnimation);
        animcomponent.animations.put(3,walkAnimation);
        animcomponent.animations.put(4,walkAnimation);
//        pacman = new Entity();
//        pacman.add(new VelocityComponent())
////                .add(new TextureComponent(new TextureRegion(pacmansprite)))
//                .add(new TextureComponent())
//                .add(animcomponent)
//                .add(new StateComponent(0))
//                .add(new TransformComponent(20,20))
//                .add(new CollisionComponent());
//        engine.addEntity(pacman);

        ghost = new Entity();
        ghost.add(new VelocityComponent())
                .add(new TextureComponent())
                .add(animcomponent)
                .add(new StateComponent(0))
                .add(new TransformComponent(20,20))
                .add(new CollisionComponent());
        engine.addEntity(ghost);



    }


    // Render the PlayScreen, for now only a picture with green background. This method i
    // needed in every screen but can be changed to show different data.
    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        app.batch.begin();
        app.batch.draw(app.img, 0, 0);
        app.batch.end();
        engine.update(delta);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
