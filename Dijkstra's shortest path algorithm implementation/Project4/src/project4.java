import java.io.*;


public class project4 {
    public static void main(String[] args) throws IOException {


        /*Instant time = Instant.now();*/
        /*File file = new File("largeCases/input/stressPar.txt");*/
        /*File file = new File(args[0]);*/
        File file = new File("as/input/inp9.in");
        BufferedReader br = new BufferedReader(new FileReader(file));
        /*FileWriter fileWriter = new FileWriter(args[1]);*/
        FileWriter fileWriter = new FileWriter("output.txt");
        Graph graph = new Graph();
        graph.fwriter = fileWriter;

        int vertexNum = Integer.parseInt( br.readLine());

        int flagNum = Integer.parseInt( br.readLine());

        String[] st = br.readLine().split(" ");
        String start = st[0];
        String target = st[1];
        String flags =  br.readLine();




        for (int i = 0; i < vertexNum; i++){
            String[] weightInfo = br.readLine().split(" ");

            if (!graph.vertexMap.containsKey(weightInfo[0])){
                graph.vertexMap.put(weightInfo[0], new Graph.Node(weightInfo[0]));
            }
            Graph.Node current = graph.vertexMap.get(weightInfo[0]);
            for (int j =1, t =2; t <weightInfo.length; j =j+2, t = t+2 ){
                if (!graph.vertexMap.containsKey(weightInfo[j])){
                    graph.vertexMap.put(weightInfo[j], new Graph.Node(weightInfo[j]));
                }
                Graph.Node adjacentNode = graph.vertexMap.get(weightInfo[j]);
                int weight = Integer.parseInt(weightInfo[t]);
                current.adjacentNodes.put(adjacentNode,weight);
                adjacentNode.adjacentNodes.put(current,weight);
            }
        }
        /*Instant time2 = Instant.now();
        System.out.println("time  : " +  Duration.between(time, time2).toMillis());*/
        graph.fwriter.write( graph.findShorthestPath(graph.vertexMap.get(start),graph.vertexMap.get(target)) +"\n");
   /*     Instant time3 = Instant.now();
        System.out.println("time after dijkstra  : " +  Duration.between(time, time3).toMillis());*/
        graph.fwriter.write( graph.destroyer(flags) +"");
 /*       Instant time4 = Instant.now();
        System.out.println("time after flags  : " +  Duration.between(time, time4).toMillis());*/
        graph.fwriter.close();

    }
}
