package crypto;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import core.ChunkManager;

public class MerkleTree {
    private final List<String> leaves;
    private final List<List<String>> treeLayers;
    private final String rootHash;

    /**
     * Constructs a Merkle Tree from the chunks loaded via ChunkManager.
     *
     * @param chunkManager The manager containing the file chunk metadata.
     * @throws IOException If chunk mapping fails.
     */
    public MerkleTree(ChunkManager chunkManager) throws IOException {
        this.leaves = new ArrayList<>();
        this.treeLayers = new ArrayList<>();
        
        // Generate leaf hashes
        for (int i = 0; i < chunkManager.getTotalChunks(); i++) {
            ByteBuffer buffer = chunkManager.getMappedChunk(i);
            byte[] hash = HashValidator.calculateSHA256(buffer);
            leaves.add(HashValidator.bytesToHex(hash));
        }
        
        this.rootHash = buildTree();
    }

    /**
     * Builds the tree layers up to the root hash.
     */
    private String buildTree() {
        if (leaves.isEmpty()) {
            return "";
        }
        
        List<String> currentLayer = new ArrayList<>(leaves);
        treeLayers.add(currentLayer);

        while (currentLayer.size() > 1) {
            List<String> nextLayer = new ArrayList<>();
            for (int i = 0; i < currentLayer.size(); i += 2) {
                String left = currentLayer.get(i);
                // Duplicate odd nodes to maintain binary structure
                String right = (i + 1 < currentLayer.size()) ? currentLayer.get(i + 1) : left;
                
                byte[] combinedBytes = (left + right).getBytes();
                byte[] parentHash = HashValidator.calculateSHA256(combinedBytes);
                nextLayer.add(HashValidator.bytesToHex(parentHash));
            }
            currentLayer = nextLayer;
            treeLayers.add(currentLayer);
        }

        return currentLayer.get(0);
    }

    public String getRootHash() {
        return rootHash;
    }

    public List<String> getLeaves() {
        return leaves;
    }

    public List<List<String>> getTreeLayers() {
        return treeLayers;
    }

    /**
     * Verifies that the integrity of a downloaded chunk matches the target leaf and propagates correctly to the root hash.
     *
     * @param chunkIndex The index of the chunk.
     * @param chunkData  The buffer containing the chunk contents.
     * @return True if the integrity is verified; false otherwise.
     */
    public boolean verifyChunk(int chunkIndex, ByteBuffer chunkData) {
        if (chunkIndex < 0 || chunkIndex >= leaves.size()) {
            return false;
        }
        
        byte[] hashBytes = HashValidator.calculateSHA256(chunkData);
        String calculatedLeafHash = HashValidator.bytesToHex(hashBytes);
        
        // Leaf hash must match the recorded leaf hash
        if (!calculatedLeafHash.equals(leaves.get(chunkIndex))) {
            return false;
        }

        // Reconstruct proof verification path
        String currentHash = calculatedLeafHash;
        int index = chunkIndex;

        for (int level = 0; level < treeLayers.size() - 1; level++) {
            List<String> layer = treeLayers.get(level);
            String sibling;
            
            if (index % 2 == 0) {
                // Left node, sibling is right
                sibling = (index + 1 < layer.size()) ? layer.get(index + 1) : currentHash;
                currentHash = HashValidator.bytesToHex(HashValidator.calculateSHA256((currentHash + sibling).getBytes()));
            } else {
                // Right node, sibling is left
                sibling = layer.get(index - 1);
                currentHash = HashValidator.bytesToHex(HashValidator.calculateSHA256((sibling + currentHash).getBytes()));
            }
            index /= 2;
        }

        return currentHash.equals(rootHash);
    }
}
