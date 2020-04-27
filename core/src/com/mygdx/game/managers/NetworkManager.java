package com.mygdx.game.managers;

import com.mygdx.game.shared.Constants;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URISyntaxException;
import java.util.UUID;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * This manager is used to communicate with server.
 * Communication involves sending and receiving data.
 */

public class NetworkManager {
    private Socket socket = null;
    private String socketID;
    public static Boolean CONNECTED = false;
    private boolean fetch = false;

    private JSONArray lobbies = new JSONArray();
    private JSONObject player = new JSONObject();
    private JSONArray serverInput;
    private String lobby = null;
    private JSONArray players = new JSONArray();
    private JSONObject response;
    String playerId;
    String playerNickname;

    SaveManager saveManager;

    public NetworkManager(SaveManager saveManager) {
        setSocket();
        this.saveManager = saveManager;
    }

    public void setSocket() {
        if (socket==null){
            try {
                socket = IO.socket(Constants.HOST);
                initializeSocket();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }
    public Socket getSocket() {
        return socket;
    }
    private void initializeSocket(){
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                socketID = socket.connect().id();
                fetch = true;
                CONNECTED = true;

                playerId = saveManager.loadDataValue("_id", String.class);
                if (playerId == null || playerId.isEmpty()) {
                    fetchPlayer();
                }
            }
        }).on(Socket.EVENT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args != null) {

                }
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {

            }
        });

        socket.connect();

        //socket.on("ping", sendPong);

        /*Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while(true) {
                        sleep(0);
                        socket.on("ping", sendPong);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();*/


        /*socket.on(Socket.EVENT_RECONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                socket.emit("joinSocket", socketID);
            }
        });*/
    }

    // Single lobby
    // Fetching a single lobby
    private void setLobby(String lobby) {
        this.lobby = lobby;
    }
    private void fetchLobby() {
        getSocket().emit(Constants.GET_LOBBY, socketID);

        getSocket().on(Constants.GET_LOBBY, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject response = (JSONObject) args[0];
                setLobby(response.getString("lobby"));
            }
        });

    }
    public String getLobby() {
        this.fetchLobby();
        String tempLobby = this.lobby;
        this.lobby = null;
        return tempLobby;
    }

    // Creating, joining and leaving a lobby
    public void createLobby(Object ...args) {
        // args: nickname, type

        final JSONObject inputs = new JSONObject();
        inputs.put("nickname", args[0]);
        inputs.put("type", args[1]);

        getSocket().emit(Constants.CREATE_LOBBY, socketID, inputs);

    }

    public void joinLobby(Object ...args) {
        // args: lobbyName, getNickname(), getPlayerType()
        if (fetch) {
            final JSONObject inputs = new JSONObject();
            inputs.put("lobbyName", args[0]);
            inputs.put("nickname", args[1]);
            inputs.put("type", args[2]);

            getSocket().emit(Constants.JOIN_LOBBY, socketID, inputs);

        }

    }

    public void readyUp(String lobbyName, Boolean isReadyUp) {
        getSocket().emit(Constants.READY_UP, socketID, lobbyName, isReadyUp);
    }

    public void leaveLobby(String lobbyName) {
        // args: lobbyName
        getSocket().emit(Constants.LEAVE_LOBBY, socketID, lobbyName);
    }

    // Input sending a receiving
    public void sendInput(Object ...args) {
        /*
         args: lobbyName, x position (float), y position (float)
        */

        final JSONObject inputs = new JSONObject();
        inputs.put("lobbyName", args[0]);
        inputs.put("x", args[1]);
        inputs.put("y", args[2]);

        getSocket().emit(Constants.INPUT, socketID, inputs);
    }

    private void setUpdate(JSONObject me, JSONArray others) {
        JSONArray input = new JSONArray();

        JSONObject myObject = new JSONObject();
        myObject.put("me", me.getString("type"));
        input.put(myObject);

        for(int i = 0; i < others.length(); i++) {
            input.put(others.get(i));
        }

        this.serverInput = input;
    }
    private void fetchUpdate(String lobbyName) {

        getSocket().on(Constants.GAME_UPDATE + "_" + lobbyName, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject response = (JSONObject)args[0];
                JSONObject me = response.getJSONObject("me");
                JSONArray others = response.getJSONArray("others");

                setUpdate(me, others);
            }
        });
    }
    public JSONArray getUpdate(String lobbyName) {
        fetchUpdate(lobbyName);
        return serverInput;
    }

    // Multiple lobbies

    // Fetching lobbies
    private void setLobbies(JSONArray lobbies) {
        this.lobbies = lobbies;
    }
    public JSONArray getLobbies() {
        if (fetch) {
            getSocket().emit(Constants.GET_GAME_LOBBIES, socketID);

            getSocket().on(Constants.GET_GAME_LOBBIES, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONArray response = (JSONArray) args[0];
                    setLobbies(response);
                }
            });
        }
        return lobbies;
    }

    // Player sockets
    private void setPlayer(JSONObject player) {
        this.player = player;
    }
    private void fetchPlayerWithNickname(final String nickname) {
        if (fetch) {
            getSocket().emit(Constants.GET_PLAYER_WITH_NICKNAME, socketID, nickname);

            getSocket().on(Constants.DATABASE_UPDATE, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject response = (JSONObject)args[0];
                    if (response == null) {
                        JSONObject errorResponse = new JSONObject();
                        errorResponse.put("error", "No user found!");
                        System.out.println(errorResponse);
                    }
                    setPlayer(response);
                }
            });

            fetch = false;
        }

    }
    public JSONObject getPlayerWithNickname(String nickname) {
        if (fetch) {
            this.fetchPlayerWithNickname(nickname);
            return this.player;
        }
        return null;
    }

    private void fetchPlayerWithID(final String id) {
        if (fetch) {
            getSocket().emit(Constants.GET_PLAYER_WITH_ID, socketID, id);

            getSocket().on(Constants.DATABASE_UPDATE, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject response = (JSONObject)args[0];
                    System.out.println(response);
                }
            });

            fetch = false;
        }
    }
    public JSONObject getPlayerWithID(String id) {
        if (fetch) {
            this.fetchPlayerWithID(id);
            return this.player;
        }
        return null;
    }

    public void changeNickname(final String id, final String nickname) {
        if (fetch) {
            getSocket().emit(Constants.CHANGE_NICKNAME, socketID, id, nickname);

            fetch = false;
        }
    }

    public void changeSkinType(final String id, final int skinType) {
       if (fetch) {
           getSocket().emit(Constants.CHANGE_SKINTYPE, socketID, id, skinType);

           fetch = false;
       }
    }

    // Score sockets
    public void updateSingleplayerScore(Object ...args) {
        // args: id, spScore
        if (fetch) {
            final JSONObject inputs = new JSONObject();
            inputs.put("id", args[0]);

            JSONArray scores = new JSONArray();
            scores.put(args[1]);
            scores.put(0);
            inputs.put("spScore", scores);
            inputs.put("mpScore", -1);

            getSocket().emit(Constants.UPDATE_HIGHSCORE, socketID, inputs);

            getSocket().on(Constants.DATABASE_UPDATE, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject response = (JSONObject)args[0];
                    System.out.println(response);
                }
            });

        }
    }

    public void updateMultiplayerScore(String id, String playerType) {
        // args: id, mpScore
        if (fetch) {
            final JSONObject inputs = new JSONObject();
            inputs.put("id", id);
            inputs.put("spScore", -1);

            int pacmanScore = 0, ghostsScore = 0;
            for (int i = 0; i < players.length(); i++) {
                JSONObject playerObject = (JSONObject)players.get(i);
                if (playerObject.getString("_id").equals(playerId)) {
                    pacmanScore = playerObject.getJSONArray("mpScore").getInt(0);
                    ghostsScore = playerObject.getJSONArray("mpScore").getInt(1);
                }
            }

            JSONArray scores = new JSONArray();
            if (playerType == "PACMAN") {
                pacmanScore++;
            } else {
                ghostsScore++;
            }
            scores.put(pacmanScore);
            scores.put(ghostsScore);
            inputs.put("mpScore", scores);

            getSocket().emit(Constants.UPDATE_HIGHSCORE, socketID, inputs);

            getSocket().on(Constants.DATABASE_UPDATE, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject response = (JSONObject)args[0];
                    System.out.println(response);
                }
            });
        }
    }

    public void setPlayers(JSONArray players) {
        this.players = players;
    }

    public JSONArray getPlayers() {
        return players;
    }

    public void fetchAllPlayers() {
        if (fetch) {
            getSocket().emit(Constants.GET_ALL_PLAYERS, socketID);

            getSocket().on(Constants.GET_ALL_PLAYERS, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONArray response = (JSONArray) args[0];
                    System.out.println("Players response: " + response);
                    setPlayers(response);
                }
            });
        }
    }

    public String getPlayerId() {
        return playerId;
    }

    private void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    private void setPlayerNickname(String playerNickname) {
        this.playerNickname = playerNickname;
    }

    public void fetchPlayer() {
        if (fetch) {
            String newId = "Kyle " +  UUID.randomUUID().toString().substring(0, 4);
            getSocket().emit(Constants.ADD_PLAYER, socketID, newId);

            getSocket().on(Constants.DATABASE_UPDATE, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    JSONObject response = (JSONObject)args[0];
                    setPlayerId(response.getString("_id"));
                    setPlayerNickname(response.getString("nickname"));

                    saveManager.saveDataValue("_id", playerId);
                    saveManager.saveDataValue("nickname", playerNickname);
                }
            });
        }
    }

}
