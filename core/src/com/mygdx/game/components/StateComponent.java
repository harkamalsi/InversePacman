package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;

public class StateComponent implements Component {

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

    public float stateTime = 0.0f;

    public StateComponent(int state){
        this.currentState = state;
        stateTime = 0.0f;
        hp = 1;
        invincibleTimer = 0;
    }

    public int getState(){
        return currentState;
    }

    public float getStateTime() {
        return stateTime;
    }

    public StateComponent() {
        this(0);
    }

    public void increaseStateTime(float delta) {
        stateTime += delta;
    }

    public void resetStateTime() {
        stateTime = 0;
    }

    public void setState(int newState) {
        if (currentState == newState) {
            return;
        }
        currentState = newState;
        //stateTime = 0;
    }

}
