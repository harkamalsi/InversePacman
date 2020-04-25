package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class PillComponent implements Component {

    public Body body, triggerBody;
    private String id;
    private boolean collected = false;
    private boolean powerPill = false;

    public PillComponent() {
    }



    public void createTriggerBody(World world, float x, float y) {
       /* BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(x,y);
        bdef.fixedRotation = true;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(30/4,30/4);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;


        this.triggerBody = world.createBody(bdef);
        triggerBody.createFixture(fixtureDef);
        shape.dispose();

        triggerBody.setLinearDamping(20f);
        triggerBody.setAngularDamping(5f);

        */
    }
    public void createPillBody(World world, String id, float x, float y){
        createTriggerBody(world, x, y);

        this.id = id;

        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set(x,y);
        bdef.fixedRotation = true;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(30/4,30/4);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.isSensor = true;

        this.body = world.createBody(bdef);
        this.body.createFixture(fixtureDef).setUserData(this);

    }

    public void PillCollected() {
        System.out.println(id + " is collected!");
        collected = true;
    }

    public boolean isCollected() {
        return collected;
    }
    public boolean isPowerPill() {
        return powerPill;
    }
    public void setPowerPill(boolean powerPill) {
        this.powerPill = true;
    }

    public void destroyPillBody() {
        body.getWorld().destroyBody(body);
    }
}
