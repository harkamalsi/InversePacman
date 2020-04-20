package com.mygdx.game.screens.leaderboard;

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
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.InversePacman;
import com.mygdx.game.managers.GameScreenManager;
import com.mygdx.game.screens.AbstractScreen;
import com.mygdx.game.systems.ButtonSystem;
import com.mygdx.game.systems.RenderingSystem;

import java.util.ArrayList;

public abstract class AbstractBoardScreen extends AbstractScreen {
    protected TextureRegion bg;

    protected static String LEADERBOARD_DIRECTORY = "leaderboard/";
    protected OrthographicCamera camera;
    protected FitViewport viewport;

    protected SpriteBatch batch;
    protected BitmapFont font;
    protected GlyphLayout layout;

    protected float scaleX;
    protected float scaleY;

    protected Entity ellipseEntity;
    protected Entity front_ellipseEntity;
    protected Entity backButton;

    protected Sprite ellipseSprite;
    protected Sprite front_ellipseSprite;
    protected Sprite backSprite;

    protected RenderingSystem renderSystem;
    protected ButtonSystem buttonSystem;
    protected Engine engine;

    protected TextureRegion ellipse;
    protected TextureRegion front_ellipse;
    protected TextureRegion back;

    public AbstractBoardScreen(InversePacman app) {
        super(app);
        scaleX = Gdx.graphics.getWidth() / (float)app.APP_WIDTH_MOBILE;
        scaleY = Gdx.graphics.getHeight() / (float)app.APP_HEIGHT_MOBILE;
        layout = new GlyphLayout(); //dont do this every frame! Store it as member
        font = new BitmapFont(Gdx.files.internal("font/rubik_font_correct.fnt"));

        ellipse = new TextureRegion(new Texture("menuscreen/Ellipse 11.png"));
        front_ellipse = new TextureRegion(new Texture("optionscreen/option_front_ellipse.png"));
        back = new TextureRegion(new Texture("back.png"));
        scaleX = Gdx.graphics.getWidth() / (float)app.APP_WIDTH_MOBILE;
        scaleY = Gdx.graphics.getHeight() / (float)app.APP_HEIGHT_MOBILE;

        setBackground();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        //batch.setProjectionMatrix(this.camera.combined);

        Gdx.gl.glClearColor(0.2f, 0.2f, 0.1f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        font.setUseIntegerPositions(false);
        batch.draw(this.bg, 0, 0, Gdx.graphics.getWidth() / 32f, Gdx.graphics.getHeight() / 32f);
        font.getData().setScale(scaleX / (32f * 1.2f), scaleY / (32f * 1.2f));

        ArrayList<PlayerScore> players = retrieveTopPlayerScores();
        drawNames(batch, font, players);
        drawScores(batch, font, players);

        batch.end();

        engine.update(delta);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void show() {
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.batch = new SpriteBatch();


        renderSystem = new RenderingSystem(batch);
        buttonSystem = new ButtonSystem(camera);

        engine = new Engine();
        engine.addSystem(renderSystem);
        engine.addSystem(buttonSystem);

        ellipseSprite = new Sprite(ellipse);
        ellipseEntity = new Entity();
        addEllipseSpriteEntity();

        front_ellipseSprite = new Sprite(front_ellipse);
        front_ellipseEntity = new Entity();
        app.addSpriteEntity(front_ellipseSprite, front_ellipseEntity, engine,Gdx.graphics.getWidth() / 2 - (front_ellipse.getRegionWidth() / 2 * (scaleX)), Gdx.graphics.getHeight() / (float)1.17, front_ellipse.getRegionWidth() * (scaleX), front_ellipse.getRegionHeight() * (scaleY), false, false, false, false);

        backSprite = new Sprite(back);
        backButton = new Entity();
        app.addSpriteEntity(backSprite, backButton, engine, 0, 0, backSprite.getRegionWidth(), backSprite.getRegionHeight(), true,false, false, false);
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

    public abstract ArrayList<PlayerScore> retrieveTopPlayerScores();
    public abstract String formatScore(int score);
    public abstract void addEllipseSpriteEntity();
    public abstract void setBackground();

    // Some classes will override this
    public String formatName(String name) {
        return name;
    }

    public void drawNames(SpriteBatch batch, BitmapFont font, ArrayList<PlayerScore> players) {
        layout.setText(font, formatName(players.get(0).name));
        font.draw(batch, layout, Gdx.graphics.getWidth() / (3.5f *32f) - layout.width / 2,Gdx.graphics.getHeight() / (1.575f * 32f) + layout.height / 2);
        layout.setText(font, formatName(players.get(1).name));
        font.draw(batch, layout, Gdx.graphics.getWidth() / (3.5f *32f) - layout.width / 2,Gdx.graphics.getHeight() / (1.8f * 32f) + layout.height / 2);
        layout.setText(font, formatName(players.get(2).name));
        font.draw(batch, layout, Gdx.graphics.getWidth() / (3.5f *32f) - layout.width / 2,Gdx.graphics.getHeight() / (2.1f * 32f) + layout.height / 2);
    }

    public void drawScores(SpriteBatch batch, BitmapFont font, ArrayList<PlayerScore> players) {
        layout.setText(font, formatScore(players.get(0).score));
        font.draw(batch, layout, Gdx.graphics.getWidth() / (1.3f *32f) - layout.width / 2,Gdx.graphics.getHeight() / (1.575f * 32f) + layout.height / 2);
        layout.setText(font, formatScore(players.get(1).score));
        font.draw(batch, layout, Gdx.graphics.getWidth() / (1.3f *32f) - layout.width / 2,Gdx.graphics.getHeight() / (1.8f * 32f) + layout.height / 2);
        layout.setText(font, formatScore(players.get(2).score));
        font.draw(batch, layout, Gdx.graphics.getWidth() / (1.3f *32f) - layout.width / 2,Gdx.graphics.getHeight() / (2.1f * 32f) + layout.height / 2);
    }

    public void handleInput() {
        if(backButton.flags == 1) {
            app.gsm.setScreen((GameScreenManager.STATE.LEADERBOARD_MENU_SCREEN));
        }
    }

    public class PlayerScore {
        private String name;
        private int score;

        public PlayerScore(String name, int score) {
            this.name = name;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setScore(int score) {
            this.score = score;
        }
    }

}