import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class VMM extends Thread {

    Variable[] memoryArray;
    List<Variable> diskSpace = new ArrayList<Variable>();
    Clock clock;
    AtomicInteger vmmlock = new AtomicInteger(0);
    //List<Commands> commandList = new ArrayList<Commands>();
    Process pro;
    Commands com;

    //vmm constructor 
    VMM(Variable[] memArray, Clock clk) {

        memoryArray = memArray;
       // commandList = listOfCommands;
        clock = clk;
        //command1 =  com;

    }
    public synchronized int getLockValue() {
        return vmmlock.get();
    }
    public synchronized void setLockValue(int lockval) {
        synchronized(this) {
        vmmlock.set(lockval);
        }
    }
    public synchronized void thisProcess(Process p) {
        pro = p;
    }
    public void whichCommand(Commands c) {
        com = c;
    }
    public synchronized int memLookup(String a) {

        // if variable exists in main memory, return value
        for (int i = 0; i < memoryArray.length; i++) {
            if (a.equals(memoryArray[i].getVariableId())) {
                memoryArray[i].setLastAccessTime(clock.getTime());
                return memoryArray[i].getValue();
            }
        }
        // check if variable exists in disk
        // 1. check if spot is available in main memory

        for (Variable var : diskSpace) {
            if (var.getVariableId().equals(a)) // if found look for space in memoryArray
            {
                var.setLastAccessTime(clock.getTime());
                for (int i = 0; i < memoryArray.length; i++) {
                    if (memoryArray[i] == null) {
                        memoryArray[i] = var; // if there is space in main memory, insert variable
                        diskSpace.remove(var);
                        return memoryArray[i].getValue();
                    }

                }
                // if there is no available space in the array:
                // swap with variable that has least access time

                int minValue = 0; //make the min value the first value of array
                Variable temp = memoryArray[0];
  

                for (int j = 0; j < memoryArray.length; j++) {
                    if (memoryArray[j].getLastAccessTime() < temp.getLastAccessTime()) { //compare value at index j with min value
                        minValue = j; // store index of one with least access value
                        temp = memoryArray[j];// temp is variable to be swapped out
                    }
                }

                // SWAP
                System.out.println("Clock: "+ clock.getTime() + " Memory Manager, SWAP: Variable" + temp.getVariableId() + "with Variable: " + var.getVariableId());
                memoryArray[minValue] = var;// store found in mem[j]
                diskSpace.add(temp);// store old mem[j] in disk
                diskSpace.remove(var);// remove found
                return memoryArray[minValue].getValue();
            }
        }
        return -1; // variable doesn't exist

    }

    public synchronized void memFree(String a) {
        for (int i = 0; i < memoryArray.length; i++) {
            if (a.equals(memoryArray[i].getVariableId())) { //check to see if the passed string matches with any id in the array
                memoryArray[i].setLastAccessTime(clock.getTime());
                memoryArray[i] = null; //make this index empty
                return;
            }
            // check if it exists in vm.txt-->disk
            else {
                for (Variable var : diskSpace) { //check the diskspace if its not in memory array
                    if (var.getVariableId().equals(a)) {
                        var.setLastAccessTime(clock.getTime()); 
                        diskSpace.remove(var); //remove it from the list
                    } else {
                        System.out.println("Variable does not exist."); //if in neither array nor disk, it doesnt exist
                    }
                }
            }

        }
    }

    public synchronized void memStore(String varID, int val) {
        Variable var = new Variable(varID, val, 0); //store the variable with passed ID and value
        var.setLastAccessTime(clock.getTime()); //set the last access time of this variable to the clock time
     
        for (int i = 0; i < memoryArray.length; i++) {
            if (memoryArray[i] == null) { //if there's an empty space in the memory array
                memoryArray[i] = var; //insert the variable at the empty index
                return;
            } else {
                // write into vm.txt-->disk
                diskSpace.add(var); //otherwise, put it in the disk list

            }
        }

    }
    
    public void run() {

        while (true) { //always running 
           
            while(vmmlock.get() == 0){};
 
            synchronized(this) {
         
            if (com.getVMMFunction().equals("Store")) {
                // call memStore
                System.out.println("Store is called");
                memStore(com.getID(), com.getValue());
                System.out.println(" Clock: " + (clock.getTime()+500) + " Process: " + pro.getProcessID() + " Store: Variable: " + com.getID() + " Value: " + com.getValue());


            } else if (com.getVMMFunction().equals("Lookup")) {
                // call memLookup
                memLookup(com.getID());
                System.out.println(" Clock: " + (clock.getTime()+500) + " Process: " + pro.getProcessID() + " Lookup: Variable: " + com.getID() + " Value: " + com.getValue());

            } else if (com.getVMMFunction().equals( "Release")) {
                // call memFree
                memFree(com.getID());
                System.out.println(" Clock: " + (clock.getTime()+500) + " Process: " + pro.getProcessID() + " Release: Variable: " + com.getID());
            } else {
                System.out.println("Invalid command.");
            
            }
           
        FileWriter filewriter = null;
        try {
            filewriter = new FileWriter("vm.txt");
            
        for (Variable var : diskSpace) {
       
        filewriter.write("Variable ID:" + var.getVariableId() + "Value:" + var.getValue() + "LAT:" + var.getLastAccessTime() + "\n");
        }
        filewriter.close();
    }
        catch (IOException e) {
           
            e.printStackTrace();
        }
        vmmlock.set(0);
    }
  
}
 

}

}