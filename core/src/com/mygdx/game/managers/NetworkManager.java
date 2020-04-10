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
            socket = IO.socket(Constants.BASE_URL);
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

        getSocket().emit(Constants.INPUT, args);
    }

    public void receiveUpdate() {

        socket.on(Constants.GAME_UPDATE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject response = (JSONObject)args[0];
            }
        });
    }

    public void getPlayerFromNickname(String nickname) {
        Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.GET);
        request.setUrl(Constants.BASE_URL + Constants.API_PLAYERS + Constants.NICKNAME + nickname);
        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                if (httpResponse.getStatus().getStatusCode() == HttpStatus.SC_OK) {
                    String data = httpResponse.getResultAsString();

                    // TODO: Somehow return data
                }
            }

            @Override
            public void failed(Throwable t) {

            }

            @Override
            public void cancelled() {

            }
        });
    }

    public void changeNickname(String id, String nickname) {
        Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.POST);
        request.setUrl(Constants.BASE_URL + Constants.API_PLAYERS + Constants.UPDATE_PLAYER);

        // TODO: Somehow add id and nickname to body

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                if (httpResponse.getStatus().getStatusCode() == HttpStatus.SC_OK) {
                    String data = httpResponse.getResultAsString();

                    // TODO: Somehow return data
                }
            }

            @Override
            public void failed(Throwable t) {

            }

            @Override
            public void cancelled() {

            }
        });
    }

    public void getSkinType(String nickname) {
        // TODO: call getPlayerWithNickname() or getPlayerDatabaseInformation() and then return skin type
    }

    public void changeSkinType(String nickname) {
        // TODO: update player
    }

    public void getPlayerType() {
        // TODO: use socket to get player type or database if it is saved in database
    }

    public void getPlayerDatabaseInformation(@Nullable String id, String nickname) {
        // TODO: call api
    }

}
