package pelemenguin.util;

import java.util.ArrayList;

import dev.latvian.mods.kubejs.typings.Info;

public class DescriptorUtils {
    
    private DescriptorUtils() {}

    @Info(
        """
        Convert a field type to its descriptor.

        According to [Java Virtual Machine Specification](https://docs.oracle.com/javase/specs/jvms/se17/html/jvms-4.html#jvms-4.3.2),
        field descriptor is described below:

        |Type        |Descriptor                                                |
        |------------|----------------------------------------------------------|
        |`byte`      |`B`                                                       |
        |`char`      |`C`                                                       |
        |`double`    |`D`                                                       |
        |`float`     |`F`                                                       |
        |`int`       |`I`                                                       |
        |`long`      |`J`                                                       |
        |`short`     |`S`                                                       |
        |`void`      |`V`                                                       |
        |`boolean`   |`Z`                                                       |
        |*reference* |`L` + *className* + `;` (*className* is seperated by `/`) |
        |*array*     |`[` + *elementDescriptor*                                 |

        **Note:** `V` (which represents `void`) is not actually a valid field desciptor,
            but is used as a return type in method descriptors.
            However, you can still use `DescriptorUtils.toFieldDescriptor("void")` to get `"V"`.

        @param fieldType - The field type.
        @returns The result descriptor.

        @example
        // "I"
        console.info(DescriptorUtils.toFieldDescriptor("int"));

        // "F"
        console.info(DescriptorUtils.toFieldDescriptor("float"));

        // "Ljava/lang/String;"
        console.info(DescriptorUtils.toFieldDescriptor("java.lang.String"));

        // "[Ljava/lang/Integer;"
        console.info(DescriptorUtils.toFieldDescriptor("java.lang.Integer[]"));
        """
    )
    public static String toFieldDescriptor(String fieldType) {
        return switch (fieldType) {
            case "byte" -> "B";
            case "char" -> "C";
            case "double" -> "D";
            case "float" -> "F";
            case "int" -> "I";
            case "long" -> "J";
            case "short" -> "S";
            case "boolean" -> "Z";
            case "void" -> "V";
            default -> {
                if (fieldType.endsWith("[]")) {
                    String elementType = fieldType.substring(0, fieldType.length() - 2);
                    yield "[" + toFieldDescriptor(elementType);
                } else if (fieldType.contains(".")) {
                    yield "L" + fieldType.replace('.', '/') + ";";
                } else {
                    yield "L" + fieldType + ";";
                }
            }
        };
    }

    @Info(
        """
        Convert parameter types and a return type to a method descriptor.

        According to [Java Virtual Machine Specification](https://docs.oracle.com/javase/specs/jvms/se17/html/jvms-4.html#jvms-4.3.3),
        method descriptor is described as `(` + *param1* + *param2* + ... + `)` + *return*,
        where *param1*, *param2*, ..., *return* are field descriptors.

        @param paramTypes - An array of parameter types.
        @param returnType - The return type.
        @returns The result descriptor.

        @example
        // "()V"
        console.info(DescriptorUtils.toMethodDescriptor([], "void"));

        // "(I[Ljava/lang/Object;)F"
        console.info(DescriptorUtils.toMethodDescriptor(["int", "java.lang.Object[]"], "float"));

        // "([Ljava/lang/String;)Ljava/lang/String;"
        console.info(DescriptorUtils.toMethodDescriptor(["java.lang.String[]"], "java.lang.String"));
        """
    )
    public static String toMethodDescriptor(String[] paramTypes, String returnType) {
        StringBuilder descriptor = new StringBuilder();
        descriptor.append('(');
        for (String paramType : paramTypes) {
            descriptor.append(toFieldDescriptor(paramType));
        }
        descriptor.append(')');
        descriptor.append(toFieldDescriptor(returnType));
        return descriptor.toString();
    }

    @Info(
        """
        Get the field type from a field descriptor.

        See {@linkcode toFieldDescriptor} for more information.

        @param descriptor - A field descriptor.
        @returns The result field type.

        @example
        // "int"
        console.info(DescriptorUtils.fromFieldDescriptor("I"));

        // "float"
        console.info(DescriptorUtils.fromFieldDescriptor("F"));

        // "java.lang.String"
        console.info(DescriptorUtils.fromFieldDescriptor("Ljava/lang/String;"));

        // "java.lang.Integer[]"
        console.info(DescriptorUtils.fromFieldDescriptor("[Ljava/lang/Integer;"));
        """
    )
    public static String fromFieldDescriptor(String descriptor) {
        if (descriptor.startsWith("L") && descriptor.endsWith(";")) {
            return descriptor.substring(1, descriptor.length() - 1).replace('/', '.');
        } else if (descriptor.startsWith("[")) {
            return fromFieldDescriptor(descriptor.substring(1)) + "[]";
        } else {
            return switch (descriptor) {
                case "B" -> "byte";
                case "C" -> "char";
                case "D" -> "double";
                case "F" -> "float";
                case "I" -> "int";
                case "J" -> "long";
                case "S" -> "short";
                case "Z" -> "boolean";
                case "V" -> "void";
                default -> descriptor; // Unknown type, return as is
            };
        }
    }

    @Info(
        """
        Get the parameter types and the return type from a method descriptor.

        See {@linkcode toMethodDescriptor} for more information.
        
        **Note:** The last element of the array is the return type.

        @param descriptor - A method descriptor.

        @example
        // ["void"]
        console.info(DescriptorUtils.fromMethodDescriptor("()V"));

        // ["int", "java.lang.Object[]", "float"]
        console.info(DescriptorUtils.fromMethodDescriptor("(I[Ljava/lang/Object;)F"));

        // ["java.lang.String[]", "java.lang.String"]
        console.info(DescriptorUtils.fromMethodDescriptor("([Ljava/lang/String;)Ljava/lang/String;"));
        """
    )
    public static String[] fromMethodDescriptor(String descriptor) {
        if (!descriptor.startsWith("(")) {
            throw new IllegalArgumentException("Invalid method descriptor: " + descriptor);
        }

        int endParamsIndex = descriptor.indexOf(')');
        if (endParamsIndex == -1) {
            throw new IllegalArgumentException("Invalid method descriptor: " + descriptor);
        }

        String paramsPart = descriptor.substring(1, endParamsIndex);
        String returnPart = descriptor.substring(endParamsIndex + 1);

        // Parse parameter types
        ArrayList<String> paramTypes = new ArrayList<>();
        for (int i = 0; i < paramsPart.length(); ) {
            char c = paramsPart.charAt(i);
            int arrayDepth = 0;
            while (c == '[') {
                arrayDepth++;
                i++;
                c = paramsPart.charAt(i);
            }

            String type;
            if (c == 'L') {
                int semicolonIndex = paramsPart.indexOf(';', i);
                if (semicolonIndex == -1) {
                    throw new IllegalArgumentException("Invalid method descriptor: " + descriptor);
                }
                type = paramsPart.substring(i, semicolonIndex + 1);
                i = semicolonIndex + 1;
            } else {
                type = String.valueOf(c);
                i++;
            }

            String fieldType = fromFieldDescriptor(type);
            for (int j = 0; j < arrayDepth; j++) {
                fieldType += "[]";
            }
            paramTypes.add(fieldType);
        }

        // Parse return type
        String returnType = fromFieldDescriptor(returnPart);

        // Combine parameter types and return type into a single array
        String[] result = new String[paramTypes.size() + 1];
        for (int i = 0; i < paramTypes.size(); i++) {
            result[i] = paramTypes.get(i);
        }
        result[paramTypes.size()] = returnType;

        return result;
    }

}
