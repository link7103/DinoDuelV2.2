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

public class Bullet extends Sprite {
    private float speedX;
    private float speedY;
    private float duration;
    public double damage;
    private float x;
    private float y;
    public Body bBody;
    public World world;
    public boolean flag = false;
    private float heightOffset;

    private TextureRegion img;
    protected Dino user;
    public boolean draw = false;
    public Gun gun;
    public PlayScreen screen;

    public Fixture fixture;

    private float buildTime = 0;

    public Bullet(float sX, float sY, float dr, double dm, float x, float y, Dino u, PlayScreen screen, World world, Gun gun, float heightOffset) {
        super(screen.getweaponAtlas().findRegion("weapons"));
        this.speedX = sX;
        this.speedY = sY;
        this.duration = dr;
        this.damage = dm;
        this.x = x;
        this.y = y;
        this.user = u;
        this.world = world;
        this.gun = gun;
        this.screen = screen;
        this.heightOffset = heightOffset;
        img = new TextureRegion(getTexture(), 358, 138, 12, 6);
        defineBullet();
        fixture.setUserData(this);
        if (speedX < 0) {
            img.flip(true, false);
        }

        setBounds(x, y, 12 / DinoDuel.PPM, 6 / DinoDuel.PPM);
        setRegion(img);
        //setPosition(x, y);
        // FIXME: 2/24/2020 I Dont this the setposition works - it is not affected by a heightoffeset variable
        setPosition(bBody.getPosition().x / DinoDuel.PPM - getWidth() / 2, bBody.getPosition().y / DinoDuel.PPM + heightOffset);
        setSize((16f / 5f) / DinoDuel.PPM, (8f / 5f) / DinoDuel.PPM);

    }//end constructor

    public void hit(Dino target) {
        //add code so that when hit, decrease by damage
        //additional feature, accuracy, random number generated multiplied by accuracy multiplier
        //System.out.println("hit ");
        float hitNum = 10 - (gun.accuracy * (float) Math.random());
        if (hitNum < gun.accuracy) {
            //System.out.println("success");
            target.health -= (float) (0.1 * damage);
        }
    }//end hit

    public void defineBullet() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(x, y + heightOffset);
        bdef.type = BodyDef.BodyType.DynamicBody;
        bBody = world.createBody(bdef);
        bBody.setGravityScale(0);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(12 / 10 / DinoDuel.PPM, 6 / 10 / DinoDuel.PPM);
        fdef.shape = shape;
        fdef.isSensor = true;

        fdef.filter.categoryBits = DinoDuel.CATEGORY_BULLET;
        fdef.filter.maskBits = DinoDuel.MASK_BULLET;
        fixture = bBody.createFixture(fdef);
        bBody.setLinearVelocity(new Vector2(speedX+user.b2body.getLinearVelocity().x, speedY+user.b2body.getLinearVelocity().y));
        bBody.setBullet(true);
    }//end defineBullet

    public void setUser(Dino dino) {
        this.user = dino;
    }//end setUser

    public void update(float dt) {
        buildTime += dt;
        if (buildTime < duration)
            setPosition(bBody.getPosition().x - getWidth() / 2, bBody.getPosition().y - getHeight() / 2 + heightOffset);
        else
            flag = true;
    }//end Update
}//end class
