import java.io.FileWriter;
import java.util.*;

public class Graph {
    public static class Node implements Comparable<Node> {
        public String label;
        public Map<Node, Integer> adjacentNodes;
        boolean check ;

        public int cost;

        public Node(String label) {
            this.label = label;
            adjacentNodes = new HashMap<>();
            cost = Integer.MAX_VALUE;
            check = false;
        }

        @Override
        public int compareTo(Node otherNode) {
            return Integer.compare(this.cost, otherNode.cost);
        }
    }

    public HashMap<String, Node> vertexMap;
    public PriorityQueue<Node> heap;
    public HashSet<Node> flagNames = new HashSet<>();

    public FileWriter fwriter;

    public Graph() {
        vertexMap = new HashMap<>();
        heap = new PriorityQueue<>();
    }

    public int findShorthestPath(Node start, Node target) {

        HashSet<Node> settledNodes = new HashSet<>();
        start.cost = 0;
        heap.add(start);

        while (settledNodes.size() != vertexMap.size()) {
            if (heap.isEmpty()) {
                return -1;                     // if graph is not connected
            }

            Node closestNode = heap.poll();

            if (settledNodes.contains(closestNode)) {
                if (closestNode == target){
                    return closestNode.cost;
                }
                continue;
            }
            settledNodes.add(closestNode);

            for (Map.Entry<Node, Integer> adjacentPair : closestNode.adjacentNodes.entrySet()) {
                Node adjacentNode = adjacentPair.getKey();
                int weight = adjacentPair.getValue();

                if (!settledNodes.contains(vertexMap.get(adjacentNode.label))) {

                    if (closestNode.cost + weight < adjacentNode.cost) {
                        adjacentNode.cost = closestNode.cost + weight;
                        if (heap.contains(adjacentNode)){
                            heap.remove(adjacentNode);
                        }
                        heap.add(adjacentNode);

                    }

                }
            }
        }
        return target.cost;

    }

    public int destroyer (String flags){
        String[] flagArr = flags.split(" ");
        for (String s : flagArr){
            flagNames.add(vertexMap.get(s));
        }
        resetFirst();
        return  modifiedDijkstra( vertexMap.get(flagArr[0]));

    }

    public void resetFirst() {

        for (Graph.Node nd : vertexMap.values()) {
            nd.cost = Integer.MAX_VALUE;
        }
        heap.clear();
    }


    public int modifiedDijkstra(Node start) {
        HashSet<Node> settledFlag = new HashSet<>();
        start.cost = 0;
        heap.add(start);
        start.check =true;
        int res = 0;
        while (settledFlag.size() != flagNames.size()) {
            if (heap.isEmpty()) {
                return -1;
            }
            Node closestNode = heap.poll();
            closestNode.check = false;
            if (settledFlag.contains(closestNode)){
                continue;
            }
            if (flagNames.contains(closestNode)) {
                /*System.out.println(closestNode.label+ " " +closestNode.cost );*/
                res += closestNode.cost;
                settledFlag.add(closestNode);

                for (Map.Entry<Node, Integer> adjacentPair : closestNode.adjacentNodes.entrySet()) {
                    Node adjacentNode = adjacentPair.getKey();
                    int weight = adjacentPair.getValue();
                    if (!settledFlag.contains(adjacentNode)) {
                        if (weight < adjacentNode.cost) {

                            adjacentNode.cost = weight;
                            /*adjacentNode.shortestPath.add(closestNode.label);
                            adjacentNode.parentFlag = closestNode.label;*/
                            if (adjacentNode.check ){
                                heap.remove(adjacentNode);
                            }
                            heap.add(adjacentNode);
                            adjacentNode.check =true;
                        }

                    }
                }
            } else {

                for (Map.Entry<Node, Integer> adjacentPair : closestNode.adjacentNodes.entrySet()) {
                    Node adjacentNode = adjacentPair.getKey();
                    int weight = adjacentPair.getValue();
                    if (!settledFlag.contains(adjacentNode)) {
                        if (closestNode.cost + weight < adjacentNode.cost) {
                            adjacentNode.cost = closestNode.cost + weight;

                            if (adjacentNode.check ){
                                heap.remove(adjacentNode);
                            }
                            heap.add(adjacentNode);
                            adjacentNode.check =true;

                        }

                    }


                }
            }

        }return res;
    }


    /**  My first approach was to construct a new graph which only includes Flag nodes with adjacency matrices, then applying Prim's algortihm on it.
     * But the time complexity was a bit more costly than it should, so I changed the main algorithm */


    /*   public  void resetCosts(){
        for (Node nd : vertexMap.values()){
            nd.cost = Integer.MAX_VALUE;
        }
        heap.clear();
    }*//*

    public void resetPrim() {
        for (String flag : flagNames){
            vertexMap.get(flag).adjacentNodes.clear();
            vertexMap.get(flag).cost = Integer.MAX_VALUE;
        }
        heap.clear();
    }
    public void resetFirst() {
        for (Graph.Node nd : vertexMap.values()) {
            nd.cost = Integer.MAX_VALUE;
        }
        heap.clear();
    }

    public void resetCosts(String start) {
        for (Graph.Node nd : vertexMap.values()) {
            if (!start.equals(nd.label) && flagNames.contains(nd.label) && nd.cost != Integer.MAX_VALUE) {
                System.out.println(start+" "+ nd.label+ " " +nd.cost);
                pairHeap.add(new Pair(start, nd.label, nd.cost));
            }
            nd.cost = Integer.MAX_VALUE;
        }
        heap.clear();
    }

    private class Pair {
        String node1;
        String node2;
        int cost;

        public Pair(String node1, String node2, int cost) {
            this.node1 = node1;
            this.node2 = node2;
            this.cost = cost;
        }

        @Override
        public String toString() {
            return "Pair{" +
                    "node1='" + node1 + '\'' +
                    ", node2='" + node2 + '\'' +
                    ", cost=" + cost +
                    '}';
        }

        public int getCost() {
            return cost;
        }
    }

    */
/*    public int flagCombinationPath(String flags){
            resetCosts();
            Set<String> settledFlags = new HashSet<>();
            String[] flagArr = flags.split(" ");
            if(flagArr.length <=1){
                return 0;
            }
            HashMap<String, Integer> pairsDistance = new HashMap<>();
            PriorityQueue<Pair> pq = new PriorityQueue<>(Comparator.comparing(Pair::getCost));
            int sum = 0;
            for (int i = 0; i < flagArr.length-1; i++){
                for (int j =i+1; j < flagArr.length; j++){
                    pairsDistance.put(flagArr[i]+" "+ flagArr[j], findShorthestPath(vertexMap.get(flagArr[i]),vertexMap.get(flagArr[j])));
                    pq.add(new Pair(flagArr[i],flagArr[j],findShorthestPath(vertexMap.get(flagArr[i]),vertexMap.get(flagArr[j]))));

                    resetCosts();
                }
            }
            while (settledFlags.size() < flagArr.length ){
                Pair pair = pq.poll();
                if (settledFlags.contains(pair.node1) && settledFlags.contains(pair.node2)){
                    continue;
                }
                settledFlags.add(pair.node1);
                settledFlags.add(pair.node2);
                sum += pair.cost;

            }


            return sum;


        }*//*

    public int flagCombinationPath(String flags) {
        resetFirst();
        */
    /*Set<String> settledFlags = new HashSet<>();*//*

        String[] flagArr = flags.split(" ");
        for (String flag : flagArr){
            flagNames.add(flag);
        }
        if (flagArr.length <= 1) {
            return 0;
        }

        for (String flag : flagArr) {
            findShorthestPath(vertexMap.get(flag), vertexMap.get(flagArr[0]));
            resetCosts(flag);
        }

        resetPrim();
        while(!pairHeap.isEmpty() ){
            Pair pair = pairHeap.poll();
            vertexMap.get(pair.node1).adjacentNodes.put(vertexMap.get(pair.node2), pair.cost);
            vertexMap.get(pair.node2).adjacentNodes.put(vertexMap.get(pair.node1), pair.cost);
        }
        Graph.Node flag = vertexMap.get(flagArr[1]);

        int sum = 0;

        int res = prim(flag);
        if (res !=-1){
            for (String flagName: flagArr){

                System.out.println(flagName +" " + sum);
                sum +=vertexMap.get(flagName).cost;

                System.out.println(flagName +" " + sum);
            }
        }else {
            return -1;
        }

*/
/*        while (settledFlags.size() < flagArr.length) {
            if (pairHeap.size() ==0){
                return -1;
            }
            Pair pair = pairHeap.poll();
            if (settledFlags.contains(pair.node1) && settledFlags.contains(pair.node2)) {
                continue;
            }
            settledFlags.add(pair.node1);
            settledFlags.add(pair.node2);
            sum += pair.cost;

        }*//*

        return sum;


    }


    public int prim(Graph.Node start) {

        HashSet<Graph.Node> settledNodes = new HashSet<>();
        start.cost = 0;
        heap.add(start);

        while (settledNodes.size() != flagNames.size()) {
            if (heap.isEmpty()) {
                return -1;                     // if graph is not connected
            }

            Graph.Node closestNode = heap.poll();

            if (settledNodes.contains(closestNode)) {
                continue;
            }
            settledNodes.add(closestNode);

            for (Map.Entry<Graph.Node, Integer> adjacentPair : closestNode.adjacentNodes.entrySet()) {
                Graph.Node adjacentNode = adjacentPair.getKey();
                int weight = adjacentPair.getValue();

                if (!settledNodes.contains(adjacentNode)) {

                    if ( weight < adjacentNode.cost) {
                        adjacentNode.cost =  weight;
                        heap.add(adjacentNode);
                    }

                }
            }

        }
        return 1;

    }

}
*/






}
