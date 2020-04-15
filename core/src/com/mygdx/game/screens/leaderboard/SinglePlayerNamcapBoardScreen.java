package com.mygdx.game.screens.leaderboard;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.game.InversePacman;

import java.util.ArrayList;

public class SinglePlayerNamcapBoardScreen extends AbstractBoardScreen {
    public SinglePlayerNamcapBoardScreen(final InversePacman app) {
        super(app);

        this.bg = new TextureRegion(new Texture(AbstractBoardScreen.LEADERBOARD_DIRECTORY + "namcap.png"));
    }

    @Override
    public ArrayList<PlayerScore> retrieveTopPlayerScores() {
        //app.saveManager.loadDataValue("ghosts", ArrayList.class);
        ArrayList<PlayerScore> scores = new ArrayList<>();
        scores.add(new PlayerScore("Player 1", "9000p"));
        scores.add(new PlayerScore("Player 2", "2500p"));
        scores.add(new PlayerScore("Player 3", "500p"));

        return scores;
    }


}
