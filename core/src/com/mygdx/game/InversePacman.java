package com.mygdx.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.components.ButtonComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.managers.GameScreenManager;
import com.mygdx.game.managers.SaveManager;

import java.io.File;
import java.io.IOException;

public class InversePacman extends Game {


	// App Variables
	public static final String APP_TITLE = "InversePacman v0.1";
	public static final int APP_WIDTH_MOBILE = 1080;
	public static final int APP_HEIGHT_MOBILE = 1800;
	public static final int APP_WIDTH = 1200;
	public static final int APP_HEIGHT = 2220;
	public static final int APP_FPS = 60;

	// Game Variables
	public static final int V_WIDTH = 720;
	public static final int V_HEIGHT = 420;

	// Managers
	public GameScreenManager gsm;
	public AssetManager assets;
	public SaveManager saveManager;

	// Batches
	public SpriteBatch batch;
	public ShapeRenderer shapeBatch;

	// Texture for testing (Use AssetsManager later an remove this)
	public Texture img;


	public float stored_music_volume;
	public float stored_sound_volume;
	// Music volume for our game. When setting music volume use this variable
	public float music_volume;
	// Sound volume for our game. When setting sound volume use this variable
	public float sound_volume;

	public boolean music;
	public boolean sound;



	public float a;

	public float scale;
	private boolean change = false;
	public float b = (float)(Math.PI / 2);
	private boolean bright = false;

	// Creates The managers,
	@Override
	public void create () {

		//FileHandle file = new FileHandle(Gdx.files.internal("settings.txt").path());
		//file.
		//System.out.println(file.exists());

		/*File file = new File(Gdx.files.internal("settings.txt").path());
		if(!file.exists()) {
			try {
				System.out.println("Yellow");
				file.getParentFile().mkdirs();
				file.createNewFile();


			}
			catch (IOException e){
				System.out.println(e);
			}
		}*/

		if(!Gdx.files.local("settings.txt").exists()){
			FileHandle put = Gdx.files.local("settings.txt");
			try {
				put.file().createNewFile();
			}
			catch (IOException e) {
				System.out.println(e);
			}
			put.copyTo(Gdx.files.local("."));

		}
        FileHandle settings = Gdx.files.local("settings.txt");
		String text = settings.readString();
		String wordsArray[] = text.split("\\r?\\n|,");
		try {
			music = Boolean.parseBoolean(wordsArray[1]);
			sound = Boolean.parseBoolean(wordsArray[3]);
			stored_music_volume = Float.parseFloat(wordsArray[0]);
			stored_sound_volume = Float.parseFloat(wordsArray[2]);
		}
		catch (ArrayIndexOutOfBoundsException e) {
			settings.writeString(0.5 + "," + true + "\n", false);
			settings.writeString(0.5 + "," + true, true);
			music = true;
			sound = true;
			stored_music_volume = (float)0.5;
			stored_sound_volume = (float)0.5;
		}
		if(music) {
			music_volume = stored_music_volume;
		}
		if(sound) {
			sound_volume = stored_sound_volume;
		}

		batch = new SpriteBatch();
		shapeBatch = new ShapeRenderer();

		//Setup managers
		assets = new AssetManager();
		gsm = new GameScreenManager(this);
		saveManager = new SaveManager(false);

		//Picture
		img = new Texture("Test1.png");
		//System.out.println(scaleX + " " + scaleY);
	}

	@Override
	public void render() {
		super.render();

		// Changing the different screens based on the button pressed, should be changed to touch inputs from menu.
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}

		/*else if (Gdx.input.isKeyPressed(Input.Keys.P) && gsm.currentState == GameScreenManager.STATE.MAIN_MENU_SCREEN) {
			System.out.println("PAUSING!");
			gsm.setScreen(GameScreenManager.STATE.PAUSE);
		}
		else if (Gdx.input.isKeyJustPressed(Input.Keys.R) && gsm.currentState == GameScreenManager.STATE.PAUSE) {
			System.out.println("UNPAUSING!");
			//gsm.popScreen();
			gsm.setScreen(GameScreenManager.STATE.PLAY);
			//gsm.popScreen();
		}*/
		else if (Gdx.input.isKeyPressed(Input.Keys.S) && gsm.currentState == GameScreenManager.STATE.PLAY) {
			gsm.setScreen(GameScreenManager.STATE.SINGLE_PLAYER_GHOSTS_BOARD_SCREEN);
		}
		else if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
			gsm.setScreen((GameScreenManager.STATE.LEADERBOARD_MENU_SCREEN));

		}
	}

	@Override
	public void dispose() {
		batch.dispose();
		shapeBatch.dispose();
		assets.dispose();
	}

	// used to change the opacity of textures
	public void step() {
		if (!bright) {
			a += 0.01f; // 0.01f is your time step - "how fast change"
			if (a >= 1.0f) {
				bright = true;
			}
		} else if (bright) {
			a -= 0.01f;
			if (a <= 0.0f) {
				bright = false;
			}
		}
	}

	public void step_scale() {
	    if(change) {
            b -= 0.01f;
            if(b <= 0.25f){
                change = false;
            }
        }
	    else if(b <= Math.PI) {
	        b += 0.01f;
	        if(b >= (float)(Math.PI / 2 - 0.25)){
	            change = true;
            }
        }
	}

	public void addSpriteEntity(Sprite sprite, Entity entity, Engine engine, float x, float y, float width, float height, boolean isButton, boolean change, boolean bounds) {
		entity.add(new TextureComponent(sprite, x, y, width, height, change, bounds))
				.add(new TransformComponent(x, y));
		if(isButton){
			entity.add(new ButtonComponent(x, y, width, height));
		}
		engine.addEntity(entity);
	}


}
