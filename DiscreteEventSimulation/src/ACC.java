
import java.util.*;

public class ACC {
    public String name;
    public Queue<Flight> readyQueue = new LinkedList<>();
    public ATC[] airportArray = new ATC[1000];
    public Queue<Flight> waitQueue = new PriorityQueue<>(Comparator.comparing(Flight::compareWaitTimes));     /** This is changed with new version */

    /*public List<Flight> tempWaitQueue = new LinkedList<>();*/
    public Queue<Flight> tempWaitQueue = new PriorityQueue<>(Comparator.comparing(Flight::compareWaitTimes));
    public Queue<Flight> tempQueueOld = new PriorityQueue<>(Comparator.comparing(Flight::compareCodes));
    public Queue<Flight> tempQueueNew = new PriorityQueue<>(Comparator.comparing(Flight::compareCodes));

    public List<ATC> atcList = new LinkedList<>();

    public  int runCounter = 0;
    public  int totalTime = 0;

    public int totalFlights ;
    public int totaloutNumber=0;
    public Queue<Flight> flights = new PriorityQueue<>(Comparator.comparing(Flight::compareAdmissionTime));


    public ACC(String name) {
        this.name = name;
    }

    public int run(int forward){
        if (readyQueue.size() ==0){
            return -1;
        }
        Flight curr = readyQueue.peek();
        if (runCounter+forward != 30){

            boolean res =  curr.process(false, forward);
            if  (res){
                readyQueue.poll();
                runCounter = 0;
                return 2;
            }else {
                runCounter+=forward;
            }
        }else {
            readyQueue.poll();
            curr.process(true, forward);
            runCounter = 0;
            return 2;
        }
        return 0;
    }
/*    public int waitACC(int forward){
        if (waitQueue.size() ==0){
            return -1;
        }

        Flight curr = waitQueue.peek();
        curr.process(false);
        return 0;

    }*/
public int waitACC(int forward){
    if (waitQueue.size() ==0){
        return -1;
    }

    Flight curr = waitQueue.peek();
    curr.process(false,forward);
    return 0;

}


    public boolean isFinished(){
        return totaloutNumber == totalFlights;
    }


    public  void  mergeTemps(){


        while (tempQueueNew.size() !=0){
            readyQueue.add(tempQueueNew.poll());
        }

        while (tempQueueOld.size() !=0) {
            readyQueue.add(tempQueueOld.poll());
        }


        while (tempWaitQueue.size() !=0) {
            waitQueue.add(tempWaitQueue.poll());
        }
        /*if (tempWaitQueue !=null){

            for (Flight i : tempWaitQueue){
                waitQueue.add(i);
            }
            tempWaitQueue.clear();
        }*/
    }


/**
    This part creates Atc objects and hash them
 */
    public void addAtc(String code){
        int res = hash(code);

        while (airportArray[res] !=null){                   //linear probing
            res++;
            if (res ==1000){
                res = 0;
            }
        }
        String res1 =  res+"";
        airportArray[res] = new ATC( code );
        atcList.add(airportArray[res]);
        airportArray[res].hcode = "0".repeat(3-res1.length())+res1;
    }


    public ATC getAtc(String code){
        int res = hash(code);
        while (airportArray[res]!=null){
            if (airportArray[res].airportName.equals( code)){
                return airportArray[res];
            }
            res++;
            if (res ==1000){
                res = 0;
                if (airportArray[res]==null){
                    return airportArray[999];
                }
            }
        }
        return airportArray[res-1];
    }
    public int hash(String code){
        long res = 0;
        for (int i = 0; i < code.length(); i++){
            res += code.charAt(i)* Math.pow(31,i);
        }
        return (int) (res % 1000);
    }

}
