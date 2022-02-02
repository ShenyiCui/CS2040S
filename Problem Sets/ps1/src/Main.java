public class Main {
    static int MysteryFunction(int argA, int argB) {
        int c = 1;
        int d = argA;
        int e = argB;
        while (e > 0) {
            System.out.println(2 * (e / 2));
            if (2 * (e / 2) != e) {
                c = c * d;
                System.out.println("c: " + c);
            }
            d = d * d;
            e = e / 2;

            System.out.println("d: " + d);
            System.out.println("e: " + e);
            System.out.println("");
        }
        return c;
    }

    public static void main(String args[]) {
        int output = MysteryFunction(3, 5);
        System.out.printf("The answer is: " + output + ".");
    }
}
