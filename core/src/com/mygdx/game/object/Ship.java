package com.mygdx.game.object;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets;
import com.mygdx.game.Controls;

public class Ship {

    enum State {
        IDLE, LEFT, RIGHT, SHOOT, DYING;
    }

    Vector2 position;

    State state;
    float stateTime;
    float speed = 5;
    boolean pausa;
    int initialPosition;

    TextureRegion frame;

    Weapon weapon;

    public boolean isPausa() {
        return pausa;
    }

    Ship(int initialPosition){
        position = new Vector2(initialPosition, 10);
        this.initialPosition = initialPosition;
        state = State.IDLE;
        stateTime = 0;

        weapon = new Weapon();
    }


    void setFrame(Assets assets){
        switch (state){
            case IDLE:
                frame = assets.naveidle.getKeyFrame(stateTime, true);
                break;
            case LEFT:
                frame = assets.naveleft.getKeyFrame(stateTime, true);
                break;
            case RIGHT:
                frame = assets.naveright.getKeyFrame(stateTime, true);
                break;
            case SHOOT:
                frame = assets.naveshoot.getKeyFrame(stateTime, true);
                break;
            case DYING:
                frame = assets.navedie.getKeyFrame(stateTime);
                break;
            default:
                frame = assets.naveidle.getKeyFrame(stateTime, true);
                break;
        }
    }

    void render(SpriteBatch batch){
        batch.draw(frame, position.x, position.y);

        weapon.render(batch);
    }

    public void update(float delta, Assets assets) {
        stateTime += delta;

        if(Controls.isLeftPressed() && state != State.DYING){
            moveLeft();
        } else if(Controls.isRightPressed() && state != State.DYING){
            moveRight();
        } else if(Controls.isUpPressed() && state != State.DYING){
            moveUp();
        } else if(Controls.isDownPressed() && state != State.DYING){
            moveDown();
        } else if (state == State.DYING){
            if(assets.navedie.isAnimationFinished(stateTime)){
                pausa = true;
            }
        } else {
            idle();
        }

        if(Controls.isShootPressed()) {
            shoot();
            assets.shootSound.play();
        }


        setFrame(assets);

        weapon.update(delta, assets);
    }

    void idle(){
        state = State.IDLE;
    }

    void moveLeft(){
        position.x -= speed;
        state = State.LEFT;
    }

    void moveRight(){
        position.x += speed;
        state = State.RIGHT;
    }

    void moveDown() {
        position.y -= speed;
        state = State.IDLE;
    }

    void moveUp() {
        position.y += speed;
        state = State.IDLE;
    }

    void shoot(){
        state = State.SHOOT;
        weapon.shoot(position.x +16,position.y+frame.getRegionHeight());
    }


    public void damage() {
        position.y -= frame.getRegionHeight();
        stateTime = 0;
        state = State.DYING;
    }

    public void revive(){
        position.x = initialPosition;
        position.y = 10;
        idle();
        pausa = false;
    }
}