# genious

## Fan-Controlled Device Temperature Simulation

Java simulation of a device with fan-controlled cooling, written to comply with the school assignment requirements (points 1–12).

### Assignment compliance

| # | Requirement | Status |
|---|-------------|--------|
| 1 | Package `inicjaly.domenaszkoly` | ✅ All classes use `package inicjaly.domenaszkoly;` |
| 2 | Classes: `Fan`, `Sensor`, `Device`, `Main` | ✅ |
| 3 | Full encapsulation (private fields, getters/setters) | ✅ |
| 4 | `Fan` accepts only power values 0, 50, 100 | ✅ Validated in `setPower()` |
| 5 | Fan thresholds: temp ≤ 50 → 0%, temp ≤ 75 → 50%, temp > 75 → 100% | ✅ |
| 6 | Warning message when temp > 100°C | ✅ `"Ostrzeżenie: wysoka temperatura"` |
| 7 | Emergency shutdown when temp > 130°C (fans → 100%) | ✅ `"Krytyczna temperatura - urządzenie wyłączone."` |
| 8 | Auto-restart when temp drops to ≤ 55°C after emergency shutdown | ✅ `"Urządzenie zostało ponownie uruchomione."` |
| 9 | Temperature-change formulas: fan=0 → +5°C/min; fan=50 → +2/50°C/min; fan=100 → +2/100 − 1/tempZ °C/min | ✅ |
| 10 | Fixed devices: `Device(25, 70, 1, 8)` and `Device(25, 70, 2, 8)` | ✅ |
| 11 | Random device temp: 10–90°C; external temp: −10–45°C | ✅ `rand.nextInt(81)+10` / `rand.nextInt(56)-10` |
| 12 | Work time randomly selected from {8, 16, 24} hours | ✅ `times[rand.nextInt(times.length)]` |

### How to compile and run

```bash
# From the repository root, compile all sources into the out/ directory:
javac -d out src/inicjaly/domenaszkoly/*.java

# Run the simulation:
java -cp out inicjaly.domenaszkoly.Main
```

The program prints minute 1 and every 60th minute so the output stays readable.
Emergency and warning messages are printed immediately when triggered.