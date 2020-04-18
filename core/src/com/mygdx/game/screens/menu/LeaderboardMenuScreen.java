package com.mygdx.game.screens.menu;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
    private TextureRegion nampac;

    private TextureRegion ellipse;
    private TextureRegion front_ellipse;

    private Entity singlePlayerGhostsButton;
    private Entity singlePlayerNampacButton;
    private Entity multiplayerGhostsButton;
    private Entity multiplayerNampacButton;

    private Entity ellipseEntity;
    private Entity front_ellipseEntity;

    private SpriteBatch batch;

    private Sprite singlePlayerGhostsSprite;
    private Sprite singlePlayerNampacSprite;
    private Sprite multiplayerGhostsSprite;
    private Sprite multiplayerNampacSprite;

    private Sprite ellipseSprite;
    private Sprite front_ellipseSprite;

    private ButtonSystem buttonSystem;
    private RenderingSystem renderSystem;

    private Engine engine;
    private float scaleX;
    private float scaleY;

    private static String LEADERBOARD_MENU_DIRECTORY = "leaderboardmenuscreen/";

    public LeaderboardMenuScreen(final InversePacman app) {
        super(app);
        scaleX = Gdx.graphics.getWidth() / (float)app.APP_WIDTH_MOBILE;
        scaleY = Gdx.graphics.getHeight() / (float)app.APP_HEIGHT_MOBILE;

        bg = new TextureRegion(new Texture(LEADERBOARD_MENU_DIRECTORY + "leaderboardcorrectiswear.png"));
        ghosts = new TextureRegion(new Texture(LEADERBOARD_MENU_DIRECTORY + "ghosts_button.png"));
        nampac = new TextureRegion(new Texture(LEADERBOARD_MENU_DIRECTORY + "nampac_button.png"));

        ellipse = new TextureRegion(new Texture("menuscreen/ellipse_color_change_correct.png"));
        front_ellipse = new TextureRegion(new Texture("optionscreen/option_front_ellipse.png"));
    }

    private void handleInput() {
        if (singlePlayerGhostsButton.flags == 1) {
            app.gsm.setScreen(GameScreenManager.STATE.SINGLE_PLAYER_GHOSTS_BOARD_SCREEN);
        }

        if (singlePlayerNampacButton.flags == 1) {
            app.gsm.setScreen(GameScreenManager.STATE.SINGLE_PLAYER_NAMPAC_BOARD_SCREEN);
        }

        if (multiplayerGhostsButton.flags == 1) {
            app.gsm.setScreen(GameScreenManager.STATE.MULTIPLAYER_GHOSTS_BOARD_SCREEN);
        }

        if (multiplayerGhostsButton.flags == 1) {
            app.gsm.setScreen(GameScreenManager.STATE.MULTIPLAYER_NAMPAC_BOARD_SCREEN);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);


        //batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0.2f, 0.2f, 0.1f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(this.bg, 0, 0, Gdx.graphics.getWidth() / 32f, Gdx.graphics.getHeight() / 32f);
        //singlePlayerGhostsSprite.draw(batch);
        //singlePlayerNampacSprite.draw(batch);
        //multiplayerGhostsSprite.draw(batch);
        //multiplayerNampacSprite.draw(batch);


        batch.end();
        engine.update(delta);
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
        ellipseEntity.add(new TextureComponent(ellipseSprite, (Gdx.graphics.getWidth() / 2 - (ellipse.getRegionWidth() / 2 * (scaleX))), (Gdx.graphics.getHeight() - (ellipse.getRegionHeight() * (scaleY))), (ellipse.getRegionWidth() * (scaleX)), (ellipse.getRegionHeight() * (scaleY)), true, true))
                .add(new TransformComponent(Gdx.graphics.getWidth() / 2 - (ellipse.getRegionWidth() / 2 * (scaleX)), Gdx.graphics.getHeight() - (ellipse.getRegionHeight() * (scaleY))));
        engine.addEntity(ellipseEntity);


        front_ellipseSprite = new Sprite(front_ellipse);

        front_ellipseEntity = new Entity();
        front_ellipseEntity.add(new TextureComponent(front_ellipseSprite, Gdx.graphics.getWidth() / 2 - (front_ellipse.getRegionWidth() / 2 * (scaleX)), Gdx.graphics.getHeight() / (float)1.17, front_ellipse.getRegionWidth() * (scaleX), front_ellipse.getRegionHeight() * (scaleY), false, false))
                .add(new TransformComponent(Gdx.graphics.getWidth() / 2 - (front_ellipse.getRegionWidth() / 2 * (scaleX)), Gdx.graphics.getHeight() / (float)1.17));
        engine.addEntity(front_ellipseEntity);

        // Single Player ghosts button
        float firstYPosition = Gdx.graphics.getHeight();

        singlePlayerGhostsSprite = new Sprite(ghosts);
        singlePlayerGhostsButton = new Entity();
        addButton(singlePlayerGhostsSprite, singlePlayerGhostsButton, firstYPosition / 1.75f);

        // Single Player Nam-Pac button
        singlePlayerNampacSprite = new Sprite(nampac);
        singlePlayerNampacButton = new Entity();
        addButton(singlePlayerNampacSprite, singlePlayerNampacButton, firstYPosition / 2f);

        // Multiplayer ghosts button
        multiplayerGhostsSprite = new Sprite(ghosts);
        multiplayerGhostsButton = new Entity();
        addButton(multiplayerGhostsSprite, multiplayerGhostsButton, firstYPosition / 4f);

        // Multiplayer Nam-Pac button
        multiplayerNampacSprite = new Sprite(nampac);
        multiplayerNampacButton = new Entity();
        addButton(multiplayerNampacSprite, multiplayerNampacButton, firstYPosition / 5.6f);
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
        //sprite.setBounds(Gdx.graphics.getWidth()/ 2 - sprite.getRegionWidth() / 2,
                //y, sprite.getRegionWidth(), sprite.getRegionHeight());

        button.add(new TextureComponent(sprite, Gdx.graphics.getWidth()/ 2  - (sprite.getRegionWidth() / 2 * (scaleX)) ,
                y, sprite.getRegionWidth() * scaleX, sprite.getRegionHeight() * scaleY, false, false))
                .add(new ButtonComponent(Gdx.graphics.getWidth()/ 2  - (sprite.getRegionWidth() / 2 * (scaleX)) ,
                        y, sprite.getRegionWidth() * scaleX, sprite.getRegionHeight() * scaleY))
                .add(new TransformComponent(Gdx.graphics.getWidth()/ 2  - (sprite.getRegionWidth() / 2 * (scaleX)) ,
                        y));
        engine.addEntity(button);
    }

}
