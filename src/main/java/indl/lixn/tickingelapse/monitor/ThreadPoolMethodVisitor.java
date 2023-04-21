package indl.lixn.tickingelapse.monitor;


import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

/**
 * @author listen
 **/
public class ThreadPoolMethodVisitor extends MethodVisitor {

    public ThreadPoolMethodVisitor(MethodVisitor mv) {
        super(Opcodes.ASM5, mv);
    }

    @Override
    public void visitInsn(int opcode) {
        if (opcode == Opcodes.RETURN) {
            // 执行任务完成时的逻辑
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, "java/util/concurrent/ThreadPoolExecutor", "completedTaskCount", "J");
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/concurrent/ThreadPoolExecutor", "getTaskCount", "()J", false);
            mv.visitInsn(Opcodes.LSUB);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/example/ThreadPoolMonitor", "monitor", "(J)V", false);
        }
        mv.visitInsn(opcode);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        if (opcode == Opcodes.INVOKEVIRTUAL && "java/util/concurrent/ThreadPoolExecutor".equals(owner) && "execute".equals(name)) {
            // 执行任务前的逻辑
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/concurrent/ThreadPoolExecutor", "getActiveCount", "()I", false);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "com/example/ThreadPoolMonitor", "monitor", "(I)V", false);
        }
        mv.visitMethodInsn(opcode, owner, name, desc, itf);
    }

}