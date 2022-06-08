package dev.xdark.ssvm.value;

import java.util.concurrent.atomic.AtomicLongFieldUpdater;

public abstract class AbstractReferenceCounted implements ReferenceCounted{

	volatile long refCount;
	private static final AtomicLongFieldUpdater<AbstractReferenceCounted> refCountUpdater =
			AtomicLongFieldUpdater.newUpdater(AbstractReferenceCounted.class, "refCount");

	@Override
	public void increaseRefCount() {
		refCountUpdater.incrementAndGet(this);
	}

	@Override
	public boolean decreaseRefCount() {
		long newRefCount = refCountUpdater.decrementAndGet(this);
		if(newRefCount == 0) {
			destroy();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public long getRefCount() {
		return refCount;
	}

	/**
	 * Called when the reference count reaches zero.
	 */
	protected abstract void destroy();
}
