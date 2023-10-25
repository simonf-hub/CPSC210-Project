package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.LinkedList;

public class Bomb implements Writable {
    private static final int RANGE = 50;

    private int bombX;
    private int bombY;
    private LinkedList<EfficientBlock> range;

    public Bomb(int x, int y) {
        bombX = x;
        bombY = y;

        range = new LinkedList<>();
        constructEfficientBlock();
    }

    //MODIFIES: this.range
    //EFFECTS: construct the field range which is in the SPEED region
    //         note: SPEED region means bombX +- RANGE, bombY +- RANGE
    public void constructEfficientBlock() {
        for (int initialX = bombX - RANGE; initialX <= bombX + RANGE; initialX++) {
            for (int initialY = bombY - RANGE; initialY <= bombY + RANGE; initialY++) {
                EfficientBlock blockToAdd = new EfficientBlock(initialX, initialY);
                range.add(blockToAdd);
            }
        }
    }

    //EFFECTS: check whether one monster is in the range of the bomb
    public boolean monsterInValidRange(Monster m) {
        boolean validRange = false;
        for (EfficientBlock eb : range) {
            if (m.getMonsterX() == eb.getTheX() && m.getMonsterY() == eb.getTheY()) {
                validRange = true;
            }
        }
        return validRange;
    }


    //getters
    public int getBombX() {
        return bombX;
    }

    public int getBombY() {
        return bombY;
    }

    public LinkedList<EfficientBlock> getRange() {
        return range;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("bombX", bombX);
        json.put("bombY", bombY);
        return json;
    }
}
