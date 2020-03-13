package com.dinoduel.game.Sprites.Weapons;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Screens.PlayScreen;

public class AK extends Gun {
    public AK(float x, float y, World world, PlayScreen screen) {
        super(x, y, world, screen);
        xSize = 192;
        ySize = 66;
        heldXOffset = 0.05f;
        heldYOffset = -0.03f;

        ammo = 30;
        magCap = 15;
        mag = magCap;
        firerate = .5f;
        speedX = 3;
        accuracy = 6;
        damage = 3.4;
        duration = .75f;
        bulletHeightOffset = 0.005f;
        img = new TextureRegion(getTexture(), 198, 54, xSize, ySize);

        defineWeapon();

        setBounds(x, y, xSize / DinoDuel.PPM, ySize / DinoDuel.PPM);
        setRegion(img);
        setPosition(wBody.getPosition().x / DinoDuel.PPM - getWidth() / 2, wBody.getPosition().y / DinoDuel.PPM - getHeight() / 2);

    }//end Consrtuctor

    @Override
    protected TextureRegion getFrame(float dt) {
        TextureRegion region = img;
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
            if (reloadCount < 50) {
                rotate(-.5f*neg);

                reloadCount++;
            } else if ( reloadCount < 100) {
                rotate(.5f*neg);
                reloadCount++;
            } else if (reloadCount < 190) {
                translateX(neg*1f/DinoDuel.PPM);
                reloadCount++;
            } else if (reloadCount<280) {
                translateX(-1f*neg/DinoDuel.PPM);
                reloadCount++;
            } else {
                reloadCount = -1;
            }

        }




        return region;
    }//end getFrame

    public String getName() {
        return "AK";
    }//end getName
}//end class

