import java.util.ArrayList;
import java.util.List;

public class Device {
    private final Sensor externalSensor;
    private final Sensor deviceSensor;
    private final List<Fan> fans;

    private boolean isOn;
    private boolean emergencyShutdown;
    private int workTimeHours;

    public Device(double externalTemp, double deviceTemp, int fanCount, int workTimeHours) {
        this.externalSensor = new Sensor(externalTemp);
        this.deviceSensor = new Sensor(deviceTemp);
        this.workTimeHours = workTimeHours;

        this.isOn = true;
        this.emergencyShutdown = false;

        if (fanCount < 1) fanCount = 1;
        this.fans = new ArrayList<>();
        for (int i = 0; i < fanCount; i++) {
            this.fans.add(new Fan());
        }
    }

    public void setWorkTimeHours(int workTimeHours) {
        this.workTimeHours = workTimeHours;
    }

    public void setOn(boolean on) {
        this.isOn = on;
        if (!on) {
            setFansPower(0);
        }
    }

    public double getExternalTemp() {
        return externalSensor.getTemperature();
    }

    public void setExternalTemp(double t) {
        externalSensor.setTemperature(t);
    }

    public double getDeviceTemp() {
        return deviceSensor.getTemperature();
    }

    public void setDeviceTemp(double t) {
        deviceSensor.setTemperature(t);
    }

    public void simulate() {
        int minutes = workTimeHours * 60;

        for (int minute = 1; minute <= minutes; minute++) {
            double temp = deviceSensor.getTemperature();
            double tempZ = externalSensor.getTemperature();

            // pkt 4: temp < 0 -> wylacz + komunikat + przerwij
            if (temp < 0) {
                isOn = false;
                setFansPower(0);
                System.out.println("za niska temperatura urządzenia grozi awarią - system został wyłączony.");
                break;
            }

            // pkt 6: temp > 130 -> awaryjne wylaczenie, wentylatory 100%
            if (temp > 130 && !emergencyShutdown) {
                isOn = false;
                emergencyShutdown = true;
                setFansPower(100);
                System.out.println("Krytyczna temperatura - urządzenie wyłączone.");
            }

            // pkt 6: auto-wlaczenie gdy temperatura osiagnie 55
            if (emergencyShutdown && temp <= 55) {
                isOn = true;
                emergencyShutdown = false;
                System.out.println("Urządzenie zostało ponownie uruchomione.");
            }

            // pkt 5: ostrzezenie > 100
            if (temp > 100) {
                System.out.println("ostrzeżenie: wysoka temperatura");
            }

            // pkt 1-3: sterowanie wentylatorami (tylko gdy urzadzenie ON)
            if (isOn) {
                if (temp <= 50) setFansPower(0);
                else if (temp <= 75) setFansPower(50);
                else setFansPower(100);
            }

            int power = getFanPower();

            // pkt 9: zmiana temperatury na minute
            double change;
            if (power == 0) {
                // bez wentylatorow: +5 stopni/min gdy urzadzenie wlaczone
                change = isOn ? 5.0 : 0.0;
            } else if (power == 50) {
                // wentylator 50%: nagrzewanie +2/50
                change = 2.0 / 50.0;
            } else {
                // wentylator 100%: nagrzewanie +2/100 ORAZ chlodzenie -1/tempZ
                change = 2.0 / 100.0;
                if (tempZ != 0) {
                    change -= 1.0 / tempZ;
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
