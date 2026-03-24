package inicjaly.domenaszkoly;

public class Fan {
    private int power; // 0, 50, or 100 percent

    public Fan() {
        this.power = 0;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        if (power == 0 || power == 50 || power == 100) {
            this.power = power;
        }
    }
}
