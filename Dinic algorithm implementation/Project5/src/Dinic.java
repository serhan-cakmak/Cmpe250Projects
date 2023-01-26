import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Dinic {
    public String[] indexArr ;
    public FileWriter fileWriter;
    public HashMap<String, Integer> nameMap = new HashMap<>();
    public Queue<Integer> q = new LinkedList<>();

    // Stores the graph.
    public ArrayList<ArrayList<Edge>> adj;
    public int n;

    // s = source, t = sink
    public int s;
    public int t;

    public boolean[] blocked;
    public int[] dist;
    public final int baseNum = 6;

    public Dinic(int N) {

        // s is the source, t is the sink, add these as last two nodes.
        n = N + baseNum;
        s = n++;
        t = n++;
        indexArr = new String[n];
        // Everything else is empty.
        blocked = new boolean[n];
        dist = new int[n];
        adj = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            adj.add(new ArrayList<Edge>());
        }
    }
    public void add(int v1, int v2, int cap, int flow) {
        Edge e = new Edge(v1, v2, cap, flow);
        Edge rev = new Edge(v2, v1, 0, 0);
        rev.r = true;
        adj.get(v1).add(rev.reverseEdge = e);
        adj.get(v2).add(e.reverseEdge = rev);
    }

    /** Starting from sink */
    public boolean bfs() {
        q.clear();
        Arrays.fill(dist, -1);
        dist[t] = 0;
        q.add(t);

        while (!q.isEmpty()) {
            int node = q.poll();
            if (node == s)
                return true;
            for (Edge e : adj.get(node)) {
                if (e.reverseEdge.capacity > e.reverseEdge.flow && dist[e.vertex2] == -1) {
                    dist[e.vertex2] = dist[node] + 1;
                    q.add(e.vertex2);
                }
            }
        }

        return dist[s] != -1;
    }

    /**  Dfs proceeds according to bottleneck value */
    public int dfs(int pos, int min) {
        if (pos == t)
            return min;
        int flow = 0;
        for (Edge e : adj.get(pos)) {
            int cur = 0;
            if (!blocked[e.vertex2] && dist[e.vertex2] == dist[pos] - 1 && e.capacity - e.flow > 0) {
                cur = dfs(e.vertex2, Math.min(min - flow, e.capacity - e.flow));
                e.flow += cur;
                e.reverseEdge.flow = -e.flow;
                flow += cur;
            }
            if (flow == min)
                return flow;
        }
        blocked[pos] = flow != min;
        return flow;
    }

    public void flow() throws IOException {
        int ret = 0;
        /** While there is an augmented path from sink to source (be careful about the order!),  continue. */
        while (bfs()) {
            Arrays.fill(blocked, false);

            ret += dfs(s, Integer.MAX_VALUE);
        }
        fileWriter.write(ret+"\n");

        /** In order to find the min cut, first divide graph into 2 disjoint sets. One of them includes Source and the other one includes Sink
         * In this part, we get all the reacheable nodes from source with bfs and find the edges that flow equals to capacity*/
        HashSet<Integer> res  = reversebfs();

        for (int i : res){
            for (Edge e : adj.get(i)){
                if(e.r)
                    continue;
                if (!res.contains(e.vertex2) ){
                        if (e.flow == e.capacity){
                            String q = (indexArr[ e.vertex1] == null) ? "" : indexArr[ e.vertex1]+" ";
                            /*System.out.println(q + indexArr[e.vertex2] +" "+e.flow);*/
                            fileWriter.write(q + indexArr[e.vertex2] +"\n");
                        }
                }
            }
        }
    }




    /** Bfs starting from source, since direction is different with the other bfs we need to change if statements accordingly*/
    public HashSet<Integer> reversebfs() {
        HashSet<Integer> res = new HashSet<>();
        Arrays.fill(blocked, false);
        res.add(s);
        // Set up BFS
        q.clear();
        Arrays.fill(dist, -1);
        dist[s] = 0;
        q.add(s);


        while (!q.isEmpty()) {
            int node = q.poll();
            //cannot happen
            /*if (node == t){
                return res;
            }*/
            for (Edge e : adj.get(node)) {
                if (e.capacity > e.flow && dist[e.vertex2] == -1 ) {
                    /*System.out.println(e.vertex1+" "+e.capacity +" " + e.flow +" " + e.reverseEdge.capacity+" "+e.reverseEdge.flow);*/
                    dist[e.vertex2] = dist[node] + 1;
                    /*System.out.print(indexArr[e.vertex2]+" ");*/
                    q.add(e.vertex2);
                    res.add(e.vertex2);
                }

            }
        }

        return res;
    }




}
