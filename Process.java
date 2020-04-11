import java.util.ArrayList;
import java.util.List;

public class Process extends Thread {
    int arrivalTime;
    int burstTime;
    int processNumber;
    int key;
    VMM VMMObject;
    List<Commands> commandList = new ArrayList<Commands>();


    Process(int arrival, int burst, int number, List<Commands> listOfCommands,VMM vmm) {
        arrivalTime = arrival;
        burstTime = burst;
        processNumber = number;
        commandList = listOfCommands;
        VMMObject = vmm;
    }

    public int getArrival() {
        return arrivalTime;

    }

    public void setArrival(int arrTime) {
        arrivalTime = arrTime;
    }

    public Boolean isFinished() {
        if (this.burstTime == 0) {
            return true;
        }
        else {
            return false;
        }

    }

    public int getBurstTime() {
        return burstTime;
    }

    public void run() {
        //process shouldn't run if burstTime has reached 0
      
        //TODO: Last Access Time
        //TODO: Clock -->remaining burst time

        while (burstTime > 0) {
            //put this piece of code in appropriate place
        for (Commands com: commandList) {
            if (com.getVMMFunction().equals("Store")) {
                //call memStore
                VMMObject.memStore(com.getID(), com.getValue());
                
            }
            else if (com.getVMMFunction().equals("Lookup")) {
                //call memLookup
                VMMObject.memLookup(com.getID());

            }
            else if (com.getVMMFunction().equals("Release")) {
                //call memFree
                VMMObject.memFree(com.getID());
            }
            else {
                System.out.println("Invalid command.");
                return;
            }
            burstTime = burstTime - 1000;
        }

        }
        
    }
}