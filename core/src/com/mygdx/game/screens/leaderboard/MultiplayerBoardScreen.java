package com.mygdx.game.screens.leaderboard;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.mygdx.game.InversePacman;
import com.mygdx.game.screens.AbstractScreen;

public class MultiplayerBoardScreen extends AbstractScreen {
    private OrthographicCamera camera;

    public MultiplayerBoardScreen(final InversePacman app) {
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
}
