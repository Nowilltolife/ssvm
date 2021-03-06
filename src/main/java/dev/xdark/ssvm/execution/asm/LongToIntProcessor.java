package dev.xdark.ssvm.execution.asm;

import dev.xdark.ssvm.execution.ExecutionContext;
import dev.xdark.ssvm.execution.InstructionProcessor;
import dev.xdark.ssvm.execution.Result;
import dev.xdark.ssvm.execution.Stack;
import dev.xdark.ssvm.value.IntValue;
import org.objectweb.asm.tree.AbstractInsnNode;

/**
 * Converts long to int.
 *
 * @author xDark
 */
public final class LongToIntProcessor implements InstructionProcessor<AbstractInsnNode> {

	@Override
	public Result execute(AbstractInsnNode insn, ExecutionContext ctx) {
		Stack stack = ctx.getStack();
		stack.push(IntValue.of(stack.popWide().asInt()));
		return Result.CONTINUE;
	}
}
