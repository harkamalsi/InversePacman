package com.mygdx.game.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.InversePacman;
import com.mygdx.game.managers.GameScreenManager;
import com.mygdx.game.screens.AbstractScreen;

public class SplashScreen extends AbstractScreen {
    private TextureRegion bg;
    private BitmapFont font;
    private GlyphLayout layout;
    private float scaleX;
    private float scaleY;

    private SpriteBatch batch;


    public SplashScreen(InversePacman app) {
        super(app);

        bg = new TextureRegion(new Texture("invpac_splash_screen.png"));
        font = new BitmapFont(Gdx.files.internal("font/rubik_font_correct.fnt"));
        layout = new GlyphLayout();

        scaleX = Gdx.graphics.getWidth() / (float)app.APP_WIDTH_MOBILE;
        scaleY = Gdx.graphics.getHeight() / (float)app.APP_HEIGHT_MOBILE;
    }

    @Override
    public void update(float delta) {
        handleInput();
        app.step();
    }

    public void handleInput() {
        if(Gdx.input.justTouched()) {
            app.gsm.setScreen(GameScreenManager.STATE.MAIN_MENU_SCREEN);
        }
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.begin();
        font.setUseIntegerPositions(false);
        font.getData().setScale(scaleX / 2, scaleY / 2);
        batch.draw(bg, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        layout.setText(font,"Press anywhere to continue");
        font.setColor(font.getColor().r, font.getColor().g, font.getColor().b, app.a);
        font.draw(batch, layout, Gdx.graphics.getWidth() / 2 - layout.width / 2, Gdx.graphics.getHeight() / 7 - layout.height / 2);
        batch.end();


    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
