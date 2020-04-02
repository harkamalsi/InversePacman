package com.mygdx.game.screens.menu;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.InversePacman;
import com.mygdx.game.components.ButtonComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.managers.GameScreenManager;
import com.mygdx.game.screens.AbstractScreen;
import com.mygdx.game.systems.ButtonSystem;
import com.mygdx.game.systems.RenderingSystem;
import com.mygdx.game.managers.EntityManager;

public class MainMenuScreen extends AbstractScreen {
    private OrthographicCamera camera;
    private FitViewport viewport;

    private EntityManager entityManager;

    private SpriteBatch batch;

    private Engine engine;
    private Vector3 clickPosition;

    private ButtonSystem buttonSystem;
    private RenderingSystem renderingSystem;

    private TextureComponent textureComponent1;
    private ButtonComponent buttonComponent1;
    private TransformComponent transformComponent1;

    private TextureComponent textureComponent2;
    private ButtonComponent buttonComponent2;
    private TransformComponent transformComponent2;

    private TextureRegion play;
    private Texture multiplay;
    private TextureRegion bg;

    private Entity singleplayerButton;
    private Entity multiplayerButton;
    private Entity settingsButton;

    private Sprite playsprite;
    private Sprite multisprite;


/*
I am not sure if we are going to use Gdx.graphics.getWidth/Height or InversePacman.V_WIDTH/HEIGHT
 */

    public MainMenuScreen(final InversePacman app) {
        super(app);
        bg = new TextureRegion(new Texture("menuscreen.png"));
        play = new TextureRegion(new Texture("new_play.png"));
        multiplay = new Texture("multiplayer.png");

    }


    public void handleInput(){
        if (Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
            app.gsm.setScreen(GameScreenManager.STATE.PLAY);
        }
        if(singleplayerButton.flags == 1) {
            app.gsm.setScreen(GameScreenManager.STATE.PLAY);
        }
        if (multiplayerButton.flags == 1) {
            app.gsm.setScreen(GameScreenManager.STATE.MULTI_PLAYER_BOARD_SCREEN);
        }
    }

    @Override
    public void render(float delta){
        super.render(delta);

        engine.update(delta);
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0.2f, 0.2f, 0.1f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(bg, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        playsprite.setSize(250,150);
        playsprite.draw(batch);
        multisprite.draw(batch);
        batch.end();

    }

    @Override
    public void update(float delta) {
        handleInput();
    }

    @Override
    public void show() {
        this.camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());



        batch = new SpriteBatch();

        buttonSystem = new ButtonSystem(camera);
        playsprite = new Sprite(new TextureRegion(new Texture("new_play.png")));
        playsprite.setBounds(Gdx.graphics.getWidth() / 2 - playsprite.getRegionWidth() / 2, Gdx.graphics.getHeight() - 200, playsprite.getRegionWidth(), playsprite.getRegionHeight());




        multisprite = new Sprite(new TextureRegion(new Texture("multiplayer.png")));
        multisprite.setBounds(Gdx.graphics.getWidth() / 2 - multisprite.getWidth() / 2, Gdx.graphics.getHeight() - 400, multisprite.getRegionWidth(), multisprite.getRegionHeight());


        textureComponent1 = new TextureComponent(playsprite);
        buttonComponent1 = new ButtonComponent(Gdx.graphics.getWidth() / 2 - playsprite.getRegionWidth() / 2, Gdx.graphics.getHeight() - 200, playsprite.getRegionWidth(),  playsprite.getRegionHeight());
        transformComponent1 = new TransformComponent(Gdx.graphics.getWidth() / 2 - playsprite.getRegionWidth() / 2, Gdx.graphics.getHeight() - 200);

        textureComponent2 = new TextureComponent(multisprite);
        buttonComponent2 = new ButtonComponent(Gdx.graphics.getWidth()  / 2 - multisprite.getRegionWidth() / 2, Gdx.graphics.getHeight() - 400, multisprite.getRegionWidth(), multisprite.getRegionHeight());
        transformComponent2 = new TransformComponent(Gdx.graphics.getWidth() / 2 - multisprite.getRegionWidth() / 2, Gdx.graphics.getHeight() - 400);



        singleplayerButton = new Entity();
        multiplayerButton = new Entity();
        settingsButton = new Entity();
        singleplayerButton.add(textureComponent1);
        singleplayerButton.add(buttonComponent1);
        singleplayerButton.add(transformComponent1);

        multiplayerButton.add(textureComponent2);
        multiplayerButton.add(buttonComponent2);
        multiplayerButton.add(transformComponent2);






        engine = new Engine();

        //entityManager = new EntityManager(engine, app.batch);

        engine.addSystem(buttonSystem);
        engine.addEntity(singleplayerButton);
        engine.addEntity(multiplayerButton);


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
