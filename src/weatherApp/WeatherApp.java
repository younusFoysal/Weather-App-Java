package weatherApp;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

/**
 * WeatherApp is a JavaFX application that fetches and displays current weather
 * and 5-day forecast data for a specified city using the OpenWeatherMap API.
 */
public class WeatherApp extends Application {

    // API credentials and endpoints
    private final String API_KEY = "0890ffb45e53216c355499fe373a8342"; // Replace with your weather API key
    private final String API_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&units=%s&appid=" + API_KEY;
    private final String FORECAST_URL = "https://api.openweathermap.org/data/2.5/forecast?q=%s&units=%s&appid=" + API_KEY;

    // List to store search history
    private final List<String> searchHistory = new LinkedList<>();
    private final ToggleGroup unitToggleGroup = new ToggleGroup(); // Toggle group for unit selection

    @Override
    public void start(Stage primaryStage) {
        // Main layout using BorderPane
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(10));

        // Input Area - Contains text field, search button, and unit toggles
        HBox inputArea = new HBox(10);
        inputArea.setPadding(new Insets(10));
        inputArea.setAlignment(Pos.CENTER);

        TextField locationInput = new TextField();
        locationInput.setPromptText("Enter city name");
        locationInput.setPrefWidth(200);

        Button searchButton = new Button("Search");
        searchButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        RadioButton celsiusButton = new RadioButton("Celsius");
        RadioButton fahrenheitButton = new RadioButton("Fahrenheit");
        celsiusButton.setToggleGroup(unitToggleGroup);
        celsiusButton.setSelected(true); // Default to Celsius
        fahrenheitButton.setToggleGroup(unitToggleGroup);

        inputArea.getChildren().addAll(locationInput, searchButton, celsiusButton, fahrenheitButton);

        // Weather Display Area - Contains current weather and forecast sections
        VBox weatherDisplay = new VBox(15);
        weatherDisplay.setPadding(new Insets(20));
        weatherDisplay.setAlignment(Pos.TOP_CENTER);

        // Current Weather Section
        VBox currentWeather = new VBox(10);
        currentWeather.setAlignment(Pos.CENTER);
        currentWeather.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-padding: 15; -fx-background-radius: 10;");

        Label temperatureLabel = new Label();
        Label humidityLabel = new Label();
        Label windSpeedLabel = new Label();
        Label conditionsLabel = new Label();
        ImageView weatherIcon = new ImageView();
        weatherIcon.setFitHeight(100);
        weatherIcon.setFitWidth(100);

        // Styling labels for consistency
        String labelStyle = "-fx-font-size: 14px; -fx-font-weight: bold;";
        temperatureLabel.setStyle(labelStyle);
        humidityLabel.setStyle(labelStyle);
        windSpeedLabel.setStyle(labelStyle);
        conditionsLabel.setStyle(labelStyle);

        currentWeather.getChildren().addAll(weatherIcon, temperatureLabel, humidityLabel, windSpeedLabel, conditionsLabel);

        // Forecast Section
        VBox forecastSection = new VBox(10);
        forecastSection.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-padding: 15; -fx-background-radius: 10;");

        Label forecastLabel = new Label("5-Day Forecast");
        forecastLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ListView<String> forecastList = new ListView<>();
        forecastList.setPrefHeight(200);
        forecastList.setStyle("-fx-background-radius: 5;");

        forecastSection.getChildren().addAll(forecastLabel, forecastList);

        // Adding sections to weather display
        weatherDisplay.getChildren().addAll(currentWeather, forecastSection);

        // Search History Section
        VBox historySection = new VBox(10);
        historySection.setPadding(new Insets(10));
        historySection.setMaxHeight(150);

        Label historyLabel = new Label("Search History");
        historyLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        ListView<String> historyList = new ListView<>();
        historyList.setPrefHeight(100);

        historySection.getChildren().addAll(historyLabel, historyList);

        // Setting layout regions
        mainLayout.setTop(inputArea);
        mainLayout.setCenter(weatherDisplay);
        mainLayout.setBottom(historySection);

        // Search button event handler
        searchButton.setOnAction(event -> {
            String location = locationInput.getText().trim();
            String unit = celsiusButton.isSelected() ? "metric" : "imperial";

            if (!location.isEmpty()) {
                fetchWeatherData(location, unit, temperatureLabel, humidityLabel, windSpeedLabel, conditionsLabel, weatherIcon);
                fetchForecastData(location, unit, forecastList);
                searchHistory.add(0, location + " (" + LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")) + ")");
                historyList.getItems().setAll(searchHistory);
            } else {
                showAlert("Input Error", "Please enter a valid location.");
            }
        });

        // Setting the scene
        Scene scene = new Scene(mainLayout, 800, 700);
        setDynamicBackground(mainLayout); // Adjust background dynamically based on time

        primaryStage.setScene(scene);
        primaryStage.setTitle("Weather Information App");
        primaryStage.show();
    }

    /**
     * Fetches current weather data from the OpenWeatherMap API.
     */
    private void fetchWeatherData(String location, String unit, Label temperatureLabel, Label humidityLabel, Label windSpeedLabel, Label conditionsLabel, ImageView weatherIcon) {
        try {
            String urlString = String.format(API_URL, location, unit);
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
                JsonObject main = jsonResponse.getAsJsonObject("main");
                JsonObject wind = jsonResponse.getAsJsonObject("wind");
                String weatherCondition = jsonResponse.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();

                temperatureLabel.setText("Temperature: " + main.get("temp").getAsDouble() + (unit.equals("metric") ? " °C" : " °F"));
                humidityLabel.setText("Humidity: " + main.get("humidity").getAsInt() + "%");
                windSpeedLabel.setText("Wind Speed: " + wind.get("speed").getAsDouble() + (unit.equals("metric") ? " m/s" : " mph"));
                conditionsLabel.setText("Condition: " + weatherCondition);

                String iconCode = jsonResponse.getAsJsonArray("weather").get(0).getAsJsonObject().get("icon").getAsString();
                String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
                weatherIcon.setImage(new Image(iconUrl));
            } else {
                showAlert("API Error", "Failed to fetch weather data. Please try again.");
            }
        } catch (Exception e) {
            showAlert("Error", "An error occurred: " + e.getMessage());
        }
    }

    /**
     * Fetches 5-day forecast data from the OpenWeatherMap API.
     */
    private void fetchForecastData(String location, String unit, ListView<String> forecastList) {
        try {
            String urlString = String.format(FORECAST_URL, location, unit);
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
                forecastList.getItems().clear();

                if (jsonResponse.has("list")) {
                    var forecastArray = jsonResponse.getAsJsonArray("list");
                    for (int i = 0; i < Math.min(5, forecastArray.size()); i++) {
                        JsonObject forecast = forecastArray.get(i).getAsJsonObject();
                        JsonObject mainData = forecast.getAsJsonObject("main");
                        JsonObject weatherData = forecast.getAsJsonArray("weather").get(0).getAsJsonObject();

                        String dateTime = forecast.get("dt_txt").getAsString();
                        double temp = mainData.get("temp").getAsDouble();
                        String condition = weatherData.get("description").getAsString();

                        String forecastEntry = String.format("%s: %.1f%s - %s",
                                dateTime,
                                temp,
                                unit.equals("metric") ? "°C" : "°F",
                                condition);

                        forecastList.getItems().add(forecastEntry);
                    }
                }

                if (forecastList.getItems().isEmpty()) {
                    forecastList.getItems().add("No forecast data available");
                }
            } else {
                showAlert("API Error", "Failed to fetch forecast data. Response code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to fetch forecast data: " + e.getMessage());
        }
    }

    /**
     * Sets a dynamic background for the application based on the time of day.
     */
    private void setDynamicBackground(BorderPane layout) {
        LocalTime currentTime = LocalTime.now();
        String style;

        if (currentTime.isBefore(LocalTime.NOON)) {
            style = "-fx-background-color: linear-gradient(to bottom, #87CEEB, #E0FFFF);"; // Morning theme
        } else if (currentTime.isBefore(LocalTime.of(18, 0))) {
            style = "-fx-background-color: linear-gradient(to bottom, #FF7F50, #FFA07A);"; // Afternoon theme
        } else {
            style = "-fx-background-color: linear-gradient(to bottom, #daeff8, #f5f5f5);"; // Evening theme
        }

        layout.setStyle(style);
    }

    /**
     * Displays an alert with a specified title and message.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Main method to launch the JavaFX application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
