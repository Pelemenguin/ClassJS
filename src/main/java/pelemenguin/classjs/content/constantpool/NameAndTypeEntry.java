package pelemenguin.classjs.content.constantpool;

import pelemenguin.classjs.content.ClassCreator;

public class NameAndTypeEntry extends ConstantEntry {
    
    public static final byte TAG = 12;

    private final int nameIndex;
    private final int descriptorIndex;

    public NameAndTypeEntry(int nameIndex, int descriptorIndex) {
        this.nameIndex = nameIndex;
        this.descriptorIndex = descriptorIndex;
    }

    public static int addEntryTo(ClassCreator creator, String name, String descriptor) {
        NameAndTypeEntry entry = new NameAndTypeEntry(
            creator.addConstantEntry(new Utf8Entry(name)),
            creator.addConstantEntry(new Utf8Entry(descriptor))
        );
        return creator.addConstantEntry(entry);
    }

    @Override
    public byte[] generateByteCode() {
        return new byte[] {
            TAG,
            (byte) (nameIndex >> 8), (byte) nameIndex,
            (byte) (descriptorIndex >> 8), (byte) descriptorIndex
        };
    }

    @Override
    public String toString() {
        return "NameAndType((#" + this.descriptorIndex + ") #" + this.nameIndex + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof NameAndTypeEntry nameAndTypeEntry) {
            return this.nameIndex == nameAndTypeEntry.nameIndex && this.descriptorIndex == nameAndTypeEntry.descriptorIndex;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (nameIndex << 16) | descriptorIndex;
    }

}
