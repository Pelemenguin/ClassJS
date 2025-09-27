package pelemenguin.classjs.library;

import java.util.HashMap;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

public class ClassJSClassLoader extends ClassLoader {

    @SuppressWarnings("unused")
    private static final Logger LOADER_LOGGER = LogUtils.getLogger();
    public static final HashMap<String, Class<?>> CREATED_CLASSES = new HashMap<>();

    public Class<?> defineClass(String name, byte[] b) {
        Class<?> result = this.defineClass(name, b, 0, b.length);
        CREATED_CLASSES.put(name, result);
        return result;
    }


}
