package inicjaly.domenaszkoly;

import java.util.Random;

public class Main {
    public static void main(String[] args) {

        // 12a: two devices, fixed values
        Device d1 = new Device(25, 70, 1, 8);
        Device d2 = new Device(25, 70, 2, 8);

        System.out.println("=== URZĄDZENIE 1 ===");
        d1.simulate();

        System.out.println("=== URZĄDZENIE 2 ===");
        d2.simulate();

        // 12b: two devices with random values
        Random rand = new Random();
        int[] times = {8, 16, 24};

        // externalTemp: -10..45 inclusive => nextInt(56) - 10
        // deviceTemp:   10..90 inclusive  => nextInt(81) + 10
        // workTime: random choice from {8, 16, 24}
        Device d3 = new Device(
                rand.nextInt(56) - 10,          // externalTemp: -10..45
                rand.nextInt(81) + 10,          // deviceTemp:   10..90
                1,
                times[rand.nextInt(times.length)]
        );

        Device d4 = new Device(
                rand.nextInt(56) - 10,          // externalTemp: -10..45
                rand.nextInt(81) + 10,          // deviceTemp:   10..90
                2,
                times[rand.nextInt(times.length)]
        );

        System.out.println("=== LOSOWE URZĄDZENIE 1 ===");
        d3.simulate();

        System.out.println("=== LOSOWE URZĄDZENIE 2 ===");
        d4.simulate();
    }
}
