package com.dinoduel.game.Sprites.Weapons;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.dinoduel.game.Screens.PlayScreen;

public abstract class Gun extends Weapon {
    int ammo;
    int magCap;
    float firerate;
    int accuracy;
    int mag;
    float speedX;
    float duration;
    float bulletHeightOffset;
    Animation<Texture> reload;

    Gun(float x, float y, World world, PlayScreen screen) {
        //adjust how this works - Currently loads one big section, change to loading individuals
        super(x, y, world, screen);
    }//end Constructor

    public abstract String getName();

    public void useWeapon() {
        if (buildTime - lastFireTime > firerate) {
            if (ammo > 0) {
                if (mag > 0) {
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
                    //reload

                    mag = magCap;
                    lastFireTime = buildTime;
                }
            } else {
                screen.game.playingSoundEffect = screen.game.manager.assetManager.get(screen.game.manager.sFX[3]);
                screen.game.playingSoundEffect.play();
                empty = true;
            }
        }
    }//end useWeapon
}//end class

