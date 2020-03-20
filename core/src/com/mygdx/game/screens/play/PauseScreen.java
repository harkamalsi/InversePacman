package com.mygdx.game.screens.play;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.mygdx.game.InversePacman;
import com.mygdx.game.screens.AbstractScreen;


public final class PauseScreen extends AbstractScreen {

    private OrthographicCamera camera;

    public BitmapFont font = new BitmapFont(); //or use alex answer to use custom font

    public PauseScreen(final InversePacman app) {
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
    public void render(float delta) {
        super.render(delta);
        app.batch.begin();
        font.draw(app.batch, "PAUSE \n \n Click 'R' to resume", (camera.viewportWidth / 2), camera.viewportHeight / 2);
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
