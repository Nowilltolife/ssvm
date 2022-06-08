package dev.xdark.ssvm.execution.asm;

import dev.xdark.ssvm.asm.VMLdcInsnNode;
import dev.xdark.ssvm.execution.ExecutionContext;
import dev.xdark.ssvm.execution.InstructionProcessor;
import dev.xdark.ssvm.execution.Result;

import static dev.xdark.ssvm.value.ReferenceCounted.retain;

/**
 * Fast path for LDC instruction.
 *
 * @author xDark
 */
public final class VMLdcProcessor implements InstructionProcessor<VMLdcInsnNode> {

	@Override
	public Result execute(VMLdcInsnNode insn, ExecutionContext ctx) {
		ctx.getStack().pushGeneric(retain(insn.getValue()));
		return Result.CONTINUE;
	}
}
