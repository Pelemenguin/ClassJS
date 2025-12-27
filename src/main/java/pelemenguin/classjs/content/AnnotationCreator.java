package pelemenguin.classjs.content;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import dev.latvian.mods.kubejs.typings.Info;
import pelemenguin.classjs.util.ClassNameWrapper;
import pelemenguin.classjs.util.DescriptorUtils;

public class AnnotationCreator<P> {

    private P parent;
    private boolean visible = true;
    private String descriptor;
    private Consumer<AnnotationVisitor> toBuild;
    private @Nullable String nameAsChildAnnotation;
    
    public AnnotationCreator(P parent, ClassNameWrapper annotationClass) {
        this.parent = parent;
        this.descriptor = DescriptorUtils.toFieldDescriptor(annotationClass);
        this.toBuild = (av) -> {};
    }

    @Info(
        """
        Marks the annotation as visible at runtime.

        @returns The current `AnnotationCreator` instance.
        """
    )
    public AnnotationCreator<P> visible() {
        this.visible = true;
        return this;
    }

    @Info(
        """
        Marks the annotation as invisible at runtime.

        @returns The current `AnnotationCreator` instance.
        """
    )
    public AnnotationCreator<P> invisible() {
        this.visible = false;
        return this;
    }

    @Info(
        """
        Adds a string value to the annotation.

        @param name - The name of the string value.
        @param value - The string value to add.
        @returns The current `AnnotationCreator` instance.
        """
    )
    public AnnotationCreator<P> withString(String name, String value) {
        this.toBuild = this.toBuild.andThen(av -> av.visit(name, value));
        return this;
    }

    @Info(
        """
        Adds a byte value to the annotation.

        @param name - The name of the byte value.
        @param value - The byte value to add.
        @returns The current `AnnotationCreator` instance.
        """
    )
    public AnnotationCreator<P> withByte(String name, byte value) {
        this.toBuild = this.toBuild.andThen(av -> av.visit(name, value));
        return this;
    }

    @Info(
        """
        Adds a boolean value to the annotation.

        @param name - The name of the boolean value.
        @param value - The boolean value to add.
        @returns The current `AnnotationCreator` instance.
        """
    )
    public AnnotationCreator<P> withBoolean(String name, boolean value) {
        this.toBuild = this.toBuild.andThen(av -> av.visit(name, value));
        return this;
    }

    @Info(
        """
        Adds a char value to the annotation.

        @param name - The name of the char value.
        @param value - The char value to add.
        @returns The current `AnnotationCreator` instance.
        """
    )
    public AnnotationCreator<P> withChar(String name, char value) {
        this.toBuild = this.toBuild.andThen(av -> av.visit(name, value));
        return this;
    }

    @Info(
        """
        Adds a short value to the annotation.

        @param name - The name of the short value.
        @param value - The short value to add.
        @returns The current `AnnotationCreator` instance.
        """
    )
    public AnnotationCreator<P> withShort(String name, short value) {
        this.toBuild = this.toBuild.andThen(av -> av.visit(name, value));
        return this;
    }

    @Info(
        """
        Adds an int value to the annotation.

        @param name - The name of the int value.
        @param value - The int value to add.
        @returns The current `AnnotationCreator` instance.
        """
    )
    public AnnotationCreator<P> withInt(String name, int value) {
        this.toBuild = this.toBuild.andThen(av -> av.visit(name, value));
        return this;
    }

    @Info(
        """
        Adds a long value to the annotation.

        @param name - The name of the long value.
        @param value - The long value to add.
        @returns The current `AnnotationCreator` instance.
        """
    )
    public AnnotationCreator<P> withLong(String name, long value) {
        this.toBuild = this.toBuild.andThen(av -> av.visit(name, value));
        return this;
    }

    @Info(
        """
        Adds a float value to the annotation.

        @param name - The name of the float value.
        @param value - The float value to add.
        @returns The current `AnnotationCreator` instance.
        """
    )
    public AnnotationCreator<P> withFloat(String name, float value) {
        this.toBuild = this.toBuild.andThen(av -> av.visit(name, value));
        return this;
    }

    @Info(
        """
        Adds a double value to the annotation.

        @param name - The name of the double value.
        @param value - The double value to add.
        @returns The current `AnnotationCreator` instance.
        """
    )
    public AnnotationCreator<P> withDouble(String name, double value) {
        this.toBuild = this.toBuild.andThen(av -> av.visit(name, value));
        return this;
    }

    @Info(
        """
        Adds an enum value to the annotation.

        @param name - The name of the enum value.
        @param enumClass - The class of the enum
        @param enumValue - The enum value to add.
        @returns The current `AnnotationCreator` instance.
        """
    )
    public AnnotationCreator<P> withEnum(String name, ClassNameWrapper enumClass, String enumValue) {
        String descriptor = DescriptorUtils.toFieldDescriptor(enumClass);
        this.toBuild = this.toBuild.andThen(av -> av.visitEnum(name, descriptor, enumValue));
        return this;
    }

    @Info(
        """
        Adds an enum value to the annotation.

        @param name - The name of the enum value.
        @param value - The enum value to add.
        @returns The current `AnnotationCreator` instance.
        """
    )
    public <E extends Enum<E>> AnnotationCreator<P> withEnum(String name, E value) {
        String descriptor = DescriptorUtils.toFieldDescriptor(ClassNameWrapper.fromClass(value.getClass()));
        this.toBuild = this.toBuild.andThen(av -> av.visitEnum(name, descriptor, value.name()));
        return this;
    }

    @Info(
        """
        Adds a class value to the annotation.

        @param name - The name of the class value.
        @param className - The class value to add.
        @returns The current `AnnotationCreator` instance.
        """
    )
    public AnnotationCreator<P> withClass(String name, ClassNameWrapper className) {
        String descriptor = DescriptorUtils.toFieldDescriptor(className);
        this.toBuild = this.toBuild.andThen(av -> av.visit(name, Type.getType(descriptor)));
        return this;
    }

    private <E> AnnotationCreator<P> withArray(String name, E[] values) {
        this.toBuild = this.toBuild.andThen(av -> {
            AnnotationVisitor arrayAv = av.visitArray(name);
            for (E value : values) {
                arrayAv.visit(null, value);
            }
            arrayAv.visitEnd();
        });
        return this;
    }

    @Info(
        """
        Adds a byte array as a value to the current annotation.

        @param name - The name of the byte array value.
        @param values - The byte array to add.
        @returns The current `AnnotationCreator` instance.
        """
    )
    public AnnotationCreator<P> withByteArray(String name, byte[] values) {
        Byte[] packed = new Byte[values.length];
        for (int i = 0; i < values.length; i++) {
            packed[i] = values[i];
        }
        return this.withArray(name, packed);
    }

    @Info(
        """
        Adds a boolean array as a value to the current annotation.

        @param name - The name of the boolean array value.
        @param values - The boolean array to add.
        @returns The current `AnnotationCreator` instance.
        """
    )
    public AnnotationCreator<P> withBooleanArray(String name, boolean[] values) {
        Boolean[] packed = new Boolean[values.length];
        for (int i = 0; i < values.length; i++) {
            packed[i] = values[i];
        }
        return this.withArray(name, packed);
    }

    @Info(
        """
        Adds a short array as a value to the current annotation.

        @param name - The name of the short array value.
        @param values - The short array to add.
        @returns The current `AnnotationCreator` instance.
        """
    )
    public AnnotationCreator<P> withShortArray(String name, short[] values) {
        Short[] packed = new Short[values.length];
        for (int i = 0; i < values.length; i++) {
            packed[i] = values[i];
        }
        return this.withArray(name, packed);
    }

    @Info(
        """
        Adds a char array as a value to the current annotation.

        @param name - The name of the char array value.
        @param values - The char array to add.
        @returns The current `AnnotationCreator` instance.
        """
    )
    public AnnotationCreator<P> withCharArray(String name, char[] values) {
        Character[] packed = new Character[values.length];
        for (int i = 0; i < values.length; i++) {
            packed[i] = values[i];
        }
        return this.withArray(name, packed);
    }

    @Info(
        """
        Adds an int array as a value to the current annotation.

        @param name - The name of the int array value.
        @param values - The int array to add.
        @returns The current `AnnotationCreator` instance.
        """
    )
    public AnnotationCreator<P> withIntArray(String name, int[] values) {
        Integer[] packed = new Integer[values.length];
        for (int i = 0; i < values.length; i++) {
            packed[i] = values[i];
        }
        return this.withArray(name, packed);
    }

    @Info(
        """
        Adds a long array as a value to the current annotation.

        @param name - The name of the long array value.
        @param values - The long array to add.
        @returns The current `AnnotationCreator` instance.
        """
    )
    public AnnotationCreator<P> withLongArray(String name, long[] values) {
        Long[] packed = new Long[values.length];
        for (int i = 0; i < values.length; i++) {
            packed[i] = values[i];
        }
        return this.withArray(name, packed);
    }

    @Info(
        """
        Adds a float array as a value to the current annotation.

        @param name - The name of the float array value.
        @param values - The float array to add.
        @returns The current `AnnotationCreator` instance.
        """
    )
    public AnnotationCreator<P> withFloatArray(String name, float[] values) {
        Float[] packed = new Float[values.length];
        for (int i = 0; i < values.length; i++) {
            packed[i] = values[i];
        }
        return this.withArray(name, packed);
    }

    @Info(
        """
        Adds a double array as a value to the current annotation.

        @param name - The name of the double array value.
        @param values - The double array to add.
        @returns The current `AnnotationCreator` instance.
        """
    )
    public AnnotationCreator<P> withDoubleArray(String name, double[] values) {
        Double[] packed = new Double[values.length];
        for (int i = 0; i < values.length; i++) {
            packed[i] = values[i];
        }
        return this.withArray(name, packed);
    }

    @Info(
        """
        Adds an enum array as a value to the current annotation.

        @param name - The name of the enum array value.
        @param enumClass - The class of the enum.
        @param values - The enum array to add.
        @returns The current `AnnotationCreator` instance.
        """
    )
    public AnnotationCreator<P> withEnumArray(String name, ClassNameWrapper enumClass, String[] values) {
        String descriptor = DescriptorUtils.toFieldDescriptor(enumClass);
        this.toBuild = this.toBuild.andThen(av -> {
            AnnotationVisitor arrayAv = av.visitArray(name);
            for (String value : values) {
                arrayAv.visitEnum(null, descriptor, value);
            }
            arrayAv.visitEnd();
        });
        return this;
    }

    @Info(
        """
        Adds an enum array as a value to the current annotation.

        @param name - The name of the enum array value.
        @param values - The enum array to add.
        @returns The current `AnnotationCreator` instance.
        """
    )
    public <E extends Enum<E>> AnnotationCreator<P> withEnumArray(String name, E[] values) {
        if (values.length == 0) {
            return this;
        }
        String descriptor = DescriptorUtils.toFieldDescriptor(ClassNameWrapper.fromClass(values[0].getClass()));
        this.toBuild = this.toBuild.andThen(av -> {
            AnnotationVisitor arrayAv = av.visitArray(name);
            for (E value : values) {
                arrayAv.visitEnum(null, descriptor, value.name());
            }
            arrayAv.visitEnd();
        });
        return this;
    }

    @Info(
        """
        Adds a nested annotation as a value to the current annotation.

        @param name - The name of the nested annotation value.
        @param className - The class of the nested annotation.
        @returns The created nested `AnnotationCreator` instance.
        """
    )
    public AnnotationCreator<AnnotationCreator<P>> withAnnotation(String name, ClassNameWrapper className) {
        AnnotationCreator<AnnotationCreator<P>> nested = new AnnotationCreator<AnnotationCreator<P>>(this, className);
        nested.nameAsChildAnnotation = name;
        return nested;
    }

    @Info(
        """
        Adds an array of nested annotations as a value to the current annotation.

        @param name - The name of the nested annotation array value.
        @param className - The class of the nested annotations.
        @param annotations - The consumers to build each nested annotation. You don't have to call `build()` on them.
        @returns The current `AnnotationCreator` instance.
        """
    )
    public AnnotationCreator<P> withAnnotationArray(String name, ClassNameWrapper className, Consumer<AnnotationCreator<AnnotationCreator<P>>>[] annotations) {
        this.toBuild = this.toBuild.andThen(av -> {
            AnnotationVisitor arrayAv = av.visitArray(name);
            for (Consumer<AnnotationCreator<AnnotationCreator<P>>> annotationConsumer : annotations) {
                AnnotationVisitor nestedAv = arrayAv.visitAnnotation(null, DescriptorUtils.toFieldDescriptor(className));
                AnnotationCreator<AnnotationCreator<P>> nested = new AnnotationCreator<>(this, className);
                annotationConsumer.accept(nested);
                nested.toBuild.accept(nestedAv);
                nestedAv.visitEnd();
            }
            arrayAv.visitEnd();
        });
        return this;
    }

    @Info(
        """
        Finalizes the annotation and returns to the parent creator.

        @returns The parent creator instance.
        """
    )
    public P build() {
        if (this.parent instanceof AnnotationCreator<?> parent) {
            parent.toBuild = parent.toBuild.andThen(av -> {
                AnnotationVisitor annotation = (AnnotationVisitor) av;
                AnnotationVisitor nestedAv = annotation.visitAnnotation(nameAsChildAnnotation, this.descriptor);
                this.toBuild.accept(nestedAv);
                nestedAv.visitEnd();
            });
            return this.parent;
        }

        if (this.parent instanceof MethodCreator parent) {
            parent.annotations = parent.annotations.andThen(mv -> {
                MethodVisitor method = (MethodVisitor) mv;
                AnnotationVisitor av = method.visitAnnotation(this.descriptor, this.visible);
                this.toBuild.accept(av);
                av.visitEnd();
            });
            return this.parent;
        }

        if (this.parent instanceof FieldCreator parent) {
            parent.annotations = parent.annotations.andThen(fv -> {
                FieldVisitor field = (FieldVisitor) fv;
                AnnotationVisitor av = field.visitAnnotation(this.descriptor, this.visible);
                this.toBuild.accept(av);
                av.visitEnd();
            });
            return this.parent;
        }

        if (this.parent instanceof ClassCreator parent) {
            AnnotationVisitor av = parent.classWriter.visitAnnotation(this.descriptor, this.visible);
            this.toBuild.accept(av);
            av.visitEnd();
            return this.parent;
        }

        throw new IllegalStateException("Parent type not supported for AnnotationCreator");
    }

}
