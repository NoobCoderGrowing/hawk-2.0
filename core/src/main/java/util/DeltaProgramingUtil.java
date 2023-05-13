package util;

public class DeltaProgramingUtil {


    public static byte[][] generateDeltaVintList(int base, int[] list){
        if(list.length < 1) return new byte[0][];
        byte[][] result = new byte[list.length][];
        result[0] = NumberUtil.int2Vint(list[0] - base);
        for (int i = 1; i < result.length; i++) {
            int delta = list[i] - list[i-1];
            result[i] = NumberUtil.int2Vint(delta);
        }
        return result;
    }

    public static byte[][] generateDeltaVintList(int[] list){
        if(list.length < 1) return new byte[0][];
        byte[][] result = new byte[list.length][];
        result[0] = NumberUtil.int2Vint(list[0]);
        for (int i = 1; i < result.length; i++) {
            int delta = list[i] - list[i-1];
            result[i] = NumberUtil.int2Vint(delta);
        }
        return result;
    }

    public static int[] decodeDeltaVintList(byte[][] deltaList){
        if(deltaList.length < 1) return new int[0];
        int[] result = new int[deltaList.length];
        result[0] = DataInput.readVint(deltaList[0]);
        for (int i = 1; i < result.length; i++) {
            result[i] = DataInput.readVint(deltaList[i]) + result[i-1];
        }
        return result;
    }

//    public static void main(String[] args) {
//        int[] list = new int[]{1, 7, 9, 85 ,103, 106};
//        byte[][] delataList = DeltaProgramingUtil.generateDeltaVintList(-1, list);
//        int[] result = DeltaProgramingUtil.decodeDeltaVintList(delataList);
//        for (int i = 0; i < result.length; i++) {
//            System.out.println(result[i]);
//        }
//    }
}
