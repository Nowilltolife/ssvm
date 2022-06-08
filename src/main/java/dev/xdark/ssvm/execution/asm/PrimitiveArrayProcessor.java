package dev.xdark.ssvm.execution.asm;

import dev.xdark.ssvm.execution.ExecutionContext;
import dev.xdark.ssvm.execution.InstructionProcessor;
import dev.xdark.ssvm.execution.Result;
import dev.xdark.ssvm.jit.JitHelper;
import org.objectweb.asm.tree.IntInsnNode;

import static dev.xdark.ssvm.value.ReferenceCounted.retain;

/**
 * Allocates new array.
 *
 * @author xDark
 */
public final class PrimitiveArrayProcessor implements InstructionProcessor<IntInsnNode> {

	@Override
	public Result execute(IntInsnNode insn, ExecutionContext ctx) {
		ctx.getStack().push(retain(JitHelper.allocatePrimitiveArray(insn.operand, ctx)));
		return Result.CONTINUE;
	}
}
