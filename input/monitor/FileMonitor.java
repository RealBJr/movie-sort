package input.monitor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

import input.exceptions.NonExistantTeacherFileException;
import vocab.Vocabs;

public class FileMonitor {

	/**
	 * Load the object within the teacher file;
	 * 
	 * @param teacherFilePath
	 * @return
	 */
	public static Vocabs loadTeacherVocabs(String teacherFilePath) throws NonExistantTeacherFileException {
		Vocabs vocabs = null;
		try {
			vocabs = loadTxtFileVocabs(teacherFilePath);
		} catch (FileNotFoundException e) {
			throw new NonExistantTeacherFileException(teacherFilePath);
		}
		return vocabs;
	}

	/**
	 * Loop through the text file. Every time I find the topic line, I add the
	 * previous entries which will later on be transformed into a Vocab object. The
	 * entries are then reseet.
	 * 
	 * @param filePathName
	 * @return the vocabs linked list
	 */
	public static Vocabs loadTxtFileVocabs(String filePathName) throws FileNotFoundException {
		Vocabs vocabs = new Vocabs();
		Scanner loadFrom = null;
		ArrayList<String> vocabEntries = null;
		try {
			loadFrom = new Scanner(new FileInputStream(filePathName));
			while (loadFrom.hasNextLine()) {
				String line = loadFrom.nextLine();

				// Making sure not to add empty line not to break my function
				if (line.isEmpty()) {
					continue;
				}

				if (isTopicLine(line)) {
					// making sure that the first case do not break my function with a null array
					if (vocabEntries == null) {
					} else {
						// here I update linked list!
						vocabs.addVocab(vocabEntries);
					}
					vocabEntries = new ArrayList<String>();
				}

				vocabEntries.add(line);
			}
		} catch (FileNotFoundException fnfe) {
			throw new FileNotFoundException();
		}
		return vocabs;
	}

	/**
	 * Check the first character to see whether or not it is an "#"
	 * 
	 * @param nextLine
	 * @return true if it starts with an "#".
	 */
	private static boolean isTopicLine(String line) {
		return line.strip().charAt(0) == '#';
	}

	/**
	 * Re-open the given teacher file and change what is inside
	 * 
	 * @param teacherFilePath
	 * @param vocabList
	 */
	public static void overrideTeacherFile(String teacherFilePath, Vocabs vocabList)
			throws NonExistantTeacherFileException {
		PrintWriter toTeacherFile = null;
		try {
			toTeacherFile = new PrintWriter(new FileOutputStream(teacherFilePath));
			for (int i = 0; i < vocabList.getTopics().size(); i++) {
				toTeacherFile.println("#" + vocabList.getTopics().get(i));
				for (int j = 0; j < vocabList.getWordsAsList(vocabList.getTopics().get(i)).size(); j++) {
					/* Get the unique word */
					toTeacherFile.println(vocabList.getWordsAsList(vocabList.getTopics().get(i)).get(j));
				}
				toTeacherFile.println();
			}
			toTeacherFile.close();
		} catch (FileNotFoundException e) {
			throw new NonExistantTeacherFileException(teacherFilePath);
		} catch (IOException io) {
			System.out.println("Unexpected IO error, FileMonitor" + io);
			System.exit(0);
		}
	}

}
