package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.InversePacman;

public abstract class AbstractScreen implements Screen {

    protected final InversePacman app;
    Stage stage;

    public AbstractScreen(final InversePacman app) {
        this.app = app;
        this.stage = new Stage();
    }

    public abstract void update(float delta);

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(1, 0f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        this.stage.dispose();
    }
}