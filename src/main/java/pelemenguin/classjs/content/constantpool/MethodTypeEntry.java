package pelemenguin.classjs.content.constantpool;

import pelemenguin.classjs.content.ClassCreator;

public class MethodTypeEntry extends ConstantEntry {
    
    public static final byte TAG = 16;

    private final int descriptorIndex;

    public MethodTypeEntry(int descriptorIndex) {
        this.descriptorIndex = descriptorIndex;
    }

    public static int addEntryTo(ClassCreator creator, String descriptor) {
        MethodTypeEntry entry = new MethodTypeEntry(
            creator.addConstantEntry(new Utf8Entry(descriptor))
        );
        return creator.addConstantEntry(entry);
    }

    @Override
    public byte[] generateByteCode() {
        return new byte[] {
            TAG,
            (byte) (descriptorIndex >> 8),
            (byte) descriptorIndex
        };
    }

    @Override
    public String toString() {
        return "MethodType(#" + this.descriptorIndex + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof MethodTypeEntry mte) {
            return this.descriptorIndex == mte.descriptorIndex;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return descriptorIndex;
    }

}
