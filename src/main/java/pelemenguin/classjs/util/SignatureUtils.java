package pelemenguin.classjs.util;

import java.util.ArrayList;
import java.util.function.Consumer;

import javax.annotation.Nullable;

public class SignatureUtils {
    
    private SignatureUtils() {}

    public static TypeVarBuilder typeVariableBuilder(String identifier) {
        return new TypeVarBuilder(identifier);
    }

    public static TypeSignatureBuilder typeSignatureBuilder(String className) {
        return new TypeSignatureBuilder(className);
    }

    static class TypeVarBuilder {
        
        private String identifier;
        private @Nullable String superClass;
        private ArrayList<String> superInterfaces = new ArrayList<>();

        public TypeVarBuilder(String identifier) {
            this.identifier = identifier;
        }

        public TypeVarBuilder extending(String superClass) {
            this.superClass = "L" + superClass.replace('.', '/') + ";";
            return this;
        }

        public TypeVarBuilder extending(String superClass, Consumer<TypeSignatureBuilder> typeVariableBuilder) {
            TypeSignatureBuilder tsb = new TypeSignatureBuilder(superClass);
            typeVariableBuilder.accept(tsb);
            this.superClass = tsb.toString();
            return this;
        }

        public TypeVarBuilder implementing(String superInterface) {
            this.superInterfaces.add("L" + superInterface.replace('.', '/') + ";");
            return this;
        }

        public TypeVarBuilder implementing(String superInterface, Consumer<TypeSignatureBuilder> typeVariableBuilder) {
            TypeSignatureBuilder tsb = new TypeSignatureBuilder(superInterface);
            typeVariableBuilder.accept(tsb);
            this.superInterfaces.add(tsb.toString());
            return this;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.identifier);

            sb.append(':');
            if (this.superClass != null) {
                sb.append(this.superClass);
            }
            
            for (String superInterface : this.superInterfaces) {
                sb.append(':').append(superInterface);
            }

            return sb.toString();
        }

    }

    static class TypeSignatureBuilder {

        private String className;
        private ArrayList<String> typeParams = new ArrayList<>();
        private int arrayDimension = 0;

        public TypeSignatureBuilder(String className) {
            this.className = className.replace('.', '/');
        }

        public TypeSignatureBuilder appendType(String type) {
            this.typeParams.add(DescriptorUtils.toFieldDescriptor(type));
            return this;
        }

        public TypeSignatureBuilder appendType(String type, Consumer<TypeSignatureBuilder> addingTypeParameters) {
            TypeSignatureBuilder tsb = new TypeSignatureBuilder(type);
            addingTypeParameters.accept(tsb);
            this.typeParams.add(tsb.toString());
            return this;
        }

        public TypeSignatureBuilder appendTypeVariable(String identifier) {
            this.typeParams.add("T" + identifier + ";");
            return this;
        }

        public TypeSignatureBuilder appendArrayTypeVariable(String identifier) {
            this.typeParams.add("[T" + identifier + ";");
            return this;
        }

        public TypeSignatureBuilder appendArrayTypeVariable(String identifier, int dimension) {
            this.typeParams.add("[".repeat(dimension) + "T" + identifier + ";");
            return this;
        }

        public TypeSignatureBuilder appendAny() {
            this.typeParams.add("*");
            return this;
        }

        public TypeSignatureBuilder appendAnyExtends(String className) {
            this.typeParams.add("+L" + className.replace('.', '/') + ";");
            return this;
        }

        public TypeSignatureBuilder appendAnyExtends(String className, Consumer<TypeSignatureBuilder> addingTypeParameters) {
            TypeSignatureBuilder tsb = new TypeSignatureBuilder(className);
            addingTypeParameters.accept(tsb);
            this.typeParams.add("+" + tsb.toString());
            return this;
        }

        public TypeSignatureBuilder appendAnySuper(String className) {
            this.typeParams.add("-L" + className.replace('.', '/') + ";");
            return this;
        }

        public TypeSignatureBuilder appendAnySuper(String className, Consumer<TypeSignatureBuilder> addingTypeParameters) {
            TypeSignatureBuilder tsb = new TypeSignatureBuilder(className);
            addingTypeParameters.accept(tsb);
            this.typeParams.add("-" + tsb.toString());
            return this;
        }

        public TypeSignatureBuilder toArrayType() {
            this.arrayDimension ++;
            return this;
        }

        public TypeSignatureBuilder fromArrayType() {
            if (this.arrayDimension > 0) {
                this.arrayDimension --;
            } else {
                throw new IllegalStateException(this.toString() + " is not an array type.");
            }
            return this;
        }

        @Override
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
