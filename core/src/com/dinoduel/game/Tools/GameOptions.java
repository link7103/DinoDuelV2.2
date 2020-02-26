package com.dinoduel.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GameOptions {
    private static final String PREF_MUSIC_VOLUME = "volume";
    private static final String PREF_MUSIC_ENABLED = "music.enabled";
    private static final String PREF_SOUND_ENABLED = "sound.enabled";
    private static final String PREF_SOUND_VOL = "sound";
    private static final String PREFS_NAME = "DinoD";

    protected Preferences getOps() {
        return Gdx.app.getPreferences(PREFS_NAME);
    }//end getOps;

    public boolean isSoundEffectsEnabled() {
        return getOps().getBoolean(PREF_SOUND_ENABLED, true);
    }//end isSoundEffectsEnabled

    public void setSoundEffectsEnabled(boolean soundEffectsEnabled) {
        getOps().putBoolean(PREF_SOUND_ENABLED, soundEffectsEnabled);
        getOps().flush();
    }//end setSoundEffectsEnabled

    public boolean isMusicEnabled() {
        return getOps().getBoolean(PREF_MUSIC_ENABLED, true);
    }//end isMusicEnabled

    public void setMusicEnabled(boolean musicEnabled) {
        getOps().putBoolean(PREF_MUSIC_ENABLED, musicEnabled);
        getOps().flush();
    }//end setMusicEnabled

    public float getMusicVolume() {
        return getOps().getFloat(PREF_MUSIC_VOLUME, 0.5f);
    }//end getMusicVolume

    public void setMusicVolume(float volume) {
        getOps().putFloat(PREF_MUSIC_VOLUME, volume);
        getOps().flush();
    }//end setMusicVolume

    public float getSoundVolume() {
        return getOps().getFloat(PREF_SOUND_VOL, 0.5f);
    }//end getSoundVolume

    public void setSoundVolume(float volume) {
        getOps().putFloat(PREF_SOUND_VOL, volume);
        getOps().flush();
    }//end setSoundVolume
}//end class

