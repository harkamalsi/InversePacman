package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class PlayerTestComponent implements Component {

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

        public void createPlayerBody(World world, String id, float x, float y){
            this.id = id;
            BodyDef bdef = new BodyDef();
            bdef.type = BodyDef.BodyType.DynamicBody;
            bdef.position.set(x,y);
            bdef.fixedRotation = true;
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(32/2,32/2);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 1.0f;

            this.body = world.createBody(bdef);
            this.body.createFixture(fixtureDef).setUserData(this);

            body.createFixture(shape, 1.0f);
            shape.dispose();
        }

        public PlayerTestComponent() {
            currentState = IDLE;
            hp = 1;
            invincibleTimer = 0;
        }

        public void setCurrentState(int state) {
            currentState = state;
        }

    }
