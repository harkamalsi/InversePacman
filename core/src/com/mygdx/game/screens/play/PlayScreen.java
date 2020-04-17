package com.mygdx.game.screens.play;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.InversePacman;
import com.mygdx.game.components.CollisionComponent;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.components.VelocityComponent;
import com.mygdx.game.managers.EntityManager;
import com.mygdx.game.managers.GameScreenManager;
import com.mygdx.game.screens.AbstractScreen;
import com.mygdx.game.systems.CollisionSystem;
import com.mygdx.game.systems.MovementSystem;
import com.mygdx.game.systems.PlayerInputSystem;
import com.mygdx.game.systems.RenderingSystem;

import java.util.ArrayList;
import java.util.Random;

public final class PlayScreen extends AbstractScreen {

    private OrthographicCamera camera;

    private SpriteBatch batch;
    private EntityManager entityManager;

    private Texture pacmansprite;

    private Entity pacman;

    private Engine engine;



    private CollisionSystem collisionSystem;
    private MovementSystem movementSystem;
    private PlayerInputSystem playerInputSystem;
    private RenderingSystem renderingSystem;

    private Music song;
    private FileHandle trackdir;
    private FileHandle store;

    private ArrayList<FileHandle> tracks;
    private int tracknr;


    public PlayScreen(final InversePacman app) {
        super(app);


        // Sets the camera; width and height.
        // this.camera = new OrthographicCamera();
        // this.camera.setToOrtho(false, InversePacman.V_WIDTH, InversePacman.V_HEIGHT);
    }

    public void handleInput(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
            app.gsm.setScreen(GameScreenManager.STATE.MAIN_MENU_SCREEN);
            song.dispose();
        }
    }

    @Override
    public void update(float delta) {
        //app.tmr.setView(app.camera);
        //TiledObjectUtil.parseTiledObjectLayer(app.world, app.map.getLayers().get("Collision").getObjects());
        handleInput();
        // Chooses the next song to play if the song has finished
        // Had to add the second condition since it chose to play a new song as I switched screens
        if(!song.isPlaying() && app.gsm.currentState == GameScreenManager.STATE.PLAY){
            System.out.println("song changed");
            // Song needs to be disposed before it is changed
            song.dispose();
            playMusic(tracks, tracknr);
        }


    }


    @Override
    public void show() {


        // To add a new songs, place the file under the folder assets/music/play
        trackdir = Gdx.files.internal("music/play");
        tracks = new ArrayList<FileHandle>();
        // Collects all the songs in the music/play directory to a list
        for(FileHandle track : trackdir.list()) {
            tracks.add(track);
        }

        playMusic(tracks, -1);

        batch = new SpriteBatch();
        playerInputSystem = new PlayerInputSystem();
        movementSystem = new MovementSystem();
        collisionSystem = new CollisionSystem();
        renderingSystem = new RenderingSystem(batch);

        engine = new Engine();
        engine.addSystem(playerInputSystem);
        engine.addSystem(movementSystem);
        engine.addSystem(collisionSystem);
        engine.addSystem(renderingSystem);

        pacmansprite = new Texture("ghosts.png");
        pacman = new Entity();
        pacman.add(new VelocityComponent())
                .add(new TextureComponent(new TextureRegion(pacmansprite)))
                .add(new TransformComponent(20,20))
                .add(new VelocityComponent())
                .add(new CollisionComponent())
                .add(new PlayerComponent());
        engine.addEntity(pacman);

    }

    // Render the PlayScreen, for now only a picture with green background. This method i
    // needed in every screen but can be changed to show different data.
    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //app.b2dr.render(app.world, app.camera.combined);
        app.tmr.render();
        engine.update(delta);

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

    private void playMusic(ArrayList<FileHandle> tracks, int lasttrack) {
        Random track = new Random();
        System.out.println("Start " + tracknr);
        System.out.println("Last track " + lasttrack);
        /* The if statements make sure that the same song never plays twice in a row, unless there
           is only one song
         */
        if(lasttrack > -1 && tracks.size() > 1) {
            store = tracks.remove(lasttrack);

        }

        tracknr = track.nextInt(tracks.size());
        System.out.println("tracknr: " + tracknr);
        if(lasttrack > -1 && !(store == null)) {
            tracks.add(store);
        }
        //tracknr = track.nextInt(tracks.size());
        System.out.println("tracknr: " + tracknr);
        System.out.println("now playing: " + tracks.get(tracknr).name());

        // Plays a random song from the available songs in the music/play directory
        song = Gdx.audio.newMusic(Gdx.files.internal(tracks.get(tracknr).toString()));
        song.setLooping(false);
        song.setVolume(app.music_volume);
        song.play();
    }

    @Override
    public void dispose() {
        song.dispose();
    }
}
