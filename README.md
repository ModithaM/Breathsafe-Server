# 🌿 BreathSafe Backend

<img width="1440" height="900" alt="Screenshot 2025-10-18 at 2 53 44 PM" src="https://github.com/user-attachments/assets/f672cec7-96c4-4190-a98e-8e74ee77ef3c" />


The **BreathSafe Backend** is a Spring Boot–based REST API server designed to collect, process, and analyze air quality data from IoT sensors.  
It powers the BreathSafe system — an environmental health monitoring platform that provides **real-time air quality tracking** and **AI-generated safety recommendations**.

---

##  Overview

The BreathSafe system was initially planned to use the **AHT21** (temperature & humidity) and **ENS160** (air quality & VOC) combined sensors.  
Due to limited hardware availability, the current prototype uses only the **AHT21** sensor, while **dummy values** are submitted for air quality data to maintain backend compatibility.

The backend provides:
- Sensor data ingestion & storage
- User and device (sensor) management
- Subscription to sensors (each user can subscribe to multiple sensors)
- Automatic **warning email generation** when unsafe readings are detected
- **AI-powered prevention tips** included in emails to guide users in improving air quality or safety

---

## ⚙️ Tech Stack

- **Java 17**
- **Spring Boot 3.5.4
- **Spring Data JPA / Hibernate**
- **MySQL**
- **Maven**
- **Spring Mail** (for email alerts)
- **Gemini Ai** (for generating preventive instructions)

---

##  Main Features

### 🔹 Sensor Data Collection
- Accepts readings from AHT21 sensors (temperature & humidity).
- Stores all readings in the database for historical tracking.

### 🔹 Intelligent Warning System
- Continuously analyzes sensor readings.
- When unsafe levels are detected:
    - Generates a **warning email** to subscribed users.
    - Uses an **AI module** to create **personalized prevention tips** 
    - Sends emails via the configured SMTP server.

### 🔹 Sensor Subscription System
- Users can **subscribe** to one or more sensors.
- Subscribed users receive live updates and warning alerts.
- Supports unsubscription and re-subscription via API.

### 🔹 Community Sensor Requests
- Members of the BreathSafe community can **request new sensor installations** in specific areas (e.g., a school, children park).
- Admins can review and approve or reject requests.
- Helps expand sensor coverage based on real community demand.

---
