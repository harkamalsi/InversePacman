package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;

public class StateComponent implements Component {


    public int currentState;

    public int hp;

    public float invincibleTimer;

    public float stateTime;

    public StateComponent(int state){
        this.currentState = state;
        stateTime = 0.0f;
        hp = 1;
        invincibleTimer = 0;
    }

    public int getState(){
        return currentState;
    }

    public void setState(int newState) {
        if (currentState == newState) {
            return;
        }
        currentState = newState;
    }

}
