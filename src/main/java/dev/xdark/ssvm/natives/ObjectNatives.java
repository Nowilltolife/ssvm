package dev.xdark.ssvm.natives;

import dev.xdark.ssvm.VirtualMachine;
import dev.xdark.ssvm.api.MethodInvoker;
import dev.xdark.ssvm.api.VMInterface;
import dev.xdark.ssvm.execution.Locals;
import dev.xdark.ssvm.execution.Result;
import dev.xdark.ssvm.memory.MemoryData;
import dev.xdark.ssvm.memory.MemoryManager;
import dev.xdark.ssvm.mirror.ArrayJavaClass;
import dev.xdark.ssvm.mirror.InstanceJavaClass;
import dev.xdark.ssvm.mirror.JavaClass;
import dev.xdark.ssvm.util.VMHelper;
import dev.xdark.ssvm.symbol.VMSymbols;
import dev.xdark.ssvm.value.ArrayValue;
import dev.xdark.ssvm.value.InstanceValue;
import dev.xdark.ssvm.value.IntValue;
import dev.xdark.ssvm.value.ObjectValue;
import lombok.experimental.UtilityClass;

/**
 * Initializes java/lang/Object.
 *
 * @author xDark
 */
@UtilityClass
public class ObjectNatives {

	/**
	 * @param vm
	 * 		VM instance.
	 */
	public void init(VirtualMachine vm) {
		VMInterface vmi = vm.getInterface();
		VMSymbols symbols = vm.getSymbols();
		InstanceJavaClass object = symbols.java_lang_Object();
		vmi.setInvoker(object, "registerNatives", "()V", MethodInvoker.noop());
		vmi.setInvoker(object, "<init>", "()V", ctx -> {
			ctx.getLocals().<InstanceValue>load(0).initialize();
			return Result.ABORT;
		});
		vmi.setInvoker(object, "getClass", "()Ljava/lang/Class;", ctx -> {
			ctx.setResult(ctx.getLocals().<ObjectValue>load(0).getJavaClass().getOop());
			return Result.ABORT;
		});
		vmi.setInvoker(object, "notify", "()V", ctx -> {
			ObjectValue value = ctx.getLocals().<ObjectValue>load(0);
			if (!value.isHeldByCurrentThread()) {
				vm.getHelper().throwException(symbols.java_lang_IllegalMonitorStateException());
			}
			value.monitorNotify();
			return Result.ABORT;
		});
		vmi.setInvoker(object, "notifyAll", "()V", ctx -> {
			ObjectValue value = ctx.getLocals().<ObjectValue>load(0);
			if (!value.isHeldByCurrentThread()) {
				vm.getHelper().throwException(symbols.java_lang_IllegalMonitorStateException());
			}
			value.monitorNotifyAll();
			return Result.ABORT;
		});
		vmi.setInvoker(object, "wait", "(J)V", ctx -> {
			Locals locals = ctx.getLocals();
			ObjectValue value = locals.<ObjectValue>load(0);
			if (!value.isHeldByCurrentThread()) {
				vm.getHelper().throwException(symbols.java_lang_IllegalMonitorStateException());
			}
			try {
				value.monitorWait(locals.load(1).asLong());
			} catch(InterruptedException ex) {
				vm.getHelper().throwException(symbols.java_lang_InterruptedException());
			}
			return Result.ABORT;
		});
		vmi.setInvoker(object, "hashCode", "()I", ctx -> {
			ctx.setResult(IntValue.of(ctx.getLocals().load(0).hashCode()));
			return Result.ABORT;
		});
		vmi.setInvoker(object, "clone", "()Ljava/lang/Object;", ctx -> {
			ObjectValue _this = ctx.getLocals().<ObjectValue>load(0);
			JavaClass type = _this.getJavaClass();
			VMHelper helper = vm.getHelper();
			MemoryManager memoryManager = vm.getMemoryManager();
			ObjectValue clone;
			if (type instanceof ArrayJavaClass) {
				ArrayValue arr = (ArrayValue) _this;
				clone = memoryManager.newArray((ArrayJavaClass) type, arr.getLength());
			} else {
				clone = memoryManager.newInstance((InstanceJavaClass) type);
			}
			int originalOffset = memoryManager.valueBaseOffset(_this);
			int offset = memoryManager.valueBaseOffset(clone);
			helper.checkEquals(originalOffset, offset);
			MemoryData copyTo = clone.getMemory().getData();
			MemoryData copyFrom = _this.getMemory().getData();
			copyFrom.copy(
					offset,
					copyTo,
					offset,
					copyFrom.length() - offset
			);
			ctx.setResult(clone);
			return Result.ABORT;
		});
	}
}
