package com.mygdx.game.utils;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

public class TiledObjectUtil {

    private static ArrayList<ArrayList<Node>> nodeCostMatrix;

    public static void parseTiledObjectLayer(World world, MapObjects objects, MapLayer layer) {
        TiledMapTileLayer mapLayer = (TiledMapTileLayer) layer;
        nodeCostMatrix = new ArrayList<>();

        for (MapObject object : objects) {

            Shape shape;
            if (object instanceof PolygonMapObject) {
                shape = createPolyLine((PolygonMapObject) object);

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
        System.out.println(vertices[7]);


        for(int i=0; i < worldVertices.length; i++) {
            worldVertices[i] = new Vector2(vertices[i*2], vertices[i*2+1]);

        }
        System.out.println(worldVertices[0]);
        ChainShape cs = new ChainShape();
        cs.createLoop(worldVertices);
        return cs;
    }
}
