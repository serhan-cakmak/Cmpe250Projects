

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Flight implements Comparable<Flight> {
    public int admissionTime;
    public String flightCode;
    public ACC acc;
    public ATC departurePort;
    public ATC arrivalPort;
    public int[] times ;
    public int peekIndex = 0;



    public Flight(int admissionTime, String flightCode, ACC acc, ATC departurePort, ATC arrivalPort, int[]times) {
        this.admissionTime = admissionTime;

        this.flightCode = flightCode;
        this.acc = acc;
        this.departurePort = departurePort;
        this.arrivalPort = arrivalPort;
        this.times = times;
    }

    public boolean process(boolean is30, int forward){
        switch (peekIndex){

            case 2:               //ACC to ATC Departure
                times[peekIndex]-= forward;
                if (times[peekIndex] == 0){
                    peekIndex++;
                    departurePort.tempQueueNew.add(this);
                    return true;
                }
                if (is30){
                    acc.tempQueueOld.add(this);
                    return true;
                }

                return false;
            case 12:               //ACC to ATC Arrival
                times[peekIndex]-= forward;
                if (times[peekIndex] == 0){
                    peekIndex++;
                    arrivalPort.tempQueueNew.add(this);
                    return true;
                }
                if (is30){
                    acc.tempQueueOld.add(this);
                    return true;
                }
                return false;
            case 9,19:               //ATC departure to ACC
                times[peekIndex]-= forward;
                if (times[peekIndex] == 0){
                    peekIndex++;
                    acc.tempQueueNew.add(this);
                    return true;
                }
                return false;
            case 3,5,7:        //DepartureATC Running
                times[peekIndex]-= forward;
                if (times[peekIndex] == 0){
                    peekIndex++;
                    departurePort.tempWaitQueue.add(this);
                    return true;
                }
                return false;
            case 13,15,17:       //Arrival running
                times[peekIndex]-= forward;
                if (times[peekIndex] == 0){
                    peekIndex++;
                    arrivalPort.tempWaitQueue.add(this);
                    return true;
                }
                return false;
            case 4,6,8:        //DepartureATC wait to Departure ATC running
                List<Flight> temp = new LinkedList<>();
                for (Flight ob : departurePort.waitQueue){
                    ob.times[ob.peekIndex]-=forward;
                    if (ob.times[ob.peekIndex] ==0){
                        ob.peekIndex++;
                        temp.add(ob);

                        departurePort.tempQueueNew.add(ob);
                    }
                }
                atcWaitQueueRemove(departurePort, temp);
                temp.clear();
                return false;
            case  14,16,18:
                temp = new LinkedList<>();
                for (Flight ob : arrivalPort.waitQueue) {
                    ob.times[ob.peekIndex]-=forward;
                    if (ob.times[ob.peekIndex] == 0) {
                        ob.peekIndex++;
                        temp.add(ob);

                        arrivalPort.tempQueueNew.add(ob);
                    }
                }
                atcWaitQueueRemove(arrivalPort, temp);
                temp.clear();
                return false;
            case 0, 10, 20:               //ACC running to wait
                times[peekIndex]-=forward;
                if (times[peekIndex] == 0){
                    peekIndex++;
                    if (peekIndex ==21){
                        acc.totaloutNumber++;
                        return true;
                    }
                    acc.tempWaitQueue.add(this);
                    return true;
                }
                if (is30){
                    acc.tempQueueOld.add(this);
                    return true;
                }
                return false;
            default:         //src.ACC wait
                temp = new LinkedList<>();
                for (Flight ob : acc.waitQueue) {
                    ob.times[ob.peekIndex]-=forward;
                    if (ob.times[ob.peekIndex] == 0) {
                        ob.peekIndex++;
                        temp.add(ob);
                        acc.tempQueueNew.add(ob);
                    }
                }
                accWaitQueueRemove(acc, temp);
                return false;
        }
    }
    public void accWaitQueueRemove(ACC acc, List<Flight> tmp){
        for (Flight i : tmp){
            acc.waitQueue.remove(i);
        }
    }
    public void atcWaitQueueRemove(ATC atc, List<Flight> tmp){
        for (Flight i : tmp){
            atc.waitQueue.remove(i);
        }
    }
    @Override
    public int compareTo(Flight o) {
        return  times[peekIndex]- o.times[o.peekIndex];
    }
    public String compareCodes(){
        return flightCode;
    }
    public  int compareAdmissionTime(){
        return admissionTime;
    }
    public  int compareWaitTimes(){
        return times[peekIndex];
    }




}
