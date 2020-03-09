package com.dinoduel.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dinoduel.game.DinoDuel;


public class MainMenuScreen extends AbstractScreen {
    private Stage stage;
    private int scale;

    public MainMenuScreen(DinoDuel game) {
        super(game);
        /// create stage and set it as input processor
        stage = new Stage(new FillViewport(DinoDuel.V_WIDTH, DinoDuel.V_HEIGHT));
        Gdx.input.setInputProcessor(stage);

    }//end constructor

    @Override
    public void render(float delta) {
        // clear the screen ready for next set of images to be drawn
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell our stage to do actions and draw itself
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }//end render

    @Override
    public void resize(int width, int height) {
        // if (width / stage.getViewport().getScreenWidth() > stage.getViewport().getScreenHeight() / height) {
        scale = width / stage.getViewport().getScreenWidth();
        System.out.println(scale);
        //}
        stage.getViewport().update(width, height, true);
    }//end resize

    @Override
    public void show() {
        //Asset Stuff
        Skin skin = game.manager.assetManager.get("Skin/8BitSkinTest.json");
        game.playingSong = game.manager.assetManager.get("Music/TheBlackFrame.mp3");
        game.playingSong.play();
        //game.getOptions().m
        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        stage.addActor(table);

        //create buttons
        TextButton newGame = new TextButton("Start\n Game ", skin);
        TextButton options = new TextButton("Options", skin);
        TextButton exit = new TextButton("Exit", skin);

        //add buttons to table
        table.add(newGame).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(options).fillX().uniformX();
        table.row();
        table.add(exit).fillX().uniformX();

        // create button listeners
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }//end changed
        });

        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new CharcterSelectMenu(game));
                dispose();
            }//end changed
        });

        options.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new OptionsScreen(game, "MainMenuScreen"));
                dispose();
            }//end changed
        });
    }//end show

    @Override
    public void hide() {
    }//end hide

    @Override
    public void pause() {
    }//end pause

    @Override
    public void resume() {
    }//end resume

    @Override
    public void dispose() {
        stage.dispose();
        super.dispose();
    }//end dispose
}//end class
