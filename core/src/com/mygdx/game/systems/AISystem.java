package com.mygdx.game.systems;

/*
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;

import com.badlogic.ashley.core.ComponentMapper;

import com.badlogic.ashley.systems.IteratingSystem;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.components.VelocityComponent;
import java.lang.*;

import java.util.ArrayList;
import java.util.Arrays;

public class AISystem extends IteratingSystem {
    private ComponentMapper<TransformComponent> tm = ComponentMapper.getFor(TransformComponent.class);
    private ComponentMapper<VelocityComponent> vm = ComponentMapper.getFor(VelocityComponent.class);

    public AISystem() {
        super(Family.all(TransformComponent.class, VelocityComponent.class).get());
 */
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.components.GhostComponent;
import com.mygdx.game.components.StateComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.components.VelocityComponent;

import java.util.ArrayList;
import java.util.Arrays;

public class AISystem extends IteratingSystem{


    private static final float X_VELOCITY = 5f;
    private static final float Y_VELOCITY = 5f;


    private ComponentMapper<GhostComponent> ghostM;
    private ComponentMapper<VelocityComponent> velocityM;
    private ComponentMapper<TransformComponent> transformM;
    private ComponentMapper<StateComponent> stateM;
    private ComponentMapper<TextureComponent> texM;

    public AISystem(){
        super(Family.all(GhostComponent.class,VelocityComponent.class,TransformComponent.class,StateComponent.class,TextureComponent.class).get());
        ghostM = ComponentMapper.getFor(GhostComponent.class);
        velocityM = ComponentMapper.getFor(VelocityComponent.class);
        transformM = ComponentMapper.getFor(TransformComponent.class);
        stateM = ComponentMapper.getFor(StateComponent.class);
        texM = ComponentMapper.getFor(TextureComponent.class);

        //Gdx.input.setInputProcessor(this);
        GhostComponent gc = ghostM.get(entity);
        VelocityComponent vc = velocityM.get(entity);
        TransformComponent tc = transformM.get(entity);
        StateComponent sc = stateM.get(entity);
        TextureComponent texc = texM.get(entity);

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
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        /*
        VelocityComponent velocity = vm.get(entity);
        TransformComponent pos = tm.get(entity);
         */
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

    public static void main(String[] args) {
        AISystem ai = new AISystem();
        BoardDummy board = new BoardDummy();
        System.out.println(board.getLegalMoves());
        board.printBoard();
        /*
        BoardDummy newBoard = board.getLegalMoves().get(0);
        newBoard.printBoard();
         */
        for(int i = 0; i<18; i++) {
            Integer a = i;
            board = ai.miniMax(board, 5);
            board.printBoard();
            System.out.println(board.currentPlayer().getPlayerRep());
            System.out.println(i);
        }
        /*
        System.out.println("______Moves:________");
        for(BoardDummy moves:board.getLegalMoves()) {
            moves.printBoard();
            System.out.println(moves.utility());
        }
         */
    }

}

class BoardDummy {
    private float predictedUtility;
    private int currentPlayer = 0;
    private DummyPlayer pacMan = new PackMan(new Vector2(3,3));
    private DummyPlayer ghost = new Ghost(new Vector2(3,0));
    private ArrayList<DummyPlayer> players = new ArrayList<DummyPlayer>();
    private ArrayList<ArrayList<String>> board = new ArrayList<ArrayList<String>>();
    private int boardSize = 6;
    private ArrayList<Vector2> directions = new ArrayList(Arrays.asList(
            new Vector2(1, 0),
            new Vector2(0, 1),
            new Vector2(-1, 0),
            new Vector2(0, -1)));

    public BoardDummy() {
        this.players.add(pacMan);
        this.players.add(ghost);
        for(int i = 0; i < this.boardSize; i++) {
            board.add(new ArrayList<String>());
            for(int j = 0; j < this.boardSize; j++) {
                board.get(i).add("X");
            }
        }
        /*
        writePosition(pacMan);
        writePosition(ghost);
         */
    }

    private void movePlayer(DummyPlayer player, Vector2 dir) {
        erasePosition(player);
        Vector2 newPos = new Vector2(player.getPosition().x + dir.x, player.getPosition().y + dir.y);
        player.setPosition(newPos);
        writePosition(player);
    }

    public float getPredictedUtility() {
        return this.predictedUtility;
    }

    public void setPredictedUtility(float utility) {
        this.predictedUtility = utility;
    }

    public void movePac(Vector2 dir) {
        movePlayer(this.pacMan, dir);
    }

    public void erasePosition(DummyPlayer player) {
        board.get((int) player.getPosition().y).set((int) player.getPosition().x, "X");
    }

    public void writePosition(DummyPlayer player) {
        board.get((int) player.getPosition().y).set((int) player.getPosition().x, player.getPlayerRep());
    }

    public void printBoard() {
        System.out.println("______________________________");
        for(ArrayList<String> i:board) {
            for(DummyPlayer player:this.players) {
                this.board.get((int) player.getPosition().y).set((int) player.getPosition().x, player.getPlayerRep());
            }
            System.out.println(i);
        }
        System.out.println("______________________________");
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
        for(DummyPlayer player: this.players) {
            float xPos = player.getPosition().x;
            float yPos = player.getPosition().y;
            if(xPos > this.boardSize-1 || xPos < 0 || yPos > this.boardSize-1 || yPos < 0) {
                return false;
            }
            /*
            for(DummyPlayer compPlayer: this.players) {
                if(!(player == compPlayer)) {//&& (player.getPosition().equals(compPlayer.getPosition()) && !(player.isMaxPlayer() ^ compPlayer.isMaxPlayer()))) {
                    return false;
                }
            }
             */
        }
        return true;
    }

    public int distance(DummyPlayer player1, DummyPlayer player2) {
        int xDist = (int) Math.abs(player1.getPosition().x-player2.getPosition().x);
        int yDist = (int) Math.abs(player1.getPosition().y-player2.getPosition().y);
        return yDist+xDist;
    }

    public boolean isTerminal() {
        for(DummyPlayer player1: this.players) {
            for(DummyPlayer player2: this.players) {
                if(player1.getPosition().equals(player2.getPosition()) && !(player1==player2)) {
                    return true;
                }
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

