package pelemenguin.classjs.util;

import dev.latvian.mods.rhino.util.HideFromJS;

public class ClassNameWrapper {
    
    private String value;

    public static final ClassNameWrapper VOID = new ClassNameWrapper("void");
    public static final ClassNameWrapper INT = new ClassNameWrapper("int");
    public static final ClassNameWrapper LONG = new ClassNameWrapper("long");
    public static final ClassNameWrapper FLOAT = new ClassNameWrapper("float");
    public static final ClassNameWrapper DOUBLE = new ClassNameWrapper("double");
    public static final ClassNameWrapper BOOLEAN = new ClassNameWrapper("boolean");
    public static final ClassNameWrapper CHAR = new ClassNameWrapper("char");
    public static final ClassNameWrapper BYTE = new ClassNameWrapper("byte");
    public static final ClassNameWrapper SHORT = new ClassNameWrapper("short");

    public ClassNameWrapper(String className) {
        this.value = className;
    }

    public static ClassNameWrapper fromClassName(String className) {
        return new ClassNameWrapper(className);
    }

    public static ClassNameWrapper fromClass(Class<?> clazz) {
        return new ClassNameWrapper(clazz.getName());
    }

    public String getClassName() {
        return this.value;
    }

    public String toFieldDescriptor() {
        return DescriptorUtils.toFieldDescriptor(this);
    }

    @HideFromJS
    public static ClassNameWrapper fromJS(Object obj) {
        return obj instanceof Class<?> njc ? fromClass(njc) : fromClassName(obj.toString());
    }

    @Override
    public String toString() {
        return this.getClassName();
    }

}
