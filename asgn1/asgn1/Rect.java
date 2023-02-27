package asgn1;

public class Rect {
    private int left;
    private int right;
    private int top;
    private int bottom;
    private int area;

    public Rect(int x, int y, int width, int height) {
        this.left = x;
        this.right = x + width;
        this.bottom = y;
        this.top = y + height;
        this.area = width * height;
    }

    public int area() { return this.area; }

    public int intersectionArea(Rect r) {
        int width = MyMath.max(
            MyMath.min(right, r.right) - Math.max(left, r.left),
            0
        );
        int height = MyMath.max(
            MyMath.min(top, r.top) - Math.max(bottom, r.bottom),
            0
        );

        return width * height;
    }

    public int unionArea(Rect r) {
        return area() + r.area() - intersectionArea(r);
    }
}

