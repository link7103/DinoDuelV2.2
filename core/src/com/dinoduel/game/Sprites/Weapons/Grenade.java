package com.dinoduel.game.Sprites.Weapons;

import com.badlogic.gdx.physics.box2d.World;
import com.dinoduel.game.Screens.PlayScreen;



public class Grenade extends Weapon {

    Grenade(float x, float y, World world, PlayScreen screen) {
        //adjust how this works - Currently loads one big section, change to loading individuals
        super(x, y, world, screen);
    }//end Constructor

    @Override
    public void useWeapon() {

    }


}
