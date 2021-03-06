package dev.xdark.ssvm;

import dev.xdark.ssvm.execution.Stack;
import dev.xdark.ssvm.execution.ThreadStack;
import dev.xdark.ssvm.value.DoubleValue;
import dev.xdark.ssvm.value.IntValue;
import dev.xdark.ssvm.value.LongValue;
import dev.xdark.ssvm.value.NullValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class StackTest {

	@Test
	public void testDup() {
		try (ThreadStack stack = new ThreadStack(2)) {
			stack.push(IntValue.of(5));
			stack.dup();
			assertEquals(5, stack.pop().asInt());
			assertEquals(5, stack.pop().asInt());
			assertTrue(stack.isEmpty());
		}
	}

	@Test
	public void testDupX1() {
		try (ThreadStack stack = new ThreadStack(3)) {
			stack.push(IntValue.of(2));
			stack.push(IntValue.ONE);
			stack.dupx1();
			assertEquals(1, stack.pop().asInt());
			assertEquals(2, stack.pop().asInt());
			assertEquals(1, stack.pop().asInt());
			assertTrue(stack.isEmpty());
		}
	}

	@Test
	public void testDupX2Form1() {
		try (ThreadStack stack = new ThreadStack(4)) {
			stack.push(IntValue.of(3));
			stack.push(IntValue.of(2));
			stack.push(IntValue.ONE);
			stack.dupx2();
			assertEquals(1, stack.pop().asInt());
			assertEquals(2, stack.pop().asInt());
			assertEquals(3, stack.pop().asInt());
			assertEquals(1, stack.pop().asInt());
			assertTrue(stack.isEmpty());
		}
	}

	@Test
	public void testDupX2Form2() {
		try (ThreadStack stack = new ThreadStack(4)) {
			stack.pushWide(LongValue.of(2L));
			stack.push(IntValue.ONE);
			stack.dupx2();
			assertEquals(1, stack.pop().asInt());
			assertEquals(2L, stack.popWide().asLong());
			assertEquals(1, stack.pop().asInt());
			assertTrue(stack.isEmpty());
		}
	}

	@Test
	public void testDup2Form1() {
		try (ThreadStack stack = new ThreadStack(4)) {
			stack.push(IntValue.of(2));
			stack.push(IntValue.ONE);
			stack.dup2();
			assertEquals(1, stack.pop().asInt());
			assertEquals(2, stack.pop().asInt());
			assertEquals(1, stack.pop().asInt());
			assertEquals(2, stack.pop().asInt());
			assertTrue(stack.isEmpty());
		}
	}

	@Test
	public void testDup2Form2() {
		try (ThreadStack stack = new ThreadStack(4)) {
			stack.pushWide(LongValue.ONE);
			stack.dup2();
			assertEquals(1L, stack.popWide().asLong());
			assertEquals(1L, stack.popWide().asLong());
			assertTrue(stack.isEmpty());
		}
	}

	@Test
	public void testDup2X1Form1() {
		try (ThreadStack stack = new ThreadStack(5)) {
			stack.push(IntValue.of(3));
			stack.push(IntValue.of(2));
			stack.push(IntValue.ONE);
			stack.dup2x1();
			assertEquals(1, stack.pop().asInt());
			assertEquals(2, stack.pop().asInt());
			assertEquals(3, stack.pop().asInt());
			assertEquals(1, stack.pop().asInt());
			assertEquals(2, stack.pop().asInt());
			assertTrue(stack.isEmpty());
		}
	}

	@Test
	public void testDup2X1Form2() {
		try (ThreadStack stack = new ThreadStack(5)) {
			stack.push(IntValue.of(2));
			stack.pushWide(LongValue.ONE);
			stack.dup2x1();
			assertEquals(1L, stack.popWide().asLong());
			assertEquals(2, stack.pop().asInt());
			assertEquals(1L, stack.popWide().asLong());
			assertTrue(stack.isEmpty());
		}
	}

	@Test
	public void testDup2X2Form1() {
		try (ThreadStack stack = new ThreadStack(6)) {
			stack.push(IntValue.of(4));
			stack.push(IntValue.of(3));
			stack.push(IntValue.of(2));
			stack.push(IntValue.ONE);
			stack.dup2x2();
			assertEquals(1, stack.pop().asInt());
			assertEquals(2, stack.pop().asInt());
			assertEquals(3, stack.pop().asInt());
			assertEquals(4, stack.pop().asInt());
			assertEquals(1, stack.pop().asInt());
			assertEquals(2, stack.pop().asInt());
			assertTrue(stack.isEmpty());
		}
	}

	@Test
	public void testDup2X2Form2() {
		try (ThreadStack stack = new ThreadStack(6)) {
			stack.push(IntValue.of(3));
			stack.push(IntValue.of(2));
			stack.pushWide(LongValue.ONE);
			stack.dup2x2();
			assertEquals(1L, stack.popWide().asLong());
			assertEquals(2, stack.pop().asInt());
			assertEquals(3, stack.pop().asInt());
			assertEquals(1L, stack.popWide().asLong());
			assertTrue(stack.isEmpty());
		}
	}

	@Test
	public void testDup2X2Form3() {
		try (ThreadStack stack = new ThreadStack(6)) {
			stack.pushWide(LongValue.of(3L));
			stack.push(IntValue.of(2));
			stack.push(IntValue.ONE);
			stack.dup2x2();
			assertEquals(1, stack.pop().asInt());
			assertEquals(2, stack.pop().asInt());
			assertEquals(3L, stack.popWide().asLong());
			assertEquals(1, stack.pop().asInt());
			assertEquals(2, stack.pop().asInt());
			assertTrue(stack.isEmpty());
		}
	}

	@Test
	public void testDup2X2Form4() {
		try (ThreadStack stack = new ThreadStack(6)) {
			stack.pushWide(LongValue.of(2L));
			stack.pushWide(LongValue.ONE);
			stack.dup2x2();
			assertEquals(1L, stack.popWide().asLong());
			assertEquals(2L, stack.popWide().asLong());
			assertEquals(1L, stack.popWide().asLong());
			assertTrue(stack.isEmpty());
		}
	}

	@Test
	public void testSwap() {
		try (ThreadStack stack = new ThreadStack(2)) {
			stack.push(IntValue.ZERO);
			stack.push(IntValue.ONE);
			stack.swap();
			assertEquals(0, stack.pop().asInt());
			assertEquals(1, stack.pop().asInt());
			assertTrue(stack.isEmpty());
		}
	}

	@Test
	public void testEquality() {
		try (ThreadStack stack1 = new ThreadStack(8);
			 ThreadStack stack2 = new ThreadStack(8)) {
			filLStack(stack1);
			filLStack(stack2);
			assertEquals(stack1, stack2);
		}
	}
	
	private static void filLStack(Stack stack) {
		stack.push(IntValue.ONE);
		stack.pushWide(LongValue.ZERO);
		stack.pushWide(new DoubleValue(1.3D));
		stack.push(NullValue.INSTANCE);
		stack.pushWide(LongValue.ONE);
	}
}
