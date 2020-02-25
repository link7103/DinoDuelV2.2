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

    protected Fixture fixture;
    protected Dino user;
    public boolean drawn = false;
    public boolean update = false;

    protected float buildTime = 0;
    protected float lastFireTime = 0;
    protected float dropTime = 0;

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
            if (user.isRunningRight()) {
                setPosition(user.b2body.getPosition().x - getWidth() / 2 + heldXOffset, user.b2body.getPosition().y - getHeight() / 2 + heldYOffset);
            } else {
                setPosition(user.b2body.getPosition().x - getWidth() / 2 - heldXOffset, user.b2body.getPosition().y - getHeight() / 2 + heldYOffset);
            }
            setRegion(getFrame());
        } else {
            setPosition(wBody.getPosition().x - getWidth() / 2, wBody.getPosition().y - getHeight() / 2);
        }

        if (empty && user == null) {
            if (dropTime==0) {
                dropTime = buildTime;
            } else if (buildTime-dropTime > 10) {
                flag = true;


            }
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
    }//end defineWeapon

    public void setUser(Dino dino) {
        if (!empty) {
            user = dino;
            wBody.setAwake(false);
            world.destroyBody(wBody);
            wBody = null;
        }
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

        wBody.setLinearVelocity(user.b2body.getLinearVelocity());
        if (user.isRunningRight()) {
            wBody.applyLinearImpulse(new Vector2(.5f, 2f), new Vector2(wBody.getWorldCenter()), false);
            wBody.applyAngularImpulse(-20f, true);
        } else {
            wBody.applyLinearImpulse(new Vector2(-.5f, 2f), new Vector2(wBody.getWorldCenter()), false);
            wBody.applyAngularImpulse(20f, true);
        }
        this.clearUser();
        this.update(0);
    }//end dropped
}//end class
