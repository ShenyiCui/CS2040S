///////////////////////////////////
// This is the main shift register class.
// Notice that it implements the ILFShiftRegister interface.
// You will need to fill in the functionality.
///////////////////////////////////

import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.util.Arrays;

/**
 * class ShiftRegister
 * @author
 * Description: implements the ILFShiftRegister interface.
 */
public class ShiftRegister implements ILFShiftRegister {
    int tap;
    int size;
    int register[];

    ShiftRegister(int size, int tap){
        this.tap = tap;
        this.size = size;
        register = new int[size];
    }

    /**
     * setSeed
     * @param seed
     * Description:
     */
    @Override
    public void setSeed(int[] seed) throws Exception{
       if(size != seed.length){
           throw new Exception("Size of Array and Register Size do not match");
       }
       register = seed;
    }

    /**
     * shift
     * @return
     * Description:
     */
    @Override
    public int shift() {
        int arrLen = register.length;
        int[] shiftedArr = new int[arrLen];
        for(int i = 0; i<arrLen-1; i++){
            shiftedArr[i+1] = register[i];
        }
        int leastSigDigit = (register[tap] ^ register[arrLen-1]);
        shiftedArr[0] = leastSigDigit;
        //System.out.println(Arrays.toString(register));
        register = shiftedArr;
        return leastSigDigit;
    }

    /**
     * generate
     * @param k
     * @return
     * Description:
     */
    @Override
    public int generate(int k) {
        int[] binaryArr = new int[k];
        for(int i = k-1; i>-1; i--) {
            binaryArr[i] = shift();
        }
        return toBinary(binaryArr);
    }

    /**
     * Returns the integer representation for a binary int array.
     * @param array
     * @return
     */
    private int toBinary(int[] array) {
        //System.out.println(Arrays.toString(array));
        int denaryNum = 0;
        int arrLength = array.length;
        for(int i = 0; i<arrLength; i++){
            denaryNum = (int) (denaryNum + Math.pow(2, i) * array[i]);
        }
        return denaryNum;
    }
}
