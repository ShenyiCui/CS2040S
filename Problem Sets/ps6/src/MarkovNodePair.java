public class MarkovNodePair<T> {
    public int frequency = 1;
    public T value;

    public MarkovNodePair(T value) {
        this.value = value;
    }

    public void addFrequency() {
        this.frequency++;
    }
}