# Weather Analysis Module (Spring Boot)

An enterprise-grade, end-to-end Spring Boot microservice designed to fetch, cache, and analyze real-time weather data. This module demonstrates optimized memory management using concurrent data structures, clean data processing via the Java Stream API, automated background cache maintenance, and a robust global exception handling layer.

---

## 🚀 Key Features & Architecture

* **Decoupled Multi-Layer Architecture:** Follows clean enterprise patterns with clear separation across Controller, Service, Model, DTO, and Exception boundaries.
* **High-Performance In-Memory Caching:** Utilizes a thread-safe `ConcurrentHashMap` to store high-speed weather lookups, minimizing external network latency.
* **Advanced Data Processing:** Leverages the modern **Java Stream API** to execute declarative filtering, mapping, and high-humidity threshold analysis.
* **Automated Cache Maintenance:** Implements an asynchronous `@Scheduled` background task running an eviction algorithm to proactively wipe out stale data (entries older than 15 minutes) and prevent memory leaks.
* **Unified Error Boundaries:** Implements a global `@RestControllerAdvice` exception layer that intercepts runtime faults (e.g., city missing, infrastructure timeouts) and transforms them into clean, standardized JSON responses.

---

## 🛠️ Tech Stack

* **Backend Framework:** Spring Boot 3.x
* **Java Version:** Java 17 / 21
* **Build Tool:** Maven (Wrapper included)
* **Core APIs:** Java Streams, Concurrency Utilities, Spring Scheduling, RestTemplate

---

## ⚙️ Configuration & Installation

### 1. Prerequisites
Ensure you have Java 17 (or higher) and Maven installed on your local environment.

### 2. Properties Setup
The module is built with property fallbacks to ensure environment stability even without a live external API key. To connect to a live weather stream, update `src/main/resources/application.properties`:

```properties
weather.api.url=[https://api.openweathermap.org/data/2.5/weather](https://api.openweathermap.org/data/2.5/weather)
weather.api.key=YOUR_OPENWEATHERMAP_API_KEY
3. Build & PackageTo run a clean compilation and bundle the application into an executable JAR file, run the following command from the root directory:Bash./mvnw clean package -DskipTests
🔌 API Endpoints1. Fetch & Cache Weather DataSends a request to pull the current data for a specific location. If valid, maps the payload and updates the in-memory cache.URL: /api/weather/{city}Method: POSTSample Response:JSON{
  "city": "London",
  "temperature": 15.4,
  "humidity": 82,
  "lastUpdated": 1715789412000
}
2. Stream-Based High Humidity AnalysisAnalyzes cached data using Stream pipelines to extract all metrics exceeding a requested threshold level.URL: /api/weather/high-humidityMethod: GETURL Parameters: level=[integer] (e.g., ?level=80)Sample Response:JSON[
  {
    "city": "London",
    "temperature": 15.4,
    "humidity": 82,
    "lastUpdated": 1715789412000
  }
]
🧹 Background Maintenance (Eviction Logic)To maintain a lightweight memory footprint, a scheduled background worker wakes up every 60 seconds to clean the cache. It processes the dataset as a stream, applying the following eviction logic:$$\text{Current Time} - \text{Record Last Updated Time} > 15\text{ minutes}$$Any cache record fulfilling this condition is stripped from memory, ensuring the service remains highly performant over extended lifecycles.
