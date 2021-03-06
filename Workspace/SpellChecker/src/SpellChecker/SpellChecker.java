package SpellChecker;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Spell checking class for assignment 3
 * @author Michael Forman
 */
public class SpellChecker {

	/**
	 * Main method
	 * @param args not used
	 */
	public static void main(String[] args) {
		String fileToCheck;
		String dictionaryFile;
		ArrayList<String> fileWords = new ArrayList<String>();
		ArrayList<String> dictionaryWords = new ArrayList<String>();

		fileToCheck = getFileToCheck(); // get filename of checked file with error checking
		dictionaryFile = getFileToCheck();// get filename of dictionary file with error checking

		fileWords = fillArrayList(fileToCheck, fileWords); // send filetocheck to be read into array
		dictionaryWords = fillArrayList(dictionaryFile, dictionaryWords); // send dictionary to array

		for (String fileWord : fileWords) {// iterates the to be checked file for bad words
			checkForTheWord(fileWord, dictionaryWords);
		}
		System.out.println("Checking Complete!");
		System.exit(0);
	}

	/**
	 * method to check for the presence of the word in the dictionary
	 * @param theWord word to be checked against dictionary
	 */
	public static void checkForTheWord(String theWord, ArrayList<String> listToCheck) {
		if (listToCheck.contains(theWord) == true) {
			return;
		}
		System.out.println(theWord + " is an unknown word.");
		return;
	}

	/**
	 * Method to get name of the file to be checked. Checks for errors
	 * @return fileToCheck filename of file to be checked
	 */
	public static String getFileToCheck() {
		int fileIsThere = 1;
		String fileToCheck = "";
		do {
			System.out.println("Input the file to check: ");
			Scanner keyboard = new Scanner(System.in);
			fileToCheck = keyboard.nextLine();
			File mainWordFile = new File(fileToCheck);
			if (mainWordFile.exists() == true) {
				fileIsThere = 0;
			}
			else {
				System.out.println("File does not exist. Please re-enter.");
			}
		} 
		while (fileIsThere == 1);
		return fileToCheck;
	}

	/**
	 * Method to fill an ArrayList with contents of a file
	 * @param fileToBeRead filename of file to be read into array
	 * @param arrayToBeFilled name of arrayList to be filled with file
	 * @return arraylist once filled
	 */
	public static ArrayList<String> fillArrayList(String fileToBeRead, ArrayList<String> arrayToBeFilled) {
		try {// reads dictionary file into string array
			File dictionaryWordFile = new File(fileToBeRead);
			Scanner input = new Scanner(dictionaryWordFile);
			while (input.hasNextLine()) {
				String dictWord = input.nextLine();
				try {
					arrayToBeFilled.add(dictWord);
				} catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return arrayToBeFilled;
	}
}
