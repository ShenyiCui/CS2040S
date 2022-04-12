import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class TSPGraph implements IApproximateTSP {

    TreeMapPriorityQueue<Double, Integer> pq = new TreeMapPriorityQueue<>();
    HashSet<Integer> s = new HashSet<>();
    HashMap<Integer, Integer> parent = new HashMap<>();

    HashMap<Integer, LinkedList<Integer>> children = new HashMap<>();
    HashSet<Integer> alreadyVisited = new HashSet<>();
    Integer lastNonVisitedNode = null;

    @Override
    public void MST(TSPMap map) {
        // Prim's Initialisation
        for (Integer i = 0; i < map.getCount(); i++) {
            this.pq.add(i, Double.MAX_VALUE);
        }
        pq.decreasePriority(0, 0d);
        this.s.add(0);
        this.parent.put(0, null);

        // Prim's Algo
        while (!pq.isEmpty()) {
            Integer v = this.pq.extractMin();
            if (parent.get(v) != null) {
                map.setLink(v, parent.get(v), false);
            }

            this.s.add(v);
            for (int w = 0; w < map.getCount(); w++) {
                if (this.s.contains(w)) continue;
                this.pq.decreasePriority(w, map.pointDistance(v, w));
                if (pq.lookup(w) < map.pointDistance(v, w)) continue;
                parent.put(w, v);
            }
        }
        map.redraw();
    }

    @Override
    public void TSP(TSPMap map) {
        MST(map);
        this.getChildren();
        this.DFS(0, map);
        map.setLink(0, this.lastNonVisitedNode);
        map.redraw();
    }

    public void DFS(Integer currentNode, TSPMap map) {
        LinkedList<Integer> nodeChildren = this.children.get(currentNode);
        if (this.lastNonVisitedNode != null) {
            map.setLink(currentNode, this.lastNonVisitedNode, false);
        }
        if (!this.alreadyVisited.contains(currentNode)) {
            this.lastNonVisitedNode = currentNode;
            this.alreadyVisited.add(currentNode);
        }
        while (nodeChildren != null && !nodeChildren.isEmpty()) {
            Integer nextChild = nodeChildren.poll();
            DFS(nextChild, map);
        }
    }

    public void getChildren() {
        for (Integer child : this.parent.keySet()) {
            Integer parent = this.parent.get(child);
            if (this.children.get(parent) == null) {
                LinkedList<Integer> temp = new LinkedList<>();
                temp.add(child);
                this.children.put(parent, temp);
                continue;
            }
            this.children.get(parent).add(child);
        }
    }

    @Override
    public boolean isValidTour(TSPMap map) {
        HashSet<Integer> alreadyVisited = new HashSet<>();
        Integer startNode = 0;
        Integer nextNode = 0;
        for (int i = 0; i < map.getCount(); i++) {
            if (nextNode == -1) return false;
            if (alreadyVisited.contains(nextNode)) return false;
            alreadyVisited.add(nextNode);
            nextNode = map.getLink(nextNode);
        }
        return nextNode == startNode;
    }

    @Override
    public double tourDistance(TSPMap map) {
        if (!isValidTour(map)) return -1;
        Integer nextNode = 0;
        Integer prevNode = 0;
        Double accumulatedDistance = 0d;
        for (int i = 0; i < map.getCount() + 1; i++) {
            accumulatedDistance += map.pointDistance(prevNode, nextNode);
            prevNode = nextNode;
            nextNode = map.getLink(nextNode);
        }
        return accumulatedDistance;
    }

    public static void main(String[] args) {
        // 105.8381124
        TSPMap map = new TSPMap(args.length > 0 ? args[0] : "test.txt");
        TSPGraph graph = new TSPGraph();

        graph.MST(map);
        graph.TSP(map);
        System.out.println(graph.isValidTour(map));
        System.out.println(graph.tourDistance(map));
    }
}
