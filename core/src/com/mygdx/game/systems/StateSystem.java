package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.components.StateComponent;

public class StateSystem extends IteratingSystem {

    private ComponentMapper<StateComponent> stateM;

    public StateSystem() {
        super(Family.all(StateComponent.class).get());

        stateM = ComponentMapper.getFor(StateComponent.class);
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        stateM.get(entity).stateTime += deltaTime;

    }
}
