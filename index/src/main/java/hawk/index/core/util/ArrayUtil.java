package hawk.index.core.util;

public class ArrayUtil {

    public static byte[] bytesConcatenation(byte[] bytes1, byte[] bytes2){
        byte[] temp = new byte[bytes1.length + bytes2.length];
        System.arraycopy(bytes1, 0, temp, 0, bytes1.length);
        System.arraycopy(bytes2, 0, temp, bytes1.length, bytes2.length);
        return temp;
    }

    public static int[] intsConcatenation(int[] a, int[] b){
        int[] temp = new int[a.length + b.length];
        System.arraycopy(a, 0, temp, 0, a.length);
        System.arraycopy(b, 0, temp, a.length, b.length);
        return temp;
    }

    public static byte[][] bytePoolGrow(byte[][] old){
        byte[][] ret = new byte[old.length+1][];
        for (int i = 0; i < old.length; i++) {
            ret[i] = old[i];
        }
        return ret;
    }
}
