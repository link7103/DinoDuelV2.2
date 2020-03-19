package com.dinoduel.game.Sprites.Weapons;

import com.badlogic.gdx.graphics.g2d.Animation;
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
    protected World world;
    public Body wBody;
    TextureRegion img;
    protected boolean reloading = false;
    protected Animation<TextureRegion> weaponEmpty;
    protected TextureRegion idle;

    double damage;
    protected PlayScreen screen;

    public Dino.State currentState;
    public Dino.State previousState;
    protected float stateTimer;

    boolean empty = false;
    public boolean flag = false;

    protected float x;
    protected float y;
    //When Held:
    float heldXOffset;
    float heldYOffset;

    //Size
    public int xSize;
    public int ySize;

    private float previousAngle = 0;

    private Fixture fixture;
    Dino user;
    public boolean drawn = false;
    public boolean update = false;

    float buildTime = 0;
    float lastFireTime = 0;
    private float dropTime = 0;
    public boolean spinStop = false;
    int reloadCount = -1;

    Weapon(float x, float y, World world, PlayScreen screen) {
        super(screen.getweaponAtlas().findRegion("weaponsV2"));
        this.x = x;
        this.y = y;
        this.world = world;
        this.screen = screen;
        stateTimer = 0;
    }//end Constructor

    protected TextureRegion getFrame(float dt) {
        TextureRegion region;
        if (!empty)
            region = img;
        else
            region = weaponEmpty.getKeyFrame(stateTimer, true);

        float neg;
        if (user.isRunningRight()) {
             neg = 1;
        } else {
             neg = -1;
        }
        if (!user.isRunningRight() && !region.isFlipX()) {
            region.flip(true, false);
        } else if (user.isRunningRight() && region.isFlipX()) {
            region.flip(true, false);
        }

        if(reloading) {

            reloading = false;
            reloadCount = 0;
        } else if (reloadCount >=0) {
            System.out.println(dt);
            if (reloadCount < 180) {
                rotate(neg * .5f);
                reloadCount++;
            } else if ( reloadCount < 280) {
                translateY(-1f*neg/DinoDuel.PPM);
                reloadCount++;
            } else if (reloadCount < 380) {
                translateY(neg*1f/DinoDuel.PPM);
                reloadCount++;
            } else if (reloadCount<560) {
                rotate(-.5f*neg);
                reloadCount++;
            } else {
                reloadCount = -1;
            }

        }



        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }//end getFrame

    public abstract void useWeapon();

    public void update(float dt) {
        buildTime += dt;


        if (user != null) {




            spinStop = false;
            if (user.isRunningRight()) {
                setPosition(user.getB2body().getPosition().x - getWidth() / 2 + heldXOffset, user.getB2body().getPosition().y - getHeight() / 2 + heldYOffset);


            } else {
                setPosition(user.getB2body().getPosition().x - getWidth() / 2 - heldXOffset, user.getB2body().getPosition().y - getHeight() / 2 + heldYOffset);
            }
            setRegion(this.getFrame(dt));

        } else {

            if( isFlipX() && !img.isFlipX())
                img.flip(true,false);
            else if(!isFlipX() && img.isFlipX())
                img.flip(true,false);

            setRegion(img);


            if (spinStop) {
                //System.out.println("stop check angle" + (3/4*3.14)%(3.14/2));
                if ((wBody.getAngle() > (Math.PI/2) && wBody.getAngle() < (3*Math.PI/2) )|| (wBody.getAngle() < (-Math.PI/2) && wBody.getAngle() > (-3*Math.PI/2) )) {
                    wBody.setTransform(wBody.getPosition(), (float) Math.PI);
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
        if (this instanceof Explosive) {
            ((Explosive) this).explosiveUpdate();
        }




    }//end update

    void defineWeapon() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(x / DinoDuel.PPM, y / DinoDuel.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        wBody = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(xSize / 20f / DinoDuel.PPM, ySize / 20f / DinoDuel.PPM);

        fdef.shape = shape;

        fdef.filter.categoryBits = DinoDuel.CATEGORY_WEAPON;
        fdef.filter.maskBits = DinoDuel.MASK_WEAPON;
        fixture = wBody.createFixture(fdef);
        fixture.setUserData(this);
    }//end defineWeapon

    public void setUser(Dino dino) {
        if (user == null) {
            spinStop = true;
            update(0);


            setRotation(0);
            previousAngle = 0;


            user = dino;
            if (wBody != null ) {
                wBody.setAwake(false);
                world.destroyBody(wBody);
                wBody = null;
            }
        }

    }//end setUser

    public Dino getUser() {
        return this.user;
    }//end getUser

    protected void clearUser() {
        this.user = null;



    }//end clearUser

    public void dropped() {
        //recreates fixture

        if(reloadCount !=-1) {
            setRotation(0);
            reloadCount = -1;
        }

        if (user != null) {
            BodyDef bdef = new BodyDef();
            bdef.position.set(user.getB2body().getPosition().x, user.getB2body().getPosition().y);
            bdef.type = BodyDef.BodyType.DynamicBody;
            wBody = world.createBody(bdef);

            FixtureDef fdef = new FixtureDef();
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(xSize / 20f / DinoDuel.PPM, ySize / 20f / DinoDuel.PPM);

            fdef.shape = shape;

            fdef.filter.categoryBits = DinoDuel.CATEGORY_WEAPON;
            fdef.filter.maskBits = DinoDuel.MASK_WEAPON;
            fixture = wBody.createFixture(fdef);
            fixture.setUserData(this);

            wBody.setLinearVelocity(user.getB2body().getLinearVelocity());
            wBody.setFixedRotation(false);
            //System.out.println("start angle" + wBody.getAngle());

            if (user.getB2body().getLinearVelocity().equals(new Vector2(0, 0)))
                wBody.setAngularVelocity(0);
            else if (user.isRunningRight()) {
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

        }
    }//end dropped

}//end class
