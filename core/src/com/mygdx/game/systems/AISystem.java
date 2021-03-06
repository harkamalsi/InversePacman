package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.StateComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.components.VelocityComponent;
import com.mygdx.game.worldbuilder.Node;
import com.mygdx.game.worldbuilder.WorldBuilder;

import java.util.ArrayList;
import java.util.HashMap;


public class AISystem extends IteratingSystem{

    private ComponentMapper<PlayerComponent> playerM;
    private ComponentMapper<StateComponent> stateM;
    private ComponentMapper<TextureComponent> texM;
    private ComponentMapper<VelocityComponent> velM;

    private AiNode currentNode = new AiNode();
    private ArrayList<AiNode> openNodeList = new ArrayList<>();
    private ArrayList<AiNode> closedNodeList = new ArrayList<>();
    private ArrayList<Vector2> shortestPath = new ArrayList<>();
    private Vector2 pacmanPosition;
    private String difficulty = "MEDIUM";

    private int[] nav_x = new int[]{-1, 1, 0, 0};
    private int[] nav_y = new int[]{0, 0, -1, 1};
    private HashMap<String, Integer> difficultyMap = new HashMap<>();
    private boolean changed = false;

    public AISystem(){
        super(Family.all(PlayerComponent.class,VelocityComponent.class,TransformComponent.class,StateComponent.class,TextureComponent.class).get());
        playerM = ComponentMapper.getFor(PlayerComponent.class);
        stateM = ComponentMapper.getFor(StateComponent.class);
        texM = ComponentMapper.getFor(TextureComponent.class);
        velM = ComponentMapper.getFor(VelocityComponent.class);

        difficultyMap.put("ALWAYS_RANDOM", 0);
        difficultyMap.put("SUPEREASY", 30);
        difficultyMap.put("EASY", 50);
        difficultyMap.put("MEDIUM", 100);
        difficultyMap.put("HARD", 450);
        difficultyMap.put("MURDEROUS", 30000);





    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        //This is called for each iteration of the game
        PlayerComponent pc = playerM.get(entity);
        StateComponent sc = stateM.get(entity);
        TextureComponent texc = texM.get(entity);
        VelocityComponent velocityC = velM.get(entity);

        Vector2 ghostPosition = getTiledPosition(pc.getBody().getPosition());
        if(!pc.id.equals("PACMAN") || !ghostPosition.equals(getTiledPosition(WorldBuilder.getPlayerList().get(4).getBody().getPosition()))) {
            behaviourManager(pc);
            makeVisible();
            aStar(ghostPosition);
            finishVisible();
            Vector2 firstPos = shortestPath.get(shortestPath.size() - 1);
            Vector2 nextPos = shortestPath.get(shortestPath.size() - 2);
            Vector2 move = new Vector2(nextPos.x - firstPos.x, nextPos.y - firstPos.y);


        float x = 0f;
        float y = 0f;

        //this is where the calculated direction is given to the player component
            if (move.equals(new Vector2(0, 1))){
                x = 0f;
                y = velocityC.ghostVelocity.y;

                sc.setState(1);
            }

            if (move.equals(new Vector2(0, -1))){
                x = 0f;
                y = -velocityC.ghostVelocity.y;

                sc.setState(2);
            }


            if (move.equals(new Vector2(-1, 0))){
                x = -velocityC.ghostVelocity.x;
                y = 0f;

                sc.setState(3);

                //flips texture
                if (texc.region != null && texc.region.isFlipX()){
                    texc.region.flip(true,false);
                }
            }

            if (move.equals(new Vector2(1, 0))){
                x = velocityC.ghostVelocity.x;
                y = 0f;

                sc.setState(4);
                //flips texture
                if (texc.region != null && !texc.region.isFlipX()){
                    texc.region.flip(true,false);
                }
            }
            pc.body.setLinearVelocity(x*50, pc.body.getLinearVelocity().y);
            pc.body.setLinearVelocity(pc.body.getLinearVelocity().x, y*50);

        }


    }

    private void aStar(Vector2 ghostPosition) {
        closedNodeList.clear();
        openNodeList.clear();
        shortestPath.clear();
        currentNode.setTypeOf("Ghost");
        currentNode.setPosition(ghostPosition);
        currentNode.setaStarDistance(calculateAStarDistance(ghostPosition,currentNode));
        currentNode.setPreviousNode(currentNode);
        openNodeList.add(currentNode);

        if (!currentNode.getTypeOf().equals("PACMAN")) {
            shortestPath.add(new Vector2(0,0));
        }

        while (!currentNode.getTypeOf().equals("PACMAN")) {
            if (openNodeList.size() == 0) {
                shortestPath.add(new Vector2(0,0));
                break;
            }
            currentNode = openNodeList.get(0);

            //Adding and generatin up to four nodes to the "open" list
            for (int i = 0; i < 4; i++) {
                Vector2 adjacentNodePos = new Vector2();
                int new_x = (int) currentNode.getPosition().x + nav_x[i];
                int new_y = (int) currentNode.getPosition().y + nav_y[i];
                adjacentNodePos.set(new_x, new_y);

                //Checks that you dont go into a wall and that the node is not in "closed list"
                if (!WorldBuilder.getNodeCostMatrix().get(new_y).get(new_x).getType().equals("wall")
                        && notInClosedList(adjacentNodePos)) {
                    AiNode adjacentNode = generateNode(adjacentNodePos);

                    //Puts the node in the open list at the apropriate place, lowest A* distance goes first.
                    for (int j=0; j < openNodeList.size(); j++) {
                        if (adjacentNode.getaStarDistance() < openNodeList.get(j).getaStarDistance()) {
                            openNodeList.add(j, adjacentNode);
                            break;
                        }
                        if (openNodeList.size() == j+1) {
                            openNodeList.add(adjacentNode);
                            break;
                        }
                    }
                }
            }
            closedNodeList.add(currentNode);
            openNodeList.remove(currentNode);
        }

        while ( !currentNode.getTypeOf().equals("Ghost")) {
            currentNode = currentNode.getPreviousNode();
            shortestPath.add(currentNode.getPosition());
        }

    }

    //Makes the tile Nampac is standing on visible so that the algorithm can reach him.
    private void makeVisible() {
        Node packNode = WorldBuilder.getNodeCostMatrix().get((int) pacmanPosition.y).get((int) pacmanPosition.x);
        if (packNode.getType().equals("wall")) {
            WorldBuilder.getNodeCostMatrix().get((int) pacmanPosition.y).get((int) pacmanPosition.x).setType("ground");
            this.changed = true;
        }
    }

    //Makes the tile invisible to the algorithm again if the ghosts are not supposed to go there
    private void finishVisible() {
        Node packNode = WorldBuilder.getNodeCostMatrix().get((int) pacmanPosition.y).get((int) pacmanPosition.x);
        if (this.changed) {
            packNode.setType("wall");
            this.changed = false;
        }
    }

    //Decides the behaviour of the current entity
    private void behaviourManager(PlayerComponent pc) {
        if (pc.getRandomPos() != null) {
            this.pacmanPosition = pc.getRandomPos();
            if (Math.abs(pc.getRandomPos().x - getTiledPosition(pc.getBody().getPosition()).x) < 4
                    && (Math.abs(pc.getRandomPos().y -
                    getTiledPosition(pc.getBody().getPosition()).y) < 4)) {
                pc.setRandomPos(null);
            }

        }
        if (pc.getRandomPos() == null) {
            int diffNmber = this.difficultyMap.get(this.difficulty);
            if ((int) (Math.random() * diffNmber) == 0) {
                ArrayList<ArrayList<Node>> nodeMatrix = WorldBuilder.getNodeCostMatrix();
                Vector2 randomPos = new Vector2((int) (Math.random() * (nodeMatrix.get(0).size() -
                        1)), (int) (Math.random() * (nodeMatrix.size() - 1)));
                if (nodeMatrix.get((int) randomPos.y).get((int) randomPos.x).isWalkThrough()) {
                    pc.setRandomPos(randomPos);
                }
            }
            this.pacmanPosition = getTiledPosition(WorldBuilder.getPlayerList().get(4).getBody().getPosition());
        }
    }

    private AiNode generateNode(Vector2 nodePos) {
        AiNode aiNode = new AiNode();
        aiNode.setPosition(nodePos);
        //Gets th position for Nampac and checks it.
        if (nodePos.x == (int) pacmanPosition.x && nodePos.y == (int) pacmanPosition.y) { //og endre her
            aiNode.setTypeOf("PACMAN");
        }
        else {
            aiNode.setTypeOf("Arbitrary");
        }
        aiNode.setPreviousNode(currentNode);
        aiNode.setaStarDistance(calculateAStarDistance(nodePos, aiNode));
        return aiNode;
    }

    private int calculateAStarDistance(Vector2 nodePosition, AiNode aiNode) {
        //Calculates manhattan distance from nodePosition to Nampac.
        Vector2 endPostion = pacmanPosition;
        int h = (int )Math.abs(nodePosition.y - endPostion.y + Math.abs(nodePosition.x-endPostion.x));
        aiNode.setHeuristic(h);
        return h;
    }

    private boolean notInClosedList(Vector2 nodePosition) {
        for (AiNode closedNode : closedNodeList) {
            if (closedNode.getPosition().x == nodePosition.x && closedNode.getPosition().y == nodePosition.y) {
                return false;
            }
        }
        return true;
    }

    private Vector2 getTiledPosition(Vector2 position) {
        int newX = (int) (0.0625 * position.x);
        int newY = (int) (0.0625 * position.y);
        return new Vector2(newX, newY);
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}

class AiNode {

    private Vector2 position = new Vector2(0, 0);
    private AiNode previousNode = null;
    private int heuristic = 0;
    private int distanceFromStart = 0;
    private int aStarDistance = 0;
    private String typeOf = "";


    int getaStarDistance() {
        return aStarDistance;
    }

    AiNode getPreviousNode() {
        return previousNode;
    }

    String getTypeOf() {
        return typeOf;
    }

    Vector2 getPosition() {
        return position;
    }

    void setaStarDistance(int aStarDistance) {
        this.aStarDistance = aStarDistance;
    }

    void setDistanceFromStart(int distanceFromStart) {
        this.distanceFromStart = distanceFromStart;
    }

    void setHeuristic(int heuristic) {
        this.heuristic = heuristic;
    }

    void setPosition(Vector2 position) {
        this.position = position;
    }

    void setPreviousNode(AiNode previousNode) {
        this.previousNode = previousNode;
    }

    void setTypeOf(String typeOf) {
        this.typeOf = typeOf;
    }
}