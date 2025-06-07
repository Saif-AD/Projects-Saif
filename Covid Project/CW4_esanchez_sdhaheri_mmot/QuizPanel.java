import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.control.Button;

/**
 * Quiz Panel is the scene that holds the quiz.
 * By: Eduardo Sanchez Morales (k23025983), Marc Mot (k23040798), Saif Al Dhaheri (k21210342)
 */ 
public class QuizPanel extends VBox {
    private int CurrentQuestionNum = 0; // Keeps track of the current question number
    private int score = 0; // Keeps track of the user's score (+1 if they get it correct)
    private Text questionText; 
    private RadioButton[] options = new RadioButton[3]; // I used this library called RadioBUtton to make the multiple choice answers
    private Button submitButton; // Button to submit 
    private Button backButton; // Button to go back
    
    // Array of questions and their possible answers
    private String[][] questions = {
        {"What's a thing you can do to stop germs from spreading?", "Start Meowing", "Wash your hands", "Jump 6 times"},
        {"Where should you sneeze?", "Hand", "Person next to you", "Elbow"},
        {"Can animals get covid?", "Only cats", "Rarely but yes", "No"},
        {"How long should you wash your hands to kill 99.99999% of germs?", "Meow 10 times", "0.2 seconds", "Until you feel like stopping"},
        {"What should you wear to help protect against covid?", "A wizard hat", "A face mask", "Bunny ears"},
        {"When is it okay not to wear a mask during the pandemic?", "While swimming", "In a cult", "In a party"},
        {"If you feel sick, what should you do?", "Go to Heaven", "Stay home", "Go to PPA lecture"},
        {"What are the chances of getting covid after being in a room with someone that has covid?", "0.002", "8", "I don't know"},
        {"What should you do with used tissues?", "Eat them", "Throw them in the bin", "Keep them for later"},
        {"Which of these is NOT a COVID-19 symptom?", "I start meowing too often", "Sore throat", "Increased appetite"}
    };
    private int[] answers = {1, 2, 2, 0, 1, 0, 1, 2, 1, 2}; // Correct answers indices

    //Constructor
    public QuizPanel() {
        questionText = new Text(); //initliaizing the texts and 
        ToggleGroup group = new ToggleGroup(); // This makes sure that there is only 1 selection possible int he group

        // For each answer option, initialize them into the ToggleGroyps
        for (int i = 0; i < options.length; i++) {
            options[i] = new RadioButton();
            options[i].setToggleGroup(group);
        }

        // Initilizing buttons
        submitButton = new Button("Submit");
        submitButton.setOnAction(event -> checkAnswer());
        backButton = new Button("Back to Welcome");
        this.getChildren().addAll(questionText, options[0], options[1], options[2], submitButton, backButton);
        loadQuestion(); //load the questions
    }

    // Loads the current question and its answers
    private void loadQuestion() {
        if (CurrentQuestionNum < questions.length) {
            // Set the question text and answer to the first in the list
            questionText.setText(questions[CurrentQuestionNum][0]);

            for (int i = 0; i < options.length; i++) {
                options[i].setText(questions[CurrentQuestionNum][i + 1]);
                options[i].setSelected(false);
            }
        } else {
            // If the list is finished, finish the quiz
            finishQuiz();
        }
    }

    // Checks the current question with the current question answer, and if it is selected (the corresponding)
    //one in the array, then it is correct, so score +1. If not, then go to the next question.
    private void checkAnswer() {
        if (options[answers[CurrentQuestionNum]].isSelected()) {
            score++;
        }
        CurrentQuestionNum++;
        loadQuestion(); // load the next question
    }

    // Displaying the final result
    private void finishQuiz() {
        this.getChildren().clear();
        Text resultText = new Text(); //create a new text field

        // if the socre is more than 7 or 7, then the user passes.
        if (score >= 7) {
            resultText.setText("Congratulations! You are now capable of fighting COVID!");
            this.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
        } else { //if less, then they don't pass.
            resultText.setText("Please don't leave your house. You are a menace to society.");
            this.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
        }
        
        this.getChildren().add(resultText); // Show the resultText field.
    }

    // Sets the action for the back button
    public void setBackAction(Runnable backAction) {
        backButton.setOnAction(event -> backAction.run());
    }
}
    

 