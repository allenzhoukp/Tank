package zhou.kunpeng.tank.comm;

/**
 * Created by JA on 2017/5/22.
 * Code source: http://blog.csdn.net/giantpoplar/article/details/47657333
 */
public class ByteUtil {

    public static byte[] append(byte[] org, byte[] to) {
        byte[] newByte = new byte[org.length + to.length];
        System.arraycopy(org, 0, newByte, 0, org.length);
        System.arraycopy(to, 0, newByte, org.length, to.length);
        return newByte;

    }

    public static byte[] getByteArray(char c) {
        byte[] b = new byte[2];
        b[0] = (byte) ((c & 0xff00) >> 8);
        b[1] = (byte) (c & 0x00ff);
        return b;
    }

    /**
     * get a char from arr[index] to arr[index + 2].
     */
    public static char getChar(byte[] arr, int index) {
        return (char) (0xff00 & arr[index] << 8 | (0xff & arr[index + 1]));
    }

    public static byte[] getByteArray(short s) {
        byte[] b = new byte[2];
        b[0] = (byte) ((s & 0xff00) >> 8);
        b[1] = (byte) (s & 0x00ff);
        return b;
    }

    /**
     * get a short from arr[index] to arr[index + 2].
     */
    public static short getShort(byte[] arr, int index) {
        return (short) (0xff00 & arr[index] << 8 | (0xff & arr[index + 1]));
    }

    public static byte[] getByteArray(int i) {
        byte[] b = new byte[4];
        b[0] = (byte) ((i & 0xff000000) >> 24);
        b[1] = (byte) ((i & 0x00ff0000) >> 16);
        b[2] = (byte) ((i & 0x0000ff00) >> 8);
        b[3] = (byte) (i & 0x000000ff);
        return b;
    }

    /**
     * get an int from arr[index] to arr[index + 4].
     */
    public static int getInt(byte[] arr, int index) {
        return (0xff000000 & (arr[index + 0] << 24)) |
                (0x00ff0000 & (arr[index + 1] << 16)) |
                (0x0000ff00 & (arr[index + 2] << 8)) |
                (0x000000ff & arr[index + 3]);
    }

    public static byte[] getByteArray(float f) {
        int intBits = Float.floatToIntBits(f);
        return getByteArray(intBits);
    }

    /**
     * get a float from arr[index] to arr[index + 4].
     */
    public static float getFloat(byte[] arr, int index) {
        return Float.intBitsToFloat(getInt(arr, index));
    }

    public static byte[] getByteArray(long l) {
        byte b[] = new byte[8];
        b[0] = (byte) (0xff & (l >> 56));
        b[1] = (byte) (0xff & (l >> 48));
        b[2] = (byte) (0xff & (l >> 40));
        b[3] = (byte) (0xff & (l >> 32));
        b[4] = (byte) (0xff & (l >> 24));
        b[5] = (byte) (0xff & (l >> 16));
        b[6] = (byte) (0xff & (l >> 8));
        b[7] = (byte) (0xff & l);
        return b;
    }

    /**
     * get a long from arr[index] to arr[index + 8].
     */
    public static long getLong(byte[] arr, int index) {
        return (0xff00000000000000L & ((long) arr[index + 0] << 56)) |
                (0x00ff000000000000L & ((long) arr[index + 1] << 48)) |
                (0x0000ff0000000000L & ((long) arr[index + 2] << 40)) |
                (0x000000ff00000000L & ((long) arr[index + 3] << 32)) |
                (0x00000000ff000000L & ((long) arr[index + 4] << 24)) |
                (0x0000000000ff0000L & ((long) arr[index + 5] << 16)) |
                (0x000000000000ff00L & ((long) arr[index + 6] << 8)) |
                (0x00000000000000ffL & (long) arr[index + 7]);
    }

    public static byte[] getByteArray(double d) {
        long longBits = Double.doubleToLongBits(d);
        return getByteArray(longBits);
    }

    /**
     * get a double from arr[index] to arr[index + 8].
     */
    public static double getDouble(byte[] arr, int index) {
        return Double.longBitsToDouble(getLong(arr, index));
    }
}