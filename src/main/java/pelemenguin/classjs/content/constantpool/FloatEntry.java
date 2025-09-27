package pelemenguin.classjs.content.constantpool;

import java.nio.ByteBuffer;

public class FloatEntry extends ConstantEntry {
    
    public static final byte TAG = 4;

    private float value;

    public FloatEntry(float value) {
        this.value = value;
    }

    @Override
    public byte[] generateByteCode() {
        return ByteBuffer.allocate(5).put(TAG).putFloat(this.value).array();
    }

    @Override
    public String toString() {
        return "Float(" + this.value + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof FloatEntry fe) {
            return this.value == fe.value;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Float.hashCode(this.value);
    }

}
