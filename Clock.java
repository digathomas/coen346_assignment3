public class Clock {
    int time;

    Clock(int time1) {
        time = time1;
    }

    public synchronized int getTime() {
        return time;
    }

    public synchronized void increment() {
        time += time;

    }

}