import java.util.Random;

public class Main {
    public static void main(String[] args) {

        // a) fixed temperatures
        Device d1 = new Device(25, 70, 1, 8);
        Device d2 = new Device(25, 70, 2, 8);

        System.out.println("=== URZADZENIE 1 (1 wentylator) ===");
        d1.simulate();

        System.out.println("=== URZADZENIE 2 (2 wentylatory) ===");
        d2.simulate();

        // b) random values
        Random rand = new Random();
        int[] times = {8, 16, 24};

        double devT1 = rand.nextInt(81) + 10;    // 10..90
        double envT1 = rand.nextInt(56) - 10;    // -10..45
        int time1 = times[rand.nextInt(3)];

        double devT2 = rand.nextInt(81) + 10;    // 10..90
        double envT2 = rand.nextInt(56) - 10;    // -10..45
        int time2 = times[rand.nextInt(3)];

        System.out.println("Los1: tempU=" + devT1 + ", tempZ=" + envT1 + ", czas=" + time1 + "min");
        System.out.println("Los2: tempU=" + devT2 + ", tempZ=" + envT2 + ", czas=" + time2 + "min");

        Device d3 = new Device(envT1, devT1, 1, time1);
        Device d4 = new Device(envT2, devT2, 2, time2);

        System.out.println("=== LOSOWE URZADZENIE 1 ===");
        d3.simulate();

        System.out.println("=== LOSOWE URZADZENIE 2 ===");
        d4.simulate();
    }
}
