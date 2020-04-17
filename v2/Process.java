import java.io.File;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.Random;

//This class is the process class that will simulate processes running on a single thread

public class Process implements Runnable {
	Semaphore hasCpu = null; // Used to ensure exclusion/simulate single thread
	private String name;
	private int arrivalTime; // start time
	private int processTime; // duration time
	private int quantum;
	private int elapsedTime;
	private int remainingTime;
	private boolean isStarted;
	private boolean isFinished;
	static int currentTime = 0;
	static int refName = 0;
	public static Scanner nextCommand = null;
	public static MemoryManager VMM = Scheduler.VMM;

	// Constructor
	Process(int arrivalTime, int processTime, Semaphore hasCpu) {
		this.arrivalTime = arrivalTime * 1000;
		this.processTime = processTime * 1000;
		this.remainingTime = processTime * 1000;
		this.isStarted = false;
		quantum = 1000;
		this.hasCpu = hasCpu;
		refName += 1;
		name = "Process " + refName;
		if (nextCommand == null) {
			try {
				Process.nextCommand = new Scanner(new File("commands.txt"));
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public String getNextCommand() { // Returns the next line from the command.txt or "EOF"
		if (nextCommand.hasNextLine()) {
			return nextCommand.nextLine();
		}
		return "EOF";
	}

	public void run() {
		try {
			hasCpu.acquireUninterruptibly();
			String thisCommand = "";
			int commandTime = 0;
			if (!isStarted) {
				Scheduler.appendOutputLog("Clock: " + currentTime + ", " + name + ", " + "Started" + "\n");
				setStarted(true);
			}
			Scheduler.appendOutputLog("Clock: " + currentTime + ", " + name + ", " + "Resumed" + "\n");
			elapsedTime += quantum;
			remainingTime -= quantum;
			while ((commandTime += 10 + new Random().nextInt(49) * 10) < this.quantum) {
				thisCommand = getNextCommand();
				if (thisCommand != "EOF") {
					MemoryManager.setNextCommandAndTime(name, thisCommand, currentTime + commandTime);
					this.hasCpu.release();
					VMM.run();
					hasCpu.acquireUninterruptibly();
				}
			}
			if (elapsedTime >= processTime) {
				currentTime += processTime - (elapsedTime - quantum);
				remainingTime = 0;
				elapsedTime = processTime;
				setFinished(true);
			} else {
				currentTime += quantum;
			}
			Scheduler.appendOutputLog("Clock: " + currentTime + ", " + name + ", " + "Paused" + "\n");
			if (isFinished) {
				Scheduler.appendOutputLog("Clock: " + currentTime + ", " + name + ", " + "Finished" + "\n");
			}
		} catch (Exception e) {
			System.out.println("Error while process acquiring semaphore.");
			e.printStackTrace();
		} finally {
			hasCpu.release();
		}

	}

	public void setArrivalTime(int arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public void setProcessTime(int processTime) {
		this.processTime = processTime;
	}

	public void setStarted(boolean isStarted) {
		this.isStarted = isStarted;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	public void setElapsedTime(int elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public static void setCurrentTime(int i) {
		currentTime = i;
	}

	public void setRemainingTime(int remainingTime) {
		this.remainingTime = remainingTime;
	}

	public int getArrivalTime() {
		return arrivalTime;
	}

	public int getProcessTime() {
		return processTime;
	}

	public boolean isStarted() {
		return isStarted;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public int getElapsedTime() {
		return elapsedTime;
	}

	public static int getCurrentTime() {
		return currentTime;
	}

	public int getRemainingTime() {
		return remainingTime;
	}

	public String getName() {
		return name;
	}
}
