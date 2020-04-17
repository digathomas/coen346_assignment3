public class Clock {
    int time;

    Clock(int time1) { //clock constructor 
        time = time1;
    }

    public synchronized int getTime() { //synchronize time as it may get accessed by  multiple threads at the same time
        return time; //return time for the getter
    }

    public synchronized void increment() { //increment the clock by the time quantum of 1000ms
        time = time + 1000;

    }

}