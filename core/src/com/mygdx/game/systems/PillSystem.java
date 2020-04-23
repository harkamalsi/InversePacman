package com.mygdx.game.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.components.AnimationComponent;
import com.mygdx.game.components.PillComponent;
import com.mygdx.game.components.StateComponent;
import com.mygdx.game.components.TextureComponent;
import com.mygdx.game.scenes.Hud;
import com.mygdx.game.worldbuilder.WorldBuilder;

public class PillSystem extends IteratingSystem {
    private ComponentMapper<PillComponent> pillM;
    private int pillCount;

    public PillSystem() {
        super(Family.all(TextureComponent.class, PillComponent.class).get());
        pillM = ComponentMapper.getFor(PillComponent.class);
        pillCount = WorldBuilder.getPillList().size();
        System.out.println("Pills remaining: " + pillCount);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PillComponent pillComponent = pillM.get(entity);

        if (pillComponent.isCollected()) {
            Hud.addPointsToScore(500);
            getEngine().removeEntity(entity);
            pillComponent.body.getWorld().destroyBody(pillComponent.body);
            pillCount--;
            System.out.println("Pills remaining: " + pillCount);
        }
    }

    public boolean allPillsCollected() {
        return pillCount == 0;
    }
}
