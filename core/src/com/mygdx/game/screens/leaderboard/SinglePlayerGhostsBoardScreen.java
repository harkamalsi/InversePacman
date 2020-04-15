package com.mygdx.game.screens.leaderboard;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Json;
import com.mygdx.game.InversePacman;

import java.util.ArrayList;

public class SinglePlayerGhostsBoardScreen extends AbstractBoardScreen {

    public SinglePlayerGhostsBoardScreen(final InversePacman app) {
        super(app);

        this.bg = new TextureRegion(new Texture(AbstractBoardScreen.LEADERBOARD_DIRECTORY + "ghosts.png"));
    }

    @Override
    public ArrayList<PlayerScore> retrieveTopPlayerScores() {
        //app.saveManager.loadDataValue("ghosts", ArrayList.class);
        ArrayList<PlayerScore> scores = new ArrayList<>();
        scores.add(new PlayerScore("Player 1", "1:23"));
        scores.add(new PlayerScore("Player 2", "1:34"));
        scores.add(new PlayerScore("Player 3", "1:40"));

        return scores;
    }

}
