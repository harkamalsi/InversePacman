package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.components.VelocityComponent;

public class MovementSystem extends IteratingSystem {
    private Vector2 temp = new Vector2();

    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<VelocityComponent> vm;

    public MovementSystem() {
        super(Family.all(TransformComponent.class, VelocityComponent.class).get());

        tm = ComponentMapper.getFor(TransformComponent.class);
        vm = ComponentMapper.getFor(VelocityComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        TransformComponent tc = tm.get(entity);
        VelocityComponent vel = vm.get(entity);

        temp.set(vel.acceleration).scl(deltaTime);
        vel.velocity.add(temp);

        temp.set(vel.velocity).scl(deltaTime);
        tc.position.add(temp.x,temp.y);
    }
}
