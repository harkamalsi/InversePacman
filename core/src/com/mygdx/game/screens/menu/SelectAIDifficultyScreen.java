package com.mygdx.game.screens.menu;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.InversePacman;
import com.mygdx.game.components.ButtonComponent;
import com.mygdx.game.components.MusicComponent;
import com.mygdx.game.managers.GameScreenManager;
import com.mygdx.game.screens.AbstractScreen;
import com.mygdx.game.systems.ButtonSystem;
import com.mygdx.game.systems.MusicSystem;
import com.mygdx.game.systems.RenderingSystem;


public class SelectAIDifficultyScreen extends AbstractScreen {
    private OrthographicCamera camera;
    private FitViewport viewport;


    private SpriteBatch batch;

    private Engine engine;

    private ButtonSystem buttonSystem;
    private RenderingSystem renderingSystem;
    private MusicSystem musicSystem;

    private TextureRegion back;
    private TextureRegion bg;
    private TextureRegion sandbox;
    private TextureRegion supereasy;
    private TextureRegion easy;
    private TextureRegion medium;
    private TextureRegion hard;
    private TextureRegion murderous;

    private TextureRegion ellipse;
    private TextureRegion front_ellipse;

    private Entity backButton;
    private Entity ellipseEntity;
    private Entity front_ellipseEntity;
    private Entity bgEntity;
    private Entity sandboxButton;
    private Entity supereasyButton;
    private Entity easyButton;
    private Entity mediumButton;
    private Entity hardButton;
    private Entity murderousButton;

    private Entity musicEntity;

    private Sprite backSprite;
    private Sprite ellipseSprite;
    private Sprite front_ellipseSprite;
    private Sprite bgSprite;
    private Sprite sandboxSprite;
    private Sprite supereasySprite;
    private Sprite easySprite;
    private Sprite mediumSprite;
    private Sprite hardSprite;
    private Sprite murderousSprite;

    private float scaleX;
    private float scaleY;

    public SelectAIDifficultyScreen(InversePacman app, Engine engine) {
        super(app, engine);
        this.engine = engine;

        ellipse = new TextureRegion(new Texture("menuscreen/ellipse_color_change_correct.png"));
        front_ellipse = new TextureRegion(new Texture("optionscreen/option_front_ellipse.png"));
        back = new TextureRegion(new Texture("back3x.png"));
        bg = new TextureRegion(new Texture("menuscreen/menu_bg.png"));
        sandbox = new TextureRegion(new Texture("ai_difficulty_buttons/sandbox.png"));
        supereasy = new TextureRegion(new Texture("ai_difficulty_buttons/supereasy.png"));
        easy = new TextureRegion(new Texture("ai_difficulty_buttons/easy.png"));
        medium = new TextureRegion(new Texture("ai_difficulty_buttons/medium.png"));
        hard = new TextureRegion(new Texture("ai_difficulty_buttons/hard.png"));
        murderous = new TextureRegion(new Texture("ai_difficulty_buttons/murderous.png"));


        scaleX = Gdx.graphics.getWidth() / (float)app.APP_WIDTH_MOBILE;
        scaleY = Gdx.graphics.getHeight() / (float)app.APP_HEIGHT_MOBILE;
    }

    private void handleInput() {
        for(Entity i :engine.getEntitiesFor(Family.all(ButtonComponent.class).get())) {
            if(i.flags == 1) {
                engine.removeAllEntities();
                musicSystem.dispose();
            }
        }
        if(sandboxButton.flags == 1) {
            app.ai_difficulty = "ALWAYS_RANDOM";
            app.gsm.setScreen(GameScreenManager.STATE.PLAY);
        }

        if(supereasyButton.flags == 1) {
            app.ai_difficulty = "SUPEREASY";
            app.gsm.setScreen(GameScreenManager.STATE.PLAY);
        }

        if(easyButton.flags == 1) {
            app.ai_difficulty = "EASY";
            app.gsm.setScreen(GameScreenManager.STATE.PLAY);
        }

        if(mediumButton.flags == 1) {
            app.ai_difficulty = "MEDIUM";
            app.gsm.setScreen(GameScreenManager.STATE.PLAY);
        }

        if(hardButton.flags == 1) {
            app.ai_difficulty = "HARD";
            app.gsm.setScreen(GameScreenManager.STATE.PLAY);
        }

        if(murderousButton.flags == 1) {
            app.ai_difficulty = "MURDEROUS";
            app.gsm.setScreen(GameScreenManager.STATE.PLAY);
        }

        if(backButton.flags == 1) {
            //engine.removeSystem(musicSystem);

            app.gsm.setScreen(GameScreenManager.STATE.MAIN_MENU_SCREEN);
        }
    }


    @Override
    public void update(float delta) {
        handleInput();

    }

    @Override
    public void show() {
        this.camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch = new SpriteBatch();


        buttonSystem = new ButtonSystem(camera);
        renderingSystem = new RenderingSystem(batch);
        musicSystem = new MusicSystem();

        engine = new Engine();
        engine.addSystem(buttonSystem);
        engine.addSystem(musicSystem);
        engine.addSystem(renderingSystem);

        bgSprite = new Sprite(bg);
        bgEntity = new Entity();
        //Don't know why but the background doesn't surround the whole screen, therefore I added some +/- on the parameters
        app.addSpriteEntity(bgSprite, bgEntity, engine,0, -2, Gdx.graphics.getWidth() +2, Gdx.graphics.getHeight() +2,false,false,false, false );

        ellipseSprite = new Sprite(ellipse);
        ellipseEntity = new Entity();
        app.addSpriteEntity(ellipseSprite, ellipseEntity, engine, (Gdx.graphics.getWidth() / 2 - (ellipse.getRegionWidth() / 2 * (scaleX))), (Gdx.graphics.getHeight() - (ellipse.getRegionHeight() * (scaleY))), (ellipse.getRegionWidth() * (scaleX)), (ellipse.getRegionHeight() * (scaleY)), false, true, true, false);


        front_ellipseSprite = new Sprite(front_ellipse);
        front_ellipseEntity = new Entity();
        app.addSpriteEntity(front_ellipseSprite, front_ellipseEntity, engine, Gdx.graphics.getWidth() / 2 - (front_ellipse.getRegionWidth() / 2 * (scaleX)), Gdx.graphics.getHeight() / (float)1.17, front_ellipse.getRegionWidth() * (scaleX), front_ellipse.getRegionHeight() * (scaleY),false, false, false, false );

        sandboxSprite = new Sprite(sandbox);
        sandboxButton = new Entity();
        app.addSpriteEntity(sandboxSprite, sandboxButton, engine, Gdx.graphics.getWidth() / 2 - (sandboxSprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)1.50, sandboxSprite.getRegionWidth() * scaleX ,sandboxSprite.getRegionHeight() * scaleY, true,false, false, false);

        supereasySprite = new Sprite(supereasy);
        supereasyButton = new Entity();
        app.addSpriteEntity(supereasySprite, supereasyButton, engine, Gdx.graphics.getWidth() / 2 - (supereasySprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)1.5 - (sandboxSprite.getRegionHeight() * 2 * scaleY), supereasySprite.getRegionWidth() * scaleX ,supereasySprite.getRegionHeight() * scaleY, true,false, false, false);

        easySprite = new Sprite(easy);
        easyButton = new Entity();
        app.addSpriteEntity(easySprite, easyButton, engine, Gdx.graphics.getWidth() / 2 - (easySprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)1.5 - (sandboxSprite.getRegionHeight() * 2 * scaleY * 2), easySprite.getRegionWidth() * scaleX ,easySprite.getRegionHeight() * scaleY, true,false, false, false);

        mediumSprite = new Sprite(medium);
        mediumButton = new Entity();
        app.addSpriteEntity(mediumSprite, mediumButton, engine, Gdx.graphics.getWidth() / 2 - (mediumSprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)1.5 - (sandboxSprite.getRegionHeight() * 2 * scaleY * 3), mediumSprite.getRegionWidth() * scaleX ,mediumSprite.getRegionHeight() * scaleY, true,false, false, false);

        hardSprite = new Sprite(hard);
        hardButton = new Entity();
        app.addSpriteEntity(hardSprite, hardButton, engine, Gdx.graphics.getWidth() / 2 - (hardSprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)1.5 - (sandboxSprite.getRegionHeight() * 2 * scaleY * 4), hardSprite.getRegionWidth() * scaleX ,hardSprite.getRegionHeight() * scaleY, true,false, false, false);

        murderousSprite = new Sprite(murderous);
        murderousButton = new Entity();
        app.addSpriteEntity(murderousSprite, murderousButton, engine, Gdx.graphics.getWidth() / 2 - (murderousSprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)1.5 - (sandboxSprite.getRegionHeight() * 2 * scaleY * 5), murderousSprite.getRegionWidth() * scaleX ,murderousSprite.getRegionHeight() * scaleY, true,false, false, false);

        backSprite = new Sprite(back);
        backButton = new Entity();
        app.addSpriteEntity(backSprite, backButton, engine, 0, 0, backSprite.getRegionWidth() * scaleX, backSprite.getRegionHeight() * scaleX, true,false, false, false);


        musicEntity = new Entity();
        musicEntity.add(new MusicComponent(Gdx.files.internal("music/menu")));
        engine.addEntity(musicEntity);

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        engine.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
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
    public void dispose() {
        super.dispose();
        engine.removeAllEntities();
    }


}
