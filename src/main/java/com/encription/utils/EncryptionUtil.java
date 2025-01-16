package com.encription.utils;

import com.encription.exception.EncryptionDecryptionException;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

@Component
public class EncryptionUtil {

    private static SecretKeySpec secretKey;
    private static byte[] key;
    private static final String ALGORITHM = "AES";
    private static final String SHA_ALGORITHM = "SHA-1";

    private static void prepareSecretKey(String myKey) {
        try {
            key = myKey.getBytes(StandardCharsets.UTF_8);
            MessageDigest sha = MessageDigest.getInstance(SHA_ALGORITHM);
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new EncryptionDecryptionException("Error while preparing secret key", e);
        }
    }

    public static String encrypt(String strToEncrypt, String secret) {
        try {
            prepareSecretKey(secret);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new EncryptionDecryptionException("Error while encrypting", e);
        }
    }

    public static String decrypt(String strToDecrypt, String secret) {
        try {
            prepareSecretKey(secret);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new EncryptionDecryptionException("Error while decrypting", e);
        }
    }
}
