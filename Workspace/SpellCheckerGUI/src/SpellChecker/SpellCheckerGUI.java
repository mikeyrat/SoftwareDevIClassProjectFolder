package SpellChecker;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Spell checking with GUI class for assignment 7
 * @author/ Michael Forman
 * @date 10/18/2018
 */
public class SpellCheckerGUI extends Application {
	
	private Label fileToCheckField = new Label("No file selected.");
	private Label dictionaryFileField = new Label("No file selected.");
	private Label fileToCheckLabel = new Label(" File to Spell Check: ");
	private Label resultsLabel = new Label("Spell Check Results:");
	private Button chooseFileButton = new Button("Open File to Check...  ");
	private Button dictionaryButton = new Button("Open Dictionary File...");
	private Label dictionaryfileLabel = new Label("Dictionary Filename: ");
	private Button checkButton = new Button("    Check    ");
	private Button exitButton = new Button("   Exit   ");
	ArrayList<String> fileWords = new ArrayList<String>();
	ArrayList<String> dictionaryWords = new ArrayList<String>();
	static StringBuilder badWords = new StringBuilder();
	private TextArea badWordArea = new TextArea();
	private boolean checkFileThere = false;
	private boolean dictFileThere = false;
	
	/**
	 * Main method just launches start
	 * @param args not used
	 */
	public static void main(String[] args) {
		launch(args);
	}//end Main

	/**
	 * start method just launches establishes GUI, sets primary stage
	 * @param primaryStage main stage for GUI
	 */
	public void start(Stage primaryStage) throws Exception {
		
		primaryStage.setTitle("Spell Checker");
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Files", "*.txt", "*.text");
		fileChooser.getExtensionFilters().add(extFilter);
		checkButton.setDisable(true);
		
		/**
		 * event handler for button to choose which file to check for errors
		 */
		chooseFileButton.setOnAction (new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				fileWords.clear(); //clear array in case old data exists
				checkButton.setDisable(true); //disables "Check" button since no file is yet selected
				File mainWordFile = fileChooser.showOpenDialog(primaryStage); //gets file through regular open file interface
				if (mainWordFile !=null) { //if user selects a file, then put the name in the box and fill array
					fileToCheckField.setText(mainWordFile.getName());
				fileWords = fillArrayList(mainWordFile, fileWords); // send filetocheck to be read into array
				checkFileThere = true; //set switch indicating file to check is ready
				if (dictFileThere == true)//check to see if dict file is also ready
					checkButton.setDisable(false);//if so, turn on "Check" button
				}
				else {//if no file selected set text in field and exit handler
					fileToCheckField.setText("No File Selected");
				}
			}
			
		});//end chooseFileButton
		
		/**
		 * event handler for button to choose which file to use as dictionary
		 * Same comments apply to this handler's steps as above just differnet variable names
		 */
		dictionaryButton.setOnAction (new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				dictionaryWords.clear(); 
				checkButton.setDisable(true);
				File mainWordFile = fileChooser.showOpenDialog(primaryStage);
				if (mainWordFile !=null) {
				dictionaryFileField.setText(mainWordFile.getName());
				dictionaryWords = fillArrayList(mainWordFile, dictionaryWords); // send filetocheck to be read into array
				dictFileThere = true;
				if (checkFileThere == true)
					checkButton.setDisable(false);
				}
				else {
					dictionaryFileField.setText("No File Selected");
				}
			}
			
		});//end dictionaryButton
		
		/**
		 * event handler for "Check" button. Initiates for/each loop in array, fills stringbuilder and places in TextArea
		 */
		checkButton.setOnAction (new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				for (String fileWord : fileWords) {// iterates the to be checked file for bad words
					checkForTheWord(fileWord, dictionaryWords);
				}
				badWords.append("Checking Complete!\n");
				badWordArea.setText(badWords.toString());
				//System.exit(0);
				}
			});//end CheckButton
		
		/**
		 * event handler for "Exit" button. 
		 */
		exitButton.setOnAction (new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					System.exit(0);
				}
			});//end Exit Button
			
		 	final GridPane inputGridPane = new GridPane();
		 	final FlowPane outputGridPane = new FlowPane();
		 	final HBox  buttonGridPane = new HBox ();
		 	buttonGridPane.setPadding(new Insets(5));
		 	buttonGridPane.setSpacing(10);
		 	inputGridPane.setPadding(new Insets(10, 5, 5, 10));
		 	buttonGridPane.setPadding(new Insets(10, 5, 5, 10));
		 	outputGridPane.setPadding(new Insets(10, 5, 5, 10));
		 
		 
		 	GridPane.setConstraints(chooseFileButton, 0, 1);
	        GridPane.setConstraints(fileToCheckLabel, 1, 1);
	        GridPane.setConstraints(fileToCheckField, 2, 1);
	        GridPane.setConstraints(dictionaryButton, 0, 2);
	        GridPane.setConstraints(dictionaryfileLabel, 1, 2);
	        GridPane.setConstraints(dictionaryFileField, 2, 2);
	        
	        
	        inputGridPane.setHgap(5);
	        inputGridPane.setVgap(5);
	        outputGridPane.setHgap(5);
	        outputGridPane.setVgap(5);
	        inputGridPane.getChildren().addAll(chooseFileButton, dictionaryButton, fileToCheckLabel, dictionaryFileField, dictionaryfileLabel, fileToCheckField);
	        outputGridPane.getChildren().addAll(resultsLabel, badWordArea);
	        buttonGridPane.getChildren().addAll(checkButton, exitButton);
	        BorderPane borderPane = new BorderPane();
	        borderPane.setTop(inputGridPane);
	        borderPane.setCenter(outputGridPane);
	        borderPane.setBottom(buttonGridPane);
	        
	        final Pane mainPane = new VBox(12);
	        mainPane.getChildren().addAll(borderPane);
	        mainPane.setPadding(new Insets(12, 12, 12, 12));
	 
	        primaryStage.setScene(new Scene(mainPane));
	        primaryStage.show();
	}//end start
	
	/** method to fill arrays with either the words from the file to be checked or the dictionary
	 * @param fileToBeRead  target or dictionary file passed from event handler for either file
	 * @param arrayToBeFilled the target array to be filled, either the source document words or dictionary words
	 * @return the appropriate filled array 
	 */
	public static ArrayList<String> fillArrayList(File fileToBeRead, ArrayList<String> arrayToBeFilled) {
		try {// reads dictionary file into string array
			Scanner input = new Scanner(fileToBeRead);
			while (input.hasNextLine()) {
				String dictWord = input.nextLine();
				try {
					arrayToBeFilled.add(dictWord);
				} 
				catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
				}
			}
			input.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return arrayToBeFilled;
	}//end fillArrayList()
	
	/**
	 * method to check for the presence of the word in the dictionary
	 * @param theWord word to be checked against dictionary
	 */
	public static void checkForTheWord(String theWord, ArrayList<String> listToCheck) {
		if (listToCheck.contains(theWord) == true) {
			return;
		}
		badWords.append(theWord).append("\n");
		return;
	}//end checkForTheWord()
	
}//end Class
