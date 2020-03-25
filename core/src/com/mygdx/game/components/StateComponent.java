package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;

public class StateComponent implements Component {
    public static final int STATE_STOPPED = 0;
    public static final int STATE_MOVING = 1;
    public static final int STATE_HIT = 2;

    private int state = 0;

    public void set(int newState){
        state = newState;
    }

    public int get(){
        return state;
    }

}
