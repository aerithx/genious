package inicjaly.domenaszkoly;

public class Sensor {
    private double temperature;

    public Sensor(double temperature) {
        this.temperature = temperature;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
