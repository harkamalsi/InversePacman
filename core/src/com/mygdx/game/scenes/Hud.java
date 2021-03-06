package com.mygdx.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.screens.play.PlayScreen;

public class Hud implements Disposable {
    // Skin for the table, that uses our font
    private Skin skin;

    // Table
    private  Table table;

    // Stage and viewport for HUD
    public Stage stage;
    private Viewport viewport;

    // Player score/time tracking
    private float timer;
    private static Integer score;
    public static Integer remainingLives;

    // Scene2D widgets
    private Label timerText;
    private static Label scoreText;
    private static Label remainingLivesText;
    private Label timerLabel;
    private Label scoreLabel;
    private Label remainingLivesLabel;
    private Label filler1;
    private Label filler2;
    private Label hintText;
    private BitmapFont font;

    private float hintTimer = 10;

    private static final int DEFAULT_LIVES = 3;

    public Hud(SpriteBatch sb) {
        timer = 0f;
        score = 0;
        
        font = new BitmapFont(Gdx.files.internal("font/rubik_font_correct.fnt"));
        font.getData().setScale(PlayScreen.scaleX * 0.75f);

        BitmapFont hintFont = new BitmapFont(Gdx.files.internal("font/rubik_font_correct.fnt"));
        hintFont.getData().setScale(PlayScreen.scaleX * .5f);

        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, sb);
        skin = new Skin(Gdx.files.internal("font/uiskin.json"));
        skin.getFont("default-font").getData().setScale(0.5f, 0.5f);

        table = new Table(skin);
        table.top();
        table.setFillParent(true);

        timerText = new Label(String.format("%03d", (int) timer), new Label.LabelStyle(font, Color.WHITE));
        remainingLivesText = new Label(String.format("%d", remainingLives), new Label.LabelStyle(font, Color.WHITE));
        scoreText = new Label(String.format("%06d", score), new Label.LabelStyle(font, Color.WHITE));
        timerLabel = new Label("TIME", new Label.LabelStyle(font, Color.WHITE));
        scoreLabel = new Label("SCORE", new Label.LabelStyle(font, Color.WHITE));
        remainingLivesLabel = new Label("LIVES", new Label.LabelStyle(font, Color.WHITE));
        filler1 = new Label("filler", new Label.LabelStyle(font, Color.WHITE));
        filler2 = new Label("filler", new Label.LabelStyle(font, Color.WHITE));

        //
        hintText = new Label("Move Namcap to collect all\npills without colliding\nwith the ghosts!", new Label.LabelStyle(hintFont, Color.WHITE));
        //hintText = new Label("Move Namcap", new Label.LabelStyle(font, Color.WHITE));
        filler1.setVisible(false);
        filler2.setVisible(false);
        table.add(filler1).expandX().padTop(10);
        table.add(scoreLabel).expandX().padTop(10);
        table.add(filler2).expandX().padTop(10);

        table.row();
        table.add(filler1).expandX().padTop(10);
        table.add(scoreText).expandX();
        table.add(filler2).expandX().padTop(10);

        table.row();
        table.add(filler1);
        table.row();
        table.row();
        table.add(timerLabel).expandX().padTop(10);
        table.add(filler1);
        table.add(remainingLivesLabel).expandX().padTop(10);
        table.row();
        table.add(timerText).expandX();
        table.add(filler1);
        table.add(remainingLivesText).expandX();

        table.row();
        table.add(hintText).colspan(3).center().padTop(90);

        stage.addActor(table);
    }

    public void update(float dt) {
        if(PlayScreen.pause) {
            table.setVisible(false);
        }
        if(!PlayScreen.pause) {
            table.setVisible(true);
            timer += dt;
        }

        if (hintTimer > 0) {
            hintTimer -= dt;
        } else {
            hintText.setVisible(false);
        }
        timerText.setText(String.format("%03d", (int)timer));
    }

    public static void addPointsToScore(int points) {
        score += points;
        scoreText.setText(String.format("%06d", score));
    }

    public static void setLives(int lives) {
        remainingLives = lives;
        remainingLivesText.setText(String.format("%d", remainingLives));
    }

    public int getScore() {
        return score;
    }


    public void reset() {
        score = 0;
        timer = 0;
        timerText.setText(String.format("%03d", (int)timer));
        scoreText.setText(String.format("%06d", score));



    }
    @Override
    public void dispose() {
        stage.dispose();
    }
}
