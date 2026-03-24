package inicjaly.domenaszkoly;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a device that contains fans and temperature sensors.
 *
 * Fan control rules:
 *   device temp <= 50  -> fans OFF  (0%)
 *   device temp <= 75  -> fans HALF power (50%)
 *   device temp <= 110 -> fans FULL power (100%)
 *   device temp >  100 -> print warning
 *   device temp >  130 -> shutdown + fans 100%, auto-restart at 55 C
 *   device temp <  0   -> shutdown, print critical message, stop simulation
 *
 * Temperature change per minute:
 *   fans 100%: delta = -1/externalTemp + 2/100  (guard: if externalTemp==0, skip cooling term)
 *   fans  50%: delta = +2/50
 *   fans   0%: delta = +5
 */
public class Device {

    // Sensors
    private final Sensor envSensor;
    private final Sensor deviceSensor;

    // Fans
    private final List<Fan> fans;

    // State
    private boolean on;
    private int workTimeHours;

    private final String label;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * @param externalTemp   initial environment temperature (°C)
     * @param deviceTemp     initial device temperature (°C)
     * @param numFans        number of fans (>= 1)
     * @param workTimeHours  planned work time in hours
     */
    public Device(double externalTemp, double deviceTemp, int numFans, int workTimeHours) {
        this(externalTemp, deviceTemp, numFans, workTimeHours, true, "Urządzenie");
    }

    /**
     * Full constructor.
     *
     * @param externalTemp   initial environment temperature (°C)
     * @param deviceTemp     initial device temperature (°C)
     * @param numFans        number of fans (>= 1)
     * @param workTimeHours  planned work time in hours
     * @param on             initial on/off state
     * @param label          display label for this device
     */
    public Device(double externalTemp, double deviceTemp, int numFans, int workTimeHours,
                  boolean on, String label) {
        if (numFans < 1) {
            throw new IllegalArgumentException("Urządzenie musi mieć co najmniej 1 wentylator.");
        }
        this.envSensor    = new Sensor("Czujnik otoczenia", externalTemp);
        this.deviceSensor = new Sensor("Czujnik urządzenia", deviceTemp);
        this.fans = new ArrayList<>();
        for (int i = 0; i < numFans; i++) {
            fans.add(new Fan());
        }
        this.on            = on;
        this.workTimeHours = workTimeHours;
        this.label         = label;
    }

    // -------------------------------------------------------------------------
    // Getters / Setters
    // -------------------------------------------------------------------------

    public double getExternalTemperature() {
        return envSensor.getTemperature();
    }

    public void setExternalTemperature(double temp) {
        envSensor.setTemperature(temp);
    }

    public double getDeviceTemperature() {
        return deviceSensor.getTemperature();
    }

    public void setDeviceTemperature(double temp) {
        deviceSensor.setTemperature(temp);
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public int getWorkTimeHours() {
        return workTimeHours;
    }

    public void setWorkTimeHours(int workTimeHours) {
        this.workTimeHours = workTimeHours;
    }

    public int getFanCount() {
        return fans.size();
    }

    /** Returns the power of the first fan (all fans are always set to the same level). */
    public int getFanPower() {
        return fans.get(0).getPower();
    }

    // -------------------------------------------------------------------------
    // Internal helpers
    // -------------------------------------------------------------------------

    private void setAllFansPower(int power) {
        for (Fan fan : fans) {
            fan.setPower(power);
        }
    }

    private void printStatus(int minute) {
        System.out.printf("  Minuta %4d | Temp. urządzenia: %7.2f C | Temp. otoczenia: %6.2f C"
                        + " | Wentylatory (%dx): %3d%% | Stan: %s%n",
                minute,
                deviceSensor.getTemperature(),
                envSensor.getTemperature(),
                fans.size(),
                getFanPower(),
                on ? "ON" : "OFF");
    }

    // -------------------------------------------------------------------------
    // Simulation
    // -------------------------------------------------------------------------

    /**
     * Runs the minute-by-minute simulation for the configured work time.
     * Prints status at minute 1 and then every 60 minutes.
     * Warning / shutdown messages are always printed immediately.
     */
    public void simulate() {
        int totalMinutes = workTimeHours * 60;
        boolean overheating = false;    // true while device is shut down due to temp > 130
        boolean warningSent = false;    // true once warning "wysoka temperatura" was printed

        System.out.printf("[%s] Start symulacji: temp.urz.=%.1f C, temp.ot.=%.1f C,"
                        + " wentylatory=%d, czas=%d godz. (%d min)%n",
                label,
                deviceSensor.getTemperature(),
                envSensor.getTemperature(),
                fans.size(),
                workTimeHours,
                totalMinutes);

        for (int minute = 1; minute <= totalMinutes; minute++) {

            double deviceTemp = deviceSensor.getTemperature();
            double extTemp    = envSensor.getTemperature();

            // ------------------------------------------------------------------
            // 1. Check critically LOW temperature -> stop simulation (req. 4)
            // ------------------------------------------------------------------
            if (deviceTemp < 0) {
                System.out.println("  [KRYTYCZNY] za niska temperatura urządzenia grozi awarią"
                        + " - system został wyłączony.");
                setAllFansPower(0);
                on = false;
                printStatus(minute);
                return;
            }

            // ------------------------------------------------------------------
            // 2. Check critically HIGH temperature -> shutdown (req. 6)
            // ------------------------------------------------------------------
            if (deviceTemp > 130 && on) {
                System.out.printf("  [WYŁĄCZENIE] Minuta %4d | Temperatura krytyczna: %.2f C"
                        + " > 130 C! Urządzenie wyłączone, wentylatory 100%%.%n",
                        minute, deviceTemp);
                on = false;
                overheating = true;
                setAllFansPower(100);
            }

            // ------------------------------------------------------------------
            // 3. Auto-restart after overheat (req. 6)
            // ------------------------------------------------------------------
            if (overheating && deviceTemp <= 55) {
                on = true;
                overheating = false;
                System.out.printf("  [RESTART]   Minuta %4d | Temperatura: %.2f C"
                        + " - urządzenie włączone ponownie.%n",
                        minute, deviceTemp);
            }

            // ------------------------------------------------------------------
            // 4. Determine fan power
            // ------------------------------------------------------------------
            int power;
            if (!on) {
                // Device is cooling down after overheating: fans stay at 100%
                power = 100;
            } else if (deviceTemp <= 50) {
                power = 0;      // req. 1
            } else if (deviceTemp <= 75) {
                power = 50;     // req. 2
            } else {
                power = 100;    // req. 3  (covers 75 < temp <= 110 and > 110)
            }
            setAllFansPower(power);

            // ------------------------------------------------------------------
            // 5. High-temperature warning (req. 5) – printed once per event
            // ------------------------------------------------------------------
            if (on && deviceTemp > 100) {
                if (!warningSent) {
                    System.out.printf("  [OSTRZEŻENIE] Minuta %4d | ostrzeżenie: wysoka temperatura"
                            + " (%.2f C)%n", minute, deviceTemp);
                    warningSent = true;
                }
            } else {
                // Reset warning flag once temperature drops back below 100°C
                warningSent = false;
            }

            // ------------------------------------------------------------------
            // 6. Calculate new device temperature (req. 9)
            // ------------------------------------------------------------------
            double delta;
            if (power == 100) {
                // Cooled by 1/externalTemp AND heated by 2/100
                if (extTemp == 0) {
                    // Guard against division by zero – only apply heating term
                    delta = 2.0 / 100.0;
                } else {
                    delta = (-1.0 / extTemp) + (2.0 / 100.0);
                }
            } else if (power == 50) {
                // Only heated by 2/50 (no cooling term)
                delta = 2.0 / 50.0;
            } else {
                // Fan off – heated by 5°C/min
                delta = 5.0;
            }
            deviceSensor.setTemperature(deviceTemp + delta);

            // ------------------------------------------------------------------
            // 7. Periodic status output (minute 1 and every 60 minutes)
            // ------------------------------------------------------------------
            if (minute == 1 || minute % 60 == 0) {
                printStatus(minute);
            }
        }

        System.out.printf("[%s] Symulacja zakończona po %d minutach (%d godz.).%n",
                label, totalMinutes, workTimeHours);
    }

    @Override
    public String toString() {
        return String.format("Device{label='%s', deviceTemp=%.2f, extTemp=%.2f, fans=%d, on=%b, workTime=%dh}",
                label, deviceSensor.getTemperature(), envSensor.getTemperature(),
                fans.size(), on, workTimeHours);
    }
}
