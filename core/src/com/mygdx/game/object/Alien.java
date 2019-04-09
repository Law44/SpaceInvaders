package com.mygdx.game.object;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets;

import java.util.Random;

public class Alien {

    int fila;
    enum State {
        LIVE, DYING, DEAD, MOVINGDOWN, FORMING
    }
    Vector2 position;
    float stateTime;
    TextureRegion frame;
    State state;
    Bonus bonus;
    double powerUp;

    public Alien(int x, int y, int fila) {
        position = new Vector2(x, y);
        state = State.LIVE;
        this.fila = fila;
        bonus = new Bonus(x, y);
        powerUp = Math.random()*10;
    }

    public void render(SpriteBatch batch) {
        batch.draw(frame, position.x, position.y);
        if (state == State.DYING) {
            if (bonus.isStart()) {
                bonus.render(batch);
            }
        }
    }

    void update(float delta, Assets assets){
        stateTime += delta;
        if(state == State.LIVE || state == State.FORMING || state == State.MOVINGDOWN) {
            frame = assets.alien.getKeyFrame(stateTime, true);
        } else if(state == State.DYING){
            frame = assets.aliendie.getKeyFrame(stateTime, false);
        }

        if(state == State.DYING){
            if (powerUp >= 9.5 && !bonus.isStart()) {
                bonus.spawn(true, position.x, position.y);
            }
            else if(assets.aliendie.isAnimationFinished(stateTime) && (bonus.isGet() || bonus.isDeleted())){
                state = State.DEAD;
            }
            else if (assets.aliendie.isAnimationFinished(stateTime) && powerUp < 9.5){
                state = State.DEAD;
            }


        }

        bonus.update(delta,assets);

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