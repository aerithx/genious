import java.util.Random;

public class Main {
    public static void main(String[] args) {

        // a)
        Device d1 = new Device(25, 70, 1, 8);
        Device d2 = new Device(25, 70, 2, 8);

        System.out.println("=== URZĄDZENIE 1 ===");
        d1.simulate();

        System.out.println("=== URZĄDZENIE 2 ===");
        d2.simulate();

        // b)
        Random rand = new Random();
        int[] times = {8, 16, 24};

        Device d3 = new Device(
                rand.nextInt(56) - 10,   // temp zewnętrzna: -10..45
                rand.nextInt(81) + 10,   // temp urządzenia: 10..90
                1,
                times[rand.nextInt(3)]
        );

        Device d4 = new Device(
                rand.nextInt(56) - 10,   // temp zewnętrzna: -10..45
                rand.nextInt(81) + 10,   // temp urządzenia: 10..90
                2,
                times[rand.nextInt(3)]
        );

        System.out.println("=== LOSOWE URZĄDZENIE 1 ===");
        d3.simulate();

        System.out.println("=== LOSOWE URZĄDZENIE 2 ===");
        d4.simulate();
    }
}
