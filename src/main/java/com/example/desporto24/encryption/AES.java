package com.example.desporto24.encryption;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static com.example.desporto24.constant.SecurityConstant.KEY;

public class AES {
    private static final String encryptionCipher = "AES";

    public static String encryptString(String plainText, String userId) {
        try {
            String encryptedString;
            String localKey = KEY.concat(userId);

            //Composing secret key
            SecretKey secretKey = new SecretKeySpec(localKey.getBytes(), encryptionCipher);

            //AES cipher instance
            Cipher cipher = Cipher.getInstance(encryptionCipher);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            //Start encryption
            byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

            //Encode for readability
            encryptedString = Base64.getEncoder().encodeToString(encryptedBytes);

            return encryptedString;
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException |
                 InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public static String decryptString(String encryptedString, String userId) {
        try {
            String decryptedString;
            String localKey = KEY.concat(userId);

            //Composing secret key
            SecretKey secretKey = new SecretKeySpec(localKey.getBytes(), encryptionCipher);

            //AES cipher instance
            Cipher cipher = Cipher.getInstance(encryptionCipher);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            //Decrypt the encoded text
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedString));

            //Convert the decrypted bytes back to text.
            decryptedString = new String(decryptedBytes);

            return decryptedString;
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException |
                 InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}