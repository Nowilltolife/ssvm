package dev.xdark.ssvm.value;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

public abstract class AbstractReferenceCounted implements ReferenceCounted {

    private volatile long refCount;
    private static final AtomicLongFieldUpdater<AbstractReferenceCounted> refCountUpdater =
            AtomicLongFieldUpdater.newUpdater(AbstractReferenceCounted.class, "refCount");
    private List<Throwable> hints;

    @Override
    public void increaseRefCount() {
        hints.add(new Exception("increase"));
        refCountUpdater.incrementAndGet(this);
    }

    @Override
    public boolean decreaseRefCount() {
        hints.add(new Exception("decrease"));
        long newRefCount = refCountUpdater.decrementAndGet(this);
        if (newRefCount == 0) {
            hints.add(new Exception("destroy"));
            destroy();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void touch() {
        hints = new ArrayList<>();
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
