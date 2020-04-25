package com.mygdx.game.shared;

public class Constants {
    public static final String HOST = "https://inverse-pacman-api.herokuapp.com";
//    public static final String HOST = "http://localhost:8080";
    //public static final String HOST = "http://10.0.2.2:8080";
    //public static final String HOST = "http://192.168.60.1:8080";

    public static final String GET_GAME_LOBBIES = "get_game_lobbies";
    public static final String JOIN_LOBBY = "join_lobby";
    public static final String GET_LOBBY = "get_lobby";
    public static final String LEAVE_LOBBY = "leave_lobby";
    public static final String CREATE_LOBBY = "create_lobby";
    public static final String PLAYER_JOINED_JOINED = "player_joined_lobby";
    public static final String FULL_LOBBY = "full_lobby";
    public static final String GAME_UPDATE = "game_update";
    public static final String INPUT = "input";
    public static final String READY_UP = "ready_up";
    public static final String GAME_OVER_PACMAN = "pacman_lost";
    public static final String GAME_OVER_GHOSTS = "ghosts_lost";

    public static final String DATABASE_UPDATE = "database_update";
    public static final String GET_ALL_PLAYERS = "get_all_players";
    public static final String ADD_PLAYER = "add_player";
    public static final String UPDATE_PLAYER = "update_player";
    public static final String UPDATE_HIGHSCORE = "update_highscore";
    public static final String GET_PLAYER_WITH_NICKNAME = "get_player_with_nickname";
    public static final String GET_PLAYER_WITH_ID = "get_player_with_id";
    public static final String CHANGE_NICKNAME = "change_nickname";
    public static final String CHANGE_SKINTYPE = "change_skin_type";

    public static final String SINGLE_PLAYER_SCORE_KEY = "spScore";
    public static final String MULTIPLAYER_SCORE_KEY = "mpScore";

    public static final int DEFAULT_PILL_POINTS = 100;
}
