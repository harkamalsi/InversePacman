package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.InversePacman;
import com.mygdx.game.components.MusicComponent;

import java.util.ArrayList;
import java.util.Random;

public class MusicSystem extends IteratingSystem {

    private ArrayList tracks;
    private Music song;
    private Music pauseMusic;
    private Sound sound;
    private InversePacman app;

    private FileHandle trackdir;
    private FileHandle store;
    private FileHandle settings;
    private boolean start;

    private int tracknr;

    private float stored_music_volume;
    private float stored_sound_volume;
    private boolean music;
    private boolean soundon;
    private  boolean pause;
    private float music_volume;
    private float sound_volume;
    private ArrayList<Sound> sounds;
    private Array<Entity> entityArray;
    private MusicComponent mc;


    private ComponentMapper<MusicComponent> musicM;


    public MusicSystem() {
        super(Family.all(MusicComponent.class).get());
        musicM = ComponentMapper.getFor(MusicComponent.class);

        entityArray = new Array<Entity>();

        pauseMusic = Gdx.audio.newMusic(Gdx.files.internal("music/pause/InvPacMan Chill Pause.mp3"));

        tracks = new ArrayList<FileHandle>();

        settings = Gdx.files.local("settings.txt");
        String text = settings.readString();
        String wordsArray[] = text.split("\\r?\\n|,");
        music = Boolean.parseBoolean(wordsArray[1]);
        soundon = Boolean.parseBoolean(wordsArray[3]);
        stored_music_volume = Float.parseFloat(wordsArray[0]);
        stored_sound_volume = Float.parseFloat(wordsArray[2]);
        if(music) {
            music_volume = stored_music_volume;
        }
        if(soundon) {
            sound_volume = stored_sound_volume;
        }


    }

    public void addedToEngine (Engine engine) {
        super.addedToEngine(engine);
    }

    public void dispose() {
        // have to check this because it can crash if screens are switched faster than the music can load
        if(song != null) {
            song.dispose();
            pauseMusic.dispose();
        }
        for(int i = 0; i < sounds.size(); i++) {
                sound = sounds.get(i);
                sound.dispose();
        }
    }

    public void pause() {
        update(1);
        song.pause();
        pauseMusic.play();
        pause = true;
    }

    public void resume() {
        pauseMusic.pause();
        song.play();
        pause = false;
    }

    public void setMusicPosition(float position) {
        update(1);
        song.setPosition(position);
    }

    public float getMusicPosition() {
        update(1);
        return song.getPosition();
    }

    private void playMusic(ArrayList<FileHandle> tracks, int lasttrack) {
        Random track = new Random();

        if(lasttrack > -1 && tracks.size() > 1) {
            store = tracks.remove(lasttrack);
        }

        ArrayList<String> tracklist = new ArrayList<String>();
        for(FileHandle tracktostring : tracks) {
            String name = tracktostring.name();
            tracklist.add(name);
        }

        tracknr = track.nextInt(tracks.size());
        if(lasttrack > -1 && !(store == null)) {
            tracks.add(store);
        }

        // Plays a random song from the available songs in the music/play directory
        song = Gdx.audio.newMusic(Gdx.files.internal(tracks.get(tracknr).toString()));
        song.setLooping(false);
        song.setVolume(music_volume);
        song.play();
    }

    public void playSound(int soundfile) {
        update(1);
        sounds.get(soundfile);
        sound = sounds.get(soundfile);
        sound.play(sound_volume);
    }

    private void setSettings() {
        settings = Gdx.files.local("settings.txt");
        String text = settings.readString();
        String wordsArray[] = text.split("\\r?\\n|,");
        music = Boolean.parseBoolean(wordsArray[1]);
        soundon = Boolean.parseBoolean(wordsArray[3]);
        stored_music_volume = Float.parseFloat(wordsArray[0]);
        stored_sound_volume = Float.parseFloat(wordsArray[2]);
        if(music) {
            music_volume = stored_music_volume;
        }
        if(soundon) {
            sound_volume = stored_sound_volume;
        }
    }

    public float setMusic(float music_volume) {
        this.music_volume = music_volume;
        return this.music_volume;
    }

    private void getMusic() {
        song.setVolume(music_volume);
    }

    @Override
    public void update(float dt) {
        super.update(dt);

        for (Entity entity : entityArray) {
            mc = musicM.get(entity);
            this.trackdir = mc.trackdir;
            this.sounds = new ArrayList<Sound>();
            this.sounds = mc.sounds;

            if(tracks.isEmpty()) {
                for (FileHandle track : mc.trackdir.list()) {
                    tracks.add(track);
                }
            }

            if(!start && !tracks.isEmpty()) {
                playMusic(tracks, -1);
                start = true;
            }


            if(!tracks.isEmpty()) {
                getMusic();
                if (!song.isPlaying() && !pause) {
                    // Song needs to be disposed before it is changed
                    song.dispose();
                    playMusic(tracks, tracknr);
                }
            }
        }
        entityArray.clear();


    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        entityArray.add(entity);
    }
}
