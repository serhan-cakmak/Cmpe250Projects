
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Organizer {


    public List<ACC> accs = new LinkedList<>();
    public FileWriter fwriter;



    public void addnew(ACC acc, int forward){
        if (acc.flights.size()==0){
            return;
        }
        // forward <= peek

        if (acc.flights.peek().admissionTime < acc.totalTime){
            return;
        }

        while (acc.flights.peek().admissionTime == acc.totalTime){
            acc.tempQueueNew.add( acc.flights.poll());
            if (acc.flights.size()==0){
                return;
            }
        }
    }


    public void initalize(ACC acc){
        if (acc.flights.size()==0){
            return;
        }
        final int real = acc.flights.size();
        acc.totalFlights = real;
        while (acc.flights.peek().admissionTime==0){
            acc.tempQueueNew.add( acc.flights.poll());
            if (acc.flights.size() ==0){
                return;
            }
        }
    }

    public void process() throws IOException {

        for (ACC acc : accs){
            if (acc.flights.size() ==0) {
                printgood(acc);
                continue;
            }
            initalize(acc);
            acc.mergeTemps();

            while (!(acc.isFinished())){

                int forward = findMin(acc);

                acc.totalTime+=forward;
                addnew(acc,forward);
                acc.run(forward);
                acc.waitACC(forward);

                for (ATC atc : acc.atcList) {
                    if (atc!=null){
                        atc.run(forward);
                        atc.waitATC(forward);
                        atc.mergeTemps();
                    }
                }

                if (acc.isFinished()){
                    printgood(acc);

                }
                acc.mergeTemps();

            }

        }
    }

    public int findMin(ACC acc){
        int minTime = Integer.MAX_VALUE;
        int res ;
        if (acc.flights.size()!=0){
            res = acc.flights.peek().admissionTime - acc.totalTime;
            if (res < minTime){
                minTime = res;
            }
        }

        if (acc.readyQueue.size()!=0){
            res= Math.min(acc.readyQueue.peek().times[acc.readyQueue.peek().peekIndex], 30-acc.runCounter );
            if (res < minTime){
                minTime = res;
            }
        }

        if (acc.waitQueue.size()!=0){
            res =acc.waitQueue.peek().times[acc.waitQueue.peek().peekIndex] ;
            if (res < minTime){
                minTime = res;
            }
        }

        for (ATC atc: acc.atcList){
            if (atc !=null){
                if (atc.readyQueue.size()!=0){

                    res= atc.readyQueue.peek().times[atc.readyQueue.peek().peekIndex];
                    if (res < minTime){
                        minTime = res;
                    }
                }

                if (atc.waitQueue.size()!=0){
                    res =atc.waitQueue.peek().times[atc.waitQueue.peek().peekIndex] ;
                    if (res < minTime){
                        minTime = res;
                    }
                }
            }
        }
        return minTime;

    }



    public void printgood(ACC acc) throws IOException {
            fwriter.write(acc.name + " " + acc.totalTime );
            /*System.out.print(acc.name + " " + acc.totalTime + " ");*/
            for (ATC j : acc.airportArray){
                if (j !=null){
                    fwriter.write(" "+j.airportName +j.hcode );
                    /*System.out.print( j.airportName  +" ");*/
                }

        }
            fwriter.write("\n");
        /*System.out.println();*/
    }




}
