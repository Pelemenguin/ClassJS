package pelemenguin.classjs.content.constantpool;

import pelemenguin.classjs.content.ClassCreator;

public class DynamicEntry extends ConstantEntry {
    
    public static final byte TAG = 17;

    private final int bootstrapMethodAttrIndex;
    private final int nameAndTypeIndex;

    public DynamicEntry(int bootstrapMethodAttrIndex, int nameAndTypeIndex) {
        this.bootstrapMethodAttrIndex = bootstrapMethodAttrIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    public static int addEntryTo(ClassCreator creator, int bootstrapMethodAttrIndex, String name, String type) {
        DynamicEntry entry = new DynamicEntry(
            bootstrapMethodAttrIndex,
            NameAndTypeEntry.addEntryTo(creator, name, type)
        );
        return creator.addConstantEntry(entry);
    }

    @Override
    public byte[] generateByteCode() {
        return new byte[] {
            TAG,
            (byte) (bootstrapMethodAttrIndex >> 8),
            (byte) bootstrapMethodAttrIndex,
            (byte) (nameAndTypeIndex >> 8),
            (byte) nameAndTypeIndex
        };
    }

    @Override
    public String toString() {
        return "Dynamic(#" + this.bootstrapMethodAttrIndex + ".#" + this.nameAndTypeIndex + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof DynamicEntry de) {
            return this.bootstrapMethodAttrIndex == de.bootstrapMethodAttrIndex && this.nameAndTypeIndex == de.nameAndTypeIndex;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (bootstrapMethodAttrIndex << 16) | nameAndTypeIndex;
    }

}
