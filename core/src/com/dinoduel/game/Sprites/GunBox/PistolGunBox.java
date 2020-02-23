package com.dinoduel.game.Sprites.GunBox;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.dinoduel.game.Screens.PlayScreen;
import com.dinoduel.game.Sprites.GunBox.GunBox;

public class PistolGunBox extends GunBox {
    //Red
    public PistolGunBox(World world, TiledMap map, Rectangle bounds, PlayScreen screen) {
        super(world, map, bounds, screen);
    }

    public int onHeadHit() {
        //Gdx.app.log("Gun Box", "Collision");
        //generates random number to be passed in play screen to choose gun
        //needs to be adjusted for more weapons

        if (System.nanoTime()/(float)(Math.pow(10, 9)) - startTime <=15) {

            return -1;
        } else  {
            startTime = System.nanoTime()/(float)(Math.pow(10, 9));
            return 0;

        }


        //create random weapon

    }
}
