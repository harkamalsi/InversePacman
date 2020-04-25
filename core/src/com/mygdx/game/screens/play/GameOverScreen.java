package com.mygdx.game.screens.play;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.InversePacman;
import com.mygdx.game.components.MusicComponent;
import com.mygdx.game.managers.GameScreenManager;
import com.mygdx.game.multiplayermessage.MultiplayerMessage;
import com.mygdx.game.screens.AbstractScreen;
import com.mygdx.game.systems.MusicSystem;
import com.mygdx.game.systems.PillSystem;
import com.mygdx.game.systems.RenderingSystem;

public class GameOverScreen extends AbstractScreen {
    private SpriteBatch batch;

    private OrthographicCamera camera;
    private Engine engine;
    protected TextureRegion excitementBg;
    private TextureRegion won_bg;
    private TextureRegion lost_bg;
    private float elapsed;
    private Sound sound;

    private MultiplayerMessage connection = MultiplayerMessage.getInstance();

    private RenderingSystem renderingSystem;


    private MusicSystem musicSystem;
    private boolean won = true;
    private boolean start = true;
    private boolean resultpageadded = false;

    private TextureRegion ellipse;
    private TextureRegion front_ellipse;

    private Entity ellipseEntity;
    private Entity front_ellipseEntity;
    private Entity bgEntity;
    private Entity won_bgEntity;
    private Entity lost_bgEntity;

    private Entity musicEntity;

    private Sprite ellipseSprite;
    private Sprite front_ellipseSprite;
    private Sprite bgSprite;
    private Sprite won_bgSprite;
    private Sprite lost_bgSprite;

    private float scaleX;
    private float scaleY;

    public GameOverScreen(final InversePacman app, Engine engine) {
        super(app, engine);
        this.engine = engine;

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, InversePacman.V_WIDTH, InversePacman.V_HEIGHT);

        sound = Gdx.audio.newSound(Gdx.files.internal("sound/the_winner.ogg"));

        excitementBg = new TextureRegion(new Texture("gameover/excitement_bg.png"));
        won_bg = new TextureRegion(new Texture("gameover/won.png"));
        lost_bg = new TextureRegion(new Texture("gameover/gameover.png"));
        ellipse = new TextureRegion(new Texture("menuscreen/ellipse_color_change_correct.png"));
        front_ellipse = new TextureRegion(new Texture("optionscreen/option_front_ellipse.png"));


        scaleX = Gdx.graphics.getWidth() / (float)app.APP_WIDTH_MOBILE;
        scaleY = Gdx.graphics.getHeight() / (float)app.APP_HEIGHT_MOBILE;

    }

    @Override
    public void update(float delta) {
        System.out.println("time elapsed " + elapsed);
        elapsed += delta;
        if(Gdx.input.justTouched() && elapsed > 2){
            musicSystem.dispose();
            engine.removeAllEntities();
            app.gsm.setScreen(GameScreenManager.STATE.MAIN_MENU_SCREEN);
            //engine.removeSystem(musicSystem);

        }
        // need to add start boolean or music would not stop playing when switching screens
        if(elapsed > 2 && start) {
            musicSystem.resume();
            start = false;
        }

    }

    @Override
    public void show() {
        elapsed = 0;
        resultpageadded = false;

        System.out.println("Did we win? " + engine.getSystem(PillSystem.class).allPillsCollected());

        //sound.play(app.sound_volume);
        //Must check and retrieve if the player won or lost

        musicSystem = new MusicSystem();

        engine.addSystem(musicSystem);



        bgSprite = new Sprite(excitementBg);
        bgEntity = new Entity();
        //Don't know why but the background doesn't surround the whole screen, therefore I added some +/- on the parameters
        app.addSpriteEntity(bgSprite, bgEntity, engine,0, -2, Gdx.graphics.getWidth() +2, Gdx.graphics.getHeight() +2,false,false,false, false );

        ellipseSprite = new Sprite(ellipse);
        ellipseEntity = new Entity();
        app.addSpriteEntity(ellipseSprite, ellipseEntity, engine, (Gdx.graphics.getWidth() / 2 - (ellipse.getRegionWidth() / 2 * (scaleX))), (Gdx.graphics.getHeight() - (ellipse.getRegionHeight() * (scaleY))), (ellipse.getRegionWidth() * (scaleX)), (ellipse.getRegionHeight() * (scaleY)), false, true, true, false);


        front_ellipseSprite = new Sprite(front_ellipse);
        front_ellipseEntity = new Entity();
        app.addSpriteEntity(front_ellipseSprite, front_ellipseEntity, engine, Gdx.graphics.getWidth() / 2 - (front_ellipse.getRegionWidth() / 2 * (scaleX)), Gdx.graphics.getHeight() / (float)1.17, front_ellipse.getRegionWidth() * (scaleX), front_ellipse.getRegionHeight() * (scaleY),false, false, false, false );

        won_bgSprite = new Sprite(won_bg);
        won_bgEntity = new Entity();
        //Don't know why but the background doesn't surround the whole screen, therefore I added some +/- on the parameters
        app.addSpriteEntity(won_bgSprite, won_bgEntity, engine,0, -2, Gdx.graphics.getWidth() +2, Gdx.graphics.getHeight() +2,false,false,false, false );
        engine.removeEntity(won_bgEntity);

        lost_bgSprite = new Sprite(lost_bg);
        lost_bgEntity = new Entity();
        //Don't know why but the background doesn't surround the whole screen, therefore I added some +/- on the parameters
        app.addSpriteEntity(lost_bgSprite, lost_bgEntity, engine,0, -2, Gdx.graphics.getWidth() +2, Gdx.graphics.getHeight() +2,false,false,false, false );
        engine.removeEntity(lost_bgEntity);

        musicEntity = new Entity();

        if(engine.getSystem(PillSystem.class).allPillsCollected()) {
            connection.leaveLobby();
            musicEntity.add(new MusicComponent(Gdx.files.internal("music/gameover/won"), sound));
        }
        if(!engine.getSystem(PillSystem.class).allPillsCollected()) {
            connection.leaveLobby();
            musicEntity.add(new MusicComponent(Gdx.files.internal("music/gameover/lost"), sound));
        }

        engine.addEntity(musicEntity);
        musicSystem.pause();
        musicSystem.playSound(0);

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

        if (elapsed > 2.0) {
            engine.removeEntity(bgEntity);
        }
        if(elapsed > 2.0 && engine.getSystem(PillSystem.class).allPillsCollected() && !resultpageadded) {
            engine.removeEntity(front_ellipseEntity);
            engine.removeEntity(ellipseEntity);
            engine.addEntity(won_bgEntity);
            engine.addEntity(ellipseEntity);
            engine.addEntity(front_ellipseEntity);

            resultpageadded = true;
        }
        if(elapsed > 2.0 && !engine.getSystem(PillSystem.class).allPillsCollected() && !resultpageadded) {
            engine.removeEntity(front_ellipseEntity);
            engine.removeEntity(ellipseEntity);
            engine.addEntity(lost_bgEntity);
            engine.addEntity(ellipseEntity);
            engine.addEntity(front_ellipseEntity);
            resultpageadded = true;
        }
        engine.update(delta);


    }
}
