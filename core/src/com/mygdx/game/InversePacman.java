package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.game.managers.GameScreenManager;
import com.mygdx.game.managers.NetworkManager;
import com.mygdx.game.shared.Constants;

import java.util.Arrays;

public class InversePacman extends Game {


	// App Variables
	public static final String APP_TITLE = "InversePacman v0.1";
	public static final int APP_WIDTH = 1200;
	public static final int APP_HEIGHT = 2220;
	public static final int APP_FPS = 60;

	// Game Variables
	public static final int V_WIDTH = 720;
	public static final int V_HEIGHT = 420;

	// Managers
	public GameScreenManager gsm;
	public AssetManager assets;

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


	// Creates The managers,
	@Override
	public void create () {
		//NetworkManager nm = new NetworkManager();
		//nm.joinLobby("lobby0","foker", "pacman");
		//nm.createLobby("foker", "pacman");
		//nm.addPlayer("pepsi");
		//nm.getPlayerWithNickname("foker");
		//nm.getPlayerWithID("5e974d393e2369898849a59");
		//nm.changeNickname("5e974d393e2369898849a590", "pepsi");
		//nm.changeSkinType("5e976bcdae78bbff0ac11b6d", 1);

		// System.out.println(nm.getSocket());
		// System.out.println(nm.getSocketID());


		if(!Gdx.files.local("settings.txt").exists()){
			FileHandle put = Gdx.files.internal("settings.txt");
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

		//Picture
		img = new Texture("Test1.png");
	}

	@Override
	public void render() {
		super.render();

		// Changing the different screens based on the button pressed, should be changed to touch inputs from menu.
		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
		}

		else if (Gdx.input.isKeyPressed(Input.Keys.P)) {
			gsm.setScreen(GameScreenManager.STATE.PAUSE);
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.R) && gsm.currentState == GameScreenManager.STATE.PAUSE) {
			gsm.setScreen(GameScreenManager.STATE.PLAY);
			//gsm.popScreen();
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.S) && gsm.currentState == GameScreenManager.STATE.PLAY) {
			gsm.setScreen(GameScreenManager.STATE.SINGLE_PLAYER_BOARD_SCREEN);
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
		shapeBatch.dispose();
		assets.dispose();
	}
}
