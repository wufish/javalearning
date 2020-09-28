package com.wufish.javalearning.jvm.bytecode;

/**
 * The type Byte utils.
 *
 * @Author wzj
 * @Create time : 2018/07/03 14:25
 * @Description:
 */
public class ByteUtils {

    /**
     * 当byte要转化为int的时候，高的24位必然会补1，这样，其二进制补码其实已经不一致了，
     * &0xff可以将高的24位置为0，低8位保持原样。这样做的目的就是为了保证二进制数据的一致性。
     *
     * @param b     the b
     * @param start the start
     * @param len   the len
     * @return int
     */
    public static int bytes2Int(byte[] b, int start, int len) {
        int sum = 0;
        int end = start + len;
        for (int i = start; i < end; i++) {
            int n = ((int) b[i]) & 0xff;
            n <<= (--len) * 8;
            sum += n;
        }
        return sum;
    }

    /**
     * 将value装换成len长度的字节数组
     *
     * @param value the value
     * @param len   the len
     * @return byte [ ]
     */
    public static byte[] int2Bytes(int value, int len) {
        byte[] b = new byte[len];
        for (int i = 0; i < len; i++) {
            b[len - i - 1] = (byte) ((value >> 8 * i) & 0xff);
        }
        return b;
    }

    /**
     * Bytes 2 string string.
     *
     * @param b     the b
     * @param start the start
     * @param len   the len
     * @return the string
     */
    public static String bytes2String(byte[] b, int start, int len) {
        return new String(b, start, len);
    }

    /**
     * String 2 bytes byte [ ].
     *
     * @param str the str
     * @return the byte [ ]
     */
    public static byte[] string2Bytes(String str) {
        return str.getBytes();
    }

    /**
     * Byte replace byte [ ].
     *
     * @param originalBytes the original bytes
     * @param offset        the offset
     * @param len           the len
     * @param replaceBytes  the replace bytes
     * @return the byte [ ]
     */
    public static byte[] byteReplace(byte[] originalBytes, int offset, int len, byte[] replaceBytes) {
        byte[] newBytes = new byte[originalBytes.length + (replaceBytes.length - len)];
        System.arraycopy(originalBytes, 0, newBytes, 0, offset);
        System.arraycopy(replaceBytes, 0, newBytes, offset, replaceBytes.length);
        System.arraycopy(originalBytes, offset + len, newBytes, offset + replaceBytes.length,
                originalBytes.length - offset - len);
        return newBytes;
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        byte[] bytes = int2Bytes(16, 2);
        System.out.println(bytes);
    }
}
