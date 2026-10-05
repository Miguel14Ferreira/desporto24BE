package com.example.desporto24.encryption;

import javax.crypto.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Map;

public class AES {

    private static final String ENCRYPTION_CIPHER = "AES";
    private static final String AES_ENVIRONMENT_KEY = "AES_ENCRYPTION_KEY";

    public static String encryptString(String plainText, String userId) {
        if (plainText == null) {
            return null;
        }
        try {
            SecretKey secretKey = new SecretKeySpec(composedKey(userId), ENCRYPTION_CIPHER);
            Cipher cipher = Cipher.getInstance(ENCRYPTION_CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException |
                 BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public static String hashLookupValue(String plainText) {
        if (plainText == null) {
            return null;
        }
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(
                    resolveBaseKey(System.getenv()).getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"));
            return HexFormat.of().formatHex(
                    mac.doFinal(plainText.trim().getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public static String decryptString(String encryptedString, String userId) {
        if (encryptedString == null) {
            return null;
        }
        try {
            SecretKey secretKey = new SecretKeySpec(composedKey(userId), ENCRYPTION_CIPHER);
            Cipher cipher = Cipher.getInstance(ENCRYPTION_CIPHER);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedString));
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException |
                 BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    static String resolveBaseKey(Map<String, String> environment) {
        String key = environment.get(AES_ENVIRONMENT_KEY);
        if (key == null || key.isBlank()) {
            throw new IllegalStateException("AES_ENCRYPTION_KEY must be configured");
        }
        return key;
    }

    private static byte[] composedKey(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("userId is required for AES encryption");
        }
        byte[] key = (resolveBaseKey(System.getenv()) + userId).getBytes(StandardCharsets.UTF_8);
        if (key.length != 16 && key.length != 24 && key.length != 32) {
            throw new IllegalStateException("AES_ENCRYPTION_KEY and userId must compose a valid AES key length");
        }
        return key;
    }
}
