package iot_sensor.util;

public class Checkout {
    public static final byte HEADER = 0x55;

    /**
     * 异或校验(首位不校验)
     *
     * @param bt
     * @return
     */
    public static byte[] xor(byte[] bt) {
        int len = bt.length;
        byte[] result = new byte[len + 1];
        for (int i = 0; i < len; i++) {
            result[i] = bt[i];
            if (i != 0) {
                result[len] ^= bt[i];
            }
        }
        return result;
    }

    /**
     * 校验指定数据是否满足异或校验规则
     *
     * @param bt
     * @return
     */
    public static boolean xor_check(byte[] bt) {
        if (bt[0] != HEADER || bt.length < 3) {
            return false;
        } else {
            int check = 0;
            for (int i = 1, len = bt.length; i < len; i++) {
                check ^= bt[i];
            }
            return check == 0 ? true : false;
        }
    }
}
