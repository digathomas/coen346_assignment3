import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class OSVMM extends Thread {

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {

        Scanner processesFile = new Scanner(new File("processes.txt"));
        Scanner memconfigFile = new Scanner(new File("memconfig.txt"));
         Scanner commandsFile = new Scanner(new File("commands.txt"))
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
            if (commandsFile.next().equals("Store")) {

                vmmFunction = commandsFile.next();
                id = commandsFile.next();
                String a = commandsFile.next();
                value = Integer.parseInt(a);
                Commands command = new Commands(vmmFunction, id, value);
                commandsList.add(command);
            } else if (commandsFile.next().equals("Lookup")) {
                vmmFunction = commandsFile.next();
                id = commandsFile.next();
                Commands command = new Commands(vmmFunction, id);
                commandsList.add(command);

            } else if (commandsFile.next().equals("Release")) {

                vmmFunction = commandsFile.next();
                id = commandsFile.next();
                Commands command = new Commands(vmmFunction, id);
                commandsList.add(command);

            } else {
                System.out.println("Command not available.");
            }

        }

        // Read processesFile
        Scheduler schedule = new Scheduler(processes, ready, clock1);
        VMM virtualMemory = new VMM(mainMemoryArray);
        while (processesFile.hasNextLine()) {
            arrivalTime = processesFile.nextInt();
            burstTime = processesFile.nextInt();
            processNumber += processNumber;
            Process pro = new Process(arrivalTime, burstTime, processNumber, commandsList, virtualMemory);
            processes.add(pro);

        }

        processesFile.close();

        Collections.sort(processes, new Comparator<Process>() {

            @Override
            public int compare(Process p1, Process p2) {
                return p1.arrivalTime - p2.arrivalTime;
            }
        });

        // order?
        schedule.start();
        virtualMemory.start();
        schedule.join();
        virtualMemory.join();
    }
}