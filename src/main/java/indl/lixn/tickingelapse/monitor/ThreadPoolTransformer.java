package indl.lixn.tickingelapse.monitor;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @author listen
 **/
public class ThreadPoolTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        // 判断是否是线程池类
        if (className.startsWith("java/util/concurrent/ThreadPoolExecutor")) {
            try {
                // 使用ASM等工具修改线程池类的字节码
                ClassReader cr = new ClassReader(classfileBuffer);
                ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
                ClassVisitor cv = new ThreadPoolClassVisitor(cw);
                cr.accept(cv, ClassReader.SKIP_DEBUG);
                return cw.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return classfileBuffer;
    }

}