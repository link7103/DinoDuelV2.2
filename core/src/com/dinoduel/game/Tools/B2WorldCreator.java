package com.dinoduel.game.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Screens.PlayScreen;
import com.dinoduel.game.Sprites.GunBox.AKGunBox;
import com.dinoduel.game.Sprites.GunBox.GunBox;

import com.dinoduel.game.Sprites.GunBox.PistolGunBox;
import com.dinoduel.game.Sprites.GunBox.ShotgunGunBox;
import com.dinoduel.game.Sprites.GunBox.SniperGunBox;
import com.dinoduel.game.Sprites.InteractiveTileObject;
import com.dinoduel.game.Sprites.Ladder;
import com.dinoduel.game.Sprites.SemiSolid;

public class B2WorldCreator {


    public B2WorldCreator(World world, TiledMap map, PlayScreen screen) {

        BodyDef bDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fDef = new FixtureDef();
        Body body;
        Fixture fixture;

        //the first get(x); x = layer number in tiled counting from bottom up starting at 0
        //Ground layer
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)
        ) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bDef.type = BodyDef.BodyType.StaticBody;
            bDef.position.set((rect.getX() + rect.getWidth() / 2) / DinoDuel.PPM, (rect.getY() + rect.getHeight() / 2) / DinoDuel.PPM);

            body = world.createBody(bDef);

            shape.setAsBox(rect.getWidth() / 2 / DinoDuel.PPM, rect.getHeight() / 2 / DinoDuel.PPM);
            fDef.shape = shape;
            fDef.filter.categoryBits = DinoDuel.CATEGORY_SCENERY;
            fDef.filter.maskBits = DinoDuel.MASK_SCENERY;
            fixture = body.createFixture(fDef);
            fixture.setUserData("ground");

        }

        //Guns 6
        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)
        ) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bDef.type = BodyDef.BodyType.StaticBody;
            bDef.position.set((rect.getX() + rect.getWidth() / 2) / DinoDuel.PPM, (rect.getY() + rect.getHeight() / 2) / DinoDuel.PPM);

            body = world.createBody(bDef);

            shape.setAsBox(rect.getWidth() / 2 / DinoDuel.PPM, rect.getHeight() / 2 / DinoDuel.PPM);
            fDef.shape = shape;
            fDef.filter.categoryBits = DinoDuel.CATEGORY_SCENERY;
            fDef.filter.maskBits = DinoDuel.MASK_SCENERY;
            fixture = body.createFixture(fDef);
            fixture.setUserData("ground");
        }
        //normal GunBox 7
        for (MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)
        ) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            InteractiveTileObject newBox = new GunBox(world, map, rect, screen);
            screen.allBoxes.add(newBox);
        }



        //shotgun blue GunBox 8
        for (MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)
        ) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            InteractiveTileObject newBox = new ShotgunGunBox(world, map, rect, screen);
            screen.allBoxes.add(newBox);
        }


        //AK pink GunBox 10
        for (MapObject object : map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)
        ) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            InteractiveTileObject newBox = new AKGunBox(world, map, rect, screen);
            screen.allBoxes.add(newBox);
        }

        //sniper purple GunBox 11
        for (MapObject object : map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)
        ) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            InteractiveTileObject newBox = new SniperGunBox(world, map, rect, screen);
            screen.allBoxes.add(newBox);
        }
        //pistol red GunBox 12
        for (MapObject object : map.getLayers().get(12).getObjects().getByType(RectangleMapObject.class)
        ) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            InteractiveTileObject newBox = new PistolGunBox(world, map, rect, screen);
            screen.allBoxes.add(newBox);
        }



        //
       //here
        //SemiSolids 15

        for (MapObject object : map.getLayers().get(15).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new SemiSolid(world, map, rect, screen);
        }

        for (MapObject object : map.getLayers().get(14).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            System.out.println(rect.width + " "+ rect.x );
            Ladder newLadder = new Ladder(world, map, rect, screen);
            screen.allLadders.add(newLadder);
        }


    }
}//end class
