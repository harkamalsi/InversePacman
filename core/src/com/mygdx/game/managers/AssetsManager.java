package com.mygdx.game.managers;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Basically just a wrapper around libGDX's AssetManager class.
 * The fact that our wrapper only differs in name by one character
 * probably isn't the best practice. But anyways, this utilizes
 * the Singleton design pattern. It makes loading and getting
 * assets slightly easier.
 *
 * We probably also should add some more methods to facilitate creating
 * custom fonts. It's kind a pain to do manually where assets are actually being used (whether it's in
 * the systems or screens), so it would be nice if we could
 * abstract that behavior away from there and stick it in here.
 */
public class AssetsManager {

    private static AssetsManager assetsManagerInstance = null;
    private AssetManager assets;

    private AssetsManager()
    {
        assets = new AssetManager();
    }

    public static AssetsManager getInstance()
    {
        if (assetsManagerInstance == null) {
            assetsManagerInstance = new AssetsManager();
        }

        return assetsManagerInstance;
    }

    public void loadMusic(String fileName) {
        assets.load(fileName, Music.class);
    }

    public void loadSound(String fileName) {
        assets.load(fileName, Sound.class);
    }

    /**
     * Loads a simple BitmapFont
     * @param fileName
     */
    public void loadFont(String fileName) {
        assets.load(fileName, BitmapFont.class);
    }

    /**
     * Load a Texture without any parameters
     * @param fileName
     */
    public void loadTexture(String fileName) {
        assets.load(fileName, Texture.class);
    }

    public void loadTextureAtlas(String fileName) {
        assets.load(fileName, TextureAtlasLoader.class);
    }

    public void getMusic(String fileName) {
        assets.get(fileName, Music.class);
    }

    public void getSound(String fileName) {
        assets.get(fileName, Sound.class);
    }

    /**
     * Retrieve a BitmapFont
     * @param fileName
     */
    public void getFont(String fileName) {
        assets.get(fileName, BitmapFont.class);
    }

    public void getTexture(String fileName) {
        assets.get(fileName, Texture.class);
    }

    /**
     * Returns the underlying AssetManager (the libGDX one). Just in
     * case we need to interact with it directly instead of through this
     * wrapper class.
     */
    public AssetManager getAssets() {
        return this.assets;
    }

    public void dispose() {
        assets.dispose();
    }
}
