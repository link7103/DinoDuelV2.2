package com.dinoduel.game.Weapons;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Screens.PlayScreen;
import com.dinoduel.game.Sprites.Dino;

import java.util.ArrayList;

public abstract class Gun extends Sprite implements Weapon {
    public World world;
    public Body wBody;
    protected TextureRegion img;

    public boolean inUse = false;
    protected int ammo;
    protected int magCap;
    protected float firerate;
    protected int accuracy;
    public ArrayList<Bullet> mag = new ArrayList<Bullet>();
    protected Vector2 speed;
    protected int duration;
    protected int damage;

    protected float x;
    protected float y;
    //When Held:
    protected float heldXOffset;
    protected float heldYOffset;

    //Size
    public int xSize;
    public int ySize;

    protected Fixture fixture;
    protected Dino user;
    public boolean drawn = false;
    public boolean update = false;

    public Gun(float x, float y, World world, PlayScreen screen) {
        //adjust how this works - Currently loads one big section, change to loading individuals
        super(screen.getweaponAtlas().findRegion("weapons"));
        this.x = x;
        this.y = y;
        this.world = world;
    }


    public void defineWeapon() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(x / DinoDuel.PPM, y / DinoDuel.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        wBody = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        //*
        shape.setAsBox(xSize/20  / DinoDuel.PPM, ySize/20 / DinoDuel.PPM);

        fdef.shape = shape;

        fdef.filter.categoryBits = DinoDuel.CATEGORY_WEAPON;
        fdef.filter.maskBits = DinoDuel.MASK_WEAPON;
        fixture = wBody.createFixture(fdef);
    }//end defineWeapon

    public void update() {
        if (user != null) {
            if (user.isRunningRight()) {
                setPosition(user.b2body.getPosition().x - getWidth() / 2 + heldXOffset, user.b2body.getPosition().y - getHeight() / 2 + heldYOffset);
            } else {
                setPosition(user.b2body.getPosition().x - getWidth() / 2 - heldXOffset, user.b2body.getPosition().y - getHeight() / 2 + heldYOffset);
            }
            setRegion(getFrame());
        } else {
            setPosition(wBody.getPosition().x - getWidth() / 2, wBody.getPosition().y - getHeight() / 2);
        }

    }//end update

    public TextureRegion getFrame() {
        TextureRegion region = img;
        if (!user.isRunningRight() && !region.isFlipX()) {
            region.flip(true, false);
        } else if (user.isRunningRight() && region.isFlipX()) {
            region.flip(true, false);
        }
        return region;
    }//end getFrame

    public void setUser(Dino dino) {
        user = dino;
        wBody.setAwake(false);
        world.destroyBody(wBody);
        wBody = null;
        inUse = true;
    }//end setUser

    public abstract String getName();

    public Dino getUser() {
        return this.user;
    }//end getUser

    public void clearUser() {
        this.user = null;
    }//end clearUser

    public void useWeapon() {
        if (mag.size()>0) {
            //fire
            Bullet fired = mag.get(0);
            fired.draw = true;
            System.out.println("Drew bullet");


            // TODO: 2020-02-11 change to speed variable
            //fired.bBody.setLinearVelocity(20/DinoDuel.PPM, 0);

            mag.remove(fired);

        } else {
            //reload
            System.out.println("needs to be reloaded");
        }
    }

    public void dropped() {
        inUse = false;
        //recreates fixture
        BodyDef bdef = new BodyDef();
        bdef.position.set(user.b2body.getPosition().x, user.b2body.getPosition().y - user.getHeight() / 2);
        bdef.type = BodyDef.BodyType.DynamicBody;
        wBody = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(xSize / 20 / DinoDuel.PPM, ySize / 20 / DinoDuel.PPM);

        fdef.shape = shape;

        fdef.filter.categoryBits = DinoDuel.CATEGORY_WEAPON;
        fdef.filter.maskBits = DinoDuel.MASK_WEAPON;
        fixture = wBody.createFixture(fdef);

        wBody.setAwake(true);
        this.clearUser();
        this.update();
    }//end dropped
}//end class

