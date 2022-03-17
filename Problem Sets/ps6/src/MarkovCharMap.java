import java.util.HashMap;
import java.util.Random;

public class MarkovCharMap {
    public static final char NOCHARACTER = (char) 0;

    public HashMap<Character, MarkovNodePair<Character>> charMap = new HashMap<>();
    public SortedCharArray allKeys = new SortedCharArray();

    public void put(Character key) {
        boolean isExist = this.charMap.get(key) != null;
        allKeys.add(key);

        if (!isExist) {
            MarkovNodePair newNode = new MarkovNodePair<>(key);
            this.charMap.put(key, newNode);
            return;
        }

        this.charMap.get(key).addFrequency();
    }

    public int getKeyFrequency(Character key) {
        MarkovNodePair<Character> markovNodePair = this.charMap.get(key);
        if (markovNodePair == null) {
            return 0;
        }
        return markovNodePair.frequency;
    }

    public Character pickRandomChar(Random generator) {
        int max = allKeys.size();
        if (max == 0)
            return NOCHARACTER;

        int randomPick = generator.nextInt(max);
        return allKeys.get(randomPick);
    }
}