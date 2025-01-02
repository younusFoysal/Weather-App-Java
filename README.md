# Weather Information App

The Weather Information App is a JavaFX-based desktop application that fetches current weather data and a 5-day forecast for a user-specified location. The app also displays a dynamic background based on the time of day and keeps a history of search queries.

---


<p align="center">
  <img src="https://github.com/younusFoysal/Weather-App-Java/blob/main/images/screenshot.png" alt="Weather Information App">
</p>
<br>


## Features
1. **Current Weather Data**:
   - Displays temperature, humidity, wind speed, and weather conditions.
   - Fetches real-time weather information using the OpenWeatherMap API.
2. **5-Day Forecast**:
   - Shows weather forecasts for the next 5 days with temperature and conditions.
3. **Dynamic Background**:
   - Updates background colors based on the time of day (morning, afternoon, evening).
4. **Search History**:
   - Stores the user's previous searches with timestamps.
5. **Unit Toggle**:
   - Allows switching between Celsius and Fahrenheit for temperature display.

---

## Requirements
1. **Java Development Kit (JDK)** 8 or higher.
2. **JavaFX SDK**: Download from the [JavaFX website](https://openjfx.io/).
3. **Eclipse IDE** or any Java IDE of your choice.
4. **Internet Connection** for API requests.
5. **OpenWeatherMap API Key**: 
   - Sign up at [OpenWeatherMap](https://openweathermap.org/) to obtain a free API key.

---

## Setup in Eclipse
### 1. Clone or Download the Repository
- Clone the repository or download it as a ZIP file and extract it to a directory of your choice.

### 2. Import into Eclipse
1. Open Eclipse IDE.
2. Go to `File > Import`.
3. Select `Existing Projects into Workspace` and click `Next`.
4. Browse to the location of the project directory and click `Finish`.

### 3. Add Dependencies
The app uses **Gson** for parsing JSON. Ensure that the Gson library is available:
1. Download the Gson library from [Maven Central](https://mvnrepository.com/artifact/com.google.code.gson/gson).
2. Add the `gson-<version>.jar` file to your project's `Build Path`:
   - Right-click on the project in Eclipse.
   - Go to `Build Path > Configure Build Path > Libraries`.
   - Click `Add External JARs` and select the downloaded Gson JAR file.

### 4. Configure JavaFX SDK
1. Download the JavaFX SDK from the [JavaFX website](https://openjfx.io/).
2. Extract the downloaded ZIP file to a directory (e.g., `C:\Program Files\Java\javafx-sdk-23.0.1`).
3. Add the following VM arguments in Eclipse:
   - Right-click on the project in Eclipse and select `Run As > Run Configurations`.
   - Under the `Arguments` tab, in the **VM arguments** section, add:
     ```plaintext
     --module-path "C:\Program Files\Java\javafx-sdk-23.0.1\lib" --add-modules javafx.controls,javafx.fxml
     ```
   - Replace the path with the location where you installed the JavaFX SDK.

### 5. Replace the API Key
- Open `WeatherApp.java` and locate the line:
  ```java
  private final String API_KEY = "xxxxxxxx53216c355499fe373a8342";

- Replace the placeholder key with your OpenWeatherMap API key.


---

## Running the Application
1. Right-click on `WeatherApp.java` in the `weatherApp` package.
2. Select `Run As > Java Application`.
3. Ensure the VM arguments for JavaFX are correctly configured (see Step 4 above).
4. The application window should launch.

---

## How to Use
1. Enter a city name in the input box and click the **Search** button.
2. Toggle between Celsius and Fahrenheit using the radio buttons.
3. View:
   - **Current Weather**: Includes temperature, humidity, wind speed, and conditions.
   - **5-Day Forecast**: Lists weather data for the next 5 days.
4. Check your **Search History** at the bottom of the app to revisit previous searches.

---

## Implementation Details
- **JavaFX**: Used for the GUI design.
- **OpenWeatherMap API**: Fetches real-time weather and forecast data.
- **JSON Parsing**: Gson library processes the API response.
- **Dynamic Background**: Adjusts colors based on the current time.
- **Search History**: Maintained in a `List` and displayed using a `ListView`.

---

## Known Issues
- Ensure proper error handling for invalid city names or API errors.
- The app depends on an active internet connection for fetching weather data.

---

## Author
This app was developed as a JavaFX learning project. Contributions and suggestions are welcome!
```

Let me know if you need additional details or adjustments!
