package com.dinoduel.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Screens.PlayScreen;
import com.dinoduel.game.Sprites.Weapons.Shotgun;
import com.dinoduel.game.Sprites.Weapons.Weapon;

import java.util.ArrayList;


public class Dino extends Sprite {
    //States
    public enum State {FALLING, JUMPING, STANDING, RUNNING, DUCKING, DUCKRUNNING, DUCKFALLING, CLIMBING, KICKING}

    public State currentState;
    public State previousState;
    private float stateTimer;

    public World world;
    public Body b2body;

    PlayScreen screen;

    //Animations and Textures
    private TextureRegion dinoIdle0;
    private Animation<TextureRegion> dinoIdle;
    private TextureRegion dinoDuck;
    private Animation<TextureRegion> dinoRun;
    private Animation<TextureRegion> dinoJump;
    private Animation<TextureRegion> dinoDuckRun;

    //Used for mapping the textures
    private boolean runningRight;
    public boolean playerDucking = false;

    //Weapons
    private Weapon weapon;
    public boolean hasWeapon = false;

    //kicking
    public boolean kicking = false;

    //determine if climbing
    public boolean climbing;

    //Directions
    public boolean KEYUP = false;
    public boolean KEYDOWN = false;
    public boolean KEYRIGHT = false;
    public boolean KEYLEFT = false;

    public Ladder currentLadder = null;

    //Health
    public float health;

    public Dino(World world, PlayScreen screen, String name, int spriteStartingYValue) {
        //Initialize Variables
        super(screen.getDinoAtlas().findRegion(name));
        this.world = world;
        this.screen = screen;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;
        health = 1;


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
        if (playerDucking && currentState != State.FALLING && currentState != State.JUMPING) {
            if (runningRight) {
                setPosition(b2body.getPosition().x - 0.025f - getWidth() / 2, b2body.getPosition().y + 0.0125f - getHeight() / 2);
            } else {
                setPosition(b2body.getPosition().x + 0.025f - getWidth() / 2, b2body.getPosition().y + 0.0125f - getHeight() / 2);

            }
        } else {
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        }

        setRegion(getFrame(dt));

        if (weapon != null) {
            weapon.update(dt);
        }
        healthCheck();
    }//end update

    public TextureRegion getFrame(float dt) { // Controls which animation or frame is played.

        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case KICKING:
                region = dinoJump.getKeyFrame(stateTimer, true);
                break;
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
            case CLIMBING:
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
        //Sets different states
        if (b2body.getLinearVelocity().y > 0 && previousState == State.DUCKING) {
            defineDino(2);

            return State.JUMPING;
        } else if (b2body.getLinearVelocity().y < 0 && !playerDucking && previousState == State.DUCKFALLING) {
            defineDino(2);

            return State.FALLING;
        } else if ((playerDucking && previousState != State.DUCKING && previousState != State.DUCKRUNNING && b2body.getLinearVelocity().y == 0)) {
            defineDino(1);

        } else if ((!playerDucking && (previousState == State.DUCKING || previousState == State.DUCKRUNNING)) || (previousState == State.CLIMBING && !climbing)) {
            defineDino(2);

        }

        if (climbing && previousState != State.CLIMBING) {
            defineDino(3);
            return State.CLIMBING;
        } else if (climbing) {
            return State.CLIMBING;
        } else if (b2body.getLinearVelocity().y > 0 || (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)) {
            return State.JUMPING;
        } else if (b2body.getLinearVelocity().y < 0 && (previousState == State.DUCKING || previousState == State.DUCKRUNNING || previousState == State.DUCKFALLING)) {
            return State.DUCKFALLING;
        } else if (b2body.getLinearVelocity().y < 0) {
            return State.FALLING;
        } else if (b2body.getLinearVelocity().x != 0) {
            if (playerDucking) {
                return State.DUCKRUNNING;
            } else {
                return State.RUNNING;
            }
        } else if (playerDucking) {
            return State.DUCKING;
        } else {
            return State.STANDING;
        }
    }//end getState

    public void defineDino(int instruction) { //Side Sensors may need to be tweaked - (Head area?)
        //0 = Initialize, 1 = Ducking, 2 = Not Ducking, 3 climbing
        BodyDef bdef = new BodyDef();

        if (instruction == 0) {
            //starting position. (Pass in for multiple players?)
            health = 1;
            bdef.position.set(32 / DinoDuel.PPM, 32 / DinoDuel.PPM);
            bdef.type = BodyDef.BodyType.DynamicBody;
            b2body = world.createBody(bdef);

            FixtureDef fdef = new FixtureDef();
            fdef.filter.categoryBits = DinoDuel.CATEGORY_DINO;
            fdef.filter.maskBits = DinoDuel.MASK_DINO;

            PolygonShape headShape = new PolygonShape();
            headShape.setAsBox(7 / DinoDuel.PPM, 3 / DinoDuel.PPM, new Vector2(+0, 5f / DinoDuel.PPM), 0);
            fdef.shape = headShape;
            b2body.createFixture(fdef).setUserData(this);

            PolygonShape bodyShape = new PolygonShape();
            bodyShape.setAsBox(4 / DinoDuel.PPM, 7 / DinoDuel.PPM, new Vector2(+0, +1f / DinoDuel.PPM), 0);
            fdef.shape = bodyShape;
            b2body.createFixture(fdef).setUserData(this);

            //Head Sensor
            EdgeShape head = new EdgeShape();
            head.set(new Vector2(-3 / DinoDuel.PPM, 7.8f / DinoDuel.PPM), new Vector2(3 / DinoDuel.PPM, 7.8f / DinoDuel.PPM));
            fdef.shape = head;
            fdef.isSensor = true;
            b2body.createFixture(fdef).setUserData("head");

            //Side Sensors
            /*EdgeShape right = new EdgeShape();
            right.set(new Vector2(3 / DinoDuel.PPM, -8 / DinoDuel.PPM), new Vector2(5 / DinoDuel.PPM, 8 / DinoDuel.PPM));
            fdef.shape = right;
            fdef.isSensor = true;
            b2body.createFixture(fdef).setUserData("side");

            EdgeShape left = new EdgeShape();
            left.set(new Vector2(-3 / DinoDuel.PPM, -8 / DinoDuel.PPM), new Vector2(-5 / DinoDuel.PPM, 8 / DinoDuel.PPM));
            fdef.shape = left;
            fdef.isSensor = true;
            b2body.createFixture(fdef).setUserData("side");

             */


        } else {
            Vector2 currentPosition = b2body.getPosition();
            Vector2 currentVelocity = b2body.getLinearVelocity();
            world.destroyBody(b2body);
            bdef.position.set(currentPosition);

            if (instruction == 1) {//Duck
                //System.out.println(1);
                bdef.type = BodyDef.BodyType.DynamicBody;
                b2body = world.createBody(bdef);

                FixtureDef fdef = new FixtureDef();
                PolygonShape shape = new PolygonShape();
                shape.setAsBox(8 / DinoDuel.PPM, 6 / DinoDuel.PPM);

                fdef.shape = shape;
                fdef.filter.categoryBits = DinoDuel.CATEGORY_DINO;
                fdef.filter.maskBits = DinoDuel.MASK_DINO;
                b2body.createFixture(fdef);
                b2body.createFixture(fdef).setUserData(this);

                //side sensors
                /*EdgeShape right = new EdgeShape();
                right.set(new Vector2(8 / DinoDuel.PPM, -6.65f / DinoDuel.PPM), new Vector2(8 / DinoDuel.PPM,  6.65f / DinoDuel.PPM));
                fdef.shape = right;
                fdef.isSensor = true;
                b2body.createFixture(fdef).setUserData("side");

                EdgeShape left = new EdgeShape();
                left.set(new Vector2(-8 / DinoDuel.PPM, -6.65f / DinoDuel.PPM), new Vector2(-8 / DinoDuel.PPM,  6.65f / DinoDuel.PPM));
                fdef.shape = left;
                fdef.isSensor = true;
                b2body.createFixture(fdef).setUserData("side");


                 */
            } else if (instruction == 2) {//Unduck
                //System.out.println(2);
                bdef.type = BodyDef.BodyType.DynamicBody;
                b2body = world.createBody(bdef);

                FixtureDef fdef = new FixtureDef();
                fdef.filter.categoryBits = DinoDuel.CATEGORY_DINO;
                fdef.filter.maskBits = DinoDuel.MASK_DINO;

                PolygonShape headShape = new PolygonShape();
                headShape.setAsBox(7 / DinoDuel.PPM, 3 / DinoDuel.PPM, new Vector2(+0, 5f / DinoDuel.PPM), 0);
                fdef.shape = headShape;
                b2body.createFixture(fdef).setUserData(this);

                PolygonShape bodyShape = new PolygonShape();
                bodyShape.setAsBox(4 / DinoDuel.PPM, 7 / DinoDuel.PPM, new Vector2(+0, +1f / DinoDuel.PPM), 0);
                fdef.shape = bodyShape;
                b2body.createFixture(fdef).setUserData(this);

                //head sensor
                EdgeShape head = new EdgeShape();
                head.set(new Vector2(-3 / DinoDuel.PPM, 7.8f / DinoDuel.PPM), new Vector2(3 / DinoDuel.PPM, 7.8f / DinoDuel.PPM));
                fdef.shape = head;
                fdef.isSensor = true;
                b2body.createFixture(fdef).setUserData("head");
            } else if (instruction == 3) { //Climbing
                //System.out.println(3);
                currentVelocity = new Vector2(0, 0);
                bdef.type = BodyDef.BodyType.DynamicBody;
                b2body = world.createBody(bdef);
                b2body.setGravityScale(0);

                FixtureDef fdef = new FixtureDef();
                fdef.filter.categoryBits = DinoDuel.CATEGORY_DINO;
                fdef.filter.maskBits = DinoDuel.MASK_DINOCLIMBING;

                PolygonShape headShape = new PolygonShape();
                headShape.setAsBox(7 / DinoDuel.PPM, 3 / DinoDuel.PPM, new Vector2(+0, 5f / DinoDuel.PPM), 0);
                fdef.shape = headShape;
                b2body.createFixture(fdef).setUserData(this);

                CircleShape bodyShape = new CircleShape();
                bodyShape.setRadius(4 / DinoDuel.PPM);
                bodyShape.setPosition(new Vector2(0, -4f / DinoDuel.PPM));
                fdef.shape = bodyShape;
                b2body.createFixture(fdef).setUserData(this);

            }
            b2body.setLinearVelocity(currentVelocity);
        }
    }//end defineDino

    public void pickupWeapon(ArrayList<Weapon> allWeapons) {
        for (Weapon weapon : allWeapons) {
            if (!hasWeapon) {
                //Checks to see if the x and y coordinate of the Dino is inside of the gun (+ of - a couple of pixels to be safe)
                if (((weapon.getBoundingRectangle().contains(b2body.getPosition().x, b2body.getPosition().y - 0.04f)) || (weapon.getBoundingRectangle().contains(b2body.getPosition().x - 0.02f, b2body.getPosition().y - 0.04f)) || (weapon.getBoundingRectangle().contains(b2body.getPosition().x + 0.02f, b2body.getPosition().y - 0.04f))) && !weapon.inUse) {
                    hasWeapon = true;
                    weapon.setUser(this);
                    this.weapon = weapon;
                    break;
                }
            }
        }
    }//end pickupGun

    public void dropWeapon() {
        hasWeapon = false;
        weapon.dropped();
        weapon = null;
    }//end dropGun

    public void useWeapon() {
        weapon.useWeapon();
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public boolean isRunningRight() {
        return runningRight;
    }//end isRunningRight

    public boolean isDucking() {
        return playerDucking;
    }

    public float getYVel() {
        return b2body.getLinearVelocity().y;
    }

    public void climbing() {

    }

    public void kick() {
        kicking = true;
    }

    public void healthCheck() {
        if (health <= 0) {
            if (hasWeapon) {
                dropWeapon();
            }
            world.destroyBody(b2body);
            defineDino(0);
        }
    }//end HealthCheck

}//end Dino
