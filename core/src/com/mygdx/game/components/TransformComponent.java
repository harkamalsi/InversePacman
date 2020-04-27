package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;


public class TransformComponent implements Component {
    public Vector2 position = new Vector2();

    public Vector2 scale = new Vector2(1, 1);
    public float rotation = 0;

    //different constructors depending on arguments
    public TransformComponent(float x, float y) {
        this(x, y, 1.0f, 1.0f, 0);
    }

    public TransformComponent(float x, float y, float scaleX, float scaleY, float rotation) {
        position.set(x, y);
        scale.set(scaleX, scaleY);
        this.rotation = rotation;
    }

    public TransformComponent(Vector2 position, Vector2 scale, float rotation) {
        this.position.set(position);
        this.scale.set(scale);
        this.rotation = rotation;
    }
}
