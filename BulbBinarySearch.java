import java.io.*;

//Write a recursive threading method to find the defective bulbs and the number of threads that have been created for this purpose

public class BulbBinarySearch {

    public static void main(String[] args) throws Exception {

        // Reading Input.txt file.
        // =====================================================
        // change the path to your corresponding Input.txt file
        // =====================================================
        File file = new File("/Users/nadiranusratrouf/Documents/COEN346_Assignment1/coen346_assignment1/Input.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String tempString = null;

        // Initialize array of integers.
        tempString = br.readLine();
        int arraySize = Integer.parseInt(tempString);
        int bulbArray[];
        bulbArray = new int[arraySize];
        for (int i = 0; i < arraySize; i++) {
  
            tempString = br.readLine();
            if(tempString.isEmpty()) {System.out.println("Space in text file."); br.close(); return;}
            bulbArray[i] = Integer.parseInt(tempString);
        }
        //if number of bulbs in array exceeds indicated array size
        if (br.readLine() !=null) {
            System.out.println("Too many bulbs for indicated array size.");
            br.close();
            return;

        }
        br.close();

        //check if there exists a value other than 0 or 1 in bulbArray
        for (int i=0;i<arraySize;i++) {
            if (bulbArray[i] != 0 && bulbArray[i] !=1) {
                System.out.println("Bulb value other than 0 or 1 exists in array.");
                return;
            }
        }

        // Print out bulbArray to terminal.
        for (int i = 0; i < arraySize; i++) {
            System.out.println("In bulbArray[" + i + "]: " + bulbArray[i]);
        }

        counter c1 = new counter();
        counter c2 = new counter();

        // Run FindDefective recursion method to find 0s.
        int positionArray[] = new int[arraySize];

        // positionArray =
        Thread mainT = new Thread(new Runnable() {
            public void run() {
                FindDefective(positionArray, bulbArray, 0, c1, c2);
                c1.increment();

            }
        });
        mainT.start();
        try {
            mainT.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.printf("==============================================\n");

        // Print out the positions of defective bulbs (0s).
        for (int i = 0; i < c2.getcount(); i++) {

            System.out.println("Position of defective lightbulb: " + (positionArray[i] + 1));
        }
        
        // Print out the number of threads used.
        System.out.println("Number of Threads used: " + c1.getcount());

        System.out.printf("==============================================\n");
    }

    // TA instructions:
    // Assume that the BulbArray is a 2^n, always divisible by 2.

    public static void FindDefective(int[] positionArray, int[] subBulbArray, int position, counter c1, counter c2) {

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