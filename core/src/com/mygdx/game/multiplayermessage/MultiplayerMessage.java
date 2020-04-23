package com.mygdx.game.multiplayermessage;

import com.mygdx.game.InversePacman;
import com.mygdx.game.managers.NetworkManager;
import com.mygdx.game.screens.play.LobbyScreen;
import com.mygdx.game.shared.Constants;

import org.json.JSONArray;
import org.json.JSONObject;


public class MultiplayerMessage {

    private NetworkManager networkManager = InversePacman.NETWORKMANAGER;
    public static String METYPE;
    public static JSONArray DIRECTIONS = new JSONArray();
    JSONArray tempResponse = new JSONArray();
    JSONArray response = new JSONArray();

    public MultiplayerMessage() {

    }

    public void sendInput(JSONArray directions) {
        System.out.println("LOL11111111111");
        if (LobbyScreen.LOBBY_JOINED != null) {
            networkManager.sendInput(LobbyScreen.LOBBY_JOINED, directions);
            System.out.println("LOL22222222222");
        }
    }

    //android:networkSecurityConfig="@xml/network_security_config"

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

}
