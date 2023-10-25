package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MonsterTest {

    @Test
    public void monsterCheckBoundaryTest() {
        Monster bob = new Monster(10,10);
        bob.checkBoundary(9,8);
        assertEquals(9, bob.getMonsterX());
        assertEquals(8, bob.getMonsterY());

        Monster adam = new Monster(-1,-2);
        adam.checkBoundary(10,10);
        assertEquals(0, adam.getMonsterY());
        assertEquals(0, adam.getMonsterX());
    }
}
