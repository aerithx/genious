package inicjaly.domenaszkoly;

import java.util.Random;

/**
 * Entry point – runs four device simulations:
 *
 * a) Two deterministic devices (req. 12a):
 *    - Device 1: 1 fan,  device temp 70°C, external 25°C, work time 8 h
 *    - Device 2: 2 fans, device temp 70°C, external 25°C, work time 8 h
 *
 * b) Two devices with random parameters (req. 12b):
 *    - initial device temp: random 10..90°C
 *    - external temp:       random -10..45°C
 *    - work time:           randomly chosen from {8, 16, 24} hours
 */
public class Main {

    public static void main(String[] args) {

        // ------------------------------------------------------------------ //
        // a) Deterministic devices                                            //
        // ------------------------------------------------------------------ //

        Device d1 = new Device(25, 70, 1, 8, true, "Urządzenie A1 (1 wentylator)");
        Device d2 = new Device(25, 70, 2, 8, true, "Urządzenie A2 (2 wentylatory)");

        printSeparator("SYMULACJA A1 – 1 wentylator, temp.urz.=70°C, temp.ot.=25°C, czas=8h");
        d1.simulate();

        printSeparator("SYMULACJA A2 – 2 wentylatory, temp.urz.=70°C, temp.ot.=25°C, czas=8h");
        d2.simulate();

        // ------------------------------------------------------------------ //
        // b) Random devices                                                   //
        // ------------------------------------------------------------------ //

        Random rand = new Random();
        int[] workTimes = {8, 16, 24};

        // Device temp: 10..90 inclusive  -> rand.nextInt(81) + 10
        // External temp: -10..45 inclusive -> rand.nextInt(56) - 10
        // Work time: random element of {8, 16, 24}

        int devTemp3  = rand.nextInt(81) + 10;
        int extTemp3  = rand.nextInt(56) - 10;
        int workTime3 = workTimes[rand.nextInt(workTimes.length)];

        int devTemp4  = rand.nextInt(81) + 10;
        int extTemp4  = rand.nextInt(56) - 10;
        int workTime4 = workTimes[rand.nextInt(workTimes.length)];

        Device d3 = new Device(extTemp3, devTemp3, 1, workTime3, true,
                "Urządzenie B1 (losowe, 1 wentylator)");
        Device d4 = new Device(extTemp4, devTemp4, 2, workTime4, true,
                "Urządzenie B2 (losowe, 2 wentylatory)");

        printSeparator("SYMULACJA B1 – losowe wartości (1 wentylator)");
        d3.simulate();

        printSeparator("SYMULACJA B2 – losowe wartości (2 wentylatory)");
        d4.simulate();
    }

    private static void printSeparator(String title) {
        System.out.println();
        System.out.println("=".repeat(80));
        System.out.println("  " + title);
        System.out.println("=".repeat(80));
    }
}
