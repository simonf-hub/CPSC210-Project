package persistence;

import model.Bomb;
import model.Game;
import model.Monster;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest {

    @Test
    void testReaderNotExistFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Game game = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyWorkRoom() {
        Game game = new Game(100,100);
        JsonWriter writer = new JsonWriter("./data/testReaderEmptyWorkroom.json");
        try {
            writer.open();
            writer.write(game);
            writer.close();
            JsonReader reader = new JsonReader("./data/testReaderEmptyWorkRoom.json");

            game = reader.read();
            assertEquals(0, game.getScore());
            assertEquals(50, game.getCharacter().getCharacterX());
            assertEquals(50, game.getCharacter().getCharacterY());
            assertEquals(1, game.getCharacter().getLevel());
            assertEquals(100, game.getCharacter().getHealth());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralWorkRoom() {
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
            JsonWriter writer = new JsonWriter("./data/testReaderGeneralWorkroom.json");
            writer.open();
            writer.write(game);
            writer.close();

            JsonReader reader = new JsonReader("./data/testReaderGeneralWorkroom.json");
            Game newGame = new Game(200,200);
            newGame = reader.read();
            assertEquals(1, game.getScore());
            assertEquals(0, game.getBombs().size());
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }
}
