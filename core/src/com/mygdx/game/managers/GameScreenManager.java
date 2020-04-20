package com.mygdx.game.managers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
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
import com.mygdx.game.screens.menu.SplashScreen;
import com.mygdx.game.screens.play.GameOverScreen;
import com.mygdx.game.screens.play.LobbyScreen;
import com.mygdx.game.screens.play.PauseScreen;
import com.mygdx.game.screens.play.PlayScreen;

import java.util.ArrayList;
import java.util.HashMap;

public class GameScreenManager {

    public final InversePacman app;
    private HashMap<STATE, AbstractScreen> gameScreens;
    private Array<STATE> prevScreens;
    public Engine engine;
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
        SPLASH_SCREEN,
    }
    public STATE currentState;

    public GameScreenManager(final InversePacman app) {
        this.app = app;
        prevScreens = new Array<>();
        this.engine = engine;

        initGameScreens();
        setScreen(STATE.SPLASH_SCREEN);


        //System.out.println(prevScreens);
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
        this.gameScreens.put(STATE.SPLASH_SCREEN, new SplashScreen(app));
    }

    public void setScreen(STATE nextScreen) {
        //System.out.println("prevscreens" + prevScreens);
        this.prevScreens.add(nextScreen);
        app.setScreen(gameScreens.get(nextScreen));
        //System.out.println("prevscreens" + prevScreens);
        currentState = nextScreen;
        System.out.println(nextScreen);
    }

   /* public void pushScreen(STATE pushedScreen) {
        this.prevScreens.add(pushedScreen);
        gameScreens.get(currentState).pause();
        app.setScreen(gameScreens.get(pushedScreen));
        currentState = pushedScreen;
    }


    public void popScreen() {
        STATE prevScreen = getprevScreen();
        gameScreens.get(prevScreen).resume();
        app.setScreen(gameScreens.get(prevScreen));
        currentState = prevScreen;
    }

    private STATE getprevScreen() {
        prevScreens.pop();
        return prevScreens.peek();
    }*/
    public void dispose() {
        for (AbstractScreen screen : gameScreens.values()) {
            if (screen != null) {
                screen.dispose();
            }
        }
    }
}
