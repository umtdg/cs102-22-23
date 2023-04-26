package asgn5;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MainFrame extends JFrame {
    public static final String FMT_TITLE = "Color Game - Score: %d";

    GamePanel gamePanel;
    int score;

    public MainFrame(double scale, int depth) {
        super(String.format(FMT_TITLE, 0));
        score = 0;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth() * scale;
        double height = screenSize.getHeight() * scale;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setMinimumSize(new Dimension((int)width, (int)height));
        setLocationRelativeTo(null);
        setUndecorated(false);

        // add a game panel
        gamePanel = new GamePanel(depth, this);
        add(gamePanel);

        pack();
    }

    void checkGameOver() {
        if (gamePanel.hasSameColors()) {
            JOptionPane.showMessageDialog(
                this,
                String.format("Score: %d", score),
                "Game over!",
                JOptionPane.NO_OPTION
            );

            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }

    @Override
    public void update(Graphics g) {
        super.update(g);
    }
}
