package com.mygdx.game.screens.leaderboard;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.InversePacman;
import com.mygdx.game.screens.AbstractScreen;

import java.util.ArrayList;

public abstract class AbstractBoardScreen extends AbstractScreen {
    protected TextureRegion bg;

    protected static String LEADERBOARD_DIRECTORY = "leaderboard/";
    private OrthographicCamera camera;
    private FitViewport viewport;

    private SpriteBatch batch;
    private BitmapFont font;

    public AbstractBoardScreen(InversePacman app, Engine engine) {
        super(app, engine);
        font = new BitmapFont();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        batch.setProjectionMatrix(this.camera.combined);

        Gdx.gl.glClearColor(0.2f, 0.2f, 0.1f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(this.bg, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        font.getData().setScale(1.5f);

        ArrayList<PlayerScore> players = retrieveTopPlayerScores();
        drawNames(batch, font, players);
        drawScores(batch, font, players);

        batch.end();
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void show() {
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(InversePacman.V_WIDTH, InversePacman.V_HEIGHT, this.camera);
        this.camera.setToOrtho(false, this.viewport.getWorldWidth(), this.viewport.getWorldHeight());
        this.batch = new SpriteBatch();
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

    private void drawNames(SpriteBatch batch, BitmapFont font, ArrayList<PlayerScore> players) {
        font.draw(batch, players.get(0).name, 40f, 280f);
        font.draw(batch, players.get(1).name, 40f, 246f);
        font.draw(batch, players.get(2).name, 40f, 214f);
    }

    private void drawScores(SpriteBatch batch, BitmapFont font, ArrayList<PlayerScore> players) {
        font.draw(batch, players.get(0).score, viewport.getWorldWidth() - 260f, 280f);
        font.draw(batch, players.get(1).score, viewport.getWorldWidth() - 250f, 246f);
        font.draw(batch, players.get(2).score, viewport.getWorldWidth() - 260f, 214f);
    }

    public class PlayerScore {
        private String name;
        private String score;

        public PlayerScore(String name, String score) {
            this.name = name;
            this.score = score;
        }

        public String getName() {
            return name;
        }

        public String getScore() {
            return score;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setScore(String score) {
            this.score = score;
        }
    }

}
