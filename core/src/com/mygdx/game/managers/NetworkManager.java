package com.mygdx.game.managers;

import com.mygdx.game.shared.Constants;

import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;



/**
 * This manager is used to communicate with server.
 * Communication involves sending and receiving data.
 */

public class NetworkManager {


    private Socket socket;
    private String socketID;

    public NetworkManager() {
        socket = new SocketIOManager().getSocketInstance();
        socketID = socket.connect().id();
    }

    public Socket getSocket() {
        return socket;
    }

    public void createLobby(Object ...args) {
        // args: getNickname(), getPlayerType()
        System.out.println("Create Lobby is called!");

        final JSONObject inputs = new JSONObject();
        inputs.put("nickname", args[0]);
        inputs.put("type", args[1]);

        getSocket().on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                socketID = getSocket().connect().id();
                getSocket().emit(Constants.CREATE_LOBBY, socketID, inputs);
            }
        });

    }

    public void joinLobby(Object ...args) {
        // args: lobbyName, getNickname(), getPlayerType()
        System.out.println("Join Lobby is called!");

        final JSONObject inputs = new JSONObject();
        inputs.put("lobbyName", args[0]);
        inputs.put("nickname", args[1]);
        inputs.put("type", args[2]);

        getSocket().on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                socketID = getSocket().connect().id();
                getSocket().emit(Constants.JOIN_LOBBY, socketID, inputs);
            }
        });

    }

    public void sendInput(Object ...args) {
        /*
         args: lobbyName, direction
         float direction = Math.random() * 2 * Math.PI;
        */

        System.out.println("Send Input is called!");

        final JSONObject inputs = new JSONObject();
        inputs.put("lobbyName", args[0]);
        inputs.put("direction", args[1]);

        getSocket().on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                socketID = getSocket().connect().id();
                getSocket().emit(Constants.INPUT, socketID, inputs);
            }
        });


    }

    public void receiveUpdate() {

        System.out.println("Receive update is called!");

        getSocket().on(Constants.GAME_UPDATE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject response = (JSONObject)args[0];
                System.out.println(response);
            }
        });
    }

    public void addPlayer(final String nickname) {
        System.out.println("Add player is called!");

        getSocket().on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                socketID = getSocket().connect().id();
                getSocket().emit(Constants.ADD_PLAYER, socketID, nickname);
            }
        });

        getSocket().on(Constants.DATABASE_UPDATE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject response = (JSONObject)args[0];
                System.out.println(response);
            }
        });
    }

    public void getPlayerWithNickname(final String nickname) {
        System.out.println("Get player with nickname is called!");

        getSocket().on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                socketID = getSocket().connect().id();
                getSocket().emit(Constants.GET_PLAYER_WITH_NICKNAME, socketID, nickname);
            }
        });

        getSocket().on(Constants.DATABASE_UPDATE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject response = (JSONObject)args[0];
                if (response == null) {
                    JSONObject errorResponse = new JSONObject();
                    errorResponse.put("error", "No user found!");
                    System.out.println(errorResponse);
                }
                System.out.println(response);
            }
        });

    }

    public void getPlayerWithID(final String id) {
        System.out.println("Get player with ID is called!");

        getSocket().on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                socketID = getSocket().connect().id();
                getSocket().emit(Constants.GET_PLAYER_WITH_ID, socketID, id);
            }
        });

        getSocket().on(Constants.DATABASE_UPDATE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject response = (JSONObject)args[0];
                System.out.println(response);
            }
        });
    }

    public void changeNickname(final String id, final String nickname) {
        System.out.println("Change nickname is called!");

        getSocket().on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                socketID = getSocket().connect().id();
                getSocket().emit(Constants.CHANGE_NICKNAME, socketID, id, nickname);
            }
        });

        getSocket().on(Constants.DATABASE_UPDATE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject response = (JSONObject)args[0];
                System.out.println(response);
            }
        });
    }

    public void changeSkinType(final String id, final int skinType) {
        System.out.println("Change skin type is called!");

        getSocket().on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                socketID = getSocket().connect().id();
                getSocket().emit(Constants.CHANGE_SKINTYPE, socketID, id, skinType);
            }
        });

        getSocket().on(Constants.DATABASE_UPDATE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject response = (JSONObject)args[0];
                System.out.println(response);
            }
        });
    }

}