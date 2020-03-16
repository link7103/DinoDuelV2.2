package com.dinoduel.game.Sprites.Weapons;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Screens.PlayScreen;

import java.util.ArrayList;

public abstract class Gun extends Weapon {
    int ammo;
    int magCap;
    float firerate;
    int accuracy;
    int mag;
    float speedX;
    float duration;
    float bulletHeightOffset;


    Gun(float x, float y, World world, PlayScreen screen) {
        //adjust how this works - Currently loads one big section, change to loading individuals
        super(x, y, world, screen);




    }//end Constructor

    public abstract String getName();

    public void useWeapon() {
        if (mag ==0 && reloadCount ==-1 || empty) {
            //reload
            reloading = true;
            mag = magCap;
            lastFireTime = buildTime;
        } else if (buildTime - lastFireTime > firerate && reloadCount == -1) {
            if (ammo > 0) {

                    //fire
                    screen.game.playingSoundEffect = screen.game.manager.assetManager.get(screen.game.manager.sFX[4]);
                    screen.game.playingSoundEffect.play();
                    if (user.isRunningRight()) {
                        if (speedX < 0)
                            speedX *= -1;
                    } else {
                        if (speedX > 0)
                            speedX *= -1;
                    }

                    float bulletX;
                    if (speedX > 0) {
                        bulletX = getX() + getWidth();
                    } else {
                        bulletX = getX();
                    }
                    Bullet fired = new Bullet(speedX, 0, duration, damage, bulletX, getY() + getHeight() / 2, user, screen, world, this, bulletHeightOffset);
                    fired.draw = true;
                    screen.allBullets.add(fired);
                    mag--;
                    ammo--;
                    lastFireTime = buildTime;

            } else {
                screen.game.playingSoundEffect = screen.game.manager.assetManager.get(screen.game.manager.sFX[3]);
                screen.game.playingSoundEffect.play();
                empty = true;
            }
        }
    }//end useWeapon

    @Override
    protected TextureRegion getFrame(float dt) {
        TextureRegion region;
        if (empty  || mag == 0)
            region = weaponEmpty.getKeyFrame(stateTimer, true);
        else
            region = img;

        float neg;
        if (user.isRunningRight()) {
            neg = 1;
        } else {
            neg = -1;
        }
        if (!user.isRunningRight() && !region.isFlipX()) {
            region.flip(true, false);
        } else if (user.isRunningRight() && region.isFlipX()) {
            region.flip(true, false);
        }

        if(reloading) {
            System.out.println("Should be changing region");
            reloading = false;
            reloadCount = 0;
        } else if (reloadCount >=0) {
            System.out.println(dt);
            if (reloadCount < 180) {
                rotate(neg * .5f);
                reloadCount++;
            } else if ( reloadCount < 280) {
                translateY(-1f*neg/ DinoDuel.PPM);
                reloadCount++;
            } else if (reloadCount < 380) {
                translateY(neg*1f/DinoDuel.PPM);
                reloadCount++;
            } else if (reloadCount<560) {
                rotate(-.5f*neg);
                reloadCount++;
            } else {
                reloadCount = -1;
            }

        }



        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }//end getFrame


}//end class

