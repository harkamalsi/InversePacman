package com.mygdx.game;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.managers.GameScreenManager;
import com.mygdx.game.worldbuilder.WorldBuilder;
import com.mygdx.game.managers.SaveManager;


public class InversePacman extends Game {


	// App Variables
	public static final String APP_TITLE = "InversePacman v0.1";
	public static final int APP_WIDTH = 816;
	public static final int APP_HEIGHT = 800;
	public static final int APP_WIDTH_MOBILE = 1080;
	public static final int APP_HEIGHT_MOBILE = 1800;
	public static final int APP_FPS = 60;

	// Game Variables
	public static final int V_WIDTH = 816;
	public static final int V_HEIGHT = 800;

	// Managers
	public GameScreenManager gsm;
	public AssetManager assets;
	public SaveManager saveManager;

	// Batches
	public SpriteBatch batch;
	public ShapeRenderer shapeBatch;

	//ashley engine
	public Engine engine;

	// Texture for testing (Use AssetsManager later an remove this)
	public Texture img;

//	//World building
//	public World world;
//	public Body player;
//	public Box2DDebugRenderer b2dr;
//
//	//Box2d
//	public OrthogonalTiledMapRenderer tmr;
//	public TiledMap map;

	//Camera
	public OrthographicCamera camera;


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

		//Ashley Engine
		engine = new Engine();

		//Setup managers
		assets = new AssetManager();
	gsm = new GameScreenManager(this);
		saveManager = new SaveManager(false);

		//Picture
		img = new Texture("Test1.png");

		// Camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false,V_WIDTH, V_HEIGHT);

//		//world
//		world = new World(new Vector2(0f, 0), false);
//		b2dr = new Box2DDebugRenderer();
//
//
//		//Tiled map creation and WorldBuilder call
//		map = new TmxMapLoader().load("World/InvPac_Maze2.tmx");
//		tmr = new OrthogonalTiledMapRenderer(map);
//		WorldBuilder.parseTiledObjectLayer(world, map.getLayers().get("Collision").getObjects(), map.getLayers().get("BackgroundLayer"));

		//Player
//		player = createPlayer();
	}

	@Override
	public void render() {
		super.render();

		//Disse tre er noe som også bør flyttes til playscreen eller inn i renedringklassen. Den siste er kun en debugger og ska skrus av når alt fungerer.
//		update(Gdx.graphics.getDeltaTime());
//		tmr.setView(camera);
//		b2dr.render(world, camera.combined.scl(1.0f));

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
	}





	//Flytt denne til nødvendig klasse
//	public Body createPlayer(){
//		Body pBody;
//		BodyDef def = new BodyDef();
//		def.type = BodyDef.BodyType.DynamicBody;
//		def.position.set(32,32);
//		def.fixedRotation = true;
//		pBody = world.createBody(def);
//		PolygonShape shape = new PolygonShape();
//		shape.setAsBox(32/2,32/2);
//
//		pBody.createFixture(shape, 1.0f);
//		shape.dispose();
//		return pBody;
//	}

	// Egenlaget update funksjon, kalles på i render. Bør kanskje fjernes og flyttes til playscreen?
//	public void update(float delta) {
//		world.step(1/60f,6,2);
//		inputUpdate(delta);
//	}

	// Bør også fjernes og legges inn i enten playscreen?
	// Får box2d player til å bevege seg.
//	public void inputUpdate(float delta){
//		int horizontalForce = 0;
//		int verticalForce = 0;
//
//		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
//			horizontalForce -= 5;
//		}
//
//		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
//			horizontalForce += 5;
//		}
//		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
//			verticalForce += 5;
//		}
//		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
//			verticalForce -= 5;
//		}
//		player.setLinearVelocity(horizontalForce*50, player.getLinearVelocity().y);
//		player.setLinearVelocity(player.getLinearVelocity().x, verticalForce*50);
//
//
//	}



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

}
