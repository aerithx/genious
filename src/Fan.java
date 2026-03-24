public class Fan {
    private int power; // 0, 50, 100

    public Fan() {
        this.power = 0;
    }

    public void setPower(int power) {
        if (power == 0 || power == 50 || power == 100) {
            this.power = power;
        }
    }

    public int getPower() {
        return power;
    }
}
