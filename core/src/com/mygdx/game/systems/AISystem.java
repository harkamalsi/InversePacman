package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.components.GhostComponent;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.components.StateComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.components.TransformComponent;
import com.mygdx.game.components.VelocityComponent;
import com.mygdx.game.worldbuilder.WorldBuilder;

import java.util.ArrayList;

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

    public AISystem(){
        super(Family.all(PlayerComponent.class,VelocityComponent.class,TransformComponent.class,StateComponent.class,TextureComponent.class).get());
//        ghostM = ComponentMapper.getFor(GhostComponent.class);
        playerM = ComponentMapper.getFor(PlayerComponent.class);
        velocityM = ComponentMapper.getFor(VelocityComponent.class);
        transformM = ComponentMapper.getFor(TransformComponent.class);
        stateM = ComponentMapper.getFor(StateComponent.class);
        texM = ComponentMapper.getFor(TextureComponent.class);

        //Gdx.input.setInputProcessor(this);



    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
//        GhostComponent gc = ghostM.get(entity);
        PlayerComponent pc = playerM.get(entity);
        VelocityComponent vc = velocityM.get(entity);
        TransformComponent tc = transformM.get(entity);
        StateComponent sc = stateM.get(entity);
        TextureComponent texc = texM.get(entity);

        if (!kalt) {
            engine();
        }

        float x = 0f;
        float y = 0f;

        if (pc.id != "PACMAN"){
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

//        vc.setVelocity(x,y);
//        vc.setAcceleration(x,y);
            pc.body.setLinearVelocity(x*50, pc.body.getLinearVelocity().y);
            pc.body.setLinearVelocity(pc.body.getLinearVelocity().x, y*50);

        }


    }

    private AiNode currentNode = new AiNode();
    private ArrayList<AiNode> openNodeList = new ArrayList<>();
    private ArrayList<AiNode> closedNodeList = new ArrayList<>();
    private ArrayList<Vector2> shortestPath = new ArrayList<>();

    public void engine() {
        int[] nav_x = new int[]{-1, 1, 0, 0};
        int[] nav_y = new int[]{0, 0, -1, 1};

        int t = 0;

        currentNode.setTypeOf("Ghost");
        //Trenger posisjon til ghost
        currentNode.setPosition(new Vector2(46,2));
        currentNode.setaStarDistance(calculateAStarDistance(new Vector2(46,2),currentNode));
        currentNode.setPreviousNode(currentNode);
        openNodeList.add(currentNode);

        while (currentNode.getTypeOf() != "PACMAN") {
            t += 1;
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
        while ( currentNode.getTypeOf() != "Ghost") {
            currentNode = currentNode.getPreviousNode();
            shortestPath.add(currentNode.getPosition());
        }

        for (Vector2 pos : shortestPath) {
            System.out.print("X: "+ pos.x);
            System.out.println("Y: "+ pos.y);
        }


    }
    private AiNode generateNode(Vector2 nodePos) {
        AiNode aiNode = new AiNode();
        aiNode.setPosition(nodePos);
        //skal her få posisjonen til pacman og sjekke om det er den
        if (nodePos.x == 25 && nodePos.y == 26) {
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
        Vector2 endPostion = new Vector2(25,26);
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