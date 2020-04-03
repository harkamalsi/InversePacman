package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.components.VelocityComponent;
import java.lang.Math;

public class PlayerInputSystem extends IteratingSystem implements InputProcessor {

    private boolean isDragging = false;
    private boolean isLeftDragged = false;
    private boolean isRightDragged = false;
    private boolean isUpDragged = false;
    private boolean isDownDragged = false;

    private static final float X_VELOCITY = 10f;
    private static final float Y_VELOCITY = 10f;

    private int locationStartTouchedX;
    private int locationStartTouchedY;

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

        Gdx.input.setInputProcessor(this);

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        VelocityComponent vc = velocityM.get(entity);
        TransformComponent tc = transformM.get(entity);
        PlayerComponent pc = playerM.get(entity);

        float x = 0f;
        float y = 0f;

        if (isUpDragged || Gdx.input.isKeyPressed(Input.Keys.I)){
            x = 0f;
            y = Y_VELOCITY;
            System.out.println("Up");
        }

        if (isDownDragged || Gdx.input.isKeyPressed(Input.Keys.K)){
            x = 0f;
            y = -Y_VELOCITY;
            System.out.println("Down");
        }

        if (isLeftDragged || Gdx.input.isKeyPressed(Input.Keys.J)){
            x = -X_VELOCITY;
            y = 0f;
            System.out.println("Left");
        }

        if (isRightDragged || Gdx.input.isKeyPressed(Input.Keys.L)){
            x = X_VELOCITY;
            y = 0f;
            //System.out.println("Right");
        }

        vc.setVelocity(x,y);


    }

    private void toggleDirection(int locationStartTouchedX, int locationStartTouchedY, int screenX, int screenY) {
        System.out.println("locationStartTouchedX: " + locationStartTouchedX + "screenX: " + screenX + "locationStartTouchedY: " + locationStartTouchedY + "screenY: "+ screenY);
        boolean yIsGreater = ((Math.abs(locationStartTouchedY - screenY)) - (Math.abs(locationStartTouchedX - screenX)) > 0);

        if (yIsGreater){
            if ((locationStartTouchedY - screenY) > 0){
                isUpDragged = true;
                isDownDragged = false;
                isLeftDragged = false;
                isRightDragged = false;
            }

            if ((locationStartTouchedY - screenY) < 0){

                isUpDragged = false;
                isDownDragged = true;
                isLeftDragged = false;
                isRightDragged = false;
            }
        }

        if(!yIsGreater) {

            if ((locationStartTouchedX - screenX) > 0){
                isUpDragged = false;
                isDownDragged = false;
                isLeftDragged = true;
                isRightDragged = false;
            }

            if ((locationStartTouchedX - screenX) < 0){
                isUpDragged = false;
                isDownDragged = false;
                isLeftDragged = false;
                isRightDragged = true;
            }

        }


    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        isDragging = true;
        locationStartTouchedX = screenX;
        locationStartTouchedY = screenY;
        System.out.println("touchdown");
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        isDragging = false;
        toggleDirection(locationStartTouchedX,locationStartTouchedY,screenX,screenY);
        System.out.println("touchup");
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
//        System.out.println("dragging");
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