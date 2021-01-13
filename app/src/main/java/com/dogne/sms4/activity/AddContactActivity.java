package com.dogne.sms4.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dogne.sms4.R;
import com.dogne.sms4.constant.Constant;
import com.dogne.sms4.firebase.Database;

public class AddContactActivity extends AppCompatActivity
{
    private EditText aTxtAliassid;
    private Button aBtnAddContact;
    private Button aBtnPublickey;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        windowSetUP();
        connect_layout();
        publickeyOnClick();
        addContactOnClick();
    }

    /**
     * _@purpose connects activity to layout
     */
    private void connect_layout()
    {
        aTxtAliassid = findViewById(R.id.add_contact_txt_aliassid);
        aBtnAddContact = findViewById(R.id.add_contact_btn_contact);
        aBtnPublickey = findViewById(R.id.add_contact_btn_public);
    }

    /**
     * _@purpose sets windows dimensions
     */
    public void windowSetUP()
    {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = (int) (dm.widthPixels * Constant.POPUP_CONTACT_WIDTH);
        int height = (int) (dm.heightPixels* Constant.POPUP_CONTACT_HEIGHT);

        getWindow().setLayout(width,height);
    }

    /**
     * _@purpose adds contact to user database
     */
    private  void addContactOnClick()
    {
        aBtnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  aliasSid = aTxtAliassid.getText().toString().replaceAll("\\s", "");
                if (!TextUtils.isEmpty(aliasSid))
                    Database.addContact(aliasSid,AddContactActivity.this);
            }
        });
    }

    /**
     * _@purpsose gets users public key
     */
    private void publickeyOnClick()
    {
        aBtnPublickey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String  aliasSid = aTxtAliassid.getText().toString();
                if (!TextUtils.isEmpty(aliasSid)) {
                    Database.getPublickey(aliasSid,AddContactActivity.this);
                }
            }
        });
    }
}