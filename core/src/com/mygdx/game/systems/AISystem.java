package com.mygdx.game.systems;


import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.components.GhostComponent;
import com.mygdx.game.components.PacmanComponent;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.StateComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.components.VelocityComponent;
import com.mygdx.game.worldbuilder.Node;
import com.mygdx.game.worldbuilder.WorldBuilder;

import java.util.ArrayList;
import java.util.Arrays;

public class AISystem extends IteratingSystem{


    private static final float X_VELOCITY = 5f;
    private static final float Y_VELOCITY = 5f;


    private ComponentMapper<GhostComponent> ghostM;
    private ComponentMapper<PacmanComponent> packM;
    private ComponentMapper<VelocityComponent> velocityM;
    private ComponentMapper<TransformComponent> transformM;
    private ComponentMapper<StateComponent> stateM;
    private ComponentMapper<TextureComponent> texM;
    WorldBuilder world = new WorldBuilder(); //must get the actual world builder!!!

    public AISystem(){
        super(Family.all(GhostComponent.class,VelocityComponent.class,TransformComponent.class,StateComponent.class,TextureComponent.class).get());
        ghostM = ComponentMapper.getFor(GhostComponent.class);
        packM = ComponentMapper.getFor(PacmanComponent.class);
        velocityM = ComponentMapper.getFor(VelocityComponent.class);
        transformM = ComponentMapper.getFor(TransformComponent.class);
        stateM = ComponentMapper.getFor(StateComponent.class);
        texM = ComponentMapper.getFor(TextureComponent.class);


        //Gdx.input.setInputProcessor(this);

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        GhostComponent gc = ghostM.get(entity);
        VelocityComponent vc = velocityM.get(entity);
        TransformComponent tc = transformM.get(entity);
        StateComponent sc = stateM.get(entity);
        TextureComponent texc = texM.get(entity);
        BoardDummy board = new BoardDummy();
        board.createFromWorld(this.world);

        

        float x = 0f;
        float y = 0f;

        if (Gdx.input.isKeyPressed(Input.Keys.T)){
            x = 0f;
            y = Y_VELOCITY;

            sc.setState(1);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.G)){
            x = 0f;
            y = -Y_VELOCITY;

            sc.setState(2);
        }


        if (Gdx.input.isKeyPressed(Input.Keys.F)){
            x = -X_VELOCITY;
            y = 0f;

            sc.setState(3);

            //flips texture
            if (texc.region != null && texc.region.isFlipX()){
                texc.region.flip(true,false);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.H)){
            x = X_VELOCITY;
            y = 0f;

            sc.setState(4);
            //flips texture
            if (texc.region != null && !texc.region.isFlipX()){
                texc.region.flip(true,false);
            }
        }

        vc.setVelocity(x,y);
        vc.setAcceleration(x,y);
    }

    public BoardDummy miniMax(BoardDummy board, int depth) {
        if (depth == 0 || board.isTerminal()) {
            board.setPredictedUtility(board.utility());
            return board;
        }
        if(board.currentPlayer().isMaxPlayer()) {
            BoardDummy bestBoard = new BoardDummy();
            bestBoard.setPredictedUtility(-1000000);
            for (BoardDummy nextBoard : board.getLegalMoves()) {
                BoardDummy max = miniMax(nextBoard, depth-1);
                if(max.getPredictedUtility() > bestBoard.getPredictedUtility()) {
                    bestBoard = nextBoard;
                }
            }
            return bestBoard;
        }
        else {
            BoardDummy bestBoard = new BoardDummy();
            bestBoard.setPredictedUtility(1000000);
            for (BoardDummy nextBoard : board.getLegalMoves()) {
                BoardDummy min = miniMax(nextBoard, depth-1);
                if(min.getPredictedUtility() < bestBoard.getPredictedUtility()) {
                    bestBoard = nextBoard;
                }
            }
            return bestBoard;
        }
    }

}

class BoardDummy {
    private float predictedUtility;
    private int currentPlayer = 0;
    private DummyPlayer pacMan = new PackMan(new Vector2(3,3));
    private DummyPlayer ghost = new Ghost(new Vector2(3,0));
    private ArrayList<DummyPlayer> players = new ArrayList<>();
    private ArrayList<ArrayList<Node>> board = new ArrayList<>();
    private int boardSize = 6;
    private double xTileRatio = 0.1;
    private double yTileRatio = 0.1;
    private ArrayList<Vector2> directions = new ArrayList(Arrays.asList(
            new Vector2(1, 0),
            new Vector2(0, 1),
            new Vector2(-1, 0),
            new Vector2(0, -1)));

    public BoardDummy() {
        /*
        this.players.add(pacMan);
        this.players.add(ghost);
         */
    }

    public void createFromWorld(WorldBuilder world) {
        this.board = world.getNodeCostMatrix();
        this.players = playersFromComponent(world.getPlayerList());
    }

    public Vector2 getTiledPosition(Vector2 position) {
        return new Vector2((int)xTileRatio*position.x, (int)yTileRatio*position.y);
    }

    public ArrayList<DummyPlayer> playersFromComponent(ArrayList<PlayerComponent> comPlayers) {
        ArrayList<DummyPlayer> dummyPlayers = new ArrayList<>();
        for (PlayerComponent comPlayer: comPlayers) {
            DummyPlayer dummyPlayer = new DummyPlayer(getTiledPosition(comPlayer.body.getPosition()));
            dummyPlayers.add(dummyPlayer);
        }
        return dummyPlayers;
     }

     /*
     private void movePlayer(DummyPlayer player, Vector2 dir) {
        erasePosition(player);
        Vector2 newPos = new Vector2(player.getPosition().x + dir.x, player.getPosition().y + dir.y);
        player.setPosition(newPos);
        writePosition(player);
    }
      */

    public float getPredictedUtility() {
        return this.predictedUtility;
    }

    public void setPredictedUtility(float utility) {
        this.predictedUtility = utility;
    }

    public void setCurrentPlayer(int player) {
        this.currentPlayer = player;
    }

    public void nextPlayer() {
        this.currentPlayer+=1;
        if(this.currentPlayer == this.players.size()) {
            this.currentPlayer = 0;
        }
    }

    public DummyPlayer getNextPlayer() {
        int nextPlayer = this.currentPlayer +1;
        if(nextPlayer == this.players.size()) {
            nextPlayer = 0;
        }
        return this.players.get(nextPlayer);
    }

    public DummyPlayer currentPlayer() {
        return this.players.get(this.currentPlayer);
    }

    public ArrayList<DummyPlayer> getPlayers() {
        return this.players;
    }

    public boolean legalState() {
            int xPos = (int)this.currentPlayer().getPosition().x;
            int yPos = (int)this.currentPlayer().getPosition().y;
            if(xPos > this.board.get(0).size()-1 || xPos < 0 || yPos > this.board.size()-1 || yPos < 0) {
                return false;
            }
            if(!board.get(yPos).get(xPos).isWalkThrough()) {
                return false;
            }
            for(DummyPlayer player: this.players) {
                if(player.getPosition().equals(this.currentPlayer())) {
                    return false;
            }
        }
        return true;
    }

    public int distance(DummyPlayer player1, DummyPlayer player2) {
        int xDist = (int) Math.abs(player1.getPosition().x-player2.getPosition().x);
        int yDist = (int) Math.abs(player1.getPosition().y-player2.getPosition().y);
        return yDist+xDist;
    }

    public boolean isTerminal() {
        for(DummyPlayer player: this.players) {
            if(currentPlayer().getPosition().equals(player.getPosition()) && (currentPlayer().isMaxPlayer() ^ player.isMaxPlayer())) {
                return true;
            }
        }
        return false;
    }

    public float utility() { //only works for one maxPlayer
        int distance = 100000000;
        for(DummyPlayer player1: this.players) {
            for (DummyPlayer player2: this.players) {
                if (player1.isMaxPlayer() & !player2.isMaxPlayer()) {
                    distance = Math.min(distance, distance(player1, player2));
                }
            }
        }
        if(distance == 0) {
            return -100000000;
        }
        return distance;
    }

    public ArrayList<BoardDummy> getLegalMoves() {
        ArrayList<BoardDummy> legalMoves = new ArrayList<BoardDummy>();
        for(Vector2 direction: this.directions) {
            BoardDummy board = new BoardDummy();
            board.setCurrentPlayer(this.currentPlayer);
            for(int i = 0; i<this.players.size(); i++) {
                board.getPlayers().get(i).setPosition(this.getPlayers().get(i).getPosition());
            }
            board.currentPlayer().getPosition().add(direction);
            if(board.legalState()) {
                legalMoves.add(board);
                board.nextPlayer();
            }
        }
        return legalMoves;
    }


}

class DummyPlayer {
    private boolean maxPlayer;
    private Vector2 position;
    private String playerRep;

    public DummyPlayer(Vector2 position) {
        this.position = position;
    }

    public void setMaxPlayer(boolean maxPlayer) {
        this.maxPlayer = maxPlayer;
    }

    public void setPlayerRep(String playerRep) {
        this.playerRep = playerRep;
    }

    public boolean isMaxPlayer() {
        return maxPlayer;
    }

    public void setPosition(Vector2 position) {
        this.position.x = position.x;
        this.position.y = position.y;
    }

    public Vector2 getPosition() {
        return position;
    }

    public String getPlayerRep() {
        return playerRep;
    }




}

class PackMan extends DummyPlayer {

    public PackMan(Vector2 position) {
        super(position);
        super.setMaxPlayer(true);
        super.setPlayerRep("P");
    }
}

class Ghost extends DummyPlayer {

    public Ghost(Vector2 position) {
        super(position);
        super.setMaxPlayer(false);
        super.setPlayerRep("G");
    }
}

