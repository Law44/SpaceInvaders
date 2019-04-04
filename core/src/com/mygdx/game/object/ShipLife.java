package com.mygdx.game.object;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets;

public class ShipLife {

    Vector2 position;
    TextureRegion frame;
    float stateTime;
    int WORLD_WIDTH, WORLD_HEIGHT;


    int vidas;

    ShipLife(int WORLD_WIDTH, int WORLD_HEIGHT){
        this.WORLD_HEIGHT = WORLD_HEIGHT;
        this.WORLD_WIDTH = WORLD_WIDTH;
    }

    void update(int vidas, float delta, Assets assets){
        stateTime += delta;
        this.vidas = vidas;
        this.frame = assets.naveidle.getKeyFrame(stateTime, true);
    }
    void render(SpriteBatch batch){
        for (int i = 1; i <= vidas; i++) {
            batch.draw(frame, WORLD_WIDTH-(40 * i), WORLD_HEIGHT - 15);
        }
    }
}
