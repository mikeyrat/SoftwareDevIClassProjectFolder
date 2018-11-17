package SpellChecker;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
 * Modified Spell checking with GUI class for assignment 11 - uses MySQL database for critical functions
 * @author/ Michael Forman
 * @date 11/17/2018
 */
public class SpellCheckerGUI extends Application {
	
	private Label fileToCheckField = new Label("No file selected.");
	private Label dictionaryFileField = new Label("No file selected.");
	private Label fileToCheckLabel = new Label(" File to Spell Check: ");
	private Label resultsLabel = new Label("Spell Check Results:");
	private Button chooseFileButton = new Button("Open File to Check...  ");
	private Button dictionaryButton = new Button("Open Dictionary File...");
	private Label dictionaryfileLabel = new Label("Dictionary Filename: ");
	private Button checkButton = new Button("Check Using Words In Database   ");
	private Button checkButtonTwo = new Button("Check Using Words Loaded From Database");
	private Button exitButton = new Button("   Exit   ");
	ArrayList<String> fileWords = new ArrayList<String>();
	ArrayList<String> dictionaryWords = new ArrayList<String>();
	static StringBuilder badWords = new StringBuilder();
	private static TextArea badWordArea = new TextArea();
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
		checkButtonTwo.setDisable(true);
		
		/**
		 * event handler for button to choose which file to check for errors
		 */
		chooseFileButton.setOnAction (new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				fileWords.clear(); //clear array in case old data exists
				checkButton.setDisable(true); //disables "Check" button since no file is yet selected
				checkButtonTwo.setDisable(true);
				File mainWordFile = fileChooser.showOpenDialog(primaryStage); //gets file through regular open file interface
				if (mainWordFile !=null) { //if user selects a file, then put the name in the box and fill array
					fileToCheckField.setText(mainWordFile.getName());
				try {
					fileWords = fillArrayListFile(mainWordFile, fileWords);
				} catch (Exception e1) {
					e1.printStackTrace();
				} // send filetocheck to be read into array
				checkFileThere = true; //set switch indicating file to check is ready
				if (dictFileThere == true) {//check to see if dict file is also ready
					checkButton.setDisable(false);//if so, turn on "Check" button
				checkButtonTwo.setDisable(false);
				}
				}
				else {//if no file selected set text in field and exit handler
					fileToCheckField.setText("No File Selected");
				}
			}
		});//end chooseFileButton
		
		/**
		 * event handler for button to choose which file to use as dictionary
		 * Same comments apply to this handler's steps as above just different variable names
		 */
		dictionaryButton.setOnAction (new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				dictionaryWords.clear(); 
				checkButton.setDisable(true);
				checkButtonTwo.setDisable(true);
				File mainWordFile = fileChooser.showOpenDialog(primaryStage);
				if (mainWordFile !=null) {
				dictionaryFileField.setText(mainWordFile.getName());
				try {
					createFillTable(mainWordFile);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				try {
					dictionaryWords = fillArrayListDB(dictionaryWords);
				} catch (Exception e2) {
					e2.printStackTrace();
				} 
				dictFileThere = true;
				if (checkFileThere == true) {
					checkButton.setDisable(false);
					checkButtonTwo.setDisable(false);
				}
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
				badWords.setLength(0);
				badWords.append("Checking using SQL queries to database...\n");
				badWordArea.setText(badWords.toString());
				long startTime = System.currentTimeMillis();
				for (String fileWord : fileWords) {// iterates the to be checked file for bad words
					checkForTheWordDB(fileWord);
				}
				long endTime = System.currentTimeMillis();
				badWords.append("Checking Complete!\n");
				badWords.append("Checking words using database SQL queries took " + (endTime - startTime) + " milliseconds.\n");
				badWordArea.setText(badWords.toString());
				//System.exit(0);
				}
			});//end CheckButton
		
		/**
		 * event handler for "CheckButtonTwo" button. Initiates for/each loop in array, fills stringbuilder and places in TextArea
		 */
		checkButtonTwo.setOnAction (new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				badWords.setLength(0);
				badWords.append("Checking using Array loaded from Database...\n");
				badWordArea.setText(badWords.toString());
				long startTime = System.currentTimeMillis();
				for (String fileWord : fileWords) {// iterates the to be checked file for bad words
					checkForTheWordArray(fileWord, dictionaryWords);
				}
				long endTime = System.currentTimeMillis();
				badWords.append("Checking Complete!\n");
				badWords.append("Checking words using pre-filled array took " + (endTime - startTime) + " milliseconds.\n");
				badWordArea.setText(badWords.toString());
				//System.exit(0);
				}
			});
		/**
		 * event handler for "Exit" button. 
		 */
		exitButton.setOnAction (new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					deleteWords();
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
	        buttonGridPane.getChildren().addAll(checkButton, checkButtonTwo, exitButton);
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
	
	/** Method to open the connection to the database for other methods
	 * @return connection to the database
	 * @throws Exception hope it never does tho...
	 */
	public static Connection getConnection() throws Exception {
		try {
			//String driver  = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://localhost:3306/spelling";
			String username = "spellinguser";
			String password = "wordspass";
			Connection  conn = DriverManager.getConnection(url, username, password);
			return conn;
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return null;
	}//End getConnection
	
	/** Method to check if table exists, if not create it, once created, then fill it with dictionary words
	 * @param fileToBeRead dictionary file with which to fill database table word
	 * @throws Exception hope it never pops up
	 */
	public static void createFillTable(File fileToBeRead) throws Exception {
		badWords.append("Creating and Inserting words into Table...\n");
		badWordArea.setText(badWords.toString());
		long startTime = System.currentTimeMillis();
		try {// reads dictionary file into string array
			Scanner input = new Scanner(fileToBeRead);
			Connection con = getConnection();
			PreparedStatement create = con.prepareStatement("CREATE TABLE IF NOT EXISTS word(wordId int NOT NULL AUTO_INCREMENT, word varchar(45), PRIMARY KEY(wordId))");
			create.executeUpdate();
			while (input.hasNextLine()) {
				String dictWord = input.nextLine();
				try {
					PreparedStatement posting = con.prepareStatement("INSERT INTO word (word) VALUES ('"+dictWord+"')");
					posting.executeUpdate();
				} 
				catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
				}
			}
			input.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		long endTime = System.currentTimeMillis();
		badWords.append("Creating and Inserting into the table took " + (endTime - startTime) + " milliseconds.\n");
		badWordArea.setText(badWords.toString());
		return;
	}//end createFillTable()
	
	/** 
	 * method to simply clear the dictionary out of the database when user hits the exit button
	 */
	public static void deleteWords() {
	try {
		Connection con = getConnection();
		PreparedStatement create = con.prepareStatement("TRUNCATE TABLE word");
		create.executeUpdate();
		} catch (Exception a){
			System.out.println(a.getMessage());
		}
	}
	
	/** method to fill array list with the file to be checked from a text file
	 * @param fileToBeRead the textfile with the file to be checked
	 * @param arrayToBeFilled arraylist containing the words int he file
	 * @return array list to be checked
	 */
	public static ArrayList<String> fillArrayListFile(File fileToBeRead, ArrayList<String> arrayToBeFilled) {
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
	
	/** Method to read the dictionary file from the database and return a filled array.
	 * @param arrayToBeFilled from the database using SQL query
	 * @return the filled ArrayList
	 * @throws Exception
	 */
	public static ArrayList<String> fillArrayListDB(ArrayList<String> arrayToBeFilled) throws Exception {
		badWords.append("Creating Array and Loading From Database...\n");
		badWordArea.setText(badWords.toString());
		long startTime = System.currentTimeMillis();
				try {
					Connection con = getConnection();
					PreparedStatement checkWord = con.prepareStatement("SELECT * FROM word");
					ResultSet result = checkWord.executeQuery();
					while (result.next()){
						
					arrayToBeFilled.add(result.getString("word"));
					}
					System.out.println();
				} 
				catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
				}
				long endTime = System.currentTimeMillis();
				badWords.append("Loading array took " + (endTime - startTime) + " milliseconds.\n");
				badWordArea.setText(badWords.toString());
		return arrayToBeFilled;
	}//end fillArrayList()
	
	/**
	 * method to check for the presence of the word in the database dictionary using SQL searches.
	 * @param theWord word to be checked against dictionary
	 * 
	 */
	
	public static void checkForTheWordDB(String theWord) {
		try {
			Connection con = getConnection();
			PreparedStatement checkWord = con.prepareStatement("SELECT * FROM word WHERE word = '"+theWord+"' LIMIT 1");
			ResultSet result = checkWord.executeQuery();
			while (result.next()) {
			String compareWord = result.getString("word");
			if (compareWord.equals(theWord)) {
				return;
			}
			badWords.append(theWord).append("\n");
			return;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		badWords.append(theWord).append("\n");
		return;
	}//end checkForTheWord()
	
	/** This method uses the pre-loaded arraylist to check the words.
	 * @param theWord to be found
	 * @param listToCheck Array List to use to as dictionary
	 */
	public static void checkForTheWordArray(String theWord, ArrayList<String> listToCheck) {
		if (listToCheck.contains(theWord) == true) {
			return;
		}
		badWords.append(theWord).append("\n");
		return;
	}//end checkForTheWord()
	
}//end Class
