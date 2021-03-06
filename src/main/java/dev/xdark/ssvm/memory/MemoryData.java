package dev.xdark.ssvm.memory;

import java.nio.ByteBuffer;

/**
 * Memory data source.
 *
 * @author xDark
 */
public interface MemoryData {

	/**
	 * Read long at the specific offset.
	 *
	 * @param offset
	 * 		Data offset.
	 *
	 * @return long value.
	 */
	long readLong(long offset);

	/**
	 * Read int at the specific offset.
	 *
	 * @param offset
	 * 		Data offset.
	 *
	 * @return int value.
	 */
	int readInt(long offset);

	/**
	 * Read char at the specific offset.
	 *
	 * @param offset
	 * 		Data offset.
	 *
	 * @return char value.
	 */
	char readChar(long offset);

	/**
	 * Read short at the specific offset.
	 *
	 * @param offset
	 * 		Data offset.
	 *
	 * @return short value.
	 */
	short readShort(long offset);

	/**
	 * Read byte at the specific offset.
	 *
	 * @param offset
	 * 		Data offset.
	 *
	 * @return byte value.
	 */
	byte readByte(long offset);

	/**
	 * Write long at the specific offset.
	 *
	 * @param offset
	 * 		Data offset.
	 * @param value
	 * 		Long value.
	 */
	void writeLong(long offset, long value);

	/**
	 * Write int at the specific offset.
	 *
	 * @param offset
	 * 		Data offset.
	 * @param value
	 * 		Int value.
	 */
	void writeInt(long offset, int value);

	/**
	 * Write char at the specific offset.
	 *
	 * @param offset
	 * 		Data offset.
	 * @param value
	 * 		Char value.
	 */
	void writeChar(long offset, char value);

	/**
	 * Write short at the specific offset.
	 *
	 * @param offset
	 * 		Data offset.
	 * @param value
	 * 		Short value.
	 */
	void writeShort(long offset, short value);

	/**
	 * Write byte at the specific offset.
	 *
	 * @param offset
	 * 		Data offset.
	 * @param value
	 * 		Byte value.
	 */
	void writeByte(long offset, byte value);

	/**
	 * Volatile version of {@link MemoryData#readLong(long)}.
	 */
	long readLongVolatile(long offset);

	/**
	 * Volatile version of {@link MemoryData#readInt(long)}.
	 */
	int readIntVolatile(long offset);

	/**
	 * Volatile version of {@link MemoryData#readChar(long)}.
	 */
	char readCharVolatile(long offset);

	/**
	 * Volatile version of {@link MemoryData#readShort(long)}.
	 */
	short readShortVolatile(long offset);

	/**
	 * Volatile version of {@link MemoryData#readByte(long)}.
	 */
	byte readByteVolatile(long offset);

	/**
	 * WVolatile version of {@link MemoryData#writeLong(long, long)}.
	 */
	void writeLongVolatile(long offset, long value);

	/**
	 * WVolatile version of {@link MemoryData#writeInt(long, int)}.
	 */
	void writeIntVolatile(long offset, int value);

	/**
	 * WVolatile version of {@link MemoryData#writeChar(long, char)}.
	 */
	void writeCharVolatile(long offset, char value);

	/**
	 * WVolatile version of {@link MemoryData#writeShort(long, short)}.
	 */
	void writeShortVolatile(long offset, short value);

	/**
	 * WVolatile version of {@link MemoryData#writeByte(long, byte)}.
	 */
	void writeByteVolatile(long offset, byte value);

	/**
	 * Fills data region.
	 *
	 * @param offset
	 * 		Data offset.
	 * @param bytes
	 * 		Data length.
	 * @param value
	 * 		Value to fill with.
	 */
	void set(long offset, long bytes, byte value);

	/**
	 * Copies this data region.
	 *
	 * @param srcOffset
	 * 		Source offset.
	 * @param dst
	 * 		Destination data.
	 * @param dstOffset
	 * 		Destination offset.
	 * @param bytes
	 * 		Data length.
	 */
	void copy(long srcOffset, MemoryData dst, long dstOffset, long bytes);

	/**
	 * Write buffer at the specific offset.
	 *
	 * @param offset
	 * 		Data offset.
	 * @param buffer
	 * 		Buffer to write.
	 */
	void write(long offset, ByteBuffer buffer);

	/**
	 * Write array of bytes at the specific offset.
	 *
	 * @param dstOffset
	 * 		Data offset.
	 * @param array
	 * 		Array to write.
	 * @param arrayOffset
	 * 		Array offset.
	 * @param length
	 * 		Array length.
	 */
	void write(long dstOffset, byte[] array, int arrayOffset, int length);

	/**
	 * @return size of this memory data.
	 */
	long length();

	/**
	 * Creates slice of this memory data.
	 *
	 * @param offset
	 * 		Slice offset.
	 * @param bytes
	 * 		Slice length.
	 *
	 * @return data slice.
	 */
	MemoryData slice(long offset, long bytes);

	/**
	 * Transfers data of this memory to
	 * another memory data.
	 *
	 * @param other
	 * 		Memory data to transfer to.
	 */
	void transferTo(MemoryData other);

	/**
	 * Creates buffer backed memory data.
	 *
	 * @param buffer
	 * 		Buffer to use.
	 *
	 * @return memory data instance.
	 */
	static MemoryData buffer(ByteBuffer buffer) {
		return new BufferMemoryData(buffer);
	}
}
