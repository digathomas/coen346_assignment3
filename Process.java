import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Process extends Thread {
    AtomicInteger keyAccess;
    int processId;
    static int refProcessId = 0;
    int arrivalTime;
    int burstTime;
    VMM VMMObject;
    List<Commands> commandList = new ArrayList<Commands>();
    String output;

    // Pass AtomicInteger in Process constructor?
    Process(int arrivalTime, int burstTime, List<Commands> commandList, VMM vmmObject, AtomicInteger key) {
        nextProcessId();
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

    String getOutput() {
        return this.output;
    }

    public void setArrival(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void nextProcessId() {
        refProcessId++;
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
            for (Commands command : commandList) {
                if (command.getVMMFunction().equals("Store")) {
                    // call memStore
                    VMMObject.memStore(command.getId(), command.getValue());
                    output = "Process " + processId + ", " + command.getVMMFunction() + ": Variable " + command.getId()
                            + ", Value: " + command.getValue();

                } else if (command.getVMMFunction().equals("Lookup")) {
                    // call memLookup
                    VMMObject.memLookup(command.getId());
                    output = "Process " + processId + ", " + command.getVMMFunction() + ": Variable " + command.getId()
                            + ", Value: " + command.getValue();

                } else if (command.getVMMFunction().equals("Release")) {
                    // call memFree
                    VMMObject.memFree(command.getId());
                    output = "Process " + processId + ", " + command.getVMMFunction() + ": Variable " + command.getId();
                } else {
                    System.out.println("Invalid command.");
                    // return;
                }

                burstTime = burstTime - 1000;
                keyAccess.getAndIncrement();
            }
        }
    }
}
