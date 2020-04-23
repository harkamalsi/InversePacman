package com.mygdx.game.screens.play;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
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
import com.mygdx.game.components.TableComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.managers.EntityManager;
import com.mygdx.game.managers.GameScreenManager;
import com.mygdx.game.managers.NetworkManager;
import com.mygdx.game.screens.AbstractScreen;
import com.mygdx.game.systems.ButtonSystem;
import com.mygdx.game.systems.MusicSystem;
import com.mygdx.game.systems.RenderingSystem;
import com.mygdx.game.systems.TableSystem;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import static com.mygdx.game.screens.play.PlayScreen.MULTIPLAYER;

public class LobbyScreen extends AbstractScreen {
    private OrthographicCamera camera;
    private FitViewport viewport;

    private EntityManager entityManager;
    private NetworkManager networkManager = InversePacman.NETWORKMANAGER;
    public static String LOBBY_JOINED;

    private SpriteBatch batch;
    private SpriteBatch bgBatch;

    private Engine engine;
    private Vector3 clickPosition;

    private ButtonSystem buttonSystem;
    private TableSystem tableSystem;
    private RenderingSystem renderingSystem;
    private MusicSystem musicSystem;

    private TableComponent table;

    private TextureRegion createLobby;
    private TextureRegion multiplay;
    private TextureRegion highscore;
    private TextureRegion option;
    private TextureRegion bg;

    private TextureRegion ellipse;
    private TextureRegion front_ellipse;

    private Entity joinLobbyButton;
    private Entity createLobbyButton;
    private Entity multiplayerButton;
    private Entity highscoreButton;
    private Entity optionButton;
    private Entity ellipseEntity;
    private Entity front_ellipseEntity;
    private Entity bgEntity;
    private Entity tbEntity;


    private Sprite createLobbySprite;
    private Sprite multisprite;
    private Sprite highscoresprite;
    private Sprite optionsprite;
    private Sprite ellipseSprite;
    private Sprite front_ellipseSprite;
    private Sprite bgSprite;



    private float scaleX;
    private float scaleY;

    private BitmapFont font;
    private GlyphLayout layout;

    public LobbyScreen(final InversePacman app, Engine engine) {
        super(app, engine);
        this.engine = engine;

        /*this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, InversePacman.V_WIDTH, InversePacman.V_HEIGHT);*/
        //networkManager = new NetworkManager();
        table = new TableComponent(networkManager);

        bg = new TextureRegion(new Texture("lobbyscreen/lobbyScreen.png"));
        createLobby = new TextureRegion(new Texture("lobbyscreen/createLobbyButton.png"));
        multiplay = new TextureRegion(new Texture("menuscreen/multi.png"));
        highscore = new TextureRegion(new Texture("menuscreen/high.png"));
        option = new TextureRegion(new Texture("menuscreen/options.png"));

        ellipse = new TextureRegion(new Texture("menuscreen/ellipse_color_change_correct.png"));
        front_ellipse = new TextureRegion(new Texture("optionscreen/option_front_ellipse.png"));

        font = new BitmapFont(Gdx.files.internal("font/rubik_font_correct.fnt"));
        layout = new GlyphLayout(); //dont do this every frame! Store it as member

        scaleX = (Gdx.graphics.getWidth() / (float)app.APP_WIDTH_MOBILE) * 0.8f;
        scaleY = (Gdx.graphics.getHeight() / (float)app.APP_HEIGHT_MOBILE) * 0.8f;
    }

    @Override
    public void render(float delta){
        super.render(delta);
        //engine.update(delta);
        batch.begin();
        batch.draw(bg, 0,0, Gdx.graphics.getWidth() / 32f, Gdx.graphics.getHeight() / 32f);
        batch.end();
        engine.update(delta);
    }



    @Override
    public void update(float delta) {
        handleInput();

    }

    public void handleInput() {
        String nickname = "PepsiCoke";
        String playerType = "GHOST";
        String joinLobbyName = "Lobby1";

        if(createLobbyButton.flags == 1) {
            System.out.println("CreateLobbyButton pressed but lobby not created!");

            if (table.createLobby) {
                System.out.println("Create Lobby Called!");
                networkManager.createLobby(nickname, playerType);
                MULTIPLAYER = true;

                String lobby = networkManager.getLobby();
                while (lobby == null) {
                    lobby = networkManager.getLobby();
                }
                LobbyScreen.LOBBY_JOINED = lobby;
                app.gsm.setScreen(GameScreenManager.STATE.PLAY);
            } 
            createLobbyButton.flags = 0;
        }

    }

    @Override
    public void show() {
        this.camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch = new SpriteBatch();
        bgBatch = new SpriteBatch();

        ellipseSprite = new Sprite(ellipse);

        buttonSystem = new ButtonSystem(camera);
        tableSystem = new TableSystem(this.app);
        renderingSystem = new RenderingSystem(batch);
        musicSystem = new MusicSystem(Gdx.files.internal("music/menu"));

        engine = new Engine();
        engine.addSystem(buttonSystem);
        engine.addSystem(tableSystem);
        engine.addSystem(musicSystem);
        engine.addSystem(renderingSystem);

        bgSprite = new Sprite(bg);

        bgEntity = new Entity();

        //Don't know why but the background doesn't surround the whole screen, therefore I added some +/- on the parameters
        bgEntity.add(new TextureComponent(bgSprite, 0, -2, Gdx.graphics.getWidth() + 2,
                Gdx.graphics.getHeight() + 2,false,false,false))
                .add(new TransformComponent(0,0));
        //engine.addEntity(bgEntity);

        tbEntity = new Entity();
        tbEntity.add(table);
        engine.addEntity(tbEntity);

        createLobbySprite = new Sprite(createLobby);

        createLobbyButton = new Entity();
        createLobbyButton.add(new TextureComponent(createLobbySprite,
                (Gdx.graphics.getWidth() / 2 - (createLobbySprite.getRegionWidth() / 2 * scaleX)),
                Gdx.graphics.getHeight() / 20f, createLobbySprite.getRegionWidth() * scaleX,
                createLobbySprite.getRegionHeight() * scaleY, false, false,false, 255, 0, 0))
                .add(new ButtonComponent((Gdx.graphics.getWidth() / 2 - (createLobbySprite.getRegionWidth() / 2 * scaleX)), Gdx.graphics.getHeight() / 20f, createLobbySprite.getRegionWidth() * scaleX, createLobbySprite.getRegionHeight() * scaleY))
                .add(new TransformComponent((Gdx.graphics.getWidth() / 2 - (createLobbySprite.getRegionWidth() / 2 * scaleX)), Gdx.graphics.getHeight() / 20f));

        engine.addEntity(createLobbyButton);
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