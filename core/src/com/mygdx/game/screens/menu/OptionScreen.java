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
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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
import com.mygdx.game.systems.MusicSystem;
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
    private MusicSystem musicSystem;

    private TextureRegion back;
    private TextureRegion volume_mute;
    private TextureRegion sound_mute;
    private TextureRegion increase_music;
    private TextureRegion decrease_music;
    private TextureRegion increase_sound;
    private TextureRegion decrease_sound;
    private TextureRegion bg;
    private TextureRegion ellipse;
    private TextureRegion front_ellipse;
    private TextureRegion settings;

    private Entity backButton;
    private Entity volume_muteButton;
    private Entity sound_muteButton;

    private Entity increaseVolumButton;
    private Entity decreaseVolumButton;

    private Entity increaseSoundButton;
    private Entity decreaseSoundButton;


    private Sprite backSprite;
    private Sprite volume_muteSprite;
    private Sprite sound_muteSprite;

    private Sprite increaseVolumSprite;
    private Sprite decreaseVolumSprite;

    private Sprite increaseSoundSprite;
    private Sprite decreaseSoundSprite;

    private Sprite ellipseSprite;

    private Music music;

    private BitmapFont font;

    private float scaleX;
    private float scaleY;


    private DecimalFormat df;
    private GlyphLayout layout;



    public OptionScreen(InversePacman app) {
        super(app);
        bg = new TextureRegion(new Texture("optionscreen/option_bg.png"));
        back = new TextureRegion(new Texture("back.png"));
        settings = new TextureRegion(new Texture("optionscreen/options_button.png"));
        increase_music = new TextureRegion(new Texture("optionscreen/music_louder.png"));
        decrease_music = new TextureRegion(new Texture("optionscreen/music_lower.png"));
        increase_sound = new TextureRegion(new Texture("optionscreen/louder_sound.png"));
        decrease_sound = new TextureRegion(new Texture("optionscreen/lower_sound.png"));
        volume_mute = new TextureRegion(new Texture("optionscreen/mute_music.png"));
        sound_mute = new TextureRegion(new Texture("optionscreen/mute_sound.png"));
        ellipse = new TextureRegion(new Texture("menuscreen/ellipse_color_change_correct.png"));
        front_ellipse = new TextureRegion(new Texture("optionscreen/option_front_ellipse.png"));
        font = new BitmapFont(Gdx.files.internal("font/rubik_font_correct.fnt"));
        layout = new GlyphLayout(); //dont do this every frame! Store it as member

        df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        scaleX = Gdx.graphics.getWidth() / (float)app.APP_WIDTH_MOBILE;
        scaleY = Gdx.graphics.getHeight() / (float)app.APP_HEIGHT_MOBILE;

    }

    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
            engine.removeSystem(musicSystem);
            musicSystem.dispose();
            app.gsm.setScreen(GameScreenManager.STATE.MAIN_MENU_SCREEN);

            // saves the volume and sound levels to the settings file
            FileHandle settings = Gdx.files.local("settings.txt");
            settings.writeString(app.stored_music_volume + "\n", false);
            settings.writeString(String.valueOf(app.stored_sound_volume), true);

        }
        if(backButton.flags == 1) {
            engine.removeSystem(musicSystem);
            musicSystem.dispose();

            // saves the volume and sound levels to the settings file
            FileHandle settings = Gdx.files.local("settings.txt");
            settings.writeString(app.stored_music_volume + "," + app.music + "\n", false);
            settings.writeString(app.stored_sound_volume + "," + app.sound, true);
            app.gsm.setScreen(GameScreenManager.STATE.MAIN_MENU_SCREEN);
        }
        // increases the volume by 10% each time the volume button increase is pressed
        if(increaseVolumButton.flags == 1 && app.stored_music_volume < 1) {
            app.stored_music_volume += (float)0.1;
            increaseVolumButton.flags = 0;
            if(app.stored_music_volume > 1) {
                app.stored_music_volume = 1;
            }
            app.music_volume = app.stored_music_volume;
            app.music = true;
            musicSystem.setMusic(app.music_volume);
        }
        else {
            increaseVolumButton.flags = 0;
        }
        // decreases the volume by 10% each time the volume button decrease is pressed
        if(decreaseVolumButton.flags == 1 && app.stored_music_volume > 0) {
            app.stored_music_volume -= (float)0.1;

            decreaseVolumButton.flags = 0;
            if(app.stored_music_volume < 0) {
                app.stored_music_volume = 0;
            }
            app.music_volume = app.stored_music_volume;
            app.music = true;
            musicSystem.setMusic(app.music_volume);

        }
        else {
            decreaseVolumButton.flags = 0;
        }
        // increases the sound by 10% each time the sound button increase is pressed
        if(increaseSoundButton.flags == 1 && app.stored_sound_volume < 1) {
            app.stored_sound_volume += (float)0.1;
            increaseSoundButton.flags = 0;
            if(app.stored_sound_volume > 1) {
                app.stored_sound_volume = 1;
            }
            app.sound_volume = app.stored_sound_volume;
            app.sound = true;
        }
        else {
            increaseSoundButton.flags = 0;
        }
        // decreases the sound by 10% each time the sound button decrease is pressed
        if(decreaseSoundButton.flags == 1 && app.stored_sound_volume > 0) {
            app.stored_sound_volume -= (float)0.1;
            decreaseSoundButton.flags = 0;
            if(app.stored_sound_volume < 0) {
                app.stored_sound_volume = 0;
            }
            app.sound_volume = app.stored_sound_volume;
            app.sound = true;
        }
        else {
            decreaseSoundButton.flags = 0;
        }

        if(volume_muteButton.flags == 1) {
            if(app.music) {
                app.music = false;
                FileHandle settings = Gdx.files.local("settings.txt");
                settings.writeString(app.stored_music_volume + "," + app.music + "\n", false);
                settings.writeString(app.stored_sound_volume + "," + app.sound, true);
                app.music_volume = 0;
            }
            else if(!app.music) {
                app.music = true;
                app.music_volume = app.stored_music_volume;
                FileHandle settings = Gdx.files.local("settings.txt");
                settings.writeString(app.stored_music_volume + "," + app.music + "\n", false);
                settings.writeString(app.stored_sound_volume + "," + app.sound, true);
            }
            musicSystem.setMusic(app.music_volume);
            volume_muteButton.flags = 0;
        }

        if(sound_muteButton.flags == 1) {
            if(app.sound) {
                app.sound = false;
                FileHandle settings = Gdx.files.local("settings.txt");
                settings.writeString(app.stored_music_volume + "," + app.music + "\n", false);
                settings.writeString(app.stored_sound_volume + "," + app.sound, true);
                app.sound_volume = 0;
            }
            else if(!app.sound) {
                app.sound = true;
                app.sound_volume = app.stored_sound_volume;
                FileHandle settings = Gdx.files.local("settings.txt");
                settings.writeString(app.stored_music_volume + "," + app.music + "\n", false);
                settings.writeString(app.stored_sound_volume + "," + app.sound, true);
            }
            sound_muteButton.flags = 0;
        }

    }

    @Override
    public void update(float delta) {
        handleInput();
        app.step();
        //System.out.println("Width app: " + app.APP_WIDTH + " Actual width: " + Gdx.graphics.getWidth());
        //System.out.println("Height app " + app.APP_HEIGHT + " Actual height " + Gdx.graphics.getHeight());


        //System.out.println(music.getVolume());

    }

    @Override
    public void show() {
        this.camera = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());





        batch = new SpriteBatch();

        buttonSystem = new ButtonSystem(camera);
        renderingSystem = new RenderingSystem(batch);
        musicSystem = new MusicSystem(Gdx.files.internal("music/pause"));

        engine = new Engine();
        engine.addSystem(buttonSystem);
        engine.addSystem(musicSystem);

        ellipseSprite = new Sprite(ellipse);
        ellipseSprite.setBounds(Gdx.graphics.getWidth() / 2 - (ellipse.getRegionWidth() / 2 * (scaleX)), Gdx.graphics.getHeight() - (ellipse.getRegionHeight() * (scaleY)), ellipse.getRegionWidth() * (scaleX), ellipse.getRegionHeight() * (scaleY));

        // ***** Increase music button START *****

        increaseVolumSprite = new Sprite(increase_music);
        increaseVolumSprite.setBounds(Gdx.graphics.getWidth() / (float)1.175 - (increaseVolumSprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)1.95 - (increaseVolumSprite.getRegionHeight() / 2 * scaleY), increaseVolumSprite.getRegionWidth() * scaleX, increaseVolumSprite.getRegionHeight() * scaleY);

        increaseVolumButton = new Entity();

        increaseVolumButton.add(new TextureComponent(increaseVolumSprite))
                .add(new ButtonComponent(Gdx.graphics.getWidth() / (float)1.175 - (increaseVolumSprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)1.95 - (increaseVolumSprite.getRegionHeight() / 2 * scaleY), increaseVolumSprite.getRegionWidth() * scaleX,  increaseVolumSprite.getRegionHeight() * scaleY))
                .add(new TransformComponent(Gdx.graphics.getWidth() / (float)1.175 - (increaseVolumSprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)1.95 - (increaseVolumSprite.getRegionHeight() / 2* scaleY)));
        engine.addEntity(increaseVolumButton);

        // ***** Increase music button END *****

        // ***** Decrease music button START *****

        decreaseVolumSprite = new Sprite(decrease_music);
        decreaseVolumSprite.setBounds(Gdx.graphics.getWidth() / (float)1.5 - (decreaseVolumSprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)1.95 - (decreaseVolumSprite.getRegionHeight() / 2 * scaleY), decreaseVolumSprite.getRegionWidth() * scaleX, decreaseVolumSprite.getRegionHeight() * scaleY);

        decreaseVolumButton = new Entity();

        decreaseVolumButton.add(new TextureComponent(decreaseVolumSprite))
                .add(new ButtonComponent(Gdx.graphics.getWidth() / (float)1.5 - (decreaseVolumSprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)1.95 - (decreaseVolumSprite.getRegionHeight() / 2 * scaleY), decreaseVolumSprite.getRegionWidth() * scaleX,  decreaseVolumSprite.getRegionHeight() * scaleY))
                .add(new TransformComponent(Gdx.graphics.getWidth() / (float)1.5 - (decreaseVolumSprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)1.95 - decreaseVolumSprite.getRegionHeight() / 2 * scaleY));
        engine.addEntity(decreaseVolumButton);

        // ***** Decrease music button END *****


        // ***** Increase sound button START *****

        increaseSoundSprite = new Sprite(increase_sound);
        increaseSoundSprite.setBounds(Gdx.graphics.getWidth() / (float)1.175 - (increaseSoundSprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)4.55 - (increaseSoundSprite.getRegionHeight() / 2 * scaleY), increaseSoundSprite.getRegionWidth() * scaleX, increaseSoundSprite.getRegionHeight() * scaleY);

        increaseSoundButton = new Entity();

        increaseSoundButton.add(new TextureComponent(increaseSoundSprite))
                .add(new ButtonComponent(Gdx.graphics.getWidth() / (float)1.175 - (increaseSoundSprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)4.55 - (increaseSoundSprite.getRegionHeight() / 2 * scaleY), increaseSoundSprite.getRegionWidth() * scaleX,  increaseSoundSprite.getRegionHeight() * scaleY))
                .add(new TransformComponent(Gdx.graphics.getWidth() / (float)1.175 - (increaseSoundSprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)4.55 - (increaseSoundSprite.getRegionHeight() / 2 * scaleY)));
        engine.addEntity(increaseSoundButton);

        // ***** Increase sound button END *****

        // ***** Decrease sound button START *****

        decreaseSoundSprite = new Sprite(decrease_sound);
        decreaseSoundSprite.setBounds(Gdx.graphics.getWidth() / (float)1.5 - (decreaseSoundSprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)4.55 - (decreaseSoundSprite.getRegionHeight() / 2 * scaleY), decreaseSoundSprite.getRegionWidth() * scaleX, decreaseSoundSprite.getRegionHeight() * scaleY);

        decreaseSoundButton = new Entity();

        decreaseSoundButton.add(new TextureComponent(decreaseSoundSprite))
                .add(new ButtonComponent(Gdx.graphics.getWidth() / (float)1.5 - (decreaseSoundSprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)4.55 - (decreaseSoundSprite.getRegionHeight() / 2 * scaleY), decreaseSoundSprite.getRegionWidth() * scaleX, decreaseSoundSprite.getRegionHeight() * scaleY))
                .add(new TransformComponent(Gdx.graphics.getWidth() / (float)1.5 - (decreaseSoundSprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)4.55 - (decreaseSoundSprite.getRegionHeight() / 2 * scaleY)));
        engine.addEntity(decreaseSoundButton);

        // ***** Decrease sound button END *****



        // ***** Mute music button START *****

        volume_muteSprite = new Sprite(volume_mute);
        volume_muteSprite.setBounds(Gdx.graphics.getWidth() / (float)3 - (volume_muteSprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)1.95 - (volume_muteSprite.getRegionHeight() / 2 * scaleY), volume_muteSprite.getRegionWidth() * scaleX, volume_muteSprite.getRegionHeight() * scaleY);

        volume_muteButton = new Entity();

        volume_muteButton.add(new TextureComponent(volume_muteSprite))
                .add(new ButtonComponent(Gdx.graphics.getWidth() / (float)3 - (volume_muteSprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)1.95 - (volume_muteSprite.getRegionHeight() / 2 * scaleY), volume_muteSprite.getRegionWidth() * scaleX, volume_muteSprite.getRegionHeight() * scaleY))
                .add(new TransformComponent(Gdx.graphics.getWidth() / (float)3 - (volume_muteSprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)1.95 - (volume_muteSprite.getRegionHeight() / 2 * scaleY)));
        engine.addEntity(volume_muteButton);

        // ***** Mute music button END *****



        // ***** Mute sound button START *****

        sound_muteSprite = new Sprite(sound_mute);
        sound_muteSprite.setBounds(Gdx.graphics.getWidth() / (float)3 - (sound_muteSprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)4.55 - (sound_muteSprite.getRegionHeight() / 2 * scaleY), sound_muteSprite.getRegionWidth() * scaleX, sound_muteSprite.getRegionHeight() * scaleY);

        sound_muteButton = new Entity();

        sound_muteButton.add(new TextureComponent(sound_muteSprite))
                .add(new ButtonComponent(Gdx.graphics.getWidth() / (float)3 - (sound_muteSprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)4.55 - (sound_muteSprite.getRegionHeight() / 2 * scaleY), sound_muteSprite.getRegionWidth() * scaleX, sound_muteSprite.getRegionHeight() * scaleY))
                .add(new TransformComponent(Gdx.graphics.getWidth() / (float)3 - (sound_muteSprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)4.55 - (sound_muteSprite.getRegionHeight() / 2 * scaleY)));
        engine.addEntity(sound_muteButton);

        // ***** Mute sound button END *****






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
        //batch.setColor(0,78, 59,a);
        batch.draw(bg, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        font.getData().setScale(scaleX,scaleY);
        ellipseSprite.setColor(0,78, 59,app.a);
        ellipseSprite.draw(batch);
        batch.draw(front_ellipse, Gdx.graphics.getWidth() / 2 - (front_ellipse.getRegionWidth() / 2 * (scaleX)), Gdx.graphics.getHeight() / (float)1.17, front_ellipse.getRegionWidth() * (scaleX), front_ellipse.getRegionHeight() * (scaleY));
        layout.setText(font,"Options");
        //font.setColor(0,78, 59,a);
        font.draw(batch,layout,Gdx.graphics.getWidth() / 2 - layout.width / 2, (Gdx.graphics.getHeight() - ((layout.height / 2) / (float)1.5)) / (float)1.06);

        layout.setText(font, "music");
        font.draw(batch,layout, Gdx.graphics.getWidth() / 2 - layout.width / 2,Gdx.graphics.getHeight() / (float)1.625 + layout.height / 2);
        layout.setText(font, df.format(app.music_volume * 100) + "%");
        font.draw(batch,layout, Gdx.graphics.getWidth() / 6 - layout.width / 2,Gdx.graphics.getHeight() / (float)1.90);
        //font.draw(batch, "music: " + df.format(app.music_volume * 100) + "%", 0, Gdx.graphics.getHeight() / (float)1.80 + increaseVolumSprite.getRegionHeight() / 2);
        //batch.draw(settings, (Gdx.graphics.getWidth() / 2 - ((settings.getRegionWidth() / 2) * (float)1.5)), (Gdx.graphics.getHeight() - ((settings.getRegionHeight() / 2) / (float)1.5)) / (float)1.07, settings.getRegionWidth()*(float)1.5, settings.getRegionHeight()*(float)1.5);
        // REMEMBER to change the textures for the 2 sprites below, since there is no intuitively way to change the hitboxes
        increaseVolumSprite.draw(batch);
        decreaseVolumSprite.draw(batch);

        layout.setText(font, "sound");
        font.draw(batch, layout, Gdx.graphics.getWidth() / 2 - layout.width / 2,Gdx.graphics.getHeight() / (float)2.95);
        layout.setText(font, df.format(app.sound_volume * 100) + "%");
        font.draw(batch, layout,Gdx.graphics.getWidth() / 6 - layout.width / 2,Gdx.graphics.getHeight() / (float)4.2);
        //font.draw(batch, "sound: " + df.format(app.sound_volume * 100) + "%", 0,Gdx.graphics.getHeight() / (float)3 + decreaseVolumSprite.getRegionHeight() / 2);
        increaseSoundSprite.draw(batch);
        decreaseSoundSprite.draw(batch);
        volume_muteSprite.draw(batch);
        sound_muteSprite.draw(batch);
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
