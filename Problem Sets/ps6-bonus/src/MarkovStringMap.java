import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MarkovStringMap {
    public static final char NOCHARACTER = (char) 0;

    public HashMap<Character, MarkovNodePair<String>> stringMap = new HashMap<>();
    public ArrayList<String> allKeys = new ArrayList<>();

    public void put(String key) {
        boolean isExist = this.stringMap.get(key) != null;
        allKeys.add(key);

        if (!isExist) {
            MarkovNodePair newNode = new MarkovNodePair<>(key);
            this.stringMap.put(key, newNode);
            return;
        }

        this.stringMap.get(key).addFrequency();
    }

    public int getKeyFrequency(Character key) {
        MarkovNodePair<Character> markovNodePair = this.stringMap.get(key);
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