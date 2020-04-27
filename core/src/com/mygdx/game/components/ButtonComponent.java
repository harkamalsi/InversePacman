package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class ButtonComponent implements Component {
    protected int x, y, width, height;
    protected TextureRegion texture;
    public Rectangle bounds = new Rectangle();

    public ButtonComponent(float x, float y, float width, float height) {
        bounds.set(x, y, width, height);

    }


}

