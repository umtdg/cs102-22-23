package asgn6;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImageSorter extends JPanel {
    private static final float SCALE_RED = 0.2126f;
    private static final float SCALE_GREEN = 0.7152f;
    private static final float SCALE_BLUE = 0.0722f;

    BufferedImage bufferedImage;
    ImageIcon imageIcon;
    int currentRow;
    int currentColumn;
    int maxRows;
    int maxColumns;
    int horizontalStepColumn;
    int verticalStepRow;

    public ImageSorter() {
        bufferedImage = null;
        imageIcon = null;
        currentRow = 0;
        currentColumn = 0;
        maxRows = 0;
        maxColumns = 0;
        verticalStepRow += 1;
        horizontalStepColumn += 1;
    }

    public boolean isLoaded() {
        return bufferedImage != null;
    }

    public boolean isVerticallyCompleted() {
        return isLoaded()
            && verticalStepRow > maxRows;
    }

    public boolean isHorizontallyCompleted() {
        return isLoaded()
            && horizontalStepColumn > maxColumns;
    }

    public boolean isCompleted() {
        return isVerticallyCompleted() && isHorizontallyCompleted();
    }

    public void loadImage(String fileName) throws IOException {
        bufferedImage = ImageIO.read(new File(fileName));

        imageIcon = new ImageIcon(bufferedImage);
        currentColumn = 0;
        currentRow = 0;
        maxRows = bufferedImage.getHeight() - 1;
        maxColumns = bufferedImage.getWidth() - 1;
    }

    public void displayImage(Graphics g) {
        if (bufferedImage == null) return;

        Graphics2D g2d = (Graphics2D) g.create();
        int x = (this.getWidth() - bufferedImage.getWidth()) / 2;
        int y = (this.getHeight() - bufferedImage.getHeight()) / 2;
        g2d.drawImage(bufferedImage, x, y, this);
        g2d.dispose();
    }

    public void horizontalStep() {
        if (isHorizontallyCompleted()) return;

        for (int j = 0; j < maxColumns - horizontalStepColumn; j++) {
            Color current = new Color(bufferedImage.getRGB(j, currentRow));
            Color next = new Color(bufferedImage.getRGB(j + 1, currentRow));
            swapIfBrighter(
                j, currentRow, current,
                j + 1, currentRow, next
            );
        }

        currentRow += 1;
        if (currentRow >= maxRows) {
            horizontalStepColumn += 1;
            currentRow = 0;
        }
    }

    public void verticalStep() {
        if (isVerticallyCompleted()) return;

        for (int j = 0; j < maxRows - verticalStepRow - 1; j++) {
            Color current = new Color(bufferedImage.getRGB(currentColumn, j));
            Color next = new Color(bufferedImage.getRGB(currentColumn, j + 1));
            swapIfBrighter(
                currentColumn, j, current,
                currentColumn, j + 1, next
            );
        }

        currentColumn += 1;
        if (currentColumn >= maxColumns) {
            verticalStepRow += 1;
            currentColumn = 0;
        }
    }

    public void diagonalStep() {
        horizontalStep();
        verticalStep();
    }

    @Override
    public Dimension getPreferredSize() {
        if (bufferedImage == null) return new Dimension(0, 0);

        return new Dimension(
            bufferedImage.getWidth(),
            bufferedImage.getHeight()
        );
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        displayImage(g);
    }

    private float getBrightness(Color c) {
        return SCALE_RED * c.getRed()
            + SCALE_GREEN * c.getGreen()
            + SCALE_BLUE * c.getBlue();
    }

    private void swapIfBrighter(
        int row1, int col1, Color c1,
        int row2, int col2, Color c2
    ) {
        if (getBrightness(c1) > getBrightness(c2)) return;

        bufferedImage.setRGB(row1, col1, c2.getRGB());
        bufferedImage.setRGB(row2, col2, c1.getRGB());
    }
}
