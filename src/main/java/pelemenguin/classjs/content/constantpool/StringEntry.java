package pelemenguin.classjs.content.constantpool;

import pelemenguin.classjs.content.ClassCreator;

public class StringEntry extends ConstantEntry {
    
    public static final byte TAG = 8;

    private final int stringIndex;

    public StringEntry(int stringIndex) {
        this.stringIndex = stringIndex;
    }

    public static int addEntryTo(ClassCreator creator, String value) {
        StringEntry entry = new StringEntry(
            creator.addConstantEntry(new Utf8Entry(value))
        );
        return creator.addConstantEntry(entry);
    }

    @Override
    public byte[] generateByteCode() {
        return new byte[] {
            TAG,
            (byte) (stringIndex >> 8),
            (byte) stringIndex
        };
    }

    @Override
    public String toString() {
        return "String(#" + this.stringIndex + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof StringEntry se) {
            return this.stringIndex == se.stringIndex;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return stringIndex;
    }

}
