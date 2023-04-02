package asgn4;

import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {
    // Animation speed per-tick
    private static final int BUS_SPEED = 1;

    // Interval between each ticks
    private static final int ANIMATION_INTERVAL = 50/3;

    private JPanel panelMain;
    private JPanel panelInput;
    private BusPanel panelBus;

    private JTextField txtLength;
    private JTextField txtNumWheels;
    private JButton btnUpdate;
    private JButton btnPlayPause;

    private Timer animationTimer;

    private boolean isPlaying;
    private int direction;

    private int busLength;
    private int numWheels;
    private int busCenter;

    public MainFrame() {
        super("School Bus Animation");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setResizable(false);
        this.setSize(800, 600);

        this.setLocationRelativeTo(null);

        // Set bus center
        this.busCenter = 0;

        // Create and add components
        this.createComponents();
        this.addComponents();

        this.busLength = panelBus.getLength();
        this.numWheels = panelBus.getNumWheels();
        this.isPlaying = false;
        this.direction = 1;

        this.animationTimer = new Timer(ANIMATION_INTERVAL, e -> renderBus());
    }

    private void createComponents() {
        // Create the panels
        panelMain = new JPanel(new BorderLayout());
        panelInput = new JPanel(new GridLayout(2, 3));
        panelBus = new BusPanel(this.busCenter);

        // Create text fields
        txtLength = new JTextField(
            String.format("%d", panelBus.getLength())
        );
        txtNumWheels = new JTextField(
            String.format("%d", panelBus.getNumWheels())
        );

        // Create buttons
        btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(e -> updateBus());

        btnPlayPause = new JButton("Play");
        btnPlayPause.addActionListener(e -> playBus());
    }

    private void addComponents() {
        // Input panel row 1
        panelInput.add(new JLabel("Length:"));
        panelInput.add(txtLength);
        panelInput.add(btnUpdate);

        // Input panel row 2
        panelInput.add(new JLabel("Wheels:"));
        panelInput.add(txtNumWheels);
        panelInput.add(btnPlayPause);

        panelMain.add(panelInput, BorderLayout.NORTH);
        panelMain.add(panelBus, BorderLayout.CENTER);

        this.add(panelMain);
    }

    private void updateBus() {
        try {
            int maxLength = this.getWidth() - 100;

            int length = Integer.parseInt(txtLength.getText());
            int wheels = Integer.parseInt(txtNumWheels.getText());

            // Check length
            if (length < BusPanel.MIN_BUS_LENGTH) {
                JOptionPane.showMessageDialog(
                    this,
                    String.format("Bus is too short (< %d)", BusPanel.MIN_BUS_LENGTH)
                );

                return;
            } else if (length > this.getWidth() - 100) {
                JOptionPane.showMessageDialog(
                    this,
                    String.format("Bus is too long (> %d)", maxLength)
                );

                return;
            }

            // Check wheels
            if (wheels < BusPanel.MIN_NUM_WHEELS) {
                JOptionPane.showMessageDialog(
                    this,
                    "Not enough wheels"
                );

                return;
            }

            // Update and re-paint the bus
            this.busLength = length;
            this.numWheels = wheels;

            // only updates numerical values, does not call super
            panelBus.update(busLength, numWheels, busCenter);
            panelBus.repaint();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                this,
                "Length and wheel must be integers",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void playBus() {
        if (isPlaying) {
            this.isPlaying = false;
            this.animationTimer.stop();
            this.btnPlayPause.setText("Play");
        } else {
            this.isPlaying = true;
            this.animationTimer.start();
            this.btnPlayPause.setText("Pause");
        }
    }

    private void renderBus() {
        if (!isPlaying) return;

        int drawableEdge = direction * panelBus.getDrawableWidth() / 2;
        int busEdge = busCenter + direction* busLength/2;

        if (direction == 1 && busEdge >= drawableEdge) {
            direction = -1;
        } else if (direction == -1 && busEdge <= drawableEdge) {
            direction = 1;
        }

        this.busCenter += direction * BUS_SPEED;
        this.panelBus.update(busLength, numWheels, busCenter);
        this.panelBus.repaint();
    }
}
