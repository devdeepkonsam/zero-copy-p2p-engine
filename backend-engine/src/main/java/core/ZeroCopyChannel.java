package core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ZeroCopyChannel {

    /**
     * Transfers bytes zero-copy from the file directly to a destination channel
     * using OS-level kernel channel mapping (via transferTo).
     *
     * @param filePath The path of the source file on disk.
     * @param offset   The offset position in the file where the transfer begins.
     * @param length   The number of bytes to transfer.
     * @param target   The destination WritableByteChannel (e.g. SocketChannel).
     * @return The number of bytes actually transferred.
     * @throws IOException If any I/O errors occur.
     */
    public static long transferChunk(Path filePath, long offset, long length, WritableByteChannel target) throws IOException {
        try (FileChannel fileChannel = FileChannel.open(filePath, StandardOpenOption.READ)) {
            return fileChannel.transferTo(offset, length, target);
        }
    }

    /**
     * Maps a portion of the file directly into native memory outside the JVM garbage-collected heap.
     *
     * @param filePath The path of the file on disk.
     * @param offset   The file offset where mapping starts.
     * @param length   The length of the region to map.
     * @return A direct MappedByteBuffer pointing to the mapped area.
     * @throws IOException If any mapping errors occur.
     */
    public static ByteBuffer mapChunk(Path filePath, long offset, int length) throws IOException {
        try (FileChannel fileChannel = FileChannel.open(filePath, StandardOpenOption.READ)) {
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, offset, length);
        }
    }
}
