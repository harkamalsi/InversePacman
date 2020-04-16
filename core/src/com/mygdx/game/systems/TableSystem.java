package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.components.ButtonComponent;
import com.mygdx.game.components.TableComponent;
import com.mygdx.game.components.TransformComponent;

public class TableSystem extends IteratingSystem {
    private SpriteBatch batch;
    private TableComponent cc;
    private ComponentMapper<TableComponent> tableM;
    private ComponentMapper<TransformComponent> tc;


    @SuppressWarnings("unchecked")
    public TableSystem() {
        super(Family.all(TableComponent.class).get());
        tableM = ComponentMapper.getFor(TableComponent.class);
        //tc = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        cc.draw();
        System.out.println(cc.getRow().toString());
        System.out.println(cc.toString());


    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        //ButtonComponent click = cc.get(entity);
         cc = tableM.get(entity);

        cc.addRow("Name: ", "Foker");
        // TransformComponent transform = tc.get(entity);

        /*//update click bounds
        click.bounds.x = transform.position.x;
        click.bounds.y = transform.position.y;
        // not done yet example I found and modified needs testing with the menubuttons
        if(Gdx.input.justTouched()){
            // touching
            Vector3 clickPosition = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            System.out.println(clickPosition+":"+click.bounds.toString());
            if(click.bounds.contains(clickPosition.x, clickPosition.y)){
                //object clicked
                //do your thing
                System.out.println("Touched"+entity.toString());

                entity.flags = 1;

            }
        }*/
    }


}