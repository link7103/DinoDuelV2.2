package com.dinoduel.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Sprites.DemoDinos;

import java.util.ArrayList;

public class CharcterSelectMenu extends AbstractScreen {
    private Stage stage;
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
    private OrthographicCamera gameCam;

    public CharcterSelectMenu(DinoDuel game) {

        super(game);
        /// create stage and set it as input processor
        stage = new Stage(new FillViewport(DinoDuel.V_WIDTH, DinoDuel.V_HEIGHT));
        Gdx.input.setInputProcessor(stage);
        //Arrows
        arrowUp = new Texture("Arrows/upButton.png");
        arrowDown = new Texture("Arrows/downButton.png");

        //Creates the world
        world = new World(new Vector2(0, -10), true);
        dinoAtlas = new TextureAtlas("Dinos/DinoSprites.txt");
        player1 = new DemoDinos(this, "douxSprites", -240, -180, 700, 0);
        player2 = new DemoDinos(this, "mortSprites", -80, -180,700,0);
        player3 = new DemoDinos(this, "nullSprites", 80, -180,700,0);
        player4 = new DemoDinos(this, "nullSprites", 240, -180,700,0);
        selections = new int[]{1, 2, 0, 0};
        takenSelections = new ArrayList<>();
        takenSelections.add(1);
        takenSelections.add(2);
        gameCam = new OrthographicCamera();
        gameCam.position.set(DinoDuel.V_WIDTH / DinoDuel.PPM, DinoDuel.V_HEIGHT / DinoDuel.PPM, 0);
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
        player3.update(deltaTime);
        player4.update(deltaTime);
        handleInput(deltaTime);
        game.batch.setProjectionMatrix(gameCam.combined);
        gameCam.update();
        game.batch.begin();

        player1.draw(game.batch);
        player2.draw(game.batch);
        player3.draw(game.batch);
        player4.draw(game.batch);
        game.batch.draw(arrowUp, -240 - arrowUp.getWidth() * 3 / 2, 0 - arrowUp.getHeight() * 3 / 2, arrowUp.getWidth() * 3, arrowUp.getHeight() * 3);
        game.batch.draw(arrowUp, -80 - arrowUp.getWidth() * 3 / 2, 0 - arrowUp.getHeight() * 3 / 2, arrowUp.getWidth() * 3, arrowUp.getHeight() * 3);
        game.batch.draw(arrowUp, 80 - arrowUp.getWidth() * 3 / 2, 0 - arrowUp.getHeight() * 3 / 2, arrowUp.getWidth() * 3, arrowUp.getHeight() * 3);
        game.batch.draw(arrowUp, 240 - arrowUp.getWidth() * 3 / 2, 0 - arrowUp.getHeight() * 3 / 2, arrowUp.getWidth() * 3, arrowUp.getHeight() * 3);
        game.batch.draw(arrowDown, -240 - arrowUp.getWidth() * 3 / 2, -200 - arrowUp.getHeight() * 3 / 2, arrowUp.getWidth() * 3, arrowUp.getHeight() * 3);
        game.batch.draw(arrowDown, -80 - arrowUp.getWidth() * 3 / 2, -200 - arrowUp.getHeight() * 3 / 2, arrowUp.getWidth() * 3, arrowUp.getHeight() * 3);
        game.batch.draw(arrowDown, 80 - arrowUp.getWidth() * 3 / 2, -200 - arrowUp.getHeight() * 3 / 2, arrowUp.getWidth() * 3, arrowUp.getHeight() * 3);
        game.batch.draw(arrowDown, 240 - arrowUp.getWidth() * 3 / 2, -200 - arrowUp.getHeight() * 3 / 2, arrowUp.getWidth() * 3, arrowUp.getHeight() * 3);
        game.batch.end();
    }//end render

    public void update(float dt) { //Updates the screen every frame
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        gameCam.viewportWidth = stage.getViewport().getScreenWidth();
        gameCam.viewportHeight = stage.getViewport().getScreenHeight();
        gameCam.update();
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
            }//end changed
        });

        start.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!player1.getName().equals("nullSprites") && !player2.getName().equals("nullSprites")) {
                    PlayScreen tempScreen = new PlayScreen(game);
                    game.setScreen(tempScreen);
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
        super.dispose();
    }//end dispose

    public TextureAtlas getDinoAtlas() {
        return dinoAtlas;
    }//end getDinoAtlas

    private void handleInput(float dt) {
        //Player 1
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            testUp(0);
            player1 = setPlayer(selections[0], -240);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            testDown(0);
            player1 = setPlayer(selections[0], -240);
        }
        //Player 2
        if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            testUp(1);
            player2 = setPlayer(selections[1], -80);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            testDown(1);
            player2 = setPlayer(selections[1], -80);
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
    }//end testDown

    private DemoDinos setPlayer(int dinoNumber, float startingPosX) {
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
        return new DemoDinos(this, name, startingPosX,-180, 700,0);
    }//end setplayer

    public static String getDinoData(int playerNum) {
        if (playerNum == 2) {
        return player2.getName();
    } else if (playerNum == 3) {
        return player3.getName();
    } else if (playerNum == 4) {
        return player4.getName();
    }
        return player1.getName();
}//end getDinoData
}//end class