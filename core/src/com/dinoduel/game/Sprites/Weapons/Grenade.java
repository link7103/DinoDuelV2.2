package com.dinoduel.game.Sprites.Weapons;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.World;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Screens.PlayScreen;

public class Grenade extends Explosive {

    public Grenade(float x, float y, World world, PlayScreen screen) {
        super(x, y, world, screen);
        xSize = 192;
        ySize = 66;
        heldXOffset = 0.05f;
        heldYOffset = -0.03f;
        radius = 10f/DinoDuel.PPM;
        timer = 5;






        damage = 10;


        img = new TextureRegion(getTexture(), 198, 54, xSize, ySize);

        defineWeapon();

        setBounds(x, y, xSize / DinoDuel.PPM, ySize / DinoDuel.PPM);
        setRegion(img);
        setPosition(wBody.getPosition().x / DinoDuel.PPM - getWidth() / 2, wBody.getPosition().y / DinoDuel.PPM - getHeight() / 2);

    }//end Constructor

}
