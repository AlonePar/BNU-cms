package cn.edu.bnu.cms.common;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Created by dave on 16/5/19.
 */
public class Crypto {
    private static Mac macInstance;
    private static final Object LOCK = new Object();
    private static final String HMAC_SHA1 = "HMAC_SHA1";

    public static String encryptByMd5(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(text.getBytes());
            return byte2Hex(digest.digest());
        } catch (NoSuchAlgorithmException ex) {
            return "";
        }
    }

    public static String encryptBySha1(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(text.getBytes());
            return byte2Hex(digest.digest());
        } catch (NoSuchAlgorithmException ex) {
            return "";
        }
    }

    public static String byte2Hex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (byte b : bytes) {
            String hex = Integer.toHexString(b & 0xFF);
            if (hex.length() < 2) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    //AES加密,并返回BASE64编码
    //key不是16字节的倍数时,用字符#补齐成16字节的倍数
    public static String encryptAesBase64(String text, String key) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(alignBytes(key, 16), "AES"));
            return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes()));
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return "";
    }

    //解密上述加密的结果
    public static String decryptAesBase64(String text, String key) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(alignBytes(key, 16), "AES"));
            return new String(cipher.doFinal(Base64.getDecoder().decode(text)));
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static byte[] hmacSha1(byte[] key, byte[] data) {
        try {
            if (macInstance == null) {
                synchronized (LOCK) {
                    macInstance = Mac.getInstance(HMAC_SHA1);
                }
            }
            Mac mac;
            try {
                mac = (Mac) macInstance.clone();
            } catch (CloneNotSupportedException e) {
                mac = Mac.getInstance(HMAC_SHA1);
            }
            mac.init(new SecretKeySpec(key, HMAC_SHA1));
            return mac.doFinal(data);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("Unsupported algorithm: " + HMAC_SHA1, ex);
        } catch (InvalidKeyException ex) {
            throw new RuntimeException("Invalid key", ex);
        }
    }

    //将text字节长度补齐成base的倍数,返回字节数组
    private static byte[] alignBytes(String text, int base) {
        byte[] bytes = text.getBytes();
        if (bytes.length % base != 0) {
            StringBuilder sb = new StringBuilder(text);
            for (int i = 0; i < base - bytes.length % base; i += 1) {
                sb.append('#');
            }
            bytes = sb.toString().getBytes();
        }
        return bytes;
    }
}
