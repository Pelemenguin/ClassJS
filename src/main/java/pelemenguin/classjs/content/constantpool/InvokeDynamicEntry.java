package pelemenguin.classjs.content.constantpool;

import pelemenguin.classjs.content.ClassCreator;

public class InvokeDynamicEntry extends ConstantEntry {
    
    public static final byte TAG = 18;

    private final int bootstrapMethodAttrIndex;
    private final int nameAndTypeIndex;

    public InvokeDynamicEntry(int bootstrapMethodAttrIndex, int nameAndTypeIndex) {
        this.bootstrapMethodAttrIndex = bootstrapMethodAttrIndex;
        this.nameAndTypeIndex = nameAndTypeIndex;
    }

    public static int addEntryTo(ClassCreator creator, int bootstrapMethodAttrIndex, String name, String type) {
        InvokeDynamicEntry entry = new InvokeDynamicEntry(
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
        return "InvokeDynamic(#" + this.bootstrapMethodAttrIndex + ".#" + this.nameAndTypeIndex + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof InvokeDynamicEntry ide) {
            return this.bootstrapMethodAttrIndex == ide.bootstrapMethodAttrIndex && this.nameAndTypeIndex == ide.nameAndTypeIndex;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (bootstrapMethodAttrIndex << 16) | nameAndTypeIndex;
    }

}
