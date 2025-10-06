package pelemenguin.classjs.util;

import dev.latvian.mods.kubejs.typings.Info;
import pelemenguin.classjs.content.ClassCreator;

public class ClassJSUtils {
    
    @Info(
        """
        Get the name of an already loaded class.

        @param clazz The loaded class. Can be classes loaded from `Java.loadClass(String)` or a custom class created by `ClassCreator`.
        @returns The full qualified name of the class. For example, `java.lang.String` for String class. (Not just `String`)
        """
    )
    public static String getClassName(Class<?> clazz) {
        return clazz.getName();
    }

    @Info(
        """
        Transform a custom class name to the actual full qualified name loaded into JVM.

        Custom classes created by `ClassCreator` are actually loaded into a package named `generated.kubejs.custom`.

        **Note:** We cannot promise that the package name will always be `generated.kubejs.custom` in future versions.
            So use this method to get the actual class name instead of hardcoding the package name.
        
        @param customClassName The custom class name used in `ClassCreator.create(String)`.
        @returns The full qualified name of the class. For example, if the input is `MyClass`, the output will be `generated.kubejs.custom.MyClass`.

        @example
        // "generated.kubejs.custom.TestClass"
        ClassJSUtils.getCustomClassName("TestClass");

        // "generated.kubejs.custom.examplePackage.ExampleClass"
        ClassJSUtils.getCustomClassName("examplePackage.ExampleClass");
        """
    )
    public static String getCustomClassName(String customClassName) {
        return ClassCreator.CUSTOM_PREFIX + "." + customClassName;
    }

}
