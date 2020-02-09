package com.dinoduel.game.Weapons;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Screens.PlayScreen;

public class PPK extends Gun {
    public PPK(float x, float y, World world, PlayScreen screen) {
        super(x, y, world, screen);

        xSize = 120;
        ySize = 66;
        heldXOffset = (float)0.03;
        heldYOffset = (float)-0.018;

        img = new TextureRegion(getTexture(), 0, 120, xSize, ySize);

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
        return "PPK";
    }//end getName

}//end class
