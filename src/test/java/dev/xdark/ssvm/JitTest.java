package dev.xdark.ssvm;

import dev.xdark.ssvm.execution.VMException;
import dev.xdark.ssvm.fs.FileDescriptorManager;
import dev.xdark.ssvm.fs.HostFileDescriptorManager;
import dev.xdark.ssvm.jit.JitClass;
import dev.xdark.ssvm.jit.JitCompiler;
import dev.xdark.ssvm.jit.JitInstaller;
import dev.xdark.ssvm.mirror.InstanceJavaClass;
import dev.xdark.ssvm.mirror.JavaMethod;
import dev.xdark.ssvm.util.VMHelper;
import dev.xdark.ssvm.value.IntValue;
import dev.xdark.ssvm.value.LongValue;
import dev.xdark.ssvm.value.NullValue;
import dev.xdark.ssvm.value.TopValue;
import dev.xdark.ssvm.value.Value;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.Type;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JitTest {

	private static long a;
	private static int b;
	private static String c;
	private static long d;
	private static int e;

	@Test
	public void testJit() throws IOException {
		VirtualMachine vm = new VirtualMachine() {
			@Override
			protected FileDescriptorManager createFileDescriptorManager() {
				return new HostFileDescriptorManager();
			}
		};
		vm.bootstrap();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (InputStream in = JitTest.class.getClassLoader().getResourceAsStream(Type.getInternalName(JitTest.class) + ".class")) {
			byte[] buf = new byte[512];
			int r;
			while ((r = in.read(buf)) != -1) {
				baos.write(buf, 0, r);
			}
		}
		byte[] bc = baos.toByteArray();
		VMHelper helper = vm.getHelper();
		InstanceJavaClass jc = helper.defineClass(
				NullValue.INSTANCE,
				null,
				bc, 0, bc.length,
				NullValue.INSTANCE,
				"JVM_DefineClass"
		);
		ThreadLocalRandom rng = ThreadLocalRandom.current();
		a = rng.nextLong();
		b = rng.nextInt();
		c = Long.toBinaryString(rng.nextLong());
		d = rng.nextLong();
		e = rng.nextInt();
		JavaMethod m = jc.getStaticMethod("jitCall", "(JILjava/lang/String;JI)V");
		// Force compile
		JitClassLoader loader = new JitClassLoader();
		try {
			for (JavaMethod toCompile : jc.getStaticMethodLayout().getAll()) {
				if ("testJit".equals(toCompile.getName())) {
					continue;
				}
				JitClass compiled = JitCompiler.compile(toCompile, 3);
				JitInstaller.install(toCompile, loader, compiled);
			}
		} catch (ReflectiveOperationException ex) {
			throw new IllegalStateException(ex);
		}
		try {
			helper.invokeStatic(m, new Value[0], new Value[]{
					LongValue.of(a),
					TopValue.INSTANCE,
					IntValue.of(b),
					helper.newUtf8(c),
					LongValue.of(d),
					TopValue.INSTANCE,
					IntValue.of(e)
			});
		} catch (VMException ex) {
			helper.invokeVirtual("printStackTrace", "()V", new Value[0], new Value[]{ex.getOop()});
			throw ex;
		}
		assertEquals(a, jc.getStaticValue("a", "J").asLong());
		assertEquals(b, jc.getStaticValue("b", "I").asInt());
		assertEquals(c, helper.readUtf8(jc.getStaticValue("c", "Ljava/lang/String;")));
		assertEquals(d, jc.getStaticValue("d", "J").asLong());
		assertEquals(e, jc.getStaticValue("e", "I").asInt());
	}

	private static void jitCall(long a, int b, String c, long d, int e) {
		jitCallInner(a, b, c, d, e);
		testThrowInInvokeDynamic();
		testThrow();
		testMultiArray();
	}

	private static void jitCallInner(long a, int b, String c, long d, int e) {
		JitTest.a = a;
		JitTest.b = b;
		JitTest.c = c;
		JitTest.d = d;
		JitTest.e = e;
	}

	private static void testThrowInInvokeDynamic() {
		try {
			Runnable r = () -> {
				throw new IllegalArgumentException();
			};
			r.run();
			throw new IllegalStateException();
		} catch (IllegalArgumentException ignored) {
		}
	}

	private static void testThrow() {
		try {
			throwInner();
		} catch (IllegalStateException | IllegalArgumentException ignored) {
		}
	}

	private static void throwInner() {
		throw new IllegalArgumentException();
	}

	private static void testMultiArray() {
		int[][][] array = new int[1][1][1];
		array[0] = new int[1][1];
		array[0][0] = new int[1];
		array[0][0][0] = 5;
		if (array[0][0][0] != 5) {
			throw new IllegalStateException();
		}
	}

	private static final class JitClassLoader extends ClassLoader
			implements JitInstaller.ClassDefiner {

		@Override
		public Class<?> define(JitClass jitClass) {
			byte[] code = jitClass.getCode();
			return defineClass(jitClass.getClassName().replace('/', '.'), code, 0, code.length);
		}
	}
}
