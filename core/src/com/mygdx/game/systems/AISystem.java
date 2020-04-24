package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.components.GhostComponent;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.StateComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.components.VelocityComponent;
import com.mygdx.game.worldbuilder.Node;
import com.mygdx.game.worldbuilder.WorldBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AISystem extends IteratingSystem{


    private static final float X_VELOCITY = 5f;
    private static final float Y_VELOCITY = 5f;


    private ComponentMapper<GhostComponent> ghostM;
    private ComponentMapper<PlayerComponent> playerM;
    private ComponentMapper<VelocityComponent> velocityM;
    private ComponentMapper<TransformComponent> transformM;
    private ComponentMapper<StateComponent> stateM;
    private ComponentMapper<TextureComponent> texM;

    private boolean kalt = false;
    private AiNode currentNode = new AiNode();
    private ArrayList<AiNode> openNodeList = new ArrayList<>();
    private ArrayList<AiNode> closedNodeList = new ArrayList<>();
    private ArrayList<Vector2> shortestPath = new ArrayList<>();
    private Vector2 pacmanPosition;
    private String difficulty = "EASY";

    private int[] nav_x = new int[]{-1, 1, 0, 0};
    private int[] nav_y = new int[]{0, 0, -1, 1};
    private HashMap<String, Integer> difficultyMap = new HashMap<>();
    private boolean changed = false;

    public AISystem(){
        super(Family.all(PlayerComponent.class,VelocityComponent.class,TransformComponent.class,StateComponent.class,TextureComponent.class).get());
//        ghostM = ComponentMapper.getFor(GhostComponent.class);
        playerM = ComponentMapper.getFor(PlayerComponent.class);
        velocityM = ComponentMapper.getFor(VelocityComponent.class);
        transformM = ComponentMapper.getFor(TransformComponent.class);
        stateM = ComponentMapper.getFor(StateComponent.class);
        texM = ComponentMapper.getFor(TextureComponent.class);
        difficultyMap.put("ALWAYS_RANDOM", 0);
        difficultyMap.put("DRAGVOLL", 30);
        difficultyMap.put("EASY", 50);
        difficultyMap.put("MEDIUM", 100);
        difficultyMap.put("HARD", 450);
        difficultyMap.put("MURDEROUS", 30000);


        //Gdx.input.setInputProcessor(this);



    }

    public void behaviourManager(PlayerComponent pc) {
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

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PlayerComponent pc = playerM.get(entity);
        StateComponent sc = stateM.get(entity);
        TextureComponent texc = texM.get(entity);
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

            if (move.equals(new Vector2(0, 1))){
                x = 0f;
                y = Y_VELOCITY;

                sc.setState(1);
            }

            if (move.equals(new Vector2(0, -1))){
                x = 0f;
                y = -Y_VELOCITY;

                sc.setState(2);
            }


            if (move.equals(new Vector2(-1, 0))){
                x = -X_VELOCITY;
                y = 0f;

                sc.setState(3);

                //flips texture
                if (texc.region != null && texc.region.isFlipX()){
                    texc.region.flip(true,false);
                }
            }

            if (move.equals(new Vector2(1, 0))){
                x = X_VELOCITY;
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

    public void makeVisible() {
        Node packNode = WorldBuilder.getNodeCostMatrix().get((int) pacmanPosition.y).get((int) pacmanPosition.x);
        if (packNode.getType().equals("wall")) {
            WorldBuilder.getNodeCostMatrix().get((int) pacmanPosition.y).get((int) pacmanPosition.x).setType("ground");
            this.changed = true;
        }
    }

    public void finishVisible() {
        Node packNode = WorldBuilder.getNodeCostMatrix().get((int) pacmanPosition.y).get((int) pacmanPosition.x);
        if (this.changed) {
            packNode.setType("wall");
            this.changed = false;
        }
    }




    public void aStar(Vector2 ghostPosition) {
        long startTime = System.nanoTime();

        int t = 0;
        closedNodeList.clear();
        openNodeList.clear();
        shortestPath.clear();

        currentNode.setTypeOf("Ghost");
        currentNode.setPosition(ghostPosition);
        currentNode.setaStarDistance(calculateAStarDistance(ghostPosition,currentNode));
        currentNode.setPreviousNode(currentNode);
        openNodeList.add(currentNode);

        if (currentNode.getTypeOf() != "PACMAN") {
            shortestPath.add(new Vector2(0,0));
        }

        int b = 0;
        while (currentNode.getTypeOf() != "PACMAN") {
            b++;
            t += 1;
            if (openNodeList.size() == 0) {
                shortestPath.add(new Vector2(0,0));
                break;
            }
            currentNode = openNodeList.get(0);

            //Legger til og generer(maks 4) nye noder til "open" listen
            for (int i = 0; i < 4; i++) {
                Vector2 adjacentNodePos = new Vector2();
                int new_x = (int) currentNode.getPosition().x + nav_x[i];
                int new_y = (int) currentNode.getPosition().y + nav_y[i];
                adjacentNodePos.set(new_x, new_y);

                //Sjekker om man ikke går inni en vegg og samtidig om noden ikke er i closed-list.
                if (WorldBuilder.getNodeCostMatrix().get(new_y).get(new_x).getType() != "wall"
                        && notInClosedList(adjacentNodePos)) {
                    AiNode adjacentNode = generateNode(adjacentNodePos);

                    //Legger her noden inn i open-list men må først sjekke hvor den skal være, ettersom den med
                    //Lavest A* distance skal først.
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

        if(b>3000) {
            System.out.println("b:" + b);
        }

        int c = 0;
        while ( currentNode.getTypeOf() != "Ghost") {
            c++;
            currentNode = currentNode.getPreviousNode();
            shortestPath.add(currentNode.getPosition());
        }
        if(c>20) {
            System.out.println("cv:" + c);
        }
        /*
        for (Vector2 pos : shortestPath) {
            System.out.print("X: "+ pos.x);
            System.out.println("Y: "+ pos.y);
        }
        */

        long endTime = System.nanoTime();

        long duration = (endTime - startTime)/1000000;  //divide by 1000000 to get milliseconds.
        //System.out.println(duration);
        System.out.println(shortestPath.size() + "sp");

    }



    private AiNode generateNode(Vector2 nodePos) {
        AiNode aiNode = new AiNode();
        aiNode.setPosition(nodePos);
        //skal her få posisjonen til pacman og sjekke om det er den
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
        //skal her få posisjonen til pacman og sjekke om det er den
        Vector2 endPostion = pacmanPosition; //endre her
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

    public Vector2 getTiledPosition(Vector2 position) {
        int newX = (int) (0.0625 * position.x);
        int newY = (int) (0.0625 * position.y);
        Vector2 newPosition = new Vector2(newX, newY);
        return newPosition;
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

    int getDistanceFromStart() {
        return distanceFromStart;
    }

    int getHeuristic() {
        return heuristic;
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