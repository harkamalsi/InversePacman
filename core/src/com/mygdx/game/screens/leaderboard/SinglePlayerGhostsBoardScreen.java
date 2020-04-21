package com.mygdx.game.screens.leaderboard;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.InversePacman;
import com.mygdx.game.managers.GameScreenManager;
import com.mygdx.game.systems.ButtonSystem;
import com.mygdx.game.systems.RenderingSystem;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;

public class SinglePlayerGhostsBoardScreen extends AbstractBoardScreen {
    public SinglePlayerGhostsBoardScreen(final InversePacman app, Engine engine) {
        super(app, engine);
    }

    @Override
    public Array<PlayerScore> retrievePlayerScores() {
        Array<PlayerScore> scores = app.saveManager.loadDataValue("ghosts", Array.class);
        return scores;
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void setBackground() {
        this.bg = new TextureRegion(new Texture(AbstractBoardScreen.LEADERBOARD_DIRECTORY + "ghosts.png"));
    }

    @Override
    public void addEllipseSpriteEntity() {
        app.addSpriteEntity(ellipseSprite, ellipseEntity, engine, (Gdx.graphics.getWidth() / 2 - (ellipse.getRegionWidth() / 2 * (scaleX))), (Gdx.graphics.getHeight() - (ellipse.getRegionHeight() * (scaleY))), (ellipse.getRegionWidth() * (scaleX)), (ellipse.getRegionHeight() * (scaleY)), false, true, true, true, 0f, 200f, 151f);
    }

    @Override
    public String formatName(String name) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yy");
            LocalDateTime dateTime = LocalDateTime.parse(name);

            return dateTime.format(formatter);
        } catch (DateTimeParseException exception) {
            return "UNKNOWN DATE";
        }
    }

    @Override
    public String formatScore(int score) {
        int minutes = score / 60;
        int seconds = score % 60;

        String formattedScore = minutes + ":" + ((seconds > 0) ? seconds : "00");

        return formattedScore;
    }

    @Override
    public void update(float delta) {
        handleInput();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

}