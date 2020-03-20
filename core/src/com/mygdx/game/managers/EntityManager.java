package com.mygdx.game.managers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.components.SpriteComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.badlogic.ashley.core.Entity;
import com.mygdx.game.components.VelocityComponent;
import com.mygdx.game.systems.RenderingSystem;

import java.util.Comparator;

public class EntityManager {
    private Engine engine;
    private Family family;
    private Comparator comparator;

    public EntityManager(Engine e, SpriteBatch batch){
        engine = e;


        RenderingSystem rs = new RenderingSystem(batch);
        engine.addSystem(rs);
    }
    private void createPlayerPacman(){
        // Create the player Entity and all the components that will go in the entity
        Entity entity = new Entity();
        entity.add(new VelocityComponent())
                .add(new SpriteComponent());

        // add the entity to the engine
        engine.addEntity(entity);
    }
    private void createPlayerGhost(){

    }

    private void createGhostAi(){

    }
}
