package dev.xdark.ssvm.execution.asm;

import dev.xdark.ssvm.execution.ExecutionContext;
import dev.xdark.ssvm.execution.InstructionProcessor;
import dev.xdark.ssvm.execution.Result;
import dev.xdark.ssvm.jit.JitHelper;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;

import static dev.xdark.ssvm.value.ReferenceCounted.retain;

/**
 * Pushes multidimensional array.
 *
 * @author xDark
 */
public final class MultiNewArrayProcessor implements InstructionProcessor<MultiANewArrayInsnNode> {

	@Override
	public Result execute(MultiANewArrayInsnNode insn, ExecutionContext ctx) {
		ctx.getStack().push(retain(JitHelper.multiNewArray(insn.desc, insn.dims, ctx)));
		return Result.CONTINUE;
	}
}
