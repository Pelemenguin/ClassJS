package pelemenguin.classjs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import pelemenguin.classjs.content.ClassCreator;
import pelemenguin.util.DescriptorUtils;

public class ClassJSPlugin extends KubeJSPlugin {
    
    @Override
    public void registerBindings(BindingsEvent event) {
        if (event.manager.scriptType.isStartup()) {
            event.add("ClassCreator", ClassCreator.class);
            event.add("FieldDescriptor", DescriptorUtils.FieldDescriptor.class);
            event.add("MethodDescriptor", DescriptorUtils.MethodDescriptor.class);
        }
    }

}
