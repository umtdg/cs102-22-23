import java.util.Scanner;

import asgn3.*;

public class Main {
    public static void main(String[] args) {
        Scanner stdin = new Scanner(System.in);

        while (true) {
            System.out.print("Please enter the road length: ");
            int roadLength = stdin.nextInt();
            stdin.nextLine();

            System.out.print("Please enter the number of vehicles: ");
            int numVehicles = stdin.nextInt();
            stdin.nextLine();

            System.out.println();
            new RaceSim(numVehicles, roadLength).simulate();
            System.out.println();

            System.out.print("End of simulation. Do you want to play again (y/N)? ");
            String response = stdin.nextLine();
            if (!response.toLowerCase().startsWith("y")) break;

            System.out.println();
        }

        stdin.close();
    }
}
