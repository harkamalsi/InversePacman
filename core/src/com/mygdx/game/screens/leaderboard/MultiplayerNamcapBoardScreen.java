package com.mygdx.game.screens.leaderboard;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.InversePacman;
import com.mygdx.game.managers.GameScreenManager;
import com.mygdx.game.shared.Constants;
import com.mygdx.game.systems.ButtonSystem;
import com.mygdx.game.systems.RenderingSystem;

import java.util.ArrayList;

public class MultiplayerNamcapBoardScreen extends AbstractBoardScreen {
    public MultiplayerNamcapBoardScreen(final InversePacman app, Engine engine) {
        super(app, engine);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void addEllipseSpriteEntity() {
        app.addSpriteEntity(ellipseSprite, ellipseEntity, engine, (Gdx.graphics.getWidth() / 2 - (ellipse.getRegionWidth() / 2 * (scaleX))), (Gdx.graphics.getHeight() - (ellipse.getRegionHeight() * (scaleY))), (ellipse.getRegionWidth() * (scaleX)), (ellipse.getRegionHeight() * (scaleY)), false, true, true, true, 253f,181f,97f);
    }

    @Override
    public String formatScore(int score) {
        return score + " W";
    }

    @Override
    public void setBackground() {
        this.bg = new TextureRegion(new Texture(AbstractBoardScreen.LEADERBOARD_DIRECTORY + "namcap.png"));
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public String getScoreKey() {
        return Constants.MULTIPLAYER_SCORE_KEY;
    }

    @Override
    public int getScoresIndex() {
        return 0;
    }
}