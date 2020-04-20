package com.mygdx.game.screens.play;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.InversePacman;
import com.mygdx.game.components.CollisionComponent;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.components.VelocityComponent;
import com.mygdx.game.managers.EntityManager;
import com.mygdx.game.managers.GameScreenManager;
import com.mygdx.game.screens.AbstractScreen;
import com.mygdx.game.systems.AnimationSystem;
import com.mygdx.game.systems.ButtonSystem;
import com.mygdx.game.systems.CollisionSystem;
import com.mygdx.game.systems.MovementSystem;
import com.mygdx.game.systems.MusicSystem;
import com.mygdx.game.systems.PlayerInputSystem;
import com.mygdx.game.systems.RenderingSystem;
import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;
import java.util.Random;

public final class PlayScreen extends AbstractScreen {

    private OrthographicCamera camera;

    private SpriteBatch batch;
    private EntityManager entityManager;

    private Texture pacmansprite;

    private TextureRegion pausescreen;
    private TextureRegion back;

    private Sprite pauseSprite;
    private Sprite backSprite;

    private Entity pacman;
    private Entity pauseEntity;
    private Entity backButton;

    private Engine engine;

    private boolean pause = false;
    private boolean resume = false;

    private CollisionSystem collisionSystem;
    private MovementSystem movementSystem;
    private PlayerInputSystem playerInputSystem;
    private RenderingSystem renderingSystem;
    private MusicSystem musicSystem;
    private ButtonSystem buttonSystem;


    public BitmapFont font = new BitmapFont(); //or use alex answer to use custom font

    public PlayScreen(final InversePacman app) {
        super(app);
        back = new TextureRegion(new Texture("back.png"));

        // Sets the camera; width and height.
        // this.camera = new OrthographicCamera();
        // this.camera.setToOrtho(false, InversePacman.V_WIDTH, InversePacman.V_HEIGHT);
    }

    public void handleInput(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
            //Important to call both if you want to remove the music from the previous screen
            musicSystem.dispose();
            engine.removeSystem(musicSystem);

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
    }


    @Override
    public void show() {
        this.camera = new OrthographicCamera();
        //this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //this.camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        // To add a new songs, place the file under the folder assets/music/play

        batch = new SpriteBatch();
        playerInputSystem = new PlayerInputSystem();
        movementSystem = new MovementSystem();
        collisionSystem = new CollisionSystem();
        renderingSystem = new RenderingSystem(batch);
        buttonSystem = new ButtonSystem(camera);
        musicSystem = new MusicSystem(Gdx.files.internal("music/play"));

        engine = new Engine();
        engine.addSystem(playerInputSystem);
        engine.addSystem(movementSystem);
        engine.addSystem(collisionSystem);
        engine.addSystem(renderingSystem);
        engine.addSystem(musicSystem);
        engine.addSystem(buttonSystem);

        pacmansprite = new Texture("ghosts.png");
        pacman = new Entity();
        pacman.add(new VelocityComponent())
                .add(new TextureComponent(new TextureRegion(pacmansprite)))
                .add(new TransformComponent(20,20))
                .add(new VelocityComponent())
                .add(new CollisionComponent())
                .add(new PlayerComponent());
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
        Gdx.gl.glClearColor(0, 1, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        app.batch.begin();
        app.batch.draw(app.img, 0, 0);
        app.batch.end();


        // when paused engine stops updating, and textures "disappear"
        if(!pause) {
            if(resume) {
                //engine.removeEntity(pauseEntity);
                //engine.addEntity(pacman);
                musicSystem.resume();
                resume = false;
            }
            engine.update(delta);
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

    }


    @Override
    public void dispose(){
    }
}
