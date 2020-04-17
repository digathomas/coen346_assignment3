
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Process extends Thread {

    AtomicInteger CPUAccess;
    int arrivalTime;
    int burstTime;
    int processNumber;
    Clock clock;
    List<Commands> commandList = new ArrayList<Commands>();
    int processTime;
    int lock = 0;
    Commands command;
    VMM virtualMemory;
    
    //process constructor with proper assignments
    Process(int arrival, int burst, int processNum,List<Commands> listOfCommands,VMM lock, AtomicInteger key, Clock clk) {
        arrivalTime = arrival;
        burstTime = burst;
        processNumber = processNum;
        commandList = listOfCommands;
        virtualMemory=lock;
        clock = clk;
        CPUAccess=key;
    
     }

    public int getArrival() { //get the arrival time
        return arrivalTime;

    }

    public int getBurstTime() { //get burst time
        return burstTime;
    }

    public void setLock() { //set lock
        lock=processNumber;
    }
    public int getProcessID() { //get process id
        return processNumber;
    }

    public void setClock(int time) { //set the clock time with passed int
         processTime = time;
    }
    
    public void run() {
        // process shouldn't run if burstTime has reached 0
      
        while (burstTime > 0) 
        {
            while(lock!=processNumber){};
           
            synchronized(commandList) {
               
             
                if(!commandList.isEmpty()) {

                
                    Commands command1 = commandList.remove(0);
                    String function = command1.getVMMFunction();
                    String variableid = command1.getID();
                    int variableval = command1.getValue();
                    //commandList.remove(0);
                   
                    while(virtualMemory.getLockValue() == 1);
                  
                    if (function.equals("Store")) {
                   
                        virtualMemory.whichCommand(command1);
                        virtualMemory.thisProcess(this);
                        virtualMemory.setLockValue(1);
                 
                    }
                    else if (function.equals("Lookup")) {
                        virtualMemory.whichCommand(command1);
                        virtualMemory.thisProcess(this);
                        virtualMemory.setLockValue(1);
                    }
                    else if (function.equals("Release")) {
                        virtualMemory.whichCommand(command1);
                        virtualMemory.thisProcess(this);
                        virtualMemory.setLockValue(1);
                    }

                    while(virtualMemory.getLockValue() == 1);
                    
                burstTime = burstTime - 1000;
                lock=0;
                CPUAccess.getAndIncrement();
                System.out.println(CPUAccess.get());
           
             }
        }

    }
}
}
 