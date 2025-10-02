package pelemenguin.classjs.content;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import pelemenguin.classjs.library.ClassJSClassLoader;

@Info("A class for Java classes creation.")
public class ClassCreator {

    private static final String CUSTOM_PREFIX = "generated.kubejs.custom";

    private String packagePrefix = "generated.kubejs"; // Fallback value
    private String name;
    private ClassWriter classWriter;

    private ClassCreator(String name) {
        if (ClassJSClassLoader.CREATED_CLASSES.containsKey(this.name)) {
            ConsoleJS.STARTUP.warn(
                "The class " + name + " has been created before. It is normal to see this warning during reloading. If not, check your code and see if you have some classes created more than one time."
            );
        }
        this.name = name;
        this.classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
    }

    @Info(
        """
            Creates a new `ClassCreator` instance.

            @param className - The name of the class.
                               **Note:** All classes created here are under package `generated.kubejs.custom`.
                               If you want to hava your custom package structure, class name seperated by `.` like `yourPackage.yourClass` is OK.
            
            @example
            // Actually `generated.kubejs.custom.ExampleClass`
            let exampleClassCreator  = new ClassCreator("ExampleClass");

            // Actually `generated.kubejs.custom.ExamplePackage.ExampleClass`
            let exampleClassCreator2 = new ClassCreator("ExamplePackage.ExampleClass");
        """
    )
    public static ClassCreator create(String className) {
        ClassCreator created = new ClassCreator(className);
        created.packagePrefix = CUSTOM_PREFIX;
        return created;
    }

    public String getClassName() {
        return this.packagePrefix + "." + this.name;
    }

    public String getInternalName() {
        return this.packagePrefix.replace(".", "/") + "/" + this.name;
    }

    public Object defineClass() {
        this.classWriter.visit(Opcodes.V17, Opcodes.ACC_PUBLIC, this.getInternalName(), null, "java/lang/Object", null);
        return ClassJSClassLoader.INSTANCE.defineClass(this.getClassName(), this.classWriter.toByteArray());
    }

}
