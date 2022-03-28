import java.util.ArrayList;

public class Trie {

    // Trie node root
    TrieNode root = null;
    // Wildcards
    final char WILDCARD = '.';

    private class CharArray extends ArrayList<Character> {
        /**
         * Inserts character in a sorted fashion into the arrayList
         *
         * @param c  the character to insert into the arrayList
         */
        @Override
        public boolean add(Character c) {
            int index = 0;
            while (index < this.size() && c > this.get(index))
                index++;
            this.add(index, c);
            return true;
        }
    }

    private class TrieNode {
        private TrieNode[] nodeChildren = new TrieNode[63];
        private CharArray presentChars = new CharArray();
        public String key;

        public TrieNode(String key) { this.key = key; }

        public char getChar(int i) {
            return presentChars.get(i);
        }
        public int getChildrenSize() {
            return presentChars.size();
        }

        public TrieNode getChild(int i) {
            return this.nodeChildren[i];
        }
        public void insertChild(int i, TrieNode n) {
            char c = arrayIndexToCharMap(i);
            presentChars.add(c);
            this.nodeChildren[i] = n;
        }

        /**
         * Checks to see if the current node contains a termination character.
         *
         * @return the true if it contains the termination character false otherwise
         */
        public boolean containsTermination() {
            return nodeChildren[62] == null ? false : true;
        }
        @Override
        public String toString() {
            return String.format("{ key: %s, children: %s }", this.key, this.presentChars);
        }
    }

    public Trie() {
        this.root = new TrieNode("");
    }

    /**
     * Converts a character to an array position.
     *
     * @param c  the character you want to convert
     * @return the array position to store character or retrieve character from
     */
    public int asciiToArrayMap(char c) {
        int arrayIndex = -1;
        int ascii = c;

        // character is a number
        if (ascii >= 48 && ascii < 58)
            arrayIndex = ascii - 48;
        // character is a capital letter
        if (ascii >= 65 && ascii < 91)
            arrayIndex = ascii - 55;
        // character is a lowercase letter
        if (ascii >= 97 && ascii < 123)
            arrayIndex = ascii - 61;
        // character is the terminating WILDCARD
        if (ascii == 46)
            arrayIndex = 62;

        return arrayIndex;
    }

    /**
     * Converts an array index to a char.
     *
     * @param i  the array index you want to convert
     * @return the char corresponding to your array index
     */
    public char arrayIndexToCharMap(int i) {
        int ascii = 0;
        // index is a number
        if (i >= 0 && i < 10)
            ascii = i + 48;
        // index is a capital letter
        if (i >= 10 && i < 36)
            ascii = i + 55;
        // index is a lowercase letter
        if (i >= 36 && i < 62)
            ascii = i + 61;
        // index is the terminating WILDCARD
        if (i == 62)
            ascii = 46;
        return (char) ascii;
    }

    /**
     * Inserts a character into a node
     *
     * @param c  the character you want to insert into your node
     * @param node  the node to insert the character on
     * @return either an already existing node or will create a new node and return it
     */
    public TrieNode insertCharacter(char c, TrieNode node) {
        int arrayIndex = asciiToArrayMap(c);
        if (node.getChild(arrayIndex) == null) {
            node.insertChild(arrayIndex, new TrieNode(node.key + c));
            return node.getChild(arrayIndex);
        }

        return node.getChild(arrayIndex);
    }
    /**
     * Inserts string s into the Trie.
     *
     * @param s string to insert into the Trie
     */
    void insert(String s) {
        TrieNode nodePointer = this.root;
        for (int i = 0; i < s.length(); i++)
            nodePointer = insertCharacter(s.charAt(i), nodePointer);
        insertCharacter(WILDCARD, nodePointer);
    }

    /**
     * Checks whether character c exists inside the TrieNode or not.
     *
     * @param c  the character to check for
     * @param node  the node to check whether c exists
     * @return null if character c doesn't exist and returns the next node if it does.
     */
    public TrieNode searchNode(char c, TrieNode node) {
        int arrayIndex = asciiToArrayMap(c);
        return node.getChild(arrayIndex);
    }
    /**
     * Checks whether string s exists inside the Trie or not.
     *
     * @param s string to check for
     * @return whether string s is inside the Trie
     */
    boolean contains(String s) {
        TrieNode currentPointer = this.root;
        for (int i = 0; i < s.length(); i++) {
            currentPointer = searchNode(s.charAt(i), currentPointer);
            if (currentPointer == null)
                return false;
        }
        if (!currentPointer.containsTermination())
            return false;

        return true;
    }

    /**
     * Searches a node and adds 'limit' number of leaves to the results array.
     *
     * @param node  will get all the final leaf words from the node
     * @param results  the results array
     * @param limit  the limit of the results array
     */
    void getAllLeaves(TrieNode node, ArrayList<String> results, int limit) {
        if (node.containsTermination()) {
            if (results.size() >= limit)
                return;
            results.add(node.key);
        }
        for (int i = 0; i < node.getChildrenSize(); i++) {
            int arrayIndex = asciiToArrayMap(node.getChar(i));
            getAllLeaves(node.getChild(arrayIndex), results, limit);
        }
        return;
    }

    void getChildren(String s, int index, TrieNode node, ArrayList<String> results, int limit) {
        if (node == null)
            return;

        if (index == s.length()) {
            getAllLeaves(node, results, limit);
            return;
        }
        if (s.charAt(index) == '.') {
            for (int i = 0; i < node.getChildrenSize(); i++) {
                int arrayIndex = asciiToArrayMap(node.getChar(i));
                TrieNode nextChild = node.getChild(arrayIndex);
                getChildren(s, index + 1, nextChild, results, limit);
            }
            return;
        }

        int arrayIndex = asciiToArrayMap(s.charAt(index));
        TrieNode nextChild = node.getChild(arrayIndex);
        getChildren(s, index + 1, nextChild, results, limit);
        return;
    }
    /**
     * Searches for strings with prefix matching the specified pattern sorted by lexicographical order. This inserts the
     * results into the specified ArrayList. Only returns at most the first limit results.
     *
     * @param s       pattern to match prefixes with
     * @param results array to add the results into
     * @param limit   max number of strings to add into results
     */
    void prefixSearch(String s, ArrayList<String> results, int limit) {
        getChildren(s, 0, this.root, results, limit);
    }


    // Simplifies function call by initializing an empty array to store the results.
    // PLEASE DO NOT CHANGE the implementation for this function as it will be used
    // to run the test cases.
    String[] prefixSearch(String s, int limit) {
        ArrayList<String> results = new ArrayList<>();
        prefixSearch(s, results, limit);
        return results.toArray(new String[0]);
    }


    public static void main(String[] args) {
        Trie t = new Trie();
        t.insert("peter");
        t.insert("piper");
        t.insert("picked");
        t.insert("a");
        t.insert("peck");
        t.insert("of");
        t.insert("pickled");
        t.insert("peppers");
        t.insert("pepppito");
        t.insert("pepi");
        t.insert("pik");
        t.insert("zebra");
        // System.out.println(t.root.getChild(51));
        // System.out.println(t.contains("peter"));
        // System.out.println(t.contains("pete"));
        // String[] result1 = t.prefixSearch("pe", 10);
        // String[] result2 = t.prefixSearch("p..p", 10);
        // System.out.println(Arrays.toString(result1));
        // System.out.println(Arrays.toString(result2));
        // result1 should be:
        // ["peck", "pepi", "peppers", "pepppito", "peter"]
        // result2 should contain the same elements with result1 but may be ordered arbitrarily
    }
}
