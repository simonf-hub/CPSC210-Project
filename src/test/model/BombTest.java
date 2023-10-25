package model;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BombTest {
    private static final int RANGE = 50;

    @Test
    public void constructBlocksTest() {
        Bomb dynamite = new Bomb(10,20);
        LinkedList<EfficientBlock> ebs = dynamite.getRange();

        EfficientBlock eb1 = ebs.getFirst();
        EfficientBlock ebf = new EfficientBlock(2,2);
        assertTrue(ebs.contains(eb1));
        assertFalse(ebs.contains(ebf));
    }

    @Test
    public void monsterInValidRangeTest() {
        Monster bob = new Monster(6 - RANGE + 1,6 - RANGE + 1 );
        Monster vivian = new Monster(6 - RANGE - 1,6 - RANGE - 1);
        Bomb dynamite = new Bomb(6,6);
        assertTrue(dynamite.monsterInValidRange(bob));
        assertFalse(dynamite.monsterInValidRange(vivian));
    }
}
