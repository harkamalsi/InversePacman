package com.mygdx.game.screens.leaderboard;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.InversePacman;
import com.mygdx.game.managers.GameScreenManager;
import com.mygdx.game.systems.ButtonSystem;
import com.mygdx.game.systems.RenderingSystem;

import java.util.ArrayList;

public class SinglePlayerGhostsBoardScreen extends AbstractBoardScreen {
    private OrthographicCamera camera;
    private FitViewport viewport;

    private Sprite ellipseSprite;
    private Sprite front_ellipseSprite;
    private Sprite backSprite;

    private ButtonSystem buttonSystem;
    private RenderingSystem renderSystem;
    private TextureRegion ellipse;
    private TextureRegion front_ellipse;
    private TextureRegion back;


    private Engine engine;
    private float scaleX;
    private float scaleY;

    private Entity ellipseEntity;
    private Entity front_ellipseEntity;
    private Entity backButton;

    public SinglePlayerGhostsBoardScreen(final InversePacman app) {
        super(app);
        ellipse = new TextureRegion(new Texture("menuscreen/Ellipse 11.png"));
        front_ellipse = new TextureRegion(new Texture("optionscreen/option_front_ellipse.png"));
        back = new TextureRegion(new Texture("back.png"));
        scaleX = Gdx.graphics.getWidth() / (float)app.APP_WIDTH_MOBILE;
        scaleY = Gdx.graphics.getHeight() / (float)app.APP_HEIGHT_MOBILE;

        this.bg = new TextureRegion(new Texture(AbstractBoardScreen.LEADERBOARD_DIRECTORY + "ghosts.png"));
    }

    @Override
    public ArrayList<PlayerScore> retrieveTopPlayerScores() {
        //app.saveManager.loadDataValue("ghosts", ArrayList.class);
        ArrayList<PlayerScore> scores = new ArrayList<>();
        scores.add(new PlayerScore("Player 1", "1:23"));
        scores.add(new PlayerScore("Player 2", "1:34"));
        scores.add(new PlayerScore("Player 3", "1:40"));

        return scores;
    }

    @Override
    public void show() {
        super.show();
        this.camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        renderSystem = new RenderingSystem(batch);
        buttonSystem = new ButtonSystem(camera);

        engine = new Engine();
        engine.addSystem(renderSystem);
        engine.addSystem(buttonSystem);


        ellipseSprite = new Sprite(ellipse);

        ellipseEntity = new Entity();
        app.addSpriteEntity(ellipseSprite, ellipseEntity, engine, (Gdx.graphics.getWidth() / 2 - (ellipse.getRegionWidth() / 2 * (scaleX))), (Gdx.graphics.getHeight() - (ellipse.getRegionHeight() * (scaleY))), (ellipse.getRegionWidth() * (scaleX)), (ellipse.getRegionHeight() * (scaleY)), false, true, true, true, 0f, 200f, 151f);


        front_ellipseSprite = new Sprite(front_ellipse);

        front_ellipseEntity = new Entity();
        app.addSpriteEntity(front_ellipseSprite, front_ellipseEntity, engine,Gdx.graphics.getWidth() / 2 - (front_ellipse.getRegionWidth() / 2 * (scaleX)), Gdx.graphics.getHeight() / (float)1.17, front_ellipse.getRegionWidth() * (scaleX), front_ellipse.getRegionHeight() * (scaleY), false, false, false, false);

        backSprite = new Sprite(back);
        backButton = new Entity();
        app.addSpriteEntity(backSprite, backButton, engine, 0, 0, backSprite.getRegionWidth(), backSprite.getRegionHeight(), true,false, false, false);
    }

    public void handleInput() {
        if(backButton.flags == 1) {
            app.gsm.setScreen((GameScreenManager.STATE.LEADERBOARD_MENU_SCREEN));
        }
    }

    @Override
    public void update(float delta) {
        handleInput();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        engine.update(delta);

    }

}
