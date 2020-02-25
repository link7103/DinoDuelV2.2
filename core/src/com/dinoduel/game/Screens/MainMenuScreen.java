package com.dinoduel.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.dinoduel.game.DinoDuel;

public class MainMenuScreen extends AbstractScreen {

    public MainMenuScreen(DinoDuel game) {
        super(game);
    }

    @Override
    public void render(float delta) {
        // Draws a red background
        Gdx.gl.glClearColor(0.5f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }
}
