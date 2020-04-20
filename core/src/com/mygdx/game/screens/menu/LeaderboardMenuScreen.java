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


public class LeaderboardMenuScreen extends AbstractScreen {
    private OrthographicCamera camera;
    private FitViewport viewport;

    private TextureRegion bg;
    private TextureRegion ghosts;
    private TextureRegion nampac;

    private Entity singlePlayerGhostsButton;
    private Entity singlePlayerNampacButton;
    private Entity multiplayerGhostsButton;
    private Entity multiplayerNampacButton;

    private SpriteBatch batch;

    private Sprite singlePlayerGhostsSprite;
    private Sprite singlePlayerNampacSprite;
    private Sprite multiplayerGhostsSprite;
    private Sprite multiplayerNampacSprite;

    private ButtonSystem buttonSystem;

    private Engine engine;

    private static String LEADERBOARD_MENU_DIRECTORY = "leaderboardmenuscreen/";

    public LeaderboardMenuScreen(final InversePacman app, Engine engine) {
        super(app, engine);

        bg = new TextureRegion(new Texture(LEADERBOARD_MENU_DIRECTORY + "leaderboard_menu_bg.png"));
        ghosts = new TextureRegion(new Texture(LEADERBOARD_MENU_DIRECTORY + "ghosts_button.png"));
        nampac = new TextureRegion(new Texture(LEADERBOARD_MENU_DIRECTORY + "nampac_button.png"));
    }

    public void handleInput() {
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

        this.engine.update(delta);
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0.2f, 0.2f, 0.1f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(this.bg, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        singlePlayerGhostsSprite.draw(batch);
        singlePlayerNampacSprite.draw(batch);
        multiplayerGhostsSprite.draw(batch);
        multiplayerNampacSprite.draw(batch);

        batch.end();
    }

    @Override
    public void update(float delta) {
        handleInput();
    }

    @Override
    public void show() {
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(InversePacman.V_WIDTH, InversePacman.V_HEIGHT, this.camera);
        //this.camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        this.camera.setToOrtho(false, this.viewport.getWorldWidth(), this.viewport.getWorldHeight());

        this.batch = new SpriteBatch();

        buttonSystem = new ButtonSystem(this.camera);

        engine = new Engine();
        engine.addSystem(buttonSystem);

        // Single Player ghosts button
        float firstYPosition = viewport.getWorldHeight() / 1.75f;

        singlePlayerGhostsSprite = new Sprite(ghosts);
        singlePlayerGhostsButton = new Entity();
        addButton(singlePlayerGhostsSprite, singlePlayerGhostsButton, firstYPosition);

        // Single Player Nam-Pac button
        singlePlayerNampacSprite = new Sprite(nampac);
        singlePlayerNampacButton = new Entity();
        addButton(singlePlayerNampacSprite, singlePlayerNampacButton, firstYPosition - 40);

        // Multiplayer ghosts button
        multiplayerGhostsSprite = new Sprite(ghosts);
        multiplayerGhostsButton = new Entity();
        addButton(multiplayerGhostsSprite, multiplayerGhostsButton, firstYPosition - 138);

        // Multiplayer Nam-Pac button
        multiplayerNampacSprite = new Sprite(nampac);
        multiplayerNampacButton = new Entity();
        addButton(multiplayerNampacSprite, multiplayerNampacButton, firstYPosition - 178);
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
        sprite.setBounds(viewport.getWorldWidth()/ 2 - sprite.getRegionWidth() / 2,
                y, sprite.getRegionWidth(), sprite.getRegionHeight());

        button.add(new TextureComponent(sprite))
                .add(new ButtonComponent(viewport.getWorldWidth() / 2 - sprite.getRegionWidth() / 2,
                        y, sprite.getRegionWidth(), sprite.getRegionHeight()))
                .add(new TransformComponent(viewport.getWorldWidth() / 2 - sprite.getRegionWidth() / 2,
                        y));
        this.engine.addEntity(button);
    }

}
