package com.dinoduel.game.Sprites.GunBox;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Screens.PlayScreen;

public class GreyGunBox extends Sprite {
    private TextureRegion img;
    protected PlayScreen screen;
    GreyGunBox(float x, float y, PlayScreen screen) {
        super(screen.crateAtlas.findRegion("crates"));
        this.screen = screen;
        img = new TextureRegion(getTexture(), 16, 0, 16, 16);
        setBounds(x , y, 16 / DinoDuel.PPM, 16 / DinoDuel.PPM);
        setRegion(img);
        //setPosition(x, y);
        // FIXME: 2/24/2020 I Dont this the setposition works - it is not affected by a heightoffeset variable
        setPosition(x - getWidth() / 2, y -getHeight()/2);
        screen.allGreyGunBoxes.add(this);

    }


    public void destroy() {
        screen.allGreyGunBoxes.remove(this);
    }
}
