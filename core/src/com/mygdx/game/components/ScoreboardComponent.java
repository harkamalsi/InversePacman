package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class ScoreboardComponent implements Component, Pool.Poolable {
    public int pellets = 0;
    public int powerup1 = 0;
    public int powerup2 = 0;
    public long ghostTime = 0;

    @Override
    public void reset() {
        pellets = 0;
        powerup1 = 0;
        powerup2 = 0;
        ghostTime = 0;
    }

}
