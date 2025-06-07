import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.control.Button;

/**
 * This is welcome scene. Very simple, we hardcoded it since we thought it would be easier than using Scene Buidler.
 * By: Eduardo Sanchez Morales (k23025983), Marc Mot (k23040798), Saif Al Dhaheri (k21210342)
 */ 

public class Welcome extends VBox {
    // Text fields and a button for the welcome screen
    private Text welcomeText; 
    private Text dateRangeText; 
    private Button quizButton; 

    // Constructor making all the buttons and adding the, to the welcome VBOX
    public Welcome() {
        welcomeText = new Text("Welcome to the London COVID-19 Statistics Explorer. Please select a date range to begin.");
        dateRangeText = new Text(); 
        quizButton = new Button("Quiz");
        this.getChildren().addAll(welcomeText, dateRangeText, quizButton);
    }

    // Updating the date range. 
    public void updateDateRange(String fromDate, String toDate) {
        dateRangeText.setText("Selected Date Range: " + fromDate + " - " + toDate);
    }
    
    // Method that runs the quiz panel.
    public void setQuizAction(Runnable quizAction) {
        quizButton.setOnAction(event -> quizAction.run());
    }
    
    // Prints the date ranges selected. 
    public void setDateRange(String fromDate, String toDate) {
        welcomeText.setText("Welcome to the London COVID-19 Statistics Explorer.\nSelected Date Range: " + fromDate + " - " + toDate);
    }
}