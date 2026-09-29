package crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashValidator {

    /**
     * Calculates the SHA-256 digest of a byte array.
     */
    public static byte[] calculateSHA256(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(data);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    /**
     * Calculates the SHA-256 digest of a ByteBuffer (supports direct mapped buffers).
     */
    public static byte[] calculateSHA256(java.nio.ByteBuffer buffer) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            // If the buffer is backed by an array, we can access it directly,
            // otherwise we update the message digest with the buffer itself.
            if (buffer.hasArray()) {
                digest.update(buffer.array(), buffer.arrayOffset() + buffer.position(), buffer.remaining());
            } else {
                // Buffer is direct/mapped memory
                // Duplicate buffer to prevent modifying original position indicators
                java.nio.ByteBuffer dup = buffer.duplicate();
                digest.update(dup);
            }
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    /**
     * Formats a byte array into a hex string.
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
