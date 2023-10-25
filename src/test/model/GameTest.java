package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    private static int OPERATION_BOMB_LIGHTING_RANGE = 20;

    private Game myGame;
    private ArrayList<Monster> monsters;
    private Character leo;
    private ArrayList<Bomb> bombs;

    @BeforeEach
    public void constructGame() {
        myGame = new Game(20,20);
        leo = myGame.getCharacter(); //position: 10,10
        monsters = myGame.getMonsters();
        bombs = myGame.getBombs();
    }

    @Test
    public void checkBombExplosionTest() {
        Bomb myBomb = new Bomb(10,10);
        Monster bob = new Monster(11,11); //in the range of explosion
        bombs.add(myBomb);
        assertEquals(1, bombs.size());
        monsters.add(bob);
        assertEquals(2, monsters.size()); //the monster and the initial monster make up 2
        myGame.checkBombExplosion();
        assertEquals(0, monsters.size());
        assertEquals(0, bombs.size());
    }

    @Test
    public void checkCollisionTest() {
        Monster bob = new Monster(10,10);
        monsters.add(bob);
        myGame.checkCollision();
        assertEquals(90, leo.getHealth());
    }

    @Test
    public void checkGameOverTest() {
        leo.collide();
        leo.collide();
        leo.collide();
        myGame.checkGameOver();
        assertFalse(myGame.getIsOver());
        leo.collide();
        leo.collide();
        leo.collide();
        leo.collide();
        leo.collide();
        leo.collide();
        leo.collide();
        myGame.checkGameOver();
        assertTrue(myGame.getIsOver());
    }

    @Test
    public void moveMonstersTest() {
        Monster bob = new Monster(10,10);
        monsters.add(bob);
        myGame.moveMonsters();
        int x = bob.getMonsterX();
        int y = bob.getMonsterY();
        assertTrue(x == 7 || x == 8 || x == 9 || x == 10 || x == 11);
        assertTrue(y == 7 || y == 8 || y == 9 || y == 10 || y == 11);
    }

    @Test
    public void keyEventTest() {
        assertEquals(20, myGame.getMaxX());
        assertEquals(20, myGame.getMaxY());

        myGame.keyPressed(KeyEvent.VK_UP);
        assertEquals(10 - leo.getSpeed(), leo.getCharacterY());
        myGame.keyPressed(KeyEvent.VK_DOWN);
        assertEquals(10, leo.getCharacterY());
        myGame.keyPressed(KeyEvent.VK_LEFT);
        assertEquals(10 - leo.getSpeed(), leo.getCharacterX());
        myGame.keyPressed(KeyEvent.VK_RIGHT);
        assertEquals(10, leo.getCharacterX());

        //after GameOver reset a new game
        leo.setHealth(0);
        myGame.checkGameOver();
        assertTrue(myGame.getIsOver());
        myGame.keyPressed(KeyEvent.VK_R);
        assertFalse(myGame.getIsOver());
        myGame.update();

        //test save and resume
        Game record = myGame;
        myGame.keyPressed(KeyEvent.VK_ESCAPE);
        assertTrue(myGame.getIsPause());
        myGame.update();
        myGame.keyPressed(KeyEvent.VK_A);
        myGame.update();
        myGame.keyPressed(KeyEvent.VK_ESCAPE);
        myGame.update();
        myGame.keyPressed(KeyEvent.VK_S);
        myGame.update();
        assertEquals(record, myGame);

        myGame.keyPressed(KeyEvent.VK_ESCAPE);
        myGame.update();
        myGame.keyPressed(KeyEvent.VK_R);
        myGame.update();
        assertEquals(10, myGame.getCharacter().getCharacterX());
    }

    @Test
    public void lightBombTest() {

        Bomb newBomb = new Bomb(10 + OPERATION_BOMB_LIGHTING_RANGE - 1,
                10 - OPERATION_BOMB_LIGHTING_RANGE + 1);
        myGame.getBombs().add(newBomb);


        List<Bomb> returnBomb = myGame.getCharacter().detectBomb(myGame.getBombs());
        assertEquals(1, returnBomb.size());

        assertEquals(1, myGame.getBombs().size());
        myGame.lightBomb();
        assertEquals(0, myGame.getBombs().size());
    }
}
