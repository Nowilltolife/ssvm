package dev.xdark.ssvm.value;

public interface ReferenceCounted {

	/**
	 * Increases reference count.
	 */
	void increaseRefCount();

	/**
	 * Decreases reference count and returns true if reference count is zero.
	 * @return true if reference count is zero.
	 */
	boolean decreaseRefCount();
	long getRefCount();

	void touch();

	static <T> T retain(Object value) {
		if(value instanceof ReferenceCounted) ((ReferenceCounted) value).increaseRefCount();
		return (T) value;
	}

	static <T> T release(Object value) {
		if(value instanceof ReferenceCounted) ((ReferenceCounted) value).decreaseRefCount();
		return (T) value;
	}

}
