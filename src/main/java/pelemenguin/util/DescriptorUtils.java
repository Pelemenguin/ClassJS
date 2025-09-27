package pelemenguin.util;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.NativeJavaClass;

public class DescriptorUtils {
    
    private DescriptorUtils() {}

    @Info(
        "A util class to create field descriptors."
    )
    public static class FieldDescriptor {

        private FieldDescriptor() {}

        @Info(
            "Get the field descriptor from a class loaded by `Java.loadClass`.\n" +
            "\n" +
            "@param clazz - The loaded Java class.\n" +
            "@returns The field descriptor of the class.\n" +
            "\n" +
            "@example\n" +
            "DescriptorUtils.Field.fromClass(Java.loadClass(\"java.lang.Object\"))       // -> Ljava/lang/Object (In Java: Object)" +
            "DescriptorUtils.Field.fromClass(Java.loadClass(\"java.lang.Integer\").TYPE) // -> I                 (In Java: int)" +
            "DescriptorUtils.Field.fromClass(Java.loadClass(\"[B\"))                     // -> [B                (In Java: byte[])"
        )
        public static String fromClass(NativeJavaClass clazz) {
            return fromJavaClass(clazz.getClassObject());
        }

        private static String fromJavaClass(Class<?> clazz) {
            if (clazz.isArray()) {
                return clazz.getName().replace('.', '/');
            } else if (clazz.isPrimitive()) {
                if (clazz == void.class) {
                    return "V";
                } else if (clazz == int.class) {
                    return "I";
                } else if (clazz == boolean.class) {
                    return "Z";
                } else if (clazz == byte.class) {
                    return "B";
                } else if (clazz == char.class) {
                    return "C";
                } else if (clazz == short.class) {
                    return "S";
                } else if (clazz == long.class) {
                    return "J";
                } else if (clazz == float.class) {
                    return "F";
                } else if (clazz == double.class) {
                    return "D";
                }
            }
            return "L" + clazz.getName().replace('.', '/') + ";";
        }

        @Info(
            "Get the field descriptor from a class name.\n" +
            "\n" +
            "@param className - The class's **full qualified name**. (For example, use \"java.lang.Object\" instead of \"Object\")\n" +
            "@returns The field descriptor of the class.\n" +
            "\n" +
            "@example\n" +
            "DescriptorUtils.Field.fromClass(\"java.lang.Object\") // -> Ljava/lang/Object" +
            "DescriptorUtils.Field.fromClass(\"int\") // -> I" +
            "DescriptorUtils.Field.fromClass(\"byte[]\") // -> [B"
        )
        public static String fromClassName(String className) {
            if (className.endsWith("[]")) {
                return "[" + fromClassName(className.substring(0, className.length() - 2));
            } else if (className.equals("void")) {
                return "V";
            } else if (className.equals("int")) {
                return "I";
            } else if (className.equals("boolean")) {
                return "Z";
            } else if (className.equals("byte")) {
                return "B";
            } else if (className.equals("char")) {
                return "C";
            } else if (className.equals("short")) {
                return "S";
            } else if (className.equals("long")) {
                return "J";
            } else if (className.equals("float")) {
                return "F";
            } else if (className.equals("double")) {
                return "D";
            }
            return "L" + className.replace('.', '/') + ";";
        }

    }

    public static class MethodDescriptor {

        private MethodDescriptor() {}

        @Info(
            "Get the method descriptor from parameter types and return type loaded by `Java.loadClass`.\n" +
            "\n" +
            "@param paramTypes - An array of loaded Java classes representing the parameter types.\n" +
            "@param returnType - The loaded Java class representing the return type.\n" +
            "@returns The method descriptor.\n" +
            "\n" +
            "@example\n" +
            "DescriptorUtils.Method.fromClasses([Java.loadClass(\"java.lang.String\"), Java.loadClass(\"java.lang.Integer\").TYPE], Java.loadClass(\"java.lang.Void\").TYPE) // -> (Ljava/lang/String;I)V (In Java: void methodName(String arg0, int arg1))"
        )
        public static String fromClasses(NativeJavaClass[] paramTypes, NativeJavaClass returnType) {
            StringBuilder sb = new StringBuilder();
            sb.append('(');
            for (NativeJavaClass paramType : paramTypes) {
                sb.append(FieldDescriptor.fromClass(paramType));
            }
            sb.append(')');
            sb.append(FieldDescriptor.fromClass(returnType));
            return sb.toString();
        }

        @Info(
            "Get the method descriptor from parameter types and return type class names.\n" +
            "\n" +
            "@param paramTypes - An array of class names representing the parameter types. (For example, use \"java.lang.String\" instead of \"String\")\n" +
            "@param returnType - The class name representing the return type.\n" +
            "@returns The method descriptor.\n" +
            "\n" +
            "@example\n" +
            "DescriptorUtils.Method.fromClassNames([\"java.lang.String\", \"int\"], \"void\") // -> (Ljava/lang/String;I)V (In Java: void methodName(String arg0, int arg1))"
        )
        public static String fromClassNames(String[] paramTypes, String returnType) {
            StringBuilder sb = new StringBuilder();
            sb.append('(');
            for (String paramType : paramTypes) {
                sb.append(FieldDescriptor.fromClassName(paramType));
            }
            sb.append(')');
            sb.append(FieldDescriptor.fromClassName(returnType));
            return sb.toString();
        }

    }

}
