package pelemenguin.classjs.content.constantpool;

import pelemenguin.classjs.content.ClassCreator;

public class FieldrefEntry extends ConstantEntry {
    
    public static final byte TAG = 9;

    private final int classIndex;
    private final int nameAndTypeIndex;

    public FieldrefEntry(int classIndex, int nameAndTypeIndex) {
        this.classIndex = classIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    public static int addEntryTo(ClassCreator creator, String className, String fieldName, String fieldDescriptor) {
        int classIndex = ClassEntry.addEntryTo(creator, className);
        int nameAndTypeIndex = NameAndTypeEntry.addEntryTo(creator, fieldName, fieldDescriptor);
        FieldrefEntry entry = new FieldrefEntry(classIndex, nameAndTypeIndex);
        return creator.addConstantEntry(entry);
    }

    @Override
    public byte[] generateByteCode() {
        return new byte[] {
            TAG,
            (byte) (classIndex >> 8),
            (byte) (classIndex),
            (byte) (nameAndTypeIndex >> 8),
            (byte) (nameAndTypeIndex)
        };
    }

    @Override
    public String toString() {
        return "Methodref(#" + this.classIndex + ".#" + this.nameAndTypeIndex + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof FieldrefEntry fre) {
            return this.classIndex == fre.classIndex && this.nameAndTypeIndex == fre.nameAndTypeIndex;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.classIndex << 16 | this.nameAndTypeIndex;
    }

}
