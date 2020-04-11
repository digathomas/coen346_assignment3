import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
//import java.util.concurrent.atomic.AtomicInteger;

public class Scheduler extends Thread {

    Clock clock;
    Semaphore CPUAccess = new Semaphore(2);
    List<Process> processes = new ArrayList<Process>();
    List<Process> ready = new ArrayList<Process>();
    List<Process> terminated = new ArrayList<Process>();

    Scheduler(List<Process> processList, List<Process> readyList, Clock clock1) {
        processes = processList;
        ready = readyList;
        clock = clock1;

    }

    public static void move(List<Process> departure, List<Process> destination) {
        destination.add(departure.get(0));
        departure.remove(0);
    }

    public void run() {

        // two semaphores for CPU Access because two cores. If tryAcquire() returns
        // true, then access CPU, else busy wait
        // release() semaphore once CPU time is over and busy wait
        // if burstTime = 0, send process to terminatedList

        // TODO: Create busy wait loops, assign semaphore to processes

        while (processes.size() > terminated.size()) {

            // if both lists aren't empty
            if (!processes.isEmpty()) {

                // take both processes off the list and add them to readyList
                // start both of them
                if (CPUAccess.availablePermits() <= 2) {
                    if (processes.get(0).getArrival() == clock.getTime())
                        move(processes, ready);
                    ready.add(processes.get(0));
                    processes.remove(0);
                    try {
                        CPUAccess.acquire(1);
                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }
                    ready.get(0).start();

                }
                if (processes.get(0).getArrival() == clock.getTime()) {
                    ready.add(processes.get(0));
                    try {
                        CPUAccess.acquire(1);
                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }
                    ready.get(0).start();

                } else if (ready.get(0).getArrival() == clock.getTime()) {
                    try {
                        CPUAccess.acquire(1);
                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }
                    ready.get(0).start();

                }
            } else if (!ready.isEmpty()) {
                // give this process the key and run?
                if (CPUAccess.availablePermits() <= 2) {
                    if (ready.get(0).getBurstTime() == clock.getTime()) {
                        try {
                            CPUAccess.acquire(1);
                        } catch (InterruptedException e) {

                            e.printStackTrace();
                        }
                        ready.get(0).start();
                    }
                    if (ready.get(0).getBurstTime() == clock.getTime()) {
                        try {
                            CPUAccess.acquire(1);
                        } catch (InterruptedException e) {

                            e.printStackTrace();
                        }
                        ready.get(0).start();
                    }
                }
            } else {
                // the CPU is idle and the clock is incremented
                clock.increment();
            }
        }
        // check if there are processes to resume
        if (processes.isEmpty() && !ready.isEmpty()) {
            if (ready.get(0).getBurstTime() == clock.getTime()) {
                if (CPUAccess.availablePermits() <= 2) {
                    try {
                        CPUAccess.acquire(1);
                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }
                    ready.get(0).start();
                } else {
                    while (CPUAccess.tryAcquire() == false)
                        ; // busy wait if no semaphores available
                }
            }

        }
        if (ready.get(0).isFinished()) {
            move(ready, terminated);
        }
    }
}
