package pelemenguin.classjs.util;

import org.slf4j.Logger;

public class ByteCodeLogUtils {

    // No constructors available
    private ByteCodeLogUtils() {}

    private static final int BYTES_PER_LINE = 16;

    private static final char[] HEX_TABLE = new char[] {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    /**
     * Converts an <code>byte</code> array to hex string.
     * 
     * <p>
     * There <b>IS</b> a <code>"\n"</code> at the beginning of the string.
     * 
     * @param bytes - Input bytes.
     * @return Result string
     */
    public static String logBytes(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i ++) {
            if (i % BYTES_PER_LINE == 0) {
                sb.append('\n');
            }
            int b = bytes[i] & 0xFF;
            sb.append(' ');
            sb.append(HEX_TABLE[(b >> 4) & 0x0F]);
            sb.append(HEX_TABLE[b & 0x0F]);
        }
        return sb.toString();
    }

    public static void logClassCreated(Logger logger, String className, byte[] bytes) {
        logger.debug("Class created: (" + className + ")" + logBytes(bytes));
    }
}
