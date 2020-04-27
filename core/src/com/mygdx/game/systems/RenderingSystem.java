package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;


public class RenderingSystem extends IteratingSystem {
    //pixels per meter
    public static final float PPM = 32.0f;
    //we are going to have some sort of viewport
    static final float CAMERA_WIDTH = Gdx.graphics.getWidth()/PPM;
    static final float CAMERA_HEIGHT = Gdx.graphics.getHeight()/PPM;

    public static final float PIXELS_TO_METRES = 1.0f / PPM; // get the ratio for converting pixels to metres

    // convenience method to convert pixels to meters
    public static float PixelsToMeters(float pixelValue){
        return pixelValue * PIXELS_TO_METRES;
    }
    private SpriteBatch batch;

    private Array<Entity> renderQueue;
    public OrthographicCamera cam;
    private TextureComponent tex;
    private TransformComponent t;

    private boolean bright = false;
    public float a;

    //component mappers
    private ComponentMapper<TextureComponent> textureM;
    private ComponentMapper<TransformComponent> transformM;

    public RenderingSystem(SpriteBatch batch) {
        super(Family.all(TransformComponent.class, TextureComponent.class).get());

        this.batch = batch;
        textureM = ComponentMapper.getFor(TextureComponent.class);
        transformM = ComponentMapper.getFor(TransformComponent.class);

        renderQueue = new Array<Entity>();

        cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
        cam.position.set(CAMERA_WIDTH / 2f, CAMERA_HEIGHT / 2f, 0);

    }




    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        step();
      
        cam.update();
        batch.setProjectionMatrix(cam.combined);
        batch.enableBlending();
        batch.begin();


        for (Entity entity : renderQueue) {
            tex = textureM.get(entity);
            t = transformM.get(entity);

            if (tex.region == null && tex.sprite == null) {
                continue;
            }


            if(!(tex.region == null)) {
                float width = tex.region.getRegionWidth();
                float height = tex.region.getRegionHeight();

                float originX = width/2f;
                float originY = height/2f;
                batch.draw(tex.region,
                        t.position.x - originX, t.position.y - originY,
                        originX, originY,
                        width, height,
                        PixelsToMeters(t.scale.x), PixelsToMeters(t.scale.y),
                        t.rotation);
            }
            if(!(tex.sprite == null)) {

                if(tex.bounds) {
                    tex.sprite.setBounds(tex.sprite.getX() / PPM, tex.sprite.getY() / PPM, tex.sprite.getWidth() / PPM, tex.sprite.getHeight() / PPM);
                    tex.bounds = false;
                }
                if(tex.changeOpacity) {
                    tex.sprite.setColor(tex.sprite.getColor().r,tex.sprite.getColor().g, tex.sprite.getColor().b,a);
                    if(tex.changeColor) {
                        try {
                            tex.sprite.setColor(tex.colors.get(0), tex.colors.get(1), tex.colors.get(2), tex.sprite.getColor().a);
                        }
                        catch (IndexOutOfBoundsException e) {
                            System.out.println("Wrong color format RGB! Setting standard values for: " + tex.sprite);
                        }
                    }
                    tex.sprite.draw(batch);
                }
                if(!tex.changeOpacity) {
                    batch.draw(tex.sprite, tex.sprite.getX() / PPM, tex.sprite.getY() / PPM, tex.sprite.getWidth() / PPM, tex.sprite.getHeight() / PPM);
                }

            }

        }

        batch.end();
        renderQueue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        renderQueue.add(entity);
    }

    public OrthographicCamera getCamera() {
        return cam;
    }

    public void step() {
        if (!bright) {
            a += 0.01f; // 0.01f is your time step - "how fast change"
            if (a >= 1.0f) {
                bright = true;
            }
        } else if (bright) {
            a -= 0.01f;
            if (a <= 0.0f) {
                bright = false;
            }
        }
    }
}
