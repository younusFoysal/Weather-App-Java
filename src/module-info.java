/**
 * 
 */
/**
 * 
 */
module WeatherApp {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires com.google.gson;

    opens weatherApp to javafx.graphics;
}
