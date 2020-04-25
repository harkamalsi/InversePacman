package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;
import java.util.Arrays;

public class MusicComponent implements Component {

    public FileHandle trackdir;
    public ArrayList<Sound> sounds;

    public MusicComponent(FileHandle trackdir, Sound...sounds) {
        this.trackdir = trackdir;
        this.sounds = new ArrayList<Sound>();
        this.sounds.addAll(Arrays.asList(sounds));
    }
}
