# genious

## Symulacja wentylatora w urządzeniu (Java)

Projekt Java w pakiecie `inicjaly.domenaszkoly` symulujący pracę wentylatora w urządzeniu z kontrolą temperatury.

### Struktura plików

```
src/inicjaly/domenaszkoly/
  Fan.java      – klasa wentylatora (moc: 0%, 50%, 100%)
  Sensor.java   – klasa czujnika temperatury
  Device.java   – klasa urządzenia (zawiera wentylatory i czujniki, logika symulacji)
  Main.java     – punkt wejścia, uruchamia cztery scenariusze symulacji
```

### Zasady działania

| Temp. urządzenia | Wentylator |
|------------------|-----------|
| ≤ 50 °C          | wyłączony (0%) |
| ≤ 75 °C          | połowa mocy (50%) |
| ≤ 110 °C         | pełna moc (100%) |
| > 100 °C         | komunikat ostrzeżenia |
| > 130 °C         | wyłączenie urządzenia + 100% fan; autostart przy 55 °C |
| < 0 °C           | wyłączenie + komunikat krytyczny, koniec symulacji |

### Zmiana temperatury (°C/min)

- Wentylator 100%: `−1/tempOtoczenia + 2/100` (zabezpieczenie przed dzieleniem przez 0)
- Wentylator 50%: `+2/50`
- Wentylator wyłączony: `+5`

### Kompilacja i uruchomienie

```bash
# Kompilacja
javac -encoding UTF-8 -d out src/inicjaly/domenaszkoly/*.java

# Uruchomienie
java -cp out inicjaly.domenaszkoly.Main
```

### Scenariusze symulacji

**a) Deterministyczne:**
- Urządzenie A1: 1 wentylator, temp.urz.=70°C, temp.ot.=25°C, czas=8h
- Urządzenie A2: 2 wentylatory, temp.urz.=70°C, temp.ot.=25°C, czas=8h

**b) Losowe:**
- Temp. urządzenia: losowa 10–90°C
- Temp. otoczenia: losowa −10–45°C
- Czas pracy: losowo z {8, 16, 24} godzin