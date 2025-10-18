#include <WiFi.h>
#include <HTTPClient.h>
#include <ArduinoJson.h>
#include <Wire.h>
#include <Adafruit_AHTX0.h>

const char* WIFI_SSID = "Dialog 4G 052";
const char* WIFI_PASSWORD = "2d5337d5";

const char* API_ENDPOINT = "http://192.168.8.181:8080/api/v1/publicData";
const long SENSOR_ID = 1;

const unsigned long READING_INTERVAL = 30000;  // 30 seconds between readings
const unsigned long API_SEND_INTERVAL = 100000; // ~1.67 minutes for testing (use 300000 for 5 min)

const int I2C_SDA = 21;
const int I2C_SCL = 22;

const int LED_PIN = 2;

const bool SIMULATE_AIR_QUALITY = true;

Adafruit_AHTX0 aht;

struct SensorData {
  double temperature;
  double humidity;
  double co2Level;
  int aqiValue;  // Now stores actual AQI (0-500)
};

const int MAX_READINGS = 10;
SensorData readings[MAX_READINGS];
int readingCount = 0;

unsigned long lastReadingTime = 0;
unsigned long lastApiSendTime = 0;

unsigned long seedValue = 0;

void setup() {
  Serial.begin(115200);
  delay(1000);
  
  Serial.println("\n\n=== BreathSafe Sensor Starting ===");
  Serial.println("Sensor Module: AHT21");
  if (SIMULATE_AIR_QUALITY) {
    Serial.println("Mode: Air Quality Simulation Enabled");
  }
  
  pinMode(LED_PIN, OUTPUT);
  digitalWrite(LED_PIN, LOW);
  
  // Initialize random seed with analog noise
  seedValue = analogRead(0) + millis();
  randomSeed(seedValue);
  
  // Initialize I2C with specified pins
  Wire.begin(I2C_SDA, I2C_SCL);
  Serial.printf("I2C initialized (SDA: %d, SCL: %d)\n", I2C_SDA, I2C_SCL);
  
  // Scan I2C bus
  scanI2C();
  
  // Initialize sensor
  if (!initializeSensor()) {
    Serial.println("ERROR: Sensor initialization failed!");
    blinkError();
    while(1) {
      delay(1000); // Halt if sensor fails
    }
  }
  
  // Connect to WiFi
  connectToWiFi();
  
  Serial.println("=== Setup Complete ===\n");
}

void loop() {
  unsigned long currentTime = millis();
  
  // Check WiFi connection
  if (WiFi.status() != WL_CONNECTED) {
    Serial.println("WiFi disconnected! Attempting to reconnect...");
    digitalWrite(LED_PIN, LOW);
    connectToWiFi();
  }
  
  // Take sensor readings every 30 seconds
  if (currentTime - lastReadingTime >= READING_INTERVAL) {
    lastReadingTime = currentTime;
    takeSensorReading();
  }
  
  // Send data to API every interval
  if (currentTime - lastApiSendTime >= API_SEND_INTERVAL) {
    lastApiSendTime = currentTime;
    sendDataToAPI();
    readingCount = 0; // Reset readings after sending
  }
  
  delay(100);
}

void scanI2C() {
  Serial.println("Scanning I2C bus...");
  byte error, address;
  int nDevices = 0;
  
  for(address = 1; address < 127; address++) {
    Wire.beginTransmission(address);
    error = Wire.endTransmission();
    
    if (error == 0) {
      Serial.printf("  I2C device found at address 0x%02X\n", address);
      nDevices++;
    }
  }
  
  if (nDevices == 0) {
    Serial.println("  No I2C devices found!");
  } else {
    Serial.printf("  Found %d I2C device(s)\n", nDevices);
  }
  Serial.println();
}

bool initializeSensor() {
  Serial.println("Initializing AHT21 sensor...");
  
  // Initialize AHT21
  if (!aht.begin()) {
    Serial.println("ERROR: Could not find AHT21 sensor!");
    Serial.println("Check I2C connections:");
    Serial.printf("  SDA -> GPIO %d\n", I2C_SDA);
    Serial.printf("  SCL -> GPIO %d\n", I2C_SCL);
    return false;
  }
  Serial.println("✓ AHT21 sensor initialized (I2C: 0x38)");
  
  // Wait for sensor to stabilize
  Serial.println("Waiting for sensor to stabilize...");
  delay(2000);
  
  Serial.println("✓ Sensor ready!");
  return true;
}

void connectToWiFi() {
  Serial.print("Connecting to WiFi: ");
  Serial.println(WIFI_SSID);
  
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  
  int attempts = 0;
  while (WiFi.status() != WL_CONNECTED && attempts < 20) {
    delay(500);
    Serial.print(".");
    digitalWrite(LED_PIN, !digitalRead(LED_PIN)); // Blink while connecting
    attempts++;
  }
  
  if (WiFi.status() == WL_CONNECTED) {
    Serial.println("\n✓ WiFi connected!");
    Serial.print("IP Address: ");
    Serial.println(WiFi.localIP());
    Serial.print("Signal Strength: ");
    Serial.print(WiFi.RSSI());
    Serial.println(" dBm");
    digitalWrite(LED_PIN, HIGH); // LED ON when connected
  } else {
    Serial.println("\n✗ WiFi connection failed!");
    Serial.println("Please check WiFi credentials");
    digitalWrite(LED_PIN, LOW);
  }
}

// Generate realistic AQI (0-300+ scale) based on temperature and humidity
int generateRealisticAQI(float temperature, float humidity) {
  int baseAQI = 35; // Default: Good (0-50)
  
  // Humidity affects air quality
  if (humidity > 70) {
    baseAQI = 75; // Moderate (51-100)
  } else if (humidity < 30) {
    baseAQI = 65; // Moderate (51-100)
  }
  
  // Temperature extremes affect air quality
  if (temperature > 32) {
    baseAQI += 20;
  } else if (temperature < 16) {
    baseAQI += 15;
  }
  
  // Add random variation for realism
  int variation = random(-15, 25);
  int finalAQI = baseAQI + variation;
  
  // Ensure AQI stays in reasonable range (10-200)
  if (finalAQI < 10) finalAQI = 10;
  if (finalAQI > 200) finalAQI = 200;
  
  return finalAQI;
}

// Get AQI category name for display
String getAQICategory(int aqiValue) {
  if (aqiValue <= 50) {
    return "Good";
  } else if (aqiValue <= 100) {
    return "Moderate";
  } else if (aqiValue <= 150) {
    return "Unhealthy for Sensitive";
  } else if (aqiValue <= 200) {
    return "Unhealthy";
  } else if (aqiValue <= 300) {
    return "Very Unhealthy";
  } else {
    return "Hazardous";
  }
}

double generateRealisticCO2(float temperature, float humidity, int aqiValue) {
  // Base CO2 (typical indoor levels: 400-1000 ppm)
  double baseCO2 = 450 + random(0, 150); // 450-600 ppm base
  
  // AQI affects CO2 levels (using new AQI scale)
  if (aqiValue <= 50) {
    // Good: 400-700 ppm
    baseCO2 += random(0, 100);
  } else if (aqiValue <= 100) {
    // Moderate: 600-1000 ppm
    baseCO2 += random(100, 400);
  } else if (aqiValue <= 150) {
    // Unhealthy for Sensitive: 900-1400 ppm
    baseCO2 += random(400, 800);
  } else if (aqiValue <= 200) {
    // Unhealthy: 1300-1800 ppm
    baseCO2 += random(700, 1200);
  } else {
    // Very Unhealthy: 1700-2500 ppm
    baseCO2 += random(1100, 1900);
  }
  
  // Add slight temperature influence
  if (temperature > 25) {
    baseCO2 += random(0, 50);
  }
  
  // Add random variation
  baseCO2 += random(-30, 30);
  
  // Ensure reasonable range
  if (baseCO2 < 400) baseCO2 = 400;
  if (baseCO2 > 2500) baseCO2 = 2500;
  
  return baseCO2;
}

void takeSensorReading() {
  if (readingCount >= MAX_READINGS) {
    Serial.println("WARNING: Maximum readings reached, waiting for API send...");
    return;
  }
  
  Serial.println("\n--- Taking Sensor Reading ---");
  
  sensors_event_t humidity_event, temp_event;
  aht.getEvent(&humidity_event, &temp_event);
  
  readings[readingCount].temperature = temp_event.temperature;
  readings[readingCount].humidity = humidity_event.relative_humidity;
  
  if (SIMULATE_AIR_QUALITY) {
    // Generate realistic simulated values
    readings[readingCount].aqiValue = generateRealisticAQI(
      temp_event.temperature, 
      humidity_event.relative_humidity
    );
    readings[readingCount].co2Level = generateRealisticCO2(
      temp_event.temperature,
      humidity_event.relative_humidity,
      readings[readingCount].aqiValue
    );
  } else {
    // Fallback if simulation is disabled
    readings[readingCount].aqiValue = 45; // Default "Good"
    readings[readingCount].co2Level = 500.0; // Default safe level
  }
  
  // Display readings
  Serial.printf("Reading #%d:\n", readingCount + 1);
  Serial.printf("  Temperature: %.2f °C\n", readings[readingCount].temperature);
  Serial.printf("  Humidity: %.2f %%\n", readings[readingCount].humidity);
  Serial.printf("  CO2: %.0f ppm", readings[readingCount].co2Level);
  if (SIMULATE_AIR_QUALITY) Serial.print(" [simulated]");
  Serial.println();
  Serial.printf("  AQI: %d (%s)", readings[readingCount].aqiValue, 
                getAQICategory(readings[readingCount].aqiValue).c_str());
  if (SIMULATE_AIR_QUALITY) Serial.print(" [simulated]");
  Serial.println();
  
  readingCount++;
}

void sendDataToAPI() {
  if (readingCount == 0) {
    Serial.println("No readings to send!");
    return;
  }
  
  Serial.println("\n=== Preparing to Send Data to API ===");
  
  long aqiSum = 0;
  for (int i = 0; i < readingCount; i++) {
    aqiSum += readings[i].aqiValue;
  }
  int avgAQI = aqiSum / readingCount;
  
  SensorData lastReading = readings[readingCount - 1];
  
  Serial.printf("Data to send (from %d readings):\n", readingCount);
  Serial.printf("  Temperature: %.2f °C (last)\n", lastReading.temperature);
  Serial.printf("  Humidity: %.2f %% (last)\n", lastReading.humidity);
  Serial.printf("  CO2: %.0f ppm (last)\n", lastReading.co2Level);
  Serial.printf("  AQI: %d (%s) (average)\n", avgAQI, getAQICategory(avgAQI).c_str());
  Serial.printf("  Sensor ID: %ld\n", SENSOR_ID);
  
  // Create JSON payload
  StaticJsonDocument<256> doc;
  doc["temperature"] = lastReading.temperature;
  doc["humidity"] = lastReading.humidity;
  doc["co2Level"] = lastReading.co2Level;
  doc["aqiValue"] = avgAQI;  // Now sending proper AQI value (0-300+)
  doc["sensorId"] = SENSOR_ID;
  
  String jsonPayload;
  serializeJson(doc, jsonPayload);
  
  Serial.println("JSON Payload: " + jsonPayload);
  
  // Send HTTP POST request
  if (WiFi.status() == WL_CONNECTED) {
    HTTPClient http;
    http.begin(API_ENDPOINT);
    http.addHeader("Content-Type", "application/json");
    
    int httpResponseCode = http.POST(jsonPayload);
    
    if (httpResponseCode > 0) {
      Serial.printf("✓ API Response Code: %d\n", httpResponseCode);
      String response = http.getString();
      if (response.length() > 0) {
        Serial.println("Response: " + response);
      }
      if (httpResponseCode == 200 || httpResponseCode == 201) {
        Serial.println("✓ Data sent successfully!");
        blinkSuccess();
      }
    } else {
      Serial.printf("✗ API Error: %s\n", http.errorToString(httpResponseCode).c_str());
      Serial.println("Check API endpoint and network connection");
    }
    
    http.end();
  } else {
    Serial.println("✗ WiFi not connected! Cannot send data.");
  }
  
  Serial.println("=== API Send Complete ===\n");
}

void blinkSuccess() {
  for (int i = 0; i < 3; i++) {
    digitalWrite(LED_PIN, LOW);
    delay(100);
    digitalWrite(LED_PIN, HIGH);
    delay(100);
  }
}

void blinkError() {
  for (int i = 0; i < 10; i++) {
    digitalWrite(LED_PIN, !digitalRead(LED_PIN));
    delay(200);
  }
}