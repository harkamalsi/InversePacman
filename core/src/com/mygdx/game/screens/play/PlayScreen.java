package com.mygdx.game.screens.play;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.InversePacman;
import com.mygdx.game.components.CollisionComponent;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.components.VelocityComponent;
import com.mygdx.game.managers.EntityManager;
import com.mygdx.game.managers.GameScreenManager;
import com.mygdx.game.screens.AbstractScreen;
import com.mygdx.game.systems.AnimationSystem;
import com.mygdx.game.systems.CollisionSystem;
import com.mygdx.game.systems.MovementSystem;
import com.mygdx.game.systems.PlayerInputSystem;
import com.mygdx.game.systems.RenderingSystem;
import com.badlogic.gdx.files.FileHandle;



//import java.io.File;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.naming.Context;


public final class PlayScreen extends AbstractScreen {

    private OrthographicCamera camera;

    private SpriteBatch batch;
    private EntityManager entityManager;

    private Texture pacmansprite;

    private Entity pacman;

    private Engine engine;
    private FileHandle fileHandle;


    private CollisionSystem collisionSystem;
    private MovementSystem movementSystem;
    private PlayerInputSystem playerInputSystem;
    private RenderingSystem renderingSystem;

    private Music song;
    private FileHandle track1;
    private FileHandle track2;

    private ArrayList<FileHandle> tracks;


    public BitmapFont font = new BitmapFont(); //or use alex answer to use custom font

    public PlayScreen(final InversePacman app) {
        super(app);

        // Sets the camera; width and height.
//        this.camera = new OrthographicCamera();
//        this.camera.setToOrtho(false, InversePacman.V_WIDTH, InversePacman.V_HEIGHT);


    }

    public void handleInput(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
            app.gsm.setScreen(GameScreenManager.STATE.MAIN_MENU_SCREEN);
            song.dispose();
        }
    }

    @Override
    public void update(float delta) {
        handleInput();
        if(!song.isPlaying() && app.gsm.currentState == GameScreenManager.STATE.PLAY){
            System.out.println("song changed");
            playMusic(tracks);
        }


    }

    private List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        //System.out.println("filenames" + files.toString());
        for (File file : files) {
            //System.out.println("filename: " + file.toString());
            if (file.isDirectory()) {
                inFiles.addAll(getListFiles(file));
            } else {
                if(file.getName().endsWith(".mp3")){
                    inFiles.add(file);
                }
            }
        }
        System.out.println(inFiles);
        return inFiles;
    }

    @Override
    public void show() {

        track1 = Gdx.files.internal("music/play/track1.mp3");
        track2 = Gdx.files.internal("music/play/track2.mp3");



        ArrayList<FileHandle> tracks = new ArrayList<FileHandle>();
        tracks.add(track1);
        tracks.add(track2);
        System.out.println(tracks);

        playMusic(tracks);

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
        Gdx.gl.glClearColor(0, 1, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        app.batch.begin();
        app.batch.draw(app.img, 0, 0);
        app.batch.end();
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


    private void playMusic(ArrayList<FileHandle> tracks) {
        //This only works for the desktop application, I couldn't make it work for android
        /*System.out.println("path " + Gdx.files.getLocalStoragePath() + "music/play");
        List<File> files = getListFiles(new File( "music/play"));

        Random track = new Random();
        int tracknr = track.nextInt(files.size());
        File songfile = files.get(tracknr);
        song = Gdx.audio.newMusic(Gdx.files.internal(songfile.getPath()));*/
        System.out.println(tracks);


        Random track = new Random();
        int tracknr = track.nextInt(tracks.size());
        //File songfile = tracks.get(tracknr);

        song = Gdx.audio.newMusic(Gdx.files.internal(tracks.get(tracknr).toString()));

        song.setLooping(false);
        song.setVolume(0.5f);
        song.play();
        //Gdx.files.getExternalStoragePath();
    }


    @Override
    public void dispose() {
        song.dispose();
    }
}
