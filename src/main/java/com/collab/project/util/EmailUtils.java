package com.collab.project.util;

import com.collab.project.config.CipherConfig;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

@Component
public class EmailUtils {

    @Value("${email.content.decrypt.key}")
    private String decryptKey;

    @Value("${email.content.decrypt.keySize}")
    private int keySize;

    @Value("${email.content.decrypt.iterationCount}")
    private int iterationCount;

    final Cipher cipher;

    public EmailUtils(@Autowired Cipher cipher) {
        this.cipher = cipher;
    }

    public String decryptEmailContent(String emailMessage) throws NoSuchPaddingException,
            NoSuchAlgorithmException {
        String decryptedContent =  new String(java.util.Base64.getDecoder().decode(emailMessage));
       if (decryptedContent != null && decryptedContent.split("::").length == 3) {
            String content = decrypt(decryptedContent.split("::")[1], decryptedContent.split("::")[0],
                    decryptedContent.split("::")[2]);
            return content;
        }
        return null;
    }

    public String decrypt(String salt, String iv, String ciphertext) {
        try {
            SecretKey key = generateKey(salt);
            byte[] decrypted = doFinal(Cipher.DECRYPT_MODE, key, iv, base64(ciphertext));
            return new String(decrypted, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            return null;
        }catch (Exception e){
            return null;
        }
    }

    private byte[] doFinal(int encryptMode, SecretKey key, String iv, byte[] bytes) {
        try {
            cipher.init(encryptMode, key, new IvParameterSpec(hex(iv)));
            return cipher.doFinal(bytes);
        }
        catch (InvalidKeyException
                | InvalidAlgorithmParameterException
                | IllegalBlockSizeException
                | BadPaddingException e) {
            return null;
        }
    }

    private SecretKey generateKey(String salt) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(decryptKey.toCharArray(), hex(salt), iterationCount, keySize);
            SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
            return key;
        }
        catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            return null;
        }
    }

    public byte[] base64(String str) {
        return Base64.decodeBase64(str);
    }

    public byte[] hex(String str) {
        try {
            return Hex.decodeHex(str.toCharArray());
        }
        catch (DecoderException e) {
            throw new IllegalStateException(e);
        }
    }
}
