package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.components.VelocityComponent;
import com.mygdx.game.scenes.Hud;
import com.mygdx.game.worldbuilder.WorldBuilder;

public class CollisionEventSystem extends IteratingSystem {

    private ComponentMapper<TransformComponent> transformM;
    private ComponentMapper<PlayerComponent> playerM;

    private float collisionTimer = 0f;


    public CollisionEventSystem() {
        super(Family.all(TransformComponent.class, PlayerComponent.class).get());

        transformM = ComponentMapper.getFor(TransformComponent.class);
        playerM = ComponentMapper.getFor(PlayerComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        TransformComponent tc = transformM.get(entity);
        PlayerComponent pc = playerM.get(entity);

        collisionTimer -= deltaTime;


        //scaling pacman on powerpill pickup
        if (pc.id.equals("PACMAN") && pc.powerMode ){
            if(pc.invincibleTimer > 0){
                Vector2 scalepacman = new Vector2(0.30f,0.30f);
                tc.scale = scalepacman;
                pc.invincibleTimer -= deltaTime;
            }

            else{
                Vector2 scalepacman = new Vector2(0.15f,0.15f);
                tc.scale = scalepacman;
                pc.powerMode = false;
            }

        }

        //resetting position on all players when pacman hit & resetting ghost position in powermode
        if (pc.id.equals("PACMAN") && pc.playerCollidedWith != null) {
            if(pc.playerIdCollidedWith != "PACMAN" && !pc.powerMode && collisionTimer <= 0){
                collisionTimer = 2f;
                pc.hp -= 1;
                Hud.setLives(pc.hp);
                WorldBuilder.resetBodyPositions();
                pc.playerCollidedWith = null;

            }
            else if (pc.powerMode){
                Hud.addPointsToScore(500);
                WorldBuilder.resetBodyPosition(pc.playerCollidedWith);
                pc.playerCollidedWith = null;
            }

        }

    }
}
