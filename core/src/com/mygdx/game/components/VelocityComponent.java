package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class VelocityComponent implements Component {
    public Vector2 velocity = new Vector2(1, 1);
    public Vector2 acceleration = new Vector2();

    public void setVelocity(float x, float y) {
        velocity.set(x, y);
    }
}
