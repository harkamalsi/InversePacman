package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.SnapshotArray;
import com.mygdx.game.InversePacman;
import com.mygdx.game.components.ButtonComponent;
import com.mygdx.game.components.TableComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.managers.GameScreenManager;
import com.mygdx.game.managers.NetworkManager;

import org.json.JSONArray;
import org.json.JSONObject;

public class TableSystem extends IteratingSystem {
    private SpriteBatch batch;
    private TableComponent cc;
    private ComponentMapper<TableComponent> tableM;
    private ComponentMapper<TransformComponent> tc;
    private NetworkManager networkManager = InversePacman.NETWORKMANAGER;
    private InversePacman app;


    @SuppressWarnings("unchecked")
    public TableSystem(final InversePacman app) {
        super(Family.all(TableComponent.class).get());
        this.app = app;
        tableM = ComponentMapper.getFor(TableComponent.class);
        //tc = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    private void handleLobbyButtonClicked(String lobbyName) {
        this.networkManager.joinLobby(lobbyName, "Pepsi", "pacman");
        app.gsm.setScreen(GameScreenManager.STATE.PLAY, true);

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        //ButtonComponent click = cc.get(entity);
        cc = tableM.get(entity);
        cc.reset();
        JSONArray lobbies = this.networkManager.getLobbies();

        if (networkManager.connected) {
            cc.createLobby = true;
        }

        if (!cc.createLobby && cc.draw) {
            cc.reset();
            cc.addConnectingToServerMessage();
            cc.draw = false;
        }

        if(cc.draw) {
            for (int i = 0; i < lobbies.length(); i++) { //lobbies.length
                JSONObject lobbyObject = lobbies.getJSONObject(i);
                String lobbyName = lobbyObject.getString("lobbyName");
                String lobbyPlayers = lobbyObject.getString("lobbyPlayers");

                cc.addRow(lobbyName, lobbyPlayers);
            }
            cc.draw = false;
        }
        cc.draw();

        if (cc.joinLobby) {
            handleLobbyButtonClicked(cc.getJoinLobbyName());
        }
        cc.joinLobby = false;

    }


}
