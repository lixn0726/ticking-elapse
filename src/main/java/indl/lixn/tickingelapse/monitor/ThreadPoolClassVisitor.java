package indl.lixn.tickingelapse.monitor;

import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

/**
 * @author listen
 **/
public class ThreadPoolClassVisitor extends ClassVisitor {

    public ThreadPoolClassVisitor(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        if ("execute".equals(name)) {
            // 使用MethodVisitor修改execute方法的字节码
            mv = new ThreadPoolMethodVisitor(mv);
        }
        return mv;
    }

}
