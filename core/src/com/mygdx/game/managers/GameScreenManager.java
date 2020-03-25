package com.mygdx.game.managers;

import com.mygdx.game.InversePacman;
import com.mygdx.game.screens.AbstractScreen;
import com.mygdx.game.screens.leaderboard.MultiplayerBoardScreen;
import com.mygdx.game.screens.leaderboard.SinglePlayerBoardScreen;
import com.mygdx.game.screens.menu.InGameMenuScreen;
import com.mygdx.game.screens.menu.MainMenuScreen;
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
        SINGLE_PLAYER_BOARD_SCREEN,
        MULTI_PLAYER_BOARD_SCREEN
    }
    public STATE currentState;

    public GameScreenManager(final InversePacman app) {
        this.app = app;

        initGameScreens();
        setScreen(STATE.PLAY);
    }

    private void initGameScreens() {
        this.gameScreens = new HashMap<>();
        this.gameScreens.put(STATE.PLAY, new PlayScreen(app));
        this.gameScreens.put(STATE.PAUSE, new PauseScreen(app));
        this.gameScreens.put(STATE.LOBBY_SCREEN, new LobbyScreen(app));
        this.gameScreens.put(STATE.GAME_OVER_SCREEN, new GameOverScreen(app));
        this.gameScreens.put(STATE.MAIN_MENU_SCREEN, new MainMenuScreen(app));
        this.gameScreens.put(STATE.IN_GAME_MENU_SCREEN, new InGameMenuScreen(app));
        this.gameScreens.put(STATE.SINGLE_PLAYER_BOARD_SCREEN, new SinglePlayerBoardScreen(app));
        this.gameScreens.put(STATE.MULTI_PLAYER_BOARD_SCREEN, new MultiplayerBoardScreen(app));
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
