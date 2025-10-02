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

    private int version = Opcodes.V17;
    private int access = Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER;

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

    @Info(
        """
        Specify the version of the class file.
        Default value is `61`.

        @param version - The class file's version. According to [Java Virtual Machine Specification](https://docs.oracle.com/javase/specs/jvms/se17/html/jvms-4.html#jvms-4.1-200-B.2),
            the class file version of Java 17 (which is used by Minecraft 1.20.1) should be `61`.
            For other version numbers, see the table below:

            | Java Version | Class File Version |
            |--------------|--------------------|
            | 1.1          | 45                 |
            | 1.2          | 46                 |
            | 1.3          | 47                 |
            | 1.4          | 48                 |
            | 5            | 49                 |
            | 6            | 50                 |
            | 7            | 51                 |
            | 8            | 52                 |
            | 9            | 53                 |
            | 10           | 54                 |
            | 11           | 55                 |
            | 12           | 56                 |
            | 13           | 57                 |
            | 14           | 58                 |
            | 15           | 59                 |
            | 16           | 60                 |
            | 17           | 61                 |

            Higher version is not suggested because Minecraft 1.20.1 uses Java 17.
            You cannot expect users to have Java of higher versions installed.

        @returns This `ClassCreator` instance.
        """
    )
    public ClassCreator version(int version) {
        this.version = version;
        return this;
    }

    // Choose one in `class`, `interface`, `enum`, `annotation`, or `module`

    @Info(
        """
        Set the class to be a normal `class`.
        This will remove `interface`, `enum` and `module` access modifiers, and add `super` access modifier.

        @returns This `ClassCreator` instance.
        """
    )
    public ClassCreator toClass() {
        this.access &= ~(Opcodes.ACC_INTERFACE | Opcodes.ACC_ENUM | Opcodes.ACC_MODULE);
        this.access |= Opcodes.ACC_SUPER;
        return this;
    }

    @Info(
        """
        Set the class to be an `interface`.
        This will remove `final`, `super`, `enum`, and `module` access modifiers, and add `interface` and `abstract` access modifiers.

        @returns This `ClassCreator` instance.
        """
    )
    public ClassCreator toInterface() {
        this.toClass();
        this.access &= ~(Opcodes.ACC_FINAL | Opcodes.ACC_SUPER);
        this.access |= (Opcodes.ACC_ABSTRACT | Opcodes.ACC_INTERFACE);
        return this;
    }

    @Info(
        """
        Set the class to be an `enum`.
        This will remove `abstract` access modifier, and add `enum` access modifier.

        @returns This `ClassCreator` instance.
        """
    )
    public ClassCreator toEnum() {
        this.toClass();
        this.access &= ~Opcodes.ACC_ABSTRACT;
        this.access |= Opcodes.ACC_ENUM;
        return this;
    }

    @Info(
        """
        Set the class to be an `annotation`. (`@interface` in Java.)
        This will remove `final`, `super`, and `enum` access modifiers, and add `interface`, `abstract`, and `annotation` access modifiers.

        @returns This `ClassCreator` instance.
        """
    )
    public ClassCreator toAnnotation() {
        this.toInterface();
        this.access |= Opcodes.ACC_ANNOTATION;
        return this;
    }

    @Info(
        """
        Set the class to be a `module`.
        This will remove `final`, `interface`, `enum`, and `annotation` access modifiers, and add `module` access modifier.

        @returns This `ClassCreator` instance.
        """
    )
    public ClassCreator toModule() {
        this.toClass();
        this.access |= Opcodes.ACC_MODULE;
        return this;
    }

    @Info(
        """
        Add the `public` access modifier.
        A *public* class can be accessed outside its package.

        @returns This `ClassCreator` instance.
        """
    )
    public ClassCreator toPublic() {
        this.access |= Opcodes.ACC_PUBLIC;
        return this;
    }

    @Info(
        """
        Remove the `public` access modifier.
        A *public* class can be accessed outside its package.

        @returns This `ClassCreator` instance.
        """
    )
    public ClassCreator notPublic() {
        this.access &= ~Opcodes.ACC_PUBLIC;
        return this;
    }

    @Info(
        """
        Add the `final` access modifier.
        A *final* class cannot be subclassed.

        *Note:* Setting a class to `final` causes `abstract` access modifier to be removed.

        @returns This `ClassCreator` instance.
        """
    )
    public ClassCreator toFinal() {
        this.access &= ~Opcodes.ACC_ABSTRACT;
        this.access |= Opcodes.ACC_FINAL;
        return this;
    }

    @Info(
        """
        Remove the `final` access modifier.
        A *final* class cannot be subclassed.

        @returns The `ClassCreator` instance.
        """
    )
    public ClassCreator notFinal() {
        this.access &= ~Opcodes.ACC_FINAL;
        return this;
    }

    // No cancel and set for `super`, as they were required in all the Java classes.
    // `super` is only cancelled when `interface` is set.

    @Info(
        """
        Remove the `interface` access modifier.
        The `interface` access modifier indicates that the class file defines an *interface*, not a class.

        @returns This `ClassCreator` instance.
        """
    )
    public ClassCreator notInterface() {
        this.access &= ~(Opcodes.ACC_INTERFACE | Opcodes.ACC_ABSTRACT);
        return this;
    }

    @Info(
        """
        Add the `abstract` access modifier.
        An *abstract* class cannot be instantiated, and may contain abstract methods, which are methods that are declared without an implementation.

        *Note:* Setting a class to `abstract` causes `final` access modifier to be removed.

        @returns This `ClassCreator` instance.
        """
    )
    public ClassCreator toAbstract() {
        this.access &= ~Opcodes.ACC_FINAL;
        this.access |= Opcodes.ACC_ABSTRACT;
        return this;
    }

    @Info(
        """
        Remove the `abstract` access modifier.
        An *abstract* class cannot be instantiated, and may contain abstract methods, which are methods that are declared without an implementation.

        @returns This `ClassCreator` instance.
        """
    )
    public ClassCreator notAbstract() {
        this.access &= ~Opcodes.ACC_ABSTRACT;
        return this;
    }

    @Info(
        """
        Add the `synthetic` access modifier.
        A *synthetic* class is not present in the source code, but is generated by the compiler.

        @returns This `ClassCreator` instance.
        """
    )
    public ClassCreator toSynthetic() {
        this.access |= Opcodes.ACC_SYNTHETIC;
        return this;
    }

    @Info(
        """
        Remove the `synthetic` access modifier.
        A *synthetic* class is not present in the source code, but is generated by the compiler.

        @returns This `ClassCreator` instance.
        """
    )
    public ClassCreator notSynthetic() {
        this.access &= ~Opcodes.ACC_SYNTHETIC;
        return this;
    }

    public Class<?> defineClass() {
        this.classWriter.visit(this.version, this.access, this.getInternalName(), null, "java/lang/Object", null);
        return ClassJSClassLoader.INSTANCE.defineClass(this.getClassName(), this.classWriter.toByteArray());
    }

}
