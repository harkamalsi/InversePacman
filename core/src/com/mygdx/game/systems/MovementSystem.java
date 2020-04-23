package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.components.VelocityComponent;
import com.mygdx.game.screens.play.PlayScreen;

public class MovementSystem extends IteratingSystem {
    private Vector2 temp = new Vector2();
    private Vector2 temp2 = new Vector2();

    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<VelocityComponent> vm;
    private ComponentMapper<PlayerComponent> playerM;

    public MovementSystem() {
        super(Family.all(TransformComponent.class, VelocityComponent.class, PlayerComponent.class).get());

        tm = ComponentMapper.getFor(TransformComponent.class);
        vm = ComponentMapper.getFor(VelocityComponent.class);
        playerM = ComponentMapper.getFor(PlayerComponent.class);
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        TransformComponent tc = tm.get(entity);
        VelocityComponent vel = vm.get(entity);
        PlayerComponent pc = playerM.get(entity);

//        temp.set(vel.acceleration).scl(deltaTime);
//        vel.velocity.add(temp);
//
//        temp.set(vel.velocity).scl(deltaTime);
//        tc.position.add(temp.x,temp.y);
        temp2 = pc.body.getPosition();
        temp2.x = (PlayScreen.scaleX *1.3f)*temp2.x/RenderingSystem.PPM;
        temp2.y = (PlayScreen.scaleX *1.3f)*temp2.y/RenderingSystem.PPM;
        tc.position.set(temp2);
//        tc.position.set(temp2).scl(0.0313f,0.0313f);
    }
}
