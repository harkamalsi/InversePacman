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
import java.util.HashMap;

//må gjøres:
//få riktig tile-koordinater på player/legge inn tile ratio
// gjenkjenne om en playercomponent er max eller min (packman eller ghost)
//Må vite hvem som er currentPlayer

public class AISystem extends IteratingSystem {


    private static final float X_VELOCITY = 5f;
    private static final float Y_VELOCITY = 5f;


    private ComponentMapper<GhostComponent> ghostM;
    private ComponentMapper<PacmanComponent> packM;
    private ComponentMapper<PlayerComponent> playerM;
    private ComponentMapper<VelocityComponent> velocityM;
    private ComponentMapper<TransformComponent> transformM;
    private ComponentMapper<StateComponent> stateM;
    private ComponentMapper<TextureComponent> texM;
    WorldBuilder worldBuilderObject = new WorldBuilder(); //must get the actual worldBuilderObject builder!!!

    public AISystem() {
        super(Family.all(PlayerComponent.class, VelocityComponent.class, TransformComponent.class, StateComponent.class, TextureComponent.class).get());
//      ghostM = ComponentMapper.getFor(GhostComponent.class);
        playerM = ComponentMapper.getFor(PlayerComponent.class);
        velocityM = ComponentMapper.getFor(VelocityComponent.class);
        transformM = ComponentMapper.getFor(TransformComponent.class);
        stateM = ComponentMapper.getFor(StateComponent.class);
        texM = ComponentMapper.getFor(TextureComponent.class);


        //Gdx.input.setInputProcessor(this);

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PlayerComponent pc = playerM.get(entity);
        VelocityComponent vc = velocityM.get(entity);
        TransformComponent tc = transformM.get(entity);
        StateComponent sc = stateM.get(entity);
        TextureComponent texc = texM.get(entity);
        BoardDummy board = new BoardDummy(pc);
        board.createFromWorld(this.worldBuilderObject, pc);
        DummyPlayer player = board.currentPlayer();
        if(player.getPosition().equals(new Vector2(49, 19))) {
            System.out.println(("heare"));
        }
        Vector2 bestMove = miniMax(board, 2).getDirection();

//Gdx.input.isKeyPressed(Input.Keys.T)) {

        float x = 0f;
        float y = 0f;
        if (bestMove != new Vector2(0, 1)) {
            int a = 0;
        }
        for (Integer i = 0; i < 4; i++) {
            if (pc.id.equals("GHOST_NUMBER_" + i.toString())) {
                if (bestMove.equals(new Vector2(0, 1))) {
                    x = 0f;
                    y = Y_VELOCITY;

                    sc.setState(1);
                }

                if (bestMove.equals(new Vector2(0, -1))) {
                    x = 0f;
                    y = -Y_VELOCITY;

                    sc.setState(2);
                }


                if (bestMove.equals(new Vector2(-1, 0))) {
                    x = -X_VELOCITY;
                    y = 0f;

                    sc.setState(3);

                    //flips texture
                    if (texc.region != null && texc.region.isFlipX()) {
                        texc.region.flip(true, false);
                    }
                }

                if (bestMove.equals(new Vector2(1, 0))) {
                    x = X_VELOCITY;
                    y = 0f;

                    sc.setState(4);
                    //flips texture
                    if (texc.region != null && !texc.region.isFlipX()) {
                        texc.region.flip(true, false);
                    }
                }
            }
        }
        pc.body.setLinearVelocity(x * 50, pc.body.getLinearVelocity().y);
        pc.body.setLinearVelocity(pc.body.getLinearVelocity().x, y * 50);
    }

    private Move miniMax(BoardDummy board, int depth) {
        Move utilMove = new Move(0, new Vector2(0, 0));
        if (depth == 0 || board.isTerminal()) {
            utilMove.setUtil(board.utility()+depth);
            utilMove.setDirection(board.movedDirection);
            return utilMove;
        }
        ArrayList<BoardDummy> legalBoardMoves = board.getLegalMoves();
        if (board.currentPlayer().isMaxPlayer()) {
            utilMove.setUtil(-1000);
            for (BoardDummy boardMove : legalBoardMoves) {
                Move minmax = miniMax(boardMove, depth - 1);
                if (utilMove.getUtil() < minmax.getUtil()) {
                    utilMove = minmax;
                    utilMove.setDirection(boardMove.movedDirection);
                }
            }
            return utilMove;
        } else {
            utilMove.setUtil(1000);
            for (BoardDummy boardMove : legalBoardMoves) {

                Move minmax = miniMax(boardMove, depth - 1);
                if (utilMove.getUtil() > minmax.getUtil()) {
                    utilMove = minmax;
                    utilMove.setDirection(boardMove.movedDirection);
                }
            }
            return utilMove;
        }

    }



}



class BoardDummy {
    public static ArrayList<ArrayList<ArrayList<Boolean>>> previousPlayerPositions = new ArrayList<>();
    private int predictedUtility = -1000000;
    public Vector2 movedDirection;
    private Vector2 bestMove = new Vector2(0, 0);
    private int currentPlayer = 0;
    private ArrayList<DummyPlayer> players = new ArrayList<>();
    //private static ArrayList<ArrayList<Node>> board = WorldBuilder.getNodeCostMatrix();
    private final float xTileRatio = 0.0625f;
    private final float yTileRatio = 0.0625f;
    PlayerComponent pc;
    private ArrayList<Vector2> directions = new ArrayList(Arrays.asList(
            new Vector2(1, 0),
            new Vector2(0, 1),
            new Vector2(-1, 0),
            new Vector2(0, -1)));

    public BoardDummy(PlayerComponent pc) {
        this.pc = pc;
    }

    static {
        for (ArrayList<Node> list : WorldBuilder.getNodeCostMatrix()) {
            ArrayList<ArrayList<Boolean>> bolMap = new ArrayList<>();
            for (Node node : list) {
                ArrayList<Boolean> bolList = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    bolList.add(false);
                }
                bolMap.add(bolList);
            }
            previousPlayerPositions.add(bolMap);
        }
    }

    public void createFromWorld(WorldBuilder worldBuilderObject, PlayerComponent pc) {
        this.players = playersFromComponent(worldBuilderObject.getPlayerList());
        for (int i = 0; i < this.players.size(); i++) {
            if (worldBuilderObject.getPlayerList().get(i).id.equals(pc.id)) {
                this.currentPlayer = i;
            }
        }
        for(ArrayList<ArrayList<Boolean>> list1: previousPlayerPositions) {
            for(ArrayList<Boolean> list2: list1) {
                for (int i = 0; i < list2.size(); i++) {
                    list2.set(i, false);
                }
            }
        }
    }

    public void setBestMove(Vector2 bestMove) {
        if (!(this.bestMove == null)) {
            this.bestMove.x = bestMove.x;
            this.bestMove.y = bestMove.y;
        }
    }

    public Vector2 getBestMove() {
        return this.bestMove;
    }

    public Vector2 getTiledPosition(Vector2 position) {
        int newX = (int) (xTileRatio * position.x);
        int newY = (int) (yTileRatio * position.y);
        Vector2 newPosition = new Vector2(newX, newY);
        return newPosition;
    }

    public ArrayList<DummyPlayer> playersFromComponent(ArrayList<PlayerComponent> comPlayers) {
        ArrayList<DummyPlayer> dummyPlayers = new ArrayList<>();
        for (PlayerComponent comPlayer : comPlayers) {
            DummyPlayer dummyPlayer = new DummyPlayer(getTiledPosition(comPlayer.body.getPosition()));
            dummyPlayer.setId(comPlayer.getId());
            if (comPlayer.getType().equals("GHOST")) {
                dummyPlayer.setMaxPlayer(false);
            } else if (comPlayer.getType().equals("PACMAN")) {
                dummyPlayer.setMaxPlayer(true);
            } else {
                throw new IllegalStateException("Neither pacman nor ghost");
            }
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

   public int getPredictedUtility() {
       return this.predictedUtility;
   }

   public void setPredictedUtility(int utility) {
       this.predictedUtility = utility;
   }
   */
    public void setCurrentPlayer(int player) {
        this.currentPlayer = player;
    }

    public void nextPlayer() {
        this.currentPlayer+=1;
        if(this.currentPlayer == this.players.size()) {
            this.currentPlayer = 0;
        }
    }
    /*
    public DummyPlayer getNextPlayer() {
        int nextPlayer = this.currentPlayer +1;
        if(nextPlayer == this.players.size()) {
            nextPlayer = 0;
        }
        return this.players.get(nextPlayer);
    }*/

    protected DummyPlayer currentPlayer() {
        return this.players.get(this.currentPlayer);
    }

    public ArrayList<DummyPlayer> getPlayers() {
        return this.players;
    }

    public boolean legalState() {
        int xPos = (int) this.currentPlayer().getPosition().x;
        int yPos = (int) this.currentPlayer().getPosition().y;
        if (xPos > WorldBuilder.getNodeCostMatrix().get(0).size() - 1 || xPos < 0 || yPos > WorldBuilder.getNodeCostMatrix().size() - 1 || yPos < 0) {
            return false;
        }
        if (!WorldBuilder.getNodeCostMatrix().get(yPos).get(xPos).isWalkThrough()) {
            return false;
        }
/*
        for (DummyPlayer player : this.players) {
            if (player.getPosition().equals(this.currentPlayer().getPosition())) {
                //return false;
            }
        }

 */
        Boolean bol = !previousPlayerPositions.get((yPos)).get(xPos).get(currentPlayer);
        return bol;
    }

    public int distance(DummyPlayer player1, DummyPlayer player2) {
        int xDist = (int) (Math.abs(player1.getPosition().x - player2.getPosition().x));
        int yDist = (int) (Math.abs(player1.getPosition().y - player2.getPosition().y));
        return yDist + xDist;
    }

    public boolean isTerminal() {
        for (DummyPlayer player : this.players) {
            Vector2 currenPosition = currentPlayer().getPosition();
            Vector2 playerPosition = player.getPosition();
            if (currenPosition.equals(playerPosition) && (currentPlayer().isMaxPlayer() ^ player.isMaxPlayer())) {
                return true;
            }
        }
        return false;
    }

    public int utility() {
        int distance = 100000000;
    /*
        for(DummyPlayer player1: this.players) {
            for (DummyPlayer player2: this.players) {
                if (player1.isMaxPlayer() ^ player2.isMaxPlayer()) {
                    distance = Math.min(distance, distance(player1, player2));
                }
            }
        }
        if(distance == 0) {
            return -100000000;
        }

*/
        return distance(currentPlayer(), this.players.get(4));
    }

    public void addPlayer(DummyPlayer player) {
        this.players.add(player);
    }

    public ArrayList<BoardDummy> getLegalMoves() {
        ArrayList<BoardDummy> legalMoves = new ArrayList<BoardDummy>();
        for (Vector2 direction : this.directions) {
            BoardDummy board = new BoardDummy(this.pc);
            board.movedDirection = direction;
            board.setCurrentPlayer(this.currentPlayer);
            for (int i = 0; i < this.players.size(); i++) {
                DummyPlayer newPlayer = this.players.get(i).makeCopy();
                board.addPlayer(newPlayer);
                //board.getPlayers().get(i).setPosition(this.getPlayers().get(i).getPosition());
            }
            Vector2 position = board.currentPlayer().getPosition();
            board.currentPlayer().getPosition().add(direction);
            if (board.legalState()) {
                previousPlayerPositions.get((int) (position.y)).get((int) position.x).set(currentPlayer, true);
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
    private String id;

    public DummyPlayer(Vector2 position) {
        this.position = position;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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

    public DummyPlayer makeCopy() {
        DummyPlayer player = new DummyPlayer(new Vector2(this.position.x, this.position.y));
        player.setMaxPlayer(this.isMaxPlayer());
        player.setId(this.getId());
        return player;
    }

    public Vector2 getPosition() {
        return position;
    }

    public String getPlayerRep() {
        return playerRep;
    }
}

class Move {
    private int util;
    private Vector2 direction;

    public Move(int util, Vector2 direction) {
        this.direction = direction;
        this.util = util;
    }

    public int getUtil() {
        return util;
    }

    public void setUtil(int util) {
        this.util = util;
    }

    public Vector2 getDirection() {
        return direction;
    }

    public void setDirection(Vector2 direction) {
        this.direction = direction;
    }
}



