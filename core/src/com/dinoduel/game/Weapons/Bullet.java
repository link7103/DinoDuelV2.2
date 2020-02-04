package com.dinoduel.game.Weapons;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.dinoduel.game.DinoDuel;
import com.dinoduel.game.Screens.PlayScreen;
import com.dinoduel.game.Sprites.Dino;

public class Bullet extends Sprite {
    public Vector2 speed;
    private int duration;
    public int damage;
    private int x;
    private int y;
    public Body bBody;
    public World world;
    private TextureRegion img;
    private Dino user;

    public Bullet (Vector2 s, int dr, int dm, int x, int y, Dino u, PlayScreen screen) {
        super(screen.getweaponAtlas().findRegion("guns"));
        this.speed = s;
        this.duration = dr;
        this.damage = dm;
        this.x = x;
        this.y= y;
        this.user = u;

        img = new TextureRegion(getTexture(), 27, 29, 4, 3);

        setBounds(0, 0, 4 / DinoDuel.PPM, 3 / DinoDuel.PPM);
        setRegion(img);
        setPosition(bBody.getPosition().x/DinoDuel.PPM-getWidth()/2, bBody.getPosition().y/DinoDuel.PPM-getHeight()/2);
    }

    public void hit() {
        //add code so that when hit, decrease by damage
        //additional feature, accuracy, random number generated multiplied by accuracy multiplier
    }

    public void defineBullet() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(x / DinoDuel.PPM, y / DinoDuel.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        bBody = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(4/2 / DinoDuel.PPM, 3/2 / DinoDuel.PPM);

        fdef.shape = shape;

        //fdef.filter.categoryBits = CATEGORY_WEAPON;
        //fdef.filter.maskBits = MASK_WEAPON;
        bBody.createFixture(fdef);
        bBody.setLinearVelocity(speed);
        bBody.setBullet(true);

        // TODO: 2020-02-01 make sure that it is oriented on the correct side of the bullet
        //leading edge of bullet
        EdgeShape side = new EdgeShape();
        side.set(new Vector2(2/DinoDuel.PPM, (float)-1.5/DinoDuel.PPM), new Vector2(2/DinoDuel.PPM, (float)1.5/DinoDuel.PPM));
        fdef.shape = side;
        fdef.isSensor = true;
        bBody.createFixture(fdef);
    }


}
