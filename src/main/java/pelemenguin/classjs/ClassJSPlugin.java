package pelemenguin.classjs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ClassFilter;
import pelemenguin.classjs.content.ClassCreator;
import pelemenguin.classjs.util.ClassJSUtils;
import pelemenguin.classjs.util.DescriptorUtils;
import pelemenguin.classjs.util.SignatureUtils;

public class ClassJSPlugin extends KubeJSPlugin {
    
    @Override
    public void registerBindings(BindingsEvent event) {
        if (event.manager.scriptType.isStartup()) {
            event.add("ClassCreator", ClassCreator.class);
        }
        event.add("DescriptorUtils", DescriptorUtils.class);
        event.add("ClassJSUtils", ClassJSUtils.class);
        event.add("SignatureUtils", SignatureUtils.class);
    }

    @Override
    public void registerClasses(ScriptType type, ClassFilter filter) {

        // Exceptions
        filter.allow("java.lang.Throwable");
        filter.allow("java.lang.Exception");
        filter.allow("java.lang.Error");

    }

}
