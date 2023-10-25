package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.Random;

public class Monster implements Writable {
    private static final Random RND = new Random();
    private static int randomX = 1;
    private static int randomY = 1;
    private int monsterX;
    private int monsterY;

    public Monster(int x, int y) {
        monsterX = x;
        monsterY = y;
    }

    //EFFECTS: indicate the next random move of the monster, check the boundary make sure it's not out of the screen
    public void randomMove(int maxX, int maxY) {
        monsterX = monsterX + RND.nextInt(2 * randomX + 1) - randomX;
        monsterY = monsterY + RND.nextInt(2 * randomY + 1) - randomY;

        checkBoundary(maxX, maxY);
    }

    //EFFECTS: check the boundary by 0 and maxX and maxY, if outOf boundary, pull back to the edge.
    public void checkBoundary(int maxX, int maxY) {
        if (monsterX < 0) {
            monsterX = 0;
        }

        if (monsterX > maxX) {
            monsterX = maxX;
        }

        if (monsterY < 0) {
            monsterY = 0;
        }

        if (monsterY > maxY) {
            monsterY = maxY;
        }
    }

    //getters
    public int getMonsterX() {
        return monsterX;
    }

    public int getMonsterY() {
        return  monsterY;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("monsterX", monsterX);
        json.put("monsterY", monsterY);
        return json;
    }
}
