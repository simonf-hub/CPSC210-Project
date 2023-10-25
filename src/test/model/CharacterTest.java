package model;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class CharacterTest {
    private static int OPERATION_BOMB_LIGHTING_RANGE = 20;

    @Test
    public void levelUpCounterTest() {
        Character leo = new Character(10,10);
        leo.addCounter();
        leo.addCounter();
        leo.addCounter();
        leo.addCounter();
        leo.checkCharacterLevelUp();
        assertEquals(2,leo.getLevel());
        leo.addCounter();
        leo.addCounter();
        leo.checkCharacterLevelUp();
        assertEquals(3, leo.getLevel());
    }

    @Test
    public void movementTest() {
        Character leo = new Character(10,10);
        leo.moveUp();
        assertEquals(10 - leo.getSpeed(), leo.getCharacterY());
        assertEquals(10, leo.getCharacterX());
        leo.moveDown();
        assertEquals(10, leo.getCharacterX());
        assertEquals(10, leo.getCharacterY());
        leo.moveLeft();
        assertEquals(10 - leo.getSpeed(), leo.getCharacterX());
        assertEquals(10, leo.getCharacterY());
        leo.moveRight();
        assertEquals(10, leo.getCharacterX());
        assertEquals(10, leo.getCharacterY());
    }

    @Test
    public void characterCheckBoundaryTest() {
        Character leo = new Character(10,11);
        leo.checkBoundary(9,9);
        assertEquals(9, leo.getCharacterX());
        assertEquals(9, leo.getCharacterY());

        Character mary = new Character(-1,-2);
        mary.checkBoundary(2,2);
        assertEquals(0, mary.getCharacterY());
        assertEquals(0, mary.getCharacterX());
    }

    @Test
    public void detectBombsTest() {
        Character newCharacter = new Character(10,10);

        List<Bomb> someBombs = new ArrayList<>();
        Bomb newBomb = new Bomb(10 + OPERATION_BOMB_LIGHTING_RANGE - 1,
                10 - OPERATION_BOMB_LIGHTING_RANGE + 1);
        someBombs.add(newBomb);


        List<Bomb> returnBomb = newCharacter.detectBomb(someBombs);
        assertEquals(1, returnBomb.size());
    }
}