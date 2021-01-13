package com.dogne.sms4.storage;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.dogne.sms4.constant.Constant;
import com.dogne.sms4.rsa.RSA;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**MESSAGE STRUCTURE***/

public class Message
{
    public String fromAliasSid;
    public String toAliasSid;
    public String message;
    public String id;
    public String signature;

    public Message()
    {
    }

    public Message(String fromAliasSid, String toAliasSid, String message,String key, String privateKey) throws IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, SignatureException
    {
        SimpleDateFormat s = new SimpleDateFormat("yyyy/MM/dd|hh:mm:ss:SS");
        this.fromAliasSid = RSA.encrypt(fromAliasSid,key);
        this.toAliasSid = RSA.encrypt(toAliasSid,key);
        this.message = RSA.encrypt(message,key);
        this.id =  s.format(new Date());
        this.signature = RSA.sign(privateKey);
    }
}
