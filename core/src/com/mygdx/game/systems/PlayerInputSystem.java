package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.components.VelocityComponent;

public class PlayerInputSystem extends IteratingSystem implements InputProcessor {

    private boolean isDragging = false;
    private boolean isLeftDragged = false;
    private boolean isRightDragged = false;
    private boolean isUpDragged = false;
    private boolean isDownDragged = false;

    private float X_VELOCITY = 20f;
    private float Y_VELOCITY = 20f;

    private int locationStartTouchedX = 0;
    private int locationStartTouchedY = 0;

    private int locationEndTouchedX = 0;
    private int locationEndTouchedY = 0;


    private ComponentMapper<VelocityComponent> velocityM;
    private ComponentMapper<TransformComponent> transformM;
    private ComponentMapper<PlayerComponent> playerM;
    //private ComponentMapper<StateComponent> stateM;

    public PlayerInputSystem(){
        super(Family.all(VelocityComponent.class,TransformComponent.class,PlayerComponent.class).get());
        velocityM = ComponentMapper.getFor(VelocityComponent.class);
        transformM = ComponentMapper.getFor(TransformComponent.class);
        playerM = ComponentMapper.getFor(PlayerComponent.class);

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        VelocityComponent vc = velocityM.get(entity);
        TransformComponent tc = transformM.get(entity);
        PlayerComponent pc = playerM.get(entity);

        float x = 0f;
        float y = 0f;

        if (isUpDragged){
            y = Y_VELOCITY;
        }

        if (isDownDragged){
            y = -Y_VELOCITY;
        }

        if (isLeftDragged){
            x = -X_VELOCITY;
        }

        if (isRightDragged){
            x = X_VELOCITY;
        }

        vc.setVelocity(x,y);


    }

    private void toggleDirection(int locationStartTouchedX, int locationStartTouchedY, int screenX, int screenY) {
        boolean yIsGreater = ((locationStartTouchedY + screenY) - (locationStartTouchedX + screenX) > 0);

        if (yIsGreater){
            if ((locationStartTouchedY - screenY) > 0){
                isUpDragged = true;
            }

            if ((locationStartTouchedY - screenY) < 0){
                isDownDragged = true;
            }
        }

        if (!yIsGreater){

            if ((locationStartTouchedX - screenX) > 0){
                isLeftDragged = true;
            }

            if ((locationStartTouchedX - screenX) < 0){
                isRightDragged = true;
            }

        }


    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        isDragging = true;
        locationStartTouchedX = screenX;
        locationStartTouchedY = screenY;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        isDragging = false;
        toggleDirection(locationStartTouchedX,locationStartTouchedY,screenX,screenY);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        isDragging = true;
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
