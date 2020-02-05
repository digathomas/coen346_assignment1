public class Counter {
    int count = 0;

    public synchronized int getcount() {
        return count;
    }

    public synchronized void increment() {
        count++;
    }
}