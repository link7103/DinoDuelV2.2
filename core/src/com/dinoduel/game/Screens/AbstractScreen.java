package com.dinoduel.game.Screens;

import com.badlogic.gdx.Screen;
import com.dinoduel.game.DinoDuel;

public abstract class AbstractScreen implements Screen {

    protected DinoDuel game;

    public AbstractScreen(DinoDuel game) {
        this.game = game;
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}
