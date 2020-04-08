package com.mygdx.game.systems;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.mygdx.game.InversePacman;
import com.mygdx.game.managers.GameScreenManager;

import java.util.ArrayList;
import java.util.Random;

public class MusicSystem extends EntitySystem {

    private ArrayList tracks;
    private Music song;
    private InversePacman app;

    private FileHandle trackdir;
    private FileHandle store;
    private FileHandle settings;

    private int tracknr;

    private float stored_music_volume;
    private float stored_sound_volume;
    private boolean music;
    private boolean sound;
    private  boolean pause;
    private float music_volume;
    private float sound_volume;

    public MusicSystem(FileHandle trackdir) {
        this.trackdir = trackdir;
        settings = Gdx.files.local("settings.txt");
        String text = settings.readString();
        String wordsArray[] = text.split("\\r?\\n|,");
        music = Boolean.parseBoolean(wordsArray[1]);
        sound = Boolean.parseBoolean(wordsArray[3]);
        stored_music_volume = Float.parseFloat(wordsArray[0]);
        stored_sound_volume = Float.parseFloat(wordsArray[2]);
        if(music) {
            music_volume = stored_music_volume;
        }
        if(sound) {
            sound_volume = stored_sound_volume;
        }
        tracks = new ArrayList<FileHandle>();
        for(FileHandle track : trackdir.list()) {
            tracks.add(track);
        }
        System.out.println("music volume: " + music_volume);
        playMusic(tracks, -1);

    }

    public void addedToEngine (Engine engine) {
        super.addedToEngine(engine);
    }

    public void dispose() {
        song.dispose();
    }

    public void pause() {
        song.pause();
        pause = true;
    }

    public void resume() {
        song.play();
        pause = false;
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
        ArrayList<String> tracklist = new ArrayList<String>();
        for(FileHandle tracktostring : tracks) {
            String name = tracktostring.name();
            tracklist.add(name);
        }
        System.out.println("tracklist: " + tracklist);
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
        song.setVolume(music_volume);
        song.play();
    }

    private void setSettings() {
        settings = Gdx.files.local("settings.txt");
        String text = settings.readString();
        String wordsArray[] = text.split("\\r?\\n|,");
        music = Boolean.parseBoolean(wordsArray[1]);
        sound = Boolean.parseBoolean(wordsArray[3]);
        stored_music_volume = Float.parseFloat(wordsArray[0]);
        stored_sound_volume = Float.parseFloat(wordsArray[2]);
        if(music) {
            music_volume = stored_music_volume;
        }
        if(sound) {
            sound_volume = stored_sound_volume;
        }
    }

    public float setMusic(float music_volume) {
        this.music_volume = music_volume;
        System.out.println("got music: " + music_volume);
        return this.music_volume;
    }

    private void getMusic() {
        song.setVolume(music_volume);
    }

    @Override
    public void update(float dt) {
        getMusic();
        if(!song.isPlaying() && !pause){
            System.out.println("updating music");
            System.out.println("song changed");
            // Song needs to be disposed before it is changed
            song.dispose();
            playMusic(tracks, tracknr);
        }

    }
}
