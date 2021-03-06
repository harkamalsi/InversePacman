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
import com.mygdx.game.components.MusicComponent;
import com.mygdx.game.managers.GameScreenManager;
import com.mygdx.game.screens.AbstractScreen;
import com.mygdx.game.systems.ButtonSystem;
import com.mygdx.game.systems.MusicSystem;
import com.mygdx.game.systems.RenderingSystem;

// to format the numbers shown for sound and volume
import java.text.DecimalFormat;
import java.util.ArrayList;

public class OptionScreen extends AbstractScreen {

    private OrthographicCamera camera;
    private FitViewport viewport;


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

    private TextureRegion changeTexture;
    private TextureRegion preview;

    private Entity backButton;
    private Entity volume_muteButton;
    private Entity sound_muteButton;

    private Entity increaseVolumButton;
    private Entity decreaseVolumButton;

    private Entity increaseSoundButton;
    private Entity decreaseSoundButton;

    private Entity bgEntity;
    private Entity ellipseEntity;
    private Entity front_ellipseEntity;

    private Entity musicEntity;

    private Entity changeSkinButton;
    private Entity previewEntity;


    private Sprite backSprite;
    private Sprite volume_muteSprite;
    private Sprite sound_muteSprite;

    private Sprite increaseVolumSprite;
    private Sprite decreaseVolumSprite;

    private Sprite increaseSoundSprite;
    private Sprite decreaseSoundSprite;

    private Sprite ellipseSprite;
    private Sprite bgSprite;
    private Sprite front_ellipseSprite;

    private Sprite changeSkinSprite;
    private Sprite previewSprite;

    private Music music;

    private BitmapFont font;

    private float scaleX;
    private float scaleY;


    private DecimalFormat df;
    private GlyphLayout layout;


    public OptionScreen(InversePacman app, Engine engine) {
        super(app, engine);
        this.engine = engine;
        bg = new TextureRegion(new Texture("optionscreen/option_bg.png"));
        back = new TextureRegion(new Texture("back3x.png"));
        settings = new TextureRegion(new Texture("optionscreen/options_button.png"));
        increase_music = new TextureRegion(new Texture("optionscreen/music_louder.png"));
        decrease_music = new TextureRegion(new Texture("optionscreen/music_lower.png"));
        increase_sound = new TextureRegion(new Texture("optionscreen/louder_sound.png"));
        decrease_sound = new TextureRegion(new Texture("optionscreen/lower_sound.png"));
        volume_mute = new TextureRegion(new Texture("optionscreen/mute_music.png"));
        sound_mute = new TextureRegion(new Texture("optionscreen/mute_sound.png"));
        ellipse = new TextureRegion(new Texture("menuscreen/ellipse_color_change_correct.png"));
        front_ellipse = new TextureRegion(new Texture("optionscreen/option_front_ellipse.png"));
        changeTexture = new TextureRegion(new Texture("pacman_skins/pacman.png"));
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
            //engine.removeSystem(musicSystem);
            engine.removeAllEntities();
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

        if(previewEntity.flags == 1) {
            FileHandle skin = Gdx.files.local("skin.txt");
            FileHandle skin_dir = Gdx.files.internal("pacman_skins");
            ArrayList<String> skinList = new ArrayList<String>();
            for(FileHandle skintostring : skin_dir.list()) {
                String name = skintostring.path();
                skinList.add(name);
            }

            app.skin_number += 1;
            app.skin_number = app.skin_number % skinList.size();
            skin.writeString(skinList.get(app.skin_number), false);
            app.skin = skinList.get(app.skin_number);
            drawPreview(app.skin);


        }

    }

    private void drawPreview(String name) {
        if(previewEntity != null) {
            engine.removeEntity(previewEntity);
        }
        preview = new TextureRegion(new Texture(name));
        previewSprite = new Sprite(preview);
        previewEntity = new Entity();
        app.addSpriteEntity(previewSprite, previewEntity, engine, Gdx.graphics.getWidth() / 2 - previewSprite.getRegionWidth()*scaleX / 4,Gdx.graphics.getHeight() / 15, previewSprite.getRegionWidth()*scaleX / 2, previewSprite.getRegionHeight()*scaleX / 2, true, false, false, false);
    }

    @Override
    public void update(float delta) {
        handleInput();
        app.step();

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
        app.addSpriteEntity(bgSprite, bgEntity, engine, 0, -2, Gdx.graphics.getWidth() +2, Gdx.graphics.getHeight() +2, false,false,false, false);


        ellipseSprite = new Sprite(ellipse);
        ellipseEntity = new Entity();
        app.addSpriteEntity(ellipseSprite, ellipseEntity, engine, Gdx.graphics.getWidth() / 2 - (ellipse.getRegionWidth() / 2 * (scaleX)), Gdx.graphics.getHeight() - (ellipse.getRegionHeight() * (scaleY)), ellipse.getRegionWidth() * (scaleX), ellipse.getRegionHeight() * (scaleY), false,true, true, false);


        front_ellipseSprite = new Sprite(front_ellipse);
        front_ellipseEntity = new Entity();
        app.addSpriteEntity(front_ellipseSprite, front_ellipseEntity, engine, Gdx.graphics.getWidth() / 2 - (front_ellipse.getRegionWidth() / 2 * (scaleX)), Gdx.graphics.getHeight() / (float)1.17, front_ellipse.getRegionWidth() * (scaleX), front_ellipse.getRegionHeight() * (scaleY), false,false, false, false);

        // ***** Increase music button START *****

        increaseVolumSprite = new Sprite(increase_music);
        increaseVolumButton = new Entity();
        app.addSpriteEntity(increaseVolumSprite, increaseVolumButton, engine, Gdx.graphics.getWidth() / (float)1.175 - (increaseVolumSprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)1.95 - (increaseVolumSprite.getRegionHeight() / 2 * scaleY), increaseVolumSprite.getRegionWidth() * scaleX, increaseVolumSprite.getRegionHeight() * scaleY,true,false, false, false);

        // ***** Increase music button END *****

        // ***** Decrease music button START *****

        decreaseVolumSprite = new Sprite(decrease_music);
        decreaseVolumButton = new Entity();
        app.addSpriteEntity(decreaseVolumSprite, decreaseVolumButton, engine, Gdx.graphics.getWidth() / (float)1.5 - (decreaseVolumSprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)1.95 - (decreaseVolumSprite.getRegionHeight() / 2 * scaleY), decreaseVolumSprite.getRegionWidth() * scaleX, decreaseVolumSprite.getRegionHeight() * scaleY, true,false, false, false);

        // ***** Decrease music button END *****


        // ***** Increase sound button START *****

        increaseSoundSprite = new Sprite(increase_sound);
        increaseSoundButton = new Entity();
        app.addSpriteEntity(increaseSoundSprite, increaseSoundButton, engine, Gdx.graphics.getWidth() / (float)1.175 - (increaseSoundSprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)4.55 - (increaseSoundSprite.getRegionHeight() / 2 * scaleY), increaseSoundSprite.getRegionWidth() * scaleX, increaseSoundSprite.getRegionHeight() * scaleY,true,false, false, false);

        // ***** Increase sound button END *****

        // ***** Decrease sound button START *****

        decreaseSoundSprite = new Sprite(decrease_sound);
        decreaseSoundButton = new Entity();
        app.addSpriteEntity(decreaseSoundSprite, decreaseSoundButton, engine, Gdx.graphics.getWidth() / (float)1.5 - (decreaseSoundSprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)4.55 - (decreaseSoundSprite.getRegionHeight() / 2 * scaleY), decreaseSoundSprite.getRegionWidth() * scaleX, decreaseSoundSprite.getRegionHeight() * scaleY,true,false, false, false);

        // ***** Decrease sound button END *****



        // ***** Mute music button START *****

        volume_muteSprite = new Sprite(volume_mute);
        volume_muteButton = new Entity();
        app.addSpriteEntity(volume_muteSprite, volume_muteButton, engine,Gdx.graphics.getWidth() / (float)3 - (volume_muteSprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)1.95 - (volume_muteSprite.getRegionHeight() / 2 * scaleY), volume_muteSprite.getRegionWidth() * scaleX, volume_muteSprite.getRegionHeight() * scaleY,true,false, false, false);

        // ***** Mute music button END *****

        // ***** Mute sound button START *****

        sound_muteSprite = new Sprite(sound_mute);
        sound_muteButton = new Entity();
        app.addSpriteEntity(sound_muteSprite, sound_muteButton, engine, Gdx.graphics.getWidth() / (float)3 - (sound_muteSprite.getRegionWidth() / 2 * scaleX), Gdx.graphics.getHeight() / (float)4.55 - (sound_muteSprite.getRegionHeight() / 2 * scaleY), sound_muteSprite.getRegionWidth() * scaleX, sound_muteSprite.getRegionHeight() * scaleY,true,false, false, false);

        // ***** Mute sound button END *****

        // ***** Back button START *****

        backSprite = new Sprite(back);
        backButton = new Entity();
        app.addSpriteEntity(backSprite, backButton, engine, 0, 0, backSprite.getRegionWidth() * scaleX, backSprite.getRegionHeight() * scaleX, true,false, false, false);

        // ***** Back button END *****

        musicEntity = new Entity();
        musicEntity.add(new MusicComponent(Gdx.files.internal("music/pause")));
        engine.addEntity(musicEntity);

// probably make method of this or system


        preview = new TextureRegion(new Texture(app.skin));
        previewSprite = new Sprite(preview);
        previewEntity = new Entity();
        app.addSpriteEntity(previewSprite, previewEntity, engine, Gdx.graphics.getWidth() / 2 - previewSprite.getRegionWidth()*scaleX / 4,Gdx.graphics.getHeight() / 15, previewSprite.getRegionWidth()*scaleX / 2, previewSprite.getRegionHeight()*scaleX / 2, true, false, false, false);

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        engine.update(delta);

        batch.begin();
        //font.setColor(font.getColor().r, font.getColor().g, font.getColor().b, 0.5f);
        font.setUseIntegerPositions(false);
        font.getData().setScale(scaleX / 32f, scaleY / 32f);
        layout.setText(font,"Options");
        //font.setColor(0,78, 59,a);
        font.draw(batch,layout, (Gdx.graphics.getWidth() / 64f - layout.width / 2f),(Gdx.graphics.getHeight() / (1.05f * 32f) - (layout.height / 2f)));

        layout.setText(font, "music");
        font.draw(batch,layout, Gdx.graphics.getWidth() / (2 *32f) - layout.width / 2,Gdx.graphics.getHeight() / (1.5f * 32f) + layout.height / 2);
        layout.setText(font, df.format(app.music_volume * 100) + "%");
        font.draw(batch,layout, Gdx.graphics.getWidth() / (6 * 32f) - layout.width / 2,Gdx.graphics.getHeight() / (1.90f * 32f));
        layout.setText(font, "sound");
        font.draw(batch, layout, Gdx.graphics.getWidth() / (2 * 32f) - layout.width / 2,Gdx.graphics.getHeight() / (2.95f * 32f));
        layout.setText(font, df.format(app.sound_volume * 100) + "%");
        font.draw(batch, layout,Gdx.graphics.getWidth() / (6 * 32f) - layout.width / 2,Gdx.graphics.getHeight() / (4.2f * 32f));
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
