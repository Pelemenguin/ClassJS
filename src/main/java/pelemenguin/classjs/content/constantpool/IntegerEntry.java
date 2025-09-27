package pelemenguin.classjs.content.constantpool;

import java.nio.ByteBuffer;

public class IntegerEntry extends ConstantEntry {
    
    public static final byte TAG = 3;

    private int value;

    public IntegerEntry(int value) {
        this.value = value;
    }

    @Override
    public byte[] generateByteCode() {
        return ByteBuffer.allocate(5).put(TAG).putInt(this.value).array();
    }

    @Override
    public String toString() {
        return "Integer(" + this.value + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof IntegerEntry integerEntry) {
            return this.value == integerEntry.value;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value;
    }

}
