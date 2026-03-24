import java.util.ArrayList;
import java.util.List;

public class Device {
    private static final int PRINT_INTERVAL = 60;

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

            // 4) temp < 0 -> turn off, fans to 0
            if (temp < 0) {
                isOn = false;
                setFansPower(0);
                System.out.println("Za niska temperatura urządzenia grozi awarią - system został wyłączony.");
                break;
            }

            // 6) temp > 130 -> emergency shutdown, fans to 100
            if (temp > 130) {
                isOn = false;
                emergencyShutdown = true;
                setFansPower(100);
                System.out.println("Krytyczna temperatura - urządzenie wyłączone.");
            }

            // auto restart when temp <= 55 (only after emergency shutdown)
            if (emergencyShutdown && temp <= 55) {
                isOn = true;
                emergencyShutdown = false;
                System.out.println("Urządzenie zostało ponownie uruchomione.");
            }

            // 5) warning > 100
            if (temp > 100) {
                System.out.println("Ostrzeżenie: wysoka temperatura");
            }

            // 1-3 fan control (only when not in emergency shutdown)
            if (isOn) {
                if (temp <= 50) setFansPower(0);
                else if (temp <= 75) setFansPower(50);
                else setFansPower(100);
            }

            int power = getFanPower(); // 0/50/100

            // 9) temperature change per minute
            double change = 0;

            if (power == 0) {
                // without fans: +5 degrees/min when on
                change = isOn ? 5 : 0;
            } else {
                // with fans: +2/power heating
                change = 2.0 / power;

                // additional cooling at 100%: -1/tempZ
                if (power == 100) {
                    if (tempZ != 0) {
                        change -= (1.0 / Math.abs(tempZ));
                    }
                    // if tempZ==0, skip cooling to avoid division by zero
                }
            }

            deviceSensor.setTemperature(temp + change);

            // print minute 1 and every PRINT_INTERVAL minutes; warnings/critical always print above
            if (minute == 1 || minute % PRINT_INTERVAL == 0) {
                System.out.printf("Minuta: %d  Temperatura: %.2f  Moc wentylatorów: %d%%%n",
                        minute, deviceSensor.getTemperature(), power);
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
