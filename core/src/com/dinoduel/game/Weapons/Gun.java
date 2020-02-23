package com.dinoduel.game.Weapons;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Screens.PlayScreen;
import com.dinoduel.game.Sprites.Dino;

import java.util.ArrayList;

public abstract class Gun extends Weapon  {


    protected int ammo;
    protected int magCap;
    protected float firerate;
    protected int accuracy;
    public int mag ;
    protected float speed;
    protected int duration;








    public Gun(float x, float y, World world, PlayScreen screen) {
        //adjust how this works - Currently loads one big section, change to loading individuals
        super(x,y,world,screen);
    }










    public abstract String getName();





    public void useWeapon() {
        if (ammo>0) {

            if (mag > 0) {
                //fire
                if (user.isRunningRight()) {
                    if (speed < 0)
                        speed *= -1;

                } else {
                    if (speed > 0)
                        speed *= -1;

                }

                float bulletX;
                if (speed > 0) {
                    bulletX = getX() + getWidth();
                } else {
                    bulletX = getX();
                }
                Bullet fired = new Bullet(speed, duration, damage, bulletX, getY() + getHeight() / 2, user, screen, world, this);
                fired.draw = true;
                screen.allBullets.add(fired);
                //System.out.println("Drew bullet");

                // TODO: 2020-02-11 change to speed variable
                //fired.bBody.setLinearVelocity(20/DinoDuel.PPM, 0);

                mag--;
                ammo--;

            } else {
                //reload
                System.out.println(this.getName() + " needs to be reloaded");
                mag = magCap;
            }
        } else {
            empty = true;
        }
    }


}//end class

