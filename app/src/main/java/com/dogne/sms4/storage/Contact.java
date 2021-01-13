package com.dogne.sms4.storage;

import com.dogne.sms4.constant.Constant;

import java.util.HashMap;
import java.util.Map;

/**Alice SID********/

public class Contact
{
    public String aliasSid;
    public String uid;
    public String publicKey;

    public Contact()
    {

    }

    public Contact(String aliasSid, String uid, String publicKey)
    {
        this.aliasSid = aliasSid;
        this.uid = uid;
        this.publicKey = publicKey;
    }
}
