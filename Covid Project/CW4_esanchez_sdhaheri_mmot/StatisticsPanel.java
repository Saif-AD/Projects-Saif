import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;

public class StatisticsPanel {
    private VBox panel;
    private Label titleLabel;
    private Label statisticLabel;
    private Button previousButton, nextButton;
    private CovidDataLoader dataLoader;

    private List<String> statistics = new ArrayList<>(); // Stores the data text for each statistic
    private int currentStatisticIndex = 0; // Counter to track which statistic is being displayed

    public StatisticsPanel(CovidDataLoader dataLoader) {
        this.dataLoader = dataLoader;
        createPanel(); // initializes the panel created.
        updateStatisticsDisplay(); // Updates the display with the data
    }

    //Creating the panel.
    private void createPanel() {
        panel = new VBox(20);
        panel.setAlignment(Pos.CENTER);

        titleLabel = new Label("Statistics");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        statisticLabel = new Label();
        statisticLabel.setStyle("-fx-font-size: 18px;");
        
        previousButton = new Button("<");
        nextButton = new Button(">");
        
        // Set up navigation through statistics.
        previousButton.setOnAction(e -> navigate(-1));
        nextButton.setOnAction(e -> navigate(1));
        
        HBox navigationButtons = new HBox(10, previousButton, nextButton);
        navigationButtons.setAlignment(Pos.CENTER);

        panel.getChildren().addAll(titleLabel, statisticLabel, navigationButtons);
    }

    // To navigate, we created this system with a direction to navigate through the statistics like a list. 
    private void navigate(int direction) {
        currentStatisticIndex += direction;
        if (currentStatisticIndex < 0) {
            currentStatisticIndex = 0; // Stops from going lower than the first statistic (google mobility)
        } else if (currentStatisticIndex >= statistics.size()) {
            currentStatisticIndex = statistics.size() - 1; // Stops from going over the last stat.
        }

        statisticLabel.setText(statistics.get(currentStatisticIndex)); // Updates the current statistic to the next one after the direction changes (- or +)
    }
    
    // Gets the stats depending on the dates provided by the user.
    public void setDateRange(LocalDate startDate, LocalDate endDate) {
        dataLoader.filterDataForDateRange(startDate, endDate);
        updateStatisticsDisplay();
    }
    
    // Getting the statistics and putting them in the list.
    private void updateStatisticsDisplay() {
        long averageGoogleMobility = calculateAverageGoogleMobility();
        long totalDeaths = calculateTotalDeaths();
        long totalCases = calculateTotalCases();
        double averageTotalCases = calculateAverageTotalCases();
        String maxCasesBorough = getMaxCasesBorough();
        String maxDeathsBorough = getMaxDeathsBorough();

        statistics.clear(); // Clear stats for debugging.
        
        statistics.add("Average Google Mobility: " + averageGoogleMobility);
        statistics.add("Total Deaths: " + totalDeaths);
        statistics.add("Total Cases: " + totalCases);
        statistics.add("Average Total Cases for all boroughs: " + averageTotalCases);
        statistics.add("Most Total Cases: " + maxCasesBorough);
        statistics.add("Most Total Deaths: " + maxDeathsBorough);

        // Show the first stat if the list is not empty.
        if (!statistics.isEmpty()) {
            statisticLabel.setText(statistics.get(0));
        }
    }

    // Iterates through all the data in getRetailRecreationGMR section, then adds it up to give an average.
    private long calculateAverageGoogleMobility() {
        List<CovidData> records = dataLoader.getFilteredRecords();
        long totalGoogleMobility = 0;
        for (CovidData record : records) {
            totalGoogleMobility += (record.getRetailRecreationGMR() + record.getParksGMR()) / 2.0;
        }
        if (records.isEmpty()) {
            return 0; // Returns 0 if there are no records to prevent division by zero
        } else {
            return totalGoogleMobility / records.size(); // Computes the average if there are records
        }
    }
    
    // Simple for loops to iterate through all the deaths and adding them up.
    private int calculateTotalDeaths() {
        List<CovidData> records = dataLoader.getFilteredRecords();
        int totalDeaths = 0;
        for (CovidData record : records) {
            totalDeaths += record.getTotalDeaths();
        }
        return totalDeaths; // Returns the sum of total deaths
    }
    
    private long calculateTotalCases() {
        List<CovidData> records = dataLoader.getFilteredRecords();
        long totalCases = 0;
        for (CovidData record : records) {
            totalCases += record.getTotalCases();
        }
        return totalCases; // Returns the sum of total deaths
    }
    
    // Same as above but dividing by the size of the list (records.size) to get the average.
    private long calculateAverageTotalCases() {
        List<CovidData> records = dataLoader.getFilteredRecords();
        long totalCases = 0;
        for (CovidData record : records) {
            totalCases += record.getTotalCases();
        }
        if (records.isEmpty()) {
            return 0; // Returns 0 if there are no records to prevent division by zero
        } else {
            return (long) totalCases / records.size(); // Computes the average if there are records
        }
    }
    
    // Made a list of the cases by borough, then iterated through them to get the borough with the most cases.
    // Used a map and put the borough name as the key and the cases as the value.
    private String getMaxCasesBorough() {
        List<CovidData> records = dataLoader.getFilteredRecords();
        Map<String, Integer> casesByBorough = new HashMap<>();
        for (CovidData record : records) {
            casesByBorough.put(record.getBorough(), casesByBorough.getOrDefault(record.getBorough(), 0) + record.getTotalCases());
        }
        
        String maxBorough = "Not available";
        int maxCases = -1;
        for (Map.Entry<String, Integer> entry : casesByBorough.entrySet()) {
            if (entry.getValue() > maxCases) {
                maxCases = entry.getValue();
                maxBorough = entry.getKey();
            }
        }
        return maxBorough; // Returns the borough with the most cases
    }
    
    // Same process for deaths.
    private String getMaxDeathsBorough() {
        List<CovidData> records = dataLoader.getFilteredRecords();
        Map<String, Integer> deathsByBorough = new HashMap<>();
        for (CovidData record : records) {
            deathsByBorough.put(record.getBorough(), deathsByBorough.getOrDefault(record.getBorough(), 0) + record.getTotalDeaths());
        }
        
        String maxBorough = "Not available";
        int maxDeaths = -1;
        for (Map.Entry<String, Integer> entry : deathsByBorough.entrySet()) {
            if (entry.getValue() > maxDeaths) {
                maxDeaths = entry.getValue();
                maxBorough = entry.getKey();
            }
        }
        return maxBorough; // Returns the borough with the most deaths
    }

    // Return the panel when getView() is called in the statistics button
    public VBox getView() {
        return panel;
    }
}
