import java.util.Random;
import java.util.Scanner;

import asgn1.*;

public class Main {
    public static int RAND_MAX = 50;
    public static int RECT_COORD_MAX = 10;
    public static int RECT_WIDTH_MAX = 20;
    public static int RECT_HEIGHT_MAX = 10;

    // Message formats
    public static final String FMT_Q_PRIMES = "Find the prime numbers in [%s, %s].\n\n";
    public static final String FMT_Q_VOLUME =
        "A rectangular prism of dimensions %s, %s, %s is to be filled "
        + "using cube blocks. What is the minimum number of cubes required?\n\n";
    public static final String FMT_Q_UNION_AREA =
        "What is the area of the union of two rectangles R1 and R2, where top left "
        + "corners are (%s, %s), (%s, %s) and sizes are (%s, %s), (%s, %s) respectively?\n\n";


    // Choice 1 - Primes

    // Use Sieve of Eratosthenes
    public static void printPrimesInRange(int min, int max) {
        if (max <= min) return;
        if (max <= 1) return;

        int numberCount = max + 1;
        int count = 0;
        boolean[] isPrime = new boolean[numberCount];

        // consider every number, up to max, prime by default
        for (int i = 0; i < numberCount; i++) { isPrime[i] = true; }

        // check if 2 is in the range
        if (min <= 2 && max >= 2) {
            System.out.print('2');
            count += 1;
        }

        // since only even prime number is 2, look for odd numbers only
        for (int i = 3; i <= max; i += 2) {
            if (!isPrime[i]) continue;

            // if i is in the range, print it
            if (i >= min) {
                if (count > 0) { System.out.print(", "); }
                System.out.print(i);
                count += 1;
            }

            // sieve all multiples of i, skipping even numbers
            // skip i, start with 3i and increase by 2i, this gives:
            // 3i, 5i, 7i, ...
            for (int j = 3*i; j <= max; j += 2*i) { isPrime[j] = false; }
        }
    }

    public static void doPrimes(Scanner stdin) {
        int max, min;

        // if stdin is null, generate random numbers
        if (stdin == null) {
            Random rand = new Random();
            max = rand.nextInt(RAND_MAX + 1);
            min = rand.nextInt(max);

            System.out.printf(FMT_Q_PRIMES, min, max);
        } else {
            System.out.printf(FMT_Q_PRIMES, "min", "max");

            System.out.print("Enter lower and uppder bounds with a space in between: ");
            min = stdin.nextInt();
            max = stdin.nextInt();
            stdin.nextLine(); // discard remaining
        }

        System.out.printf("Prime numbers in range [%d, %d]: ", min, max);

        printPrimesInRange(min, max);
        System.out.println();
    }


    // Choice 2 - Volume Filling
    public static void doVolume(Scanner stdin) {
        int w, h, d;

        // if stdin is null, generate random numbers
        if (stdin == null) {
            Random rand = new Random();
            w = rand.nextInt(RAND_MAX + 1) + 1;
            h = rand.nextInt(RAND_MAX + 1) + 1;
            d = rand.nextInt(RAND_MAX + 1) + 1;

            System.out.printf(FMT_Q_VOLUME, w, h, d);
        } else {
            System.out.printf(FMT_Q_VOLUME, "width", "height", "depth");

            System.out.print("Enter width, height and depth with a space in between: ");
            w = stdin.nextInt();
            h = stdin.nextInt();
            d = stdin.nextInt();
            stdin.nextLine(); // discard remaining

            if (w <= 0 || h <= 0 || d <= 0) {
                System.out.println("Invalid dimensions.");
                return;
            }
        }

        int cubeEdge = MyMath.gcd(w, h, d);
        System.out.printf(
            "Using cubes of edge length %d you need %d blocks minimum.\n",
            cubeEdge, (w * h * d) / (cubeEdge * cubeEdge * cubeEdge)
        );
    }


    // Choice 3 - Union Area
    public static void doUnionArea(Scanner stdin) {
        Rect r1, r2;

        // if stdin is null, generate random numbers
        if (stdin == null) {
            Random rand = new Random();
            int x1 = rand.nextInt(RECT_COORD_MAX + 1);
            int y1 = rand.nextInt(RECT_COORD_MAX + 1);
            int w1 = rand.nextInt(RECT_WIDTH_MAX + 1) + 1;
            int h1 = rand.nextInt(RECT_HEIGHT_MAX + 1) + 1;

            int x2 = rand.nextInt(RECT_COORD_MAX + 1);
            int y2 = rand.nextInt(RECT_COORD_MAX + 1);
            int w2 = rand.nextInt(RECT_WIDTH_MAX + 1) + 1;
            int h2 = rand.nextInt(RECT_HEIGHT_MAX + 1) + 1;

            r1 = new Rect(x1, y1, w1, h1);
            r2 = new Rect(x2, y2, w2, h2);

            System.out.printf(FMT_Q_UNION_AREA, x1, y1, w1, h1, x2, y2, w2, h2);
        } else {
            System.out.printf(FMT_Q_UNION_AREA, "x1", "y1", "x2", "y2", "w1", "h1", "w2", "h2");

            System.out.print("Enter x1, y1, w1, h1 with a space in between: ");
            r1 = new Rect(
                stdin.nextInt(), stdin.nextInt(), // x1, y1
                stdin.nextInt(), stdin.nextInt()  // w1, h1
            );
            stdin.nextLine(); // discard remaining

            System.out.print("Enter x2, y2, w2, h2 with a space in between: ");
            r2 = new Rect(
                stdin.nextInt(), stdin.nextInt(), // x2, y2
                stdin.nextInt(), stdin.nextInt()  // w2, h2
            );
            stdin.nextLine(); // discard remaining
        }

        System.out.printf("Area of the union is %d.\n", r1.unionArea(r2));
    }

    // Choice 4 - RAndom Questions
    public static void doRandomQuestions(int n) {
        Random rand = new Random();

        for (int i = 0; i < n; i++) {
            // choice is 1, 2 or 3. 4 results in infinite recursion
            int choice = rand.nextInt(3) + 1;

            System.out.printf("%d) ", i + 1);
            parseChoice(choice, null);

            System.out.println();
        }
    }

    public static void parseChoice(int choice, Scanner stdin) {
        switch (choice) {
            case 1: doPrimes(stdin); break;
            case 2: doVolume(stdin); break;
            case 3: doUnionArea(stdin); break;
            case 4:
                System.out.print("Enter number of random questions: ");
                int n = stdin.nextInt();
                stdin.nextLine(); // discard remaining
                System.out.println();

                doRandomQuestions(n);
                break;
        }
    }

    public static void main(String[] args) {
        int choice = 0;
        Scanner stdin = new Scanner(System.in);

        boolean done = false;
        while (!done) {
            System.out.println("1. Prime Numbers");
            System.out.println("2. Volume Filling");
            System.out.println("3. Union Area");
            System.out.println("4. Random Questions");
            System.out.println("5. Exit");
            System.out.println();

            System.out.print("Please enter your choice: ");
            choice = stdin.nextInt();
            stdin.nextLine(); // discard remaining
            System.out.println();

            if (choice < 1 || choice >= 6) System.out.println("Invalid choice.");
            else if (choice == 5) done = true;
            else parseChoice(choice, stdin);

            System.out.println();
        }

        stdin.close();
    }
}
