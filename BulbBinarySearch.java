import java.io.*;

public class BulbBinarySearch {

    public static void main(String[] args) throws Exception {

        // Reading Input.txt file.
        // =====================================================
        // change the path to your corresponding Input.txt file.
        // =====================================================
        File file = new File("C:\\Users\\etothra\\Workspace\\coen346_assignment1\\Input.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String tempString = null;

        // Initialize array of integers.
        // Initialize size of array.
        tempString = br.readLine();
        int arraySize = Integer.parseInt(tempString);
        int bulbArray[];
        bulbArray = new int[arraySize];
        // Handle a non 2^n array.
        int temp = arraySize;
        int remainder = 0;
        while (temp != 1) {
            remainder = temp % 2;
            if (remainder != 0) {
                System.out.println("Error: array not dividable by 2.");
                br.close();
                return;
            }
            temp /= 2;
        }
        // Set values of the array.
        for (int i = 0; i < arraySize; i++) {
            tempString = br.readLine();
            // Handle insufficient bulbs for array size.
            if (tempString == null) {
                System.out.println("Error: insufficient bulbs (to indicated array size).");
                br.close();
                return;
            }
            // Handle line break.
            if (tempString.isEmpty()) {
                System.out.println("Error: line break");
                br.close();
                return;
            }
            bulbArray[i] = Integer.parseInt(tempString);
        }
        // Handle excessive bulbs for array size.
        if (br.readLine() != null) {
            System.out.println("Error: excessive bulbs (to indicated array size).");
            br.close();
            return;
        }
        br.close();
        // End of .txt file read.

        // Handle undefined values (bulbs other than 0 or 1).
        for (int i = 0; i < arraySize; i++) {
            if (bulbArray[i] != 0 && bulbArray[i] != 1) {
                System.out.println("Error: undefined value (not 0 nor 1).");
                return;
            }
        }

        // Print out bulbArray to terminal.
        for (int i = 0; i < arraySize; i++) {
            System.out.println("In bulbArray[" + i + "]: " + bulbArray[i]);
        }

        // Instantiate counters c1 and c2
        Counter numThread = new Counter();
        Counter c2 = new Counter();
        // Instantiate positionArray. Keeps track of position of defective bulbs.
        int positionArray[] = new int[arraySize];

        // Start main thread.
        Thread mainT = new Thread(new Runnable() {
            public void run() {
                FindDefective(positionArray, bulbArray, 0, numThread, c2);
                numThread.increment();

            }
        });
        mainT.start();
        try {
            mainT.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Print out the positions of defective bulbs (0s).
        System.out.println("==============================================");
        System.out.println("OUTPUT");
        System.out.println("==============================================");
        for (int i = 0; i < c2.getcount(); i++) {
            // Position is off-set by 1 because of example answer.
            System.out.println("Position of defective lightbulb: " + (positionArray[i] + 1));
        }
        // Print out the number of threads used.
        System.out.println("Number of Threads used: " + numThread.getcount());
        System.out.println("==============================================");
    }

    public static void FindDefective(int[] positionArray, int[] subBulbArray, int position, Counter c1, Counter c2) {

        // Print the bulbArray being analyzed on terminal.
        for (int i = 0; i < subBulbArray.length; i++) {
            System.out.println(subBulbArray[i]);
        }

        // Find if there is a 0 in sub-array.
        int defectiveExists = 1;
        for (int i = 0; i < subBulbArray.length; i++) {
            defectiveExists *= subBulbArray[i];
        }

        // Case: no 0s in the sub array.
        if (defectiveExists == 1) {
            System.out.println("no defective bulb");
            return;// make sure
        }

        // Case: 0 found isolated.
        else if (subBulbArray.length == 1 && defectiveExists == 0) {
            System.out.println("defective bulb found at position: " + position);
            positionArray[c2.getcount()] = position;
            c2.increment(); // increment counter

        }
        // Case: split into sub-arrays.
        else {
            int pivot = subBulbArray.length / 2;
            System.out.println("splitting array with " + pivot + " as pivot");

            // Recursion on the left side.
            int[] leftArr = new int[pivot];
            for (int i = 0; i < leftArr.length; i++) {
                leftArr[i] = subBulbArray[i];
            }

            // creation of left thread
            Thread leftThread = new Thread(new Runnable() {
                public void run() {
                    c1.increment(); // increment counter
                    FindDefective(positionArray, leftArr, position, c1, c2);
                }
            });
            leftThread.start();
            System.out.println("start left side");

            // Recursion on the right side.
            int[] rightArr = new int[subBulbArray.length - leftArr.length];
            for (int i = 0; i < rightArr.length; i++) {
                rightArr[i] = subBulbArray[i + pivot];
            }

            // creation of right thread
            Thread rightThread = new Thread(new Runnable() {
                public void run() {
                    c1.increment();
                    FindDefective(positionArray, rightArr, position + pivot, c1, c2);
                }
            });
            rightThread.start();
            System.out.println("start right side");
            try {
                rightThread.join();
                System.out.println("end right side");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                leftThread.join();
                System.out.println("end left side");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}