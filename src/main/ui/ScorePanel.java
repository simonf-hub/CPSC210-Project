package ui;

import model.Game;

import javax.swing.*;
import java.awt.*;

public class ScorePanel extends JPanel {
    private static final String SCORE_TXT = "Score: ";
    private static final String LEVEL_TXT = "Level: ";
    private static final String HEALTH_TXT = "Health: ";
    private static final String BOMB_AMOUNT = "BombsAmount: ";
    private static final String MONSTER_AMOUNT = "MonsterAmount: ";
    private static final int LBL_WIDTH = 200;
    private static final int LBL_HEIGHT = 30;
    private Game game;
    private JLabel scoreLbl;
    private JLabel levelLbl;
    private JLabel healthLbl;
    private JLabel bombLbl;
    private JLabel monsterLbl;

    // Constructs a score panel
    // effects: sets the background colour and draws the initial labels;
    //          updates this with the game whose score is to be displayed
    public ScorePanel(Game g) {
        game = g;
        setBackground(new Color(180, 180, 180));
        scoreLbl = new JLabel(SCORE_TXT + game.getScore());

        levelLbl = new JLabel(LEVEL_TXT + game.getCharacter().getLevel());
        healthLbl = new JLabel(HEALTH_TXT + game.getCharacter().getHealth());
        bombLbl = new JLabel(BOMB_AMOUNT + game.getBombs().size());
        monsterLbl = new JLabel(MONSTER_AMOUNT + game.getMonsters().size());
        add(scoreLbl);
        add(Box.createHorizontalStrut(10));
        add(levelLbl);
        add(Box.createHorizontalStrut(10));
        add(healthLbl);
        add(Box.createHorizontalStrut(10));
        add(bombLbl);
        add(Box.createHorizontalStrut(10));
        add(monsterLbl);
    }

    //Modifies: this
    //Effects: updates score, characterLevel and characterHealth in one tick
    public void update() {
        scoreLbl.setText(SCORE_TXT + game.getScore());
        levelLbl.setText(LEVEL_TXT + game.getCharacter().getLevel());
        healthLbl.setText(HEALTH_TXT + game.getCharacter().getHealth());
        bombLbl.setText(BOMB_AMOUNT + game.getBombs().size());
        monsterLbl.setText(MONSTER_AMOUNT + game.getMonsters().size());
        repaint();
    }
}
