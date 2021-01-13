package com.dogne.sms4.rsa;

import com.dogne.sms4.constant.Constant;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import android.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSA
{
    /**
     * Convert bas64publickey String to java.security.Key object
     * @param base64PublicKey
     * @return Key
     */
    private static Key getPublicKey(String base64PublicKey)
    {
        Key publicKey = null;
        try
        {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decode(base64PublicKey.getBytes(),Base64.DEFAULT));
            KeyFactory keyFactory = KeyFactory.getInstance(Constant.ENCRYPTION_TYPE);
            publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        }
        catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
        catch (InvalidKeySpecException e) { e.printStackTrace(); }

        return publicKey;
    }

    /**Convert bas64privatekey String to java.security.Key object
     * @param base64PrivateKey
     * @return Key
     */
    private static Key getPrivateKey(String base64PrivateKey)
    {
        Key privateKey = null;
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decode(base64PrivateKey.getBytes(),Base64.DEFAULT));
        KeyFactory keyFactory = null;

        try { keyFactory = KeyFactory.getInstance("RSA"); }
        catch (NoSuchAlgorithmException e) { e.printStackTrace(); }

        try { privateKey = keyFactory.generatePrivate(keySpec); }
        catch (InvalidKeySpecException e) { e.printStackTrace(); }

        return privateKey;
    }

    /**encryt a string using Key given
     * @param data
     * @param key
     * @return String
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     */
    public static String encrypt(String data, String key) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException
    {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        if(key.length() <Constant.PUBLICKEY_LENGTH) cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(key));
        else if(key.length() > Constant.PUBLICKEY_LENGTH) cipher.init(Cipher.ENCRYPT_MODE, getPrivateKey(key));
        return Base64.encodeToString(cipher.doFinal(data.getBytes()),Base64.DEFAULT);
    }

    /**decreyts an encryoted information
     *
     * @param data
     * @param key
     * @return String
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String decrypt(String data, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException
    {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        if(key.length() <Constant.PUBLICKEY_LENGTH) cipher.init(Cipher.DECRYPT_MODE,getPublicKey(key));
        else if(key.length() >Constant.PUBLICKEY_LENGTH) cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(key));
        return new String(cipher.doFinal(Base64.decode(data.getBytes(),Base64.DEFAULT)));
    }

    public static String sign(String privateKeyStr) throws SignatureException, InvalidKeyException, NoSuchAlgorithmException
    {
        PrivateKey privateKey =  (PrivateKey) getPrivateKey(privateKeyStr);
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(privateKey);

        byte[] signature = privateSignature.sign();

        return Base64.encodeToString(signature,Base64.DEFAULT);
    }

    public static boolean verify(String signature, String publicKeyStr) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException
    {
        PublicKey publicKey = (PublicKey) getPublicKey(publicKeyStr);
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(publicKey);

        byte[] signatureBytes = Base64.decode(signature,Base64.DEFAULT);

        return publicSignature.verify(signatureBytes);
    }
}
