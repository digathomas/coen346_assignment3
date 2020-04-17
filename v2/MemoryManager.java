import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class MemoryManager implements Runnable {

	private String memconfig;
	private int size; 
	private String vm; 
	static String nextCommand = "";
	static String nextProcessName = "";
	static List<Variable> memory = new ArrayList<Variable>();
	static int currentTime; 
	Semaphore hasCPU = null;

	// Constructor
	MemoryManager(Semaphore hasCPU) {
		this.hasCPU = hasCPU;
		this.memconfig = "memconfig.txt";
		this.vm = "vm.txt";
		try {
			Scanner scMemconfig = new Scanner(new File(this.memconfig));
			this.size = Integer.parseInt(scMemconfig.nextLine());
			scMemconfig.close();
			if (this.size == 0) {
				System.out.println("Memory size is zero.");
			}
		} catch (Exception e) {
			System.out.println("Could not open memconfig.");
			e.printStackTrace();
		}
		try (PrintWriter pw = new PrintWriter(new File(vm))) {
			pw.close();
		} catch (Exception e) {
			System.out.println("Could not open vm.");
			e.printStackTrace();
		}
	}

	public synchronized void parseCommand() {
		String[] myArr = nextCommand.split(" "); // identifying command elements
		myArr[0] = myArr[0].toLowerCase();
		if ((Arrays.asList(new String[] { "store", "memstore" })).contains(myArr[0])) {
			Scheduler.appendOutputLog("Clock: " + currentTime + ", " + nextProcessName + ", ");
			Scheduler.appendOutputLog("Store: Variable " + myArr[1] + ", Value: " + myArr[2] + "\n");
			memStore(myArr[1], Integer.parseInt(myArr[2]));
		} else if ((Arrays.asList(new String[] { "memfree", "release", "free" })).contains(myArr[0])) {
			Scheduler.appendOutputLog("Clock: " + currentTime + ", " + nextProcessName + ", ");
			Scheduler.appendOutputLog("Release: Variable " + myArr[1] + "\n");
			memFree(myArr[1]);
		} else if ((Arrays.asList(new String[] { "look", "memlookup", "memlook", "lookup" })).contains(myArr[0])) {
			int value = memLookup(myArr[1]);
			Scheduler.appendOutputLog("Clock: " + currentTime + ", " + nextProcessName + ", ");
			Scheduler.appendOutputLog("Lookup: Variable " + myArr[1] + ", Value " + value + "\n");
		} else {

		}
	}

	public void memStore(String varID, int value) {
		Variable var = new Variable(varID, value);
		var.setLastAccess(currentTime);
		if (memory.size() >= this.size) {
			writeToDisk(var);
		} else { 
			memory.add(var);
		}
	}

	public void memFree(String varID) {
		boolean freed = false;
		for (Variable var : memory) { 
			if (var.getId().equals(varID)) {
				memory.remove(var);
				freed = true;
				break;
			}
		}
		if (!freed) {
			try {
				removeFromDisk(varID);
			} catch (Exception e) {
				System.out.println("Error releasing from secondary space");
			}
		}
	}

	public int memLookup(String varID) {
		for (Variable var : memory) { 
			if (var.getId().equals(varID)) {
				var.setLastAccess(currentTime);
				return var.getValue();
			}
		}
		if (checkID(varID)) { 
			try {
				Scanner sc = new Scanner(new File(vm));
				while (sc.hasNextLine()) {
					String line = sc.nextLine();
					String[] lineArr = line.split(" ");
					if (lineArr[0].equals(varID)) { 
						swap(new Variable(varID, Integer.parseInt(lineArr[1])));
						return Integer.parseInt(lineArr[1]);
					}
				}
				sc.close();
			} catch (Exception e) {
				System.out.println("Error reading variable on disk.");
				e.printStackTrace();
			}
		}
		return -1;
	}

	public void swap(Variable swapIn) { 
		Variable swapOut = memory.get(0);
		for (Variable var : memory) {
			if ((var.getLastAccess() < swapOut.getLastAccess())) {
				swapOut = var;
			}
		}
		try {
			writeToDisk(swapOut);
			Scheduler.appendOutputLog("Clock: " + (currentTime - 10) + ", " + nextProcessName + ", ");
			Scheduler.appendOutputLog("SWAP: Variable " + swapIn.getId() + " with Variable " + swapOut.getId() + "\n");
			memory.remove(swapOut);
			memory.add(swapIn);
			removeFromDisk(swapIn.getId());

		} catch (Exception e) {
			System.out.println("Error attempting swap.");
			e.printStackTrace();
		}

	}

	public boolean checkID(String varID) {
		boolean onDisk = false;
		try {
			Scanner sc = new Scanner(new File(vm));
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] lineArr = line.split(" ");
				if (lineArr[0].equals(varID)) {
					onDisk = true;
				}
			}
			sc.close();
		} catch (Exception e) {
			System.out.println("Error checking variable on disk.");
			e.printStackTrace();
		}

		return onDisk;
	}

	private void removeFromDisk(String varId) {
		try {
			Scanner sc = new Scanner(new File(vm));
			String writeToDiskString = "";
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] lineArr = line.split(" ");
				if (lineArr[0].equals(varId)) {
				} 
				else 
				{
					writeToDiskString += line + "\n";
				}
			}
			sc.close();
			PrintWriter pw = new PrintWriter(new File(vm));
			pw.print(writeToDiskString);
			pw.close();
		} catch (Exception e) {
			System.out.println("Error removing variable from disk");
		}
	}

	private void writeToDisk(Variable var) {
		try (FileWriter fw = new FileWriter(this.vm, true)) {
			PrintWriter pw = new PrintWriter(fw);
			pw.println(var.getId() + " " + var.getValue());
			pw.close();
		} catch (Exception e) {
			System.out.println("Error Writing to disk.");
			e.printStackTrace();
		}
	}

	public void setCurrentTime(int time) {
		MemoryManager.currentTime = time;
	}

	@Override
	public void run() { 
		try {
			hasCPU.acquireUninterruptibly();
			this.parseCommand();
			hasCPU.release();
		} catch (Exception e) {
			System.out.println("Error while vmm acquiring semaphore");
		}
	}

	public String getNextCommand() {
		return nextCommand;
	}

	public void setNextCommand(String nextCommand) {
		MemoryManager.nextCommand = nextCommand;
	}

	public static void setNextCommandAndTime(String name, String nextCommand, int time) {
		MemoryManager.nextProcessName = name;
		MemoryManager.nextCommand = nextCommand;
		MemoryManager.currentTime = time;
	}
}
