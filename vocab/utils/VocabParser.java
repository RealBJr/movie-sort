package vocab.utils;

import java.util.ArrayList;

/**
 * The topics and the words are distinguished within an array.
 */
public class VocabParser {

	/**
	 * If correctly formated, vocab[0] is <i>#Topic Name</i>.
	 * 
	 * @param vocab, the formatted vocab(topic + words).
	 * @return <i>Topic Name</i>
	 */
	public static String parseTopic(ArrayList<String> vocab) {
		return vocab.get(0).replaceFirst("#", "");
	}

	/**
	 * If correctly formated, vocab[0] is <i>#Topic Name</i>, and, after that, we
	 * have all the words relating to that topic.
	 * 
	 * @param vocab, the formatted vocab(topic + words).
	 * @return a reference to the String array with all the words
	 */
	public static ArrayList<String> parseWords(ArrayList<String> vocab) {
		ArrayList<String> toReturn = new ArrayList<String>(vocab);
		toReturn.remove(0);
		return toReturn;
	}

}
