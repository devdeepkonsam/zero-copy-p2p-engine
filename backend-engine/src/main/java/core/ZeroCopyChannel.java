package core;

import java.io.IOException;
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
}
