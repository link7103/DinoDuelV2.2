package com.dinoduel.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Screens.PlayScreen;
import com.dinoduel.game.Tools.B2WorldCreator;
import com.dinoduel.game.Weapons.Gun;
import com.dinoduel.game.Weapons.Weapon;

import java.util.ArrayList;


public class Dino extends Sprite {

    public enum State {FALLING, JUMPING, STANDING, RUNNING, DUCKING, DUCKRUNNING, DUCKFALLING}

    public State currentState;
    public State previousState;

    public World world;
    public Body b2body;
    private TextureRegion dinoIdle0;
    private Animation<TextureRegion> dinoIdle;
    private TextureRegion dinoDuck;
    private Animation<TextureRegion> dinoRun;
    private Animation<TextureRegion> dinoJump;
    private Animation<TextureRegion> dinoDuckRun;
    private float stateTimer;
    private boolean runningRight;
    public boolean playerDucking = true;
    private Weapon weapon;
    private boolean hasWeapon = false;

    public Dino(World world, PlayScreen screen, String name, int spriteStartingYValue) {
        //Initialize Variables
        super(screen.getDinoAtlas().findRegion(name));
        this.world = world;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        //Sets up the various animations - will need to adjust the y value for subsequent players
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 0; i < 3; i++) {
            frames.add(new TextureRegion(getTexture(), i * 24, spriteStartingYValue, 24, 24));
        }
        dinoIdle = new Animation(0.1f, frames);
        frames.clear();

        for (int i = 4; i < 9; i++) {
            frames.add(new TextureRegion(getTexture(), i * 24, spriteStartingYValue, 24, 24));
        }
        dinoRun = new Animation(0.1f, frames);
        frames.clear();

        for (int i = 11; i < 13; i++) {
            frames.add(new TextureRegion(getTexture(), i * 24, spriteStartingYValue, 24, 24));
        }
        dinoJump = new Animation(0.1f, frames);
        frames.clear();

        for (int i = 18; i < 23; i++) {
            frames.add(new TextureRegion(getTexture(), i * 24, spriteStartingYValue, 24, 24));
        }
        dinoDuckRun = new Animation(0.1f, frames);
        frames.clear();
        //Finishes setting up the dino and sets its sprite.
        defineDino(0);
        dinoIdle0 = new TextureRegion(getTexture(), 0, spriteStartingYValue, 24, 24);
        dinoDuck = new TextureRegion(getTexture(), 17 * 24, spriteStartingYValue, 24, 24);
        setBounds(0, 0, 24 / DinoDuel.PPM, 24 / DinoDuel.PPM);
        setRegion(dinoIdle0);
    }//end constructor

    public void update(float dt) { //Updates the sprite every frame
        if(playerDucking && currentState != State.FALLING && currentState != State.JUMPING) {
            if(runningRight){
                setPosition(b2body.getPosition().x - (float)0.025 - getWidth() / 2, b2body.getPosition().y + (float) 0.0125 - getHeight() / 2);
            }else{
                setPosition(b2body.getPosition().x + (float)0.025 - getWidth() / 2, b2body.getPosition().y + (float) 0.0125 - getHeight() / 2);

            }
        }else {
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        }
        setRegion(getFrame(dt));
        if (weapon!= null) {
            weapon.update();
        }
    }//end update

    public TextureRegion getFrame(float dt) { // Controls which animation or frame is played.
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
                region = dinoDuck;
                break;
            case DUCKFALLING:
                region = dinoDuck;
                break;
            case FALLING:
                region = dinoIdle0;
                break;
            case STANDING:
            default:
                region = dinoIdle.getKeyFrame(stateTimer, true);
                break;
        }
        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        } else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }
        //google to better understand
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }//end getFrame

    public State getState() {

        if (b2body.getLinearVelocity().y > 0 && previousState == State.DUCKING){
            defineDino(2);
            return State.JUMPING;
        } else if (b2body.getLinearVelocity().y < 0 && !playerDucking && previousState == State.DUCKFALLING ){
            defineDino(2);
            return State.FALLING;
        }
        //Calls for a change in collision box
        if ((playerDucking && previousState != State.DUCKING && previousState != State.DUCKRUNNING && b2body.getLinearVelocity().y == 0) ) {
            defineDino(1);
        } else if ((!playerDucking && (previousState == State.DUCKING || previousState == State.DUCKRUNNING))) {
            defineDino(2);
        }
        //Sets different states
        if (b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)) {
            return State.JUMPING;
        }else if (b2body.getLinearVelocity().y<0 && (previousState == State.DUCKING || previousState == State.DUCKRUNNING || previousState == State.DUCKFALLING))
            return State.DUCKFALLING;
        else if (b2body.getLinearVelocity().y < 0  )
            return State.FALLING;
        else if (b2body.getLinearVelocity().x != 0)
            //will need to adapt for multiple players
            if (playerDucking) {
                return State.DUCKRUNNING;
            } else {
                return State.RUNNING;
            }
        else if (playerDucking)
            return State.DUCKING;
        else
            return State.STANDING;
    }//end getState


    public void defineDino(int instruction) {
        //0 = Initialize, 1 = Ducking, 3 = Not Ducking
        BodyDef bdef = new BodyDef();

        if (instruction == 0) {
            //starting position. (Pass in for multiple players?)
            bdef.position.set(32 / DinoDuel.PPM, 32 / DinoDuel.PPM);
            bdef.type = BodyDef.BodyType.DynamicBody;
            b2body = world.createBody(bdef);

            FixtureDef fdef = new FixtureDef();
            PolygonShape shape = new PolygonShape();
            //shape.setAsBox(3 / DinoDuel.PPM, 8 /
            //sets as a polygon
            Vector2[] coordinates = new Vector2[4];
            coordinates[0] = new Vector2(-3/DinoDuel.PPM, -8/DinoDuel.PPM);
            coordinates[1] = new Vector2(3/DinoDuel.PPM, -8/DinoDuel.PPM);
            coordinates[2] = new Vector2(-5/DinoDuel.PPM, 8/DinoDuel.PPM);
            coordinates[3] = new Vector2(5/DinoDuel.PPM, 8/DinoDuel.PPM);
            shape.set(coordinates);

            fdef.shape = shape;
            fdef.filter.categoryBits = DinoDuel.CATEGORY_DINO;
            fdef.filter.maskBits = DinoDuel.MASK_DINO;
            b2body.createFixture(fdef);


            //head sensor
            EdgeShape head = new EdgeShape();
            head.set(new Vector2(-3/DinoDuel.PPM, 8/DinoDuel.PPM ), new Vector2(3/DinoDuel.PPM, 8/DinoDuel.PPM ));
            fdef.shape = head;
            fdef.isSensor = true;
            b2body.createFixture(fdef).setUserData("head");

            //side sensors
            EdgeShape right = new EdgeShape();
            right.set(new Vector2(3/DinoDuel.PPM, -8/DinoDuel.PPM), new Vector2(5/DinoDuel.PPM, 8/DinoDuel.PPM));
            fdef.shape = right;
            fdef.isSensor = true;
            b2body.createFixture(fdef).setUserData("side");

            EdgeShape left = new EdgeShape();
            left.set(new Vector2(-3/DinoDuel.PPM, -8/DinoDuel.PPM), new Vector2(-5/DinoDuel.PPM, 8/DinoDuel.PPM));
            fdef.shape = left;
            fdef.isSensor = true;
            b2body.createFixture(fdef).setUserData("side");





        } else {
            Vector2 currentPosition = b2body.getPosition();
            Vector2 currentVelocity = b2body.getLinearVelocity();
            world.destroyBody(b2body);
            bdef.position.set(currentPosition);
            if (instruction == 1) {//Duck
                bdef.type = BodyDef.BodyType.DynamicBody;
                b2body = world.createBody(bdef);

                FixtureDef fdef = new FixtureDef();
                PolygonShape shape = new PolygonShape();
                shape.setAsBox(8 / DinoDuel.PPM, (float)6.65 / DinoDuel.PPM);

                fdef.shape = shape;
                fdef.filter.categoryBits = DinoDuel.CATEGORY_DINO;
                fdef.filter.maskBits = DinoDuel.MASK_DINO;
                b2body.createFixture(fdef);
                b2body.setLinearVelocity(currentVelocity);



                //side sensors
                EdgeShape right = new EdgeShape();
                right.set(new Vector2(8/DinoDuel.PPM, -(float)6.65/DinoDuel.PPM), new Vector2(8/DinoDuel.PPM, (float)6.65/DinoDuel.PPM));
                fdef.shape = right;
                fdef.isSensor = true;
                b2body.createFixture(fdef).setUserData("side");

                EdgeShape left = new EdgeShape();
                left.set(new Vector2(-8/DinoDuel.PPM, -(float)6.65/DinoDuel.PPM), new Vector2(-8/DinoDuel.PPM, (float)6.65/DinoDuel.PPM));
                fdef.shape = left;
                fdef.isSensor = true;
                b2body.createFixture(fdef).setUserData("side");


            } else {//Unduck
                bdef.type = BodyDef.BodyType.DynamicBody;
                b2body = world.createBody(bdef);

                FixtureDef fdef = new FixtureDef();
                PolygonShape shape = new PolygonShape();
                //shape.setAsBox(3 / DinoDuel.PPM, 8 / DinoDuel.PPM);
                //sets as polygon
                Vector2[] coordinates = new Vector2[4];
                coordinates[0] = new Vector2(-3/DinoDuel.PPM, -8/DinoDuel.PPM);
                coordinates[1] = new Vector2(3/DinoDuel.PPM, -8/DinoDuel.PPM);
                coordinates[2] = new Vector2(-5/DinoDuel.PPM, 8/DinoDuel.PPM);
                coordinates[3] = new Vector2(5/DinoDuel.PPM, 8/DinoDuel.PPM);
                shape.set(coordinates);

                fdef.shape = shape;
                fdef.filter.categoryBits = DinoDuel.CATEGORY_DINO;
                fdef.filter.maskBits = DinoDuel.MASK_DINO;
                b2body.createFixture(fdef);
                b2body.createFixture(fdef).setUserData("body");

                //head sensor
                EdgeShape head = new EdgeShape();
                head.set(new Vector2(-3/DinoDuel.PPM, 8/DinoDuel.PPM ), new Vector2(3/DinoDuel.PPM, 8/DinoDuel.PPM ));
                fdef.shape = head;
                fdef.isSensor = true;
                b2body.createFixture(fdef).setUserData("head");

                //side sensors
                EdgeShape right = new EdgeShape();
                right.set(new Vector2(3/DinoDuel.PPM, -8/DinoDuel.PPM), new Vector2(5/DinoDuel.PPM, 8/DinoDuel.PPM));
                fdef.shape = right;
                fdef.isSensor = true;
                b2body.createFixture(fdef).setUserData("side");

                EdgeShape left = new EdgeShape();
                left.set(new Vector2(-3/DinoDuel.PPM, -8/DinoDuel.PPM), new Vector2(-5/DinoDuel.PPM, 8/DinoDuel.PPM));
                fdef.shape = left;
                fdef.isSensor = true;
                b2body.createFixture(fdef).setUserData("side");

                b2body.setLinearVelocity(currentVelocity);

            }
        }


    }//end defineDino



    public void pickupGun(ArrayList<Gun> guns) {
        for (Gun gun: guns
             ) {

            if (!hasWeapon) {
                Gdx.app.log("Run", "Run");
                Gdx.app.log("Dino x",  "" + (this.b2body.getPosition().x));
                Gdx.app.log("Dino y",  "" + (this.b2body.getPosition().y));
                Gdx.app.log("Dino width",  "" + (5/DinoDuel.PPM));
                Gdx.app.log("Dino height",  "" + (8/DinoDuel.PPM));
                Gdx.app.log("gun x",  "" + (gun.wBody.getPosition().x));
                Gdx.app.log("gun y",  "" + (gun.wBody.getPosition().y));
                Gdx.app.log("gun width",  "" + (gun.xSize/2/DinoDuel.PPM));
                Gdx.app.log("gun height",  "" + (gun.ySize/2/DinoDuel.PPM));
                if ((this.b2body.getPosition().x - 5/DinoDuel.PPM <= gun.wBody.getPosition().x + gun.xSize / 2/DinoDuel.PPM && this.b2body.getPosition().x - 5/DinoDuel.PPM >= gun.wBody.getPosition().x - gun.xSize / 2/DinoDuel.PPM) ||
                        (this.b2body.getPosition().x + 5/DinoDuel.PPM <= gun.wBody.getPosition().x + gun.xSize / 2/DinoDuel.PPM && this.b2body.getPosition().x + 5/DinoDuel.PPM >= gun.wBody.getPosition().x - gun.xSize / 2/DinoDuel.PPM) ||
                        (this.b2body.getPosition().x  <= gun.wBody.getPosition().x + gun.xSize / 2/DinoDuel.PPM && this.b2body.getPosition().x  >= gun.wBody.getPosition().x - gun.xSize / 2/DinoDuel.PPM)) {


                    if ((this.b2body.getPosition().y - 8/DinoDuel.PPM <= gun.wBody.getPosition().y + gun.ySize / 2/DinoDuel.PPM && this.b2body.getPosition().y - 8/DinoDuel.PPM  >= gun.wBody.getPosition().y - gun.ySize / 2/DinoDuel.PPM) || (this.b2body.getPosition().y + 8/DinoDuel.PPM <= gun.wBody.getPosition().y + gun.ySize / 2/DinoDuel.PPM && this.b2body.getPosition().y + 8/DinoDuel.PPM >= gun.wBody.getPosition().y - gun.ySize / 2/DinoDuel.PPM)) {
                        Gdx.app.log("Dino", gun.getName());
                        hasWeapon = true;
                        gun.setUser(this);

                        break;
                    }
                }
            }
        }
    }
}//end Dino
