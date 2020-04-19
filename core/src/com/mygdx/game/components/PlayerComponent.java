package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class PlayerComponent implements Component {
    public static final int IDLE = 0;
    public static final int MOVE_UP = 1;
    public static final int MOVE_DOWN = 2;
    public static final int MOVE_LEFT = 3;
    public static final int MOVE_RIGHT = 4;
    public static final int HURT = 5;
    public static final int DIE = 6;


    public int currentState;

    public int hp;

    public float invincibleTimer;

    public Body body;
    public String id;

    public Body createPlayer(World world, String id, float x, float y){
        Body pBody;
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(32,100);
        def.fixedRotation = true;
        pBody = world.createBody(def);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(32/2,32/2);

        pBody.createFixture(shape, 1.0f);
        shape.dispose();
        return pBody;
    }

    public PlayerComponent() {
        currentState = IDLE;
        hp = 1;
        invincibleTimer = 0;
    }

    public void setCurrentState(int state) {
        currentState = state;
    }

}
