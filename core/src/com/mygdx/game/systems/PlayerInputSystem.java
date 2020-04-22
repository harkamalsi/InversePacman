package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.InversePacman;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.PacmanComponent;
import com.mygdx.game.components.StateComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.components.VelocityComponent;
import com.mygdx.game.managers.GameScreenManager;
import com.mygdx.game.managers.NetworkManager;

import org.json.JSONArray;

import java.lang.Math;

public class PlayerInputSystem extends IteratingSystem implements InputProcessor {

    private boolean isDragging = false;
    private boolean isLeftDragged = false;
    private boolean isRightDragged = false;
    private boolean isUpDragged = false;
    private boolean isDownDragged = false;

    private boolean multiplayer = false;
    private NetworkManager networkManager = InversePacman.NETWORKMANAGER;

    private static final float X_VELOCITY = 2.5f;
    private static final float Y_VELOCITY = 2.5f;

    private Vector2 temp;

    private int locationStartTouchedX;
    private int locationStartTouchedY;

    private ComponentMapper<PacmanComponent> pacmanM;
    private ComponentMapper<VelocityComponent> velocityM;
    private ComponentMapper<TransformComponent> transformM;
    private ComponentMapper<StateComponent> stateM;
    private ComponentMapper<TextureComponent> texM;
    private ComponentMapper<PlayerComponent> playerM;


    public PlayerInputSystem(boolean multiplayer){
        super(Family.all(PlayerComponent.class,VelocityComponent.class,TransformComponent.class,StateComponent.class,TextureComponent.class).get());
        velocityM = ComponentMapper.getFor(VelocityComponent.class);
        transformM = ComponentMapper.getFor(TransformComponent.class);
        stateM = ComponentMapper.getFor(StateComponent.class);
        texM = ComponentMapper.getFor(TextureComponent.class);
        playerM = ComponentMapper.getFor(PlayerComponent.class);
        pacmanM = ComponentMapper.getFor(PacmanComponent.class);

        this.multiplayer = multiplayer;

        Gdx.input.setInputProcessor(this);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PlayerComponent pc = playerM.get(entity);
//        PacmanComponent pacmanc = pacmanM.get(entity);
        VelocityComponent vc = velocityM.get(entity);
        TransformComponent tc = transformM.get(entity);
        StateComponent sc = stateM.get(entity);
        TextureComponent texc = texM.get(entity);


        if (multiplayer){
            String lobbyName = networkManager.getLobby();
            JSONArray directionBooleans = new JSONArray();
            directionBooleans.put(isUpDragged);
            directionBooleans.put(isRightDragged);
            directionBooleans.put(isDownDragged);
            directionBooleans.put(isLeftDragged);
            networkManager.sendInput(lobbyName, directionBooleans);
        }

        else {
            float x = 0f;
            float y = 0f;


            if (pc.id == "PACMAN") {
                if (isUpDragged || Gdx.input.isKeyPressed(Input.Keys.I)) {
                    x = 0f;
                    y = vc.velocity.y;

                    sc.setState(1);
                }

                if (isDownDragged || Gdx.input.isKeyPressed(Input.Keys.K)) {
                    x = 0f;
                    y = -vc.velocity.y;

                    sc.setState(2);
                }


                if (isLeftDragged || Gdx.input.isKeyPressed(Input.Keys.J)) {
                    x = -vc.velocity.x;
                    y = 0f;

                    sc.setState(3);

                    //flips texture
                    if (texc.region != null && texc.region.isFlipX()) {
                        texc.region.flip(true, false);
                    }
                }

                if (isRightDragged || Gdx.input.isKeyPressed(Input.Keys.L)) {
                    x = vc.velocity.x;
                    y = 0f;

                    sc.setState(4);

                    //flips texture
                    if (texc.region != null && !texc.region.isFlipX()) {
                        texc.region.flip(true, false);
                    }
                }

                //sets velocity direction dictated by x and y
                pc.body.setLinearVelocity(x * 50, pc.body.getLinearVelocity().y);
                pc.body.setLinearVelocity(pc.body.getLinearVelocity().x, y * 50);

                if (multiplayer) {
                    // TODO: Dont need to call this everytime!
                    String lobbyName = networkManager.getLobby();

                    if (lobbyName != null) {
                        System.out.println("LobbyName: " + lobbyName);
                        JSONArray directionBooleans = new JSONArray();
                        directionBooleans.put(isLeftDragged);
                        directionBooleans.put(isRightDragged);
                        directionBooleans.put(isUpDragged);
                        directionBooleans.put(isDownDragged);
                        networkManager.sendInput(lobbyName, directionBooleans);
                    }

                    getServerInput();
                }
            }
        }
    }

    private void getServerInput(){
        System.out.println(networkManager.getUpdate());
    }

    //function for deciding drag direction
    private void toggleDirection(int locationStartTouchedX, int locationStartTouchedY, int screenX, int screenY) {
        // This is great code!
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
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (isDragging){
            toggleDirection(locationStartTouchedX,locationStartTouchedY,screenX,screenY);
        }
        isDragging = false;
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
