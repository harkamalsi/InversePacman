package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class VelocityComponent implements Component {
<<<<<<< HEAD
    public Vector2 velocity = new Vector2(0, 0);
=======
    public Vector2 velocity = new Vector2(2.5f, 2.5f);
>>>>>>> safe_dev_2
    public Vector2 acceleration = new Vector2(0,0);

    public void setVelocity(float x, float y) {
        velocity.set(x, y);
    }

    public void setAcceleration(float x, float y) {
        acceleration.set(x, y);
    }
}
