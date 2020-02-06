package com.dinoduel.game.Weapons;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Screens.PlayScreen;

public class Mossberg extends Gun {

    public Mossberg(float x, float y, World world, PlayScreen screen) {

        super(x, y, world, screen);
        xSize = 31;
        ySize = 11;
        heldXOffset = (float)0.05;
        heldYOffset = (float)-0.02;

        img = new TextureRegion(getTexture(), 122, 127, xSize, ySize);

        defineWeapon();
        fixture.setUserData("gun");
        setBounds(x, y, xSize / DinoDuel.PPM, ySize / DinoDuel.PPM);
        setRegion(img);
        setPosition(wBody.getPosition().x/DinoDuel.PPM-getWidth()/2, wBody.getPosition().y/DinoDuel.PPM-getHeight()/2);
    }//end constructor

    @Override
    public void useWeapon() {

    }//end useWeapon

    public String getName() {
        return "Mossberg";
    }//end getName

}//end class

