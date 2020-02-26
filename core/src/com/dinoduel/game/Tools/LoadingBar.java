package com.dinoduel.game.Tools;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class LoadingBar extends Actor {

    Animation<? extends TextureRegion> animation;
    TextureRegion reg;
    float stateTime;

    public LoadingBar(Animation<? extends TextureRegion> animation) {
        this.animation = animation;
        reg = animation.getKeyFrame(0);
    }//end constructor

    @Override
    public void act(float delta) {
        stateTime += delta;
        reg = animation.getKeyFrame(stateTime);
    }//end act

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(reg, getX(), getY());
    }//end draw
}//end class

