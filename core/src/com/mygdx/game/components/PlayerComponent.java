package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.scenes.Hud;
import com.mygdx.game.screens.play.PlayScreen;

public class PlayerComponent implements Component {


    public int hp;

    public float invincibleTimer;

    public String playerIdCollidedWith;
    public Body playerCollidedWith;


    public boolean powerMode;

    public Body body;
    public String id;
    private String type;
    private Vector2 randomPos = null;

    public PlayerComponent() {
        hp = 3;
        invincibleTimer = 0;
        powerMode = false;
    }

    public void createPlayerBody(World world, String id, float x, float y, String type, boolean isSensor){
        this.id = id;
        this.type = type;
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(x,y);
        bdef.fixedRotation = true;
        PolygonShape shape = new PolygonShape();


        shape.setAsBox((PlayScreen.scaleX)*11,(PlayScreen.scaleX)*11);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.isSensor = isSensor;

        this.body = world.createBody(bdef);
        this.body.createFixture(fixtureDef).setUserData(this);

        shape.dispose();
        Hud.setLives(hp);
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
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
