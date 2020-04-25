package com.mygdx.game.screens.menu;

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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.InversePacman;
import com.mygdx.game.components.ButtonComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.managers.GameScreenManager;
import com.mygdx.game.screens.AbstractScreen;
import com.mygdx.game.systems.ButtonSystem;
import com.mygdx.game.systems.RenderingSystem;


public class LeaderboardMenuScreen extends AbstractScreen {
    private OrthographicCamera camera;
    private FitViewport viewport;

    private TextureRegion bg;
    private TextureRegion ghosts;
    private TextureRegion namcap;

    private TextureRegion ellipse;
    private TextureRegion front_ellipse;
    private TextureRegion back;

    //private Entity singlePlayerGhostsButton;
    private Entity singlePlayerNamcapButton;
    private Entity multiplayerGhostsButton;
    private Entity multiplayerNamcapButton;

    private Entity ellipseEntity;
    private Entity front_ellipseEntity;
    private Entity backButton;

    private SpriteBatch batch;

    //private Sprite singlePlayerGhostsSprite;
    private Sprite singlePlayerNamcapSprite;
    private Sprite multiplayerGhostsSprite;
    private Sprite multiplayerNamcapSprite;

    private Sprite ellipseSprite;
    private Sprite front_ellipseSprite;
    private Sprite backSprite;

    private ButtonSystem buttonSystem;
    private RenderingSystem renderSystem;

    private Engine engine;
    private float scaleX;
    private float scaleY;

    private static String LEADERBOARD_MENU_DIRECTORY = "leaderboardmenuscreen/";

    private BitmapFont font;
    private GlyphLayout layout;

    public LeaderboardMenuScreen(final InversePacman app, Engine engine) {
        super(app, engine);
        scaleX = Gdx.graphics.getWidth() / (float)app.APP_WIDTH_MOBILE;
        scaleY = Gdx.graphics.getHeight() / (float)app.APP_HEIGHT_MOBILE;

        bg = new TextureRegion(new Texture(LEADERBOARD_MENU_DIRECTORY + "leaderboardcorrectiswear.png"));
        ghosts = new TextureRegion(new Texture(LEADERBOARD_MENU_DIRECTORY + "ghosts_button.png"));
        namcap = new TextureRegion(new Texture(LEADERBOARD_MENU_DIRECTORY + "nampac_button.png"));

        ellipse = new TextureRegion(new Texture("menuscreen/ellipse_color_change_correct.png"));
        front_ellipse = new TextureRegion(new Texture("optionscreen/option_front_ellipse.png"));
        back = new TextureRegion(new Texture("back3x.png"));

        font = new BitmapFont(Gdx.files.internal("font/rubik_font_correct.fnt"));
        layout = new GlyphLayout(); //dont do this every frame! Store it as member
        //System.out.println(this.engine.getSystems());
    }

    private void handleInput() {
//        if (singlePlayerGhostsButton.flags == 1) {
//            app.gsm.setScreen(GameScreenManager.STATE.SINGLE_PLAYER_GHOSTS_BOARD_SCREEN);
//        }

        if (singlePlayerNamcapButton.flags == 1) {
            app.gsm.setScreen(GameScreenManager.STATE.SINGLE_PLAYER_NAMCAP_BOARD_SCREEN);
        }

        if (multiplayerGhostsButton.flags == 1) {
            app.gsm.setScreen(GameScreenManager.STATE.MULTIPLAYER_GHOSTS_BOARD_SCREEN);
        }

        if (multiplayerNamcapButton.flags == 1) {
            app.gsm.setScreen(GameScreenManager.STATE.MULTIPLAYER_NAMCAP_BOARD_SCREEN);
        }

        if(backButton.flags == 1) {
            app.gsm.setScreen((GameScreenManager.STATE.MAIN_MENU_SCREEN));
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);


        //batch.setProjectionMatrix(camera.combined);

        batch.begin();
//
          batch.draw(this.bg, 0, 0, Gdx.graphics.getWidth() / 32f, Gdx.graphics.getHeight() / 32f);
//        font.setUseIntegerPositions(false);
//        font.getData().setScale(scaleX / 32f, scaleY / 32f);
//        layout.setText(font,"HIGHSCORES\nGHOSTS");
//
//        font.draw(batch,layout, (Gdx.graphics.getWidth() / 64f - layout.width / 2f),(Gdx.graphics.getHeight() / (1.05f * 32f) - (layout.height / 2f)));

        batch.end();
        engine.update(delta);

        batch.begin();
        font.setUseIntegerPositions(false);
        font.getData().setScale(scaleX / (32f * 1.2f), scaleY / (32f * 1.2f));
        layout.setText(font,"highscore");
        font.draw(batch,layout, (Gdx.graphics.getWidth() / 64f - layout.width / 2f),(Gdx.graphics.getHeight() / (1.05f * 32f) - (layout.height / 2f)));

        layout.setText(font, "offline");
        font.draw(batch,layout, (Gdx.graphics.getWidth() / 64f - layout.width / 2f),(Gdx.graphics.getHeight() / (1.35f * 32f) - (layout.height / 2f)));

        layout.setText(font, "online");
        font.draw(batch,layout, (Gdx.graphics.getWidth() / 64f - layout.width / 2f),(Gdx.graphics.getHeight() / (2.4f * 32f) - (layout.height / 2f)));
        batch.end();
    }

    @Override
    public void update(float delta) {
        handleInput();
    }

    @Override
    public void show() {
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //this.camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        this.batch = new SpriteBatch();

        buttonSystem = new ButtonSystem(this.camera);
        renderSystem = new RenderingSystem(batch);

        engine = new Engine();
        engine.addSystem(buttonSystem);
        engine.addSystem(renderSystem);

        ellipseSprite = new Sprite(ellipse);
        ellipseEntity = new Entity();
        app.addSpriteEntity(ellipseSprite, ellipseEntity, engine, (Gdx.graphics.getWidth() / 2 - (ellipse.getRegionWidth() / 2 * (scaleX))), (Gdx.graphics.getHeight() - (ellipse.getRegionHeight() * (scaleY))), (ellipse.getRegionWidth() * (scaleX)), (ellipse.getRegionHeight() * (scaleY)), false, true, true, false);


        front_ellipseSprite = new Sprite(front_ellipse);
        front_ellipseEntity = new Entity();
        app.addSpriteEntity(front_ellipseSprite, front_ellipseEntity, engine,Gdx.graphics.getWidth() / 2 - (front_ellipse.getRegionWidth() / 2 * (scaleX)), Gdx.graphics.getHeight() / (float)1.17, front_ellipse.getRegionWidth() * (scaleX), front_ellipse.getRegionHeight() * (scaleY), false, false, false, false);

        float firstYPosition = Gdx.graphics.getHeight();
        // Single Player ghosts button
//
//        singlePlayerGhostsSprite = new Sprite(ghosts);
//        singlePlayerGhostsButton = new Entity();
//        app.addSpriteEntity(singlePlayerGhostsSprite, singlePlayerGhostsButton, engine, Gdx.graphics.getWidth()/ 2  - (singlePlayerGhostsSprite.getRegionWidth() / 2 * (scaleX)) ,
//                firstYPosition / 1.75f, singlePlayerGhostsSprite.getRegionWidth() * scaleX, singlePlayerGhostsSprite.getRegionHeight() * scaleY, true,  false, false, false);


        // Single Player Nam-Pac button
        singlePlayerNamcapSprite = new Sprite(namcap);
        singlePlayerNamcapButton = new Entity();
        app.addSpriteEntity(singlePlayerNamcapSprite, singlePlayerNamcapButton, engine, Gdx.graphics.getWidth()/ 2  - (singlePlayerNamcapSprite.getRegionWidth() / 2 * (scaleX)) ,
                firstYPosition / 1.75f, singlePlayerNamcapSprite.getRegionWidth() * scaleX, singlePlayerNamcapSprite.getRegionHeight() * scaleY, true, false, false, false);

        // Multiplayer Nam-Pac button
        multiplayerNamcapSprite = new Sprite(namcap);
        multiplayerNamcapButton = new Entity();
        app.addSpriteEntity(multiplayerNamcapSprite, multiplayerNamcapButton, engine, Gdx.graphics.getWidth()/ 2  - (multiplayerNamcapSprite.getRegionWidth() / 2 * (scaleX)) ,
                firstYPosition / 4f, multiplayerNamcapSprite.getRegionWidth() * scaleX, multiplayerNamcapSprite.getRegionHeight() * scaleY, true, false, false, false);


        // Multiplayer ghosts button
        multiplayerGhostsSprite = new Sprite(ghosts);
        multiplayerGhostsButton = new Entity();
        app.addSpriteEntity(multiplayerGhostsSprite, multiplayerGhostsButton , engine, Gdx.graphics.getWidth()/ 2  - (multiplayerGhostsSprite.getRegionWidth() / 2 * (scaleX)) ,
                firstYPosition / 5.6f, multiplayerGhostsSprite.getRegionWidth() * scaleX, multiplayerGhostsSprite.getRegionHeight() * scaleY, true, false, false, false);

        backSprite = new Sprite(back);
        backButton = new Entity();
        app.addSpriteEntity(backSprite, backButton, engine, 0, 0, backSprite.getRegionWidth() *scaleX, backSprite.getRegionHeight()*scaleX, true,false, false, false);
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