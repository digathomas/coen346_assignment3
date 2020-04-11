import java.util.ArrayList;
import java.util.List;

//import java.lang.String;

public class VMM extends Thread {

    Variable[] memoryArray;
    List<Variable> diskSpace = new ArrayList<Variable>();

    VMM(Variable[] memArray) {

        memoryArray = memArray;

    }

    public int memLookup(String a) {

        for (int i = 0; i < memoryArray.length; i++) {
            if (a.equals(memoryArray[i].getVariableId())) {
                return memoryArray[i].getValue();
            }
        }
        for (Variable var : diskSpace) {
            if (var.getVariableId().equals(a)) {
                for (int i = 0; i < memoryArray.length; i++) {
                    if (memoryArray[i] == null) {
                        memoryArray[i] = var;
                        break; // or return ID?
                    } else {
                        // swap with variable that has least access time

                        int minValue = memoryArray[0].getLastAccessTime();
                        Variable temp;

                        for (int j = 0; j < memoryArray.length; j++) {
                            if (memoryArray[i].getLastAccessTime() < minValue) {
                                minValue = memoryArray[i].getLastAccessTime();
                                temp = memoryArray[i];
                            }
                        }
                        // SWAP
                        // goes from list to array

                    }
                }
            }
        }
        return -1;

    }

    public void memFree(String a) {
        for (int i = 0; i < memoryArray.length; i++) {
            if (a.equals(memoryArray[i].getVariableId())) {
                memoryArray[i] = null;
                return;
            }
            // check if it exists in vm.txt-->disk

        }
    }

    public void memStore(String varID, int val) {
        Variable var = new Variable(varID, val, 0);
        // TODO: check last access time
        for (int i = 0; i < memoryArray.length; i++) {
            if (memoryArray[i] == null) {
                memoryArray[i] = var;
                return;
            } else {
                // write into vm.txt-->disk
                for (Variable var2 : diskSpace) {
                    if (var2.getVariableId().equals(varID)) {
                        diskSpace.add(var);
                    }

                }

            }
        }

    }

    public void run() {

    }

}