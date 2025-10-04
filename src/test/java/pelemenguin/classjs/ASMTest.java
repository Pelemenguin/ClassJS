package pelemenguin.classjs;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ASMTest {
    
    public static void main(String[] args) throws IllegalAccessException, NoSuchMethodException, SecurityException,
        IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        cw.visit(Opcodes.V17, Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER, "pelemenguin/classjs/TestClass", null, "java/lang/Object", null);

        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "test", "(I)I", null, null);

        mv.visitCode();

        // .pushInt(0)
        // .gotoLabel("return")
        // .labelNext("return")
        // .returnInt()
        // .build()

        mv.visitInsn(Opcodes.ICONST_0);
        
        Label label = new Label();
        mv.visitJumpInsn(Opcodes.GOTO, label);

        mv.visitLabel(label);
        mv.visitInsn(Opcodes.IRETURN);

        mv.visitMaxs(0, 0);
        mv.visitEnd();

        byte[] b = cw.toByteArray();

        Class<?> c = MethodHandles.lookup().defineClass(b);
        System.out.println((Arrays.toString(c.getDeclaredMethods())));

        System.out.println((c.getDeclaredMethod("test", int.class).invoke(null, 1)));
    }

}
