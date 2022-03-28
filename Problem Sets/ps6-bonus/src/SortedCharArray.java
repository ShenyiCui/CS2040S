import java.util.ArrayList;

public class SortedCharArray extends ArrayList<Character> {
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