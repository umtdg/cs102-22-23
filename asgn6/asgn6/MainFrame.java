package asgn6;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class MainFrame extends JFrame implements KeyListener {
    private JPanel mainPanel;
    private JPanel inputPanel;

    private JFileChooser fcImage;
    private JTextField txtFileName;
    private JButton btnOpenImage;

    private JSlider sliderAnimSpeed;
    private JButton btnStartStop;

    private ImageSorter imageSorter;

    private Timer animationTimer;

    public MainFrame() {
        setTitle("Image Sorter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);
        setOpacity(1.0f);
        setUndecorated(false);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        setResizable(false);

        createComponents();
        addComponents();

        pack();
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()== KeyEvent.VK_RIGHT)
            sliderAnimSpeed.setValue(sliderAnimSpeed.getValue() + 10);
        else if (e.getKeyCode()== KeyEvent.VK_LEFT)
            sliderAnimSpeed.setValue(sliderAnimSpeed.getValue() - 10);
        else if (e.getKeyCode() == KeyEvent.VK_P)
            onStartStop();
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}

    private void createComponents() {
        mainPanel = new JPanel(new BorderLayout());
        inputPanel = new JPanel(new GridBagLayout());

        txtFileName = new JTextField();
        txtFileName.setEditable(false);
        txtFileName.setFocusable(false);

        btnOpenImage = new JButton("Open image");
        btnOpenImage.addActionListener(this::onButtonClick);
        btnOpenImage.setFocusable(false);

        sliderAnimSpeed = new JSlider(0, 1000, 200);
        sliderAnimSpeed.setPaintTicks(true);
        sliderAnimSpeed.setPaintLabels(true);
        sliderAnimSpeed.setPaintTrack(true);
        sliderAnimSpeed.setMajorTickSpacing(100);
        sliderAnimSpeed.setMinorTickSpacing(10);
        sliderAnimSpeed.addChangeListener(this::onAnimSpeedChange);
        sliderAnimSpeed.setFocusable(false);

        btnStartStop = new JButton("Start");
        btnStartStop.addActionListener(this::onButtonClick);
        btnStartStop.setFocusable(false);

        fcImage = new JFileChooser();
        imageSorter = new ImageSorter();

        animationTimer = new Timer(sliderAnimSpeed.getValue(), this::onAnimationTick);
    }

    private void addComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = .5;
        inputPanel.add(new JLabel("Image file  "), gbc);

        gbc.gridx = 1;
        gbc.weightx = .5;
        inputPanel.add(txtFileName, gbc);

        gbc.gridx = 2;
        gbc.weightx = .5;
        inputPanel.add(btnOpenImage, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        inputPanel.add(new JLabel("Animation speed (ms)  "), gbc);

        gbc.gridx = 1;
        inputPanel.add(sliderAnimSpeed, gbc);

        gbc.gridx = 2;
        inputPanel.add(btnStartStop, gbc);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(imageSorter, BorderLayout.CENTER);

        this.add(mainPanel);
    }

    private void loadImage() {
        String filePath = fcImage.getSelectedFile().getAbsolutePath();
        txtFileName.setText(filePath);

        try {
            imageSorter.loadImage(filePath);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                this,
                String.format("An error occurred when loading the image: %s", e.getLocalizedMessage()),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }

        btnStartStop.setText("Start");

        validate();
        repaint();
        pack();
    }

    // On clicking any button
    private void onButtonClick(ActionEvent e) {
        if (e.getSource() == btnOpenImage) onOpenImageClick();

        if (e.getSource() == btnStartStop) onStartStop();
    }

    // Open image action
    private void onOpenImageClick() {
        int choice = fcImage.showOpenDialog(this);

        if (choice != JFileChooser.APPROVE_OPTION) return;

        loadImage();
    }

    // Start/stop actions
    private void onStartStop() {
        if (animationTimer.isRunning()) onAnimStop();
        else onAnimStart();
    }

    private void onAnimStart() {
        if (!imageSorter.isLoaded()) {
            JOptionPane.showMessageDialog(this, "Please open an image first");
            return;
        }

        if (imageSorter.isCompleted()) {
            loadImage();
            return;
        }

        animationTimer.start();
        btnStartStop.setText("Stop");
    }

    private void onAnimStop() {
        animationTimer.stop();
        btnStartStop.setText("Start");
    }

    // Timer related events
    private void onAnimSpeedChange(ChangeEvent e) {
        if (e.getSource() != sliderAnimSpeed) return;

        int animSpeed = sliderAnimSpeed.getValue();
        animationTimer.setInitialDelay(animSpeed);
        animationTimer.setDelay(animSpeed);

        if (animationTimer.isRunning()) {
            animationTimer.restart();
        }
    }

    private void onAnimationTick(ActionEvent e) {
        imageSorter.diagonalStep();

        repaint();

        if (imageSorter.isCompleted()) {
            animationTimer.stop();
            btnStartStop.setText("Reload");
        }
    }
}
