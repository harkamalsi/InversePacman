package com.mygdx.game;

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

	//World building
	public World world;
	public Body player;
	public Box2DDebugRenderer b2dr;

	//Box2d
	public OrthogonalTiledMapRenderer tmr;
	public TiledMap map;

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


	// Creates The managers,
	@Override
	public void create () {
        FileHandle settings = Gdx.files.local("settings.txt");
		String text = settings.readString();
		String wordsArray[] = text.split("\\r?\\n|,");
		System.out.println(wordsArray);
		for(int i = 0; i < wordsArray.length; i++) {
			System.out.println(wordsArray[i]);
		}
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

		// Camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false,V_WIDTH, V_HEIGHT);

		//world
		world = new World(new Vector2(0f, 0), false);
		b2dr = new Box2DDebugRenderer();


		//Tiled map creation and WorldBuilder call
		map = new TmxMapLoader().load("World/InvPac_Maze2.tmx");
		tmr = new OrthogonalTiledMapRenderer(map);
		WorldBuilder.parseTiledObjectLayer(world, map.getLayers().get("Collision").getObjects(), map.getLayers().get("BackgroundLayer"));

		//Player
		player = createPlayer();
	}

	@Override
	public void render() {
		super.render();

		//Disse tre er noe som også bør flyttes til playscreen eller inn i renedringklassen. Den siste er kun en debugger og ska skrus av når alt fungerer.
		update(Gdx.graphics.getDeltaTime());
		tmr.setView(camera);
		b2dr.render(world, camera.combined.scl(1.0f));

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





	//Flytt denne til nødvendig klasse
	public Body createPlayer(){
		Body pBody;
		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.DynamicBody;
		def.position.set(32,32);
		def.fixedRotation = true;
		pBody = world.createBody(def);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(32/2,32/2);

		pBody.createFixture(shape, 1.0f);
		shape.dispose();
		return pBody;
	}

	// Egenlaget update funksjon, kalles på i render. Bør kanskje fjernes og flyttes til playscreen?
	public void update(float delta) {
		world.step(1/60f,6,2);
		inputUpdate(delta);
	}

	// Bør også fjernes og legges inn i enten playscreen?
	// Får box2d player til å bevege seg.
	public void inputUpdate(float delta){
		int horizontalForce = 0;
		int verticalForce = 0;

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			horizontalForce -= 5;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			horizontalForce += 5;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			verticalForce += 5;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			verticalForce -= 5;
		}
		player.setLinearVelocity(horizontalForce*50, player.getLinearVelocity().y);
		player.setLinearVelocity(player.getLinearVelocity().x, verticalForce*50);


	}



	@Override
	public void dispose() {
		batch.dispose();
		shapeBatch.dispose();
		assets.dispose();
	}
}
