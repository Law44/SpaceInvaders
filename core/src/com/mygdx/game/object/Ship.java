package com.mygdx.game.object;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Assets;
import com.mygdx.game.Controls;

public class Ship {

    enum State {
        IDLE, LEFT, RIGHT, SHOOT, WINNER
    }

    Vector2 position;

    State state;
    float stateTime;
    float speed = 5;

    TextureRegion frame;

    Weapon weapon;

    Ship(int initialPosition){
        position = new Vector2(initialPosition, 10);
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
            default:
                frame = assets.naveidle.getKeyFrame(stateTime, true);
                break;
        }
    }

    void render(SpriteBatch batch){
        batch.draw(frame, position.x, position.y);

        weapon.render(batch);
    }

    public void update(float delta, Assets assets, int WORLD_WIDTH) {
        stateTime += delta;
        if (state == State.WINNER){
            nextScreen(WORLD_WIDTH);
        }else if(Controls.isLeftPressed()){
            moveLeft();
        } else if(Controls.isRightPressed()){
            moveRight();
        } else if(Controls.isUpPressed()){
            moveUp();
        } else if(Controls.isDownPressed()){
            moveDown();
        }else {
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
    }

    private void nextScreen(int WORLD_WIDTH) {
        this.position.y += speed/4;
        int limit =frame.getRegionWidth()/2;
        if (this.position.x < WORLD_WIDTH/2- limit || this.position.x > WORLD_WIDTH/2-limit){
            if (this.position.x < WORLD_WIDTH/2- limit){
                this.position.x += speed/2;
            }else {
                this.position.x -= speed/2;
            }
        }
    }
}