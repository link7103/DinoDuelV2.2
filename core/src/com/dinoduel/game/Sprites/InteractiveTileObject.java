package com.dinoduel.game.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.dinoduel.game.Screens.PlayScreen;

public abstract class InteractiveTileObject {
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    public Body body;
    protected Fixture fixture;
    public boolean spawnGun = false;
    protected float startTime = -16;

    public InteractiveTileObject(World world, TiledMap map, Rectangle bounds, PlayScreen screen) {
        this.world = world;
        this.map = map;
        this.bounds = bounds;
    }//end Constructor

    public abstract int onHeadHit();
    public float getSpawnX() {
        return bounds.getX() + bounds.getWidth()/2;
    }
    public float getSpawnY() {
        return bounds.getY() + bounds.getHeight();
    }
    public abstract void update(float dt);
    /*public boolean canSpawn(float dt){

        dt *= 1000;

        //System.out.println("start time" + startTime + " dt " + dt);
        if (spawned) {
            spawned = false;
            startTime = dt;
            return false;
        } else if (dt-startTime <=15) {
            return false;
        } else {
            return true;
        }
    }*/
}
