package pelemenguin.classjs.content.constantpool;

import pelemenguin.classjs.content.ClassCreator;

public class PackageEntry extends ConstantEntry {
    
    public static final byte TAG = 20;

    private final int nameIndex;

    public PackageEntry(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    public static int addEntryTo(ClassCreator creator, String name) {
        PackageEntry entry = new PackageEntry(
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
        return "Package(#" + this.nameIndex + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof PackageEntry me) {
            return this.nameIndex == me.nameIndex;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return nameIndex;
    }

}
