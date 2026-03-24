import java.util.ArrayList;
import java.util.List;

public class Device {
    private Sensor externalSensor;
    private Sensor deviceSensor;
    private List<Fan> fans;

    private boolean isOn;
    private boolean emergencyShutdown;

    private int workTime; // hours

    public Device(double externalTemp, double deviceTemp, int fanCount, int workTime) {
        this.externalSensor = new Sensor(externalTemp);
        this.deviceSensor = new Sensor(deviceTemp);
        this.workTime = workTime;

        this.isOn = true;
        this.emergencyShutdown = false;

        if (fanCount < 1) fanCount = 1;
        fans = new ArrayList<>();
        for (int i = 0; i < fanCount; i++) {
            fans.add(new Fan());
        }
    }

    public void simulate() {
        int minutes = workTime * 60;

        for (int minute = 1; minute <= minutes; minute++) {
            double temp = deviceSensor.getTemperature();
            double tempZ = externalSensor.getTemperature();

            // temp < 0 -> turn off, fans to 0
            if (temp < 0) {
                isOn = false;
                setFansPower(0);
                System.out.println("Za niska temperatura urządzenia grozi awarią - system został wyłączony.");
                break;
            }

            // temp > 130 -> emergency shutdown, fans to 100
            if (temp > 130) {
                isOn = false;
                emergencyShutdown = true;
                setFansPower(100);
                System.out.println("Krytyczna temperatura - urządzenie wyłączone.");
            }

            // auto restart when temp drops to <= 55 after emergency shutdown
            if (emergencyShutdown && temp <= 55) {
                isOn = true;
                emergencyShutdown = false;
                System.out.println("Urządzenie zostało ponownie uruchomione.");
            }

            // warning when temp > 100
            if (temp > 100) {
                System.out.println("Ostrzeżenie: wysoka temperatura");
            }

            // fan control: temp <= 50 -> 0%, temp <= 75 -> 50%, temp > 75 -> 100%
            if (isOn) {
                if (temp <= 50) setFansPower(0);
                else if (temp <= 75) setFansPower(50);
                else setFansPower(100);
            }

            int power = getFanPower();

            // temperature change per minute:
            // fan=0: +5 (only when on), fan=50: +2/50, fan=100: +2/100 - 1/tempZ
            double change = 0;

            if (power == 0) {
                // no fans: heats up by 5 degrees/min when on
                change = isOn ? 5 : 0;
            } else {
                // with fans: heats up by 2/fanPower degrees/min
                change = 2.0 / power;

                // additional cooling at 100%: -1/tempZ degrees/min
                if (power == 100 && tempZ != 0) {
                    change -= (1.0 / tempZ);
                }
            }

            deviceSensor.setTemperature(temp + change);

            System.out.println("Minuta: " + minute
                    + " Temperatura: " + deviceSensor.getTemperature()
                    + " Moc wentylatorów: " + power);
        }
    }

    private void setFansPower(int power) {
        for (Fan f : fans) {
            f.setPower(power);
        }
    }

    private int getFanPower() {
        return fans.get(0).getPower();
    }
}
