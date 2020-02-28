package com.dinoduel.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Sprites.DemoDinos;

import java.util.ArrayList;

public class CharcterSelectMenu extends AbstractScreen {
    Stage stage;
    //Player
    private DemoDinos player1;
    private DemoDinos player2;
    //Player Sprites
    private TextureAtlas dinoAtlas;

    private World world;

    public CharcterSelectMenu(DinoDuel game) {
        super(game);
        /// create stage and set it as input processor
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        //Creates the world
        world = new World(new Vector2(0, -10), true);
        dinoAtlas = new TextureAtlas("Dinos/DinoSprites.txt");
        player1 = new DemoDinos(world, this, "douxSprites", 0, 100, 0, 400);
        player2 = new DemoDinos(world, this, "tardSprites", 48, 10,0,400);
    }//end constructor

    @Override
    public void render(float deltaTime) {

        // clear the screen ready for next set of images to be drawn
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell our stage to do actions and draw itself
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();


        //seperates update logic from render
       // update(deltaTime);
        player1.update(deltaTime);
        player2.update(deltaTime);

        game.batch.begin();

        player1.draw(game.batch);
        player2.draw(game.batch);
        //System.out.println(player1.b2body.getPosition());
        //System.out.println(Gdx.input.getX() + " " + Gdx.input.getY());
        game.batch.end();
    }//end render

    public void update(float dt) { //Updates the screen every frame
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

    }//end resize

    @Override
    public void show() {
        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        stage.addActor(table);

        // temporary until we have asset manager in
        Skin skin = new Skin(Gdx.files.internal("SkinTest/8BitSkinTest.json"));

        //create buttons
        TextButton start = new TextButton(" Start ", skin);
        TextButton back = new TextButton("Back", skin);

        //add buttons to table
        table.add(start).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);

        table.add(back).fillX().uniformX();

        // create button listeners
        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }//end changed
        });

        start.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new PlayScreen(game));
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
    }//end dispose

    public TextureAtlas getDinoAtlas() {
        return dinoAtlas;
    }//end getDinoAtlas

}//end class