package com.dinoduel.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.dinoduel.game.Screens.PlayScreen;

public class Ladder extends Sprite {

    protected World world;
    protected TiledMap map;
    //protected TiledMapTile tile;
    public Rectangle bounds;

    public Ladder(World world, TiledMap map, Rectangle bounds, PlayScreen screen) {
        this.world = world;
        this.bounds = bounds;
        this.map = map;
    }
}
