package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.PacmanComponent;
import com.mygdx.game.components.StateComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.components.VelocityComponent;
import com.mygdx.game.multiplayermessage.MultiplayerMessage;
import com.mygdx.game.screens.play.LobbyScreen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.Math;

public class PlayerInputSystem extends IteratingSystem implements InputProcessor {

    private boolean isDragging = false;
    private boolean isLeftDragged = false;
    private boolean isRightDragged = false;
    private boolean isUpDragged = false;
    private boolean isDownDragged = false;

    private boolean multiplayer = false;
    private MultiplayerMessage connection = MultiplayerMessage.getInstance();

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

    private float prevX, prevY = -1;
    private boolean shouldGetUpdate = false;
    private boolean shouldSendUpdate = false;


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


        float x = 0f;
        float y = 0f;
        if(pc.id.equals("PACMAN")) {
            if (!multiplayer) {
                if (isUpDragged || Gdx.input.isKeyPressed(Input.Keys.I)) {
                    x = 0f;
                    y = vc.pacmanVelocity.y;

                    sc.setState(1);
                }

                if (isDownDragged || Gdx.input.isKeyPressed(Input.Keys.K)) {
                    x = 0f;
                    y = -vc.pacmanVelocity.y;

                    sc.setState(2);
                }

                if (isLeftDragged || Gdx.input.isKeyPressed(Input.Keys.J)) {
                    x = -vc.pacmanVelocity.x;
                    y = 0f;

                    sc.setState(3);

                    //flips texture
                    if (texc.region != null && texc.region.isFlipX()) {
                        texc.region.flip(true, false);
                    }
                }

                if (isRightDragged || Gdx.input.isKeyPressed(Input.Keys.L)) {
                    x = vc.pacmanVelocity.x;
                    y = 0f;

                    sc.setState(4);

                    //flips texture
                    if (texc.region != null && !texc.region.isFlipX()) {
                        texc.region.flip(true, false);
                    }

                }

                pc.body.setLinearVelocity(x * 50, pc.body.getLinearVelocity().y);
                pc.body.setLinearVelocity(pc.body.getLinearVelocity().x, y * 50);
            }
        }

        if (multiplayer && LobbyScreen.LOBBY_JOINED != null) {

            JSONArray response = getServerInput();;


            if (response != null) {
                for (int i = 0; i < response.length(); i++) {
                    String otherType = response.getJSONObject(i).getString("type");

                    //System.out.println(response.getJSONObject(i));
                    //JSONArray xy = response.getJSONObject(i).getJSONArray("directions");

                    try {
                        x = Float.parseFloat(response.getJSONObject(i).getString("x"));
                        y = Float.parseFloat(response.getJSONObject(i).getString("y"));
                    } catch (JSONException e) {
                        return;
                    }

                    if (pc.id.equals(otherType) && x != 0 && y != 0) {
                        pc.body.setTransform(x,y,0);
                    }
                }
            }
        }

        if (pc.id.equals(connection.METYPE)) {

            Vector2 playerVelocity = vc.ghostVelocity;
            if (pc.getType().equals("PACMAN")){
                playerVelocity = vc.pacmanVelocity;
            }

            if (isUpDragged || Gdx.input.isKeyPressed(Input.Keys.I)) {
                x = 0f;
                y = playerVelocity.y;
                sc.setState(1);
            }

            if (isDownDragged || Gdx.input.isKeyPressed(Input.Keys.K)) {
                x = 0f;
                y = -playerVelocity.y;

                sc.setState(2);
            }

            if (isLeftDragged || Gdx.input.isKeyPressed(Input.Keys.J)) {
                x = -playerVelocity.x;
                y = 0f;

                sc.setState(3);

                //flips texture
                if (texc.region != null && texc.region.isFlipX()) {
                    texc.region.flip(true, false);
                }
            }

            if (isRightDragged || Gdx.input.isKeyPressed(Input.Keys.L)) {
                x = playerVelocity.x;
                y = 0f;

                sc.setState(4);

                //flips texture
                if (texc.region != null && !texc.region.isFlipX()){
                    texc.region.flip(true,false);
                }

            }

            pc.body.setLinearVelocity(x*50, pc.body.getLinearVelocity().y);
            pc.body.setLinearVelocity(pc.body.getLinearVelocity().x, y*50);



            if (prevX != pc.body.getPosition().x || prevY != pc.body.getPosition().y){
                sendServerInput(pc.body.getPosition().x,pc.body.getPosition().y);
            }

            prevX = pc.body.getPosition().x;
            prevY = pc.body.getPosition().y;

        }
    }

    private JSONArray getServerInput() {
        return connection.getInput();
    }

    private void sendServerInput(float x, float y){
        if (shouldSendUpdate) {
            connection.X = String.valueOf(x);
            connection.Y = String.valueOf(y);

            connection.sendInput();

            shouldSendUpdate = false;
        } else {
            shouldSendUpdate = true;
        }
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
