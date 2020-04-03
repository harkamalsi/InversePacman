package com.mygdx.game.screens.menu;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
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
    private TextureRegion multiplay;
    private TextureRegion highscore;
    private TextureRegion option;
    private TextureRegion bg;

    private Entity singleplayerButton;
    private Entity multiplayerButton;
    private Entity highscoreButton;
    private Entity optionButton;


    private Sprite playsprite;
    private Sprite multisprite;
    private Sprite highscoresprite;
    private Sprite optionsprite;
    private Music music;




/*
I am not sure if we are going to use Gdx.graphics.getWidth/Height or InversePacman.V_WIDTH/HEIGHT
 */

    public MainMenuScreen(final InversePacman app) {
        super(app);
        bg = new TextureRegion(new Texture("menuscreen/menuscreen_bg.png"));
        play = new TextureRegion(new Texture("menuscreen/play_button.png"));
        multiplay = new TextureRegion(new Texture("menuscreen/multiplayer_button.png"));
        highscore = new TextureRegion(new Texture("menuscreen/highscore_button.png"));
        option = new TextureRegion(new Texture("menuscreen/options_button.png"));

    }


    public void handleInput(){
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
            music.dispose();
            app.gsm.setScreen(GameScreenManager.STATE.PLAY);
        }
        if(singleplayerButton.flags == 1 || Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
            music.dispose();
            app.gsm.setScreen(GameScreenManager.STATE.PLAY);
        }
        if(multiplayerButton.flags == 1) {
            music.dispose();
            app.gsm.setScreen(GameScreenManager.STATE.MULTI_PLAYER_BOARD_SCREEN);
        }
        if(highscoreButton.flags == 1) {
            music.dispose();
            app.gsm.setScreen(GameScreenManager.STATE.SINGLE_PLAYER_BOARD_SCREEN);
        }
        if(optionButton.flags == 1) {
            music.dispose();
            // We need a settings screen, this is really going to be the option screen
            app.gsm.setScreen(GameScreenManager.STATE.MAIN_MENU_SCREEN);
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
        //playsprite.setSize(350,75);
        playsprite.draw(batch);
        multisprite.draw(batch);
        highscoresprite.draw(batch);
        optionsprite.draw(batch);
        batch.end();


    }

    @Override
    public void update(float delta) {
        handleInput();
    }

    @Override
    public void show() {

        music = Gdx.audio.newMusic(Gdx.files.internal("music/menu/menumusic.mp3"));
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();

        this.camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());



        batch = new SpriteBatch();

        buttonSystem = new ButtonSystem(camera);
        renderingSystem = new RenderingSystem(batch);

        engine = new Engine();
        engine.addSystem(buttonSystem);



        playsprite = new Sprite(play);
        playsprite.setBounds(Gdx.graphics.getWidth() / 2 - playsprite.getRegionWidth() / 2, Gdx.graphics.getHeight() / (float)1.80, playsprite.getRegionWidth(), playsprite.getRegionHeight());

        singleplayerButton = new Entity();


        //textureComponent1 = new TextureComponent(playsprite);
        //buttonComponent1 = new ButtonComponent(Gdx.graphics.getWidth() / 2 - playsprite.getRegionWidth() / 2, Gdx.graphics.getHeight() - 200, playsprite.getRegionWidth(),  playsprite.getRegionHeight());
        //transformComponent1 = new TransformComponent(Gdx.graphics.getWidth() / 2 - playsprite.getRegionWidth() / 2, Gdx.graphics.getHeight() - 200);

        singleplayerButton.add(new TextureComponent(playsprite))
                .add(new ButtonComponent(Gdx.graphics.getWidth() / 2 - playsprite.getRegionWidth() / 2, Gdx.graphics.getHeight() / (float)1.80, playsprite.getRegionWidth(),  playsprite.getRegionHeight()))
                .add(new TransformComponent(Gdx.graphics.getWidth() / 2 - playsprite.getRegionWidth() / 2, Gdx.graphics.getHeight() / (float)1.80));
        engine.addEntity(singleplayerButton);

        //singleplayerButton.add(textureComponent1);
        //singleplayerButton.add(buttonComponent1);
        //singleplayerButton.add(transformComponent1);


        multisprite = new Sprite(multiplay);
        multisprite.setBounds(Gdx.graphics.getWidth() / 2 - multisprite.getWidth() / 2, Gdx.graphics.getHeight() / (float)2.9, multisprite.getRegionWidth(), multisprite.getRegionHeight());

        multiplayerButton = new Entity();

        multiplayerButton.add(new TextureComponent(multisprite))
                .add(new ButtonComponent(Gdx.graphics.getWidth() / 2 - multisprite.getRegionWidth() / 2, Gdx.graphics.getHeight() / (float)2.9, multisprite.getRegionWidth(), multisprite.getRegionHeight()))
                .add(new TransformComponent(Gdx.graphics.getWidth() / 2 - multisprite.getRegionWidth() / 2, Gdx.graphics.getHeight() / (float)2.9));
        engine.addEntity(multiplayerButton);



        //textureComponent2 = new TextureComponent(multisprite);
        //buttonComponent2 = new ButtonComponent(Gdx.graphics.getWidth()  / 2 - multisprite.getRegionWidth() / 2, Gdx.graphics.getHeight() - 600, multisprite.getRegionWidth(), multisprite.getRegionHeight());
        //transformComponent2 = new TransformComponent(Gdx.graphics.getWidth() / 2 - multisprite.getRegionWidth() / 2, Gdx.graphics.getHeight() - 600);

        highscoresprite = new Sprite(highscore);
        highscoresprite.setBounds(Gdx.graphics.getWidth() / 2 - highscoresprite.getWidth() / 2, Gdx.graphics.getHeight() / (float)3.8, highscoresprite.getRegionWidth(), highscoresprite.getRegionHeight());

        highscoreButton = new Entity();

        highscoreButton.add(new TextureComponent(highscoresprite))
                .add(new ButtonComponent(Gdx.graphics.getWidth() / 2 - highscoresprite.getWidth() / 2, Gdx.graphics.getHeight() / (float)3.8, highscoresprite.getRegionWidth(), highscoresprite.getRegionHeight()))
                .add(new TransformComponent(Gdx.graphics.getWidth() / 2 - highscoresprite.getWidth() / 2, Gdx.graphics.getHeight() / (float)3.8));
        engine.addEntity(highscoreButton);


        optionsprite = new Sprite(option);
        optionsprite.setBounds(Gdx.graphics.getWidth() / 2 - optionsprite.getWidth() / 2, Gdx.graphics.getHeight() / (float)5.6, optionsprite.getRegionWidth(), optionsprite.getRegionHeight());

        optionButton = new Entity();

        optionButton.add(new TextureComponent(optionsprite))
                .add(new ButtonComponent(Gdx.graphics.getWidth() / 2 - optionsprite.getWidth() / 2, Gdx.graphics.getHeight() / (float)5.6, optionsprite.getRegionWidth(), optionsprite.getRegionHeight()))
                .add(new TransformComponent(Gdx.graphics.getWidth() / 2 - optionsprite.getWidth() / 2, Gdx.graphics.getHeight() / (float)5.6));
        engine.addEntity(optionButton);


        //multiplayerButton.add(textureComponent2);
        //multiplayerButton.add(buttonComponent2);
        //multiplayerButton.add(transformComponent2);

        //entityManager = new EntityManager(engine, app.batch);
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
        super.dispose();
    }
}
