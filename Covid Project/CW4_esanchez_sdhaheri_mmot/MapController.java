import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import java.time.LocalDate;
import java.util.List;
import javafx.scene.control.Button;

/**
 * This is the Map controller with all the backend code for the map. The Map is an FXML file built using
 * scene builder. 
 * By: Eduardo Sanchez Morales (k23025983), Marc Mot (k23040798), Saif Al Dhaheri (k21210342)
 */ 

public class MapController {
    @FXML
    //creating all the polygons from the FXML file.
    private Polygon ENFI, WALT, HRGY, BARN, HRRW, BREN, CAMD, ISLI, HACK, REDB, HAVE, HILL, EALI, KENS, WSTM, TOWH, NEWH, BARK, HOUN, HAMM, WAND, CITY, GWCH, BEXL, RICH, MERT, LAMB, STHW, LEWS, KING, SUTT, CROY, BROM;
    private CovidDataLoader dataLoader = new CovidDataLoader(); //loading the Covid Data from Covid Data Loader class
    private LocalDate startDate; //creating the dates for filtering
    private LocalDate endDate;
    @FXML
    private Button welcomePageButton;

     // This method setting the date range and updates the map with new data.
    public void setDateRange(LocalDate start, LocalDate end) {
        this.startDate = start;
        this.endDate = end;
        dataLoader.filterDataForDateRange(start, end);
        updateBoroughColors();
    }

    // This method is called when the map is first shown. It sets up the map with today's data.
    public void initialize() {
        LocalDate now = LocalDate.now();
        dataLoader.filterDataForDateRange(now, now);
        updateBoroughColors();
    }

    // Setting up up each borough shape on the map.
    private void setupPolygon(Polygon polygon, String boroughName) {
        polygon.setOnMouseClicked(event -> showBoroughData(boroughName));
        setBoroughColor(polygon, boroughName);
    }

    //updating the colors to their corresponding borough name using the for loop
    private void updateBoroughColors() {
        // Setup all boroughs
        Polygon[] boroughs = {ENFI, BARN, HRGY, WALT, HRRW, BREN, CAMD, ISLI, HACK, REDB, HAVE, HILL, EALI, KENS, WSTM, TOWH, NEWH, BARK, HOUN, HAMM, WAND, CITY, GWCH, BEXL, RICH, MERT, LAMB, STHW, LEWS, KING, SUTT, CROY, BROM};
        String[] names = {"Enfield", "Barnet", "Haringey", "Waltham Forest", "Harrow", "Brent", "Camden", "Islington", "Hackney", "Redbridge", "Havering", "Hillingdon", "Ealing", "Kensington and Chelsea", "Westminster", "Tower Hamlets", "Newham", "Barking and Dagenham", "Hounslow", "Hammersmith and Fulham", "Wandsworth", "City of London", "Greenwich", "Bexley", "Richmond upon Thames", "Merton", "Lambeth", "Southwark", "Lewisham", "Kingston upon Thames", "Sutton", "Croydon", "Bromley"};

        for (int i = 0; i < boroughs.length; i++) {
            setupPolygon(boroughs[i], names[i]);
        }
    }
    
    //this method shows the data we want to retrieve.

        private void showBoroughData(String boroughName) {
        List<CovidData> boroughData = dataLoader.getDataForBorough(boroughName);
    
        int totalNewCases = 0;
        int totalCases = 0;
        int totalNewDeaths = 0;
        int totalDeaths = 0;
    
        // Calculating totals by iterating and adding
        for (CovidData data : boroughData) {
            totalNewCases += data.getNewCases();
            totalCases += data.getTotalCases();
            totalNewDeaths += data.getNewDeaths();
            totalDeaths += data.getTotalDeaths();
        }
    
        // Calculating the death rate
        double deathRate = totalDeaths > 0 ? (double) totalNewDeaths / totalDeaths * 100 : 0;
    
        // Showing the scene
        Stage stage = new Stage();
        stage.setScene(createScene(boroughName, totalNewCases, totalCases, totalNewDeaths, totalDeaths, deathRate));
        stage.setTitle("COVID-19 Data for " + boroughName);
        stage.show();
    }

    
    //creating the scene to display the data in the little window that pops up. Uses all the vairables from the data.
    private Scene createScene(String boroughName, int totalNewCases, int totalCases, int totalNewDeaths, int totalDeaths, double deathRate) {
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(createTopBox());
        borderPane.setLeft(createComboBox(totalNewCases, totalCases, totalNewDeaths, totalDeaths, deathRate));
        return new Scene(borderPane, 400, 200);
    }

    // Creating the combo box displaying the statistics.
    private HBox createComboBox(int totalNewCases, int totalCases, int totalNewDeaths, int totalDeaths, double deathRate) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(
            "New COVID cases: " + totalNewCases,
            "Total COVID cases: " + totalCases,
            "New COVID deaths: " + totalNewDeaths,
            "Total COVID Deaths: " + totalDeaths,
            String.format("COVID Death Rate: %.2f%%", deathRate)
        );
    
    
        HBox comboBoxBox = new HBox(comboBox);
        comboBoxBox.setPadding(new Insets(10));
        comboBoxBox.setAlignment(Pos.TOP_LEFT);
        return comboBoxBox;
    }

    private Scene createScene(String boroughName, double deathRate) {
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(createTopBox());
        borderPane.setLeft(createComboBox(deathRate));
        return new Scene(borderPane, 400, 200);
    }
    
    // Create the combo box displaying the statistics.
    private HBox createTopBox() {
        Label dateRangeLabel = new Label("Date Range: " + startDate + " to " + endDate);
        HBox topBox = new HBox(dateRangeLabel);
        topBox.setPadding(new Insets(10));
        topBox.setAlignment(Pos.TOP_CENTER);
        return topBox;
    }

    //showcasing the deathrate box.
    private HBox createComboBox(double deathRate) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("COVID Death Rate: " + String.format("%.2f%%", deathRate));
        HBox comboBoxBox = new HBox(comboBox);
        comboBoxBox.setPadding(new Insets(10));
        comboBoxBox.setAlignment(Pos.TOP_LEFT);
        return comboBoxBox;
    }
    
    //to calculate the deathrate, we got the some of the total new deaths, divided by the total deaths *100. 
    private double calculateDeathRate(List<CovidData> boroughData) {
        int totalNewDeaths = boroughData.stream().mapToInt(CovidData::getNewDeaths).sum();
        int totalDeaths = boroughData.stream().mapToInt(CovidData::getTotalDeaths).sum();
        return totalDeaths > 0 ? (double) totalNewDeaths / totalDeaths * 100 : 0;
    }
    //accepts value of deathrate variable and, depending on it, (RGB) R changes. The rest stay 0 so it is only 
    //between red shades.  If it's low, the color will be closer to black.
    private Color getColorForDeathRate(int deathRate) {
        double intensity = Math.min(1, deathRate / 10.0);
        return Color.color(intensity, 0, 0);
    }
    
    // Set the color for a borough based on its death rate.
    private void setBoroughColor(Polygon polygon, String boroughName) {
        if (polygon != null) {
            int deathRate = dataLoader.getDeathRateForBorough(boroughName);
            Color color = getColorForDeathRate(deathRate);
            polygon.setFill(color);
        }
    }

    //when the welcome page is clicked, it takes us to the welcome page.
    public void setWelcomePageAction(Runnable welcomePageAction) {
        welcomePageButton.setOnAction(event -> welcomePageAction.run());
    }
}
    