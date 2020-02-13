package com.dinoduel.game.Weapons;

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
        firerate = 10;

        for (int i = 0; i < magCap; i++) {
            mag.add(new Bullet(speed, duration, damage, x, y, null, screen, world, this));
            screen.allBullets.add(mag.get(i));
        }

        img = new TextureRegion(getTexture(), 198, 54, xSize, ySize);

        defineWeapon();
        fixture.setUserData("gun");
        setBounds(x, y, xSize / DinoDuel.PPM, ySize / DinoDuel.PPM);
        setRegion(img);
        setPosition(wBody.getPosition().x/DinoDuel.PPM-getWidth()/2, wBody.getPosition().y/DinoDuel.PPM-getHeight()/2);
    }//end class

    @Override


    public String getName() {
        return "AK";
    }//end getName

}//end class

