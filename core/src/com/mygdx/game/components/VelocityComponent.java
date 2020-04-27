package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class VelocityComponent implements Component {
    public Vector2 pacmanVelocity = new Vector2(2.5f, 2.5f);
    public Vector2 ghostVelocity = new Vector2(2f,2f);
    public Vector2 acceleration = new Vector2(0,0);

}
