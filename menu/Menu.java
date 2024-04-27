package menu;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import input.exceptions.InvalidTopicNameException;
import input.exceptions.NonExistantTeacherFileException;
import input.monitor.FileMonitor;
import vocab.Vocabs;
import vocab.utils.VocabParser;

/**
 * Every teacher have their custom menu with their name on it
 */
public class Menu {

	private String teacherName;
	private Vocabs vocabList;

	/**
	 * Making my menus private to ensure that to create a menu, they are forced to
	 * use the configMenu() function to emphasize that the menu is custom made.<br>
	 * 
	 * @see configMenu()
	 */
	private Menu() {

	}

	private Menu(String teacherName, Vocabs vocabs) {
		this.teacherName = teacherName;
		this.vocabList = vocabs;
	}

	/**
	 * Prompt the user for the info necessary to launch the menu. Load the
	 * 
	 * @return a reference to a new menu with the name of the class' teacher
	 */
	public static Menu configMenu() {
		System.out.print("Please enter the family name of your teacher: ");
		String teacherName = MenuInput.promptText();
		Vocabs vocabs = new Vocabs();
		try {
			String teacherFilePath = formatPath(teacherName);
			vocabs = loadVocabs(teacherFilePath);
			System.out.println("Successfully loaded " + teacherName + "'s vocabulary list.");
		} catch (NonExistantTeacherFileException ne) {
			System.out.println("Hi first timer!");
		}
		return new Menu(teacherName, vocabs);
	}

	/**
	 * Regular expression to split at word boundaries. I want the first word because
	 * sometime a teacher can have name: abc-xyz
	 * 
	 * @param teacherName
	 * @return
	 */
	private static String formatPath(String teacherName) {
		return teacherName.toLowerCase().split("\\b")[0] + ".vocabs";
	}

	/**
	 * Search the computer for a file with the name of the teacherName
	 * 
	 * @param teacherName
	 * @return
	 * @throws NonExistantTeacherFileException
	 */
	private static Vocabs loadVocabs(String teacherFilePath) throws NonExistantTeacherFileException {
		Vocabs vocabs = new Vocabs();
		try {
			vocabs = FileMonitor.loadTeacherVocabs(teacherFilePath);
		} catch (NonExistantTeacherFileException ne) {
			throw ne;
		}
		return vocabs;
	}

	/**
	 * Display the menu's option
	 */
	public void displayMenu() {
		System.out.println("===========================\r\n" + teacherName + "'s Vocabulary Control Center\r\n"
				+ "===========================\r\n" + "1 Browse a topic\r\n"
				+ "2 Insert a new topic before another one\r\n" + "3 Insert a new topic after another one\r\n"
				+ "4 Remove a topic\r\n" + "5 Modify a topic\r\n" + "6 Search topics for a word\r\n"
				+ "7 Load from a file\r\n" + "8 Show all words starting with a given letter\r\n" + "9 Save to file\r\n"
				+ "0 Exit\r\n" + "===========================");
	}

	/**
	 * Executes the desired option
	 * 
	 * @param option
	 */
	public void execute(int option) {
		switch (option) {
		case 1: {
			int innerOption = 0;
			do {
				innerOption = browseTopic();
				if (innerOption == -1) {
					break;
				}
				displayTopicWords(this.vocabList.getTopics().get(innerOption));
			} while (innerOption != -1);
			break;
		}
		case 2: {
			insertBefore();
			break;
		}
		case 3: {
			insertAfter();
			break;
		}
		case 4: {
			removeTopic();
			break;
		}
		case 5: {
			modifyTopic();
			break;
		}
		case 6: {
			searchWord();
			break;
		}
		case 7: {
			loadFileVocab();
			break;
		}
		case 8: {
			showWordStartsWith();
			break;
		}
		case 9: {
			saveToTeacherFile();
			break;
		}
		default: {

		}
		}
	}

	/**
	 * Display the topics, prompt for the one he/she wants to delete
	 */
	private void removeTopic() {
		int chosen = browseTopic("to delete");
		if (chosen == -1) {
			return;
		}
		String topicToDelete = this.vocabList.getTopics().get(chosen);
		this.vocabList.removeTopic(topicToDelete);
		System.out.println(topicToDelete + " has been successfully deleted");
	}

	/**
	 * Add the topic before a prompted topic. Prompt the topic that will be after it
	 * it.
	 */
	private void insertBefore() {
		int chosen = -1;
		if (this.vocabList.getTopics().size() == 0) {
			chosen = handleEmptyTopic();
			return;
		} else {
			chosen = browseTopic("to precede your new topic");
			String toSucceed = this.vocabList.getTopics().get(chosen);
			ArrayList<String> vocab = new ArrayList<String>();
			try {
				vocab = loadVocabListFormatted();
			} catch (InvalidTopicNameException e) {
				return;
			}
			this.vocabList.addVocabBefore(vocab, toSucceed);
			System.out.println(
					"Successfully added \"" + VocabParser.parseTopic(vocab) + "\" before \"" + toSucceed + "\"");
		}
	}

	/**
	 * In case there are no more topic
	 * 
	 * @return
	 */
	private int handleEmptyTopic() {
		System.out.println(
				"There are no lists to insert before, would you like 0) to cancel, 1) add a new topic? (Type 0 or 1)");
		int chosen = MenuInput.promptOption(1);
		if (chosen == 1) {
			try {
				ArrayList<String> vocab = loadVocabListFormatted();
				this.vocabList.addVocab(vocab);
				System.out.println("Successfully added \"" + VocabParser.parseTopic(vocab) + "\" to the list");
				return chosen;
			} catch (InvalidTopicNameException e) {
				return -1;
			}
		} else {
			return -1;
		}

	}

	/**
	 * Add the topic after a prompted topic. Prompt the topic that will be before
	 * it.
	 */
	private void insertAfter() {
		int chosen = -1;
		if (this.vocabList.getTopics().size() == 0) {
			handleEmptyTopic();
			return;
		} else {
			chosen = browseTopic("to precede your new topic");
			String toPrecede = this.vocabList.getTopics().get(chosen);
			ArrayList<String> vocab = new ArrayList<String>();
			try {
				vocab = loadVocabListFormatted();
			} catch (InvalidTopicNameException e) {
				return;
			}
			this.vocabList.addVocabAfter(vocab, toPrecede);
			System.out.println(
					"Successfully added \"" + VocabParser.parseTopic(vocab) + "\" after \"" + toPrecede + "\"");
		}

	}

	private ArrayList<String> loadVocabListFormatted() throws InvalidTopicNameException {
		System.out.print("Please enter a topic name: ");
		String newTopic = MenuInput.promptText();
		if (this.vocabList.getTopics().contains(newTopic)) {
			System.out.println("\n" + newTopic
					+ " already exist, you are now redirected to main. Try again to input another topic or modify!"
					+ newTopic + "\n");
			throw new InvalidTopicNameException();
		}
		ArrayList<String> vocab = new ArrayList<String>();
		String entry = "";
		vocab.add(newTopic);
		System.out.println("Please enter the words within your new topic \"" + newTopic + "\"");
		System.out.println("Separate every entry with the \"Enter\" key. Or you can just quit by typing \"~\"");
		do {
			entry = MenuInput.promptText();
			if (entry.equals("~")) {
				return vocab;
			}
			vocab.add(entry);
		} while (!entry.equals("~"));

		return vocab;
	}

	/**
	 * Prompt for the object of the research
	 */
	private void searchWord() {
		System.out.print("Please type the word you are looking for: ");
		String searched = MenuInput.promptText();
		ArrayList<String> foundIn = this.vocabList.findWord(searched);
		if (foundIn.size() == 0) {
			System.out.println(searched + " has not been found under no topics");
			return;
		}
		for (int i = 0; i < foundIn.size(); i++) {
			System.out.println("\n\"" + searched + "\" has been found under topic = " + foundIn.get(i) + "\n");
		}
	}

	/**
	 * Loop through all the vocabList, and then loop through all the words within a
	 * vocab object, and print all the words that start with a said character. (case
	 * sensitive)
	 */
	private void showWordStartsWith() {
		System.out.print(
				"Please type the character you want(note even if you write a word, only the first letter of that word will be used)): ");
		char sought = MenuInput.promptText().charAt(0);
		ArrayList<String> allWords = this.vocabList.getAllWordsAsList();
		ArrayList<String> matching = new ArrayList<String>();
		System.out.println("Here are all the words starting with'" + sought + "': ");
		for (String word : allWords) {
			if (word.charAt(0) == sought) {
				matching.add(word);
			}
		}
		displayListFormat(matching);
	}

	/**
	 * Prompt the user what topic he/she wants to modify. Display the modify
	 * options.
	 */
	private void modifyTopic() {
		int chosen = browseTopic("To Modify");
		if (chosen == -1) {
			return;
		}
		String topic = this.vocabList.getTopics().get(chosen);
		System.out.println("---------------------------\r\n" + "Modify " + topic + " Menu\r\n"
				+ "-----------------------------\r\n" + "a Add a word in " + topic + "\r\n" + "r Remove a word in "
				+ topic + "\r\n" + "c Change a word in " + topic + "\r\n" + "0 Exit to main\r\n"
				+ "-----------------------------");
		System.out.print("Enter your choice: ");
		char option = MenuInput.promptOption();
		executeMod(option, topic);
	}

	/**
	 * Depending on what is chosen, execute the modification.
	 * 
	 * @param option
	 */
	private void executeMod(char option, String topic) {
		switch (option) {
		case 'a': {
			addWord(topic);
			break;
		}
		case 'r': {
			removeWord(topic);
			break;
		}
		case 'c': {
			changeWord(topic);
			break;
		}
		default:
			return;
		}
	}

	/**
	 * Remove the given word and add another
	 * 
	 * @param topic
	 */
	private void changeWord(String topic) {
		System.out.println("Here are the words in " + topic);
		displayTopicWords(topic);
		System.out.print("Please type the word you want to change: ");
		String toRemove = MenuInput.promptText();
		if (!this.vocabList.removeWordFrom(topic, toRemove)) {
			return;
		}
		String toAdd = "";
		do {
			System.out.print(
					"Please type the word that you want to insert instead (or type ~ to return to cancel operation): ");
			toAdd = MenuInput.promptText();
		} while (toAdd != "~" && !this.vocabList.addWordTo(topic, toAdd));
		System.out.println(toRemove + " has been successfully changed into " + toAdd + " in the " + topic + " topic");
	}

	/**
	 * Display the words, and make the student type the word that he/she wants to
	 * remove from the displayed list.
	 * 
	 * @param topic
	 */
	private void removeWord(String topic) {
		System.out.println("Here are the words in " + topic);
		displayTopicWords(topic);
		System.out.print("Please type the word you want to remove: ");
		String toRemove = MenuInput.promptText();
		if (!this.vocabList.removeWordFrom(topic, toRemove)) {
			return;
		}
		System.out.println(toRemove + " have been successfuly removed from " + topic);
	}

	/**
	 * Add a word to the selected topic.
	 * 
	 * @param topic
	 */
	private void addWord(String topic) {
		System.out.print("Please type words and press 'Enter', or type ~ to end input and return to main\r\n");
		String entered = "";
		do {
			entered = MenuInput.promptText();
			if (entered.equals("~")) {
				return;
			}
			this.vocabList.addWordTo(topic, entered);
		} while (!entered.equals("~"));
		System.out.println("Given words have been successfully added to" + topic);
	}

	/**
	 * Override the file with the teacher name.
	 */
	private void saveToTeacherFile() {
		String teacherFilePath = formatPath(teacherName);
		try {
			saveVocabs(teacherFilePath);
			System.out.println("Successfully saved " + teacherName + "'s vocabulary list into your files!");
		} catch (NonExistantTeacherFileException ne) {
			System.out.println("Unexpected file not found exception of " + teacherFilePath);
		}
	}

	/**
	 * Override the teacher's file with the new linked list
	 * 
	 * @param teacherFilePath
	 * @throws NonExistantTeacherFileException
	 */
	private void saveVocabs(String teacherFilePath) throws NonExistantTeacherFileException {
		FileMonitor.overrideTeacherFile(teacherFilePath, this.vocabList);
	}

	/**
	 * Prompt for the file you want to look for
	 */
	private void loadFileVocab() {
		System.out.println("Enter the name of the input file: ");
		String filePathName = MenuInput.promptText();
		Vocabs txtFileVocabs = null;
		try {
			txtFileVocabs = FileMonitor
					.loadTxtFileVocabs(System.getProperty("user.dir") + "\\assignment3\\givenFiles\\" + filePathName);
		} catch (FileNotFoundException e) {
			System.out.println(e);
			System.out.println("You will be redirected to the menu");
			return;
		}
		this.vocabList.appendVocabs(txtFileVocabs);
		System.out.println("Done loading");
		System.out.println("\nTest, The topics:" + this.vocabList.getTopics().toString());
	}

	/**
	 * Display the topics available, prompt the student to enter the topic he/she
	 * wants to see. Display the words from that topic.
	 * 
	 * @return the topic to watch;
	 */
	private int browseTopic(String... instructions) {
		displayTopics(instructions);
		System.out.print("Enter your choice: ");
		int option = MenuInput.promptOption(this.vocabList.getTopics().size());
		if (option == 0) {
			return -1;
		}
		return option - 1;
	}

	/**
	 * Display words in a topic
	 * 
	 * @param topic
	 */
	private void displayTopicWords(String topic) {
//		System.out.println("Topic: " + topic);
//		ArrayList<String> words = this.vocabList.getWordsAsList(topic);
		displayListFormat(topic);

	}

	/**
	 * Sort the list and then display the words in a proper format
	 * 
	 * @param words
	 */
	private void displayListFormat(ArrayList<String> words) {
		words.sort((s1, s2) -> {
			return s1.compareTo(s2);
		});

		int inBetweenLength = 20;// un-calculated desired length, I need to find the biggest
		for (int i = 0; i < words.size(); i++) {
			System.out.print(((i + 1) + ": " + words.get(i)));
			if ((i + 1) % 4 != 0) {
				for (int j = 0; j < inBetweenLength
						- (words.get(i).length() + new String("" + (i + 1)).length()); j++) {
					System.out.print(" ");
				}
			} else {
				System.out.println();
			}
		}
		System.out.println();

	}

	/**
	 * Sort the list and then display the words in a proper format, using a topic
	 * 
	 * @param words
	 */
	private void displayListFormat(String topic) {
		System.out.println("Modified display");
		ArrayList<String> linkedToArr = this.vocabList.linkedToArr(topic);
		int inBetweenLength = 20;// un-calculated desired length, I need to find the biggest
		for (int i = 0; i < linkedToArr.size(); i++) {
			System.out.print(((i + 1) + ": " + linkedToArr.get(i)));
			if ((i + 1) % 4 != 0) {
				for (int j = 0; j < inBetweenLength
						- (linkedToArr.get(i).length() + new String("" + (i + 1)).length()); j++) {
					System.out.print(" ");
				}
			} else {
				System.out.println();
			}
		}
		System.out.println();

	}

	/**
	 * Display the updated list of topic, only uses the first instruction
	 */
	private void displayTopics(String... instructions) {
		ArrayList<String> topics = this.vocabList.getTopics();
		System.out.println("----------------------------\r\n" + "Pick a topic "
				+ (instructions.length == 1 ? instructions[0] : "") + "\r\n" + "----------------------------");
		for (int i = 0; i < topics.size(); i++) {
			System.out.println((i + 1) + " " + topics.get(i));
		}
		System.out.println("0 Exit");
		System.out.println("----------------------------");
	}
}
