package asgn1;

// public final class Math {
public class MyMath {
    public static int max(int x, int y) {
        if (x > y) return x;
        else return y;
    }

    public static int min(int x, int y) {
        if (x < y) return x;
        else return y;
    }

    // Greatest common divisor of two integers using Euclidean Algorithm
    public static int gcd(int x, int y) {
        if (x < y) {
            int tmp = x;
            x = y;
            y = tmp;
        }

        int r = x % y;
        while (r != 0) {
            x = y;
            y = r;
            r = x % y;
        }

        return y;
    }

    public static int gcd(int x, int y, int z) {
        return gcd(gcd(x, y), z);
    }
}
