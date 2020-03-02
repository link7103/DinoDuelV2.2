package com.dinoduel.game.Sprites.Weapons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Screens.PlayScreen;

public class Shotgun extends Gun {

    public Shotgun(float x, float y, World world, PlayScreen screen) {
        super(x, y, world, screen);
        xSize = 161;
        ySize = 54;
        heldXOffset = 0.05f;
        heldYOffset = -0.02f;
        ammo = 8;
        magCap = 2;
        mag = magCap;
        firerate = 2;
        speedX = 2;
        accuracy = 9;
        damage = 1;
        duration = .25f;
        bulletHeightOffset = 0.005f ;
        img = new TextureRegion(getTexture(), 240, 0, xSize, ySize);

        defineWeapon();
        setBounds(x, y, xSize / DinoDuel.PPM, ySize / DinoDuel.PPM);
        setRegion(img);
        setPosition(wBody.getPosition().x / DinoDuel.PPM - getWidth() / 2, wBody.getPosition().y / DinoDuel.PPM - getHeight() / 2);


        Array<Texture> frames = new Array<>();
        for (int i = 0; i < 18; i++) {
            frames.add(this.getTexture());
            rotate(5);
        }
        reload = new Animation(0.1f, frames);
        setRotation(0);
    }//end constructor

    @Override
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
                    Bullet fired;

                    fired = new Bullet(speedX, 0, duration, damage, bulletX, getY() + getHeight() / 2, user, screen, world, this, bulletHeightOffset);
                    fired.draw = true;
                    screen.allBullets.add(fired);
                    fired = new Bullet(speedX, 0.2f, duration, damage, bulletX, getY() + getHeight() / 2, user, screen, world, this, bulletHeightOffset);
                    fired.draw = true;
                    screen.allBullets.add(fired);
                    fired = new Bullet(speedX, 0.5f, duration, damage, bulletX, getY() + getHeight() / 2, user, screen, world, this, bulletHeightOffset);
                    fired.draw = true;
                    screen.allBullets.add(fired);
                    fired = new Bullet(speedX, -0.2f, duration, damage, bulletX, getY() + getHeight() / 2, user, screen, world, this, bulletHeightOffset);
                    fired.draw = true;
                    screen.allBullets.add(fired);
                    fired = new Bullet(speedX, -0.5f, duration, damage, bulletX, getY() + getHeight() / 2, user, screen, world, this, bulletHeightOffset);
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

    public String getName() {
        return "Shotgun";
    }//end getName
}//end class

