package com.mygdx.game.screens.menu;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.InversePacman;
import com.mygdx.game.components.ButtonComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.managers.EntityManager;
import com.mygdx.game.managers.GameScreenManager;
import com.mygdx.game.screens.AbstractScreen;
import com.mygdx.game.systems.ButtonSystem;
import com.mygdx.game.systems.RenderingSystem;

// to format the numbers shown for sound and volume
import java.text.DecimalFormat;

public class OptionScreen extends AbstractScreen {

    private OrthographicCamera camera;
    private FitViewport viewport;

    private EntityManager entityManager;

    private SpriteBatch batch;

    private Engine engine;

    private ButtonSystem buttonSystem;
    private RenderingSystem renderingSystem;

    private TextureRegion back;
    private TextureRegion volume;
    private TextureRegion sounds;
    private TextureRegion increase;
    private TextureRegion decrease;
    private TextureRegion bg;
    private TextureRegion settings;

    private Entity backButton;
    private Entity volumeButton;
    private Entity soundButton;

    private Entity increaseVolumButton;
    private Entity decreaseVolumButton;

    private Entity increaseSoundButton;
    private Entity decreaseSoundButton;


    private Sprite backSprite;
    private Sprite volumeSprite;
    private Sprite soundSprite;

    private Sprite increaseVolumSprite;
    private Sprite decreaseVolumSprite;

    private Sprite increaseSoundSprite;
    private Sprite decreaseSoundSprite;

    private Music music;

    private BitmapFont font;

    private DecimalFormat df;



    public OptionScreen(InversePacman app) {
        super(app);
        bg = new TextureRegion(new Texture("menuscreen/menuscreen_bg.png"));
        back = new TextureRegion(new Texture("back.png"));
        settings = new TextureRegion(new Texture("optionscreen/options_button.png"));
        increase = new TextureRegion(new Texture("optionscreen/plus.png"));
        decrease = new TextureRegion(new Texture("optionscreen/minus.png"));
        font = new BitmapFont();
        df = new DecimalFormat();
        df.setMaximumFractionDigits(2);


    }

    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
            app.gsm.setScreen(GameScreenManager.STATE.MAIN_MENU_SCREEN);

            // saves the volume and sound levels to the settings file
            FileHandle settings = Gdx.files.local("settings.txt");
            settings.writeString(app.music_volume + "\n", false);
            settings.writeString(String.valueOf(app.sound_volume), true);
            music.dispose();
        }
        if(backButton.flags == 1) {
            music.dispose();

            // saves the volume and sound levels to the settings file
            FileHandle settings = Gdx.files.local("settings.txt");
            settings.writeString(app.music_volume + "\n", false);
            settings.writeString(String.valueOf(app.sound_volume), true);
            app.gsm.setScreen(GameScreenManager.STATE.MAIN_MENU_SCREEN);
        }
        // increases the volume by 10% each time the volume button increase is pressed
        if(increaseVolumButton.flags == 1 && app.music_volume < 1) {
            app.music_volume += (float)0.1;
            music.setVolume(app.music_volume);
            increaseVolumButton.flags = 0;
            if(app.music_volume > 1) {
                app.music_volume = 1;
            }
            System.out.println(music.getVolume());
        }
        else {
            increaseVolumButton.flags = 0;
        }
        // decreases the volume by 10% each time the volume button decrease is pressed
        if(decreaseVolumButton.flags == 1 && app.music_volume > 0) {
            app.music_volume -= (float)0.1;
            music.setVolume(app.music_volume);
            decreaseVolumButton.flags = 0;
            if(app.music_volume < 0) {
                app.music_volume = 0;
            }
            System.out.println(music.getVolume());

        }
        else {
            decreaseVolumButton.flags = 0;
        }
        // increases the sound by 10% each time the sound button increase is pressed
        if(increaseSoundButton.flags == 1 && app.sound_volume < 1) {
            app.sound_volume += (float)0.1;
            increaseSoundButton.flags = 0;
            if(app.sound_volume > 1) {
                app.sound_volume = 1;
            }
        }
        else {
            increaseSoundButton.flags = 0;
        }
        // decreases the sound by 10% each time the sound button decrease is pressed
        if(decreaseSoundButton.flags == 1 && app.sound_volume > 0) {
            app.sound_volume -= (float)0.1;
            decreaseSoundButton.flags = 0;
            if(app.sound_volume < 0) {
                app.sound_volume = 0;
            }
        }
        else {
            decreaseSoundButton.flags = 0;
        }
    }

    @Override
    public void update(float delta) {
        handleInput();
        //System.out.println(music.getVolume());

    }

    @Override
    public void show() {
        music = Gdx.audio.newMusic(Gdx.files.internal("music/menu/menumusic.mp3"));
        music.setLooping(true);
        music.setVolume(app.music_volume);
        music.play();

        this.camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());



        batch = new SpriteBatch();

        buttonSystem = new ButtonSystem(camera);
        renderingSystem = new RenderingSystem(batch);

        engine = new Engine();
        engine.addSystem(buttonSystem);

        // ***** Increase music button START *****

        increaseVolumSprite = new Sprite(increase);
        increaseVolumSprite.setBounds(Gdx.graphics.getWidth() / 2 + (increaseVolumSprite.getRegionWidth() / 2) * 2, Gdx.graphics.getHeight() / (float)1.80, increaseVolumSprite.getRegionWidth(), increaseVolumSprite.getRegionHeight());

        increaseVolumButton = new Entity();

        increaseVolumButton.add(new TextureComponent(increaseVolumSprite))
                .add(new ButtonComponent(Gdx.graphics.getWidth() / 2 + (increaseVolumSprite.getRegionWidth() / 2) * 2, Gdx.graphics.getHeight() / (float)1.80, increaseVolumSprite.getRegionWidth(),  increaseVolumSprite.getRegionHeight()))
                .add(new TransformComponent(Gdx.graphics.getWidth() / 2 + (increaseVolumSprite.getRegionWidth() / 2) * 2, Gdx.graphics.getHeight() / (float)1.80));
        engine.addEntity(increaseVolumButton);

        // ***** Increase music button END *****

        // ***** Decrease music button START *****

        decreaseVolumSprite = new Sprite(decrease);
        decreaseVolumSprite.setBounds(Gdx.graphics.getWidth() / 2 - decreaseVolumSprite.getRegionWidth() / 2, Gdx.graphics.getHeight() / (float)1.8, decreaseVolumSprite.getRegionWidth(), decreaseVolumSprite.getRegionHeight());

        decreaseVolumButton = new Entity();

        decreaseVolumButton.add(new TextureComponent(decreaseVolumSprite))
                .add(new ButtonComponent(Gdx.graphics.getWidth() / 2 - decreaseVolumSprite.getRegionWidth() / 2, Gdx.graphics.getHeight() / (float)1.8, decreaseVolumSprite.getRegionWidth(),  decreaseVolumSprite.getRegionHeight()))
                .add(new TransformComponent(Gdx.graphics.getWidth() / 2 - decreaseVolumSprite.getRegionWidth() / 2, Gdx.graphics.getHeight() / (float)1.8));
        engine.addEntity(decreaseVolumButton);

        // ***** Decrease music button END *****


        // ***** Increase sound button START *****

        increaseSoundSprite = new Sprite(increase);
        increaseSoundSprite.setBounds(Gdx.graphics.getWidth() / 2 + (increaseSoundSprite.getRegionWidth() / 2) * 2, Gdx.graphics.getHeight() / (float)3, increaseSoundSprite.getRegionWidth(), increaseSoundSprite.getRegionHeight());

        increaseSoundButton = new Entity();

        increaseSoundButton.add(new TextureComponent(increaseSoundSprite))
                .add(new ButtonComponent(Gdx.graphics.getWidth() / 2 + (increaseSoundSprite.getRegionWidth() / 2) * 2, Gdx.graphics.getHeight() / (float)3, increaseSoundSprite.getRegionWidth(),  increaseSoundSprite.getRegionHeight()))
                .add(new TransformComponent(Gdx.graphics.getWidth() / 2 + (increaseSoundSprite.getRegionWidth() / 2) * 2, Gdx.graphics.getHeight() / (float)3));
        engine.addEntity(increaseSoundButton);

        // ***** Increase sound button END *****

        // ***** Decrease sound button START *****

        decreaseSoundSprite = new Sprite(decrease);
        decreaseSoundSprite.setBounds(Gdx.graphics.getWidth() / 2 - increaseSoundSprite.getRegionWidth() / 2, Gdx.graphics.getHeight() / (float)3, increaseSoundSprite.getRegionWidth(), increaseSoundSprite.getRegionHeight());

        decreaseSoundButton = new Entity();

        decreaseSoundButton.add(new TextureComponent(increaseSoundSprite))
                .add(new ButtonComponent(Gdx.graphics.getWidth() / 2 - increaseSoundSprite.getRegionWidth() / 2, Gdx.graphics.getHeight() / (float)3, increaseSoundSprite.getRegionWidth(),  increaseSoundSprite.getRegionHeight()))
                .add(new TransformComponent(Gdx.graphics.getWidth() / 2 - increaseSoundSprite.getRegionWidth() / 2, Gdx.graphics.getHeight() / (float)3));
        engine.addEntity(decreaseSoundButton);

        // ***** Decrease sound button END *****






        // ***** Back button START *****

        backSprite = new Sprite(back);
        backSprite.setBounds(0, 0, backSprite.getRegionWidth(), backSprite.getRegionHeight());

        backButton = new Entity();

        backButton.add(new TextureComponent(backSprite))
                .add(new ButtonComponent(0, 0, backSprite.getRegionWidth(),  backSprite.getRegionHeight()))
                .add(new TransformComponent(0, 0));
        engine.addEntity(backButton);

        // ***** Back button END *****


    }

    @Override
    public void render(float delta) {
        super.render(delta);
        engine.update(delta);
        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0.2f, 0.2f, 0.1f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(bg, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        font.getData().setScale(4f);

        font.draw(batch, "music: " + df.format(app.music_volume * 100) + "%", 0, Gdx.graphics.getHeight() / (float)1.80 + increaseVolumSprite.getRegionHeight() / 2);
        batch.draw(settings, (Gdx.graphics.getWidth() / 2 - ((settings.getRegionWidth() / 2) * (float)1.5)), (Gdx.graphics.getHeight() - ((settings.getRegionHeight() / 2) / (float)1.5)) / (float)1.07, settings.getRegionWidth()*(float)1.5, settings.getRegionHeight()*(float)1.5);
        // REMEMBER to change the textures for the 2 sprites below, since there is no intuitively way to change the hitboxes
        increaseVolumSprite.draw(batch);
        decreaseVolumSprite.draw(batch);
        font.draw(batch, "sound: " + df.format(app.sound_volume * 100) + "%", 0,Gdx.graphics.getHeight() / (float)3 + decreaseVolumSprite.getRegionHeight() / 2);
        increaseSoundSprite.draw(batch);
        decreaseSoundSprite.draw(batch);
        backSprite.draw(batch);
        batch.end();
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
    }
}
