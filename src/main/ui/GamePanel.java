package ui;

import model.Bomb;
import model.Character;
import model.Game;
import model.Monster;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GamePanel extends JPanel {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final Color CHARACTER_COLOR = Color.GREEN;
    public static final Color MONSTER_COLOR = Color.RED;
    public static final Color BOMB_COLOR = Color.YELLOW;
    public static final Color BACKGROUND_COLOR = Color.GRAY;
    private static final String OVER = "Game Over!";
    private static final String REPLAY = "R to replay";
    private static final String CONTINUE = "ESC to continue the current game";
    private static final String RESUME = "S to resume the last saved game";
    private static final String SAVE = "A to save the current game";

    private Game game;

    public GamePanel(Game g) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(BACKGROUND_COLOR);
        this.game = g;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);


        drawGame(g);

        if (game.getIsOver()) {
            gameOver(g);
        }

        if (game.getIsPause()) {
            pauseGame(g);
        }

    }

    private void drawGame(Graphics g) {
        drawCharacter(g);
        drawMonsters(g);
        drawBombs(g);
    }

    private void drawCharacter(Graphics g) {
        Character c = game.getCharacter();
        Color saveCol = g.getColor();
        g.setColor(CHARACTER_COLOR);
        g.fillRect(c.getCharacterX() - 5, c.getCharacterY() - 8, 10, 16);
        g.setColor(saveCol);
    }

    private void drawMonsters(Graphics g) {
        List<Monster> ms = game.getMonsters();
        for (Monster each: ms) {
            drawMonster(g, each);
        }
    }

    private void drawMonster(Graphics g, Monster m) {
        Color saveCol = g.getColor();
        g.setColor(MONSTER_COLOR);
        g.fillRect(m.getMonsterX() - 3, m.getMonsterY() - 4, 6, 8);
        g.setColor(saveCol);
    }

    private void drawBombs(Graphics g) {
        List<Bomb> bs = game.getBombs();
        for (Bomb each: bs) {
            drawBomb(g, each);
        }
    }

    private void drawBomb(Graphics g, Bomb b) {
        Color saveCol = g.getColor();
        g.setColor(BOMB_COLOR);
        g.fillOval(b.getBombX() - 2, b.getBombY() - 2, 4, 4);
        g.setColor(saveCol);
    }

    private void gameOver(Graphics g) {
        Color saved = g.getColor();
        g.setColor(new Color(0, 0, 0));
        g.setFont(new Font("Arial", 20, 20));
        FontMetrics fm = g.getFontMetrics();
        centreString(OVER, g, fm, game.getMaxY() / 2);
        centreString(REPLAY, g, fm, game.getMaxY() / 2 + 50);
        g.setColor(saved);
    }

    private void pauseGame(Graphics g) {
        Color saved = g.getColor();
        g.setColor(new Color(0, 0, 0));
        g.setFont(new Font("Arial", 20, 20));
        FontMetrics fm = g.getFontMetrics();
        centreString(CONTINUE, g, fm, game.getMaxY() / 2 - 50);
        centreString(RESUME, g, fm, game.getMaxY() / 2);
        centreString(SAVE, g, fm, game.getMaxY() / 2 + 50);
        centreString(REPLAY, g, fm, game.getMaxY() / 2 + 100);
        g.setColor(saved);
    }

    // Centres a string on the screen
    // modifies: g
    // effects:  centres the string str horizontally onto g at vertical position yPos
    private void centreString(String str, Graphics g, FontMetrics fm, int posY) {
        int width = fm.stringWidth(str);
        g.drawString(str, (game.getMaxX() - width) / 2, posY);
    }
}
