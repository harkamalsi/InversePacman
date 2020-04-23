package com.mygdx.game.multiplayermessage;

import com.mygdx.game.InversePacman;
import com.mygdx.game.managers.NetworkManager;
import com.mygdx.game.screens.play.LobbyScreen;

import org.json.JSONArray;
import org.json.JSONObject;


public class MultiplayerMessage {

    private NetworkManager networkManager = InversePacman.NETWORKMANAGER;
    public static String METYPE;

    public MultiplayerMessage() {

    }

    public void sendInput(JSONArray directions) {
        if (LobbyScreen.LOBBY_JOINED != null) {
            networkManager.sendInput(LobbyScreen.LOBBY_JOINED, directions);
        }
    }

    public JSONArray getInput() {
        JSONArray tempResponse = null;
        JSONArray response = new JSONArray();
        if (LobbyScreen.LOBBY_JOINED != null) {
            tempResponse = networkManager.getUpdate(LobbyScreen.LOBBY_JOINED);
            if (tempResponse != null) {
                METYPE = tempResponse.getJSONObject(0).getString("me");
                for (int i = 1; i < tempResponse.length(); i++) {
                    response.put(tempResponse.get(i));
                }
                return response;
            }
        }
        return tempResponse;
    }

}
