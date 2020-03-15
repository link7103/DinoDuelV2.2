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

public class Shotgun extends Gun {

    public Shotgun(float x, float y, World world, PlayScreen screen) {
        super(x, y, world, screen);
        xSize = 179;
        ySize = 55;
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
        img = new TextureRegion(getTexture(), 0, 437, xSize, ySize);

        defineWeapon();
        setBounds(x, y, xSize / DinoDuel.PPM, ySize / DinoDuel.PPM);
        setRegion(img);
        setPosition(wBody.getPosition().x / DinoDuel.PPM - getWidth() / 2, wBody.getPosition().y / DinoDuel.PPM - getHeight() / 2);

        Array<TextureRegion> frames = new Array<TextureRegion>();

        for (int i = 1; i < 4; i++) {
            frames.add(new TextureRegion(getTexture(), i * xSize, 437, xSize, ySize));
        }
        weaponEmpty = new Animation(1f, frames);
        frames.clear();



    }//end constructor

    @Override
    public void useWeapon() {
        if (buildTime - lastFireTime > firerate && reloadCount == -1) {
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
                    reloading = true;
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

