package com.mygdx.game.managers;

import com.mygdx.game.InversePacman;
import com.mygdx.game.screens.AbstractScreen;
import com.mygdx.game.screens.leaderboard.MultiplayerBoardScreen;
import com.mygdx.game.screens.leaderboard.MultiplayerGhostsBoardScreen;
import com.mygdx.game.screens.leaderboard.MultiplayerNamcapBoardScreen;
import com.mygdx.game.screens.leaderboard.SinglePlayerBoardScreen;
import com.mygdx.game.screens.leaderboard.SinglePlayerGhostsBoardScreen;
import com.mygdx.game.screens.leaderboard.SinglePlayerNamcapBoardScreen;
import com.mygdx.game.screens.menu.InGameMenuScreen;
import com.mygdx.game.screens.menu.LeaderboardMenuScreen;
import com.mygdx.game.screens.menu.MainMenuScreen;
import com.mygdx.game.screens.menu.OptionScreen;
import com.mygdx.game.screens.play.GameOverScreen;
import com.mygdx.game.screens.play.LobbyScreen;
import com.mygdx.game.screens.play.PauseScreen;
import com.mygdx.game.screens.play.PlayScreen;

import java.util.HashMap;

public class GameScreenManager {

    public final InversePacman app;
    private HashMap<STATE, AbstractScreen> gameScreens;
    public enum STATE {
        PLAY,
        PAUSE,
        LOBBY_SCREEN,
        GAME_OVER_SCREEN,
        MAIN_MENU_SCREEN,
        IN_GAME_MENU_SCREEN,
        SINGLE_PLAYER_GHOSTS_BOARD_SCREEN,
        SINGLE_PLAYER_NAMPAC_BOARD_SCREEN,
        MULTIPLAYER_GHOSTS_BOARD_SCREEN,
        MULTIPLAYER_NAMPAC_BOARD_SCREEN,
        OPTION_SCREEN,
        LEADERBOARD_MENU_SCREEN,
    }
    public STATE currentState;

    public GameScreenManager(final InversePacman app) {
        this.app = app;

        initGameScreens();
        setScreen(STATE.MAIN_MENU_SCREEN);
    }

    private void initGameScreens() {
        this.gameScreens = new HashMap<>();
        this.gameScreens.put(STATE.PLAY, new PlayScreen(app));
        this.gameScreens.put(STATE.PAUSE, new PauseScreen(app));
        this.gameScreens.put(STATE.LOBBY_SCREEN, new LobbyScreen(app));
        this.gameScreens.put(STATE.GAME_OVER_SCREEN, new GameOverScreen(app));
        this.gameScreens.put(STATE.MAIN_MENU_SCREEN, new MainMenuScreen(app));
        this.gameScreens.put(STATE.IN_GAME_MENU_SCREEN, new InGameMenuScreen(app));
        this.gameScreens.put(STATE.OPTION_SCREEN, new OptionScreen(app));
        this.gameScreens.put(STATE.LEADERBOARD_MENU_SCREEN, new LeaderboardMenuScreen(app));
        this.gameScreens.put(STATE.SINGLE_PLAYER_GHOSTS_BOARD_SCREEN, new SinglePlayerGhostsBoardScreen(app));
        this.gameScreens.put(STATE.SINGLE_PLAYER_NAMPAC_BOARD_SCREEN, new SinglePlayerNamcapBoardScreen(app));
        this.gameScreens.put(STATE.MULTIPLAYER_GHOSTS_BOARD_SCREEN, new MultiplayerGhostsBoardScreen(app));
        this.gameScreens.put(STATE.MULTIPLAYER_NAMPAC_BOARD_SCREEN, new MultiplayerNamcapBoardScreen(app));
    }

    public void setScreen(STATE nextScreen) {
        app.setScreen(gameScreens.get(nextScreen));
        currentState = nextScreen;
        System.out.println(nextScreen);
    }
    public void dispose() {
        for (AbstractScreen screen : gameScreens.values()) {
            if (screen != null) {
                screen.dispose();
            }
        }
    }
}
