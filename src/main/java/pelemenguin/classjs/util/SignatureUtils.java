package pelemenguin.classjs.util;

import java.util.ArrayList;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import dev.latvian.mods.kubejs.typings.Info;

public class SignatureUtils {
    
    private SignatureUtils() {}

    @Info(
        """
        Create a type variable for use in generic class signatures.

        @param identifier The identifier of the type variable. For example, "T" or "E".
        @returns A builder to build the type variable.

        @example
        // "T:Ljava/lang/Number;:Ljava/lang/Runnable;"
        SignatureUtils.typeVariableBuilder("T")
            .extending("java.lang.Number")
            .implementing("java.lang.Runnable")
            .toString();
        """
    )
    public static TypeVarBuilder typeVariableBuilder(String identifier) {
        return new TypeVarBuilder(identifier);
    }

    @Info(
        """
        Create a type signature for use in generic class signatures or method signatures.

        @param className The class name of the type. For example, "java.util.List" or "java.lang.String".
        @returns A builder to build the type signature.

        @example
        // Result signature:     "Ljava/util/Map<TT;+Ljava/util/function/Consumer<-Ljava/util/ArrayList<[TE;>;>;>;"
        // Java representation:  java.util.Map<T, ? extends java.util.function.Consumer<? super java.util.ArrayList<E[]>>>
        SignatureUtils.typeSignatureBuilder("java.util.Map")
            .appendTypeVariable("T")
            .appendAnyExtends("java.util.function.Consumer", (tsb) -> {
                tsb.appendAnySuper("java.util.ArrayList", (tsb2) -> {
                    tsb2.appendArrayTypeVariable("E");
                });
            })
            .toString();
        """
    )
    public static TypeSignatureBuilder typeSignatureBuilder(String className) {
        return new TypeSignatureBuilder(className);
    }

    @Info(
        """
        Create a class signature for use in generic class signatures.

        @returns A builder to build the class signature.

        @example
        // Result signature:     "<T:Ljava/lang/Number;:Ljava/lang/Runnable;>Ljava/lang/Object;"
        // Java representation:  class ExampleClass<T extends java.lang.Number & java.lang.Runnable> extends java.lang.Object
        SignatureUtils.classSignatureBuilder()
            .withTypeParameter("T", (tvb) -> {
                tvb.extending("java.lang.Number");
                tvb.implementing("java.lang.Runnable");
            })
            .toString();
        """
    )
    public static ClassSignatureBuilder classSignatureBuilder() {
        return new ClassSignatureBuilder();
    }

    public static class ClassSignatureBuilder {

        private String superClass = "Ljava/lang/Object;";
        private ArrayList<String> superInterfaces = new ArrayList<>();
        private ArrayList<String> typeVars = new ArrayList<>();

        public ClassSignatureBuilder() {}

        @Info(
            """
            Set the superclass of the class.
            
            @param superClass The full qualified name of the superclass. For example, "java.lang.Object".
            @returns This `ClassSignatureBuilder` instance.
            """
        )
        public ClassSignatureBuilder extending(String superClass) {
            this.superClass = "L" + superClass.replace('.', '/') + ";";
            return this;
        }

        @Info(
            """
            Set the superclass of the class with type parameters.
            
            @param superClass The full qualified name of the superclass. For example, "java.util.List".
            @param typeVariableBuilder A consumer that accepts a `TypeSignatureBuilder` to build the type parameters of the superclass.
            @returns This `ClassSignatureBuilder` instance.
            """
        )
        public ClassSignatureBuilder extending(String superClass, Consumer<TypeSignatureBuilder> typeVariableBuilder) {
            TypeSignatureBuilder tsb = new TypeSignatureBuilder(superClass);
            typeVariableBuilder.accept(tsb);
            this.superClass = tsb.toString();
            return this;
        }

        @Info(
            """
            Add a superinterface to the class.
            
            @param superInterface The full qualified name of the superinterface. For example, "java.io.Serializable".
            @returns This `ClassSignatureBuilder` instance.
            """
        )
        public ClassSignatureBuilder implementing(String superInterface) {
            this.superInterfaces.add("L" + superInterface.replace('.', '/') + ";");
            return this;
        }

        @Info(
            """
            Add a superinterface to the class with type parameters.
            
            @param superInterface The full qualified name of the superinterface. For example, "java.util.List".
            @param typeVariableBuilder A consumer that accepts a `TypeSignatureBuilder` to build the type parameters of the superinterface.
            @returns This `ClassSignatureBuilder` instance.
            """
        )
        public ClassSignatureBuilder implementing(String superInterface, Consumer<TypeSignatureBuilder> typeVariableBuilder) {
            TypeSignatureBuilder tsb = new TypeSignatureBuilder(superInterface);
            typeVariableBuilder.accept(tsb);
            this.superInterfaces.add(tsb.toString());
            return this;
        }

        @Info(
            """
            Add a type parameter to the class.

            The type parameter will have an implicit upper bound of `java.lang.Object`.

            @param identifier The identifier of the type parameter. For example, "T" or "E".
            @returns This `ClassSignatureBuilder` instance.
            """
        )
        public ClassSignatureBuilder withTypeParameter(String identifier) {
            this.typeVars.add(identifier + ":Ljava/lang/Object;");
            return this;
        }

        @Info(
            """
            Add a type parameter to the class with bounds.
            @param identifier The identifier of the type parameter. For example, "T" or "E".
            @param typeVariableBuilder A consumer that accepts a `TypeVarBuilder` to build the bounds of the type parameter.
            @returns This `ClassSignatureBuilder` instance.
            """
        )
        public ClassSignatureBuilder withTypeParameter(String identifier, Consumer<TypeVarBuilder> typeVariableBuilder) {
            TypeVarBuilder tvb = new TypeVarBuilder(identifier);
            typeVariableBuilder.accept(tvb);
            this.typeVars.add(tvb.toString());
            return this;
        }

        @Override
        @Info(
            """
            Build the class signature string.

            @returns The class signature string.
            """
        )
        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (!this.typeVars.isEmpty()) {
                sb.append('<');
                for (String typeVar : this.typeVars) {
                    sb.append(typeVar);
                }
                sb.append('>');
            }
            sb.append(this.superClass);
            if (this.superInterfaces != null) {
                for (String superInterface : this.superInterfaces) {
                    sb.append(superInterface);
                }
            }
            return sb.toString();
        }

    }

    public static class TypeVarBuilder {
        
        private String identifier;
        private @Nullable String superClass;
        private ArrayList<String> superInterfaces = new ArrayList<>();

        public TypeVarBuilder(String identifier) {
            this.identifier = identifier;
        }

        @Info(
            """
            Set the superclass (upper bound) of the type variable.

            @param superClass The full qualified name of the superclass. For example, "java.lang.Number".
            @returns This `TypeVarBuilder` instance.
            """
        )
        public TypeVarBuilder extending(String superClass) {
            this.superClass = "L" + superClass.replace('.', '/') + ";";
            return this;
        }

        @Info(
            """
            Set the superclass (upper bound) of the type variable with type parameters.

            @param superClass The full qualified name of the superclass. For example, "java.util.List".
            @param typeVariableBuilder A consumer that accepts a `TypeSignatureBuilder` to build the type parameters of the superclass.
            @returns This `TypeVarBuilder` instance.
            """
        )
        public TypeVarBuilder extending(String superClass, Consumer<TypeSignatureBuilder> typeVariableBuilder) {
            TypeSignatureBuilder tsb = new TypeSignatureBuilder(superClass);
            typeVariableBuilder.accept(tsb);
            this.superClass = tsb.toString();
            return this;
        }

        @Info(
            """
            Add a superinterface (upper bound) to the type variable.

            @param superInterface The full qualified name of the superinterface. For example, "java.io.Serializable".
            @returns This `TypeVarBuilder` instance.
            """
        )
        public TypeVarBuilder implementing(String superInterface) {
            this.superInterfaces.add("L" + superInterface.replace('.', '/') + ";");
            return this;
        }

        @Info(
            """
            Add a superinterface (upper bound) to the type variable with type parameters.

            @param superInterface The full qualified name of the superinterface. For example, "java.util.List".
            @param typeVariableBuilder A consumer that accepts a `TypeSignatureBuilder` to build the type parameters of the superinterface.
            @returns This `TypeVarBuilder` instance.
            """
        )
        public TypeVarBuilder implementing(String superInterface, Consumer<TypeSignatureBuilder> typeVariableBuilder) {
            TypeSignatureBuilder tsb = new TypeSignatureBuilder(superInterface);
            typeVariableBuilder.accept(tsb);
            this.superInterfaces.add(tsb.toString());
            return this;
        }

        @Override
        @Info(
            """
            Build the type variable string.

            @returns The type variable string.
            """
        )
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.identifier);

            sb.append(':');
            if (this.superClass != null) {
                sb.append(this.superClass);
            } else if (this.superInterfaces.isEmpty()) {
                sb.append("Ljava/lang/Object;");
            }
            
            for (String superInterface : this.superInterfaces) {
                sb.append(':').append(superInterface);
            }

            return sb.toString();
        }

    }

    public static class TypeSignatureBuilder {

        private String className;
        private ArrayList<String> typeParams = new ArrayList<>();
        private int arrayDimension = 0;

        public TypeSignatureBuilder(String className) {
            this.className = className.replace('.', '/');
        }

        @Info(
            """
            Append a type to the type signature.

            @param type The full qualified name of the type. For example, "java.lang.String" or "java.util.List".
            @returns This `TypeSignatureBuilder` instance.
            """
        )
        public TypeSignatureBuilder appendType(String type) {
            this.typeParams.add(DescriptorUtils.toFieldDescriptor(type));
            return this;
        }

        @Info(
            """
            Append a type with type parameters to the type signature.

            @param type The full qualified name of the type. For example, "java.util.List".
            @param addingTypeParameters A consumer that accepts a `TypeSignatureBuilder` to build the type parameters of the type.
            @returns This `TypeSignatureBuilder` instance.
            """
        )
        public TypeSignatureBuilder appendType(String type, Consumer<TypeSignatureBuilder> addingTypeParameters) {
            TypeSignatureBuilder tsb = new TypeSignatureBuilder(type);
            addingTypeParameters.accept(tsb);
            this.typeParams.add(tsb.toString());
            return this;
        }

        @Info(
            """
            Append a type variable to the type signature.

            @param identifier The identifier of the type variable. For example, "T" or "E".
            @returns This `TypeSignatureBuilder` instance.
            """
        )
        public TypeSignatureBuilder appendTypeVariable(String identifier) {
            this.typeParams.add("T" + identifier + ";");
            return this;
        }

        @Info(
            """
            Append an array type variable to the type signature.

            @param identifier The identifier of the type variable. For example, "T" or "E".
            @returns This `TypeSignatureBuilder` instance.
            """
        )
        public TypeSignatureBuilder appendArrayTypeVariable(String identifier) {
            this.typeParams.add("[T" + identifier + ";");
            return this;
        }

        @Info(
            """
            Append an array type variable to the type signature with specified dimension.

            @param identifier The identifier of the type variable. For example, "T" or "E".
            @param dimension The dimension of the array. For example, 2 for a 2D array.
            @returns This `TypeSignatureBuilder` instance.
            """
        )
        public TypeSignatureBuilder appendArrayTypeVariable(String identifier, int dimension) {
            this.typeParams.add("[".repeat(dimension) + "T" + identifier + ";");
            return this;
        }

        @Info(
            """
            Append a wildcard type (unbounded) to the type signature.

            @returns This `TypeSignatureBuilder` instance.
            """
        )
        public TypeSignatureBuilder appendAny() {
            this.typeParams.add("*");
            return this;
        }

        @Info(
            """
            Append a wildcard type with upper bound to the type signature.

            @param className The full qualified name of the upper bound. For example, "java.lang.Number".
            @returns This `TypeSignatureBuilder` instance.
            """
        )
        public TypeSignatureBuilder appendAnyExtends(String className) {
            this.typeParams.add("+L" + className.replace('.', '/') + ";");
            return this;
        }

        @Info(
            """
            Append a wildcard type with upper bound to the type signature with type parameters.

            @param className The full qualified name of the upper bound. For example, "java.util.List".
            @param addingTypeParameters A consumer that accepts a `TypeSignatureBuilder` to build the type parameters of the upper bound.
            @returns This `TypeSignatureBuilder` instance.
            """
        )
        public TypeSignatureBuilder appendAnyExtends(String className, Consumer<TypeSignatureBuilder> addingTypeParameters) {
            TypeSignatureBuilder tsb = new TypeSignatureBuilder(className);
            addingTypeParameters.accept(tsb);
            this.typeParams.add("+" + tsb.toString());
            return this;
        }

        @Info(
            """
            Append a wildcard type with lower bound to the type signature.

            @param className The full qualified name of the lower bound. For example, "java.lang.Number".
            @returns This `TypeSignatureBuilder` instance.
            """
        )
        public TypeSignatureBuilder appendAnySuper(String className) {
            this.typeParams.add("-L" + className.replace('.', '/') + ";");
            return this;
        }

        @Info(
            """
            Append a wildcard type with lower bound to the type signature with type parameters.

            @param className The full qualified name of the lower bound. For example, "java.util.List".
            @param addingTypeParameters A consumer that accepts a `TypeSignatureBuilder` to build the type parameters of the lower bound.
            @returns This `TypeSignatureBuilder` instance.
            """
        )
        public TypeSignatureBuilder appendAnySuper(String className, Consumer<TypeSignatureBuilder> addingTypeParameters) {
            TypeSignatureBuilder tsb = new TypeSignatureBuilder(className);
            addingTypeParameters.accept(tsb);
            this.typeParams.add("-" + tsb.toString());
            return this;
        }

        @Info(
            """
            Convert the type to an array type by increasing the array dimension by 1.

            @returns This `TypeSignatureBuilder` instance.
            """
        )
        public TypeSignatureBuilder toArrayType() {
            this.arrayDimension ++;
            return this;
        }

        @Info(
            """
            Convert the type from an array type by decreasing the array dimension by 1.

            @throws IllegalStateException if the current type is not an array type (i.e., array dimension is 0).
            @returns This `TypeSignatureBuilder` instance.
            """
        )
        public TypeSignatureBuilder fromArrayType() {
            if (this.arrayDimension > 0) {
                this.arrayDimension --;
            } else {
                throw new IllegalStateException(this.toString() + " is not an array type.");
            }
            return this;
        }

        @Override
        @Info(
            """
            Build the type signature string.

            @returns The type signature string.
            """
        )
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < this.arrayDimension; i++) {
                sb.append('[');
            }
            sb.append('L');
            sb.append(this.className);
            if (!this.typeParams.isEmpty()) {
                sb.append('<');
                for (String typeParam : this.typeParams) {
                    sb.append(typeParam);
                }
                sb.append('>');
            }
            sb.append(';');
            return sb.toString();
        }

    }

}
