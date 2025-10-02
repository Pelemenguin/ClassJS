package pelemenguin.classjs.content;

import org.objectweb.asm.ClassWriter;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import pelemenguin.classjs.library.ClassJSClassLoader;

@Info("A class for Java classes creation.")
public class ClassCreator {

    private static final String CUSTOM_PREFIX = "generated.kubejs.custom";
    private static final String CUSTOM_PREFIX_SLASH = CUSTOM_PREFIX.replace(".", "/") + "/";

    private String name;
    private ClassWriter classWriter;

    public ClassCreator(String name) {
        if (ClassJSClassLoader.CREATED_CLASSES.containsKey(this.name)) {
            ConsoleJS.STARTUP.warn(
                "The class " + name + " has been created before. It is normal to see this warning during reloading. If not, check your code and see if you have some classes created more than one time."
            );
        }
        this.name = name;
        this.classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
    }

    public String getName() {
        return CUSTOM_PREFIX + "." + this.name;
    }

    public Object defineClass() {
        // TODO: defineClass
        throw new UnsupportedOperationException("Not implemented");
    }

}
