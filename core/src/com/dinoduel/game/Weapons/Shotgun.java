package com.dinoduel.game.Weapons;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Screens.PlayScreen;

public class Shotgun extends Gun {

    public Shotgun(float x, float y, World world, PlayScreen screen) {

        super(x, y, world, screen);
        xSize = 161;
        ySize = 54;
        heldXOffset = (float)0.05;
        heldYOffset = (float)-0.02;
        ammo = 8;
        magCap = 2;
        firerate = 3;
        for (int i = 0; i < magCap; i++) {
            mag.add(new Bullet(speed, duration, damage, x, y, null, screen, world, this));
            screen.allBullets.add(mag.get(i));
        }

        img = new TextureRegion(getTexture(), 240, 0, xSize, ySize);

        defineWeapon();
        fixture.setUserData("gun");
        setBounds(x, y, xSize / DinoDuel.PPM, ySize / DinoDuel.PPM);
        setRegion(img);
        setPosition(wBody.getPosition().x/DinoDuel.PPM-getWidth()/2, wBody.getPosition().y/DinoDuel.PPM-getHeight()/2);
    }//end constructor

    @Override


    public String getName() {
        return "Shotgun";
    }//end getName

}//end class

