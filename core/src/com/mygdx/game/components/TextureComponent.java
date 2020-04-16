package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureComponent implements Component {
    public TextureRegion region;
    public boolean change;
    public boolean bounds;

    public Sprite sprite;

    public TextureComponent(TextureRegion region){
        this.region = new TextureRegion(region);
    }
    public TextureComponent(Sprite sprite, float x, float y, float width, float height, boolean change, boolean bounds) {
        this.sprite = new Sprite(sprite);
        this.sprite.setBounds(x, y, width, height);
        this.change = change;
        this.bounds = bounds;
    }
}
