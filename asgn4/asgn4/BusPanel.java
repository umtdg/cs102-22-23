package asgn4;

import java.awt.*;

import javax.swing.JPanel;

public class BusPanel extends JPanel {
    // Public constants
    public static final int MIN_BUS_LENGTH = 300;
    public static final int MIN_NUM_WHEELS = 2;
    public static final int MIN_DIAMETER = 20;

    // Private constants

    // Hood of the bus
    private static final int HOOD_WIDTH = 40;
    private static final int HOOD_WIDTH_BIAS = 10; // Used to overlap hoods up to this with wheels

    // Non-variable bus properties
    private static final int BUS_HALF_HEIGHT = 75;
    private static final int BUS_HEIGHT = 2 * BUS_HALF_HEIGHT;
    private static final int BUS_Y = -BUS_HALF_HEIGHT;

    private static final int MAX_WHEEL_DIAMETER = BUS_HALF_HEIGHT;

    private static final int WINDOW_MARGIN = 10;
    private static final int WINDOW_SIZE = 50;
    private static final int WINDOW_Y = BUS_Y + WINDOW_MARGIN;

    private static final int GRASS_Y = BUS_Y + BUS_HEIGHT - (int)(0.75 * BUS_HALF_HEIGHT);

    private static final Color COLOR_BUS = Color.ORANGE;
    private static final Color COLOR_WHEEL = new Color(0x61240F);
    private static final Color COLOR_WINDOW = new Color(0xE0FBFC);
    private static final Color COLOR_GRASS = new Color(0x8AD491);

    // Bus properties
    private int length; // Length of the bus in pixels
    private int numWheels; // Number of wheels on the bus
    private int X; // X coordinate of the bus

    // Wheel properties
    private int wheelDiameter; // Diameter of each wheel
    private int wheelSpacing; // Spacing between each wheel
    private int wheelMargin; // Space to the left (back) of the bus
    private int wheelY; // Y coordinate of each wheel

    // Auxiliary
    private Insets insets;

    public BusPanel() {
        this(MIN_BUS_LENGTH, MIN_NUM_WHEELS, 0);
    }

    public BusPanel(int center) {
        this(MIN_BUS_LENGTH, MIN_NUM_WHEELS, center);
    }

    public BusPanel(int length, int numWheels, int center) {
        super();
        this.setDoubleBuffered(true);

        this.insets = getInsets();

        this.update(length, numWheels, center);
    }

    public int getLength() { return length; }
    public int getNumWheels() { return numWheels; }

    /**
     * Get width of the panel excluding left and right insets
     *
     * @return Returns width of drawable area
     */
    public int getDrawableWidth() {
        return getWidth() - insets.left - insets.right;
    }

    /**
     * Get height of the panel excluding top and bottom insets
     *
     * @return Returns height of drawable area
     */
    public int getDrawableHeight() {
        return getHeight() - insets.top - insets.bottom;
    }

    public void update(int length, int numWheels, int center) {
        // update properties
        this.length = length;
        this.numWheels = numWheels;
        this.X = center - length/2;

        // calculate the wheel radius and spacing
        int wheelAreaLength = length - HOOD_WIDTH + HOOD_WIDTH_BIAS;
        int extraSpace = 0;

        this.wheelDiameter = wheelAreaLength / (numWheels + 2);
        this.wheelMargin = wheelDiameter / 4;
        this.wheelSpacing = 2*(wheelDiameter - wheelMargin) / (numWheels - 1);
        if (wheelDiameter >= MAX_WHEEL_DIAMETER) {
            wheelSpacing += wheelDiameter - MAX_WHEEL_DIAMETER;
            wheelDiameter = MAX_WHEEL_DIAMETER;
        }

        // check if there is extra space, if so add spacing between wheels
        wheelAreaLength = length - HOOD_WIDTH - wheelDiameter;
        extraSpace =
            wheelAreaLength
                - numWheels*(wheelDiameter + wheelSpacing)
                - wheelDiameter;

        if (extraSpace > 0) {
            if (numWheels > 4) wheelSpacing += extraSpace / (numWheels - 1);
            else wheelMargin += extraSpace / 2;
        }

        // update Y coordinate of wheels and windows
        this.wheelY = BUS_Y + BUS_HEIGHT - wheelDiameter/2;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D graphics = (Graphics2D) g.create();

        // Enable anti-aliasing
        graphics.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        // Center the graphics wrt panel's width and height
        int drawableW = getDrawableWidth();
        int drawableY = getDrawableHeight();

        int w = drawableW / 2;
        int h = drawableY / 2;
        graphics.translate(w, h);

        // Draw grass
        graphics.setColor(COLOR_GRASS);

        graphics.fillRect(
            -w, GRASS_Y,
            drawableW, drawableY - GRASS_Y
        );

        // Draw the body of the bus
        graphics.setColor(COLOR_BUS);

        graphics.fillRect(
            this.X, BUS_Y,
            length - HOOD_WIDTH, BUS_HALF_HEIGHT
        );
        graphics.fillRect(
            this.X, BUS_Y + BUS_HALF_HEIGHT,
            length, BUS_HALF_HEIGHT
        );

        // Draw windows
        graphics.setColor(COLOR_WINDOW);
        int windowX = this.X + WINDOW_MARGIN;
        int maxWindowX = length + this.X - HOOD_WIDTH - WINDOW_MARGIN - WINDOW_SIZE;
        while (windowX <= maxWindowX) {
            graphics.fillRect(windowX, WINDOW_Y, WINDOW_SIZE, WINDOW_SIZE);
            windowX += WINDOW_MARGIN + WINDOW_SIZE;
        }

        // Draw the wheels of the bus
        graphics.setColor(COLOR_WHEEL);
        int wheelX;
        for (int i = 0; i < numWheels; i++) {
            wheelX = this.X + wheelMargin + i*wheelDiameter + i*wheelSpacing;
            graphics.fillOval(wheelX, wheelY, wheelDiameter, wheelDiameter);
        }
    }
}
