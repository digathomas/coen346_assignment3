import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Scheduler extends Thread {

    FileWriter writer = new FileWriter("output.txt", false);
    BufferedWriter bufferedWriter = new BufferedWriter(writer);
    List<Process> processList = new ArrayList<Process>();
    List<Process> readyList = new ArrayList<Process>();
    List<Process> runningList = new ArrayList<Process>();
    List<Process> terminatedList = new ArrayList<Process>();
    // Semaphore CpuAccess = new Semaphore(2);
    AtomicInteger cpuAccess = new AtomicInteger(2);
    Clock clock;

    Scheduler(List<Process> processList, List<Process> readyList, Clock clock) {
        this.processList = processList;
        this.readyList = readyList;
        this.clock = clock;
    }

    public static void move(List<Process> departure, List<Process> destination) {
        destination.add(departure.get(0));
        departure.remove(0);
    }

    public void run() {
        // TODO: Create busy wait loops, assign semaphore to processes
        while (!(processList.isEmpty() && readyList.isEmpty())) { 
            if (!processList.isEmpty()) { //ISSUE: what if a process in processes is there but has a starting time really far, that's going to block the cpu for all other ready...
                if (processList.get(0).getArrival() == clock.getTime()) {
                    move(processList, readyList);
                    if (cpuAccess.get() <= 2 && cpuAccess.get() > 0) {
                        cpuAccess.getAndDecrement();
                        readyList.get(0).start();
                    }
                }
                // look for second process in processList
                if (processList.get(0).getArrival() == clock.getTime()) {
                    move(processList, readyList);
                    if (cpuAccess.get() <= 2 && cpuAccess.get() > 0) {
                        cpuAccess.getAndDecrement();
                        move(readyList, runningList)
                        runningList.get(0).start();
                    } else if (!readyList.isEmpty()) { // check readyList for the second process if not in processList
                        if (cpuAccess.get() >= 2) {
                            cpuAccess.getAndDecrement();
                            move(readyList, runningList);
                            runningList.get(0).start();
                        } else {
                            while (cpuAccess.get() == 0)
                                ;
                        }
                    }
                }
                if (!readyList.isEmpty()) {
                    while (cpuAccess.get() >= 2) {
                        cpuAccess.getAndDecrement();
                        readyList.get(0).start();
                    }
                }
            }

            bufferedWriter.write("Clock: " + clock + ", " + "Process: " + );
            bufferedWriter.newLine();
        }
        bufferedWriter.close();
    }
}