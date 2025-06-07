import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import com.opencsv.CSVReader;
import java.net.URISyntaxException;
import java.util.Random;
import java.util.List;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Loads all the data from the CSV file with the Covid Cases.
 * By: Eduardo Sanchez Morales (k23025983), Marc Mot (k23040798), Saif Al Dhaheri (k21210342)
 */ 

public class CovidDataLoader {
    
    private ArrayList<CovidData> records;
    private List<CovidData> allRecords = new ArrayList<>(); //all data is stored in this array 
    private List<CovidData> filteredRecords = new ArrayList<>(); //all filtered data is stored in this array
    
    public CovidDataLoader() {
        this.records = new ArrayList<>();
        load(); 
    }
 
        /** 
         * Return an ArrayList containing the rows in the Covid London data set csv file.
         */
    public void load() {
        System.out.println("Begin loading Covid London dataset...");
        try {
            URL url = getClass().getResource("covid_london.csv");
            if (url == null) {
                throw new IOException("The CSV file could not be found.");
            }
            try (CSVReader reader = new CSVReader(new FileReader(new File(url.toURI()).getAbsolutePath()))) {
                String[] line;
                reader.readNext(); // Skips the header row
    
                while ((line = reader.readNext()) != null) {
                    String date = line[0];
                    String borough = line[1];
                    int retailRecreationGMR = convertInt(line[2]);
                    int groceryPharmacyGMR = convertInt(line[3]);
                    int parksGMR = convertInt(line[4]);
                    int transitGMR = convertInt(line[5]);
                    int workplacesGMR = convertInt(line[6]);
                    int residentialGMR = convertInt(line[7]);
                    int newCases = convertInt(line[8]);
                    int totalCases = convertInt(line[9]);
                    int newDeaths = convertInt(line[10]);
                    int totalDeaths = convertInt(line[11]);
    
                    CovidData record = new CovidData(date, borough, retailRecreationGMR,
                        groceryPharmacyGMR, parksGMR, transitGMR, workplacesGMR,
                        residentialGMR, newCases, totalCases, newDeaths, totalDeaths);
    
                    this.records.add(record); // Adds to the class field, not a local variable
                }
            }
        } catch (IOException | URISyntaxException e) {
            System.out.println("An error occurred while loading the CSV data.");
            e.printStackTrace();
        }
    
        this.allRecords = new ArrayList<>(this.records); // Copy the loaded records to allRecords
        System.out.println("Number of Loaded Records: " + this.allRecords.size());
    }
    
    public List<CovidData> getFilteredRecords() {
        return new ArrayList<>(filteredRecords);
    }
    
    /**
         *
         * @param doubleString the string to be converted to Double type
         * @return the Double value of the string, or -1.0 if the string is 
         * either empty or just whitespace
         */
        private Double convertDouble(String doubleString){
            if(doubleString != null && !doubleString.trim().equals("")){
                return Double.parseDouble(doubleString);
            }
            return 0.0;
        }

    /**
     *
     * @param intString the string to be converted to Integer type
     * @return the Integer value of the string, or -1 if the string is 
     * either empty or just whitespace
     */
    private Integer convertInt(String intString){
        if(intString != null && !intString.trim().equals("")){
            return Integer.parseInt(intString);
        }
        return 0;
    }

    
    public void filterDataForDateRange(LocalDate startDate, LocalDate endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        filteredRecords.clear();
    
        System.out.println("Filtering between " + startDate + " and " + endDate);
    
        for (CovidData record : allRecords) {
            try {
                LocalDate date = LocalDate.parse(record.getDate(), formatter);
                System.out.println("Record date: " + date);
    
                if (!date.isBefore(startDate) && !date.isAfter(endDate)) {
                    filteredRecords.add(record);
                }
            } catch (DateTimeParseException e) {
                System.err.println("Error parsing date: " + record.getDate());
            }
        }
    
        System.out.println("Filtered " + filteredRecords.size() + " records for date range.");
    }
    
    public List<CovidData> getDataForBorough(String boroughName) {
        List<CovidData> boroughData = filteredRecords.stream()
            .filter(record -> record.getBorough().equalsIgnoreCase(boroughName))
            .collect(Collectors.toList());
    
        System.out.println("Retrieved " + boroughData.size() + " records for the borough: " + boroughName);
    
        return boroughData;
    }

    public int getDeathRateForBorough(String boroughName) {
    int totalDeaths = 0; // Initialize total deaths to 0

    // Loop through each record in the filtered records
    for (CovidData record : filteredRecords) {
        // Check if the current record's borough matches the provided borough name
        if (record.getBorough().equalsIgnoreCase(boroughName)) {
            // Add the new deaths from the current record to the total deaths
            totalDeaths += record.getNewDeaths();
        }
    }

    // Return the total deaths for the borough
    return totalDeaths;
}


}
