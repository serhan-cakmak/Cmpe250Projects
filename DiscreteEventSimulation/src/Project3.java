
import java.io.*;

public class Project3 {
    public static void main(String[] args) throws IOException {

        File file = new File(args[0]);
       /* File file = new File("serhan/input3/case18.in");
        FileWriter f = new FileWriter("myouts/milion/casefinal.out");*/

        BufferedReader br = new BufferedReader(new FileReader(file));
        String[] nums = br.readLine().split(" ");

        FileWriter f = new FileWriter(args[1]);

        int accNum =  Integer.parseInt( nums[0]);
        int flightNum = Integer.parseInt( nums[1]);


        ACC acc ;
        Organizer organizer = new Organizer();
        organizer.fwriter = f;

        for (int i = 0; i < accNum; i++){
            String[] atcs = br.readLine().split(" ");
             acc = new ACC(atcs[0]);
             organizer.accs.add(acc);
            for (int j  = 1; j < atcs.length; j++){
                acc.addAtc(atcs[j]);
            }
        }
        String st;
        while ((st =br.readLine()) !=null){

            String[] arr = st.split(" ");

            int[] times = new int[21];
            for (int i =5, a =0 ; i <26; i++, a++){
                times[a] = Integer.parseInt(arr[i]);
            }

            for (ACC trueaccOb : organizer.accs){
                if (trueaccOb.name.equals( arr[2])){
                    Flight flight = new Flight(Integer.parseInt(arr[0]), arr[1], trueaccOb, trueaccOb.getAtc(arr[3]),trueaccOb.getAtc(arr[4]),times );
                    trueaccOb.flights.add(flight);
                }
            }

        }

        organizer.process();
        organizer.fwriter.close();



    }
}
