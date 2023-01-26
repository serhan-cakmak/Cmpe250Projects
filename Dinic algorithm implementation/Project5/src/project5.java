import java.io.*;

import java.util.ArrayList;

public class project5 {
    public static void main(String[] args) throws IOException {
        File file = new File(args[0]);
        /*Instant time = Instant.now();*/
        /*File file = new File("updated_test_cases\\input_200.txt");*/
        /*File file = new File("sample_input.txt");*/
        BufferedReader br = new BufferedReader(new FileReader(file));
        FileWriter fileWriter = new FileWriter(args[1]);
        /*FileWriter fileWriter = new FileWriter("output.txt");*/

        Dinic dinic = new Dinic( Integer.parseInt( br.readLine()));
        dinic.fileWriter = fileWriter;
        int a = 0;
        for (String i : br.readLine().split(" ")){
            int cur = Integer.parseInt(i);
            dinic.add(dinic.s, a ,  cur ,0 );
            dinic.nameMap.put("r"+a, a );                   //namemap starts with 6 element 5th index
            dinic.indexArr[a] = "r" + a;

            a++;
        }
        dinic.nameMap.put("KL", dinic.t);
        dinic.indexArr[dinic.t] = "KL";

        String st;
        while ((st =br.readLine()) !=null){
           String[] infos =  st.split(" ");
            if (!dinic.nameMap.containsKey(infos[0])){
                dinic.nameMap.put(infos[0],a);
                dinic.indexArr[a] = infos[0];
                a++;
            }
            for (int j =1, t =2; t <infos.length; j =j+2, t = t+2 ){

                if (!dinic.nameMap.containsKey(infos[j]) ){
                    dinic.nameMap.put(infos[j],a);
                    dinic.indexArr[a] = infos[j];
                    a++;
                }

                dinic.add(dinic.nameMap.get(infos[0]),dinic.nameMap.get(infos[j]), Integer.parseInt( infos[t]), 0 );
            }
        }


        /*System.out.println( dinic.flow());*/
        dinic.flow();
        /*Instant time2 = Instant.now();
        System.out.println("time  : " +  Duration.between(time, time2).toMillis());*/
        /*fileWriter.write( ""+dinic.flow());*/
        /*System.out.println(dinic.minCutPath());*/
        fileWriter.close();
    }
}
