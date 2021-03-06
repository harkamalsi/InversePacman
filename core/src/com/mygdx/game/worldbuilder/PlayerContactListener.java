package com.mygdx.game.worldbuilder;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.components.PillComponent;
import com.mygdx.game.components.PlayerComponent;
import com.mygdx.game.screens.play.PlayScreen;
import com.mygdx.game.worldbuilder.WorldBuilder;


public class PlayerContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if(fixtureA == null || fixtureB == null) return;
        if(fixtureA.getUserData() == null || fixtureB.getUserData() == null) return;

        if (isPlayerContact(fixtureA, fixtureB)) {
            PlayerComponent playerA = (PlayerComponent) fixtureA.getUserData();
            PlayerComponent playerB = (PlayerComponent) fixtureB.getUserData();

            //Adding ids and bodies to the playercomponent involved in the collision.
            playerA.playerIdCollidedWith = playerB.id;
            playerA.playerCollidedWith = playerB.body;
            playerB.playerIdCollidedWith = playerA.id;
            playerB.playerCollidedWith = playerA.body;

        }
        else if (isPlayerAndCoinContact(fixtureA, fixtureB)) {
            if ((fixtureB.getUserData() instanceof PillComponent)) {
                PillComponent pillB = (PillComponent) fixtureB.getUserData();
                PlayerComponent playerA = (PlayerComponent) fixtureA.getUserData();
                if(!pillB.isCollected() && playerA.getType().equals("PACMAN")) {
                    if (pillB.isPowerPill()){
                        playerA.powerMode = true;
                        playerA.invincibleTimer += 10;
                    }
                    pillB.PillCollected();
                }
            }
        }

    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if(fixtureA == null || fixtureB == null) return;
        if(fixtureA.getUserData() == null || fixtureB.getUserData() == null);

        if (isPlayerContact(fixtureA, fixtureB)) {
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    private boolean isPlayerContact(Fixture A, Fixture B) {
        if (A.getUserData() instanceof PlayerComponent && B.getUserData() instanceof PlayerComponent) {
            return true;
        }
        else {
            return false;
        }
    }

    private boolean isPlayerAndCoinContact(Fixture A, Fixture B) {
        if (A.getUserData() instanceof PlayerComponent || B.getUserData() instanceof PlayerComponent) {
            return true;
        }
        else {
            return false;
        }
    }
}
