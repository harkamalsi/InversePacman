package com.mygdx.game.screens;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.InversePacman;

public abstract class AbstractScreen implements Screen {

    protected final InversePacman app;
    Stage stage;
    private Engine engine;

    public AbstractScreen(final InversePacman app, Engine engine) {
        this.app = app;
        this.stage = new Stage();
        this.engine = engine;
    }

    public abstract void update(float delta);

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(33/255f, 32/255f, 49/255f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        this.stage.dispose();
        engine.removeAllEntities();
        for (EntitySystem system : engine.getSystems()){
            engine.removeSystem(system);
        }

    }
}
