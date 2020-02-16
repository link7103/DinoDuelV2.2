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
        mag = magCap;
        firerate = 3;
        speed = 2;
        accuracy = 9;


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

