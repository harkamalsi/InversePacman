package com.mygdx.game.systems;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.components.CoinComponent;
import com.mygdx.game.components.PlayerComponent;


public class PlayerContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        if(fixtureA == null || fixtureB == null) return;
        if(fixtureA.getUserData() == null || fixtureB.getUserData() == null) return;

        if (isPlayerContact(fixtureA, fixtureB)) {
            if (fixtureA.getUserData() instanceof PlayerComponent) {
                PlayerComponent playerA = (PlayerComponent) fixtureA.getUserData();
                //playerA.hit();
            }
            else if (fixtureA.getUserData() instanceof CoinComponent) {
                CoinComponent coinA = (CoinComponent) fixtureA.getUserData();
                if(!coinA.isCollected()) {
                    //coinA.CoinCollected();
                }

            }

            if (fixtureB.getUserData() instanceof PlayerComponent) {
                PlayerComponent playerB = (PlayerComponent) fixtureB.getUserData();
                playerB.hit();
            }
            else if (fixtureB.getUserData() instanceof CoinComponent) {
                CoinComponent coinB = (CoinComponent) fixtureB.getUserData();
                if(!coinB.isCollected()) {
                    coinB.CoinCollected();
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
        if (A.getUserData() instanceof PlayerComponent || B.getUserData() instanceof PlayerComponent) {
            return true;
        }
        else {
            return false;
        }
    }
}
