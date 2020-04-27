package com.mygdx.game.screens.play;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.InversePacman;
import com.mygdx.game.components.ButtonComponent;
import com.mygdx.game.components.MusicComponent;
import com.mygdx.game.components.TableComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.managers.GameScreenManager;
import com.mygdx.game.multiplayermessage.MultiplayerMessage;
import com.mygdx.game.screens.AbstractScreen;
import com.mygdx.game.systems.ButtonSystem;
import com.mygdx.game.systems.MusicSystem;
import com.mygdx.game.systems.RenderingSystem;
import com.mygdx.game.systems.TableSystem;

import static com.mygdx.game.screens.play.PlayScreen.MULTIPLAYER;

public class LobbyScreen extends AbstractScreen {
    private OrthographicCamera camera;

    private MultiplayerMessage connection = MultiplayerMessage.getInstance();
    public static String LOBBY_JOINED;
    public static String NICKNAME;

    private SpriteBatch batch;

    private Engine engine;

    private ButtonSystem buttonSystem;
    private TableSystem tableSystem;
    private RenderingSystem renderingSystem;
    private MusicSystem musicSystem;

    private TableComponent table;

    private TextureRegion createLobby;
    private TextureRegion bg;

    private TextureRegion ellipse;
    private TextureRegion back;

    private Entity createLobbyButton;
    private Entity bgEntity;
    private Entity tbEntity;
    private Entity backButton;

    private Entity musicEntity;

    private Sprite createLobbySprite;
    private Sprite backSprite;
    private Sprite bgSprite;

    private float scaleX;
    private float scaleY;

    public LobbyScreen(final InversePacman app, Engine engine) {
        super(app, engine);
        this.engine = engine;
        table = new TableComponent(app.saveManager);

        bg = new TextureRegion(new Texture("lobbyscreen/lobbyScreen.png"));
        createLobby = new TextureRegion(new Texture("lobbyscreen/createLobbyButton.png"));

        ellipse = new TextureRegion(new Texture("menuscreen/ellipse_color_change_correct.png"));
        back = new TextureRegion(new Texture("playscreen/back2x.png"));

        scaleX = (Gdx.graphics.getWidth() / (float)app.APP_WIDTH_MOBILE) * 0.8f;
        scaleY = (Gdx.graphics.getHeight() / (float)app.APP_HEIGHT_MOBILE) * 0.8f;

        loadAndSetNickname();
    }

    private void loadAndSetNickname(){
        String tempNickname = app.saveManager.loadDataValue("nickname", String.class);
        if (tempNickname != null || tempNickname.length() !=0) {
            NICKNAME = tempNickname;
        }
    }

    @Override
    public void render(float delta){
        super.render(delta);
        batch.begin();
        batch.draw(bg, 0,0, Gdx.graphics.getWidth() / 32f, Gdx.graphics.getHeight() / 32f);
        batch.end();
        engine.update(delta);
    }

    @Override
    public void update(float delta) {
        handleInput();
        if(tableSystem.startsignal == 1) {
            engine.removeAllEntities();
            musicSystem.dispose();
            tableSystem.startGame();
        }
    }

    public void handleInput() {
        if(createLobbyButton.flags == 1) {
            if (table.createLobby) {
                connection.createLobby(NICKNAME, TableComponent.PLAYERTYPE);
            } 
            createLobbyButton.flags = 0;
        }

        if(backButton.flags == 1) {
            engine.removeAllEntities();
            musicSystem.dispose();

            if(MULTIPLAYER){
                connection.leaveLobby();
                LobbyScreen.LOBBY_JOINED = null;
                MULTIPLAYER = false;
            }

            app.gsm.setScreen(GameScreenManager.STATE.MAIN_MENU_SCREEN);
        }

    }

    @Override
    public void show() {
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch = new SpriteBatch();

        buttonSystem = new ButtonSystem(camera);
        tableSystem = new TableSystem(app);
        renderingSystem = new RenderingSystem(batch);
        musicSystem = new MusicSystem();

        engine = new Engine();
        engine.addSystem(buttonSystem);
        engine.addSystem(tableSystem);
        engine.addSystem(musicSystem);
        engine.addSystem(renderingSystem);

        bgSprite = new Sprite(bg);
        bgEntity = new Entity();
        bgEntity.add(new TextureComponent(bgSprite, 0, -2, Gdx.graphics.getWidth() + 2,
                Gdx.graphics.getHeight() + 2,false,false,false))
                .add(new TransformComponent(0,0));

        backSprite = new Sprite(back);
        backButton = new Entity();
        app.addSpriteEntity(backSprite, backButton, engine, 20f, 50 * 32 * scaleX/ 0.67f,
                backSprite.getRegionWidth() * scaleX, backSprite.getRegionHeight() * scaleX,
                true,false, false, false);

        tbEntity = new Entity();
        table = new TableComponent(app.saveManager);
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

        musicEntity = new Entity();
        musicEntity.add(new MusicComponent(Gdx.files.internal("music/menu")));
        engine.addEntity(musicEntity);
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