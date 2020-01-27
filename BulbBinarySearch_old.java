import java.io.*;

//Write a recursive threading method to find the defective bulbs and the number of threads that have been created for this purpose

public class BulbBinarySearch_old extends Thread {

    public static void main(String[] args) throws Exception {

        // Reading Input.txt file.
        // =====================================================
        // change the path to your corresponding Input.txt file
        // =====================================================
        File file = new File("C:\\Users\\etothra\\Workspace\\coen346_assignment1\\Input.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String tempString = null;

        // Initialize array of integers.
        tempString = br.readLine();
        int arraySize = Integer.parseInt(tempString);
        int bulbArray[];
        bulbArray = new int[arraySize];
        for (int i = 0; i < arraySize; i++) {
            tempString = br.readLine();
            bulbArray[i] = Integer.parseInt(tempString);
        }
        br.close();

        // Print out bulbArray to terminal.
        for (int i = 0; i < arraySize; i++) {
            System.out.println("In bulbArray[" + i + "]: " + bulbArray[i]);
        }

        // Run FindDefective recursion method to find 0s.
        int positionArray[];
        positionArray = FindDefective(bulbArray, 0);

        System.out.printf("==============================================\n");

        // Print out the positions of defective bulbs (0s).
        for (int i = 0; i < positionArray.length; i++) {
            System.out.println("Position of defective lightbulb: " + (positionArray[i] + 1));
        }

        // Print out the number of threads used.
        System.out.println("Number of Threads used: 0");

        System.out.printf("==============================================\n");
    }

    // TA instructions:
    // Assume that the BulbArray is a 2^n, always divisible by 2.

    public static int[] FindDefective(int[] subBulbArray, int position) {

        // Print the bulbArray being analyzed on terminal.
        for (int i = 0; i < subBulbArray.length; i++) {
            System.out.println(subBulbArray[i]);
        }

        // Find if there is a 0 in sub-array.
        int defectiveExists = 1;
        for (int i = 0; i < subBulbArray.length; i++) {
            defectiveExists *= subBulbArray[i];
            if (defectiveExists == 0) {
                break;
            }
        }

        // Case: no 0s in the sub array.
        if (defectiveExists == 1) {
            System.out.println("no defective bulb");
            return null;
        }
        // Case: 0 found isolated.
        else if (subBulbArray.length == 1 && defectiveExists == 0) {
            System.out.println("defective bulb found at position: " + position);
            int[] positionArray = new int[1];
            positionArray[0] = position;
            return positionArray;
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
            System.out.println("start of left side");
            int[] positionArrayLeft = FindDefective(leftArr, position);

            // Recursion on the right side.
            int[] rightArr = new int[pivot];
            for (int i = 0; i < rightArr.length; i++) {
                rightArr[i] = subBulbArray[i + pivot];
            }
            System.out.println("start of right side");
            int[] positionArrayRight = FindDefective(rightArr, position + pivot);

            // Return array side that had 0s.
            if (positionArrayLeft == null) {
                return positionArrayRight;
            } else if (positionArrayRight == null) {
                return positionArrayLeft;
            }
            // Concatenate both positionArrays in the new positionArray.
            else {
                int[] positionArray = new int[positionArrayLeft.length + positionArrayRight.length];
                System.arraycopy(positionArrayLeft, 0, positionArray, 0, positionArrayLeft.length);
                System.arraycopy(positionArrayRight, 0, positionArray, positionArrayLeft.length,
                        positionArrayRight.length);
                return positionArray;
            }
        }
    }
}