package com.dinoduel.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Screens.AbstractScreen;
import com.dinoduel.game.Screens.CharcterSelectMenu;

public class DemoDinos extends Sprite {
    //States
    public enum State {FALLING, JUMPING, STANDING, RUNNING, DUCKING, DUCKRUNNING, DUCKFALLING}

    public State currentState;
    public State previousState;
    private float stateTimer;

    //Animations and Textures
    private TextureRegion dinoIdle0;
    private Animation<TextureRegion> dinoIdle;
    private TextureRegion dinoDuck;
    private Animation<TextureRegion> dinoRun;
    private Animation<TextureRegion> dinoJump;
    private Animation<TextureRegion> dinoDuckRun;
    private String name;
    public float timeALive;
    private int placement;

    public DemoDinos(AbstractScreen screen, String name, float startingPosX, float startingPosY, float size, float timeAlive) {
        //Initialize Variables
        super(screen.getDinoAtlas().findRegion(name));
        this.name = name;
        this.timeALive = timeAlive;
        placement = 0;
        int dinoNumber = 0;
        if (name.equalsIgnoreCase("nullSprites")) {
            dinoNumber = 0;
        } else if (name.equalsIgnoreCase("douxSprites")) {
            dinoNumber = 1;
        } else if (name.equalsIgnoreCase("mortSprites")) {
            dinoNumber = 2;
        } else if (name.equalsIgnoreCase("tardSprites")) {
            dinoNumber = 3;
        } else if (name.equalsIgnoreCase("vitaSprites")) {
            dinoNumber = 4;
        }
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        float tempX = startingPosX;
        startingPosX = tempX - 80;
        //Sets up the various animations - will need to adjust the y value for subsequent players
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 0; i < 3; i++) {
            frames.add(new TextureRegion(getTexture(), i * 24, dinoNumber * 24, 24, 24));
        }
        dinoIdle = new Animation(0.15f, frames);
        frames.clear();

        for (int i = 4; i < 9; i++) {
            frames.add(new TextureRegion(getTexture(), i * 24, dinoNumber * 24, 24, 24));
        }
        dinoRun = new Animation(0.1f, frames);
        frames.clear();

        for (int i = 11; i < 13; i++) {
            frames.add(new TextureRegion(getTexture(), i * 24, dinoNumber * 24, 24, 24));
        }
        dinoJump = new Animation(0.1f, frames);
        frames.clear();

        for (int i = 18; i < 23; i++) {
            frames.add(new TextureRegion(getTexture(), i * 24, dinoNumber * 24, 24, 24));
        }
        dinoDuckRun = new Animation(0.1f, frames);
        frames.clear();

        //Finishes setting up the dino and sets its sprite.
        dinoIdle0 = new TextureRegion(getTexture(), 0, dinoNumber * 24, 24, 24);
        dinoDuck = new TextureRegion(getTexture(), 17 * 24, dinoNumber * 24, 24, 24);
        setBounds(startingPosX, startingPosY, 24 / DinoDuel.PPM, 24 / DinoDuel.PPM);
        setRegion(dinoIdle0);
        setSize(getWidth() * size, getHeight() * size);
    }//end constructor

    public void update(float dt) { //Updates the sprite every frame
        setRegion(getFrame(dt));
    }//end update

    private TextureRegion getFrame(float dt) { // Controls which animation or frame is played.
        currentState = getState();
        TextureRegion region;
        switch (currentState) {
            case JUMPING:
                region = dinoJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = dinoRun.getKeyFrame(stateTimer, true);
                break;
            case DUCKRUNNING:
                region = dinoDuckRun.getKeyFrame(stateTimer, true);
                break;
            case DUCKING:
            case DUCKFALLING:
                region = dinoDuck;
                break;
            case STANDING:
            default:
                region = dinoIdle.getKeyFrame(stateTimer, true);
                break;
        }

        //google to better understand
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }//end getFrame

    private State getState() {
        return State.STANDING;
    }//end getState

    public String getName() {
        return name;
    }//end getName

    public void setPlacement(int num) {
        placement =  num;
    }//end setPlacement

    public int getPlacement() {
        return placement;
    }//end getPlacement

}//end Dino


