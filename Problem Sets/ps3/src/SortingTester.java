import java.security.Key;
import java.util.Random;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SortingTester {

    public static boolean checkSort(ISort sorter, int size) {
        Random randInt = new Random();
        int constInt = randInt.nextInt();
        KeyValuePair[] anIncreasingArray = new KeyValuePair[size];
        KeyValuePair[] aDecreasingArray = new KeyValuePair[size];
        KeyValuePair[] aZeroArray = new KeyValuePair[size];
        KeyValuePair[] aConstArray = new KeyValuePair[size];
        KeyValuePair[] aRandomArray = new KeyValuePair[size];

        for(int i = 0; i < size; i++) {
            anIncreasingArray[i] = new KeyValuePair(i, 0);
            aDecreasingArray[i] = new KeyValuePair((size - 1) - i, 0);
            aZeroArray[i] = new KeyValuePair(0, 0);
            aConstArray[i] = new KeyValuePair(constInt, 0);
            int newRandInt = randInt.nextInt();
            aRandomArray[i] = new KeyValuePair(newRandInt, 0);;
        }

        sorter.sort(anIncreasingArray);
        sorter.sort(aDecreasingArray);
        sorter.sort(aZeroArray);
        sorter.sort(aConstArray);
        sorter.sort(aRandomArray);

        for(int i = 0; i < size - 1; i++) {
            boolean isInvalid = anIncreasingArray[i].getKey() > anIncreasingArray[i+1].getKey() ||
                    aDecreasingArray[i].getKey() > aDecreasingArray[i+1].getKey() ||
                    aZeroArray[i].getKey() > aZeroArray[i+1].getKey() ||
                    aConstArray[i].getKey() > aConstArray[i+1].getKey() ||
                    aRandomArray[i].getKey() > aRandomArray[i+1].getKey();
            if(isInvalid) {
                return false;
            }
        }
        return true;
    }

    public static boolean isStable(ISort sorter, int size) {
        Random randInt = new Random();
        KeyValuePair[] aZeroArray = new KeyValuePair[size];
        KeyValuePair[] aRandomArray = new KeyValuePair[size];
        for(int i = 0; i < size; i++) {
            aZeroArray[i] = new KeyValuePair(0, i);

            int max = (size + 1)/2;
            int min = 1;
            int newRandInt = randInt.nextInt(max - min) + min;
            aRandomArray[i] = new KeyValuePair(newRandInt, i);
        }
        sorter.sort(aZeroArray);
        sorter.sort(aRandomArray);

        for(int i = 0; i < size - 1; i++) {
            if(aZeroArray[i].getKey() == aZeroArray[i+1].getKey()) {
                if (aZeroArray[i].getValue() > aZeroArray[i + 1].getValue()) {
                    return false;
                }
            }

            if(aRandomArray[i].getKey() == aRandomArray[i+1].getKey()) {
                if (aRandomArray[i].getValue() > aRandomArray[i + 1].getValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    public static double calculateTime(ISort sorter, KeyValuePair[] array) {
        StopWatch watch = new StopWatch();
        watch.start();
        sorter.sort(array);
        watch.stop();
        return watch.getTime();
    }

    public static KeyValuePair[] createRandomArray(int size) {
        Random randInt = new Random();
        KeyValuePair[] aRandomArray = new KeyValuePair[size];
        for(int i = 0; i < size; i++) {
            int newRandInt = randInt.nextInt();
            aRandomArray[i] = new KeyValuePair(newRandInt, 0);
        }
        return aRandomArray;
    }

    public static void createCSV(ISort[] sorters, String fileName, String workingDir, int startSize, int endSize, int step) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        PrintWriter pw = null;

        try {
            //clearing file begin
            fw = new FileWriter(workingDir + "/" + fileName, false);
            bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);
            pw.print("");
            //clearing file end

            fw = new FileWriter(workingDir + "/" + fileName, true);
            bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);

            for(int i = 0; i < sorters.length; i++) {
                pw.print("Sorter" + i + ",,");
            }
            pw.println("");
            for(int i = 0; i < sorters.length; i++) {
                pw.print("ArrayLength,Runtime,");
            }
            pw.println("");
            for(int i = startSize; i < endSize; i+=step) {
                KeyValuePair[] aRandomArray = createRandomArray(i);
                for(int j = 0; j < sorters.length; j++) {
                    double runtime = calculateTime(sorters[j], aRandomArray);
                    pw.print(i + "," + runtime + ",");
                }
                pw.println("");
                System.out.println("Sorting Array Size: " + i);
            }
            System.out.println("Data Successfully appended into file");
            pw.flush();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int size = 20;

        ISort sortingObjectA = new SorterA();
        System.out.println("Is Sorted (A): " + checkSort(sortingObjectA, size));
        System.out.println("Is Stable (A): " + isStable(sortingObjectA, size));
        ISort sortingObjectB = new SorterB();
        System.out.println("Is Sorted (B): " + checkSort(sortingObjectB, size));
        System.out.println("Is Stable (B): " + isStable(sortingObjectB, size));
        ISort sortingObjectC = new SorterC();
        System.out.println("Is Sorted (C): " + checkSort(sortingObjectC, size));
        System.out.println("Is Stable (C): " + isStable(sortingObjectC, size));
        ISort sortingObjectD = new SorterD();
        System.out.println("Is Sorted (D): " + checkSort(sortingObjectD, size));
        System.out.println("Is Stable (D): " + isStable(sortingObjectD, size));
        ISort sortingObjectE = new SorterE();
        System.out.println("Is Sorted (E): " + checkSort(sortingObjectE, size));
        System.out.println("Is Stable (E): " + isStable(sortingObjectE, size));
        ISort sortingObjectF = new SorterF();
        System.out.println("Is Sorted (F): " + checkSort(sortingObjectF, size));
        System.out.println("Is Stable (F): " + isStable(sortingObjectF, size));

        String currentWorkingDirectory = "/Users/shenyicui/Library/CloudStorage/OneDrive-Personal" +
                "/Documents/Education/NUS/Year 1/Semester 2/CS2040S/Problem Sets/ps3";
        ISort[] sorters = {sortingObjectC, sortingObjectF};
        createCSV(sorters, "SorterC vs SorterF.txt", currentWorkingDirectory,1, 2500, 2);
    }
}
