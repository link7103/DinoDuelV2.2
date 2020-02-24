package com.dinoduel.game.Sprites.Weapons;


import com.badlogic.gdx.physics.box2d.World;
import com.dinoduel.game.Screens.PlayScreen;

public abstract class Gun extends Weapon {
    protected int ammo;
    protected int magCap;
    protected float firerate;
    protected int accuracy;
    public int mag;
    protected float speedX;
    protected float duration;
    protected float bulletHeightOffset;

    public Gun(float x, float y, World world, PlayScreen screen) {
        //adjust how this works - Currently loads one big section, change to loading individuals
        super(x, y, world, screen);
    }//end Constructor

    public abstract String getName();

    public void useWeapon() {
        if (buildTime - lastFireTime > firerate) {
            if (ammo > 0) {
                if (mag > 0) {
                    //fire
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
                    //reload
                    mag = magCap;
                    lastFireTime = buildTime;
                }
            } else {
                empty = true;
            }
        }
    }//end useWeapon
}//end class

