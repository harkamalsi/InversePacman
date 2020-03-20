package com.mygdx.game.screens.play;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.mygdx.game.InversePacman;
import com.mygdx.game.screens.AbstractScreen;


public final class PlayScreen extends AbstractScreen {

    private OrthographicCamera camera;

    public BitmapFont font = new BitmapFont(); //or use alex answer to use custom font

    public PlayScreen(final InversePacman app) {
        super(app);

        // Sets the camera; width and height.
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, InversePacman.V_WIDTH, InversePacman.V_HEIGHT);

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void show() {

    }


    // Render the PlayScreen, for now only a picture with green background. This method i
    // needed in every screen but can be changed to show different data.
    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, 1, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        app.batch.begin();
        app.batch.draw(app.img, 0, 0);
        app.batch.end();
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
}
