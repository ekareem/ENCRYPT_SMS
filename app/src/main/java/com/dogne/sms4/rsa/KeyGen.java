package com.dogne.sms4.rsa;

import android.os.Build;

import com.dogne.sms4.constant.Constant;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import android.util.Base64;

public class KeyGen
{
    /**
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String [] generateKeyPair() throws NoSuchAlgorithmException
    {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(Constant.BIT_LENGTH);

        KeyPair keyPair = generator.genKeyPair();
        String publicKey = Base64.encodeToString(keyPair.getPublic().getEncoded(),Base64.DEFAULT);
        String privateKey = Base64.encodeToString(keyPair.getPrivate().getEncoded(),Base64.DEFAULT);

        String [] KeyPair = new String[Constant.PAIR];
        KeyPair[Constant.PUBLIC_TYPE] = publicKey;
        KeyPair[Constant.PRIVATE_TYPE] = privateKey;

        return KeyPair;
    }
}
