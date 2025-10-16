package pelemenguin.classjs.probe;

import com.probejs.specials.assign.ClassAssignmentManager;

import dev.latvian.mods.kubejs.typings.desc.TypeDescJS;
import pelemenguin.classjs.util.ClassNameWrapper;

public class ClassJSClassAssignments {
    
    private ClassJSClassAssignments() {}

    // private static DescriptionContext context = ComponentConverter.PROBEJS_CONTEXT;

    static void load() {
        ClassAssignmentManager.ASSIGNMENTS.put(ClassNameWrapper.class, TypeDescJS.STRING);
    }

}
