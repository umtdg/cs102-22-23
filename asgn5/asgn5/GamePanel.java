package asgn5;

import java.util.Random;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.*;

public class GamePanel extends JPanel {
    private final int ROW = 2;
    private final int COL = 2;
    private final int NESTED_PANELS = ROW * COL;

    private final Color[] colors = { Color.RED, Color.GREEN, Color.BLUE };
    private final Random random;
    private final GridLayout layout;
    private final GamePanel[] panels;
    private final JButton[] buttons;
    private final int depth;
    private final MainFrame mainFrame; // used for keeping track of the total score

    public GamePanel(int depth, MainFrame mainFrame) {
        // initialize
        this.depth = depth;
        this.mainFrame = mainFrame;

        random = new Random();
        panels = new GamePanel[NESTED_PANELS];
        buttons = new JButton[NESTED_PANELS];

        // set layout to grid layout
        layout = new GridLayout(ROW, COL);
        this.setLayout(layout);

        // create and add components
        createComponents();
    }

    /**
     * Check if all of the buttons has the same color. Irrelevant
     * when depth is greater than zero
     *
     * @return True if all of the buttons has the same color
     */
    boolean hasSameColors()  {
        if (depth > 0) {
            for (int i = 0; i < panels.length; i++) {
                if (!panels[i].hasSameColors()) return false;
            }
        } else {
            Color color = buttons[0].getBackground();
            for (int i = 1; i < buttons.length; i++) {
                if (color != buttons[i].getBackground()) return false;
            }
        }

        return true;
    }

    /**
     * Create either nested GamePanels or buttons depending
     * on depth
     */
    private void createComponents() {
        if (depth > 0) {
            createPanels();
        } else {
            createButtons();
            checkButtons();
        }
    }

    private void createPanels() {
        for (int i = 0; i < NESTED_PANELS; i++) {
            GamePanel panel = new GamePanel(depth - 1, mainFrame);

            panels[i] = panel;
            this.add(panel);
        }

        layout.setHgap(2);
        layout.setVgap(2);
    }

    private void createButtons() {
        ActionListener onClick = e -> {
            mainFrame.score -= 1;
            System.out.printf("Button click, Score: %d\n", mainFrame.score);

            JButton source = (JButton) e.getSource();
            source.setBackground(colors[random.nextInt(3)]);

            if (checkButtons()) {
                mainFrame.setTitle(String.format(MainFrame.FMT_TITLE, mainFrame.score));
                mainFrame.checkGameOver();
            }
        };

        for (int i = 0; i < NESTED_PANELS; i++) {
            JButton button = new JButton();
            button.addActionListener(onClick);
            button.setBackground(colors[random.nextInt(3)]);

            buttons[i] = button;
            this.add(button);
        }
    }

    /**
     * Check if all the buttons have the same color,
     * if so disable them, set their colors to gray
     * and add 10 points
     */
    private boolean checkButtons() {
        if (hasSameColors()) {
            for (int i = 0; i < buttons.length; i++) {
                buttons[i].setEnabled(false);
                buttons[i].setBackground(Color.GRAY);
            }

            mainFrame.score += 10;
            System.out.printf("Matching colors, Score: %d\n", mainFrame.score);

            return true;
        }

        return false;
    }
}
