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

public class CharcterSelectMenu extends AbstractScreen {
    Stage stage;
    //Player
    private static DemoDinos player1;
    private static DemoDinos player2;
    private static DemoDinos player3;
    private static DemoDinos player4;
    //Player Sprites
    private TextureAtlas dinoAtlas;

    private World world;
    //Arrows
    private Texture arrowUp;
    private Texture arrowDown;
    //Select Values
    private int[] selections;
    private ArrayList<Integer> takenSelections;

    public CharcterSelectMenu(DinoDuel game) {
        super(game);
        /// create stage and set it as input processor
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        //Arrows
        arrowUp = new Texture("Arrows/upButton.png");
        arrowDown = new Texture("Arrows/downButton.png");

        //Creates the world
        world = new World(new Vector2(0, -10), true);
        dinoAtlas = new TextureAtlas("Dinos/DinoSprites.txt");
        player1 = new DemoDinos(world, this, "douxSprites", 75, 400);
        player2 = new DemoDinos(world, this, "mortSprites", 150, 400);
        player3 = new DemoDinos(world, this, "nullSprites", 225, 400);
        player4 = new DemoDinos(world, this, "nullSprites", 300, 400);

        selections = new int[]{1, 2, 0, 0};
        takenSelections = new ArrayList<>();
        takenSelections.add(1);
        takenSelections.add(2);

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
        handleInput(deltaTime);

        game.batch.begin();

        player1.draw(game.batch);
        player2.draw(game.batch);
        game.batch.draw(arrowUp, 100, 200, arrowUp.getWidth() * 3, arrowUp.getHeight() * 3);
        game.batch.draw(arrowUp, 300, 200, arrowUp.getWidth() * 3, arrowUp.getHeight() * 3);
        game.batch.draw(arrowUp, 400, 200, arrowUp.getWidth() * 3, arrowUp.getHeight() * 3);
        game.batch.draw(arrowUp, 500, 200, arrowUp.getWidth() * 3, arrowUp.getHeight() * 3);
        game.batch.draw(arrowDown, 100, 0, arrowUp.getWidth() * 3, arrowUp.getHeight() * 3);

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
                dispose();
            }//end changed
        });

        start.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(!player1.getName().equals("nullSprites") &&!player2.getName().equals("nullSprites")) {
                    PlayScreen tempScreen = new PlayScreen(game);
                    game.setScreen(tempScreen);
                    stage.dispose();
                }
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

    private void handleInput(float dt) {
        //Player 1
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            testUp(0);
            player1 = setPlayer(selections[0], 75);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            testDown(0);
            player1 = setPlayer(selections[0], 75);
        }
        //Player 2
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            testUp(1);
            player2 = setPlayer(selections[1], 150);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            testDown(1);
            player2 = setPlayer(selections[1], 150);
        }
    }//end handleInput

    private void testUp(int num) {
        int tempselection = selections[num] + 1;
        while (true) {
            boolean canSwitch = false;
            for (int i = 0; i < takenSelections.size(); i++) {
                if (tempselection == takenSelections.get(i)) {
                    tempselection++;
                    canSwitch = false;
                    break;
                }
                canSwitch = true;
            }
            if (tempselection > 4) {
                tempselection = 0;
                break;
            } else if (canSwitch) {
                break;
            }
        }
        int previousSelection = selections[num];
        for (int i = 0; i < takenSelections.size(); i++) {
            if (previousSelection == takenSelections.get(i)) {
                takenSelections.remove(i);
            }
        }
        selections[num] = tempselection;
        takenSelections.add(tempselection);
    }//end testUp

    private void testDown(int num) {
        int tempselection = selections[num] - 1;
        while (true) {
            boolean canSwitch = false;
            for (int i = 0; i < takenSelections.size(); i++) {
                if (tempselection == takenSelections.get(i)) {
                    tempselection--;
                    canSwitch = false;
                    break;
                }
                canSwitch = true;
            }
            if (tempselection < 0) {
                tempselection = 4;
                break;
            } else if (canSwitch || tempselection == 0) {
                break;
            }
        }
        int previousSelection = selections[num];
        for (int i = 0; i < takenSelections.size(); i++) {
            if (previousSelection == takenSelections.get(i)) {
                takenSelections.remove(i);
            }
        }
        selections[num] = tempselection;
        takenSelections.add(tempselection);
    }//end tesetDown

    private DemoDinos setPlayer(int dinoNumber, int startingPos) {
        String name = "";
        if (dinoNumber == 0) {
            name = "nullSprites";
        } else if (dinoNumber == 1) {
            name = "douxSprites";
        } else if (dinoNumber == 2) {
            name = "mortSprites";
        } else if (dinoNumber == 3) {
            name = "tardSprites";
        } else if (dinoNumber == 4) {
            name = "vitaSprites";
        }
        return new DemoDinos(world, this, name, startingPos, 400);
    }//end setplayer

    public static String getDinoData(int playerNum) {
        if (playerNum == 2) {
            return  player2.getName();
        } else if (playerNum == 3) {
            return player3.getName();
        } else if (playerNum == 4) {
            return player4.getName();
        }
        return player1.getName();
    }//end getDino
}//end class