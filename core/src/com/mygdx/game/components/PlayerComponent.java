package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;

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

    public PlayerComponent() {
        currentState = IDLE;
        hp = 1;
        invincibleTimer = 0;
    }

}
