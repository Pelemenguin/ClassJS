package pelemenguin.classjs.content.constantpool;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.NativeJavaClass;
import pelemenguin.classjs.content.ClassCreator;

public class ClassEntry extends ConstantEntry {
    
    public static final byte TAG = 7;

    private int nameIndex;

    public ClassEntry(int nameIndex) {
        this.nameIndex = nameIndex;
    }

    /** <code>className</code> here must be a full qualified name (<code>/</code> seperated) */
    @Info(
        "Add a `ClassEntry` to a `ClassCreator`.\n" +
        "\n" +
        "This will also add a `Utf8Entry` for the class name if it does not already exist in the constant pool.\n" +
        "\n" +
        "@param classCreator - The `ClassCreator` to add the `ClassEntry` into.\n" +
        "@param className - The full qualified name of the class (with `/` as separator).\n" +
        "@returns The index of the added `ClassEntry` in the constant pool."
    )
    public static int addEntryTo(ClassCreator classCreator, String className) {
        return classCreator.addConstantEntry(new ClassEntry(
            classCreator.addConstantEntry(new Utf8Entry(className))
        ));
    }

    @Info(
        "Add a `ClassEntry` to a `ClassCreator` via a class loaded by `Java.loadClass`.\n" +
        "\n" +
        "This will also add a `Utf8Entry` for the class name if it does not already exist in the constant pool.\n" +
        "\n" +
        "@param classCreator - The `ClassCreator` to add the `ClassEntry` into.\n" +
        "@param clazz - The loaded Java class.\n" +
        "@returns The index of the added `ClassEntry` in the constant pool."
    )
    public static int addEntryTo(ClassCreator classCreator, NativeJavaClass clazz) {
        return addEntryTo(classCreator, clazz.getClassObject().getName().replace('.', '/'));
    }

    @Override
    public byte[] generateByteCode() {
        return new byte[] {TAG, (byte) (this.nameIndex >> 8), (byte) (this.nameIndex)};
    }

    @Override
    public String toString() {
        return "Class(#" + this.nameIndex + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ClassEntry ce) {
            return this.nameIndex == ce.nameIndex;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return nameIndex;
    }

}
