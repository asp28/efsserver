package uk.co.ankeetpatel.encryptedfilesystem.efsserver.algorithms;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

/**
 * CipherUltility class to control all RSA key functions
 */
@Component
public class CipherUtility {

    private static KeyPairGenerator keyPairGenerator = null;

    private final SecureRandom random = new SecureRandom();

    /**
     *
     * @return new KeyPair
     */
    public KeyPair getKeyPair() {
        return keyPairGenerator.genKeyPair();
    }

    /**
     *
     * @param content
     * @param pubKey
     * @return cipherContent
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public byte[] encryptBytes(byte[] content, Key pubKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] cipherContent = cipher.doFinal(content);
        return cipherContent;
    }

    /**
     *
     * @param cipherContent
     * @param privKey
     * @return decryptedContent
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public byte[] decryptBytes(byte[] cipherContent, Key privKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privKey);
        byte[] decryptedContent = cipher.doFinal(cipherContent);
        return decryptedContent;
    }

    /**
     *
     * @param bytes
     * @param privKey
     * @return messageArray
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    public byte[] decryption(ArrayList<byte[]> bytes, Key privKey) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        ArrayList<byte[]> decryptedByteList = new ArrayList<>();
        for (byte[] a : bytes) {
            byte[] decryptarr = decryptBytes(a, privKey);
            decryptedByteList.add(decryptarr);
        }
        byte[] messageArray = decryptedByteList.get(0);
        for (int i = 1; i < decryptedByteList.size(); i++) {
            messageArray = CipherUtility.combine(messageArray, decryptedByteList.get(i));
        }
        return messageArray;
    }

    /**
     *
     * @param byteFile
     * @param pubKey
     * @return encryptedByteList
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    public ArrayList<byte[]> encryption(byte[] byteFile, Key pubKey) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        ArrayList<byte[]> byteArrays = new ArrayList<>();

        for (int i = 0; i < byteFile.length; i += 117) {
            byte[] arr = Arrays.copyOfRange(byteFile, i, Math.min(byteFile.length,i+117));
            byteArrays.add(arr);
        }
        ArrayList<byte[]> encryptedByteList = new ArrayList<>();
        for (byte[] a : byteArrays) {
            byte[] encryptarr = encryptBytes(a, pubKey);
            encryptedByteList.add(encryptarr);
        }
        return encryptedByteList;
    }

    /**
     *
     * @param a
     * @param b
     * @return Combined results
     */
    public static byte[] combine(byte[] a, byte[] b) {
        int length = a.length + b.length;
        byte[] result = new byte[length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

    /**
     *
     * @param key
     * @return encodedKeyStr
     */
    public String encodeKey(Key key) {
        byte[] keyBytes = key.getEncoded();
        String encodedKeyStr = Base64.getEncoder().encodeToString(keyBytes);
        return encodedKeyStr;
    }

    /**
     *
     * @param keyStr
     * @return PublicKey
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public PublicKey decodePublicKey(String keyStr) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Base64.getDecoder().decode(keyStr);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey key = keyFactory.generatePublic(spec);
        return key;
    }

    /**
     *
     * @param keyStr
     * @return PrivateKey
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public PrivateKey decodePrivateKey(String keyStr) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Base64.getDecoder().decode(keyStr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        return key;
    }

    /**
     * Init Method
     */
    @PostConstruct
    public void init() {
        try {
            if(keyPairGenerator == null) keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024, random);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}

