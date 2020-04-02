package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;

public class PlayerComponent implements Component {
//    public static final int IDLE = 0;
//    public static final int MOVE_UP = 1;
//    public static final int MOVE_DOWN = 2;
//    public static final int MOVE_LEFT = 3;
//    public static final int MOVE_RIGHT = 4;
//    public static final int HURT = 5;
//    public static final int DIE = 6;

    private enum States {
        IDLE,
        MOVE_UP,
        MOVE_DOWN,
        MOVE_LEFT,
        MOVE_RIGHT,
        HURT,
        DIE


    }

    public States currentState;

    public int hp;

    public float invincibleTimer;

    public PlayerComponent() {
        currentState = States.IDLE;
        hp = 1;
        invincibleTimer = 0;
    }

    public void setCurrentState(States state) {
        currentState = state;
    }

}
