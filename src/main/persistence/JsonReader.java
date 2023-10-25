package persistence;

import model.Bomb;
import model.Character;
import model.Game;
import model.Monster;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class JsonReader {
    private String source;

    //EFFECTS: construct reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    //EFFECTS: reads game from file and returns it;
    //throws IOException if an error occurs reading data from file
    public Game read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseGame(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    // CITE: copy from example file
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    //EFFECTS: parses game from JSON object and return the game
    private Game parseGame(JSONObject jsonObject) {
        int maxX = jsonObject.getInt("maxX");
        int maxY = jsonObject.getInt("maxY");
        Game lastGame = new Game(maxX, maxY);
        restoreData(lastGame, jsonObject);
        return lastGame;
    }

    //EFFECTS: restore the character, monsters and bombs that was in the last game
    private void restoreData(Game game, JSONObject jsonObject) {
        //restore Character
        int characterX = jsonObject.getInt("characterX");
        int characterY = jsonObject.getInt("characterY");
        Character player = new Character(characterX, characterY);
        int health = jsonObject.getInt("health");
        int counter = jsonObject.getInt("counter");
        int level = jsonObject.getInt("level");
        player.setLevel(level);
        player.setHealth(health);
        player.setCounter(counter);
        game.setCharacter(player);

        int score = jsonObject.getInt("score");
        game.setScore(score);


        readMonsters(game, jsonObject);
        readBombs(game, jsonObject);

    }

    //EFFECTS: read each monster
    private Monster readMonster(JSONObject jsonObject) {
        int monsterX = jsonObject.getInt("monsterX");
        int monsterY = jsonObject.getInt("monsterY");
        Monster m = new Monster(monsterX,monsterY);
        return m;
    }

    //EFFECTS: read monsters
    private void readMonsters(Game game, JSONObject jsonObject) {
        JSONArray storedMonsters = jsonObject.getJSONArray("monsters");
        ArrayList<Monster> resumeMonsters = new ArrayList<>();

        for (Object m : storedMonsters) {
            JSONObject nextMonster = (JSONObject) m;
            Monster lastM = readMonster(nextMonster);
            resumeMonsters.add(lastM);
        }

        game.setMonsters(resumeMonsters);
    }

    private void readBombs(Game game, JSONObject jsonObject) {
        JSONArray storedBombs = jsonObject.getJSONArray("bombs");
        ArrayList<Bomb> resumeBombs = new ArrayList<>();

        for (Object b : storedBombs) {
            JSONObject nextBomb = (JSONObject) b;
            Bomb lastB = readBomb(nextBomb);
            resumeBombs.add(lastB);
        }

        game.setBombs(resumeBombs);
    }

    //EFFECTS: read each bomb
    private Bomb readBomb(JSONObject jsonObject) {
        int bombX = jsonObject.getInt("bombX");
        int bombY = jsonObject.getInt("bombY");
        Bomb b = new Bomb(bombX,bombY);
        return b;
    }
}
