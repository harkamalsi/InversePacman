package com.mygdx.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpStatus;
import com.mygdx.game.shared.Constants;
import com.sun.istack.internal.Nullable;

import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;



/**
 * This manager is used to communicate with server.
 * Communication involves sending and receiving data.
 */

public class NetworkManager {


    private Socket socket;


    public NetworkManager() {
        try {
            socket = IO.socket(Constants.HOST);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public void createLobby(Object... args) {
        // args: getNickname(), getPlayerType()
        getSocket().emit(Constants.CREATE_LOBBY, getSocket().id(), args);
    }

    public void joinLobby(Object... args) {
        // args: lobbyName, getNickname(), getPlayerType()
        getSocket().emit(Constants.CREATE_LOBBY, getSocket().id(), args);
    }

    public void sendInput(Object... args) {
        /*
         args: lobbyName, direction
         float direction = Math.random() * 2 * Math.PI;
        */

        getSocket().emit(Constants.INPUT, getSocket().id(), args);
    }

    public void receiveUpdate() {

        getSocket().on(Constants.GAME_UPDATE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject response = (JSONObject)args[0];
            }
        });
    }

    public void getPlayerWithNickname(String nickname) {
        getSocket().emit(Constants.GET_PLAYER_WITH_NICKNAME, getSocket().id(), nickname);
    }

    public void changeNickname(String playerID, String nickname) {
        getSocket().emit(Constants.UPDATE_PLAYER, playerID, nickname);
    }

    public void  getSkinType(String nickname) {
        getSocket().emit(Constants.GET_PLAYER_WITH_NICKNAME, getSocket().id(), nickname);

        getSocket().on(Constants.GET_PLAYER_WITH_NICKNAME, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject response = (JSONObject)args[0];
                int skinType = response.getInt("skinType");
                // return skinType;
            }
        });
    }

    public void changeSkinType(String playerID, String nickname, JSONObject playerUpdate) {
        // TODO: update player
        getSocket().emit(Constants.UPDATE_PLAYER, playerID, nickname, playerUpdate);
    }

    public void getPlayerType(String nickname) {
        getSocket().emit(Constants.GET_PLAYER_WITH_NICKNAME, getSocket().id(), nickname);

        getSocket().on(Constants.GET_PLAYER_WITH_NICKNAME, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject response = (JSONObject)args[0];
                int skinType = response.getInt("skinType");
                // return skinType;
            }
        });
    }

}
