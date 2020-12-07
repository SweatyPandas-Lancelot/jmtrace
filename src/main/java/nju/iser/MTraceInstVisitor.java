package nju.iser;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.*;

public class MTraceInstVisitor extends MethodVisitor {
    public MTraceInstVisitor(int api) {
        super(api);
    }

    public MTraceInstVisitor(int api, MethodVisitor methodVisitor) {
        super(api, methodVisitor);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        boolean isRead = false;
        switch (opcode) {
            case Opcodes.GETFIELD:
            case Opcodes.GETSTATIC:
                isRead = true;
            case Opcodes.PUTFIELD:
            case Opcodes.PUTSTATIC:
                logVisitField(isRead, owner, name, descriptor);
                break;
            default:
                break;
        }
        super.visitFieldInsn(opcode, owner, name, descriptor);
    }

    @Override
    public void visitInsn(int opcode) {
        if (opcode >= IALOAD && opcode <= SALOAD) {
            logXALOAD();
        } else if (opcode >= IASTORE && opcode <= SASTORE) {
            boolean isTwoSlotsInsn = opcode == DASTORE || opcode == LASTORE;
            logXASTORE(isTwoSlotsInsn);
        }
        super.visitInsn(opcode);
    }

    private void logVisitField(boolean isRead, String owner, String name, String desc) {
        mv.visitLdcInsn(isRead);
        mv.visitLdcInsn(owner);
        mv.visitLdcInsn(name);
        mv.visitLdcInsn(desc);
        mv.visitMethodInsn(INVOKESTATIC, MTraceLogger.getPath(), "logVisitField", "(ZLjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", false);
    }

    public void logXALOAD() {
        // [... ref index
        visitInsn(DUP2);
        // [... ref index ref index
        visitMethodInsn(INVOKESTATIC, MTraceLogger.getPath(), "xaload", "(Ljava/lang/Object;I)V", false);
        // [... ref index
    }


    public void logXASTORE(boolean isTwoSlotsInsn) {
        if (isTwoSlotsInsn) {
            //[... ref index val1 val2
            moveTop2Back2();
            //[... val1 val2 ref index
        } else {
            //[... ref index val
            moveTop1Back2();
            //[... val ref index
        }
        //prepare args
        visitInsn(DUP2);
        visitMethodInsn(INVOKESTATIC, MTraceLogger.getPath(), "xastore", "(Ljava/lang/Object;I)V", false);
        if (isTwoSlotsInsn) {
            //[... val1 val2 ref index
            moveTop2Back2();
            //[... ref index val1 val2
        } else {
            //[... val ref index
            moveTop2Back();
            //[... ref index val
        }
    }

    private void moveTop2Back() {
        visitInsn(DUP2_X1);
        visitInsn(POP2);
    }

    private void moveTop2Back2() {
        visitInsn(DUP2_X2);
        visitInsn(POP2);
    }

    private void moveTop1Back2() {
        visitInsn(DUP_X2);
        visitInsn(POP);
    }
}
