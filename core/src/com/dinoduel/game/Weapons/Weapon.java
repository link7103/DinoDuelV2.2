package com.dinoduel.game.Weapons;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.dinoduel.game.Sprites.Dino;
import com.dinoduel.game.Tools.B2WorldCreator;

public interface Weapon  {



    TextureRegion getFrame();
    void useWeapon();
    void update();
    void defineWeapon();
    void setUser(Dino dino);


}
