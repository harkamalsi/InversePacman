package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.components.AnimationComponent;
import com.mygdx.game.components.StateComponent;
import com.mygdx.game.components.TextureComponent;
import com.badlogic.gdx.graphics.g2d.Animation;

public class AnimationSystem extends IteratingSystem {

    private ComponentMapper<TextureComponent> textureM;
    private ComponentMapper<AnimationComponent> animationM;
    private ComponentMapper<StateComponent> stateM;
    private Animation<TextureRegion> animation;

    public AnimationSystem() {
        super(Family.all(TextureComponent.class, AnimationComponent.class, StateComponent.class).get());
        textureM = ComponentMapper.getFor(TextureComponent.class);
        animationM = ComponentMapper.getFor(AnimationComponent.class);
        stateM = ComponentMapper.getFor(StateComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TextureComponent tex = textureM.get(entity);
        AnimationComponent anim = animationM.get(entity);
        StateComponent state = stateM.get(entity);
        animation = anim.animations.get(state.getState());

        if (animation != null){
            tex.region = animation.getKeyFrame((state.stateTime));
        }
        state.stateTime += deltaTime;

        //loops animations after 6 deltatime
        if (state.stateTime > 6f){
            state.stateTime = 0.0f;
        }
    }
}
