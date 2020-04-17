import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;
import java.io.*;

public class Scheduler<myScheduler> implements Runnable {

	static List<Process> arrivalList = new ArrayList<Process>(); // Initialize list that stores all initial processes
	static List<Process> completeList = new ArrayList<Process>(); // Initialize list that stores all completed processes 

	// Keep track of CPU usage with semaphore 
	static Semaphore hasCpu = new Semaphore(2, true); //(with two cores)(will guarantee first-in first-out)
	static MemoryManager VMM = new MemoryManager(hasCpu); // Initialize Scheduler's MemoryManager
	static String outputLog = ""; // Store output string

	public static void main(String args[]) {
		try {
			Scanner reader = new Scanner(new File("processes.txt"));
			// Go through each line of processes.txt
			while (reader.hasNextLine()) {
				// Read line
				String line = reader.nextLine();

				// Handle too many integers in a line
				String[] splitString = line.split(" ");
				if (splitString.length > 2) {
					System.out.print("too many integers in a line\n");
				}

				// Create Process from processes.txt
				int[] tempProcess = new int[2];
				tempProcess[0] = Integer.parseInt(splitString[0]);
				tempProcess[1] = Integer.parseInt(splitString[1]);
				Process newProcess = new Process(tempProcess[0], tempProcess[1], hasCpu);

				// Add the created process onto the arrivalList
				arrivalList.add(newProcess);
			}
			reader.close();

			// Instantiate Runnable object of the Scheduler class and start it
			Scheduler myScheduler = new Scheduler();
			myScheduler.run();

			// Write outputLog string onto the ouput.txt file
			try (PrintWriter writer = new PrintWriter("output.txt")) {
				writer.print(outputLog);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// Append new string onto the outputLog string
	public static void appendOutputLog(String stringtoAdd) {
		outputLog += stringtoAdd;
	}

	public void run() {
		// Loop until all processes have finished executing
		while (arrivalList.size() > 0) {
			try {
				hasCpu.acquireUninterruptibly(); // Acquire cpu

				Process[] nextArr = { arrivalList.get(0), arrivalList.get(arrivalList.size() - 1) };

				for (int i = arrivalList.size() - 1; i > -1; i--) { // Loop to check for newly arrived method
					Process tempProcess = arrivalList.get(i);
					if (Process.getCurrentTime() >= tempProcess.getArrivalTime() && !tempProcess.isStarted()) {
						nextArr[1] = nextArr[0];
						nextArr[0] = tempProcess;
					}
				}

				// check if a process is being started before it arrives
				if (Process.getCurrentTime() < nextArr[0].getArrivalTime()
						|| Process.getCurrentTime() < nextArr[1].getArrivalTime()) {
					if (Process.getCurrentTime() < nextArr[0].getArrivalTime()) {
						nextArr[0] = null;
					}
					if (Process.getCurrentTime() < nextArr[1].getArrivalTime()) {
						nextArr[1] = null;
					}
					if (nextArr[1] == null && nextArr[0] == null) {
						Process.setCurrentTime(Process.getCurrentTime() + 1000);
						// since we know this process is the priority, but arrives after the current
						// time, we simply increment the time.
					}
				}

				if (nextArr[0] != null) {
					nextArr[0].run();
					if (nextArr[0].isFinished()) { // Removes a process and adds it to the completed list of processes
						completeList.add(nextArr[0]);
						arrivalList.remove(nextArr[0]);
					}
				}
				if (nextArr[1] != null && nextArr[0] != nextArr[1]) {
					nextArr[1].run();
					if (nextArr[1].isFinished()) { // Removes a process and adds it to the completed list of processes
						completeList.add(nextArr[1]);
						arrivalList.remove(nextArr[1]);
					}

				}

			} catch (Exception e) {
				System.out.println(e.getMessage());
			} finally {
				hasCpu.release(); 
			}
		}
	}

}
