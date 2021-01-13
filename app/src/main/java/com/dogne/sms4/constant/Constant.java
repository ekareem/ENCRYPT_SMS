package com.dogne.sms4.constant;

import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static android.content.ContentValues.TAG;

public class Constant
{
    /*_FIELD_CONSTANTS___________________________________________*/
    public static final String FIELD_USER_ALIAS = "alias";
    public static final String FIELD_USER_ALIASSID = "aliasSid";
    public static final String FIELD_USER_PRIVATE_KEY = "privateKey";
    public static final String FIELD_USER_PUBLIC_KEY = "publicKey";
    public static final String FIELD_USER_SID = "sid";
    public static final String FIELD_USER_UID = "uid";

    public static final String FIELD_MESSAGE_MID = "mid";
    public static final String FIELD_MESSAGE_MESSAGE = "message";
    public static final String FIELD_MESSAGE_RECEIVER = "receiver";
    public static final String FIELD_MESSAGE_SIGNATURE = "signature";

    public static final int FIELD_OTHER_STARTSID = 0;
    public static final int FIELD_OTHER_ENDSID = 0;
    public static final String FIELD_OTHER_DELIMETERSID = ":";
    /*END_FIELDS_CONSTANTS___________________________________________*/

    /*_SECURITY_CONSTANT_____________________________________________*/
    public static final String SECURITY_ENCRYPT_TYPE = "RSA";
    public static final String SECURITY_ENCRYPT_TYPE2 = "RSA/ECB/PKCS1Padding";
    public static final int SECURITY_ENCRYPT_BIT_LENGTH = 2048;

    public static final String SECURITY_HASH_TYPE = "SHA-1";
    public static final String SECURITY_HASH_TEXT = "0";
    public static final int SECURITY_HASH_SIGNUM = 1;
    public static final int SECURITY_HASH_RADIX = 16;
    public static final int SECURITY_HASH_BIT_LENGTH = 32;

    public static final int SECURITY_OTHER_PAIR = 2;
    public static final int SECURITY_OTHER_PUBLIC_TYPE = 0;
    public static final int SECURITY_OTHER_PRIVATE_TYPE = 1;
    public static final int SECURITY_OTHER_PUBLICKEY_LENGTH = 500;
    /*_END_SECURITY_CONSTANT_________________________________________*/

    /*_STATUS_CONSTANT_______________________________________________*/
    public static final String STATUS_FAILURE_AUTH_LOGIN = "LOGIN FAILED";
    public static final String STATUS_FAILURE_AUTH_LOGOUT = "LOGOUT FAILED";
    public static final String STATUS_FAILURE_AUTH_REGISTER = "SIGNUP FAILED";
    public static final String STATUS_FAILURE_AUTH_USER_DELETION = "DELETION FAILED";
    public static final String STATUS_FAILURE_USER = "USER NOT FOUND";
    public static final String STATUS_FAILURE_EXTERNAL_STORAGE = "PERMISSION DENIED";
    public static final String STATUS_FAILURE_NO_USER = "NO RECEIVER";
    public static final String STATUS_FAILURE_NO_MESSAGE = "NO MESSAGE";
    public static final String STATUS_FAILURE_INVALID_DECRYPTION_KEY = "INVALID DECRYPTION KEY";
    public static final String STATUS_FAILURE_INVALID_ENCRYPTION_KEY = "INVALID ENCRYPTION KEY";
    public static final String STATUS_FAILURE_VERIFICATION = "SIGNATURE VERIFICATION FAILED";

    public static final String STATUS_SUCCESS_AUTH_LOGOUT = "LOGOUT SUCCESSFUL";
    public static final String STATUS_SUCCESS_AUTH_REGISTER = "SIGN UP SUCCESSFUL";
    public static final String STATUS_SUCCESS_AUTH_USER_DELETION = "DELETION SUCCESSFUL";
    public static final String STATUS_SUCCESS_USER = "USER FOUND";
    public static final String STATUS_SUCCESS_CONTACT_ADDED = "CONTACT SUCCESSFULLY ADDED";
    public static final String STATUS_SUCCESS_EXTERNAL_STORAGE = "PERMISSION GRANTED";
    public static final String STATUS_SUCCESS_MESSAGE_SENT = "MESSAGE SIGNED AND SENT";
    public static final String STATUS_SUCCESS_MESSAGE_DELETED = "MESSAGE SUCCESSFULLY DELETED";
    public static final String STATUS_SUCCESS_VERIFICATION = "SIGNATURE VERIFIED";
    public static final String STATUS_SUCCESS_DECRYPT = "MESSAGE DECRYPTED";

    public static final String STATUS_REQUEST_EXTERNAL_STORAGE = "EXTERNAL STORAGE NEEDED";

    public static final String STATUS_OPTION_OK = "OK";
    public static final String STATUS_OPTION_CANCLE = "CANCLE";
    public static final String STATUS_OPTION_YES = "YES";
    public static final String STATUS_OPTION_NO = "NO";

    public static final String STATUS_OTHER_EMPTY_FIELDS = "FIELDS ARE EMPTY";
    public static final String STATUS_OTHER_MATCH_PASSWORD = "PASSWORD MUST MATCH";
    public static final String STATUS_OTHER_ALIAS_TOO_SMALL = "ALIAS MUST BE 5 LENGHT OR GREATER";






    /*_END_STATUS_CONSTANT_____________________________________________*/

    /*_STORAGE_CONSTANT________________________________________________*/
    public static String STORAGE_DOCUMENT_INBOX = "inbox";
    public static String STORAGE_DOCUMENT_OUTBOX = "outbox";
    public static String STORAGE_DOCUMENT_CONTACT = "contact";
    public static String STORAGE_DOCUMENT_UID = "uid";

    public static String STORAGE_COLLECTION_USER = "user";
    public static String STORAGE_COLLECTION_CONTACT = "contact";
    public static String STORAGE_COLLECTION_LIST = "list";
    /*_END_STORAGE_CONSTANT_____________________________________________*/

    /*_OTHER_CONSTANT___________________________________________________*/
    public static final double POPUP_CONTACT_WIDTH = .7;
    public static final double POPUP_CONTACT_HEIGHT = .7;
    public static final String PUT_EXTRA_ENCRYPT_RECEIVER = "receiver";
    public static final String PUT_EXTRA_MESSAGE_TYPE = "MESSAGE_TYPE";
    public static final String PUT_EXTRA_DECRYPT_FROM = "FROM";
    public static final String PUT_EXTRA_DECRYPT_TO = "TO";
    public static final String DELIMITER_SPACE = " ";
    public static final String DELIMITER_COMMA = ",";
    /*_END_OTHER_CONSTATN_______________________________________________*/

    /*_DIALOUG_________________________________________________________*/
    public static final String DIALOGUE_CONFIRM_LOGOUT = "CONFIRM LOGOUT?";
    public static final String DIALOGUE_CONFIRM_DELETION = "ARE YOU SURE YOU WANT TO DELETE ACCOUNT";
    public static final String DIALOGUE_ENCRYPT_RELIEVER_PUBLICKEY = "ENCRYPT WITH RECEIVER PUBLIC KEY";
    public static final String DIALOGUE_ENCRYPT_KEY_PROVIDED = "ENCRYPT WITH KEY PROVIDED";
    public static final String DIALOGUE_DECRYPT_KEY_PROVIDED = "DECRYPT WITH KEY PROVIDED";
    public static final String DIALOGUE_DECRYPT_PRIVATE_KEY = "DECRYPT WITH YOUR PRIVATE KEY";
    /*_END_DIALOUG_________________________________________________________*/

    public static final String ENCRYPTION_TYPE = "RSA";
    public static final int BIT_LENGTH = 2048;
    public static final int PUBLIC_TYPE = 0;
    public static final int PAIR = 2;
    public static final int PRIVATE_TYPE = 1;
    public static final int PUBLICKEY_LENGTH = 500;

    public static final int STARTSID = 0;
    public static final int ENDSID = 6;
    public static final String DELIMETERSID = ":";

    public static final String USER = "user";
    public static final String UID = "uid";
    public static final String SID = "sid";
    public static final String ALIAS = "alias";
    public static final String ALIASSID = "aliasSid";
    public static final String EMAIL = "email";
    public static final String PUBLICKEY = "publicKey";
    public static final String PRIVATEKEY = "privateKey";
    public static final String INBOX = "inbox";
    public static final String OUTBOX = "outbox";
    public static final String CONTACT = "contact";
    public static final String IMAGE = "image";

    public static final String FROMALIASSID = "fromAliasSid";
    public static final String TOALIASSID = "toAliasSid";
    public static final String MESSAGE = "message";
    public static final String MESSAGEID = "id";
    public static final String AVATAR = "avatar";

    public static final String REGISTER_AVATAR= "register_avatar";
    public static final String DEFAULT_AVATAR = "avatar_00";

    public static final String USER_CREATED_SUCCESS = "User successfully creaated";
    public static final String USER_DELETION_SUCCESS = "User successfully deleted";
    public static final String USER_DELETION_FAILURE = "User deletion error";
    public static final String LOGIN_SUCCESS = "Login success";
    public static final String CONTACT_SUCCESS = "User added to contact";
    public static final String CONTACT_FAILURE = "User not found";
    public static final String PUBLICKEY_SUCCESS = "Public Key added to clip board";

    public static final String LIST = "list";

    public static void print(String text)
    {
        Log.e(TAG,text);
    }
    public static void toast (AppCompatActivity appCompatActivity, String message,int LENGTH)
    {
        if (LENGTH != Toast.LENGTH_LONG || LENGTH != Toast.LENGTH_SHORT)
        {
            LENGTH = Toast.LENGTH_SHORT;
        }
        Toast.makeText(appCompatActivity, message, LENGTH).show();
    }
}
