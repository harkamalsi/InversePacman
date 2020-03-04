package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;

import java.util.ArrayList;

public class AnimationComponent implements Component {
    public ArrayList<Animation> animations = new ArrayList<>();
}
