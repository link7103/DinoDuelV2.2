package com.dinoduel.game.Weapons;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Screens.PlayScreen;

public class Sniper extends Gun {
    public Sniper(float x, float y, World world, PlayScreen screen) {

        super(x, y, world, screen);
        xSize = 216;
        ySize = 72;
        heldXOffset = (float)0.07;
        heldYOffset = (float)0.01;
        ammo = 5;
        magCap = 1;
        firerate = 1;

        for (int i = 0; i < magCap; i++) {
            mag.add(new Bullet(speed, duration, damage, x, y, null, screen));
        }

        img = new TextureRegion(getTexture(), 120, 120, xSize, ySize);

        defineWeapon();
        fixture.setUserData("gun");
        setBounds(x, y, xSize / DinoDuel.PPM, ySize / DinoDuel.PPM);
        setRegion(img);
        setPosition(wBody.getPosition().x/DinoDuel.PPM-getWidth()/2, wBody.getPosition().y/DinoDuel.PPM-getHeight()/2);
    }//end constructor

    @Override


    public String getName() {
        return "Sniper";
    }//end getName

}//end class
