import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert;
import java.io.IOException;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;

/**
 * This is the main classs of the prohect.It builds the whole interface with buttons, dates, and connects to 
 * all the other classes.
 * By: Eduardo Sanchez Morales (k23025983), Marc Mot (k23040798), Saif Al Dhaheri (k21210342)
 */ 

public class InterfaceApp extends Application {
    //defining the minimum and maximum dates according to the CSV file dates
    private final LocalDate startDate = LocalDate.parse("2020-02-14");
    private final LocalDate endDate = LocalDate.parse("2023-02-09");
    
    private DatePicker datePickerFrom;
    private DatePicker datePickerTo;
    private CovidDataLoader dataLoader;

    @Override
    public void start(Stage primaryStage) {
        
        dataLoader = new CovidDataLoader();  // Initiliaising the data loader
        dataLoader.load();  // loading the data
        
        BorderPane borderPane = new BorderPane();// Main view.
    
        Welcome welcomePanel = new Welcome();
        welcomePanel.setQuizAction(() -> showQuizPanel(borderPane));  //if we click on the Quiz btn, it shows the quiz.
        borderPane.setCenter(welcomePanel);
        
        
        datePickerFrom = new DatePicker(); //creating the date pickers.
        datePickerTo = new DatePicker();
        
        // The statistic button code
        Button statisticsButton = new Button("Statistics");
        statisticsButton.setOnAction(event -> {
            LocalDate fromDate = datePickerFrom.getValue();
            LocalDate toDate = datePickerTo.getValue();
            
            //if dates are not seletced, automatically set them to start date and end date.
            if (fromDate == null) fromDate = startDate;
            if (toDate == null) toDate = endDate;
        
            // filtering the data.
            dataLoader.filterDataForDateRange(fromDate, toDate);
            
            // setting up the statistics panel with the data selected.
            StatisticsPanel statisticsPanel = new StatisticsPanel(dataLoader);
            borderPane.setCenter(statisticsPanel.getView());
        });

        
        // Creating the date pickers
        DatePicker datePickerFrom = new DatePicker();
        DatePicker datePickerTo = new DatePicker();
        HBox datePickersBox = new HBox(5, datePickerFrom, datePickerTo); // 5px spacing
        datePickersBox.setAlignment(Pos.CENTER_LEFT); //used the .position library, found in Stack overflow to align the items int he pane.
        
        HBox topRightLayout = new HBox(statisticsButton);
        topRightLayout.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(topRightLayout, Priority.ALWAYS); // I had a problem keeping the stats button with the date pickers
        // because it was being overwritten, so i used the priority library to make sure it always stays there.
        
        // putting the elements in the top box.
        HBox topLayout = new HBox(datePickersBox, topRightLayout);
        HBox.setHgrow(datePickersBox, Priority.ALWAYS); 
        topLayout.setAlignment(Pos.CENTER);
        
        // Adding the buttons for Welcome and Map
        Button btnBack = new Button("Welcome");
        Button btnForward = new Button("Map");
        HBox navigationButtons = new HBox(5, btnBack, btnForward); // Added spacing
        navigationButtons.setAlignment(Pos.CENTER);
    
        borderPane.setTop(topLayout);
        borderPane.setBottom(navigationButtons);

        //date picker code to show the alert if the input is not withint he given range before.
        datePickerFrom.setOnAction(event -> {
            LocalDate fromDate = datePickerFrom.getValue();
            if (!isDateWithinRange(fromDate)) {
                showAlert();
                datePickerFrom.setValue(startDate);
            } else {
                
            }
        });
        
        datePickerTo.setOnAction(event -> {
            LocalDate toDate = datePickerTo.getValue();
            if (!isDateWithinRange(toDate)) {
                showAlert();
                datePickerTo.setValue(endDate);
            } else {
                
            }
        });

    
        btnForward.setOnAction(event -> {
            // loading map.fxml file.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("map.fxml")); // Ensure this points to your FXML file
            try {
                Pane mapView = loader.load();
                borderPane.setCenter(mapView); // make the map the new pane.
                MapController controller = loader.getController();
                controller.setDateRange(datePickerFrom.getValue(), datePickerTo.getValue());
        
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        
        //making sure the welcome button is always active so that we can always go back to the welcome page.
        btnBack.setOnAction(event -> showWelcomePanel(borderPane));
        
        //show the scene.
        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setTitle("London COVID-19 Statistics");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public void showWelcomePanel(BorderPane borderPane) {
            Welcome welcomePanel = new Welcome();
        welcomePanel.setQuizAction(() -> showQuizPanel(borderPane));
        borderPane.setCenter(welcomePanel);
    }   
    
    //boolean to check if the date range is ok.
    private boolean isDateWithinRange(LocalDate date) {
        return !(date.isBefore(startDate) || date.isAfter(endDate));
    }
    
    //the alert given when the dates are wrong.
    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Date Range Error");
        alert.setHeaderText(null);
        alert.setContentText("You can only put dates between 2020-02-14 to 2023-02-09.");
        alert.showAndWait();
    }
    
    public void showQuizPanel(BorderPane borderPane) {
        QuizPanel quizPanel = new QuizPanel();
        quizPanel.setBackAction(() -> showWelcomePanel(borderPane));
        borderPane.setCenter(quizPanel);
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    
}