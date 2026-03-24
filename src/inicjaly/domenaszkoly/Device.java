package inicjaly.domenaszkoly;

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

    public void setOn(boolean on) {
        this.isOn = on;
        if (!on) {
            setFansPower(0);
        }
    }

    public void setWorkTimeHours(int workTimeHours) {
        this.workTimeHours = workTimeHours;
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

            // req. 4: temp < 0 -> OFF, message, stop
            if (temp < 0) {
                isOn = false;
                setFansPower(0);
                System.out.println("za niska temperatura urządzenia grozi awarią - system został wyłączony.");
                break;
            }

            // req. 6: temp > 130 -> emergency OFF, fans 100%
            if (temp > 130 && !emergencyShutdown) {
                isOn = false;
                emergencyShutdown = true;
                setFansPower(100);
            }

            // req. 6: auto ON when temp reaches 55 (after emergency shutdown)
            if (emergencyShutdown && temp <= 55) {
                isOn = true;
                emergencyShutdown = false;
            }

            // req. 5: warning > 100
            if (temp > 100) {
                System.out.println("ostrzeżenie: wysoka temperatura");
            }

            // req. 1-3: fan control (only when device is ON)
            if (isOn) {
                if (temp <= 50) setFansPower(0);
                else if (temp <= 75) setFansPower(50);
                else setFansPower(100);
            }
            // when OFF due to emergency, fans stay at 100%

            int power = getFanPower();

            // req. 9: temperature change per minute
            double change;
            if (power == 0) {
                // no fans: +5°C/min only when device is ON
                change = isOn ? 5.0 : 0.0;
            } else if (power == 50) {
                // fans at 50%: +2/50 per minute
                change = 2.0 / 50.0;
            } else {
                // fans at 100%: +2/100 per minute (heating) AND -1/tempZ per minute (cooling)
                change = 2.0 / 100.0;
                if (tempZ != 0) {
                    change -= 1.0 / tempZ;
                }
                // guard: if tempZ == 0, skip the cooling term to avoid division by zero
            }

            deviceSensor.setTemperature(temp + change);

            System.out.println("Minuta: " + minute
                    + " Temperatura: " + deviceSensor.getTemperature()
                    + " Moc wentylatorów: " + power
                    + " Włączone: " + isOn);
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
