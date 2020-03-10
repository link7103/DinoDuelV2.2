package com.dinoduel.game.Sprites.Weapons;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Screens.PlayScreen;

public class Sniper extends Gun {
    public Sniper(float x, float y, World world, PlayScreen screen) {
        super(x, y, world, screen);
        xSize = 216;
        ySize = 72;
        heldXOffset =  0.07f;
        heldYOffset =  0.01f;
        ammo = 5;
        magCap = 1;
        mag = magCap;
        firerate = 2;
        speedX = 4;
        accuracy = 10;
        damage = 10;
        duration = 10;
        bulletHeightOffset = 0.005f;
        img = new TextureRegion(getTexture(), 120, 120, xSize, ySize);

        defineWeapon();

        setBounds(x, y, xSize / DinoDuel.PPM, ySize / DinoDuel.PPM);
        setRegion(img);
        setPosition(wBody.getPosition().x / DinoDuel.PPM - getWidth() / 2, wBody.getPosition().y / DinoDuel.PPM - getHeight() / 2);
        setReload();

    }//end constructor

    public String getName() {
        return "Sniper";
    }//end getName
}//end class
