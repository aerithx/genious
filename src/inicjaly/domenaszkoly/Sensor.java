package inicjaly.domenaszkoly;

/**
 * Represents a temperature sensor.
 * Used separately for environment temperature and device temperature.
 */
public class Sensor {

    private double temperature;
    private final String name;

    public Sensor(String name, double initialTemperature) {
        this.name = name;
        this.temperature = initialTemperature;
    }

    public String getName() {
        return name;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return name + ": " + String.format("%.2f", temperature) + " C";
    }
}
