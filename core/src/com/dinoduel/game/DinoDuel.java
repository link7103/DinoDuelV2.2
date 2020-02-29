package com.dinoduel.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dinoduel.game.Screens.LoadingScreen;
import com.dinoduel.game.Tools.B2AssetManager;
import com.dinoduel.game.Tools.AppPreferences;

import java.security.PublicKey;

public class DinoDuel extends Game {
    public static int V_WIDTH = 320;
    public static int V_HEIGHT = 260;
    public static final float PPM = 100;

    public static final short CATEGORY_WEAPON = 2;
    public static final short CATEGORY_SCENERY = 4;
    public static final short CATEGORY_DINO = 1;
    public static final short CATEGORY_GUNBOX = 8;
    public static final short CATEGORY_BULLET = 16;
    public static final short CATEGORY_SEMISOLID = 32;

    public static final short MASK_WEAPON = CATEGORY_SCENERY | CATEGORY_GUNBOX | CATEGORY_SEMISOLID;
    public static final short MASK_SCENERY = -1;
    public static final short MASK_DINO = CATEGORY_SCENERY | CATEGORY_GUNBOX | CATEGORY_BULLET | CATEGORY_SEMISOLID;
    public static final short MASK_DINOCLIMBING = CATEGORY_SCENERY | CATEGORY_GUNBOX | CATEGORY_BULLET;
    public static final short MASK_GUNBOX = CATEGORY_DINO | CATEGORY_WEAPON | CATEGORY_BULLET;
    public static final short MASK_BULLET = CATEGORY_SCENERY | CATEGORY_DINO | CATEGORY_GUNBOX;
    public static final short MASK_SEMISOLID = CATEGORY_DINO | CATEGORY_BULLET | CATEGORY_WEAPON;

    public SpriteBatch batch;
    public B2AssetManager manager;
    private AppPreferences options;
    public Music playingSong;

    @Override
    public void create() {
        batch = new SpriteBatch();
        manager = new B2AssetManager();
        options = new AppPreferences();
        manager.queueSkin();
        manager.queueMusic();
        manager.assetManager.finishLoading();
        //setScreen((new PlayScreen(this)));
        setScreen(new LoadingScreen(this, "MainMenuScreen"));
    }//end create

    @Override
    public void render() {
        super.render();
        playMusic();
    }//end render

    @Override
    public void dispose() {
        batch.dispose();
        manager.assetManager.dispose();
        this.getScreen().dispose();
    }//end dispose

    public AppPreferences getPreferences() {
        return this.options;
    }//end getOptions

    public void playMusic() {
        if (playingSong != null) {
            if (getPreferences().isMusicEnabled()) {
                playingSong.setVolume(getPreferences().getMusicVolume());
            } else {
                playingSong.setVolume(0);
            }
        }
    }//end playMusic
}//end class