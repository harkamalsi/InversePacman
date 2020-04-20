package com.mygdx.game.screens.menu;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
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
import com.mygdx.game.systems.MusicSystem;
import com.mygdx.game.systems.RenderingSystem;


public class LeaderboardMenuScreen extends AbstractScreen {
    private OrthographicCamera camera;
    private FitViewport viewport;

    private TextureRegion bg;
    private TextureRegion ghosts;
    private TextureRegion nampac;

    private TextureRegion ellipse;
    private TextureRegion front_ellipse;

    private Entity singlePlayerGhostsButton;
    private Entity singlePlayerNampacButton;
    private Entity multiplayerGhostsButton;
    private Entity multiplayerNampacButton;
    private Entity ellipseEntity;
    private Entity front_ellipseEntity;
    private Entity bgEntity;

    private SpriteBatch batch;
    private SpriteBatch bgBatch;

    private Sprite singlePlayerGhostsSprite;
    private Sprite singlePlayerNampacSprite;
    private Sprite multiplayerGhostsSprite;
    private Sprite multiplayerNampacSprite;
    private Sprite ellipseSprite;
    private Sprite front_ellipseSprite;
    private Sprite bgSprite;

    private float scaleX;
    private float scaleY;

    private BitmapFont font;
    private GlyphLayout layout;

    private RenderingSystem renderingSystem;
    private ButtonSystem buttonSystem;
    private MusicSystem musicSystem;

    private Engine engine;

    private static String LEADERBOARD_MENU_DIRECTORY = "leaderboardmenuscreen/";

    public LeaderboardMenuScreen(final InversePacman app) {
        super(app);

        bg = new TextureRegion(new Texture(LEADERBOARD_MENU_DIRECTORY + "leaderboard_menu_bg.png"));
        ghosts = new TextureRegion(new Texture(LEADERBOARD_MENU_DIRECTORY + "ghosts_button.png"));
        nampac = new TextureRegion(new Texture(LEADERBOARD_MENU_DIRECTORY + "nampac_button.png"));

        ellipse = new TextureRegion(new Texture("menuscreen/ellipse_color_change_correct.png"));
        front_ellipse = new TextureRegion(new Texture("optionscreen/option_front_ellipse.png"));

        font = new BitmapFont(Gdx.files.internal("font/rubik_font_correct.fnt"));
        layout = new GlyphLayout(); //dont do this every frame! Store it as member

        scaleX = Gdx.graphics.getWidth() / (float)app.APP_WIDTH_MOBILE;
        scaleY = Gdx.graphics.getHeight() / (float)app.APP_HEIGHT_MOBILE;
    }

    public void handleInput() {
        if (singlePlayerGhostsButton.flags == 1) {
            musicSystem.dispose();
            app.gsm.setScreen(GameScreenManager.STATE.SINGLE_PLAYER_GHOSTS_BOARD_SCREEN);
        }

        if (singlePlayerNampacButton.flags == 1) {
            musicSystem.dispose();
            app.gsm.setScreen(GameScreenManager.STATE.SINGLE_PLAYER_NAMPAC_BOARD_SCREEN);
        }

        if (multiplayerGhostsButton.flags == 1) {
            musicSystem.dispose();
            app.gsm.setScreen(GameScreenManager.STATE.MULTIPLAYER_GHOSTS_BOARD_SCREEN);
        }

        if (multiplayerGhostsButton.flags == 1) {
            musicSystem.dispose();
            app.gsm.setScreen(GameScreenManager.STATE.MULTIPLAYER_NAMPAC_BOARD_SCREEN);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        this.engine.update(delta);

        batch.begin();
        font.setUseIntegerPositions(false);
        font.getData().setScale(scaleX / 40f, scaleY / 40f);
        layout.setText(font, "HIGHSCORE");

        font.draw(batch, "highscore", (Gdx.graphics.getWidth() / 64f - layout.width / 2f), (Gdx.graphics.getHeight() / (1.05f * 32f) - (layout.height / 2f)));

        batch.end();
    }

    @Override
    public void update(float delta) {
        handleInput();
    }

    @Override
    public void show() {
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), this.camera);
        //this.camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        this.batch = new SpriteBatch();
        this.bgBatch = new SpriteBatch();

        ellipseSprite = new Sprite(ellipse);

        buttonSystem = new ButtonSystem(this.camera);
        renderingSystem = new RenderingSystem(batch);
        musicSystem = new MusicSystem(Gdx.files.internal("music/menu"));

        engine = new Engine();
        engine.addSystem(buttonSystem);
        engine.addSystem(musicSystem);
        engine.addSystem(renderingSystem);

        // Essentially duplicating logic from MainMenuScreen.
        // This should be pulled up into a parent class.
        bgSprite = new Sprite(bg);
        bgEntity = new Entity();
        bgEntity.add(new TextureComponent(bgSprite, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, false))
                .add(new TransformComponent(0, 0));
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

        // Single Player ghosts button
        float firstYPosition = Gdx.graphics.getHeight() / 1.75f;

        singlePlayerGhostsSprite = new Sprite(ghosts);
        singlePlayerGhostsButton = new Entity();
        addButton(singlePlayerGhostsSprite, singlePlayerGhostsButton, firstYPosition);

        // Single Player Nam-Pac button
        singlePlayerNampacSprite = new Sprite(nampac);
        singlePlayerNampacButton = new Entity();
        addButton(singlePlayerNampacSprite, singlePlayerNampacButton, firstYPosition - 60);

        // Multiplayer ghosts button
        multiplayerGhostsSprite = new Sprite(ghosts);
        multiplayerGhostsButton = new Entity();
        addButton(multiplayerGhostsSprite, multiplayerGhostsButton, firstYPosition - 275);

        // Multiplayer Nam-Pac button
        multiplayerNampacSprite = new Sprite(nampac);
        multiplayerNampacButton = new Entity();
        addButton(multiplayerNampacSprite, multiplayerNampacButton, firstYPosition - 335);
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

    private void addButton(Sprite sprite, Entity button, float y) {
        sprite.setBounds(Gdx.graphics.getWidth()/ 2 - sprite.getRegionWidth() / 2,
                y, sprite.getRegionWidth(), sprite.getRegionHeight());

        button.add(new TextureComponent(sprite, Gdx.graphics.getWidth() / 2 - (sprite.getWidth() / 2 * scaleX), y, sprite.getRegionWidth() * scaleX, sprite.getRegionHeight() * scaleY, false, false))
                .add(new ButtonComponent(Gdx.graphics.getWidth() / 2 - (sprite.getRegionWidth() / 2 * scaleX),
                        y, sprite.getRegionWidth() * scaleX, sprite.getRegionHeight() * scaleY))
                .add(new TransformComponent(Gdx.graphics.getWidth() / 2 - (sprite.getRegionWidth() / 2 * scaleX),
                        y));
        this.engine.addEntity(button);
    }

}
