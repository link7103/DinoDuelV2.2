package com.dinoduel.game.Sprites.GunBox;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.dinoduel.game.Screens.PlayScreen;

public class ShotgunGunBox extends GunBox {
    //Blue
    public ShotgunGunBox(World world, TiledMap map, Rectangle bounds, PlayScreen screen) {
        super(world, map, bounds, screen);
    }//end Constructor

    public int onHeadHit() {
        if (buildTime - startTime <= 10) {

            return -1;
        } else {
            startTime = buildTime;
            timerBox = new GreyGunBox(body.getPosition().x, body.getPosition().y, screen);
            return 1;

        }
    }//end onHeadHit
}//end class
