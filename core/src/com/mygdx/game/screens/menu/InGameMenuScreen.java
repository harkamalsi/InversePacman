package com.mygdx.game.screens.menu;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.mygdx.game.InversePacman;
import com.mygdx.game.screens.AbstractScreen;

public class InGameMenuScreen extends AbstractScreen {

    private OrthographicCamera camera;

    public InGameMenuScreen(final InversePacman app, Engine engine) {
        super(app, engine);

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
}
