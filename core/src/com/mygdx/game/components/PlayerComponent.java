package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
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
    private String type;
    private Vector2 randomPos = null;

    public PlayerComponent() {
        currentState = IDLE;
        hp = 1;
        invincibleTimer = 0;
    }

    public void createPlayerBody(World world, String id, float x, float y, String type, boolean isSensor){
        this.id = id;
        this.type = type;
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(x,y);
        bdef.fixedRotation = true;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(16/2,16/2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.isSensor = isSensor;

        this.body = world.createBody(bdef);
        this.body.createFixture(fixtureDef).setUserData(this);
        shape.dispose();
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public void setCurrentState(int state) {
        currentState = state;
    }

    public void hit(PlayerComponent playerHit) {
        System.out.println(id + " have been hit by " + playerHit.getId());
    }

    public void hitBy(PlayerComponent playerHitBy) {
        System.out.println(id + " have been hit by " + playerHitBy.getId());
    }

    public Body getBody() {
        return body;
    }

    public Vector2 getRandomPos() {
        return randomPos;
    }

    public void setRandomPos(Vector2 randomPos) {
        this.randomPos = randomPos;
    }
}
