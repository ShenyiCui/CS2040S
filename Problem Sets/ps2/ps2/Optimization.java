/**
 * The Optimization class contains a static routine to find the maximum in an array that changes direction at most once.
 */
public class Optimization {

    /**
     * A set of test cases.
     */
    static int[][] testCases = {
            {1, 3, 5, 7, 9, 11, 10, 8, 6, 4},
            {67, 65, 43, 42, 23, 17, 9, 100},
            {4, -100, -80, 15, 20, 25, 30},
            {2, 3, 4, 5, 6, 7, 8, 100, 99, 98, 97, 96, 95, 94, 93, 92, 91, 90, 89, 88, 87, 86, 85, 84, 83}
    };

    /**
     * Returns the maximum item in the specified array of integers which changes direction at most once.
     *
     * @param dataArray an array of integers which changes direction at most once.
     * @return the maximum item in data Array
     */
    public static int searchMax(int[] dataArray) {
        int high = dataArray.length-1;
        if(high < 0)
            return -1;
        return recursiveMaxSearch(dataArray, 0, high);
    }

    public static int recursiveMaxSearch(int[] arr, int low, int high){
        //base case for when the array is increasing then decreasing
        if(low == high)
            return arr[low];
        //catching mid - 1 <  0
        if(low + 1 == high){
            if(arr[low] > arr[high])
                return arr[low];
            else
                return arr[high];
        }
        //base case for when the array is decreasing then increasing
        if(arr[0] > arr[1])
            return Math.max(arr[0], arr[arr.length-1]);

        int mid = low + (high-low)/2;
        //currently, at the peak
        if(arr[mid] > arr[mid-1] && arr[mid] > arr[mid+1])
            return arr[mid];
        //currently increasing
        if(arr[mid] > arr[mid-1] && arr[mid] < arr[mid+1])
            return recursiveMaxSearch(arr, mid + 1, high);
        //currently decreasing
        else
            return recursiveMaxSearch(arr, low, mid);
    }

    /**
     * A routine to test the searchMax routine.
     */
    public static void main(String[] args) {
        for (int[] testCase : testCases) {
            System.out.println(searchMax(testCase));
        }
    }
}
