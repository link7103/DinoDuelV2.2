package com.dinoduel.game.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Screens.PlayScreen;

public class GunBox extends InteractiveTileObject {

    public PlayScreen screen;

    public GunBox(World world, TiledMap map, Rectangle bounds, PlayScreen screen) {
        super(world, map, bounds, screen);

        defineGunBox();
        fixture.setUserData(this);

        this.screen = screen;


    }

    @Override
    public int onHeadHit() {
        //Gdx.app.log("Gun Box", "Collision");
        //generates random number to be passed in play screen to choose gun
        //needs to be adjusted for more weapons

        if (System.nanoTime()/(float)(Math.pow(10, 9)) - startTime <=15) {
            System.out.println("CT " + System.nanoTime()/(float)(Math.pow(10, 9)) + "ST " + startTime );
            return -1;
        } else  {
            startTime = System.nanoTime()/(float)(Math.pow(10, 9));
            return (int) (Math.random() * 4);

        }


        //create random weapon

    }



    public void defineGunBox() {
        BodyDef bDef = new BodyDef();
        FixtureDef fDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bDef.type = BodyDef.BodyType.StaticBody;
        bDef.position.set((bounds.getX() + bounds.getWidth() / 2) / DinoDuel.PPM, (bounds.getY() + bounds.getHeight() / 2) / DinoDuel.PPM);

        body = world.createBody(bDef);

        shape.setAsBox(bounds.getWidth() / 2 / DinoDuel.PPM, bounds.getHeight() / 2 / DinoDuel.PPM);
        fDef.shape = shape;
        fDef.filter.categoryBits = DinoDuel.CATEGORY_GUNBOX;
        fDef.filter.maskBits = DinoDuel.MASK_GUNBOX;
        fixture = body.createFixture(fDef);

    }

}
