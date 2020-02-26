package com.dinoduel.game.Sprites.Weapons;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Screens.PlayScreen;
import com.dinoduel.game.Sprites.Dino;

public abstract class Weapon extends Sprite {
    public World world;
    public Body wBody;
    protected TextureRegion img;

    protected double damage;
    protected PlayScreen screen;

    public boolean empty = false;
    public boolean flag = false;

    protected float x;
    protected float y;
    //When Held:
    protected float heldXOffset;
    protected float heldYOffset;

    //Size
    public int xSize;
    public int ySize;

    protected float previousAngle = 0;

    protected Fixture fixture;
    protected Dino user;
    public boolean drawn = false;
    public boolean update = false;

    protected float buildTime = 0;
    protected float lastFireTime = 0;
    protected float dropTime = 0;
    public boolean spinStop = false;
    protected boolean upsideDown = false;

    public Weapon(float x, float y, World world, PlayScreen screen) {
        super(screen.getweaponAtlas().findRegion("weapons"));
        this.x = x;
        this.y = y;
        this.world = world;
        this.screen = screen;
    }//end Constructor

    public TextureRegion getFrame() {
        TextureRegion region = img;
        if (!user.isRunningRight() && !region.isFlipX()) {
            region.flip(true, false);
        } else if (user.isRunningRight() && region.isFlipX()) {
            region.flip(true, false);
        }
        return region;
    }//end getFrame

    public abstract void useWeapon();

    public void update(float dt) {
        buildTime += dt;



        if (user != null) {
            spinStop = false;
            if (user.isRunningRight()) {
                setPosition(user.b2body.getPosition().x - getWidth() / 2 + heldXOffset, user.b2body.getPosition().y - getHeight() / 2 + heldYOffset);


            } else {
                setPosition(user.b2body.getPosition().x - getWidth() / 2 - heldXOffset, user.b2body.getPosition().y - getHeight() / 2 + heldYOffset);
            }
            setRegion(getFrame());

        } else {
            if (spinStop) {
                //System.out.println("stop check angle" + (3/4*3.14)%(3.14/2));
                if ((wBody.getAngle() > (Math.PI/2) && wBody.getAngle() < (3*Math.PI/2) )|| (wBody.getAngle() < (-Math.PI/2) && wBody.getAngle() > (-3*Math.PI/2) )) {
                    wBody.setTransform(wBody.getPosition(), (float) Math.PI);
                    upsideDown = true;
                }else
                    wBody.setTransform(wBody.getPosition(), 0);


                wBody.setAngularVelocity(0);
                wBody.setFixedRotation(true);

                spinStop = false;

            }

            if (wBody.getAngularVelocity()!=0) {
                if (wBody.getAngularVelocity()>0) {
                    if ((wBody.getAngle()*180/Math.PI) > 360) {
                        wBody.setTransform(wBody.getPosition(), 0);
                    }
                } else {
                    if ((wBody.getAngle()*180/Math.PI) < -360) {
                        wBody.setTransform(wBody.getPosition(), 0);
                    }
                }
            }


            setPosition(wBody.getPosition().x - getWidth() / 2, wBody.getPosition().y - getHeight() / 2);
            setOriginCenter();
            rotate((float)((wBody.getAngle()-previousAngle) *180/Math.PI));
            previousAngle = wBody.getAngle();
        }

        if (empty && user == null) {
            if (dropTime==0) {
                dropTime = buildTime;
            } else if (buildTime-dropTime > 1) {
                flag = true;


            }
        } else {
            dropTime = 0;
        }



    }//end update

    public void defineWeapon() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(x / DinoDuel.PPM, y / DinoDuel.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        wBody = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(xSize / 20 / DinoDuel.PPM, ySize / 20 / DinoDuel.PPM);

        fdef.shape = shape;

        fdef.filter.categoryBits = DinoDuel.CATEGORY_WEAPON;
        fdef.filter.maskBits = DinoDuel.MASK_WEAPON;
        fixture = wBody.createFixture(fdef);
        fixture.setUserData(this);
    }//end defineWeapon

    public void setUser(Dino dino) {
            if (upsideDown) {
                rotate(180);
                upsideDown = false;
            }
            user = dino;
            wBody.setAwake(false);
            world.destroyBody(wBody);
            wBody = null;

    }//end setUser

    public Dino getUser() {
        return this.user;
    }//end getUser

    public void clearUser() {
        this.user = null;
    }//end clearUser

    public void dropped() {
        //recreates fixture
        BodyDef bdef = new BodyDef();
        bdef.position.set(user.b2body.getPosition().x, user.b2body.getPosition().y);
        bdef.type = BodyDef.BodyType.DynamicBody;
        wBody = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(xSize / 20 / DinoDuel.PPM, ySize / 20 / DinoDuel.PPM);

        fdef.shape = shape;

        fdef.filter.categoryBits = DinoDuel.CATEGORY_WEAPON;
        fdef.filter.maskBits = DinoDuel.MASK_WEAPON;
        fixture = wBody.createFixture(fdef);
        fixture.setUserData(this);

        wBody.setLinearVelocity(user.b2body.getLinearVelocity());
        wBody.setFixedRotation(false);
        //System.out.println("start angle" + wBody.getAngle());

        if (user.b2body.getLinearVelocity().equals(new Vector2(0,0))) {

        } else if (user.isRunningRight()) {
            //wBody.applyAngularImpulse(-4000f, true);
            wBody.setAngularVelocity(-10f);
            wBody.applyLinearImpulse(new Vector2(.5f, 2f), new Vector2(wBody.getWorldCenter()), true);
            previousAngle = wBody.getAngle();


        } else {
            //wBody.applyAngularImpulse(4000f, true);
            wBody.setAngularVelocity(10f);
            wBody.applyLinearImpulse(new Vector2(-.5f, 2f), new Vector2(wBody.getWorldCenter()), true);
            previousAngle = wBody.getAngle();

        }
        this.clearUser();
        this.update(0);

    }//end dropped
}//end class
