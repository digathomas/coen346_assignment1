import java.io.*;

public class BulbBinarySearch {

    public static void main(String[] args) throws Exception {

        // Reading Input.txt file.
        // =====================================================
        // change the path to your corresponding Input.txt file.
        // =====================================================
        File file = new File("D:\\Workspace\\coen346_assignment1\\Input.txt");
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

        // Instantiate counters
        // Number of threads
        Counter numThread = new Counter();
        // BulbArray size (static array)
        Counter sizePositionArray = new Counter();
        // Instantiate positionArray. Keeps track of position of defective bulbs.
        int positionArray[] = new int[arraySize];

        // Start main thread.
        Thread mainT = new Thread(new Runnable() {
            public void run() {
                FindDefective(positionArray, bulbArray, 0, numThread, sizePositionArray);
                numThread.increment();

            }
        });
        System.out.println("Start main thread");
        mainT.start();
        try {
            mainT.join();
            System.out.println("End main thread");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Print out the positions of defective bulbs (0s).
        System.out.println("==============================================");
        System.out.println("UNORDERED OUTPUT");
        System.out.println("==============================================");
        for (int i = 0; i < sizePositionArray.getcount(); i++) {
            // Position is off-set by 1 because of example answer.
            System.out.println("Position of defective lightbulb: " + (positionArray[i] + 1));
        }
        // Print out the number of threads used.
        System.out.println("Number of Threads used: " + numThread.getcount());
        System.out.println("==============================================");

        // Bubble-sort the position array
        for (int i = 0; i < sizePositionArray.getcount() - 1; i++) {
            for (int j = 0; j < sizePositionArray.getcount() - i - 1; j++) {
                if (positionArray[j] > positionArray[j + 1]) {
                    // Swap positionArray[j+1] and positionArray[i]
                    int foo = positionArray[j];
                    positionArray[j] = positionArray[j + 1];
                    positionArray[j + 1] = foo;
                }
            }
        }

        // Print out the positions of defective bulbs (0s) ascending order.
        System.out.println("==============================================");
        System.out.println("ORDERED OUPUT");
        System.out.println("==============================================");
        for (int i = 0; i < sizePositionArray.getcount(); i++) {
            // Position is off-set by 1 because of example answer.
            System.out.println("Position of defective lightbulb: " + (positionArray[i] + 1));
        }
        // Print out the number of threads used.
        System.out.println("Number of Threads used: " + numThread.getcount());
        System.out.println("==============================================");
    }

    public static void FindDefective(int[] positionArray, int[] subBulbArray, int position, Counter numThread,
            Counter sizePositionArray) {

        // Print the bulbArray being analyzed on terminal.
        for (int i = 0; i < subBulbArray.length; i++) {
            System.out.println(subBulbArray[i]);
        }
        // Find if there is a 0 in sub-array.
        int defectiveExists = 1;
        for (int i = 0; i < subBulbArray.length; i++) {
            defectiveExists *= subBulbArray[i];
            // if (subBulbArray[i] == 0) {System.out.println("no defective bulb"); return;}
        }

        // Case: no 0s in the sub array.
        if (defectiveExists == 1) {
            System.out.println("no defective bulb");
            return;// make sure
        }
        // Case: 0 found isolated.
        else if (subBulbArray.length == 1 && defectiveExists == 0) {
            System.out.println("defective bulb found at position: " + position);
            // Set position of defective bulb in positionArray
            positionArray[sizePositionArray.getcount()] = position;
            // Increment number of threads.
            sizePositionArray.increment();
        }
        // Case: split into sub-arrays.
        else {
            int pivot = subBulbArray.length / 2;
            System.out.println("splitting array with " + pivot + " as pivot");

            // Recursion on the left side.
            // Instantiate leftArr (copy of left side of mother array).
            int[] leftArr = new int[pivot];
            for (int i = 0; i < leftArr.length; i++) {
                leftArr[i] = subBulbArray[i];
            }
            // Creation of left thread.
            Thread leftThread = new Thread(new Runnable() {
                public void run() {
                    numThread.increment();
                    FindDefective(positionArray, leftArr, position, numThread, sizePositionArray);
                }
            });
            // Start of left thread.
            leftThread.start();
            System.out.println("Start left side thread");

            // Recursion on the right side.
            // Instatntiate rightArr (copy of right side of mother array).
            int[] rightArr = new int[pivot];
            for (int i = 0; i < rightArr.length; i++) {
                rightArr[i] = subBulbArray[i + pivot];
            }

            // Creation of right thread.
            Thread rightThread = new Thread(new Runnable() {
                public void run() {
                    numThread.increment();
                    FindDefective(positionArray, rightArr, position + pivot, numThread, sizePositionArray);
                }
            });
            // Start of right thread.
            rightThread.start();
            System.out.println("Start right side thread");
            // Join threads together
            try {
                rightThread.join();
                System.out.println("End right side thread");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                leftThread.join();
                System.out.println("End left side thread");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}