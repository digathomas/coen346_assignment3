import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;


public class OSVMM extends Thread {

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {

        Scanner processesFile = new Scanner(new File("processes.txt"));
        Scanner memconfigFile = new Scanner(new File("memconfig.txt"));
         Scanner commandsFile = new Scanner(new File("commands.txt"));
        // Scanner virtualMemoryFile = new Scanner(new File("virtualMemory.txt"));

        // initialize variables
        int arrivalTime = 0;
        int burstTime = 0;
        int processNumber = 0;
        String vmmFunction = "";
        String id = "";
        int value = 0;
        int startTime = 0;
        Clock clock1 = new Clock(startTime);
        AtomicInteger key = new AtomicInteger(0); // if process key is set to 0, then it does not have one

        // create process lists
        List<Process> processes = new ArrayList<Process>();
        List<Process> ready = new ArrayList<Process>();
        List<Process> terminated = new ArrayList<Process>();

        // read memconfigFile
        int numFrames = memconfigFile.nextInt();
        Variable mainMemoryArray[] = new Variable[numFrames];
        memconfigFile.close();

        // read commandsFile
        List<Commands> commandsList = new ArrayList<Commands>();
        while (commandsFile.hasNextLine()) {
            String[] command = commandsFile.nextLine().split(" ");

            if (command[0].equals("Store")) {
                vmmFunction = command[0];
                id = command[1];
                value = Integer.parseInt(command[2]);

                Commands command1 = new Commands(vmmFunction, id, value);
                commandsList.add(command1);

            } else if (command[0].equals("Lookup")) {
                
                vmmFunction = command[0];
                id = command[1];
                
                Commands command2 = new Commands(vmmFunction, id);
                commandsList.add(command2);

            } else if (command[0].equals("Release")) {

                vmmFunction = command[0];
                id = command[1];

                Commands command3 = new Commands(vmmFunction, id);
                commandsList.add(command3);

            } else {
                System.out.println("Command not available.");
            }

        }
        

        // Read processesFile
        Scheduler schedule = new Scheduler(processes, ready, clock1);
        VMM virtualMemory = new VMM(mainMemoryArray);
        while (processesFile.hasNextLine()) {
            String[] array1 = processesFile.nextLine().split(" ");

            arrivalTime = Integer.parseInt(array1[0]);
            burstTime = Integer.parseInt(array1[1]);
            processNumber += processNumber;
            Process pro = new Process(arrivalTime, burstTime, processNumber, commandsList, virtualMemory, key);
            processes.add(pro);

        }

        processesFile.close();

        Collections.sort(processes, new Comparator<Process>() {

            @Override
            public int compare(Process p1, Process p2) {
                return p1.arrivalTime - p2.arrivalTime;
            }
        });

        for (Process pro: processes) {
            System.out.println(pro.getArrival());
        }
        // order?
        schedule.start();
        virtualMemory.start();
        schedule.join();
        virtualMemory.join();
    }
}