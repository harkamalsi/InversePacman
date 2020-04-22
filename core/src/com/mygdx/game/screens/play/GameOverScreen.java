package com.mygdx.game.screens.play;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.InversePacman;
import com.mygdx.game.screens.AbstractScreen;

public class GameOverScreen extends AbstractScreen {
    private OrthographicCamera camera;
    private Engine engine;
    protected TextureRegion back;

    public GameOverScreen(final InversePacman app, Engine engine) {
        super(app, engine);
        this.engine = engine;

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, InversePacman.V_WIDTH, InversePacman.V_HEIGHT);

        back = new TextureRegion(new Texture("gameover.png"));
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
        app.batch.begin();
        app.batch.draw(back, 0, 0, camera.viewportWidth, camera.viewportHeight);

        app.batch.end();


    }
}
