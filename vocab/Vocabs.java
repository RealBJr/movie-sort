package vocab;

import java.util.ArrayList;
import vocab.utils.*;

/**
 * Defined double linked list holding the Vocab objects.
 */
public class Vocabs {
	private Vocab head, tail;
	private ArrayList<String> topics;
	private String teacherName;

	public Vocabs() {
		this.head = null;
		this.tail = null;
		this.topics = new ArrayList<String>();
	}

	/**
	 * Add the element at the end of the linked list. Unless the topic already
	 * exists. In which case simply modify the vocab with said topic.
	 * 
	 * @param vocab, the formatted array containing topic and words. Basically the
	 *               content of the Vocab object
	 * @see Vocab()
	 */
	public void addVocab(ArrayList<String> vocab) {
		String addedTopic = VocabParser.parseTopic(vocab);
		if (topics.contains(addedTopic)) {
			System.out.println("Topic already exists, it has been modified accordingly.");
			Vocab newVocab = new Vocab(vocab);
			modifyTopic_add(addedTopic, newVocab.words.asList);
			return;
		}

		Vocab newVocab = new Vocab(vocab);
		if (head == null) {
			head = newVocab;
			tail = head;
		} else {
			tail.next = newVocab;
			tail.next.prev = tail;
			tail = tail.next;
		}
	}

	/**
	 * Loop through the linked list until finding the desired vocab upon which some
	 * words will be appended.
	 * 
	 * @param seekedTopic, words
	 */
	public void modifyTopic_add(String seekedTopic, ArrayList<String> words) {
		Vocab pointer = head;
		while (!pointer.topic.equals(seekedTopic)) {
			pointer = pointer.next;
		}
		pointer.words.addWords(words);
	}

	/**
	 * Similar as add(ArrayList<\String> vocab), only i use the Vocab object
	 * 
	 * @param vocab
	 */
	private void addVocab(Vocab vocab) {
		String addedTopic = vocab.topic;
		if (topics.contains(addedTopic)) {
			System.out.println("Topic already exists, it has been modified accordingly.");
			Vocab newVocab = new Vocab(vocab);
			modifyTopic_add(newVocab.topic, newVocab.words.asList);
			return;
		}

		Vocab newVocab = new Vocab(vocab);
		if (head == null) {
			head = newVocab;
			tail = head;
		} else {
			tail.next = newVocab;
			tail = tail.next;
		}
	}

	/**
	 * Ensure that the word is in the topic list. If not, abandon the operation.
	 * Also, make sure to update the word array list.
	 * 
	 * @param topic
	 * @param toRemove
	 * @return true if remove worked, it didn't work because element is not in words
	 */
	public boolean removeWordFrom(String topic, String toRemove) {
		if (!getWordsAsList(topic).remove(toRemove)) {// update list and also check if it is contained in the list
			System.out.println(topic + " does not contain " + toRemove);
			return false;
		}
		getVocab(topic).removeWord(toRemove);
		return true;
	}

	/**
	 * Loop through the vocab linked list until you find the Vocab object with the
	 * said topic
	 * 
	 * @param topic
	 * @return
	 */
	private Vocab getVocab(String topic) {
		// No need to check if null, it has been enforced to be one that is within the
		// arrayList with the words
		Vocab pointer = head;
		while (pointer != null && !pointer.topic.equals(topic)) {
			pointer = pointer.next;
		}
		return pointer;
	}

	/**
	 * 1) Loop until you find the node before the desired topic. Change the "prev"
	 * of that node as the given topic.<br>
	 * 2) Change the "previous" of the node next to the newly inserted node as that
	 * given topic. <br>
	 * 3) Ensure the configuration of the previous and next of the topic.
	 * 
	 * @param vocab,      formatted vocab (topic + words array) to be added
	 * @param afterTopic, the topic that, <b>once updated</b>, will be the "next" of
	 *                    the given vocab.
	 */
	public void addVocabBefore(ArrayList<String> vocab, String afterTopic) {
		Vocab pointer = null;
		if (topics.contains(VocabParser.parseTopic(vocab))) {
			System.out.println("Topic already exists, operation has been abandoned.");
			return;
		}
		Vocab newVocab = new Vocab(vocab);
		if (head == null) {
			System.out.println("There are no other topics. " + VocabParser.parseTopic(vocab)
					+ " has been inserted as the first topic");
			addVocab(vocab);
			updateTopicsOrder();
		} else if (!topics.contains(afterTopic)) {
			System.out.println(afterTopic + " is not a topic within Mr/Mrs. " + teacherName
					+ "'s vocab list.\nThe operation has been abandonned.\nPlease try agian!");
			return;
		} else {
			pointer = getVocab(afterTopic);

			/*
			 * If the previous is a null object, it means we are at the head. Otherwise, we
			 * are surrounded (else case).
			 */
			if (pointer.prev == null) {
				pointer.prev = newVocab;
				// Making sure to make it the head
				head = pointer.prev;
				// Changing the next property of the newly added object
				head.next = pointer;
			} else {
				// Keeping the previous of the current position before the latter changes
				Vocab temp = pointer.prev;
				pointer.prev = newVocab;
				// Changing the next property of the previous of the newly added node
				temp.next = pointer.prev;
				// Changing the prev property of the newly added node
				pointer.prev.prev = temp;
				// Changing the next property of the newly added node
				pointer.prev.next = pointer;
			}
			updateTopicsOrder();
		}
	}

	/**
	 * 1) Loop until you find the node containing the desired topic. Change the
	 * "next" of that node as the given topic.<br>
	 * 2) Change the "previous" of the node next to the newly inserted node as that
	 * given topic. <br>
	 * 3) Ensure the configuration of the previous and next of the topic.
	 * 
	 * @param vocab,      formatted vocab (topic + words array) to be added
	 * @param afterTopic, the topic that, <b>once updated</b>, will be the
	 *                    "previous".
	 */
	public void addVocabAfter(ArrayList<String> vocab, String prevTopic) {
		Vocab pointer = null;
		if (topics.contains(VocabParser.parseTopic(vocab))) {
			System.out.println("Topic already exists, operation has been abandoned.");
			return;
		}
		Vocab newVocab = new Vocab(vocab);
		if (head == null) {
			System.out.println("There are no other topics. " + VocabParser.parseTopic(vocab)
					+ " has been inserted as the first topic");
			addVocab(vocab);
			updateTopicsOrder();
		} else if (!topics.contains(prevTopic)) {
			System.out.println(prevTopic + " is not a topic within Mr/Mrs. " + teacherName
					+ "'s vocab list.\nThe operation has been abandonned.\nPlease try agian!");
			return;
		} else {
			pointer = getVocab(prevTopic);

			/*
			 * If the next is a null object, it means we are at the tail.
			 */
			if (pointer.next == null) {
				pointer.next = newVocab;
				// Making sure to make it the head
				tail = pointer.next;
				// Changing the prev property of the newly added object
				pointer.next.prev = pointer;
			} else {
				// Keeping the next of the current position before the latter changes
				Vocab temp = pointer.next;
				pointer.next = newVocab;
				// Changing the prev property of the next of the newly added node
				temp.prev = pointer.next;
				// Changing the prev property of the newly added node
				pointer.next.prev = pointer;
				// Changing the next property of the newly added node
				pointer.next.next = temp;
			}
			updateTopicsOrder();
		}
	}

	/**
	 * Get the words based on given topic as an array list.
	 * 
	 * @param topic, topic you want to find the words for
	 */
	public ArrayList<String> getWordsAsList(String topic) {
		ArrayList<String> words = new ArrayList<String>();
		Vocab pointer = head;
		while (!pointer.topic.equals(topic)) {
			pointer = pointer.next;
		}
		words = pointer.words.asList;
		return words;
	}

	/**
	 * Get the words based on given topic as an array list.
	 * 
	 * @param topic, topic you want to find the words for
	 */
	public ArrayList<String> getAllWordsAsList() {
		ArrayList<String> allWords = new ArrayList<String>();
		Vocab pointer = head;
		while (pointer != null) {
			allWords.addAll(getWordsAsList(pointer.topic));
			pointer = pointer.next;
		}
		return allWords;
	}

	/**
	 * Loop through the vocab in the linked list in order and add them in order to a
	 * new topics list that will be reassigned.
	 */
	private void updateTopicsOrder() {
		ArrayList<String> temp = new ArrayList<String>();
		Vocab pointer = head;
		while (pointer != null) {
			temp.add(pointer.topic);
			pointer = pointer.next;
		}
		this.topics = temp;
	}

	/**
	 * Add the content of a Linked List after this linked list
	 * 
	 * @param toAppend
	 */
	public void appendVocabs(Vocabs toAppend) {
		Vocab pointer = toAppend.head;
		while (!pointer.equals(toAppend.tail)) {
			this.addVocab(pointer);
			pointer = pointer.next;
		}
	}

	/**
	 * Loop through the vocab and check which has the sought topic. Check if the
	 * topic is already in the word list in which case abandon function.
	 * 
	 * @param soughtTopic
	 * @param word
	 */
	public boolean addWordTo(String soughtTopic, String word) {
		Vocab addTo = getVocab(soughtTopic);
		if (addTo.words.asList.contains(word)) {
			System.out.println("sorry, the word: '" + word + "' already exists in " + soughtTopic);
			return false;
		}
		addTo.words.addWord(word);
		return true;
	}

	// ---------------------Getters and setters--------------------
	/**
	 * Get a deep copy of the object list of topics within this vocabs list
	 * 
	 * @return copy of the Vocabs list
	 */
	public ArrayList<String> getTopics() {
		ArrayList<String> copy = new ArrayList<String>(topics);
		return copy;
	}

	public String getTeacher() {
		return teacherName;
	}

	public void setTeacher(String teacher) {
		this.teacherName = teacher;
	}

	/**
	 * Node of the Vocabs double linked list. Wrapper class referring to a topic
	 * name and the vocabulary words associated with that topic.
	 */
	class Vocab {
		private String topic;
		private Words words;
		private Vocab next, prev;

		/**
		 * Creates the Vocab wrapper based on the topic + the words.
		 * 
		 * @param vocab, the formated array containing the topic name, and the
		 *               respective for said topic
		 */
		public Vocab(ArrayList<String> vocab) {
			if (vocab.size() == 0) {
				this.topic = "";
				this.words = new Words(new ArrayList<String>());
				return;
			}
			this.topic = VocabParser.parseTopic(vocab);
			this.words = new Words(VocabParser.parseWords(vocab));
			topics.add(this.topic);// update topic list.
		}

		public Vocab(Vocab vocab) {
			this.topic = vocab.topic;
			this.words = vocab.words;
			topics.add(this.topic);// update topic list.
		}

		/**
		 * Remove word; note that it has been verified that the word is within the list.
		 * 
		 * @param toRemove
		 */
		public void removeWord(String toRemove) {
			this.words.remove(toRemove);
		}

		/**
		 * Defined singly-linked list. It contains the words under a said topic.
		 */
		class Words {
			private Word head, tail;
			private ArrayList<String> asList;

			/**
			 * With an array containing the words under a said topic, I load the linked
			 * list.
			 * 
			 * @param words, words relating to said topic
			 */
			public Words(ArrayList<String> words) {
				this.asList = new ArrayList<String>();
				if (words.size() != 0) {
					loadSingleLinkedList(words);
				}
			}

			/**
			 * Loop until you find the word before the word that have to be removed. Change
			 * the next link of that word to null.
			 * 
			 * @param toRemove
			 */
			public void remove(String toRemove) {
				Word pointer = head;
				while (pointer.next != null && !pointer.next.value.equals(toRemove)) {
					pointer = pointer.next;
				}
				pointer.next = null;
			}

			/**
			 * Make sure to add words that are not already within the linkedList
			 * 
			 * @param words
			 */
			public void addWords(ArrayList<String> words) {
				boolean duplicates = false;
				for (int i = 0; i < words.size(); i++) {
					if (asList.contains(words.get(i))) {
						duplicates = true;
					} else {
						this.addWord(words.get(i));
					}
				}
				if (duplicates) {
					System.out.println("Some of the words added were already under the topic's list.");
					System.out.println("They have not been added for that matter.");
				}
			}

			/**
			 * Convey the object referenced by the "next" to every word nodes.
			 * 
			 * @param words
			 */
			private void loadSingleLinkedList(ArrayList<String> words) {
				head = new Word(words.get(0));
				asList.add(words.get(0));
				tail = head;
				for (int i = 1; i < words.size(); i++) {
					this.addWord(words.get(i));
				}
				// Update List
				updateList();
			}

			/**
			 * Changes the tail reference to point to the correct Word.
			 * 
			 * @param word
			 */
			private void addWord(String word) {
				Word pointer = head;
				if (pointer == null) {
					head = new Word(word);
					tail = head;
				}
				
				if (pointer == head && word.compareToIgnoreCase(pointer.value) < 0) {
					head = new Word(word, head);
					return;
				}

				while (pointer.next != null && word.compareToIgnoreCase(pointer.next.value) > 0) {
					pointer = pointer.next;
				}

				// In case for first iteration
				if (pointer.next == null) {
					pointer.next = new Word(word);
					tail = pointer.next;
				} else if (pointer.next != null) {
					Word temp = pointer.next;
					pointer.next = new Word(word, temp);
				}
			}

			/**
			 * Loop through the word and add them to the list
			 */
			private void updateList() {
				ArrayList<String> updatedList = new ArrayList<String>(this.asList.size()+1);
				Word pointer = head;
				while (pointer!= null) {
					updatedList.add(pointer.value);
					pointer = pointer.next;
				}
				this.asList = updatedList;
			}

			/**
			 * Node of the Words linked-list
			 */
			class Word {
				private Word next;
				private String value;

				public Word(String value) {
					this.next = null;
					this.value = value;
				}

				public Word(String value, Word next) {
					this.next = next;
					this.value = value;
				}

				@Override
				public String toString() {
					return value;
				}
			}

			/**
			 * Loop through the linked list and add sequentially the words to an arrayList
			 * 
			 * @return
			 */
			public ArrayList<String> turnToLinkedList() {
				ArrayList<String> toReturn = new ArrayList<String>();
				Word pointer = head;
				while (pointer != null) {
					toReturn.add(pointer.value);
					pointer = pointer.next;
				}
				return toReturn;
			}
		}

	}

	/**
	 * Loop through the topics, check if the topics contain the searched word, then
	 * add its topic's name in an arrayList
	 * 
	 * @param searched
	 */
	public ArrayList<String> findWord(String searched) {
		ArrayList<String> foundIn = new ArrayList<String>();
		Vocab pointer = head;
		while (pointer != null) {
			if (getWordsAsList(pointer.topic).contains(searched)) {
				if (!foundIn.contains(pointer.topic)) {
					foundIn.add(pointer.topic);
				}
			}
			pointer = pointer.next;
		}
		return foundIn;
	}

	/**
	 * Unlink the next of the pointer.prev as well as the previous of pointer.next
	 * 
	 * @param topicToDelete
	 */
	public void removeTopic(String topicToDelete) {
		Vocab pointer = getVocab(topicToDelete);
		Vocab prev = pointer.prev;
		Vocab next = pointer.next;

		if (pointer == tail && pointer == head) {
			tail = null;
			head = null;
		} else if (pointer == tail) {
			tail = pointer.prev;
			tail.next = null;
		} else if (pointer == head) {
			head = pointer.next;
			head.prev = null;
		} else {
			prev.next = next;
			next.prev = prev;
		}
		updateTopicsOrder();
	}

	/**
	 * 
	 * @param topic
	 * @return
	 */
	public ArrayList<String> linkedToArr(String topic) {
		return getVocab(topic).words.turnToLinkedList();
	}

}
