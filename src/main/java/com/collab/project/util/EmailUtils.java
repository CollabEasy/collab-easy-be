package com.collab.project.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.Base64;
import java.util.Base64.Decoder;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;

@Component
public class EmailUtils {

    @Value("${email.content.decrypt.key}")
    private String decryptKey;

    final Cipher cipher;

    public EmailUtils(@Autowired Cipher cipher) {
        this.cipher = cipher;
    }

    public String decryptEmailContent(String emailMessage) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Decoder decoder = Base64.getDecoder();
        byte[] encrypted1 = decoder.decode(emailMessage);

        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        SecretKeySpec keyspec = new SecretKeySpec(decryptKey.getBytes(), "AES");
        IvParameterSpec ivspec = new IvParameterSpec(decryptKey.getBytes());

        cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

        byte[] original = cipher.doFinal(encrypted1);
        return new String(original);
    }
}
