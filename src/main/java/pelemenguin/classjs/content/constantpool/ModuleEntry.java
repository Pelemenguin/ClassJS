package pelemenguin.classjs.content.constantpool;

import pelemenguin.classjs.content.ClassCreator;

public class ModuleEntry extends ConstantEntry {
    
    public static final byte TAG = 19;

    private final int nameIndex;

    public ModuleEntry(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    public static int addEntryTo(ClassCreator creator, String name) {
        ModuleEntry entry = new ModuleEntry(
            creator.addConstantEntry(new Utf8Entry(name))
        );
        return creator.addConstantEntry(entry);
    }

    @Override
    public byte[] generateByteCode() {
        return new byte[] {
            TAG,
            (byte) (nameIndex >> 8),
            (byte) nameIndex
        };
    }

    @Override
    public String toString() {
        return "Module(#" + this.nameIndex + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ModuleEntry me) {
            return this.nameIndex == me.nameIndex;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return nameIndex;
    }

}
