import java.util.Random;
import java.util.HashMap;

/**
 * This is the main class for your Markov Model.
 *
 * Assume that the text will contain ASCII characters in the range [1,255].
 * ASCII character 0 (the NULL character) will be treated as a non-character.
 *
 * Any such NULL characters in the original text should be ignored.
 */
public class MarkovModel {

	// Use this to generate random numbers as needed
	private Random generator = new Random();
	private HashMap<String, MarkovNodePair<MarkovCharMap>> kgramMap = new HashMap<>();
	private int order;
	// This is a special symbol to indicate no character
	public static final char NOCHARACTER = (char) 0;

	/**
	 * Constructor for MarkovModel class.
	 *
	 * @param order the number of characters to identify for the Markov Model sequence
	 * @param seed the seed used by the random number generator
	 */
	public MarkovModel(int order, long seed) {
		// Initialize your class here
		this.order = order;
		// Initialize the random number generator
		generator.setSeed(seed);
	}

	/**
	 * Builds the Markov Model based on the specified text string.
	 */
	public void initializeText(String text) {
		String[] splitString = text.split("\\s+");
		for (String s : splitString) {
			boolean isExist = this.kgramMap.get(s) != null;
			if (!isExist) {
				this.kgramMap.put(s, new MarkovNodePair<>(new MarkovCharMap()));
				this.kgramMap.get(s).value.put(nextChar);
				continue;
			}
			this.kgramMap.get(s).addFrequency();
			this.kgramMap.get(s).value.put(nextChar);
		}
//		for (int i = 0; i < text.length() - this.order; i++) {
//			String kgram = text.substring(i, i + this.order);
//			Character nextChar = text.charAt(i + this.order);
//
//			boolean isExist = this.kgramMap.get(kgram) != null;
//			if (!isExist) {
//				this.kgramMap.put(kgram, new MarkovNodePair<>(new MarkovCharMap()));
//				this.kgramMap.get(kgram).value.put(nextChar);
//				continue;
//			}
//
//			this.kgramMap.get(kgram).addFrequency();
//			this.kgramMap.get(kgram).value.put(nextChar);
//		}
	}

	/**
	 * Returns the number of times the specified kgram appeared in the text.
	 */
	public int getFrequency(String kgram) {
		MarkovNodePair<MarkovCharMap> markovNodePair = this.kgramMap.get(kgram);
		if (markovNodePair == null) {
			return 0;
		}
		return markovNodePair.frequency;
	}

	/**
	 * Returns the number of times the character c appears immediately after the specified kgram.
	 */
	public int getFrequency(String kgram, char c) {
		MarkovNodePair<MarkovCharMap> markovNodePair = this.kgramMap.get(kgram);
		if (markovNodePair == null) {
			return 0;
		}
		return markovNodePair.value.getKeyFrequency(c);
	}

	/**
	 * Generates the next character from the Markov Model.
	 * Return NOCHARACTER if the kgram is not in the table, or if there is no
	 * valid character following the kgram.
	 */
	public char nextCharacter(String kgram) {
		MarkovNodePair<MarkovCharMap> markovNodePair = this.kgramMap.get(kgram);
		if (markovNodePair == null) {
			return NOCHARACTER;
		}
		// System.out.println(markovNodePair.value.allKeys.toString());
		return markovNodePair.value.pickRandomChar(generator);
	}
}
