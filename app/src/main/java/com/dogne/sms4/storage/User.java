package com.dogne.sms4.storage;

import android.os.Build;


import com.dogne.sms4.constant.Constant;
import com.dogne.sms4.rsa.KeyGen;

import java.security.NoSuchAlgorithmException;

import java.util.HashMap;
import java.util.Map;

/**USER PROFILE **/

public class User
{
    public String uid;
    public String sid;
    public String alias;
    public String aliasSid;
    public String email;
    public String publicKey;
    public String privateKey;

    public User()
    {
    }

    /**
     * Constructor for creating brannw user
     *
     * @param uid
     * @param alias
     * @param email
     */

    public User(String uid, String alias, String email) throws NoSuchAlgorithmException
    {
        this.uid = uid;
        this.alias = alias;
        this.email = email;

        setSID();
        setAliasSID();

        String[] keyPair = KeyGen.generateKeyPair();
        publicKey = keyPair[Constant.PUBLIC_TYPE];
        privateKey = keyPair[Constant.PRIVATE_TYPE];
    }

    private void setSID()
    {
        sid = uid.substring(Constant.STARTSID,Constant.ENDSID);
    }

    private void setAliasSID()
    {
        aliasSid = alias + Constant.DELIMETERSID + sid;
    }
}

