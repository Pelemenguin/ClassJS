package pelemenguin.classjs.content;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

import dev.latvian.mods.kubejs.typings.Info;
import pelemenguin.classjs.util.ClassNameWrapper;
import pelemenguin.classjs.util.DescriptorUtils;
import pelemenguin.classjs.util.SignatureUtils;

public class FieldCreator {

    private ClassCreator parent;
    private String name;
    private String descriptor;
    private String signature = null;

    protected Consumer<FieldVisitor> annotations = fv -> {};

    private int access = 0;
    private Object defaultValue = null;

    public FieldCreator(ClassCreator parent, String name, ClassNameWrapper type) {
        this.parent = parent;
        this.name = name;
        this.descriptor = type.toFieldDescriptor();
    }

    @Info(
        """
        Set the generic signature of this field.

        A *generic signature* provides additional type information for fields, methods, and classes that use generics.

        **Note:** This method is optional. If not set, the field will not have a generic signature.

        @param rawType The raw type of the field. For example, `java.util.List` for a field of type `List<String>`.
        @param signature The generic signature string.
        @returns This `FieldCreator` instance.

        @example
        // Create a field with generic signature
        .createField("myList", "java.util.List")
        .signature("java.util.List", (sig) => {
            sig.appendType("java.lang.String");
        })
        .build()
        """
    )
    public FieldCreator signature(Consumer<SignatureUtils.TypeSignatureBuilder> typeSignatureBuilder) {
        SignatureUtils.TypeSignatureBuilder tsb = new SignatureUtils.TypeSignatureBuilder(ClassNameWrapper.fromClassName(DescriptorUtils.fromFieldDescriptor(this.descriptor)));
        typeSignatureBuilder.accept(tsb);
        this.signature = tsb.toString();
        return this;
    }
    
    @Info(
        """
        Annotate this field with the specified annotation.

        Annotations provide metadata about the field that can be used by the compiler and runtime.

        **Note:** This method can be called multiple times to add multiple annotations.

        @param annotationClass The class name of the annotation to add.
        @returns An `AnnotationCreator` instance to configure the annotation further.
        """
    )
    public AnnotationCreator<FieldCreator> annotated(ClassNameWrapper annotationClass) {
        return new AnnotationCreator<>(this, annotationClass);
    }

    @Info(
        """
        Set the access modifier of this field to `default` (package-private).

        A *default* field is neither `public`, `private` nor `protected`, it is considered to have *default* (package-private) access.

        **Note:** This method only removes `public`, `private` and `protected` modifiers.
            If you want to remove other modifiers like `static` or `final`, use corresponding methods like `notStatic()` or `notFinal()`.
        
        @returns This `FieldCreator` instance.

        @example
        // Create a field with default (package-private) access
        .createField("myField", "int")
        .toDefault()
        .build()
        """
    )
    public FieldCreator toDefault() {
        this.access &= ~(Opcodes.ACC_PUBLIC | Opcodes.ACC_PRIVATE | Opcodes.ACC_PROTECTED);
        return this;
    }

    @Info(
        """
        Set the access modifier of this field to `public`.

        A *public* field is accessible from any other class.

        **Note:** Fields of interfaces **must** be `public`.

        @returns This `FieldCreator` instance.
        """
    )
    public FieldCreator toPublic() {
        this.toDefault();
        this.access |= Opcodes.ACC_PUBLIC;
        return this;
    }

    @Info(
        """
        Set the access modifier of this field to `private`.

        A *private* field is only accessible within the class it is defined in.

        @returns This `FieldCreator` instance.
        """
    )
    public FieldCreator toPrivate() {
        this.toDefault();
        this.access |= Opcodes.ACC_PRIVATE;
        return this;
    }

    @Info(
        """
        Set the access modifier of this field to `protected`.

        A *protected* field is accessible within its own package and by subclasses.

        @returns This `FieldCreator` instance.
        """
    )
    public FieldCreator toProtected() {
        this.toDefault();
        this.access |= Opcodes.ACC_PROTECTED;
        return this;
    }

    @Info(
        """
        Make this field `static`.

        A *static* field belongs to the class itself rather than to any specific instance of the class.

        **Note:** Fields of interfaces **must** be `static`.

        @returns This `FieldCreator` instance.
        """
    )
    public FieldCreator toStatic() {
        this.access |= Opcodes.ACC_STATIC;
        return this;
    }

    @Info(
        """
        Remove the `static` modifier from this field.

        @returns This `FieldCreator` instance.
        """
    )
    public FieldCreator notStatic() {
        this.access &= ~Opcodes.ACC_STATIC;
        return this;
    }

    @Info(
        """
        Make this field `final`.

        A *final* field can only be assigned once, either during its declaration or within the constructor of the class.

        **Note:** This flag cannot be set if the field is also marked as `volatile`.
            Fields of interfaces **must** be `final`.

        @returns This `FieldCreator` instance.
        """
    )
    public FieldCreator toFinal() {
        this.access |= Opcodes.ACC_FINAL;
        this.access &= ~Opcodes.ACC_VOLATILE;
        return this;
    }

    @Info(
        """
        Remove the `final` modifier from this field.

        @returns This `FieldCreator` instance.
        """
    )
    public FieldCreator notFinal() {
        this.access &= ~Opcodes.ACC_FINAL;
        return this;
    }

    @Info(
        """
        Make this field `volatile`.

        A *volatile* field indicates that its value may be changed by different threads.

        **Note:** This flag cannot be set if the field is also marked as `final`.

        @returns This `FieldCreator` instance.
        """
    )
    public FieldCreator toVolatile() {
        this.access |= Opcodes.ACC_VOLATILE;
        this.access &= ~Opcodes.ACC_FINAL;
        return this;
    }

    @Info(
        """
        Remove the `volatile` modifier from this field.

        @returns This `FieldCreator` instance.
        """
    )
    public FieldCreator notVolatile() {
        this.access &= ~Opcodes.ACC_VOLATILE;
        return this;
    }

    @Info(
        """
        Make this field `transient`.

        A *transient* field is not serialized when the object containing it is serialized.

        @returns This `FieldCreator` instance.
        """
    )
    public FieldCreator toTransient() {
        this.access |= Opcodes.ACC_TRANSIENT;
        return this;
    }

    @Info(
        """
        Remove the `transient` modifier from this field.

        @returns This `FieldCreator` instance.
        """
    )
    public FieldCreator notTransient() {
        this.access &= ~Opcodes.ACC_TRANSIENT;
        return this;
    }

    @Info(
        """
        Make this field `synthetic`.

        A *synthetic* field is not present in the source code but is generated by the compiler.

        @returns This `FieldCreator` instance.
        """
    )
    public FieldCreator toSynthetic() {
        this.access |= Opcodes.ACC_SYNTHETIC;
        return this;
    }

    @Info(
        """
        Remove the `synthetic` modifier from this field.

        @returns This `FieldCreator` instance.
        """
    )
    public FieldCreator notSynthetic() {
        this.access &= ~Opcodes.ACC_SYNTHETIC;
        return this;
    }

    @Info(
        """
        Make this field an `enum` constant.

        An *enum* field is a special type of static final field that represents a constant value in an enumeration.

        @returns This `FieldCreator` instance.
        """
    )
    public FieldCreator toEnum() {
        this.access |= Opcodes.ACC_ENUM;
        return this;
    }

    @Info(
        """
        Remove the `enum` modifier from this field.

        @returns This `FieldCreator` instance.
        """
    )
    public FieldCreator notEnum() {
        this.access &= ~Opcodes.ACC_ENUM;
        return this;
    }

    @Info(
        """
        Set the default value for this field.

        The default value is used to initialize the field when an instance of the class is created.

        **Only fields that are *static*, and of primitive types (`char` excluded) type can have a default value.**

        **Note:** For `char` and `String` type fields, use `defaultStringValue()` instead.

        @param value - The default value to set. Use `null` for reference types and appropriate literals for primitive types.
        @returns This `FieldCreator` instance.
        @throws `IllegalArgumentException` if the provided value is not compatible with the field's type.
        """
    )
    public FieldCreator defaultNumericValue(@Nullable Number value) {
        if (value == null) {
            this.defaultValue = null;
            return this;
        };
        this.defaultValue = switch (this.descriptor) {
            case "I" -> Integer.valueOf(value.intValue());
            case "F" -> Float.valueOf(value.floatValue());
            case "J" -> Long.valueOf(value.longValue());
            case "D" -> Double.valueOf(value.doubleValue());
            case "B" -> Integer.valueOf(value.byteValue());
            case "S" -> Integer.valueOf(value.shortValue());
            case "Z" -> Integer.valueOf(value.intValue() == 0 ? 0 : 1);
            default -> throw new IllegalArgumentException("Field type " + this.descriptor + " cannot have a numeric default value");
        };
        return this;
    }

    @Info(
        """
        Set the default value for this field.

        The default value is used to initialize the field when an instance of the class is created.

        **Only fields that are *static*, and of `char` type or `String` type can have a default value.**

        **Note:** For numeric types, use `defaultNumericValue()` instead.

        @param value - The default value to set. Use `null` for reference types and appropriate literals for primitive types.
        @returns This `FieldCreator` instance.
        @throws `IllegalArgumentException` if the provided value is not compatible with the field's type.
        """
    )
    public FieldCreator defaultStringValue(@Nullable String value) {
        if (value == null) {
            this.defaultValue = null;
            return this;
        };
        if (this.descriptor.equals("Ljava/lang/String;")) {
            this.defaultValue = value;
            return this;
        } else if (this.descriptor.equals("C")) {
            if (value.length() != 1) {
                throw new IllegalArgumentException("Field type C (char) cannot have a string default value of length != 1");
            }
            this.defaultValue = Integer.valueOf(value.charAt(0));
            return this;
        }
        return this;
    }

    @Info(
        """
        Finalize the creation of this field and add it to the class.

        This method must be called after configuring the field using other methods like `toPublic()`, `toStatic()`, etc.

        **Note:** After calling this method, you cannot modify the field further.

        @returns This `ClassCreator` instance to allow for method chaining.
        """
    )
    public ClassCreator build() {
        FieldVisitor fv = this.parent.classWriter.visitField(this.access, this.name, this.descriptor, this.signature, this.defaultValue);
        this.annotations.accept(fv);
        fv.visitEnd();
        return this.parent;
    }

}
