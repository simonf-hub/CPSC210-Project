package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

public class Character implements Writable {
    private static int numKillToLevelUp = 3;
    private static int levelUpBonusHealth = 20;
    private static int SPEED = 5;
    private static int OPERATION_BOMB_LIGHTING_RANGE = 20;

    private int characterX;
    private int characterY;
    private int speed;
    private int health;
    private int level;
    private int counter;

    //EFFECTS: constructor, need x and y as character's born position
    public Character(int x, int y) {
        characterX = x;
        characterY = y;
        speed = SPEED;
        health = 100;
        level = 1;
        counter = 0;
    }

    //MODIFIES: this
    //EFFECTS: for every tick, check whether character should level up by counter
    public void checkCharacterLevelUp() {
        if (counter >= numKillToLevelUp) {
            level++;
            health += levelUpBonusHealth;
            counter -= numKillToLevelUp;
        }
    }

    //EFFECTS: movement of character in four direction by speed
    public void moveUp() {
        characterY -= speed;
    }

    public void moveDown() {
        characterY += speed;
    }

    public void moveRight() {
        characterX += speed;
    }

    public void moveLeft() {
        characterX -= speed;
    }

    //EFFECTS: check character's boundary, make sure it's not out of boundary
    public void checkBoundary(int maxX, int maxY) {
        if (characterX < 0) {
            characterX = 0;
        }

        if (characterX > maxX) {
            characterX = maxX;
        }

        if (characterY < 0) {
            characterY = 0;
        }

        if (characterY > maxY) {
            characterY = maxY;
        }
    }

    //MODIFIES: list of detected bombs
    //EFFECTS: return the list of bomb that is inside the SPEED region of character;
    //         note speed region means characterX +- SPEED, characterY +- SPEED
    public List<Bomb> detectBomb(List<Bomb> bombs) {
        List<Bomb> returnBombs = new ArrayList<>();

        for (Bomb each : bombs) {
            int eachX = each.getBombX();
            int eachY = each.getBombY();

            int boundX = characterX + OPERATION_BOMB_LIGHTING_RANGE;
            int boundY = characterY + OPERATION_BOMB_LIGHTING_RANGE;

            for (int initialX = characterX - OPERATION_BOMB_LIGHTING_RANGE; initialX <= boundX; initialX++) {
                for (int initialY = characterY - OPERATION_BOMB_LIGHTING_RANGE; initialY <= boundY; initialY++) {
                    if (eachX == initialX && eachY == initialY) {
                        returnBombs.add(each);
                    }
                }
            }
        }

        return returnBombs;
    }

    //EFFECTS: add counter by one
    public void addCounter() {
        counter++;
    }

    //EFFECTS: health decrease when collide with monster
    public void collide() {
        health -= 10;
    }

    //getters
    public int getCharacterX() {
        return characterX;
    }

    public int getCharacterY() {
        return characterY;
    }

    public int getHealth() {
        return health;
    }

    public int getLevel() {
        return level;
    }

    public int getCounter() {
        return counter;
    }

    public int getSpeed() {
        return speed;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("characterX", characterX);
        json.put("characterY", characterY);
        json.put("health", health);
        json.put("counter", counter);
        json.put("level", level);
        return json;
    }

    //setters
    public void setLevel(int i) {
        level = i;
    }

    public void setHealth(int i) {
        health = i;
    }

    public void setCounter(int i) {
        counter = i;
    }
}
