package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.IntMap;

import java.util.ArrayList;

public class AnimationComponent implements Component {
    //map of animations, can be used for an entity that has one or multiple animations
    public IntMap<Animation> animations = new IntMap<>();

    public AnimationComponent(int keyframe, Animation animation){
        animations.put(keyframe,animation);
    }
}
