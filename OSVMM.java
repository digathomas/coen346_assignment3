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

        // initialize variables
        int arrivalTime = 0;
        int burstTime = 0;
        String vmmFunction = "";
        String id = "";
        int value = 0;
        int startTime = 0;
        Clock clock = new Clock(startTime);
        AtomicInteger key = new AtomicInteger(0); // if process key is set to 0, then it does not have one

        // Create process lists
        List<Process> processList = new ArrayList<Process>();
        List<Process> readyList = new ArrayList<Process>();
        List<Process> terminatedList = new ArrayList<Process>();

        // Read memconfig.txt
        int numFrames = memconfigFile.nextInt();
        Variable mainMemoryArray[] = new Variable[numFrames];
        memconfigFile.close();

        // Read commands.txt 
        // Instantiate commandList
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

        // Read processes.txt
        Scheduler schedule = new Scheduler(processList, readyList, clock);
        VMM virtualMemory = new VMM(mainMemoryArray);
        while (processesFile.hasNextLine()) {
            String[] processArray = processesFile.nextLine().split(" ");
            arrivalTime = Integer.parseInt(processArray[0]);
            burstTime = Integer.parseInt(processArray[1]);
            Process process = new Process(arrivalTime, burstTime, commandsList, virtualMemory, key);
            processList.add(process);
        }
        processesFile.close();
        Collections.sort(processList, new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2) {
                return p1.arrivalTime - p2.arrivalTime;
            }
        });
        for (Process p : processList) {
            System.out.println(p.getArrival());
        }

        schedule.start();
        virtualMemory.start();
        schedule.join();
        virtualMemory.join();
    }
}