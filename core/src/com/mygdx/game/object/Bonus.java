package com.mygdx.game.object;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets;

public class Bonus {

    Vector2 position;
    float stateTime;
    TextureRegion frame;
    boolean start;
    boolean get;
    boolean delete;

    public Bonus(float x, float y) {
        this.position = new Vector2(x, y);
    }

    public void getBonus(){
        this.get = true;
    }

    public void deleteBonus(){
        this.delete = true;
    }

    public boolean isDeleted(){
        return delete;
    }

    public boolean isStart() {
        return start;
    }

    public boolean isGet() {
        return get;
    }

    public void spawn(boolean start, float x, float y) {
        this.start = start;
        this.position = new Vector2(x, y);
    }

    public void render(SpriteBatch batch) {
        batch.draw(frame, position.x, position.y);
    }

    void update(float delta, Assets assets) {
        stateTime+= delta;
        frame = assets.naveidle.getKeyFrame(stateTime, false);
        if (start) {
            position.y--;
        }
    }


}
