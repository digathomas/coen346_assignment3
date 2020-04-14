public class Clock {
    int time;

    Clock(int time) {
        this.time = time;
    }

    public synchronized int getTime() {
        return time;
    }

    public synchronized void increment() {
        time += 1;
    }
}