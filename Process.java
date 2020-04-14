import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Process extends Thread {
    AtomicInteger keyAccess;
    int processId;
    static refProcessId = 0;
    int arrivalTime;
    int burstTime;
    VMM VMMObject;
    List<Commands> commandList = new ArrayList<Commands>();
    
    //Pass AtomicInteger in Process constructor?
    Process(int arrivalTime, int burstTime, List<Commands> commandList, VMM vmmObject, AtomicInteger key) {
        this.refProcessId++;
        this.processId = refProcessId;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.commandList = commandList;
        this.VMMObject = vmmObject;
    }

    int getProcessId() {
        return this.processId;
    }

    int getArrival() {
        return this.arrivalTime;
    }

    public void setArrival(int arrTime) {
        arrivalTime = arrTime;
    }

    public Boolean isFinished() {
        if (this.burstTime == 0) {
            return true;
        } else {
            return false;
        }

    }

    public int getBurstTime() {
        return burstTime;
    }

    public void run() {
        // process shouldn't run if burstTime has reached 0

        // TODO: Last Access Time
        // TODO: Clock -->remaining burst time
        // TODO: Update vm.txt everytime something is added/removed

        while (burstTime > 0) {
            // put this piece of code in appropriate place
            for (Commands com : commandList) {
                if (com.getVMMFunction().equals("Store")) {
                    // call memStore
                    VMMObject.memStore(com.getID(), com.getValue());

                } else if (com.getVMMFunction().equals("Lookup")) {
                    // call memLookup
                    VMMObject.memLookup(com.getID());

                } else if (com.getVMMFunction().equals("Release")) {
                    // call memFree
                    VMMObject.memFree(com.getID());
                } else {
                    System.out.println("Invalid command.");
                    //return;
                }

                burstTime = burstTime - 1000;
                keyAccess.getAndIncrement();
            }
        }
    }
}
 