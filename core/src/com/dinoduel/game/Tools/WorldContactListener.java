package com.dinoduel.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Screens.PlayScreen;
import com.dinoduel.game.Sprites.Dino;
import com.dinoduel.game.Sprites.GunBox;
import com.dinoduel.game.Sprites.InteractiveTileObject;
import com.dinoduel.game.Sprites.SemiSolid;
import com.dinoduel.game.Weapons.Bullet;
import com.dinoduel.game.Weapons.Gun;
import com.dinoduel.game.Weapons.Weapon;

public class WorldContactListener implements ContactListener {
    private boolean canCollide;

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        //Detects hitting a gun box
        if ((fixA.getUserData() == "head" || fixB.getUserData() == "head")) {
            Fixture head = fixA.getUserData() == "head" ? fixA : fixB;
            Fixture object = head == fixA ? fixB : fixA;
            //Gdx.app.log("head", "collision");
            if (object.getUserData() instanceof GunBox) {
                //Gdx.app.log("Gun Box", "Collision");
                PlayScreen.screen.spawnWeapon(((InteractiveTileObject) object.getUserData()));
                ((InteractiveTileObject) object.getUserData()).onHeadHit();
            }
        }

        //Bullet collision detection
        if ((fixA.getUserData() == "side" || fixB.getUserData() == "side")) {
            Fixture side = fixA.getUserData() == "side" ? fixA : fixB;
            Fixture object = side == fixA ? fixB : fixA;

            if (object.getUserData() != null && object.getUserData() instanceof Bullet) {

            }
        }
//Detection for SemiSolids
        if ((fixA.getUserData() instanceof Dino || fixB.getUserData() instanceof Dino)) {
            Fixture body = fixA.getUserData() instanceof Dino ? fixA : fixB;
            Fixture object = body == fixA ? fixB : fixA;
            if (object.getUserData() instanceof SemiSolid) {
                if (((Dino) body.getUserData()).isDucking() || ((Dino) body.getUserData()).getYVel() > 0) {
                    canCollide = false;
                }

            } else {
                canCollide = true;
            }

        }
    }//end begin contact

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if ((fixA.getUserData() instanceof Dino || fixB.getUserData() instanceof Dino)) {
            Fixture body = fixA.getUserData() instanceof Dino ? fixA : fixB;
            Fixture object = body == fixA ? fixB : fixA;
            if (object.getUserData() instanceof SemiSolid) {
                if (((Dino) body.getUserData()).isDucking() || ((Dino) body.getUserData()).getYVel() > 0) {
                    canCollide = true;
                }
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if ((fixA.getUserData() instanceof Dino || fixB.getUserData() instanceof Dino)) {
            Fixture body = fixA.getUserData() instanceof Dino ? fixA : fixB;
            Fixture object = body == fixA ? fixB : fixA;
            if (object.getUserData() instanceof SemiSolid) {
                if (((Dino) body.getUserData()).isDucking() || ((Dino) body.getUserData()).getYVel() > 0) {
                    contact.setEnabled(canCollide);
                }
            }
        }


    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

}//end WorldContactListener
