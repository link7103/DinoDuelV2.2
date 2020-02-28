package com.dinoduel.game.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Screens.PlayScreen;

public class SemiSolid extends InteractiveTileObject {

    public PlayScreen screen;

    public SemiSolid(World world, TiledMap map, Rectangle bounds, PlayScreen screen) {
        super(world, map, bounds, screen);
        defineSemiSolid();
        fixture.setUserData(this);
        this.screen = screen;
    }//end Constructor

    @Override
    public int onHeadHit() {
        return -1;
    }//end onHeadHit



    private void defineSemiSolid() {
        BodyDef bDef = new BodyDef();
        FixtureDef fDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bDef.type = BodyDef.BodyType.StaticBody;
        bDef.position.set((bounds.getX() + bounds.getWidth() / 2) / DinoDuel.PPM, (bounds.getY() + bounds.getHeight() / 2) / DinoDuel.PPM);

        body = world.createBody(bDef);

        shape.setAsBox(bounds.getWidth() / 2 / DinoDuel.PPM, bounds.getHeight() / 2 / DinoDuel.PPM);
        fDef.shape = shape;
        fDef.filter.categoryBits = DinoDuel.CATEGORY_SEMISOLID;
        fDef.filter.maskBits = DinoDuel.MASK_SEMISOLID;
        fixture = body.createFixture(fDef);
    }//end defineSemiSolid

    public void update(float dt){}
}//end SemiSolid

