

import java.util.*;

public class ATC {
    public String airportName;
    public String hcode;
    public Queue<Flight> readyQueue = new LinkedList<>();
    public Queue<Flight> waitQueue = new PriorityQueue<>(Comparator.comparing(Flight::compareWaitTimes));
    public Queue<Flight> tempWaitQueue = new PriorityQueue<>(Comparator.comparing(Flight::compareWaitTimes));
    public Queue<Flight> tempQueueOld = new PriorityQueue<>(Comparator.comparing(Flight::compareCodes));
    public Queue<Flight> tempQueueNew = new PriorityQueue<>(Comparator.comparing(Flight::compareCodes));
    public ATC(String airportName) {
        this.airportName = airportName;
    }
    public int runCounter=0;


    public int run(int forward){
        if (readyQueue.size() ==0){
            return -1;
        }
        Flight curr = readyQueue.peek();
        if(curr.process(false,forward)){
            readyQueue.poll();
        }
        return 1;
    }


    public int waitATC(int forward){
        if (waitQueue.size() ==0){
            return -1;
        }
        Flight curr = waitQueue.peek();
        curr.process(false,forward);
        return 0;

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
    }


}
