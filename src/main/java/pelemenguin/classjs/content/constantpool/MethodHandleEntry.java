package pelemenguin.classjs.content.constantpool;

import pelemenguin.classjs.content.ClassCreator;

public class MethodHandleEntry extends ConstantEntry {
    
    public static final byte TAG = 15;

    private final byte referenceKind;
    private final int referenceIndex;

    public MethodHandleEntry(byte referenceKind, int referenceIndex) {
        this.referenceKind = referenceKind;
        this.referenceIndex = referenceIndex;
    }

    public static int addEntryTo(ClassCreator creator, byte referenceKind, int referenceIndex) {
        MethodHandleEntry entry = new MethodHandleEntry(referenceKind, referenceIndex);
        return creator.addConstantEntry(entry);
    }

    @Override
    public byte[] generateByteCode() {
        return new byte[] {
            TAG,
            referenceKind,
            (byte) (referenceIndex >> 8),
            (byte) referenceIndex
        };
    }

    @Override
    public String toString() {
        return "MethodHandle(" + this.referenceKind + ", #" + this.referenceIndex + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof MethodHandleEntry mhe) {
            return this.referenceKind == mhe.referenceKind && this.referenceIndex == mhe.referenceIndex;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (referenceKind << 16) | referenceIndex;
    }

}
