package com.dinoduel.game.Tools;

import com.badlogic.gdx.graphics.g3d.particles.values.MeshSpawnShapeValue;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
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
import com.dinoduel.game.Sprites.GunBox.GrenadeGunBox;
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

        //Ground layer 5
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
        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
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

        //Normal GunBox 7
        for (MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            InteractiveTileObject newBox = new GunBox(world, map, rect, screen);
            screen.allBoxes.add(newBox);
        }

        //Shotgun (Blue) GunBox 8
        for (MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            InteractiveTileObject newBox = new ShotgunGunBox(world, map, rect, screen);
            screen.allBoxes.add(newBox);
        }

        //AK (Pink) GunBox 10
        for (MapObject object : map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            InteractiveTileObject newBox = new AKGunBox(world, map, rect, screen);
            screen.allBoxes.add(newBox);
        }

        //Sniper (Purple) GunBox 11
        for (MapObject object : map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            InteractiveTileObject newBox = new SniperGunBox(world, map, rect, screen);
            screen.allBoxes.add(newBox);
        }

        //Pistol (Red) GunBox 12
        for (MapObject object : map.getLayers().get(12).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            InteractiveTileObject newBox = new PistolGunBox(world, map, rect, screen);
            screen.allBoxes.add(newBox);
        }

        //Pistol (Red) GunBox 13
        for (MapObject object : map.getLayers().get(13).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            InteractiveTileObject newBox = new GrenadeGunBox(world, map, rect, screen);
            screen.allBoxes.add(newBox);
        }

        //Ladders 14
        for (MapObject object : map.getLayers().get(14).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            Ladder newLadder = new Ladder(world, map, rect, screen);
            screen.allLadders.add(newLadder);
        }

        //SemiSolids 15
        for (MapObject object : map.getLayers().get(15).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new SemiSolid(world, map, rect, screen);
        }

        //Polyline layer 16
        /*
        for (MapObject object : map.getLayers().get(16).getObjects().getByType(PolygonMapObject.class)
        ) {
            System.out.println("builds polygon");
            Polygon poly = ((PolylineMapObject) object).getPolyline();

            bDef.type = BodyDef.BodyType.StaticBody;
            bDef.position.set((poly.getX()) / DinoDuel.PPM, (poly.getY()) / DinoDuel.PPM);

            body = world.createBody(bDef);
            float[] scaledVertices = new float[poly.getVertices().length];
            for (int i = 0; i < poly.getVertices().length; i++) {
                scaledVertices[i] = poly.getVertices()[i]/DinoDuel.PPM;
            }

            shape.set(scaledVertices);
            fDef.shape = shape;
            fDef.filter.categoryBits = DinoDuel.CATEGORY_SCENERY;
            fDef.filter.maskBits = DinoDuel.MASK_SCENERY;
            fixture = body.createFixture(fDef);
            fixture.setUserData("ground");
        }


         */


    }//end Constructor
}//end class
