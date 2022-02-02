public class Test {
    public static void main(String args[]){
        try{
            int[] array = new int[] {1,0,1,0,1,0};
            ShiftRegister shifter = new ShiftRegister(6, 1);
            shifter.setSeed(array);
            for (int i = 0; i < 7; i++) {
                int j = shifter.generate(0);
                System.out.print(j);
            }
        } catch (Exception e){
            System.out.println(e);
        }

    }
}
