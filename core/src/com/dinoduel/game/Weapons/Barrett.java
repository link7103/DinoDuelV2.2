package com.dinoduel.game.Weapons;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Screens.PlayScreen;

public class Barrett extends Gun {
    public Barrett(float x, float y, World world, PlayScreen screen) {

        super(x, y, world, screen);
        xSize = 55/2;
        ySize = 15/2;
        //fix sizing
        img = new TextureRegion(getTexture(), 80/2, 155/2, xSize, ySize);


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
        return "B";
    }
}
