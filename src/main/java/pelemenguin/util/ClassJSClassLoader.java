package pelemenguin.util;

import java.util.HashMap;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.rhino.NativeJavaClass;

public class ClassJSClassLoader extends ClassLoader {

    public static final ClassJSClassLoader INSTANCE = new ClassJSClassLoader();

    private static final Logger LOADER_LOGGER = LogUtils.getLogger();
    public static final HashMap<String, NativeJavaClass> CREATED_CLASSES = new HashMap<>();

    public NativeJavaClass defineClass(String name, byte[] b) {
        if (CREATED_CLASSES.containsKey(name)) {
            LOADER_LOGGER.debug("Class '" + name + "'' has been created before, will use previously created one");
            return CREATED_CLASSES.get(name);
        }
        ByteCodeLogUtils.logClassCreated(LOADER_LOGGER, name, b);

        Class<?> defined = this.defineClass(name, b, 0, b.length);
        NativeJavaClass result = new NativeJavaClass(KubeJS.getStartupScriptManager().context, KubeJS.getStartupScriptManager().topLevelScope, defined);
        CREATED_CLASSES.put(name, result);
        return result;
    }

}
