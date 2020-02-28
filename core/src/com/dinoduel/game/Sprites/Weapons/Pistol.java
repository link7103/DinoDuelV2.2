package com.dinoduel.game.Sprites.Weapons;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Screens.PlayScreen;

public class Pistol extends Gun {
    public Pistol(float x, float y, World world, PlayScreen screen) {
        super(x, y, world, screen);

        xSize = 120;
        ySize = 66;
        heldXOffset = 0.03f;
        heldYOffset = -0.018f;

        ammo = 12;
        magCap = 4;
        mag = magCap;
        firerate = 1;
        speedX = 2;
        accuracy = 7;
        damage = 3.4;
        duration = .5f;
        bulletHeightOffset = 0.007f;
        img = new TextureRegion(getTexture(), 0, 120, xSize, ySize);

        defineWeapon();


        setBounds(x, y, xSize / DinoDuel.PPM, ySize / DinoDuel.PPM);
        setRegion(img);
        setPosition(wBody.getPosition().x / DinoDuel.PPM - getWidth() / 2, wBody.getPosition().y / DinoDuel.PPM - getHeight() / 2);
    }//end constructor

    public String getName() {
        return "Pistol";
    }//end getName
}//end class
