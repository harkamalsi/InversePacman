package com.mygdx.game.worldbuilder;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.components.PillComponent;
import com.mygdx.game.components.PlayerComponent;

import java.util.ArrayList;

    public class WorldBuilder {

        private static ArrayList<RectangleMapObject> playersPostionList = new ArrayList<>();
        private static ArrayList<RectangleMapObject> pillsPostionList = new ArrayList<>();
        private static ArrayList<ArrayList<Node>> nodeCostMatrix = new ArrayList<>();
        private static ArrayList<PlayerComponent> playerList = new ArrayList<>();
        private static ArrayList<PillComponent> pillList = new ArrayList<>();


        public static void parseTiledObjectLayer(World world
                ,MapObjects wallObjects
                ,MapLayer backgroundLayer
                ,MapObjects playerObjects
                ,MapObjects pillObjects ) {

            parseWallObjectLayer(wallObjects, world);
            parseMapToNode(backgroundLayer);
            parsePillObjectLayer(pillObjects);
            parsePlayerObjectLayer(playerObjects);

        }

        private static void parseWallObjectLayer(MapObjects collisionObjects, World world){
            for (MapObject object : collisionObjects) {

                Shape shape;
                if (object instanceof PolygonMapObject) {
                    shape = createPolyLine((PolygonMapObject) object);

                }
                else if (object instanceof RectangleMapObject) {
                    RectangleMapObject point =(RectangleMapObject) object;
                    playersPostionList.add(point);
                    continue;

                }
                else {
                    System.out.println(object.getClass());
                    continue;
                }

                Body body;
                BodyDef bdef = new BodyDef();
                bdef.type = BodyDef.BodyType.StaticBody;
                body = world.createBody(bdef);
                body.createFixture(shape, 1.0f);
                shape.dispose();
            }
        }

        private static void parsePlayerObjectLayer(MapObjects playerObjects) {
            for (MapObject object : playerObjects) {
                if (object instanceof RectangleMapObject) {
                    RectangleMapObject point =(RectangleMapObject) object;
                    playersPostionList.add(point);

                }
                else {
                    System.out.println(object.getClass());
                    continue;
                }
            }
        }

        private static void parsePillObjectLayer(MapObjects pillObjects) {
            for (MapObject object : pillObjects) {
                if (object instanceof RectangleMapObject) {
                    RectangleMapObject point =(RectangleMapObject) object;
                    pillsPostionList.add(point);

                }
                else {
                    System.out.println(object.getClass());
                    continue;
                }
            }
        }

        private static void parseMapToNode(MapLayer backgroundLayer) {
            TiledMapTileLayer mapLayer = (TiledMapTileLayer) backgroundLayer;
            int height = mapLayer.getHeight();
            int width = mapLayer.getWidth();
            System.out.println(height);
            System.out.println(width);

            for (int y=0; y < height; y++) {
                nodeCostMatrix.add(y, new ArrayList<Node>());
                System.out.println("\n");
                for (int x = 0; x < width; x++){
                    try {
                        mapLayer.getCell(x,height-y-1).getTile().getId();
                        //System.out.println(mapLayer.getTileHeight());
                        //System.out.println(mapLayer.getTileWidth());
                        System.out.print(" . ");
                        nodeCostMatrix.get(y).add(x, new Node(1, "ground", true));
                    } catch (Exception e) {
                        nodeCostMatrix.get(y).add(x, new Node(-1, "wall", false));
                        System.out.print(" X ");
                    }
                }
            }
        }

        private static ChainShape createPolyLine(PolygonMapObject polyline) {
            float[] vertices = polyline.getPolygon().getTransformedVertices();
            Vector2[] worldVertices = new Vector2[vertices.length/2];


            for(int i=0; i < worldVertices.length; i++) {
                worldVertices[i] = new Vector2(vertices[i*2], vertices[i*2+1]);

            }
            ChainShape cs = new ChainShape();
            cs.createLoop(worldVertices);
            return cs;
        }


        public static void createPlayers(World world) {
           for (int i=0; i < getPlayerPositionList().size(); i++) {
               PlayerComponent player = new PlayerComponent();
               Vector2 vector = new Vector2();
               getPlayerPositionList().get(i).getRectangle().getCenter(vector);


               if (getPlayerPositionList().get(i).getName().equals("Ghost")) {
                   player.createPlayerBody(world, "GHOST_NUMBER_" + i, vector.x, vector.y, "GHOST");
                   playerList.add(player);

               }
               else if (getPlayerPositionList().get(i).getName().equals("Pacman")) {
                   player.createPlayerBody(world, "PACMAN", vector.x, vector.y, "PACMAN");
                   playerList.add(player);
               }

           }

        }

        public static void createPills(World world) {
            for (int i=0; i < getPillsPostionList().size(); i++) {

                PillComponent pill = new PillComponent();
                Vector2 vector = new Vector2();
                getPillsPostionList().get(i).getRectangle().getCenter(vector);


                if (getPillsPostionList().get(i).getName().equals("Pill")) {
                    pill.createPillBody(world, "PILL_NUMBER_" + i, vector.x, vector.y);
                    pillList.add(pill);
                }
                else {
                    continue;
                }

            }

        }

        public static ArrayList<ArrayList<Node>> getNodeCostMatrix() {
            return nodeCostMatrix;
        }

        public static ArrayList<PlayerComponent> getPlayerList() {
            return playerList;
        }

        public static ArrayList<RectangleMapObject> getPlayerPositionList() {
            return playersPostionList;
        }

        public static ArrayList<PillComponent> getPillList() {
            return pillList;
        }

        public static ArrayList<RectangleMapObject> getPillsPostionList() {
            return pillsPostionList;
        }

    }
