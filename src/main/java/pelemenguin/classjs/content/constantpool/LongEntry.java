package pelemenguin.classjs.content.constantpool;

import java.nio.ByteBuffer;

public class LongEntry extends ConstantEntry {
    
    public static final byte TAG = 5;

    private long value;

    public LongEntry(long value) {
        this.value = value;
    }

    @Override
    public byte[] generateByteCode() {
        return ByteBuffer.allocate(9).put(TAG).putLong(this.value).array();
    }

    @Override
    public String toString() {
        return "Long(" + this.value + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof LongEntry le) {
            return this.value == le.value;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }

}
