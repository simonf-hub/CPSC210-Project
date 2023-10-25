package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.JsonReader;
import persistence.JsonWriter;
import persistence.Writable;
import ui.JsonAction;

import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game implements Writable {
    private static final Random RND = new Random();
    public static final int MONSTER_BORN_PERIOD = 250;
    public static final int BOMB_OCCUR_PERIOD = 100;


    private ArrayList<Bomb> bombs;
    private ArrayList<Monster> monsters;
    private Character character;
    private boolean isOver;
    private boolean pauseBottom;
    //number of monster being killed
    private int score;
    private int maxX;
    private int maxY;
    private JsonAction ja;


    //EFFECTS: Constructs the game, create monsters and a new character
    public Game(int x, int y) {
        maxX = x;
        maxY = y;
        Monster initial = new Monster(maxX / 4, maxY / 4);
        monsters = new ArrayList<>();
        bombs = new ArrayList<>();
        monsters.add(initial);
        character = new Character(maxX / 2, maxY / 2);

        pauseBottom = false;
        isOver = false;
        score = 0;
        ja = new JsonAction(this);
    }

    //update method of the game in every tick
    public void update() {
        newBombOccur();
        newMonsterBorn();
        checkCollision();
        checkBombExplosion();
        checkGameOver();
        character.checkCharacterLevelUp();
        character.checkBoundary(maxX, maxY);
        moveMonsters();
    }

    //EFFECTS: add new bomb into bombs by BOMB_OCCUR_PERIOD
    public void newBombOccur() {
        if (RND.nextInt(BOMB_OCCUR_PERIOD) < 1) {
            Bomb b = new Bomb(RND.nextInt(maxX), RND.nextInt(maxY));
            bombs.add(b);
        }
    }

    //EFFECTS: check whether Character collide with the bomb, if yes, remove the bomb and check whether there is
    //         monster in the range of the bomb;
    //         if no, do nothing
    public void checkBombExplosion() {
        int characterX = character.getCharacterX();
        int characterY = character.getCharacterY();
        //Note: using toRemoveList to avoid concurrentModificationException
        ArrayList<Bomb> toRemoveBomb = new ArrayList<>();
        ArrayList<Monster> toRemoveMonster = new ArrayList<>();
        //check very bomb in bombs
        for (Bomb b : bombs) {
            int bombX = b.getBombX();
            int bombY = b.getBombY();
            //check whether character collide with the bomb, if so, remove the bomb
            if (characterX == bombX && characterY == bombY) {
                toRemoveBomb.add(b);
                //check for each monster may be in the bomb range
                for (Monster m : monsters) {
                    //check for each efficient blocks of the bomb
                    if (b.monsterInValidRange(m)) {
                        toRemoveMonster.add(m);
                        EventLog.getInstance().logEvent(new Event("You killed a monster at: " + m.getMonsterX()
                                + "," + m.getMonsterY()));
                        score++;
                        character.addCounter();
                    }
                }
            }
        }
        bombs.removeAll(toRemoveBomb);
        monsters.removeAll(toRemoveMonster);
    }

    //MODIFIES: this
    //EFFECTS: clear the bombs that returns by character.detectBomb and kill the monsters within the efficientBlocks
    //         of the corresponding bombs
    public void lightBomb() {
        List<Bomb> returnBombs = character.detectBomb(bombs);
        ArrayList<Monster> toRemoveMonster = new ArrayList<>();

        for (Bomb each : returnBombs) {
            toRemoveMonster.clear();
            for (Monster m : monsters) {
                //check for each efficient blocks of the bomb
                if (each.monsterInValidRange(m)) {
                    toRemoveMonster.add(m);
                    EventLog.getInstance().logEvent(new Event("You killed a monster at: " + m.getMonsterX()
                            + "," + m.getMonsterY()));
                    score++;
                    character.addCounter();
                    EventLog.getInstance().logEvent(new Event("Your score is: " + score));
                }
            }
            monsters.removeAll(toRemoveMonster);
        }

        bombs.removeAll(returnBombs);
    }

    //EFFECTS: add new monster into monsters by MONSTER_BORN_PERIOD
    public void newMonsterBorn() {
        if (RND.nextInt(MONSTER_BORN_PERIOD) < 1) {
            Monster m = new Monster(RND.nextInt(maxX), RND.nextInt(maxY));
            monsters.add(m);
        }
    }



    //EFFECTS: character's health go down when collide with a monster
    public void checkCollision() {
        for (Monster each : monsters) {
            if (each.getMonsterX() == character.getCharacterX() && each.getMonsterY() == character.getCharacterY()) {
                character.collide();
            }
        }
    }

    //EFFECTS: game over when character's health is (or below) 0
    public void checkGameOver() {
        if (character.getHealth() <= 0) {
            isOver = true;
        }

        if (isOver) {
            monsters.clear();
            bombs.clear();
        }
    }

    //EFFECTS: random movement of each monster
    //         check the boundary for each monster
    public void moveMonsters() {
        for (Monster next : monsters) {
            next.randomMove(maxX, maxY);
        }
    }

    public void keyPressed(int keyCode) {
        if (keyCode == KeyEvent.VK_R && isOver) {
            reSet();
        } else if (keyCode == KeyEvent.VK_X) {
            printLog(EventLog.getInstance());
            System.exit(0);
        } else if (keyCode == KeyEvent.VK_ESCAPE && !pauseBottom) {
            pauseGame();
        } else if (keyCode == KeyEvent.VK_ESCAPE && pauseBottom) {
            continueGame();
        } else if (keyCode == KeyEvent.VK_A && pauseBottom) {
            ja.saveGame();
        } else if (keyCode == KeyEvent.VK_S && pauseBottom) {
            resumeGame();
        } else if (keyCode == KeyEvent.VK_R && pauseBottom) {
            reSet();
        } else {
            characterControl(keyCode);
        }
    }

    //EFFECTS: print all the EventLog
    private void printLog(EventLog el) {
        for (Event each : el) {
            System.out.println(each.toString());
            System.out.println("\n");
        }
    }

    private void pauseGame() {
        pauseBottom = true;
    }

    private void continueGame() {
        pauseBottom = false;
    }

    private void resumeGame() {
        ja.resumeGame();
        Game lastGame = ja.getGame();
        reSet();
        monsters = lastGame.getMonsters();
        bombs = lastGame.getBombs();
        character = lastGame.getCharacter();
        score = lastGame.getScore();
    }


    private void reSet() {
        bombs.clear();
        monsters.clear();
        Monster initial = new Monster(maxX / 4, maxY / 4);
        monsters.add(initial);
        character = new Character(maxX / 2, maxY / 2);

        pauseBottom = false;
        isOver = false;
        score = 0;
    }

    private void characterControl(int keyCode) {
        if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_KP_LEFT) {
            character.moveLeft();
            EventLog.getInstance().logEvent(new Event("Character moved to: " + character.getCharacterX()
                    + "," + character.getCharacterY()));
        } else if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_KP_RIGHT) {
            character.moveRight();
            EventLog.getInstance().logEvent(new Event("Character moved to: " + character.getCharacterX()
                    + "," + character.getCharacterY()));
        } else if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_KP_UP) {
            character.moveUp();
            EventLog.getInstance().logEvent(new Event("Character moved to: " + character.getCharacterX()
                    + "," + character.getCharacterY()));
        } else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_KP_DOWN) {
            character.moveDown();
            EventLog.getInstance().logEvent(new Event("Character moved to: " + character.getCharacterX()
                    + "," + character.getCharacterY()));
        } else if (keyCode == KeyEvent.VK_SPACE) {
            lightBomb();
            EventLog.getInstance().logEvent(new Event("Character lighted the surrounding bombs"));
        }
    }


    //getters

    public boolean getIsOver() {
        return isOver;
    }

    public boolean getIsPause() {
        return pauseBottom;
    }

    public Character getCharacter() {
        return character;
    }

    public ArrayList<Monster> getMonsters() {
        return monsters;
    }

    public ArrayList<Bomb> getBombs() {
        return bombs;
    }

    public int getScore() {
        return score;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("maxX", maxX);
        json.put("maxY", maxY);
        json.put("score", score);
        json.put("level", character.getLevel());
        json.put("characterX", character.getCharacterX());
        json.put("characterY", character.getCharacterY());
        json.put("health", character.getHealth());
        json.put("counter", character.getCounter());
        json.put("monsters", monstersArray());
        json.put("bombs", bombsArray());

        return json;
    }

    //EFFECTS: returns monsters as a jsonArray
    private JSONArray monstersArray() {
        JSONArray jsonArray = new JSONArray();

        for (Monster m : monsters) {
            jsonArray.put(m.toJson());
        }
        return jsonArray;
    }

    //EFFECTS: returns bombs as a jsonArray
    private JSONArray bombsArray() {
        JSONArray jsonArray = new JSONArray();

        for (Bomb b : bombs) {
            jsonArray.put(b.toJson());
        }
        return jsonArray;
    }



    //setters
    public void setCharacter(Character c) {
        character = c;
    }

    public void setMonsters(ArrayList<Monster> ms) {
        monsters = ms;
    }

    public void setBombs(ArrayList<Bomb> bs) {
        bombs = bs;
    }

    public void setScore(int s) {
        score = s;
    }
}
