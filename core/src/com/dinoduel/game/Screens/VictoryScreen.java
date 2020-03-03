package com.dinoduel.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
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

public class VictoryScreen extends AbstractScreen {
    private boolean killScreen = false;
    private Stage stage;
    //Player
    private static DemoDinos player1;
    private static DemoDinos player2;
    private static DemoDinos player3;
    private static DemoDinos player4;
    // private ArrayList<DemoDinos> allPlayers = new ArrayList<>();

    //Player Sprites
    private TextureAtlas dinoAtlas;

    private World world;
    //Arrows
    private Texture arrowUp;
    private Texture arrowDown;
    //Select Values
    private int[] selections;
    private ArrayList<Integer> takenSelections;

    public VictoryScreen(DinoDuel game) {
        super(game);
        /// create stage and set it as input processor
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        //Creates the world
        world = new World(new Vector2(0, -10), true);
        dinoAtlas = new TextureAtlas("Dinos/DinoSprites.txt");
        createPlayers();
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
        //update(deltaTime);
        player1.update(deltaTime);
        player2.update(deltaTime);
        player3.update(deltaTime);
        player4.update(deltaTime);

        game.batch.begin();
        player1.draw(game.batch);
        player2.draw(game.batch);
        player3.draw(game.batch);
        player4.draw(game.batch);
        game.batch.end();
        if (killScreen) {
            dispose();
        }
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
        table.left().top();
        //table.setDebug(true);
        stage.addActor(table);

        Skin skin = game.manager.assetManager.get("Skin/8BitSkinTest.json");

        //create buttons
        TextButton start = new TextButton(" Choose Map ", skin);
        TextButton back = new TextButton(" Back ", skin);

        //add buttons to table
        table.row().pad(10, 10, 0, 0);
        table.add(back).left();
        table.row().expand(100, 50).pad(50);
        table.add(start).top();
        // create button listeners
        back.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
                killScreen = true;
            }//end changed
        });

        start.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new CharcterSelectMenu(game));
                killScreen = true;
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
        this.dispose();
    }//end dispose

    public TextureAtlas getDinoAtlas() {
        return dinoAtlas;
    }//end getDinoAtlas

    public void createPlayers() {
        String p1 = PlayScreen.getDinoData(1);
        String p2 = PlayScreen.getDinoData(2);
        String p3 = "nullSprites";
        String p4 = "nullSprites";
        if (PlayScreen.allPlayers.size() >= 3) {
            p3 = PlayScreen.getDinoData(3);
        }
        if (PlayScreen.allPlayers.size() == 4) {
            p4 = PlayScreen.getDinoData(4);
        }
        player1 = new DemoDinos(this, p1, 128, 600);
        //allPlayers.add(player1);
        player2 = new DemoDinos(this, p2, 256, 600);
        // allPlayers.add(player2);
        player3 = new DemoDinos(this, p3, 384, 600);
        // allPlayers.add(player3);
        player4 = new DemoDinos(this, p4, 512, 600);
        // allPlayers.add(player4);
    }//end createPlayers

}//end class