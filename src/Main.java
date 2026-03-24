import java.util.Random;

public class Main {
    public static void main(String[] args) {

        // a) two fixed devices: one with 1 fan, one with 2 fans,
        //    device temp 70, external temp 25, work time 8 hours
        Device d1 = new Device(25, 70, 1, 8);
        Device d2 = new Device(25, 70, 2, 8);

        System.out.println("=== URZĄDZENIE 1 ===");
        d1.simulate();

        System.out.println("=== URZĄDZENIE 2 ===");
        d2.simulate();

        // b) two devices with random values:
        //    device temp: random from 10..90
        //    external temp: random from -10..45
        //    work time: random choice from {8, 16, 24} hours
        Random rand = new Random();
        int[] times = {8, 16, 24};

        Device d3 = new Device(
                rand.nextInt(56) - 10,   // external temp: -10..45
                rand.nextInt(81) + 10,   // device temp: 10..90
                1,
                times[rand.nextInt(3)]   // work time: 8, 16 or 24 hours
        );

        Device d4 = new Device(
                rand.nextInt(56) - 10,   // external temp: -10..45
                rand.nextInt(81) + 10,   // device temp: 10..90
                2,
                times[rand.nextInt(3)]   // work time: 8, 16 or 24 hours
        );

        System.out.println("=== LOSOWE URZĄDZENIE 1 ===");
        d3.simulate();

        System.out.println("=== LOSOWE URZĄDZENIE 2 ===");
        d4.simulate();
    }
}
