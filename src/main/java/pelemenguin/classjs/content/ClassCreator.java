package pelemenguin.classjs.content;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import pelemenguin.classjs.content.constantpool.ConstantEntry;
import pelemenguin.classjs.library.ClassJSClassLoader;

@Info("A class for Java classes creation.")
public class ClassCreator {

    private String name;
    private ArrayList<ConstantEntry> constantPool = new ArrayList<>();

    public ClassCreator(String name) {
        if (ClassJSClassLoader.CREATED_CLASSES.containsKey(this.name)) {
            ConsoleJS.STARTUP.warn(
                "The class " + name + " has been created before. It is normal to see this warning during reloading. If not, check your code and see if you have some classes created more than one time."
            );
        }
        this.name = name;
        this.addConstantEntry(null); // Null reference at index 0
    }

    @Info(
        "Add a constant entry to the `ClassCreator`.\n" +
        "**Not suggested** because this method is direct, you have to import classes via `Java.loadClass` to use it.\n" +
        "\n" +
        "@param entry - A `ConstantEntry` object to be added into the class' constant pool.\n" +
        "@returns The pushed `ConstantEntry`'s index in the constant pool. Will be the last element's index if this constant is new.\n" +
        "         If this constant has already existed in the constant pool, the index of it will be directly returned."
    )
    public int addConstantEntry(ConstantEntry entry) {
        if (this.constantPool.size() >= 65536) {
            throw new RuntimeException("Too many constant pool entries for a class");
        }
        int i = this.constantPool.indexOf(entry);
        if (i < 0) {
            this.constantPool.add(entry);
            return (this.constantPool.size() - 1);
        } else {
            return i;
        }
    }

    public byte[] generateByteCode() {

        try (
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            DataOutputStream writer = new DataOutputStream(result)
        ) {
            writer.write(new byte[] {-54, -2, -70, -66}); // Magic Number: CA FE BA BE
            writer.write(new byte[] {0, 0}); // Minor version
            writer.write(new byte[] {0, 61}); // Major version (61 = Java 17)
            writer.writeShort(this.constantPool.size()); // Constant pool count
            for (ConstantEntry entry : this.constantPool) {
                if (entry != null) {
                    writer.write(entry.generateByteCode());
                }
            }
            // TODO: More contents
            return result.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Unable to generate byte code for " + this.toString(), e);
        }

    }

    @Override
    public String toString() {
        return "ClassCreator(" + this.name + ")";
    }

}
