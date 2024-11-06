package com.newsagg_nlp.news_agg.utils;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.spec.KeySpec;
import java.util.Base64;

public class Crypt {
//    public static String decrypt(String encryptedPassword, String secret) throws Exception {
//        if (encryptedPassword == null || encryptedPassword.isEmpty()) {
//            throw new IllegalArgumentException("Encrypted password cannot be null or empty.");
//        }
//
//        // Split the input string into salt, IV, and ciphertext
//        String[] parts = encryptedPassword.split(":");
//        if (parts.length != 3) {
//            throw new IllegalArgumentException("Invalid encrypted data format. Expected format: salt:iv:ciphertext");
//        }
//
//        // Base64 decode each part
//        byte[] salt = Base64.getDecoder().decode(parts[0]);
//        byte[] iv = Base64.getDecoder().decode(parts[1]);
//        byte[] cipherText = Base64.getDecoder().decode(parts[2]);
//
//        // Generate key using PBKDF2
//        SecretKeySpec key = generatePBKDF2Key(secret, salt);
//
//        // Initialize the AES cipher for decryption using the IV
//        Cipher aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding");
//        aesCBC.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
//
////        Cipher aesCBC = Cipher.getInstance("AES/CBC/NoPadding");
////        aesCBC.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
//        // Decrypt the data
//        byte[] decryptedBytes = aesCBC.doFinal(cipherText);
//
//        // Convert the decrypted bytes back into a UTF-8 string
//        return new String(decryptedBytes, StandardCharsets.UTF_8);
//    }
//
//    public static SecretKeySpec generatePBKDF2Key(String secret, byte[] salt) throws Exception {
//        int iterations = 1000;
//        int keyLength = 256; // 256-bit key
//
//        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
//        KeySpec spec = new PBEKeySpec(secret.toCharArray(), salt, iterations, keyLength);
//        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
//
//        return new SecretKeySpec(keyBytes, "AES");
//    }



    public static String decrypt(String encryptedPassword, String secret) throws Exception {
        if (encryptedPassword == null || encryptedPassword.isEmpty()) {
            throw new IllegalArgumentException("Encrypted password cannot be null or empty.");
        }

        // Split the input string into salt, IV, and ciphertext
        String[] parts = encryptedPassword.split(":");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid encrypted data format. Expected format: salt:iv:ciphertext");
        }

        // Base64 decode each part
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] iv = Base64.getDecoder().decode(parts[1]);
        byte[] cipherText = Base64.getDecoder().decode(parts[2]);

        // Generate key using PBKDF2
        SecretKeySpec key = generatePBKDF2Key(secret, salt);

        // Initialize the AES cipher for decryption using the IV
        Cipher aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding");
        aesCBC.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));

        // Decrypt the data
        byte[] decryptedBytes = aesCBC.doFinal(cipherText);

        // Convert the decrypted bytes back into a UTF-8 string
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public static SecretKeySpec generatePBKDF2Key(String secret, byte[] salt) throws Exception {
        int iterations = 1000;
        int keyLength = 256; // 256-bit key

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(secret.toCharArray(), salt, iterations, keyLength);
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();

        return new SecretKeySpec(keyBytes, "AES");
    }

}