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

        if (fanCount < 1) {
            throw new IllegalArgumentException("fanCount must be at least 1, got: " + fanCount);
        }
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

            // temp < 0 -> shutdown, fans off
            if (temp < 0) {
                isOn = false;
                setFansPower(0);
                System.out.println("Za niska temperatura urzadzenia grozi awaria - system zostal wylaczony.");
                break;
            }

            // temp > 130 -> emergency shutdown, fans 100%
            if (temp > 130 && !emergencyShutdown) {
                isOn = false;
                emergencyShutdown = true;
                setFansPower(100);
                System.out.println("Krytyczna temperatura - urzadzenie wylaczone.");
            }

            // warning > 100
            if (temp > 100) {
                System.out.println("Ostrzezenie: wysoka temperatura");
            }

            // auto-restart after emergency cooling to <= 55
            if (emergencyShutdown && temp <= 55) {
                isOn = true;
                emergencyShutdown = false;
                System.out.println("Urzadzenie zostalo ponownie uruchomione.");
            }

            // normal fan control (only when not in emergency shutdown)
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

            // temperature change per minute
            double change = 0;
            if (power == 0) {
                // no fans: heat +5 only when device is on
                change = isOn ? 5 : 0;
            } else if (power == 50) {
                // fan 50%: heat +2/50
                change = 2.0 / 50;
            } else if (power == 100) {
                // fan 100%: heat +2/100, cool -1/tempZ
                change = 2.0 / 100;
                if (tempZ != 0) {
                    change -= 1.0 / tempZ;
                }
            }

            deviceSensor.setTemperature(temp + change);

            System.out.println("Minuta: " + minute
                    + " | Temperatura: " + Math.round(deviceSensor.getTemperature() * 100.0) / 100.0
                    + " | Moc wentylatorow: " + power + "%");
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
