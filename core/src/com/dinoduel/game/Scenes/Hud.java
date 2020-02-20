package com.dinoduel.game.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dinoduel.game.DinoDuel;


public class Hud  extends Sprite implements Disposable {
    public Stage stage;
    private Viewport viewport;

    private Integer worldTimer;
    private float timeCount;
    private Integer score;

//Player1
    Label player1Label;
    Label player1NameLabel;
//Player2
    Label player2Label;
    Label player2NameLabel;
    //Player 3
    Label player3Label;
    Label player3NameLabel;
    //Player 4
    Label player4Label;
    Label player4NameLabel;

    Label timeLabel;
    Label stageLabel;
    Label stageNameLabel;
    Label countDownLabel;

    private TextureRegion player1StockIcon;

    public Hud(SpriteBatch sb) {
        // FIXME: 2/14/2020 
        //player1StockIcon = new TextureRegion(getTexture(), 0, spriteStartingYValue, 24, 0);

        worldTimer = 300;
        timeCount = 0;
        score = 3;
        viewport = new FitViewport(DinoDuel.V_WIDTH, DinoDuel.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top().bottom();
        table.setFillParent(true);

        countDownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        stageLabel = new Label("STAGE", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        stageNameLabel = new Label("StageName", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        //player1

        player1Label = new Label("Dino1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        player1NameLabel = new Label(String.format("%06d", score), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
//Add more for 2 player dino Label

        table.add(player1Label).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.add(stageLabel).expandX().padTop(10);
        table.row();
        table.add(player1NameLabel).expandX();
        table.add(countDownLabel).expandX();
        table.add(stageNameLabel).expandX().padTop(10);

        stage.addActor(table);

    }

    @Override
    public void dispose() {
        stage.dispose();
    }//end dispose
}//end class
