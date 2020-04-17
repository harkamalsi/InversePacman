package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.game.components.GhostComponent;
import com.mygdx.game.components.StateComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.components.VelocityComponent;

public class AISystem extends IteratingSystem{


    private static final float X_VELOCITY = 5f;
    private static final float Y_VELOCITY = 5f;


    private ComponentMapper<GhostComponent> ghostM;
    private ComponentMapper<VelocityComponent> velocityM;
    private ComponentMapper<TransformComponent> transformM;
    private ComponentMapper<StateComponent> stateM;
    private ComponentMapper<TextureComponent> texM;

    public AISystem(){
        super(Family.all(GhostComponent.class,VelocityComponent.class,TransformComponent.class,StateComponent.class,TextureComponent.class).get());
        ghostM = ComponentMapper.getFor(GhostComponent.class);
        velocityM = ComponentMapper.getFor(VelocityComponent.class);
        transformM = ComponentMapper.getFor(TransformComponent.class);
        stateM = ComponentMapper.getFor(StateComponent.class);
        texM = ComponentMapper.getFor(TextureComponent.class);

        //Gdx.input.setInputProcessor(this);



    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        GhostComponent gc = ghostM.get(entity);
        VelocityComponent vc = velocityM.get(entity);
        TransformComponent tc = transformM.get(entity);
        StateComponent sc = stateM.get(entity);
        TextureComponent texc = texM.get(entity);

        float x = 0f;
        float y = 0f;

        if (Gdx.input.isKeyPressed(Input.Keys.T)){
            x = 0f;
            y = Y_VELOCITY;

            sc.setState(1);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.G)){
            x = 0f;
            y = -Y_VELOCITY;

            sc.setState(2);
        }


        if (Gdx.input.isKeyPressed(Input.Keys.F)){
            x = -X_VELOCITY;
            y = 0f;

            sc.setState(3);

            //flips texture
            if (texc.region != null && texc.region.isFlipX()){
                texc.region.flip(true,false);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.H)){
            x = X_VELOCITY;
            y = 0f;

            sc.setState(4);
            //flips texture
            if (texc.region != null && !texc.region.isFlipX()){
                texc.region.flip(true,false);
            }
        }

        vc.setVelocity(x,y);
        vc.setAcceleration(x,y);


    }
}


