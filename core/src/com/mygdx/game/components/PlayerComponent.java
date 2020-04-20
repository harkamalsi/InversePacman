package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
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
    public boolean ai;

    public PlayerComponent() {
        currentState = IDLE;
        hp = 1;
        invincibleTimer = 0;
    }

    public void createPlayerBody(World world, String id, float x, float y){
        this.id = id;
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(x,y);
        bdef.fixedRotation = true;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(30/2,30/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;

        this.body = world.createBody(bdef);
        this.body.createFixture(fixtureDef).setUserData(this);

        body.createFixture(shape, 1.0f);
        shape.dispose();
    }

    public void setCurrentState(int state) {
        currentState = state;
    }

    public void hit() {
        System.out.println(id + " har blitt truffet!");
    }

}
