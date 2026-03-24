package inicjaly.domenaszkoly;

/**
 * Represents a single cooling fan.
 * Valid power levels: 0 (off), 50 (half), 100 (full).
 */
public class Fan {

    private int power; // 0, 50 or 100 percent

    public Fan() {
        this.power = 0;
    }

    public Fan(int power) {
        setPower(power);
    }

    public int getPower() {
        return power;
    }

    /**
     * Sets fan power. Only 0, 50 and 100 are valid values.
     */
    public void setPower(int power) {
        if (power != 0 && power != 50 && power != 100) {
            throw new IllegalArgumentException("Nieprawidłowa moc wentylatora: " + power + ". Dozwolone: 0, 50, 100.");
        }
        this.power = power;
    }

    @Override
    public String toString() {
        return "Wentylator{moc=" + power + "%}";
    }
}
