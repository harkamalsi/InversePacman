package com.mygdx.game.managers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.components.CollisionComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.components.VelocityComponent;
import com.mygdx.game.worldbuilder.WorldBuilder;

import java.util.Comparator;

public class EntityManager {
    private Engine engine;
    private Family family;
    private Comparator comparator;
    private World world;


    public EntityManager(){

    }
    public Entity createPlayerPacman(String internalpath){
        // Create the player Entity and all the components that will go in the entity

        Texture pacmansprite = new Texture(internalpath);

        Entity pacman = new Entity();
        pacman.add(new VelocityComponent())
                .add(new TextureComponent(new TextureRegion(pacmansprite)))
                .add(new TransformComponent(20,20))
                .add(new VelocityComponent())
                .add(new CollisionComponent())
                .add(WorldBuilder.getPlayerList().get(0));

        return pacman;

    }
    private void createPlayerGhost(){

    }

    private void createGhostAi(){

    }

    public Body createPlayer(){
        Body pBody;
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(32,32);
        def.fixedRotation = true;
        pBody = world.createBody(def);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(32/2,32/2);

        pBody.createFixture(shape, 1.0f);
        shape.dispose();
        return pBody;
    }
}
