package com.mygdx.game.managers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.InversePacman;
import com.mygdx.game.screens.AbstractScreen;
import com.mygdx.game.screens.leaderboard.MultiplayerGhostsBoardScreen;
import com.mygdx.game.screens.leaderboard.MultiplayerNamcapBoardScreen;
import com.mygdx.game.screens.leaderboard.SinglePlayerGhostsBoardScreen;
import com.mygdx.game.screens.leaderboard.SinglePlayerNamcapBoardScreen;
import com.mygdx.game.screens.menu.InGameMenuScreen;
import com.mygdx.game.screens.menu.LeaderboardMenuScreen;
import com.mygdx.game.screens.menu.MainMenuScreen;
import com.mygdx.game.screens.menu.OptionScreen;
import com.mygdx.game.screens.menu.SelectAIDifficultyScreen;
import com.mygdx.game.screens.menu.SplashScreen;
import com.mygdx.game.screens.play.GameOverScreen;
import com.mygdx.game.screens.play.LobbyScreen;
import com.mygdx.game.screens.play.PauseScreen;
import com.mygdx.game.screens.play.PlayScreen;

import java.util.HashMap;

public class GameScreenManager {

    public final InversePacman app;
    private HashMap<STATE, AbstractScreen> gameScreens;
    private Array<STATE> prevScreens;
    private Engine engine;


    //different screens
    public enum STATE {
        PLAY,
        PAUSE,
        LOBBY_SCREEN,
        GAME_OVER_SCREEN,
        MAIN_MENU_SCREEN,
        IN_GAME_MENU_SCREEN,
        SINGLE_PLAYER_GHOSTS_BOARD_SCREEN,
        SINGLE_PLAYER_NAMCAP_BOARD_SCREEN,
        MULTIPLAYER_GHOSTS_BOARD_SCREEN,
        MULTIPLAYER_NAMCAP_BOARD_SCREEN,
        OPTION_SCREEN,
        LEADERBOARD_MENU_SCREEN,
        SPLASH_SCREEN,
        SELECT_AI_DIFFICULTY,
    }
    public STATE currentState;

    public GameScreenManager(final InversePacman app) {
        this.app = app;
        prevScreens = new Array<>();

        //engine to be passed to all ecs screens
        engine = new Engine();

        initGameScreens();
        setScreen(STATE.SPLASH_SCREEN);

    }

    private void initGameScreens() {
        this.gameScreens = new HashMap<>();
        this.gameScreens.put(STATE.PLAY, new PlayScreen(app, engine));
        this.gameScreens.put(STATE.PAUSE, new PauseScreen(app, engine));
        this.gameScreens.put(STATE.LOBBY_SCREEN, new LobbyScreen(app, engine));
        this.gameScreens.put(STATE.GAME_OVER_SCREEN, new GameOverScreen(app, engine));
        this.gameScreens.put(STATE.MAIN_MENU_SCREEN, new MainMenuScreen(app, engine));
        this.gameScreens.put(STATE.IN_GAME_MENU_SCREEN, new InGameMenuScreen(app, engine));
        this.gameScreens.put(STATE.OPTION_SCREEN, new OptionScreen(app, engine));
        this.gameScreens.put(STATE.LEADERBOARD_MENU_SCREEN, new LeaderboardMenuScreen(app, engine));
        this.gameScreens.put(STATE.SINGLE_PLAYER_GHOSTS_BOARD_SCREEN, new SinglePlayerGhostsBoardScreen(app, engine));
        this.gameScreens.put(STATE.SINGLE_PLAYER_NAMCAP_BOARD_SCREEN, new SinglePlayerNamcapBoardScreen(app, engine));
        this.gameScreens.put(STATE.MULTIPLAYER_GHOSTS_BOARD_SCREEN, new MultiplayerGhostsBoardScreen(app, engine));
        this.gameScreens.put(STATE.MULTIPLAYER_NAMCAP_BOARD_SCREEN, new MultiplayerNamcapBoardScreen(app, engine));
        this.gameScreens.put(STATE.SPLASH_SCREEN, new SplashScreen(app, engine));
        this.gameScreens.put(STATE.SELECT_AI_DIFFICULTY, new SelectAIDifficultyScreen(app, engine));
    }

    public void setScreen(STATE nextScreen) {
        this.prevScreens.add(nextScreen);
        app.setScreen(gameScreens.get(nextScreen));
        currentState = nextScreen;
    }


    public void dispose() {
        for (AbstractScreen screen : gameScreens.values()) {
            if (screen != null) {
                screen.dispose();
            }
        }
    }
}
