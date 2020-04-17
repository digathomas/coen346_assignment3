import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;


public class OSVMM  {

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {

        //all files to read 
        Scanner processesFile = new Scanner(new File("processes.txt"));
        Scanner memconfigFile = new Scanner(new File("memconfig.txt"));
        Scanner commandsFile = new Scanner(new File("commands.txt"));
        // Scanner virtualMemoryFile = new Scanner(new File("virtualMemory.txt"));

        // initialize variables
        int arrivalTime = 0;
        int burstTime = 0;

        //initialization for the variables
        String vmmFunction = "";
        String variableID= "";
        int variableValue = 0;

        //initialization for the processes 
        int processNumber = 0;
        Clock clock = new Clock(1000);
        AtomicInteger CPUAccess = new AtomicInteger(2);
        // if process key is set to 0, then it does not have one
        int numberOfProcesses = 0;

        // create process lists
        List<Process> processes = new ArrayList<Process>();
        List<Process> ready = new ArrayList<Process>();
        List<Process> running = new ArrayList<Process>();
        List<Process> terminated = new ArrayList<Process>();

       // int vmmlock = 0;
        
        // read memconfigFile and create memory array of type variable
        int numFrames = memconfigFile.nextInt();
        Variable memoryArray[] = new Variable[numFrames];
        memconfigFile.close();

        // read commandsFile
        List<Commands> commandList = new ArrayList<Commands>();
        while (commandsFile.hasNextLine()) {
            String[] command = commandsFile.nextLine().split(" ");

            if (command[0].equals("Store")) { //if the command is store
                vmmFunction = command[0]; //assign what is stored in the first index of array to vmmFunction
                variableID= command[1]; //assign what is stored in the second index of the array to the variableID
                variableValue = Integer.parseInt(command[2]); //assign what is stored in the third index to the value

                Commands command1 = new Commands(vmmFunction, variableID, variableValue);
                commandList.add(command1); //add the command with the read properties into the commandList

            } else if (command[0].equals("Lookup")) { //if the read command is Lookup
                
                vmmFunction = command[0];
                variableID = command[1];
                
                Commands command2 = new Commands(vmmFunction, variableID);
                commandList.add(command2);

            } else if (command[0].equals("Release")) { //if the read command is Release

                vmmFunction = command[0];
                variableID = command[1];

                Commands command3 = new Commands(vmmFunction, variableID);
                commandList.add(command3);

            } else {
                System.out.println("Command not available."); //if there is a function that is not Store, Lookup or Release
            }

        }
        

        // Read processesFile
        VMM virtualMemory = new VMM(memoryArray, clock);
        while (processesFile.hasNextLine()) { //read the porcesses file in the same way
            String[] array1 = processesFile.nextLine().split(" ");

            arrivalTime = Integer.parseInt(array1[0])*1000; //assign the arrival time 
            burstTime = Integer.parseInt(array1[1])*1000; //assign the burst time
            processNumber++; //increment the process number, this will be the process ID
            numberOfProcesses = processNumber; //total number of processes will be the value of the last process ID
            Process pro = new Process(arrivalTime, burstTime, processNumber,commandList,virtualMemory, CPUAccess, clock);
            processes.add(pro); //add the process to the list

        }
        Scheduler schedule = new Scheduler(processes, ready, running, terminated, clock, numberOfProcesses, CPUAccess); //create scheduler 


        processesFile.close();

        Collections.sort(processes, new Comparator<Process>() { //sort the processes according to their arrival times

            @Override
            public int compare(Process p1, Process p2) {
                return p1.arrivalTime - p2.arrivalTime;
            }
        });

        // for (Process pro: processes) {
        //     System.out.println(pro.getArrival());
        // }
        virtualMemory.start();
        schedule.start();
       // virtualMemory.start();
        schedule.join();
        virtualMemory.join();
        
    }
}