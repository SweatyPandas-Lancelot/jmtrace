package nju.iser;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class MTraceTransformer implements ClassFileTransformer {
    private static final String[] LIB_LIST = new String[]{"java", "jdk", "sun"};

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        if (isLibClass(className)) {
            return classfileBuffer;
        }
        ClassReader reader = new ClassReader(classfileBuffer);
        ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES);
        ClassVisitor visitor = new MTraceClassVisitor(Opcodes.ASM9, writer);
        reader.accept(visitor, 0);
        return writer.toByteArray();
    }

    private boolean isLibClass(String className) {
        for (String lib : LIB_LIST) {
            if (className.startsWith(lib)) return true;
        }
        return false;
    }
}
