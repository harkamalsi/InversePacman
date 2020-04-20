package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;

public class TextureComponent implements Component {
    public TextureRegion region;
    public boolean changeOpacity;
    public boolean bounds;
    public boolean changeColor;
    public ArrayList<Float> colors;

    public Sprite sprite;

    public TextureComponent(TextureRegion region){
        this.region = new TextureRegion(region);
    }
    public TextureComponent(Sprite sprite, float x, float y, float width, float height, boolean changeOpacity, boolean bounds, boolean changeColor, float ...rgb) {
        this.sprite = new Sprite(sprite);
        this.sprite.setBounds(x, y, width, height);
        this.changeOpacity = changeOpacity;
        this.bounds = bounds;
        this.changeColor = changeColor;
        this.colors = new ArrayList<Float>();
        for(Float i : rgb) {
            i = i / 255f;
            colors.add(i);
        }

    }
}
