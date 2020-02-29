package com.dinoduel.game.Tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class B2AssetManager {
    public final AssetManager assetManager = new AssetManager();
    private final String map1 = "DinoDuel Basic Tilesets/map1.tmx";
    private final String skin = "Skin/8BitSkinTest.json";
    private final String skinAtlas = "Skin/8BitSkinTest.atlas";
    private final String menuMusic = "Music/TheLittleBroth.mp3";

    public B2AssetManager() {
        AssetManager assetManager = new AssetManager();
    }//end constructor

    public void queueMap() {
        assetManager.setLoader(TiledMap.class, new TmxMapLoader());
        assetManager.load(map1, TiledMap.class);
    }//end queueMap

    public void queueSkin() {
        SkinLoader.SkinParameter params = new SkinLoader.SkinParameter(skinAtlas);
        assetManager.load(skin, Skin.class, params);
    }//end queueSkin

    public void queueMusic() {
        assetManager.load(menuMusic, Music.class);
    }//end queueMusic
}//end class
