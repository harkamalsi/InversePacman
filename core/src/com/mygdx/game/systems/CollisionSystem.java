package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.components.CollisionComponent;
import com.mygdx.game.components.TransformComponent;

public class CollisionSystem extends IteratingSystem {
    private ComponentMapper<CollisionComponent> collisionMapper;
    private ComponentMapper<TransformComponent> transformMapper;

    public CollisionSystem() {
        this(Family.all(CollisionComponent.class, TransformComponent.class).get(), 0);
    }

    public CollisionSystem(int priority) {
        this(Family.all(CollisionComponent.class, TransformComponent.class).get(), priority);
    }

    public CollisionSystem(Family family, int priority) {
        super(family, priority);

        collisionMapper = ComponentMapper.getFor(CollisionComponent.class);
        transformMapper = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent transformComponent = transformMapper.get(entity);
        CollisionComponent collisionComponent = collisionMapper.get(entity);

        collisionComponent.bounds.x = transformComponent.position.x -
                collisionComponent.bounds.getWidth() * .05f;
        collisionComponent.bounds.y = transformComponent.position.y -
                collisionComponent.bounds.getHeight() * .05f;
    }
}
