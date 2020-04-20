package com.mygdx.game.screens.menu;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.InversePacman;
import com.mygdx.game.components.ButtonComponent;
import com.mygdx.game.components.CollisionComponent;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.components.VelocityComponent;
import com.mygdx.game.managers.GameScreenManager;
import com.mygdx.game.screens.AbstractScreen;
import com.mygdx.game.systems.ButtonSystem;
import com.mygdx.game.systems.MusicSystem;
import com.mygdx.game.systems.RenderingSystem;
import com.mygdx.game.managers.EntityManager;


public class MainMenuScreen extends AbstractScreen {
    private OrthographicCamera camera;
    private FitViewport viewport;


    private EntityManager entityManager;

    private SpriteBatch batch;
    private SpriteBatch bgBatch;

    private Stage stage;

    private Engine engine;
    private Vector3 clickPosition;

    private ButtonSystem buttonSystem;
    private RenderingSystem renderingSystem;
    private MusicSystem musicSystem;

    private TextureRegion play;
    private TextureRegion multiplay;
    private TextureRegion highscore;
    private TextureRegion option;
    private TextureRegion bg;

    private TextureRegion ellipse;
    private TextureRegion front_ellipse;

    private Entity singleplayerButton;
    private Entity multiplayerButton;
    private Entity highscoreButton;
    private Entity optionButton;
    private Entity ellipseEntity;
    private Entity front_ellipseEntity;
    private Entity bgEntity;


    private Sprite playsprite;
    private Sprite multisprite;
    private Sprite highscoresprite;
    private Sprite optionsprite;

    private Sprite ellipseSprite;
    private Sprite front_ellipseSprite;
    private Sprite bgSprite;


    private Skin skin;
    private Label lobbyName;
    private Label lobbyPlayers;
    private Label addressLabel;
    private TextField addressText;
    private float scaleX;
    private float scaleY;

    private BitmapFont font;
    private GlyphLayout layout;


/*
I am not sure if we are going to use Gdx.graphics.getWidth/Height or InversePacman.V_WIDTH/HEIGHT
 */

    public MainMenuScreen(final InversePacman app) {
        super(app);
        stage = new Stage(new ScreenViewport());

        bg = new TextureRegion(new Texture("menuscreen/menu_bg.png"));
        play = new TextureRegion(new Texture("menuscreen/play.png"));
        multiplay = new TextureRegion(new Texture("menuscreen/multi.png"));
        highscore = new TextureRegion(new Texture("menuscreen/high.png"));
        option = new TextureRegion(new Texture("menuscreen/options.png"));

        ellipse = new TextureRegion(new Texture("menuscreen/ellipse_color_change_correct.png"));
        front_ellipse = new TextureRegion(new Texture("optionscreen/option_front_ellipse.png"));

        font = new BitmapFont(Gdx.files.internal("font/rubik_font_correct.fnt"));
        layout = new GlyphLayout(); //dont do this every frame! Store it as member

        scaleX = Gdx.graphics.getWidth() / (float)app.APP_WIDTH_MOBILE;
        scaleY = Gdx.graphics.getHeight() / (float)app.APP_HEIGHT_MOBILE;

    }


    public void handleInput(){
        if(Gdx.input.isKeyPressed(Input.Keys.NUM_1)) {
            //Important to call both if you want to remove the music from the previous screen, order doesn't matter
            engine.removeSystem(musicSystem);
            musicSystem.dispose();

            app.gsm.setScreen(GameScreenManager.STATE.PLAY);
        }
        if(singleplayerButton.flags == 1 || Gdx.input.isKeyPressed(Input.Keys.NUM_2)) {
            engine.removeSystem(musicSystem);
            musicSystem.dispose();
            app.gsm.setScreen(GameScreenManager.STATE.PLAY);
        }
        if(multiplayerButton.flags == 1) {
            engine.removeSystem(musicSystem);
            musicSystem.dispose();
            app.gsm.setScreen(GameScreenManager.STATE.LOBBY_SCREEN);
        }

        if(highscoreButton.flags == 1) {
            engine.removeSystem(musicSystem);
            musicSystem.dispose();
            // I think it's okay if we keep ths music going here?
            //music.dispose();
            app.gsm.setScreen(GameScreenManager.STATE.LEADERBOARD_MENU_SCREEN);
        }
        if(optionButton.flags == 1) {
            engine.removeSystem(musicSystem);
            musicSystem.dispose();
            app.gsm.setScreen(GameScreenManager.STATE.OPTION_SCREEN);
        }

        /*else if (Gdx.input.isKeyPressed(Input.Keys.P) && app.gsm.currentState == GameScreenManager.STATE.MAIN_MENU_SCREEN) {
			System.out.println("PAUSING!");
			app.gsm.pushScreen(GameScreenManager.STATE.PAUSE);
            musicSystem.pause();
		}
		else if (Gdx.input.isKeyJustPressed(Input.Keys.R) && app.gsm.currentState == GameScreenManager.STATE.PAUSE) {
			System.out.println("UNPAUSING!");
			//app.gsm.popScreen();
			app.gsm.setScreen(GameScreenManager.STATE.PLAY);
		}*/

    }

    @Override
    public void render(float delta){
        super.render(delta);
        //bgBatch.begin();
        //bgBatch.draw(bg, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //bgBatch.end();
        /*batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0.2f, 0.2f, 0.1f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        //font.getData().setScale(scaleX * (float)Math.abs(Math.sin(app.b)),scaleY * (float)Math.abs(Math.sin(app.b)));
        font.getData().setScale(scaleX,scaleY);


        batch.draw(bg, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        ellipseSprite.setColor(0,78, 59,app.a);
        ellipseSprite.draw(batch);
        batch.draw(front_ellipse, Gdx.graphics.getWidth() / 2 - (front_ellipse.getRegionWidth() / 2 * (scaleX)), Gdx.graphics.getHeight() / (float)1.17, front_ellipse.getRegionWidth() * (scaleX), front_ellipse.getRegionHeight() * (scaleY));
        layout.setText(font,"Menu");
        //font.setColor(0,78, 59,a);
        font.draw(batch,layout,Gdx.graphics.getWidth() / 2 - layout.width / 2, (Gdx.graphics.getHeight() - ((layout.height / 2) / (float)1.5)) / (float)1.06);
        //playsprite.setSize(350,75);
        //playsprite.setScale(scaleX, scaleY);

        //playsprite.setColor(100,20,150, app.a);
        //playsprite.setScale(scaleX * (float)Math.abs(Math.sin(app.a)),scaleY * (float)Math.abs(Math.sin(app.a)));
        //playsprite.draw(batch);
        //multisprite.draw(batch);
        //highscoresprite.draw(batch);
        //optionsprite.draw(batch);

        batch.end();*/
        engine.update(delta);
        stage.act();
        stage.draw();
        batch.begin();

        font.setUseIntegerPositions(false);
        font.getData().setScale(scaleX / 32f, scaleY / 32f);
        layout.setText(font,"MENU");
        //System.out.println("font x: " + (Gdx.graphics.getWidth() / 2 - layout.width / 2));
        //System.out.println("font y: " + (Gdx.graphics.getHeight() / 1.05f - (layout.height / 2)));
        //System.out.println("scalex: " + scaleX + " scaled X: " + (scaleX*(float)1/32));


        font.draw(batch,"menu", (Gdx.graphics.getWidth() / 64f - layout.width / 2f),
                (Gdx.graphics.getHeight() / (1.05f * 32f) - (layout.height / 2f)));
        batch.end();


    }

    @Override
    public void update(float delta) {
        handleInput();
        //app.step_scale();
        //System.out.println("music level: " + music.getVolume());
        //System.out.println("Sound level" + app.sound_volume);
        //System.out.println("Width: " + optionsprite.getWidth() + "Region Width: " + optionsprite.getRegionWidth());
    }

    @Override
    public void show() {
        this.camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());



        batch = new SpriteBatch();
        bgBatch = new SpriteBatch();

        ellipseSprite = new Sprite(ellipse);
        //ellipseSprite.setBounds(Gdx.graphics.getWidth() / 2 - (ellipse.getRegionWidth() / 2 * (scaleX)), Gdx.graphics.getHeight() - (ellipse.getRegionHeight() * (scaleY)), ellipse.getRegionWidth() * (scaleX), ellipse.getRegionHeight() * (scaleY));



        buttonSystem = new ButtonSystem(camera);
        renderingSystem = new RenderingSystem(batch);
        musicSystem = new MusicSystem(Gdx.files.internal("music/menu"));


        engine = new Engine();
        engine.addSystem(buttonSystem);
        engine.addSystem(musicSystem);
        engine.addSystem(renderingSystem);

        bgSprite = new Sprite(bg);

        bgEntity = new Entity();

        //Don't know why but the background doesn't surround the whole screen, therefore I added some +/- on the parameters
        bgEntity.add(new TextureComponent(bgSprite, 0, -2, Gdx.graphics.getWidth() +2,
                Gdx.graphics.getHeight() +2,false,false))
                .add(new TransformComponent(0,0));
        engine.addEntity(bgEntity);


        ellipseEntity = new Entity();
        ellipseEntity.add(new TextureComponent(ellipseSprite, (Gdx.graphics.getWidth() / 2 - (ellipse.getRegionWidth() / 2 * (scaleX))), (Gdx.graphics.getHeight() - (ellipse.getRegionHeight() * (scaleY))), (ellipse.getRegionWidth() * (scaleX)), (ellipse.getRegionHeight() * (scaleY)), true, true))
                .add(new TransformComponent(Gdx.graphics.getWidth() / 2 - (ellipse.getRegionWidth() / 2 * (scaleX)), Gdx.graphics.getHeight() - (ellipse.getRegionHeight() * (scaleY))));
        engine.addEntity(ellipseEntity);


        front_ellipseSprite = new Sprite(front_ellipse);

        front_ellipseEntity = new Entity();
        front_ellipseEntity.add(new TextureComponent(front_ellipseSprite, Gdx.graphics.getWidth() / 2 - (front_ellipse.getRegionWidth() / 2 * (scaleX)), Gdx.graphics.getHeight() / (float)1.17, front_ellipse.getRegionWidth() * (scaleX), front_ellipse.getRegionHeight() * (scaleY), false, false))
                .add(new TransformComponent(Gdx.graphics.getWidth() / 2 - (front_ellipse.getRegionWidth() / 2 * (scaleX)), Gdx.graphics.getHeight() / (float)1.17));
        engine.addEntity(front_ellipseEntity);
        // ***** Play button START *****

        playsprite = new Sprite(play);
        //playsprite.setBounds(Gdx.graphics.getWidth() / 2 - (playsprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)1.80, playsprite.getRegionWidth() * scaleX, playsprite.getRegionHeight() * scaleY);

        singleplayerButton = new Entity();

        singleplayerButton.add(new TextureComponent(playsprite, Gdx.graphics.getWidth() / 2 - (playsprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)1.80, playsprite.getRegionWidth() * scaleX , playsprite.getRegionHeight() * scaleY, false, false))
                .add(new ButtonComponent(Gdx.graphics.getWidth() / 2 - (playsprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)1.80, playsprite.getRegionWidth() * scaleX , playsprite.getRegionHeight() * scaleY))
                .add(new TransformComponent(Gdx.graphics.getWidth() / 2 - (playsprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)1.80));
        engine.addEntity(singleplayerButton);

        // ***** Play button END *****

        // ***** Multiplayer button START *****

        multisprite = new Sprite(multiplay);
        //multisprite.setBounds(Gdx.graphics.getWidth() / 2 - (multisprite.getWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)2.9, multisprite.getRegionWidth() * scaleX, multisprite.getRegionHeight() * scaleY);

        multiplayerButton = new Entity();

        multiplayerButton.add(new TextureComponent(multisprite, Gdx.graphics.getWidth() / 2 - (multisprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)2.9, multisprite.getRegionWidth() * scaleX, multisprite.getRegionHeight() * scaleY, false, false))
                .add(new ButtonComponent(Gdx.graphics.getWidth() / 2 - (multisprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)2.9, multisprite.getRegionWidth() * scaleX, multisprite.getRegionHeight() * scaleY))
                .add(new TransformComponent(Gdx.graphics.getWidth() / 2 - (multisprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)2.9));
        engine.addEntity(multiplayerButton);

        // ***** Multiplayer button END *****

        // ***** Highscore button START *****

        highscoresprite = new Sprite(highscore);
        //highscoresprite.setBounds(Gdx.graphics.getWidth() / 2 - (highscoresprite.getWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)3.8, highscoresprite.getRegionWidth() * scaleX, highscoresprite.getRegionHeight() * scaleY);

        highscoreButton = new Entity();

        highscoreButton.add(new TextureComponent(highscoresprite, Gdx.graphics.getWidth() / 2 - (highscoresprite.getWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)3.8, highscoresprite.getRegionWidth() * scaleX, highscoresprite.getRegionHeight() * scaleY, false, false))
                .add(new ButtonComponent(Gdx.graphics.getWidth() / 2 - (highscoresprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)3.8, highscoresprite.getRegionWidth() * scaleX, highscoresprite.getRegionHeight() * scaleY))
                .add(new TransformComponent(Gdx.graphics.getWidth() / 2 - (highscoresprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)3.8));
        engine.addEntity(highscoreButton);

        // ***** Highscore button END *****

        // ***** Option button START *****

        optionsprite = new Sprite(option);
        //optionsprite.setBounds(Gdx.graphics.getWidth() / 2 - (optionsprite.getWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)5.6, optionsprite.getRegionWidth() * scaleX, optionsprite.getRegionHeight() * scaleY);

        optionButton = new Entity();

        optionButton.add(new TextureComponent(optionsprite, Gdx.graphics.getWidth() / 2 - (optionsprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)5.6, optionsprite.getRegionWidth() * scaleX, optionsprite.getRegionHeight() * scaleY, false, false))
                .add(new ButtonComponent(Gdx.graphics.getWidth() / 2 - (optionsprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)5.6, optionsprite.getRegionWidth() * scaleX, optionsprite.getRegionHeight() * scaleY))
                .add(new TransformComponent(Gdx.graphics.getWidth() / 2 - (optionsprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)5.6));
        engine.addEntity(optionButton);

        // ***** Option button END *****

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
