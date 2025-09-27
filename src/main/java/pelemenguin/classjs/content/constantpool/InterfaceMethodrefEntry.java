package pelemenguin.classjs.content.constantpool;

import pelemenguin.classjs.content.ClassCreator;

public class InterfaceMethodrefEntry extends ConstantEntry {
    
    public static final byte TAG = 11;

    private final int classIndex;
    private final int nameAndTypeIndex;

    public InterfaceMethodrefEntry(int classIndex, int nameAndTypeIndex) {
        this.classIndex = classIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    public static int addEntryTo(ClassCreator creator, String className, String methodName, String methodNameDescriptor) {
        int classIndex = ClassEntry.addEntryTo(creator, className);
        int nameAndTypeIndex = NameAndTypeEntry.addEntryTo(creator, methodName, methodNameDescriptor);
        InterfaceMethodrefEntry entry = new InterfaceMethodrefEntry(classIndex, nameAndTypeIndex);
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
        return "InterfaceMethodref(#" + this.classIndex + ".#" + this.nameAndTypeIndex + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof InterfaceMethodrefEntry imre) {
            return this.classIndex == imre.classIndex && this.nameAndTypeIndex == imre.nameAndTypeIndex;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.classIndex << 16 | this.nameAndTypeIndex;
    }
    
}
