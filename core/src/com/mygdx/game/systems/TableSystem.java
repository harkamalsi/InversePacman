package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.InversePacman;
import com.mygdx.game.components.TableComponent;
import com.mygdx.game.managers.GameScreenManager;
import com.mygdx.game.managers.NetworkManager;
import com.mygdx.game.multiplayermessage.MultiplayerMessage;
import com.mygdx.game.screens.play.LobbyScreen;
import com.mygdx.game.screens.play.PlayScreen;

import org.json.JSONArray;
import org.json.JSONObject;

public class TableSystem extends IteratingSystem {
    private TableComponent cc;
    private ComponentMapper<TableComponent> tableM;
    private MultiplayerMessage connection = MultiplayerMessage.getInstance();
    private InversePacman app;
    public boolean prevIsReadyUpChecked = false;
    public int startsignal;


    @SuppressWarnings("unchecked")
    public TableSystem(final InversePacman app) {
        super(Family.all(TableComponent.class).get());
        this.startsignal = 0;
        this.app = app;
        tableM = ComponentMapper.getFor(TableComponent.class);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    private void handleLobbyButtonClicked(String lobbyName, String type) {
        this.connection.joinLobby(lobbyName, LobbyScreen.NICKNAME, type);
    }

    public void startGame() {
        app.gsm.setScreen(GameScreenManager.STATE.PLAY);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        cc = tableM.get(entity);
        cc.reset();

        JSONArray lobbies = connection.getLobbies();

        if (NetworkManager.CONNECTED) {
            cc.createLobby = true;
        }

        if (!cc.createLobby && cc.draw) {
            cc.addConnectingToServerMessage();
            cc.draw = false;
        }

        if(cc.draw) {
            cc.addPlayerNickname();
            for (int i = 0; i < lobbies.length(); i++) {
                JSONObject lobbyObject = lobbies.getJSONObject(i);

                String lobbyName = lobbyObject.getString("lobbyName");
                String lobbyPlayers = lobbyObject.getString("lobbyPlayers");

                cc.addRow(lobbyName, lobbyPlayers);
            }
            cc.addReadyUpCheckbox();
            cc.addTypeCheckboxes();
            cc.draw = false;
        }

        cc.draw();

        String lobbyJoined = connection.getLobby();

        if (cc.lobbyButtonClicked) {
            handleLobbyButtonClicked(cc.getJoinLobbyName(), cc.PLAYERTYPE);
        }

        if (lobbyJoined != null) {
            PlayScreen.MULTIPLAYER = true;
            LobbyScreen.LOBBY_JOINED = lobbyJoined;
            cc.lobbyButtonClicked = false;
            cc.resetJoinLobbyName();

            if (prevIsReadyUpChecked != cc.isReadyUpChecked) {
                connection.readyUp(LobbyScreen.LOBBY_JOINED, cc.isReadyUpChecked);
            }
            prevIsReadyUpChecked = cc.isReadyUpChecked;

            if (cc.isReadyUpChecked && LobbyScreen.LOBBY_JOINED != null) {
                cc.isReadyUpChecked = false;
                startsignal = 1;
            }
        }
    }
}
