package pelemenguin.classjs.util;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.MutableCallSite;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nullable;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Opcodes;

import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.Function;
import dev.latvian.mods.rhino.Scriptable;
import dev.latvian.mods.rhino.util.HideFromJS;
import pelemenguin.classjs.ClassJS;

public class InvokeDynamicHelper {

    public static record FunctionStatus(Function function, CallSite callSite, String descriptor) {}

    private final static ConcurrentHashMap<String, FunctionStatus> REGISTERED_FUNCTIONS = new ConcurrentHashMap<>();

    public static final Handle HANDLE = new Handle(
        Opcodes.H_INVOKESTATIC,
        InvokeDynamicHelper.class.getName().replace(".", "/"),
        "callJSFunction",
        "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;)Ljava/lang/invoke/CallSite;",
        false
    );

    @HideFromJS
    public static CallSite callJSFunction(MethodHandles.Lookup lookup, String methodName, MethodType methodType, String funcId) throws NoSuchMethodException, IllegalAccessException {
        FunctionStatus status = REGISTERED_FUNCTIONS.get(funcId);
        if (status == null) {
            throw new IllegalArgumentException("Function id '%s' not found. All registered: %s. Current Class Loader: %s".formatted(funcId, REGISTERED_FUNCTIONS.keySet().toString(), InvokeDynamicHelper.class.getClassLoader()));
        }

        if (status.callSite != null) return status.callSite;

        Function jsFunction = status.function;

        CallSite result = new MutableCallSite(methodType);
        result.setTarget(createMethodHandle(jsFunction, lookup, methodType));

        REGISTERED_FUNCTIONS.put(funcId, new FunctionStatus(jsFunction, result, methodType.toMethodDescriptorString()));

        return result;
    }

    private static MethodHandle createMethodHandle(Function jsFunction, MethodHandles.Lookup lookup, MethodType methodType) throws NoSuchMethodException, IllegalAccessException {
        if (methodType.returnType() == void.class) {
            MethodHandle handle = lookup.findStatic(
                InvokeDynamicHelper.class,
                "callJSNoReturn",
                MethodType.methodType(void.class, Function.class, Object[].class)
            );
            handle = MethodHandles.insertArguments(handle, 0, jsFunction);
            handle = methodType.parameterCount() == 0
                ? MethodHandles.insertArguments(handle, 0, new Object[0])
                : handle.asCollector(Object[].class, methodType.parameterCount());
            handle = handle.asType(methodType);
            return handle;
        }

        MethodHandle handle = lookup.findStatic(
            InvokeDynamicHelper.class,
            "callJS",
            MethodType.methodType(Object.class, Function.class, Class.class, Object[].class)
        );

        handle = MethodHandles.insertArguments(handle, 0, jsFunction, methodType.returnType());
        handle = methodType.parameterCount() == 0
            ? MethodHandles.insertArguments(handle, 0, new Object[0])
            : handle.asCollector(Object[].class, methodType.parameterCount());
        handle = handle.asType(methodType);

        return handle;
    }

    @HideFromJS
    public static Object callJS(Function f, Class<?> returnType, Object[] args) {
        Context context = KubeJS.getStartupScriptManager().context;
        Scriptable scope = f.getParentScope();

        Object[] jsObjects = new Object[args.length];
        for (int i = 0; i < jsObjects.length; i ++) {
            jsObjects[i] = Context.javaToJS(context, args[i], scope);
        }

        Object result = f.call(context, scope, null, args);
        return Context.jsToJava(context, result, returnType);
    }

    @HideFromJS
    public static void callJSNoReturn(Function f, Object[] args) {
        Context context = KubeJS.getStartupScriptManager().context;
        Scriptable scope = f.getParentScope();

        Object[] jsObjects = new Object[args.length];
        for (int i = 0; i < jsObjects.length; i ++) {
            jsObjects[i] = Context.javaToJS(context, args[i], scope);
        }

        f.call(context, scope, null, args);
    }

    @HideFromJS
    public static void registerFunction(String id, Function func, @Nullable String descriptor, @Nullable MethodHandles.Lookup lookup) throws NoSuchMethodException, IllegalAccessException {
        if (REGISTERED_FUNCTIONS.containsKey(id)) {
            FunctionStatus status = REGISTERED_FUNCTIONS.get(id);

            // If method is not called before, the `CallSite` is not generated.
            if (status.callSite == null) {
                REGISTERED_FUNCTIONS.put(id, new FunctionStatus(func, null, status.descriptor));
                return;
            }

            if (lookup == null) throw new IllegalArgumentException("lookup cannot be null when updating functions");
            ClassJS.LOGGER.debug("Reload previous function: " + id);
            MethodHandle created = createMethodHandle(func, lookup, MethodType.fromMethodDescriptorString(status.descriptor, ClassJSClassLoader.INSTANCE));

            status.callSite.setTarget(created);
            REGISTERED_FUNCTIONS.put(id, new FunctionStatus(func, status.callSite, status.descriptor));
            return;
        }
        if (descriptor == null) throw new IllegalArgumentException("type cannot be null when initializing");
        REGISTERED_FUNCTIONS.put(id, new FunctionStatus(func, null, descriptor));
        ClassJS.LOGGER.debug("New function registered: " + id);
        ClassJS.LOGGER.debug("All registered functions: " + REGISTERED_FUNCTIONS.keySet());
    }

}
