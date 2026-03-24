package inicjaly.domenaszkoly;

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

            // temp < 0 → shut down, fans off
            if (temp < 0) {
                isOn = false;
                setFansPower(0);
                System.out.println("Za niska temperatura urządzenia grozi awarią - system został wyłączony.");
                break;
            }

            // temp > 130 → emergency shutdown, fans at 100%
            if (temp > 130) {
                isOn = false;
                emergencyShutdown = true;
                setFansPower(100);
                System.out.println("Krytyczna temperatura - urządzenie wyłączone.");
            }

            // auto-restart when temp drops to ≤ 55°C after emergency shutdown
            if (emergencyShutdown && temp <= 55) {
                isOn = true;
                emergencyShutdown = false;
                System.out.println("Urządzenie zostało ponownie uruchomione.");
            }

            // warning when temp > 100°C
            if (temp > 100) {
                System.out.println("Ostrzeżenie: wysoka temperatura");
            }

            // fan control (only while running): temp ≤ 50 → 0%, temp ≤ 75 → 50%, temp > 75 → 100%
            if (isOn) {
                if (temp <= 50) {
                    setFansPower(0);
                } else if (temp <= 75) {
                    setFansPower(50);
                } else {
                    setFansPower(100);
                }
            }

            int power = getFanPower();

            // temperature change per minute:
            //   fan=0:   +5°C/min (device is on and no fans running)
            //   fan=50:  +2/50°C/min
            //   fan=100: +2/100°C/min − 1/tempZ°C/min (extra cooling; skip if tempZ==0)
            double change;
            if (power == 0) {
                change = isOn ? 5.0 : 0.0;
            } else {
                change = 2.0 / power;
                if (power == 100 && tempZ > 0) {
                    change -= (1.0 / tempZ);
                }
            }

            deviceSensor.setTemperature(temp + change);

            if (minute == 1 || minute % 60 == 0) {
                System.out.println("Minuta: " + minute
                        + " Temperatura: " + deviceSensor.getTemperature()
                        + " Moc wentylatorów: " + power + "%");
            }
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
