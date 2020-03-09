package com.dinoduel.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.dinoduel.game.Screens.LoadingScreen;
import com.dinoduel.game.Tools.B2AssetManager;
import com.dinoduel.game.Tools.AppPreferences;

import java.security.PublicKey;

public class DinoDuel extends Game {
    public static int V_WIDTH;
    public static int V_HEIGHT;
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
    public Music playingSoundEffect;

    @Override
    public void create() {
        batch = new SpriteBatch();
        manager = new B2AssetManager();
        options = new AppPreferences();
        manager.queueSkin();
        manager.queueMusic();
        manager.queueSFX();
        manager.assetManager.finishLoading();
        //setScreen((new PlayScreen(this)));
        V_WIDTH = Gdx.graphics.getWidth();
       V_HEIGHT = Gdx.graphics.getHeight();
        setScreen(new LoadingScreen(this, "MainMenuScreen"));
    }//end create

    @Override
    public void render() {
        super.render();
        playMusic();
        playSoundEffect();
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
            playingSong.setLooping(true);
            if (getPreferences().isMusicEnabled()) {
                playingSong.setVolume(getPreferences().getMusicVolume());
            } else {
                playingSong.setVolume(0);
            }
        }
    }//end playMusic

    public void playSoundEffect() {
        if (playingSoundEffect != null) {
            playingSoundEffect.setLooping(false);
            if (getPreferences().isSoundEffectsEnabled()) {
                playingSoundEffect.setVolume(getPreferences().getSoundVolume());
            } else {
                playingSoundEffect.setVolume(0);
            }
        }
    }//end playSoundEffect
}//end class