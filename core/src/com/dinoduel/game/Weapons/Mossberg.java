package com.dinoduel.game.Weapons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Screens.PlayScreen;

public class Mossberg extends Gun {

    public Mossberg(float x, float y, World world, PlayScreen screen) {

        super(x, y, world, screen);
        xSize = 37;
        ySize = 12;
        heldXOffset = (float)0.05;
        heldYOffset = (float)-0.02;
        img = new TextureRegion(getTexture(), 151, 157, xSize, ySize);


        defineWeapon();
        fixture.setUserData("gun");


        setBounds(x, y, xSize / DinoDuel.PPM, ySize / DinoDuel.PPM);
        setRegion(img);
        setPosition(wBody.getPosition().x/DinoDuel.PPM-getWidth()/2, wBody.getPosition().y/DinoDuel.PPM-getHeight()/2);


    }

    @Override
    public void useWeapon() {

    }

    public String getName() {
        return "Mossberg";
    }
}

