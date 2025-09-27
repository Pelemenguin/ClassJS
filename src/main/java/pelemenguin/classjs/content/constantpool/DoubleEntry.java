package pelemenguin.classjs.content.constantpool;

import java.nio.ByteBuffer;

public class DoubleEntry extends ConstantEntry {
    
    public static final byte TAG = 6;

    private double value;

    public DoubleEntry(double value) {
        this.value = value;
    }

    @Override
    public byte[] generateByteCode() {
        return ByteBuffer.allocate(9).put(TAG).putDouble(this.value).array();
    }

    @Override
    public String toString() {
        return "Double(" + this.value + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof DoubleEntry de) {
            return this.value == de.value;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

}
