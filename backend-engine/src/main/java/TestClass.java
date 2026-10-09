import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.stream.Stream;

import core.ChunkManager;

public class TestClass {
    public static void main(String[] args) {
        try {
            Path projectRoot = findProjectRoot();
            Path inputDirectory = projectRoot.resolve("testing/input");
            Path chunksDirectory = projectRoot.resolve("testing/chunks");

            Files.createDirectories(inputDirectory);
            Files.createDirectories(chunksDirectory);

            Path sourceFile = findInputFile(inputDirectory);
            if (sourceFile == null) {
                System.out.println("No input file found.");
                System.out.println("Add a file to: " + inputDirectory.toAbsolutePath());
                return;
            }

            int chunkSize = calculateChunkSize(Files.size(sourceFile));
            ChunkManager chunkManager = new ChunkManager(sourceFile, chunkSize);

            deleteOldChunks(chunksDirectory);
            writeChunks(chunkManager, chunksDirectory);

            System.out.println("\nChunking completed successfully.");
            System.out.println("Input file: " + sourceFile.toAbsolutePath());
            System.out.println("Input size: " + Files.size(sourceFile) + " bytes");
            System.out.println("Chunk size: " + chunkSize + " bytes");
            System.out.println("Chunks written: " + chunkManager.getTotalChunks());
            System.out.println("Integrity verification: deferred");
            System.out.println("Output directory: " + chunksDirectory.toAbsolutePath());

        } catch (IOException e) {
            System.err.println("Chunking failed: " + e.getMessage());
        }
    }

    private static Path findInputFile(Path inputDirectory) throws IOException {
        try (Stream<Path> files = Files.list(inputDirectory)) {
            Optional<Path> inputFile = files
                    .filter(Files::isRegularFile)
                    .filter(path -> !path.getFileName().toString().equals(".gitkeep"))
                    .sorted()
                    .findFirst();

            return inputFile.orElse(null);
        }
    }

    private static void deleteOldChunks(Path chunksDirectory) throws IOException {
        try (Stream<Path> files = Files.list(chunksDirectory)) {
            files
                    .filter(Files::isRegularFile)
                    .filter(path -> !path.getFileName().toString().equals(".gitkeep"))
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            throw new RuntimeException("Unable to delete old chunk: " + path, e);
                        }
                    });
        }
    }

    private static void writeChunks(
            ChunkManager chunkManager,
            Path chunksDirectory) throws IOException {
        for (int chunkIndex = 0;
             chunkIndex < chunkManager.getTotalChunks();
             chunkIndex++) {

            Path chunkFile = chunksDirectory.resolve(
                    String.format("chunk-%04d.bin", chunkIndex));

            try (FileChannel chunkChannel = FileChannel.open(
                    chunkFile,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE)) {

                long bytesWritten = chunkManager.streamChunk(
                        chunkIndex,
                        chunkChannel);

                long expectedBytes = chunkManager.getChunkLength(chunkIndex);
                if (bytesWritten != expectedBytes) {
                    throw new IOException(
                            "Incomplete transfer for chunk " + chunkIndex);
                }
            }
        }
    }

    private static Path findProjectRoot() {
        Path currentDirectory = Path.of("").toAbsolutePath().normalize();
        if (Files.isDirectory(currentDirectory.resolve("backend-engine"))) {
            return currentDirectory;
        }
        return currentDirectory.getParent();
    }

    private static int calculateChunkSize(long fileSize) {
        long megabyte = 1024 * 1024;
        long targetChunkCount = 100;
        long estimatedSize = (fileSize + targetChunkCount - 1) / targetChunkCount;
        long sizeInMegabytes = (estimatedSize + megabyte - 1) / megabyte;
        long boundedSize = Math.max(1, Math.min(8, sizeInMegabytes));
        return (int) (boundedSize * megabyte);
    }
}
