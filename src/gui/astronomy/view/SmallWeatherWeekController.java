package gui.astronomy.view;

import gui.astronomy.GUIAstronomy;
import gui.astronomy.Preferences;
import gui.astronomy.api.ForecastAPI;
import gui.astronomy.api.Data;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;

public class SmallWeatherWeekController {
    
    private GUIAstronomy mainApp;
    private Preferences pref;
    private ForecastAPI forecast;
    
    // Labels for dates and days
    @FXML
    private Label date1, date2, date3, date4, date5, date6;
    @FXML
    private Label day2, day3, day4, day5, day6;
    // Labels for weather data
    @FXML
    private Label lunar1, lunar2, lunar3, lunar4, lunar5, lunar6;
    @FXML
    private Label cloud1, cloud2, cloud3, cloud4, cloud5, cloud6;
    @FXML
    private Label vis1, vis2, vis3, vis4, vis5, vis6;
    
    @FXML
    private ChoiceBox<String> viewLayoutSelection;
    
    @FXML
    private ComboBox<String> locationSelection;
    
    @FXML
    private void initialize(){
        
        // Displays dates and days of the week
        Format dateFormat = new SimpleDateFormat("MMM d");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);
        
        // Populates numeric dates for 5 days after the current day
        Label dates[] = {date1, date2, date3, date4, date5, date6};
        for(int i = 0; i < 6; i++) {
            dates[i].setText(dateFormat.format(new GregorianCalendar(year, month, day+i).getTime()));
        }
        
        // Populates days of the week for 5 days after the current day
        Format dayFormat = new SimpleDateFormat("EE");
        Label days[] = {day2, day3, day4, day5, day6};
        for(int i = 0; i < 5; i++) {
            days[i].setText(dayFormat.format(new GregorianCalendar(year, month, day+i+1).getTime()));
        }

        // Default location set to London
        showWeatherData("London");
        
        // Handle changes in layout
        viewLayoutSelection.getItems().addAll("This Week","Today");
        viewLayoutSelection.getSelectionModel().selectFirst();
        viewLayoutSelection.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                SmallWeatherWeekController.this.handleChoiceBox(newValue);
            }
        });
        
        // Handle changes in location (refreshes weather data if location is changed)
        locationSelection.getItems().addAll("London", "Dublin", "Edinburgh", "Berlin", "Brussels", "Copenhagen",
            "Barcelona", "Paris", "Madrid", "Rome", "Florence");
        locationSelection.setEditable(true); 
        locationSelection.getSelectionModel().selectFirst();
        locationSelection.valueProperty().addListener(new ChangeListener<String>(){
            public void changed(ObservableValue ov, String oldValue, String newValue){
                showWeatherData(newValue);
            }
        });
    }

    // Populates the necessary weather data (lunar phase, cloud coverage, visibility) for the next week
    // for the location provided in the argument
    private void showWeatherData(String location) {
        forecast = new ForecastAPI();
        forecast.setLocation(location);
        ObservableList<Data> weekData = forecast.getDaily();
        
        Label lunarPhases[] = {lunar1, lunar2, lunar3, lunar4, lunar5, lunar6};
        Label cloudCover[] = {cloud1, cloud2, cloud3, cloud4, cloud5, cloud6};
        Label visibility[] = {vis1, vis2, vis3, vis4, vis5, vis6};
        for(int i = 0; i < 6; i++) {
            lunarPhases[i].setText(forecast.getLunarPhaseName(i)[2].substring(0, forecast.getLunarPhaseName(i)[2].indexOf(".")));
            cloudCover[i].setText(weekData.get(i).getCloudCoverage());
            visibility[i].setText(weekData.get(i).getVisibility());
        }   
    }
    
    // Changes to hourly view
    private void handleChoiceBox(Number value){
        if (value.intValue() == 1){
            mainApp.showWeatherToday(pref);
        }
    }
    
    public void setMainApp(GUIAstronomy mainApp, Preferences pref) {
        this.mainApp = mainApp;
        this.pref = pref;
    }
    
    @FXML
    public void openPreferences(){
        mainApp.showPreferences();
    }
    
}