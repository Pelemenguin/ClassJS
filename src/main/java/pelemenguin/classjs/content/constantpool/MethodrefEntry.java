package pelemenguin.classjs.content.constantpool;

import pelemenguin.classjs.content.ClassCreator;

public class MethodrefEntry extends ConstantEntry {
    
    public static final byte TAG = 10;

    private final int classIndex;
    private final int nameAndTypeIndex;

    public MethodrefEntry(int classIndex, int nameAndTypeIndex) {
        this.classIndex = classIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    public static int addEntryTo(ClassCreator creator, String className, String methodName, String methodDescriptor) {
        int classIndex = ClassEntry.addEntryTo(creator, className);
        int nameAndTypeIndex = NameAndTypeEntry.addEntryTo(creator, methodName, methodDescriptor);
        MethodrefEntry entry = new MethodrefEntry(classIndex, nameAndTypeIndex);
        return creator.addConstantEntry(entry);
    }

    @Override
    public byte[] generateByteCode() {
        return new byte[] {
            TAG,
            (byte) (this.classIndex >> 8),
            (byte) (this.classIndex),
            (byte) (this.nameAndTypeIndex >> 8),
            (byte) (this.nameAndTypeIndex)
        };
    }

    @Override
    public String toString() {
        return "Methodref(#" + this.classIndex + ".#" + this.nameAndTypeIndex + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof MethodrefEntry mre) {
            return this.classIndex == mre.classIndex && this.nameAndTypeIndex == mre.nameAndTypeIndex;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.classIndex << 16 | this.nameAndTypeIndex;
    }

}
