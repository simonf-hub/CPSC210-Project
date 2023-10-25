package persistence;

import model.Bomb;
import model.Game;
import model.Monster;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest {

    @Test
    void testWriterInvalidFile() {
        try {
            Game game = new Game(100, 100);
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterInitialWorkroom() {
        Game game = new Game(100,100);
        JsonWriter writer = new JsonWriter("./data/testWriterEmptyWorkroom.json");
        try {
            writer.open();
            writer.write(game);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyWorkroom.json");
            game = reader.read();
            assertEquals(0, game.getScore());
            assertEquals(50, game.getCharacter().getCharacterX());
            assertEquals(50, game.getCharacter().getCharacterY());
            assertEquals(1, game.getCharacter().getLevel());
            assertEquals(100, game.getCharacter().getHealth());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
        try {
            Game game = new Game(200,200); //character: 100,100
            Bomb bomb = new Bomb(100,100);
            Monster monster = new Monster(101,101);
            ArrayList<Monster> ms = new ArrayList<>();
            ArrayList<Bomb> bs = new ArrayList<>();
            ms.add(monster);
            bs.add(bomb);
            game.setMonsters(ms);
            game.setBombs(bs);
            game.checkBombExplosion();
            //should have score of 1
            assertEquals(1, game.getScore());
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralWorkroom.json");
            writer.open();
            writer.write(game);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralWorkroom.json");
            Game newGame = new Game(200,200);
            newGame = reader.read();
            assertEquals(1, game.getScore());
            assertEquals(0, game.getBombs().size());
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }

    }
}
