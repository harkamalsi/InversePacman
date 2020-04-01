package com.mygdx.game.screens.leaderboard;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.mygdx.game.InversePacman;
import com.mygdx.game.screens.AbstractScreen;

public class SinglePlayerBoardScreen extends AbstractScreen {

    public BitmapFont font = new BitmapFont(); //or use alex answer to use custom font

    private OrthographicCamera camera;

    public SinglePlayerBoardScreen(final InversePacman app) {
        super(app);

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, InversePacman.V_WIDTH, InversePacman.V_HEIGHT);

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void show() {

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

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, 2, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        app.batch.begin();
        font.draw(app.batch, "Single Player Board \n \n Click 'P' to Pause", (camera.viewportWidth / 2), camera.viewportHeight / 2);
        app.batch.end();
    }
}
