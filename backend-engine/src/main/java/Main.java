
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import core.ChunkManager;
import crypto.MerkleTree;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Zero-Copy Multi-Platform P2P Asset Streaming Engine ===");
        
        try {
            // Create a dummy file for demo if no arguments are provided
            Path tempFile = Path.of("demo_payload_8MB.bin");
            if (!Files.exists(tempFile)) {
                System.out.println("Generating dummy 8MB payload file for streaming demonstration...");
                byte[] data = new byte[8 * 1024 * 1024]; // 8 MB
                // Populate with some deterministic data
                for (int i = 0; i < data.length; i++) {
                    data[i] = (byte) (i % 256);
                }
                Files.write(tempFile, data, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            }
            
            System.out.println("Loading file: " + tempFile.toAbsolutePath());
            System.out.println("File Size: " + Files.size(tempFile) + " bytes");

            // Chunk Size: 2MB (2097152 bytes)
            int chunkSize = 2 * 1024 * 1024;
            System.out.println("Initializing ChunkManager with Chunk Size: 2 MB...");
            ChunkManager chunkManager = new ChunkManager(tempFile, chunkSize);
            System.out.println("Total Chunks: " + chunkManager.getTotalChunks());

            // Build Merkle Tree
            System.out.println("Generating Merkle Tree...");
            MerkleTree merkleTree = new MerkleTree(chunkManager);
            System.out.println("Merkle Root Hash: " + merkleTree.getRootHash());

            // List Leaf Hashes
            List<String> leaves = merkleTree.getLeaves();
            for (int i = 0; i < leaves.size(); i++) {
                System.out.println("  Chunk #" + i + " SHA-256: " + leaves.get(i));
            }

            // Demonstrate Memory-Mapping & Verification
            System.out.println("\n--- Verifying Integrity of Chunk #2 ---");
            ByteBuffer mappedBuffer = chunkManager.getMappedChunk(2);
            boolean isValid = merkleTree.verifyChunk(2, mappedBuffer);
            System.out.println("Chunk #2 Verification Result: " + (isValid ? "SUCCESS (INTEGRITY VERIFIED)" : "FAILED"));

            // Demonstrate Zero-Copy Stream Transfer to a mock channel
            System.out.println("\n--- Streaming Chunk #0 Zero-Copy to Output Channel (First 128 bytes preview) ---");
            WritableByteChannel previewChannel = new WritableByteChannel() {
                private boolean open = true;
                private int bytesWritten = 0;

                @Override
                public int write(ByteBuffer src) throws IOException {
                    int len = src.remaining();
                    byte[] temp = new byte[len];
                    src.get(temp);
                    
                    // Only print the first 128 bytes
                    int toPrint = Math.min(128 - bytesWritten, len);
                    if (toPrint > 0) {
                        System.out.print("Data Bytes Hex Preview: ");
                        for (int i = 0; i < toPrint; i++) {
                            System.out.printf("%02X ", temp[i]);
                        }
                        System.out.println("...");
                        bytesWritten += toPrint;
                    }
                    return len;
                }

                @Override
                public boolean isOpen() { return open; }

                @Override
                public void close() { open = false; }
            };

            chunkManager.streamChunk(0, previewChannel);
            System.out.println("Zero-Copy Stream Transfer chunk streaming completed successfully.");
            
            // Clean up demo file
            Files.deleteIfExists(tempFile);
            System.out.println("Cleanup completed.");

        } catch (IOException e) {
            System.err.println("Error running engine demo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
