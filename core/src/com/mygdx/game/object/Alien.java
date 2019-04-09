package com.mygdx.game.object;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets;

public class Alien {

    int fila;
    enum State {
        LIVE, DYING, DEAD, MOVINGDOWN, FORMING
    }
    Vector2 position;
    float stateTime;
    TextureRegion frame;
    State state;

    public Alien(int x, int y, int fila) {
        position = new Vector2(x, y);
        state = State.LIVE;
        this.fila = fila;
    }

    public void render(SpriteBatch batch) {
        batch.draw(frame, position.x, position.y);
    }

    void update(float delta, Assets assets){
        stateTime += delta;
        if(state == State.LIVE || state == State.FORMING || state == State.MOVINGDOWN) {
            frame = assets.alien.getKeyFrame(stateTime, true);
        } else if(state == State.DYING){
            frame = assets.aliendie.getKeyFrame(stateTime, false);
        }

        if(state == State.DYING){
            if(assets.aliendie.isAnimationFinished(stateTime)){
                state = State.DEAD;
            }
        }
    }

    void shoot(){

    }

    public void kill() {
        state = State.DYING;
        stateTime = 0;

    }

    public boolean isAlive() {
        return state == State.LIVE;
    }

}