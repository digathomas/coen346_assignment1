//import java.io.*;

class Hello implements Runnable {
    public static void main(String[] args) {
        // auto generated method stub
        Thread t = new Thread(new Hello());
        t.start();
    }

    public void run() {
        System.out.println("Hello Concurent World");
    }
}