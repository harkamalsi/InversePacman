package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.IntMap;

import java.util.ArrayList;

public class AnimationComponent implements Component {
    public IntMap<Animation> animations = new IntMap<>();

    public AnimationComponent(int keyframe, Animation animation){
        animations.put(keyframe,animation);
    }
}
