package com.dinoduel.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.bullet.collision._btMprSimplex_t;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Sprites.DemoDinos;

import java.util.ArrayList;

public class VictoryScreen extends AbstractScreen {
    private Stage stage;
    //Player
    private static DemoDinos player1;
    private static DemoDinos player2;
    private static DemoDinos player3;
    private static DemoDinos player4;
    //Player Sprites
    private TextureAtlas dinoAtlas;
    public static ArrayList<DemoDinos> allPlayers = new ArrayList<>();
    private OrthographicCamera gameCam;


    public VictoryScreen(DinoDuel game) {
        super(game);
        gameCam = new OrthographicCamera();
        gameCam.position.set(DinoDuel.V_WIDTH / DinoDuel.PPM, DinoDuel.V_HEIGHT / DinoDuel.PPM, 0);
        /// create stage and set it as input processor
        stage = new Stage(new FillViewport(DinoDuel.V_WIDTH, DinoDuel.V_HEIGHT));

        Gdx.input.setInputProcessor(stage);

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
        // update(deltaTime);
        player1.update(deltaTime);
        player2.update(deltaTime);
        player3.update(deltaTime);
        player4.update(deltaTime);
        game.batch.setProjectionMatrix(gameCam.combined);
        gameCam.update();

        game.batch.begin();
        player1.draw(game.batch);
        player2.draw(game.batch);
        player3.draw(game.batch);
        player4.draw(game.batch);
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
                    game.setScreen(new PlayScreen(game));
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

    public void createPlayers() {
        String p1 = PlayScreen.getDinoName(1);
        float p1Time = PlayScreen.getDinoTime(1);
        String p2 = PlayScreen.getDinoName(2);
        float p2Time = PlayScreen.getDinoTime(2);
        String p3 = "nullSprites";
        float p3Time = 0;
        String p4 = "nullSprites";
        float p4Time = 0;
        if (PlayScreen.allPlayers.size() >= 3) {
            p3 = PlayScreen.getDinoName(3);
            p3Time = PlayScreen.getDinoTime(3);
        }
        if (PlayScreen.allPlayers.size() == 4) {
            p4 = PlayScreen.getDinoName(4);
            p4Time = PlayScreen.getDinoTime(4);
        }
        player1 = new DemoDinos(this, p1, 0, 0, 700, p1Time);
        allPlayers.add(player1);
        player2 = new DemoDinos(this, p2, 0, 0, 700, p2Time);
        allPlayers.add(player2);
        player3 = new DemoDinos(this, p3, 0, 0, 700, p3Time);
        allPlayers.add(player3);
        player4 = new DemoDinos(this, p4, 0, 0, 700, p4Time);
        allPlayers.add(player4);
        PlayScreen.allPlayers.clear();

        //FIXME Improve how this functions (There has to be a better way
        //Give the players placements based on their time alive
        int firstPlace = 0;
        for (int i = 0; i < allPlayers.size(); i++) {
            if (allPlayers.get(i).timeALive > allPlayers.get(firstPlace).timeALive) {
                firstPlace = i;
            }
        }
        allPlayers.get(firstPlace).setPlacement(1);
        allPlayers.remove(firstPlace);

        int secondPlace = 0;
        for (int i = 0; i < allPlayers.size(); i++) {
            if (allPlayers.get(i).timeALive > allPlayers.get(secondPlace).timeALive) {
                secondPlace = i;
            }
        }
        allPlayers.get(secondPlace).setPlacement(2);
        allPlayers.remove(secondPlace);

        int thirdPlace = 0;
        for (int i = 0; i < allPlayers.size(); i++) {
            if (allPlayers.get(i).timeALive > allPlayers.get(thirdPlace).timeALive) {
                thirdPlace = i;
            }
        }
        allPlayers.get(thirdPlace).setPlacement(3);
        allPlayers.remove(thirdPlace);

        //Fourth
        allPlayers.get(0).setPlacement(4);
        allPlayers.remove(0);
        //Re Adds The players
        allPlayers.add(player1);
        allPlayers.add(player2);
        allPlayers.add(player3);
        allPlayers.add(player4);

        for (int i = 0; i < allPlayers.size(); i++) {
            if (allPlayers.get(i).getPlacement() == 1) {
                allPlayers.get(i).setPosition(-80, -10);
            }
            else if(allPlayers.get(i).getPlacement() == 2) {
                allPlayers.get(i).setPosition(+80, -60);
            }else if (allPlayers.get(i).getPlacement() == 3) {
                allPlayers.get(i).setPosition(-240, -110);
            } else {
                allPlayers.get(i).setPosition(240, -160);
            }

        }
    }//end createPlayers
}//end class
