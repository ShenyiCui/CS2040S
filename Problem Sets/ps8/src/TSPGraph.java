public class TSPGraph implements IApproximateTSP {

    @Override
    public void MST(TSPMap map) {
        // TODO: implement this method
    }

    @Override
    public void TSP(TSPMap map) {
        MST(map);
        // TODO: implement the rest of this method.
    }

    @Override
    public boolean isValidTour(TSPMap map) {
        // Note: this function should with with *any* map, and not just results from TSP().
        // TODO: implement this method
        return false;
    }

    @Override
    public double tourDistance(TSPMap map) {
        // Note: this function should with with *any* map, and not just results from TSP().
        // TODO: implement this method
        return 0;
    }

    public static void main(String[] args) {
        TSPMap map = new TSPMap(args.length > 0 ? args[0] : "../hundredpoints.txt");
        TSPGraph graph = new TSPGraph();

        graph.MST(map);
        // graph.TSP(map);
        // System.out.println(graph.isValidTour(map));
        // System.out.println(graph.tourDistance(map));
    }
}
