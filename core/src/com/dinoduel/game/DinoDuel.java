package com.dinoduel.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dinoduel.game.Screens.PlayScreen;

public class DinoDuel extends Game {
    public static final int V_WIDTH = 320;
    public static final int V_HEIGHT = 200;
    public static final float PPM = 100;

    public static final short CATEGORY_WEAPON = 2;
    public static final short CATEGORY_SCENERY = 4;
    public static final short CATEGORY_DINO = 1;
    public static final short CATEGORY_GUNBOX = 8;
    public static final short CATEGORY_BULLET = 16;
    public static final short CATEGORY_SEMISOLID = 32;

    public static final short MASK_WEAPON = CATEGORY_SCENERY | CATEGORY_GUNBOX;
    public static final short MASK_SCENERY = -1;
    public static final short MASK_DINO = CATEGORY_SCENERY | CATEGORY_GUNBOX | CATEGORY_BULLET | CATEGORY_SEMISOLID;
    public static final short MASK_DINOCLIMBING = CATEGORY_SCENERY | CATEGORY_GUNBOX | CATEGORY_BULLET;
    public static final short MASK_GUNBOX = CATEGORY_DINO | CATEGORY_WEAPON | CATEGORY_BULLET;
    public static final short MASK_BULLET = CATEGORY_SCENERY | CATEGORY_DINO | CATEGORY_GUNBOX;
    public static final short MASK_SEMISOLID = CATEGORY_DINO | CATEGORY_BULLET;



    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen((new PlayScreen(this)));
    }//end create

    @Override
    public void render() {
        super.render();
    }//end render
}//end class
//testing