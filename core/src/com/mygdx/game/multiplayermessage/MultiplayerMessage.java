package com.mygdx.game.multiplayermessage;

import com.mygdx.game.InversePacman;
import com.mygdx.game.managers.NetworkManager;
import com.mygdx.game.screens.play.LobbyScreen;
import com.mygdx.game.shared.Constants;

import org.json.JSONArray;
import org.json.JSONObject;


public class MultiplayerMessage {

    private static final MultiplayerMessage instance = new MultiplayerMessage();

    private NetworkManager networkManager = InversePacman.NETWORKMANAGER;
    public static String METYPE;
    public static String X = "10";
    public static String Y = "10";
    JSONArray tempResponse = new JSONArray();
    JSONArray response = new JSONArray();

    private MultiplayerMessage() {}

    public static MultiplayerMessage getInstance(){
        return instance;
    }

    // Sending request
    public void sendInput() {
        if (LobbyScreen.LOBBY_JOINED != null) {
            networkManager.sendInput(LobbyScreen.LOBBY_JOINED, X, Y);
        }
    }

    public void joinLobby(String lobbyName, String nickname, String type) {
        networkManager.joinLobby(lobbyName, nickname, type);
    }

    public void readyUp(String lobbyName, Boolean isReadyUp) {
        networkManager.readyUp(lobbyName, isReadyUp);
    }

    // Getting response
    public JSONArray getInput() {
        tempResponse = networkManager.getUpdate(LobbyScreen.LOBBY_JOINED);
        if (tempResponse != null) {
            METYPE = tempResponse.getJSONObject(0).getString("me");
            response = new JSONArray();
            for (int i = 1; i < tempResponse.length(); i++) {
                response.put(tempResponse.get(i));
            }
            return response;
        }
        return tempResponse;
    }

    public void createLobby(String nickname, String playerType) {
        networkManager.createLobby(nickname, playerType);
    }

    public String getLobby() {
        return networkManager.getLobby();
    }



    public JSONArray getLobbies() {
        return networkManager.getLobbies();
    }

}
