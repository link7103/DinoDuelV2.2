package com.dinoduel.game.Screens;

import com.badlogic.gdx.Screen;
import com.dinoduel.game.DinoDuel;

public abstract class AbstractScreen implements Screen {

    protected DinoDuel game;

    public AbstractScreen(DinoDuel game) {
        this.game = game;
    }//end constructor

    @Override
    public void pause() {
    }//end pause

    @Override
    public void resume() {
    }//end resume

    @Override
    public void dispose() {
    }//end dispose
}//end class
