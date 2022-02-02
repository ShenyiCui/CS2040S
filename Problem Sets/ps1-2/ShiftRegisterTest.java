import static org.junit.Assert.*;

import org.junit.Test;

/**
 * ShiftRegisterTest
 * @author dcsslg
 * Description: set of tests for a shift register implementation
 */
/*
 * In a proper situation an erroneous test should throw an error that will be caught such that the code doesn't
 * continue pass the error. The error should thereafter be part of a feedback to the user.
 * This can be achieved Java's try catch and throw exception.
 * To test this case we should expect our erroneous test case to receive a specific error from the code.
 * If an error is received the error test would be considered a success.
 * This can be added to the JUNIT test by adding an expected Exception to @Test e.g.
 * @Test(expected = RuntimeException.class, message = "Size of Array and Register Size do not match")
 */
public class ShiftRegisterTest {
    /**
     * Returns a shift register to test.
     * @param size
     * @param tap
     * @return a new shift register
     */
    ILFShiftRegister getRegister(int size, int tap){
        return new ShiftRegister(size, tap);
    }

    /**
     * Tests shift with simple example.
     */
    @Test
    public void testShift1() {
        try {
            ILFShiftRegister r = getRegister(9, 7);
            int[] seed = {0, 1, 0, 1, 1, 1, 1, 0, 1};
            r.setSeed(seed);
            int[] expected = {1, 1, 0, 0, 0, 1, 1, 1, 1, 0};
            for (int i = 0; i < 10; i++) {
                assertEquals(expected[i], r.shift());
            }
        } catch (Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testShift2(){
        try {
            int[] array = new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1};
            ShiftRegister shifter = new ShiftRegister(9, 2);
            shifter.setSeed(array);
            int[] expected = {0, 0, 0, 1, 1, 1, 0, 0, 0, 0};
            for (int i = 0; i < 10; i++) {
                int j = shifter.shift();
                assertEquals(expected[i], j);
            }
        } catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * Tests generate with simple example.
     */
    @Test
    public void testGenerate1() {
        try {
            ILFShiftRegister r = getRegister(9, 7);
            int[] seed = {0, 1, 0, 1, 1, 1, 1, 0, 1};
            r.setSeed(seed);
            int[] expected = {6, 1, 7, 2, 2, 1, 6, 6, 2, 3};
            for (int i = 0; i < 10; i++) {
                assertEquals("GenerateTest", expected[i], r.generate(3));
            }
        } catch (Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testGenerate2() {
        try {
            ILFShiftRegister shifter = getRegister(6, 1);
            int[] seed = new int[] {1, 0, 1, 0, 1, 0};
            shifter.setSeed(seed);
            int[] expected = {0, 1, 0, 0, 1, 1, 1};
            for (int i = 0; i < 7; i++) {
                int j = shifter.generate(2);
                assertEquals("GenerateTest", expected[i], j);
            }
        } catch (Exception e){
            System.out.println(e);
        }
    }


    /**
     * Tests register of length 1.
     */
    @Test
    public void testOneLength() {
        try{
            ILFShiftRegister r = getRegister(1, 0);
            int[] seed = {1};
            r.setSeed(seed);
            int[] expected = {0,0,0,0,0,0,0,0,0,0};
            for (int i=0; i<10; i++){
                assertEquals(expected[i], r.generate(3));
            }
        } catch (Exception e){
            System.out.println(e);
        }
    }

    @Test
    public void testZeroGenerate() {
        try{
            ILFShiftRegister r = getRegister(5, 1);
            int[] seed = {1,1,1,1,1};
            r.setSeed(seed);
            int[] expected = {0,0,0,0,0,0,0,0,0,0};
            for (int i=0; i<10; i++){
                assertEquals(expected[i], r.generate(0));
            }
        } catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * Tests with erroneous seed.
     */
    @Test
    public void testError() {
        try {
            ILFShiftRegister r = getRegister(4, 1);
            int[] seed = {1, 0, 0, 0, 1, 1, 0};
            r.setSeed(seed);
            r.shift();
            r.generate(4);
        } catch (Exception e){
            System.out.println(e);
        }
    }

}
