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
    public float speed;
    private int duration;
    public int damage;
    private float x;
    private float y;
    public Body bBody;
    public World world;
    public boolean flag = false;


    private TextureRegion img;
    protected Dino user;
    public boolean draw = false;
    public Gun gun;
    public PlayScreen screen;

    public Fixture fixture;

    public Bullet(float s, int dr, int dm, float x, float y, Dino u, PlayScreen screen, World world, Gun gun) {
        super(screen.getweaponAtlas().findRegion("weapons"));
        this.speed = s;
        this.duration = dr;
        this.damage = dm;
        this.x = x;
        this.y = y;
        this.user = u;
        this.world = world;
        this.gun = gun;
        this.screen = screen;

        img = new TextureRegion(getTexture(), 358, 138, 12, 6);
        defineBullet();
        fixture.setUserData(this);
        if (speed < 0) {
            img.flip(true, false);
        }

        setBounds(x, y, 12 / DinoDuel.PPM, 6 / DinoDuel.PPM);
        setRegion(img);
        //setPosition(x, y);
        setPosition(bBody.getPosition().x / DinoDuel.PPM - getWidth() / 2, bBody.getPosition().y / DinoDuel.PPM - getHeight() / 2);
        setSize(12 / 5 / DinoDuel.PPM, 6 / 5 / DinoDuel.PPM);

    }//end constructor

    public void hit(Dino target) {
        //add code so that when hit, decrease by damage
        //additional feature, accuracy, random number generated multiplied by accuracy multiplier
        System.out.println("hit ");
        float hitNum = 10 - (gun.accuracy * (float) Math.random());
        if (hitNum < gun.accuracy) {
            System.out.println("success");
            // TODO: 2020-02-20 Damage 
            target.health -= 0.1f;
        }
    }

    public void defineBullet() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(x, y);
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
        bBody.setLinearVelocity(new Vector2(speed, 0));
        bBody.setBullet(true);
    }//end defineBullet

    public void setUser(Dino dino) {
        this.user = dino;
    }

    public void update() {
        setPosition(bBody.getPosition().x - getWidth() / 2, bBody.getPosition().y - getHeight() / 2);

    }


}
