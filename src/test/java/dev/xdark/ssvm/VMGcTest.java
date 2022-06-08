package dev.xdark.ssvm;

import dev.xdark.ssvm.execution.ExecutionContext;
import dev.xdark.ssvm.memory.SimpleMemoryManager;
import dev.xdark.ssvm.mirror.InstanceJavaClass;
import dev.xdark.ssvm.value.ObjectValue;
import dev.xdark.ssvm.value.Value;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.ClassNode;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Opcodes.LRETURN;

public class VMGcTest {

	private static VirtualMachine vm;

	@BeforeAll
	private static void setup() {
		(vm = new VirtualMachine()).bootstrap();
	}

	@Test
	public void testGc() {
		ClassNode node = new ClassNode();
		node.visit(V11, ACC_PUBLIC, "Test", null, null, null);
		MethodVisitor mv = node.visitMethod(ACC_STATIC, "test", "()V", null, null);
		// create new string
		mv.visitTypeInsn(NEW, "java/lang/String");
		mv.visitInsn(DUP);
		mv.visitLdcInsn("Hello World");
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/String", "<init>", "(Ljava/lang/String;)V", false);
		mv.visitVarInsn(ASTORE, 1);
		// create new string
		mv.visitTypeInsn(NEW, "java/lang/String");
		mv.visitInsn(DUP);
		mv.visitLdcInsn("Hello World");
		mv.visitMethodInsn(INVOKESPECIAL, "java/lang/String", "<init>", "(Ljava/lang/String;)V", false);
		mv.visitVarInsn(ASTORE, 2);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitVarInsn(ASTORE, 2);
		// 1 should be garbage collected
		mv.visitInsn(RETURN);
		mv.visitMaxs(0, 0);
		InstanceJavaClass jc = TestUtil.createClass(vm, node);
		ExecutionContext result = vm.getHelper().invokeStatic(jc, "test", "()V", new Value[0], new Value[0]);
		SimpleMemoryManager manager = (SimpleMemoryManager) vm.getMemoryManager();
		for (ObjectValue listObject : manager.listObjects()) {
			if(listObject.getRefCount() > 1) {
				Assertions.fail("Object " + listObject + " has ref count " + listObject.getRefCount());
			}
		}

		return;
	}

}
