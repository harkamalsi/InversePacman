package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Rectangle;


public class CollisionComponent implements Component {
    //public Entity collisionEntity;
    public final Rectangle bounds = new Rectangle();
}
