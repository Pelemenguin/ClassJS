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

    @Info(
        """
        Create a method signature for use in generic method signatures.

        @returns A builder to build the method signature.

        @example
        // Result signature:     "<T:Ljava/lang/Number;>(Ljava/util/List<TT;>;)TT;^Ljava/io/IOException;"
        // Java representation:  <T extends java.lang.Number> T exampleMethod(java.util.List<T>) throws java.io.IOException
        SignatureUtils.methodSignatureBuilder()
            .withTypeVariable("T", (tvb) -> {
                tvb.extending("java.lang.Number");
            })
            .addParameter("java.util.List", (tsb) -> {
                tsb.appendTypeVariable("T");
            })
            .setReturnType("T")
            .throwing("java.io.IOException")
            .toString();
        """
    )
    public static MethodSignatureBuilder methodSignatureBuilder() {
        return new MethodSignatureBuilder();
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

    public static class MethodSignatureBuilder {

        private ArrayList<String> typeVars = new ArrayList<>();
        private ArrayList<String> params = new ArrayList<>();
        private String returnType = "V";
        private ArrayList<String> throwing = new ArrayList<>();

        public MethodSignatureBuilder() {}

        @Info(
            """
            Add a type variable to the method.

            The type variable will have an implicit upper bound of `java.lang.Object`.

            @param identifier The identifier of the type variable. For example, "T" or "E".
            @returns This `MethodSignatureBuilder` instance.
            """
        )
        public MethodSignatureBuilder withTypeVariable(String identifier) {
            this.typeVars.add(identifier + ":Ljava/lang/Object;");
            return this;
        }

        @Info(
            """
            Add a type variable to the method with bounds.

            @param identifier The identifier of the type variable. For example, "T" or "E".
            @param typeVariableBuilder A consumer that accepts a `TypeVarBuilder` to build the bounds of the type variable.
            @returns This `MethodSignatureBuilder` instance.
            """
        )
        public MethodSignatureBuilder withTypeVariable(String identifier, Consumer<TypeVarBuilder> typeVariableBuilder) {
            TypeVarBuilder tvb = new TypeVarBuilder(identifier);
            typeVariableBuilder.accept(tvb);
            this.typeVars.add(tvb.toString());
            return this;
        }

        @Info(
            """
            Add a parameter to the method.

            @param parameterType The full qualified name of the parameter type. For example, "java.lang.String".
            @returns This `MethodSignatureBuilder` instance.
            """
        )
        public MethodSignatureBuilder addParameter(String parameterType) {
            this.params.add(DescriptorUtils.toFieldDescriptor(parameterType));
            return this;
        }

        @Info(
            """
            Add a parameter to the method with type parameters.

            @param rawType The raw type of the parameter. For example, "java.util.List".
            @param typeSignatureBuilder A consumer that accepts a `TypeSignatureBuilder` to build the type parameters of the parameter.
            @returns This `MethodSignatureBuilder` instance.
            """
        )
        public MethodSignatureBuilder addParameter(String rawType, Consumer<TypeSignatureBuilder> typeSignatureBuilder) {
            TypeSignatureBuilder tsb = new TypeSignatureBuilder(rawType);
            typeSignatureBuilder.accept(tsb);
            this.params.add(tsb.toString());
            return this;
        }

        @Info(
            """
            Add a parameter to the method as a type variable.

            @param identifier The identifier of the type variable. For example, "T" or "E".
            @returns This `MethodSignatureBuilder` instance.
            """
        )
        public MethodSignatureBuilder addTypeVariableParameter(String identifier) {
            this.params.add("T" + identifier + ";");
            return this;
        }

        @Info(
            """
            Add a parameter to the method as an array of type variable.

            @param identifier The identifier of the type variable. For example, "T" or "E".
            @returns This `MethodSignatureBuilder` instance.
            """
        )
        public MethodSignatureBuilder addTypeVariableArrayParameter(String identifier) {
            this.params.add("[T" + identifier + ";");
            return this;
        }

        @Info(
            """
            Add a parameter to the method as an array of type variable with specified dimension.

            @param identifier The identifier of the type variable. For example, "T" or "E".
            @param dimension The dimension of the array. For example, 2 for a 2D array.
            @returns This `MethodSignatureBuilder` instance.
            """
        )
        public MethodSignatureBuilder addTypeVariableArrayParameter(String identifier, int dimension) {
            this.params.add("[".repeat(dimension) + "T" + identifier + ";");
            return this;
        }

        @Info(
            """
            Set the return type of the method.

            @param returnType The full qualified name of the return type. For example, "java.lang.String".
            @returns This `MethodSignatureBuilder` instance.
            """
        )
        public MethodSignatureBuilder setReturnType(String returnType) {
            this.returnType = DescriptorUtils.toFieldDescriptor(returnType);
            return this;
        }

        @Info(
            """
            Set the return type of the method with type parameters.

            @param rawType The raw type of the return type. For example, "java.util.List".
            @param typeSignatureBuilder A consumer that accepts a `TypeSignatureBuilder` to build the type parameters of the return type.
            @returns This `MethodSignatureBuilder` instance.
            """
        )
        public MethodSignatureBuilder setReturnType(String rawType, Consumer<TypeSignatureBuilder> typeSignatureBuilder) {
            TypeSignatureBuilder tsb = new TypeSignatureBuilder(rawType);
            typeSignatureBuilder.accept(tsb);
            this.returnType = tsb.toString();
            return this;
        }

        @Info(
            """
            Set the return type of the method as a type variable.

            @param identifier The identifier of the type variable. For example, "T" or "E".
            @returns This `MethodSignatureBuilder` instance.
            """
        )
        public MethodSignatureBuilder setTypeVariableReturn(String identifier) {
            this.returnType = "T" + identifier + ";";
            return this;
        }

        @Info(
            """
            Set the return type of the method as an array of type variable.

            @param identifier The identifier of the type variable. For example, "T" or "E".
            @returns This `MethodSignatureBuilder` instance.
            """
        )
        public MethodSignatureBuilder setTypeVariableArrayReturn(String identifier) {
            this.returnType = "[T" + identifier + ";";
            return this;
        }

        @Info(
            """
            Set the return type of the method as an array of type variable with specified dimension.

            @param identifier The identifier of the type variable. For example, "T" or "E".
            @param dimension The dimension of the array. For example, 2 for a 2D array.
            @returns This `MethodSignatureBuilder` instance.
            """
        )
        public MethodSignatureBuilder setTypeVariableArrayReturn(String identifier, int dimension) {
            this.returnType = "[".repeat(dimension) + "T" + identifier + ";";
            return this;
        }

        @Info(
            """
            Add a thrown exception to the method.

            @param exceptionType The full qualified name of the exception type. For example, "java.io.IOException".
            @returns This `MethodSignatureBuilder` instance.
            """
        )
        public MethodSignatureBuilder throwing(String exceptionType) {
            this.throwing.add("L" + exceptionType.replace('.', '/') + ";");
            return this;
        }

        @Info(
            """
            Add a thrown exception to the method with type parameters.

            @param rawType The raw type of the exception. For example, "java.util.List".
            @param typeSignatureBuilder A consumer that accepts a `TypeSignatureBuilder` to build the type parameters of the exception.
            @returns This `MethodSignatureBuilder` instance.
            """
        )
        public MethodSignatureBuilder throwing(String rawType, Consumer<TypeSignatureBuilder> typeSignatureBuilder) {
            TypeSignatureBuilder tsb = new TypeSignatureBuilder(rawType);
            typeSignatureBuilder.accept(tsb);
            this.throwing.add(tsb.toString());
            return this;
        }

        @Override
        @Info(
            """
            Build the method signature string.

            @returns The method signature string.
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
            sb.append('(');
            for (String param : this.params) {
                sb.append(param);
            }
            sb.append(')');
            sb.append(this.returnType);
            for (String ex : this.throwing) {
                sb.append('^').append(ex);
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
