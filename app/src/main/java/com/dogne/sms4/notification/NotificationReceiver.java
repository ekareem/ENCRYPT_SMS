package com.dogne.sms4.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.dogne.sms4.constant.Constant;
import com.dogne.sms4.rsa.RSA;

public class NotificationReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        String message = intent.getStringExtra(Constant.MESSAGE);
        String privateKey = intent.getStringExtra(Constant.PRIVATEKEY);
        try
        {
            assert privateKey != null;
            message = RSA.decrypt(message,privateKey);
            Toast.makeText(context,message, Toast.LENGTH_LONG).show();
        }catch (Exception e)
        {
            Toast.makeText(context,Constant.STATUS_FAILURE_INVALID_DECRYPTION_KEY, Toast.LENGTH_LONG).show();
        }
    }
}
