package com.mygdx.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Hud implements Disposable {
    // Stage and viewport for HUD
    public Stage stage;
    private Viewport viewport;

    // Player score/time tracking
    private float timer;
    private static Integer score;
    private Integer remainingLives;

    // Scene2D widgets
    private Label timerText;
    private static Label scoreText;
    private Label remainingLivesText;
    private Label timerLabel;
    private Label scoreLabel;
    private Label remainingLivesLabel;

    private static final int DEFAULT_LIVES = 3;

    public Hud(SpriteBatch sb) {
        timer = 0f;
        score = 0;
        remainingLives = DEFAULT_LIVES;

        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        timerText = new Label(String.format("%03d", (int) timer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        remainingLivesText = new Label(String.format("%d", remainingLives), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreText = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timerLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label("SCORE", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        remainingLivesLabel = new Label("LIVES", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(timerLabel).expandX().padTop(10);
        table.add(remainingLivesLabel).expandX().padTop(10);
        table.add(scoreLabel).expandX().padTop(10);

        table.row();
        table.add(timerText).expandX();
        table.add(remainingLivesText).expandX();
        table.add(scoreText).expandX();

        stage.addActor(table);
    }

    public void update(float dt) {
        timer += dt;
        timerText.setText(String.format("%03d", (int)timer));
    }

    public static void addPointsToScore(int points) {
        score += points;
        scoreText.setText(String.format("%06d", score));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
