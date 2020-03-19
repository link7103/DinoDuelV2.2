package com.dinoduel.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Screens.PlayScreen;
import com.dinoduel.game.Sprites.Weapons.Weapon;

import java.util.ArrayList;


public class Dino extends Sprite {
    public Body getB2body() {
        return b2body;
    }

    public void setB2body(Body b2body) {
        this.b2body = b2body;
    }

    //States
    public enum State {FALLING, JUMPING, STANDING, RUNNING, DUCKING, SLIDING, DUCKFALLING, CLIMBING, KICKING, DYING}

    public State currentState;
    public State previousState;
    private float stateTimer;

    public World world;
    private Body b2body;
    private boolean firstKickFrame = false;

    //Animations and Textures
    private TextureRegion dinoIdle0;
    private TextureRegion dinoDead;
    private Animation<TextureRegion> dinoIdle;
    private TextureRegion dinoDuck;
    private Animation<TextureRegion> dinoRun;
    private Animation<TextureRegion> dinoJump;
    private TextureRegion dinoSlide;
    private Animation<TextureRegion> dinoDies;
    private Animation<TextureRegion> dinoClimb;
    private Animation<TextureRegion> dinoKick;
    private TextureRegion dinoStationaryClimb;

    //Used for mapping the textures
    private boolean runningRight;
    public boolean playerDucking = false;

    //Weapons
    private Weapon weapon;
    public boolean hasWeapon = false;

    //kicking
    private boolean kicking = false;

    //determine if climbing
    public boolean climbing;

    //Directions
    public boolean KEYUP = false;
    public boolean KEYDOWN = false;
    public boolean KEYRIGHT = false;
    public boolean KEYLEFT = false;

    public boolean canMove;

    public Ladder currentLadder = null;

    private float standingHeight = 0;
    private float standingWidth = 0;
    private Vector2 startingPos;
    //Health
    public float health;
    private int lives;
    private boolean permaDead = false;
    private boolean dead;
    //Stats
    public long timeAlive;
    private String name;

    private PlayScreen screen;

    public Dino(World world, PlayScreen screen, String name, Vector2 startingPos, int lives) {
        //Initialize Variables
        super(screen.getDinoAtlas().findRegion(name));
        this.name = name;
        this.screen = screen;
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
        this.world = world;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;
        health = 1;
        this.lives = lives;
        this.startingPos = startingPos;
        //Sets up the various animations - will need to adjust the y value for subsequent players
        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < 3; i++) {
            frames.add(new TextureRegion(getTexture(), i * 24, dinoNumber * 24, 24, 24));
        }
        dinoIdle = new Animation(0.1f, frames);
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

        for (int i = 11; i < 13; i++) {
            frames.add(new TextureRegion(getTexture(), i * 24, dinoNumber * 24, 24, 24));
        }
        dinoKick = new Animation(0.1f, frames);
        frames.clear();

        for (int i = 19; i < 22; i++) {
            frames.add(new TextureRegion(getTexture(), i * 24, dinoNumber * 24, 24, 24));
        }
        dinoClimb = new Animation(0.1f, frames);
        frames.clear();
        //Dies
        frames.add(new TextureRegion(getTexture(), 15 * 24, dinoNumber * 24, 24, 24));
        frames.add(new TextureRegion(getTexture(), 14 * 24, dinoNumber * 24, 24, 24));
        frames.add(new TextureRegion(getTexture(), 15 * 24, dinoNumber * 24, 24, 24));
        frames.add(new TextureRegion(getTexture(), 14 * 24, dinoNumber * 24, 24, 24));
        frames.add(new TextureRegion(getTexture(), 15 * 24, dinoNumber * 24, 24, 24));
        frames.add(new TextureRegion(getTexture(), 14 * 24, dinoNumber * 24, 24, 24));

        dinoDies = new Animation(0.105f, frames);
        frames.clear();

        //Finishes setting up the dino and sets its sprite.
        defineDino(0);
        dinoIdle0 = new TextureRegion(getTexture(), 0, dinoNumber * 24, 24, 24);
        dinoDuck = new TextureRegion(getTexture(), 17 * 24, dinoNumber * 24, 24, 24);
        dinoSlide = new TextureRegion(getTexture(), 18 * 24, dinoNumber * 24, 24, 24);
        dinoDead = new TextureRegion(getTexture(), 0, dinoNumber * 24, 1, 1);
        dinoStationaryClimb = new TextureRegion(getTexture(), 20 * 24, dinoNumber * 24, 24, 24);
        setBounds(0, 0, 24 / DinoDuel.PPM, 24 / DinoDuel.PPM);
        setRegion(dinoIdle0);

        standingHeight = this.getHeight();
        standingWidth = this.getWidth();
    }//end constructor

    public void update(float dt) { //Updates the sprite every frame
        if (playerDucking && currentState != State.FALLING && currentState != State.JUMPING && currentState != State.CLIMBING) {
            if (currentState == State.SLIDING) {
                setPosition(getB2body().getPosition().x - 0.015f - getWidth() / 2, getB2body().getPosition().y - 0.025f - getHeight() / 2);
            } else {
                setPosition(getB2body().getPosition().x + 0.025f - getWidth() / 2, getB2body().getPosition().y + 0.0125f - getHeight() / 2);
            }
        } else {
            setPosition(getB2body().getPosition().x - getWidth() / 2, getB2body().getPosition().y - getHeight() / 2);
        }
        if (lives > 0) {
            setRegion(getFrame(dt));
        } else {
            setRegion(dinoDead);
        }


    }//end update

    private TextureRegion getFrame(float dt) { // Controls which animation or frame is played.

        currentState = getState();
        TextureRegion region;
        switch (currentState) {
            case DYING:
                region = dinoDies.getKeyFrame(stateTimer);
                if (!dead) {
                    dying();
                } else {
                    if (dinoDies.isAnimationFinished(stateTimer)) {
                        dies();
                    }
                }
                break;
            case KICKING:

                region = dinoKick.getKeyFrame(stateTimer, false);

                if (dinoKick.isAnimationFinished(stateTimer) && !firstKickFrame) {
                    kicking = false;
                }
                firstKickFrame = false;
                break;
            case JUMPING:
                region = dinoJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = dinoRun.getKeyFrame(stateTimer, true);
                break;
            case SLIDING:
                region = dinoSlide;
                break;
            case DUCKING:
            case DUCKFALLING:
                region = dinoDuck;
                break;
            case CLIMBING:
                if (b2body.getLinearVelocity().x != 0 || b2body.getLinearVelocity().y != 0)
                    region = dinoClimb.getKeyFrame(stateTimer, true);
                else
                    region = dinoStationaryClimb;
                break;
            case FALLING:
                region = dinoIdle0;
                break;
            case STANDING:
            default:
                region = dinoIdle.getKeyFrame(stateTimer, true);
                break;
        }
        if ((getB2body().getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        } else if ((getB2body().getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }
        //google to better understand
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }//end getFrame

    private State getState() {
        //Sets different states
        if (health <= 0) {
            health = 0;
            return State.DYING;
        } else if (kicking) {
            return State.KICKING;
        } else if (getB2body().getLinearVelocity().y > 0 && previousState == State.DUCKING) {
            defineDino(2);
            return State.JUMPING;
        } else if (getB2body().getLinearVelocity().y < 0 && !playerDucking && previousState == State.DUCKFALLING) {
            defineDino(2);
            return State.FALLING;
        } else if (playerDucking && getB2body().getLinearVelocity().x != 0 && getB2body().getLinearVelocity().y == 0) {
            defineDino(4);
        } else if ((playerDucking && previousState != State.DUCKING && previousState != State.SLIDING && getB2body().getLinearVelocity().y == 0)) {
            defineDino(1);
        } else if ((!playerDucking && (previousState == State.DUCKING || previousState == State.SLIDING)) || (previousState == State.CLIMBING && !climbing)) {
            defineDino(2);

        }
        if (climbing && previousState != State.CLIMBING) {
            defineDino(3);
            return State.CLIMBING;
        } else if (climbing) {
            return State.CLIMBING;
        } else if (getB2body().getLinearVelocity().y > 0 || (getB2body().getLinearVelocity().y < 0 && previousState == State.JUMPING)) {
            return State.JUMPING;
        } else if (getB2body().getLinearVelocity().y < 0 && (previousState == State.DUCKING || previousState == State.DUCKFALLING) && previousState != State.SLIDING) {
            return State.DUCKFALLING;
        } else if (getB2body().getLinearVelocity().y < 0 && previousState != State.SLIDING) {
            return State.FALLING;
        } else if (getB2body().getLinearVelocity().x != 0) {
            if (playerDucking) {
                return State.SLIDING;
            } else {
                return State.RUNNING;
            }
        } else if (playerDucking) {
            if (previousState == State.SLIDING) {
                return State.SLIDING;
            } else {
                return State.DUCKING;
            }
        } else {
            return State.STANDING;
        }

    }//end getState

    private void defineDino(int instruction) { //Side Sensors may need to be tweaked - (Head area?)
        //0 = Initialize, 1 = Ducking, 2 = Not Ducking, 3 = Climbing, 4 = Sliding
        BodyDef bdef = new BodyDef();
        if (instruction == 0) {
            //starting position. (Pass in for multiple players?)
            canMove = true;
            health = 1;
            bdef.position.set(startingPos);
            bdef.type = BodyDef.BodyType.DynamicBody;
            setB2body(world.createBody(bdef));

            FixtureDef fdef = new FixtureDef();
            fdef.filter.categoryBits = DinoDuel.CATEGORY_DINO;
            fdef.filter.maskBits = DinoDuel.MASK_DINO;

            PolygonShape headShape = new PolygonShape();
            headShape.setAsBox(6 / DinoDuel.PPM, 3 / DinoDuel.PPM, new Vector2(+0, 5f / DinoDuel.PPM), 0);
            fdef.shape = headShape;
            getB2body().createFixture(fdef).setUserData(this);

            PolygonShape bodyShape = new PolygonShape();
            bodyShape.setAsBox(4 / DinoDuel.PPM, 7 / DinoDuel.PPM, new Vector2(+0, +1f / DinoDuel.PPM), 0);
            fdef.shape = bodyShape;
            getB2body().createFixture(fdef).setUserData(this);

            //Head Sensor
            EdgeShape head = new EdgeShape();
            head.set(new Vector2(-3 / DinoDuel.PPM, 7.8f / DinoDuel.PPM), new Vector2(3 / DinoDuel.PPM, 7.8f / DinoDuel.PPM));
            fdef.shape = head;
            fdef.isSensor = true;
            getB2body().createFixture(fdef).setUserData("head");

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
            Vector2 currentPosition = getB2body().getPosition();
            Vector2 currentVelocity = getB2body().getLinearVelocity();
            world.destroyBody(getB2body());
            bdef.position.set(currentPosition);

            if (instruction == 1 && currentLadder == null) {//Duck
                bdef.type = BodyDef.BodyType.DynamicBody;
                setB2body(world.createBody(bdef));

                FixtureDef fdef = new FixtureDef();
                PolygonShape shape = new PolygonShape();
                shape.setAsBox(8f / DinoDuel.PPM, 5f / DinoDuel.PPM);


                fdef.shape = shape;
                fdef.filter.categoryBits = DinoDuel.CATEGORY_DINO;
                fdef.filter.maskBits = DinoDuel.MASK_DINO;
                getB2body().createFixture(fdef);
                getB2body().createFixture(fdef).setUserData(this);


                //setSize(getWidth()*(6f/8f), getHeight()*(2f/3f));


                //head sensor
                EdgeShape head = new EdgeShape();
                head.set(new Vector2(-8 / DinoDuel.PPM, 5 / DinoDuel.PPM), new Vector2(8 / DinoDuel.PPM, 5 / DinoDuel.PPM));
                fdef.shape = head;
                fdef.isSensor = true;
                getB2body().createFixture(fdef).setUserData("head");
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
            } else if (instruction == 4) {//Slide
                bdef.type = BodyDef.BodyType.DynamicBody;
                setB2body(world.createBody(bdef));

                FixtureDef fdef = new FixtureDef();
                PolygonShape shape = new PolygonShape();
                shape.setAsBox(11f / DinoDuel.PPM, 3f / DinoDuel.PPM);


                fdef.shape = shape;
                fdef.filter.categoryBits = DinoDuel.CATEGORY_DINO;
                fdef.filter.maskBits = DinoDuel.MASK_DINO;
                getB2body().createFixture(fdef);
                getB2body().createFixture(fdef).setUserData(this);


            } else if (instruction == 2) {//Unduck
                bdef.type = BodyDef.BodyType.DynamicBody;
                setB2body(world.createBody(bdef));

                FixtureDef fdef = new FixtureDef();
                fdef.filter.categoryBits = DinoDuel.CATEGORY_DINO;
                fdef.filter.maskBits = DinoDuel.MASK_DINO;

                PolygonShape headShape = new PolygonShape();
                headShape.setAsBox(6 / DinoDuel.PPM, 3 / DinoDuel.PPM, new Vector2(+0, 5f / DinoDuel.PPM), 0);
                fdef.shape = headShape;
                getB2body().createFixture(fdef).setUserData(this);

                PolygonShape bodyShape = new PolygonShape();
                bodyShape.setAsBox(4 / DinoDuel.PPM, 7 / DinoDuel.PPM, new Vector2(+0, +1f / DinoDuel.PPM), 0);
                fdef.shape = bodyShape;
                getB2body().createFixture(fdef).setUserData(this);


                //head sensor
                EdgeShape head = new EdgeShape();
                head.set(new Vector2(-3 / DinoDuel.PPM, 7.8f / DinoDuel.PPM), new Vector2(3 / DinoDuel.PPM, 7.8f / DinoDuel.PPM));
                fdef.shape = head;
                fdef.isSensor = true;
                getB2body().createFixture(fdef).setUserData("head");

                setSize(standingWidth, standingHeight);
            } else if (instruction == 3 || (currentLadder != null && instruction == 1)) { //Climbing
                currentVelocity = new Vector2(0, 0);
                bdef.type = BodyDef.BodyType.DynamicBody;
                setB2body(world.createBody(bdef));
                getB2body().setGravityScale(0);

                FixtureDef fdef = new FixtureDef();
                fdef.filter.categoryBits = DinoDuel.CATEGORY_DINO;
                fdef.filter.maskBits = DinoDuel.MASK_DINOCLIMBING;

                PolygonShape headShape = new PolygonShape();
                headShape.setAsBox(6 / DinoDuel.PPM, 3 / DinoDuel.PPM, new Vector2(+0, 5f / DinoDuel.PPM), 0);
                fdef.shape = headShape;
                getB2body().createFixture(fdef).setUserData(this);

                PolygonShape bodyShape = new PolygonShape();
                bodyShape.setAsBox(4 / DinoDuel.PPM, 7 / DinoDuel.PPM, new Vector2(+0, +1f / DinoDuel.PPM), 0);
                fdef.shape = bodyShape;
                getB2body().createFixture(fdef).setUserData(this);

            }
            getB2body().setLinearVelocity(currentVelocity);
        }
    }//end defineDino

    public void pickupWeapon(ArrayList<Weapon> allWeapons) {
        for (Weapon weapon : allWeapons) {
            if (!hasWeapon && weapon.getUser() == null) {
                //Checks to see if the x and y coordinate of the Dino is inside of the gun
                if (weapon.getBoundingRectangle().overlaps(new Rectangle(getB2body().getPosition().x - .04f, getB2body().getPosition().y - .07f, .08f, .14f)) || weapon.getBoundingRectangle().overlaps(new Rectangle(getB2body().getPosition().x - .04f, getB2body().getPosition().y + .03f, .12f, .06f)) || ((currentState == State.DUCKING || currentState == State.SLIDING) && weapon.getBoundingRectangle().overlaps(new Rectangle(getB2body().getPosition().x - .08f, getB2body().getPosition().y - .05f, .16f, .10f)))) {
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

    public void setWeapon(Weapon newWeapon) {
        weapon = newWeapon;
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
        return getB2body().getLinearVelocity().y;
    }

    public void kick(ArrayList<Dino> allPlayers) {
        kicking = true;
        firstKickFrame = true;
        for (Dino player : allPlayers
        ) {
            if (player != this) {
                if (player.playerDucking) {
                    if (new Rectangle(b2body.getPosition().x - .06f, b2body.getPosition().y - .07f, .12f, .14f).overlaps(new Rectangle(player.getB2body().getPosition().x - .08f, player.getB2body().getPosition().y - .08f, .16f, .10f))) {
                        player.headKicked();
                    }
                } else {


                    if ((new Rectangle(b2body.getPosition().x - .06f, b2body.getPosition().y - .07f, .12f, .14f).overlaps(new Rectangle(player.getB2body().getPosition().x - .06f, player.getB2body().getPosition().y + .03f, .12f, .06f))) && this.b2body.getPosition().y > player.getB2body().getPosition().y + 0.01f) {
                        player.headKicked();
                    } else if (new Rectangle(b2body.getPosition().x - .06f, b2body.getPosition().y - .07f, .12f, .14f).overlaps(new Rectangle(player.getB2body().getPosition().x - .06f, player.getB2body().getPosition().y - .07f, .12f, .14f))) {
                        player.kicked();
                    }
                }
            }
        }

    }

    private void dying() {
        if (hasWeapon) {
            dropWeapon();
        }
        canMove = false;
        getB2body().setGravityScale(0);
        getB2body().setLinearVelocity(0, 0);
        dead = true;
    }//end dying

    private void dies() {
        screen.game.playingSoundEffect = screen.game.manager.assetManager.get(screen.game.manager.sFX[0]);
        screen.game.playingSoundEffect.play();
        lives--;
        world.destroyBody(getB2body());
        dead = false;
        if (lives > 0) {
            defineDino(0);
        } else {
            permaDead = true;
        }
    }//end dies

    public boolean getPermaDead() {
        return permaDead;
    }//end getPermaDead

    public String getName() {
        return name;
    }//end getName

    public void kicked() {
        health -= .11f;
    }

    public void headKicked() {
        health -= .21f;
    }

    public void explosionDamage(Circle blastArea) {
        int divisor = (int) (b2body.getPosition().dst(blastArea.x, blastArea.y)*DinoDuel.PPM);
        System.out.println("divisor" + divisor);

        if (divisor < blastArea.radius * DinoDuel.PPM && divisor>0) {
            health -= 1f/divisor;
        } else if (divisor == 0) {
            health-=1f;
            this.dies();
        }
    }
}//end Dino
