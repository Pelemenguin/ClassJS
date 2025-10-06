package pelemenguin.classjs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import pelemenguin.classjs.content.ClassCreator;
import pelemenguin.classjs.util.ClassJSUtils;
import pelemenguin.classjs.util.DescriptorUtils;

public class ClassJSPlugin extends KubeJSPlugin {
    
    @Override
    public void registerBindings(BindingsEvent event) {
        if (event.manager.scriptType.isStartup()) {
            event.add("ClassCreator", ClassCreator.class);
            event.add("DescriptorUtils", DescriptorUtils.class);
            event.add("ClassJSUtils", ClassJSUtils.class);
        }
    }

}
