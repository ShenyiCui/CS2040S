public class NodePowerPair {
    public Node node;
    private int power;

    public NodePowerPair(Node node) {
        this.node = node;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int currentPower() {
        return this.power;
    }

    public boolean canUsePower() {
        return this.power > 0;
    }

    public void resetNode(int allPowers) {
        this.node.resetNode(allPowers);
        this.power = 0;
    }

    @Override
    public String toString() {
        return this.node.toString() + "[power:"+this.power+ "]";
    }
}
