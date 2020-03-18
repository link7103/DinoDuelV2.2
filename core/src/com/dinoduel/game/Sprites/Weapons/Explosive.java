package com.dinoduel.game.Sprites.Weapons;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.physics.box2d.World;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Screens.PlayScreen;
import com.dinoduel.game.Sprites.Dino;


public abstract class Explosive extends Weapon {

    Circle blastArea;
    float timer;
    boolean priming;
    float startTime;
    Explosive(float x, float y, World world, PlayScreen screen) {
        //adjust how this works - Currently loads one big section, change to loading individuals
        super(x, y, world, screen);
    }//end Constructor

    @Override
    public void useWeapon() {
        if (!priming) {
            priming = true;
            startTime = buildTime;
            System.out.println("primes");
            //blinking animation?
        }
    }

    public void explosiveUpdate() {
        if (priming)  {
            if (timer< buildTime-startTime) {
                this.explode();

            }
        }
    }

    public void explode() {
        //explosion animation
        System.out.println("Explodes");
        blastArea.setPosition(this.getX(), this.getY());
        System.out.println("x: " + blastArea.x + " y: " + blastArea.y + " radius: " + blastArea.radius);
        for (Dino player: screen.allLivingPlayers) {
            System.out.println("x: "+ player.getB2body().getPosition().x + "y: "+ player.getB2body().getPosition().y);
            if (blastArea.overlaps(new Circle(player.getB2body().getPosition(), .10f))) {
                player.explosionDamage(blastArea);
                System.out.println("Damages" + player.getName());
            }
        }
        this.clearUser();
        flag = true;

    }


}
