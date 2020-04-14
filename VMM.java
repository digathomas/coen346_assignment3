import java.util.ArrayList;
import java.util.List;

public class VMM extends Thread {

    Variable[] memoryArray;
    List<Variable> diskSpace = new ArrayList<Variable>();

    VMM(Variable[] memoryArray) {

        this.memoryArray = memoryArray;

    }

    public int memLookup(String a) {

        //if variable exists in main memory, return value
        for (int i = 0; i < memoryArray.length; i++) {
            if (a.equals(memoryArray[i].getVariableId())) {
                return memoryArray[i].getValue();
            }
        }
        //check if variable exists in disk 
        //1. check if spot is available in main memory
  
        for (Variable var : diskSpace) {
            if (var.getVariableId().equals(a)) //if found look for space in memoryArray
            {
                
                for (int i = 0; i < memoryArray.length; i++) 
                {
                    if (memoryArray[i] == null) {
                        memoryArray[i] = var;   //if there is space in main memory, insert variable
                        diskSpace.remove(var);
                        return memoryArray[i].getValue(); 
                    } 
    
                }
                        //if there is no available space in the array:
                        // swap with variable that has least access time

                        int minValue = memoryArray[0].getLastAccessTime();
                        Variable temp = new Variable("",0,0);

                        for (int j = 0; j < memoryArray.length; j++) {
                            if (memoryArray[j].getLastAccessTime() < minValue) {
                                minValue = j;         //store index of one with least access value
                                temp = memoryArray[j];//temp is variable to be swapped out
                            }
                        }

                    //SWAP
                     memoryArray[minValue]=var;//store found in mem[j]
                     diskSpace.add(temp);//store old mem[j] in disk
                     diskSpace.remove(var);//remove found
                     return  memoryArray[minValue].getValue();
                 }
        }
        return -1; //variable doesn't exist 

    }

    public void memFree(String a) {
        for (int i = 0; i < memoryArray.length; i++) {
            if (a.equals(memoryArray[i].getVariableId())) {
                memoryArray[i] = null;
                return;
            }
            // check if it exists in vm.txt-->disk
            else {
                for (Variable var: diskSpace) {
                    if (var.getVariableId().equals(a)) {
                        diskSpace.remove(var);
                    }
                    else {
                        System.out.println("Variable does not exist.");
                    }
                }
            }

            

        }
    }

    public void memStore(String varID, int val) {
        Variable var = new Variable(varID, val, 0);
        // TODO: check last access time
        for (int i = 0; i < memoryArray.length; i++) {
            if (memoryArray[i] == null) {
                memoryArray[i] = var;
                
            } else {
                // write into vm.txt-->disk
                diskSpace.add(var);

            }
        }

    }
    
    public void run() {

    }
}