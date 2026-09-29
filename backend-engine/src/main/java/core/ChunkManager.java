package core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;

public class ChunkManager {
    private final Path filePath;
    private final int chunkSize;
    private final long fileSize;
    private final int totalChunks;

    /**
     * Initializes the chunk manager for a target file.
     *
     * @param filePath  Path of the file.
     * @param chunkSize Size of each chunk in bytes.
     * @throws IOException If the file cannot be accessed.
     */
    public ChunkManager(Path filePath, int chunkSize) throws IOException {
        if (chunkSize <= 0) {
            throw new IllegalArgumentException("Chunk size must be greater than zero");
        }
        this.filePath = filePath;
        this.chunkSize = chunkSize;
        this.fileSize = Files.size(filePath);
        this.totalChunks = (int) Math.ceil((double) fileSize / chunkSize);
    }

    public long getFileSize() {
        return fileSize;
    }

    public int getTotalChunks() {
        return totalChunks;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    /**
     * Calculates the starting byte index of a chunk.
     */
    public long getChunkOffset(int chunkIndex) {
        if (chunkIndex < 0 || chunkIndex >= totalChunks) {
            throw new IllegalArgumentException("Invalid chunk index: " + chunkIndex);
        }
        return (long) chunkIndex * chunkSize;
    }

    /**
     * Calculates the actual byte size of a chunk, handling the trailing chunk.
     */
    public long getChunkLength(int chunkIndex) {
        if (chunkIndex < 0 || chunkIndex >= totalChunks) {
            throw new IllegalArgumentException("Invalid chunk index: " + chunkIndex);
        }
        if (chunkIndex == totalChunks - 1) {
            return fileSize - getChunkOffset(chunkIndex);
        }
        return chunkSize;
    }

    /**
     * Streams a specific chunk to the destination channel zero-copy.
     */
    public long streamChunk(int chunkIndex, WritableByteChannel target) throws IOException {
        long offset = getChunkOffset(chunkIndex);
        long length = getChunkLength(chunkIndex);
        return ZeroCopyChannel.transferChunk(filePath, offset, length, target);
    }

    /**
     * Maps a specific chunk directly to a DirectByteBuffer for validation.
     */
    public ByteBuffer getMappedChunk(int chunkIndex) throws IOException {
        long offset = getChunkOffset(chunkIndex);
        long length = getChunkLength(chunkIndex);
        return ZeroCopyChannel.mapChunk(filePath, offset, (int) length);
    }
}
