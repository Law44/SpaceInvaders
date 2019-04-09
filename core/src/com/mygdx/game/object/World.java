package com.mygdx.game.object;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.Assets;
import com.mygdx.game.Timer;

public class World {
    Space space;
    Ship ship;
    AlienArmy alienArmy;
    BitmapFont font;
    ShipLife shipLife;
    Timer pauseTimer;
    float pause = 3;
    int lifes = 3;
    int score;
    enum StateGame{
        FINISHED,ONGOING,PAUSED
    }
    StateGame stateGame;

    int WORLD_WIDTH, WORLD_HEIGHT;
    private Alien alienMovingDown;

    public World(int WORLD_WIDTH, int WORLD_HEIGHT){
        this.WORLD_WIDTH = WORLD_WIDTH;
        this.WORLD_HEIGHT = WORLD_HEIGHT;


        space = new Space();
        shipLife = new ShipLife(WORLD_WIDTH, WORLD_HEIGHT);
        ship = new Ship(WORLD_WIDTH/2);
        alienArmy = new AlienArmy(WORLD_WIDTH, WORLD_HEIGHT);
        font = new BitmapFont();
        stateGame = StateGame.ONGOING;
        pauseTimer = new Timer(pause);
    }

    public void render(float delta, SpriteBatch batch, Assets assets){
        if (lifes > 0) {

            update(delta, assets);

            batch.begin();
            space.render(batch);
            ship.render(batch);
            if (stateGame == StateGame.ONGOING) alienArmy.render(batch);
            font.draw(batch, "SCORE: " + score, 20, WORLD_HEIGHT - 5);
            shipLife.render(batch);
            if (stateGame == StateGame.FINISHED)
                font.draw(batch, "YOU WIN \n" + score, WORLD_WIDTH / 2 - 40, WORLD_HEIGHT / 2);
        }
        else {
            update(delta,assets);
            batch.begin();
            space.render(batch);
            ship.render(batch);
            alienArmy.render(batch);
            if (ship.isRespawning()) {
                font.draw(batch, "YOU LOSE", WORLD_WIDTH / 2 - 30, WORLD_HEIGHT / 2);
            }
        }
        batch.end();
    }

    public void update(float delta, Assets assets){
        if (!ship.isRespawning()) {

            space.update(delta, assets);
            ship.update(delta, assets, WORLD_WIDTH);
            if (stateGame == StateGame.ONGOING) alienArmy.update(delta, assets);
            shipLife.update(lifes, delta, assets);
            checkCollisions(assets);
            checkStatusGame();
        }
        else {
            pauseTimer.update(delta);
            if (pauseTimer.check()){
                ship.revive();
            }
        }
    }

    private void checkStatusGame(){
        checkAliensNumber();
        checkGetBonus();
        checkBonusinWorld();
    }



    private void checkCollisions(Assets assets) {
        checkNaveInWorld();
        checkShootsInWorld();
        checkShootsToAlien(assets);
        checkShootsToShip();
        checkAlienInWorld();
        checkAlienCollisionShip();
    }

    private void checkAlienCollisionShip() {
        if (alienMovingDown != null){
            Rectangle shipRectangle = new Rectangle(ship.position.x, ship.position.y, ship.frame.getRegionWidth(), ship.frame.getRegionHeight());
            Rectangle alienRectangle = new Rectangle(alienMovingDown.position.x, alienMovingDown.position.y, alienMovingDown.frame.getRegionWidth(), alienMovingDown.frame.getRegionHeight());
            if (Intersector.overlaps(alienRectangle, shipRectangle)) {
                ship.damage();
                lifes--;
                alienMovingDown.kill();
                alienMovingDown = null;
            }
        }
    }

    private void checkAliensNumber() {
        if (alienArmy.aliens.size == 0){
            ship.state = Ship.State.WINNER;
            stateGame = StateGame.FINISHED;
        }
    }

    private void checkAlienInWorld() {
        for(Alien alien : alienArmy.aliens){
            if (alien.state == Alien.State.MOVINGDOWN || alien.state == Alien.State.FORMING){
                alienMovingDown = alien;
                if (alien.position.y<0){
                    alien.position.y = WORLD_HEIGHT;
                    alien.state = Alien.State.FORMING;
                }
                if (alien.state == Alien.State.FORMING) {
                    if (alien.position.y > WORLD_HEIGHT - 40 - alien.fila * 12 && alien.position.y < WORLD_HEIGHT - 20 - alien.fila * 12) {
                        alien.position.y = WORLD_HEIGHT - 30 - alien.fila * 12;
                        alien.state = Alien.State.LIVE;
                        alienMovingDown = null;
                    }
                }
            }
        }
    }

    private void checkShootsToShip() {
        Rectangle shipRectangle = new Rectangle(ship.position.x, ship.position.y, ship.frame.getRegionWidth(), ship.frame.getRegionHeight());

        for(AlienShoot shoot: alienArmy.shoots){
            Rectangle shootRectangle = new Rectangle(shoot.position.x, shoot.position.y, shoot.frame.getRegionWidth(), shoot.frame.getRegionHeight());

            if (Intersector.overlaps(shootRectangle, shipRectangle)) {
                ship.damage();
                lifes--;
                shoot.remove();
                score = 0;
            }
        }
    }

    private void checkShootsToAlien(Assets assets) {
        for(Shoot shoot: ship.weapon.shoots){
            Rectangle shootRectangle = new Rectangle(shoot.position.x, shoot.position.y, shoot.frame.getRegionWidth(), shoot.frame.getRegionHeight());
            for(Alien alien: alienArmy.aliens){
                if(alien.isAlive()) {
                    Rectangle alienRectangle = new Rectangle(alien.position.x, alien.position.y, alien.frame.getRegionWidth(), alien.frame.getRegionHeight());

                    if (Intersector.overlaps(shootRectangle, alienRectangle)) {
                        alien.kill();
                        shoot.remove();
                        assets.aliendieSound.play();
                        score+= 100;
                    }
                }
            }
        }
    }

    private void checkGetBonus() {
        Rectangle shipRectangle = new Rectangle(ship.position.x, ship.position.y, ship.frame.getRegionWidth(), ship.frame.getRegionHeight());

        for (Alien alien : alienArmy.aliens) {
            if (alien.bonus.isStart()) {
                Rectangle bonusRectangle = new Rectangle(alien.bonus.position.x, alien.bonus.position.y, alien.bonus.frame.getRegionWidth(), alien.bonus.frame.getRegionHeight());
                if (Intersector.overlaps(bonusRectangle, shipRectangle)) {
                    alien.bonus.getBonus();
                    lifes++;
                }
            }
        }
    }

    private void checkBonusinWorld(){
        for (Alien alien : alienArmy.aliens) {
            if (alien.bonus.isStart()) {
                if (alien.bonus.position.y < 0) {
                    alien.bonus.deleteBonus();
                }
            }
        }
    }

    private void checkShootsInWorld() {
        for(Shoot shoot: ship.weapon.shoots){
            if(shoot.position.y > WORLD_HEIGHT){
                shoot.remove();
            }
        }

        for(AlienShoot shoot: alienArmy.shoots){
            if(shoot.position.y < 0 || ship.isRespawning()){
                shoot.remove();
            }
        }
    }

    private void checkNaveInWorld() {
        if (stateGame == StateGame.ONGOING) {
            if (ship.position.x > WORLD_WIDTH - 32) {
                ship.position.x = WORLD_WIDTH - 32;
            } else if (ship.position.x < 0) {
                ship.position.x = 0;
            }
            if (ship.position.y > WORLD_HEIGHT / 4 - 16) {
                ship.position.y = WORLD_HEIGHT / 4 - 16;
            } else if (ship.position.y < 0) {
                ship.position.y = 0;
            }
        }
    }
}