package com.dinoduel.game.Sprites.GunBox;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Screens.PlayScreen;
import com.dinoduel.game.Sprites.InteractiveTileObject;

public class GunBox extends InteractiveTileObject {
    public PlayScreen screen;
    GreyGunBox timerBox = null;
    float buildTime = 0;

    public GunBox(World world, TiledMap map, Rectangle bounds, PlayScreen screen) {
        super(world, map, bounds, screen);
        defineGunBox();
        fixture.setUserData(this);
        this.screen = screen;
    }//end Constructor

    @Override
    public int onHeadHit() {
        //Gdx.app.log("Gun Box", "Collision");
        //generates random number to be passed in play screen to choose gun
        //needs to be adjusted for more weapons
        if (buildTime - startTime <= 10) {

            return -1;
        } else {

            startTime = buildTime;
            timerBox = new GreyGunBox(body.getPosition().x, body.getPosition().y, screen);

            return (int) (Math.random() * 4);

        }
        //create random weapon
    }//end onHeadHit

    public void update(float dt) {
        buildTime += dt;
        if (buildTime - startTime >= 10) {
            if (timerBox!= null) {
                timerBox.destroy();
                timerBox = null;
            }
        }
    }


    private void defineGunBox() {
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
    }//end defineGunBox
}//end class
