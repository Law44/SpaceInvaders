package com.mygdx.game.object;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.Assets;
import com.mygdx.game.Timer;

import java.util.Random;

public class AlienArmy {

    int x, y, maxX;

    float speedX = 8f;
    float speedY = 13f;

    Array<Alien> aliens;
    Array<AlienShoot> shoots;

    Timer moveTimer, shootTimer,alienMoveDown;
    Random random = new Random();

    AlienArmy(int WORLD_WIDTH, int WORLD_HEIGHT){

        this.x = 0;
        this.y = WORLD_HEIGHT-30;
        this.maxX = 60;

        aliens = new Array<Alien>();
        shoots = new Array<AlienShoot>();

        moveTimer = new Timer(0.8f);
        shootTimer = new Timer(random.nextFloat()%5+1);
        alienMoveDown = new Timer(5f);

        positionAliens();
    }


    void render(SpriteBatch batch){
        for(Alien alien: aliens) {
            alien.render(batch);
        }

        for (AlienShoot shoot: shoots) {
            shoot.render(batch);
        }
    }

    public void update(float delta, Assets assets) {
        moveTimer.update(delta);
        shootTimer.update(delta);
        alienMoveDown.update(delta);

        move();
        shoot(assets);
        alienGoDown();

        for(Alien alien: aliens) {
            alien.update(delta, assets);
        }

        for(AlienShoot shoot: shoots){
            shoot.update(delta, assets);
        }


        removeDeadAliens();
        removeShoots();
    }

    void alienGoDown() {
        if (alienMoveDown.check()){
            aliens.get(random.nextInt(aliens.size)).state = Alien.State.MOVINGDOWN;
        }
    }


    void positionAliens(){
        for (int i = 0; i < 5; i++) {  // fila
            for (int j = 0; j < 11; j++) {  // columna
                aliens.add(new Alien(j*30 + 10, y - i*12,i));
            }
        }

    }


    void move() {
        if (moveTimer.check()){
            x += speedX;

            if(x > maxX){
                x = maxX;
                speedX *= -1;
            } else if(x < 0){
                x = 0;
                speedX *= -1;
            }

            for (Alien alien : aliens) {
                if (alien.state == Alien.State.MOVINGDOWN || alien.state == Alien.State.FORMING){
                    alienMoveDown.reset();
                    alien.position.y -= speedY;
                }
                alien.position.x += speedX;
            }
        }
    }

    void shoot(Assets assets){
        if(shootTimer.check()){
            int alienNum = random.nextInt(aliens.size);

            Alien alien = aliens.get(alienNum);

            shoots.add(new AlienShoot(new Vector2(alien.position)));

            assets.alienSound.play();

            shootTimer.set(random.nextFloat()%5+1);
        }
    }

    private void removeDeadAliens() {
        Array<Alien> aliensToRemove = new Array<Alien>();
        for(Alien alien: aliens){
            if(alien.state == Alien.State.DEAD){
                aliensToRemove.add(alien);
            }
        }

        for (Alien alien: aliensToRemove){
            aliens.removeValue(alien, true);
        }
    }
    public void removeShoots(){
        Array<AlienShoot> shootsToRemove = new Array<AlienShoot>();
        for(AlienShoot shoot:shoots){
            if(shoot.state == AlienShoot.State.TO_REMOVE){
                shootsToRemove.add(shoot);
            }
        }

        for (AlienShoot shoot: shootsToRemove){
            shoots.removeValue(shoot, true);
        }
    }

}