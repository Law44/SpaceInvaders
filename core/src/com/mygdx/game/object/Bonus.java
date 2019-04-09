package com.mygdx.game.object;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets;

public class Bonus {

    Vector2 position;
    float stateTime;
    TextureRegion frame;
    State state;

    enum State {
        FALLING, OBTAINED, DELETED
    }

    public Bonus(float x, float y) {
        this.position = new Vector2(x, y);
    }

    public void getBonus(){
        this.state = State.OBTAINED;
    }

    public void deleteBonus(){
        this.state = State.DELETED;
    }

    public boolean isDeleted(){
        return state == State.DELETED;
    }

    public boolean isFalling() {
        return state == State.FALLING;
    }

    public boolean isObtained() {
        return state == State.OBTAINED;
    }

    public void spawn(float x, float y) {
        this.state = State.FALLING;
        this.position = new Vector2(x, y);
    }

    public void render(SpriteBatch batch) {
        batch.draw(frame, position.x, position.y, frame.getRegionWidth()*0.8f, frame.getRegionHeight()*0.8f);
    }

    void update(float delta, Assets assets) {
        stateTime+= delta;
        frame = assets.naveidle.getKeyFrame(stateTime, false);
        if (this.state == State.FALLING) {
            position.y--;
        }
    }


}
