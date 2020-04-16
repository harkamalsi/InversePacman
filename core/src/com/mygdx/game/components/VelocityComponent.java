package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class VelocityComponent implements Component {
    public Vector2 velocity = new Vector2(10, 10);
    public Vector2 acceleration = new Vector2(0,0);

    public void setVelocity(float x, float y) {
        velocity.set(x, y);
    }

    public void setAcceleration(float x, float y) {
        acceleration.set(x, y);
    }
}
