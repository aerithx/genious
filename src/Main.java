import java.util.Random;

public class Main {
    public static void main(String[] args) {

        // 12a) dwa urzadzenia: tempU=70, tempZ=25, czas=8h
        Device d1 = new Device(25, 70, 1, 8);
        Device d2 = new Device(25, 70, 2, 8);

        System.out.println("=== URZĄDZENIE 1 ===");
        d1.simulate();

        System.out.println("=== URZĄDZENIE 2 ===");
        d2.simulate();

        // 12b) dwa urzadzenia z losowymi wartosciami
        Random rand = new Random();
        int[] times = {8, 16, 24};

        // tempU: 10..90 (nextInt(81)+10)
        // tempZ: -10..45 (nextInt(56)-10)
        // czas: losowo z {8, 16, 24}
        Device d3 = new Device(
                rand.nextInt(56) - 10,
                rand.nextInt(81) + 10,
                1,
                times[rand.nextInt(times.length)]
        );

        Device d4 = new Device(
                rand.nextInt(56) - 10,
                rand.nextInt(81) + 10,
                2,
                times[rand.nextInt(times.length)]
        );

        System.out.println("=== LOSOWE URZĄDZENIE 1 ===");
        d3.simulate();

        System.out.println("=== LOSOWE URZĄDZENIE 2 ===");
        d4.simulate();
    }
}
