package com.newsagg_nlp.news_agg.utils;

import org.junit.Test;

import static com.newsagg_nlp.news_agg.utils.Crypt.decrypt;
import static org.junit.Assert.assertEquals;

public class test {
    @Test
    public void testDecryptionWithKnownValues() throws Exception {
        String knownEncryptedPassword = "your_salt:your_iv:your_cipherText";
        String secretKey = "123";
        String expectedPassword = "password123";

        String decryptedPassword = decrypt(knownEncryptedPassword, secretKey);
        assertEquals("Decrypted password does not match", expectedPassword, decryptedPassword);
    }

}
