package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.components.ButtonComponent;
import com.mygdx.game.components.TransformComponent;

public class ButtonSystem extends IteratingSystem {
    private ComponentMapper<ButtonComponent> cc;
    private ComponentMapper<TransformComponent> tc;
    private OrthographicCamera camera;


    @SuppressWarnings("unchecked")
    public ButtonSystem(OrthographicCamera cam) {
        super(Family.all(ButtonComponent.class,TransformComponent.class).get());
        cc = ComponentMapper.getFor(ButtonComponent.class);
        tc = ComponentMapper.getFor(TransformComponent.class);
        camera = cam;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ButtonComponent click = cc.get(entity);
        TransformComponent transform = tc.get(entity);

        //update click bounds
        click.bounds.x = transform.position.x;
        click.bounds.y = transform.position.y;

        if(Gdx.input.isButtonPressed(Input.Keys.A)){
            // touching
            Vector3 clickPosition = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            System.out.println(clickPosition+":"+click.bounds.toString());
            if(click.bounds.contains(clickPosition.x, clickPosition.y)){
                //object clicked
                //do your thing
                System.out.println("Touched"+entity.toString());
            }
        }
    }

}
