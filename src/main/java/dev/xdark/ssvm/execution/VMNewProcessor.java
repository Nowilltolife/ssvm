package dev.xdark.ssvm.execution;

import dev.xdark.ssvm.asm.NewInsnNode;
import dev.xdark.ssvm.jit.JitHelper;
import org.objectweb.asm.Opcodes;

import static dev.xdark.ssvm.value.ReferenceCounted.retain;

/**
 * Fast path for NEW instruction.
 *
 * @author xDark
 */
public final class VMNewProcessor implements InstructionProcessor<NewInsnNode> {

	@Override
	public Result execute(NewInsnNode insn, ExecutionContext ctx) {
		ctx.getStack().push(retain(JitHelper.allocateInstance(insn.getJavaType(), ctx)));
		return Result.CONTINUE;
	}
}
