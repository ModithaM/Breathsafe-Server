# BreathSafe Arduino Code

This folder contains the Arduino source code for the **BreathSafe sensor module**.

## Overview
The initial plan was to use the **AHT21** (temperature & humidity) and **ENS160** (air quality & VOC) combined sensor setup for data collection. However, during implementation, we were only able to obtain the **AHT21** sensor.

To maintain backend integration and ensure continuous testing, the Arduino code has been **modified** to:
- Collect **real sensor readings** from the **AHT21** sensor.
- Submit **dummy values** for the **ENS160-related air quality data** to the backend.

## Notes
- The dummy values are placeholders and can be easily replaced once the ENS160 sensor is available.
- The backend endpoints remain the same, ensuring compatibility for future updates.

## Future Work
Once the ENS160 sensor is available:
1. Integrate real readings for air quality metrics (TVOC, eCO2, etc.).
2. Remove dummy data submission logic.
3. Re-test backend data flow with both sensors active.

---

**Author:** Moditha Marasingha  
**Project:** BreathSafe  
**Language:** Arduino (C++)  
