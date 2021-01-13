package com.dogne.sms4.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dogne.sms4.R;
import com.dogne.sms4.constant.Constant;
import com.dogne.sms4.rsa.RSA;
import com.dogne.sms4.storage.Contact;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.Objects;

public class DecryptActivity extends AppCompatActivity
{

    private TextView aTxtFrom;
    private TextView aTxtTo;
    private TextView aTxtSendersPublicKey;
    private TextView aTxtCipher;
    private EditText aTxtKey;
    private TextView aTxtDate;
    private TextView aTxtSignature;
    private TextView aTxtAvatar;
    private Button aBtnDecrypt;
    private Button aBtnVerify;
    private ImageButton aBtnCopyFrom;
    private ImageButton aBtnCopyTo;
    private FloatingActionButton aBtnDelete;
    private FloatingActionButton aBtnReply;

    private String mMessageId;
    private String mMessageType;

    private ClipboardManager mClipboard;
    private String mSignature;

    private boolean mDecrypted;
    private boolean mVerified;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt);


        connect_layout();
        initializeVariable();
        fetch_data();

        copy_on_click();
        decrypt_on_click();
        deleteOnClick();
        replyOnClick();
        verifyOnClick();
        textChange();
    }


    /**
     * _@purpose initialize variable
     */
    private  void initializeVariable()
    {
        mMessageId = getIntent().getStringExtra(Constant.MESSAGEID);
        mMessageType = getIntent().getStringExtra(Constant.PUT_EXTRA_MESSAGE_TYPE);
        mClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        mDecrypted = false;
        mVerified = false;

        FirebaseFirestore.getInstance()
                .collection(Constant.USER)
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object> messages = documentSnapshot.getData();
                        assert messages != null;
                        //get user private key
                        String privateKey = (String) messages.get(Constant.PRIVATEKEY);
                        aTxtKey.setText(privateKey);
                    }
                });
    }

    /**
     * _@purpose connects activity to layout
     */
    private void connect_layout()
    {
        aTxtFrom = findViewById(R.id.decrypt_txt_from_aliassid);
        aTxtTo = findViewById(R.id.decrypt_txt_to_aliassid);
        aTxtSendersPublicKey = findViewById(R.id.decrypt_txr_signKey);
        aTxtCipher =findViewById(R.id.decrypt_txt_chiper);
        aTxtKey = findViewById(R.id.decrypt_txt_key);
        aTxtDate = findViewById(R.id.decrypt_txt_date);
        aTxtSignature = findViewById(R.id.decrypt_txt_signature);
        aTxtAvatar = findViewById(R.id.decrypt_txt_avatar);
        aBtnDecrypt = findViewById(R.id.decrypt_btn_decrypt);
        aBtnVerify = findViewById(R.id.decrypt_btn_verify);
        aBtnCopyFrom = findViewById(R.id.decrypt_btn_copy_from);
        aBtnCopyTo = findViewById(R.id.decrypt_btn_copy_to);
        aBtnDelete = findViewById(R.id.decrypt_btn_delete);
        aBtnReply = findViewById(R.id.decrypt_btn_reply);
    }

    /**
     * fetch message form database
     */
    @SuppressWarnings("unchecked")
    private void fetch_data()
    {
        FirebaseFirestore.getInstance()
                .collection(Constant.USER).document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .collection(Constant.LIST)
                .document(mMessageType)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot)
                    {
                        Map<String, Object> messages = documentSnapshot.getData();

                        Map<String, Object> message;
                        assert messages != null;
                        message = (Map<String, Object>) messages.get(mMessageId);
                        if(message != null) {

                            aTxtFrom.setText((String) message.get(Constant.FROMALIASSID));
                            aTxtTo.setText((String) message.get(Constant.TOALIASSID));
                            aTxtCipher.setText((String) message.get(Constant.MESSAGE));
                            mSignature = (String) message.get(Constant.FIELD_MESSAGE_SIGNATURE);
                            aTxtAvatar.setText(aTxtFrom.getText().toString().substring(0,1).toUpperCase());
                            aTxtSignature.setText((String)message.get(Constant.FIELD_MESSAGE_SIGNATURE));
                            aTxtDate.setText((String) message.get(Constant.MESSAGEID));
                        }
                        else
                        {
                            DecryptActivity.super.onBackPressed();
                        }
                    }
                });
    }

    /**
     * copy aliasFrom ot aliasTo
     */
    @SuppressWarnings("deprecation")
    private void copy_on_click()
    {
        aBtnCopyFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClipboard.setText(aTxtFrom.getText());
            }
        });
        aBtnCopyTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClipboard.setText(aTxtTo.getText());
            }
        });
    }

    private void decrypt_on_click()
    {
        aBtnDecrypt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v)
            {
                if(!TextUtils.isEmpty(aTxtKey.getText()))
                    decrypt(aTxtKey.getText().toString());
                else
                    decrypt();
            }
        });
    }

    /**
     * decrypt with users private key
     */
    private void decrypt()
    {
        FirebaseFirestore.getInstance()
                .collection(Constant.USER)
                .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object> messages = documentSnapshot.getData();
                        assert messages != null;
                        //get user private key
                        String privateKey = (String) messages.get(Constant.PRIVATEKEY);
                        decrypt(privateKey);
                    }
                });
    }

    /**
     * _@purpse decrypt with key provided
     * @param key key
     */
    @SuppressWarnings("deprecation")
    private void decrypt(String key)
    {
        if (!mDecrypted)
            try {
                String fromAliassid = RSA.decrypt(aTxtFrom.getText().toString(), key);
                String toAliassid = RSA.decrypt(aTxtTo.getText().toString(), key);
                String cipherTxt = RSA.decrypt(aTxtCipher.getText().toString(), key);
                String date = RSA.decrypt(aTxtDate.getText().toString(), key);

                aTxtFrom.setText(fromAliassid);
                aTxtTo.setText(toAliassid);
                aTxtCipher.setText(cipherTxt);
                aTxtDate.setText(date);

                FirebaseFirestore.getInstance().collection(Constant.CONTACT)
                        .document(aTxtFrom.getText().toString())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                Contact c = documentSnapshot.toObject(Contact.class);
                                if (c != null) {
                                    aTxtSendersPublicKey.setText(c.publicKey);
                                }
                            }
                        });
                mDecrypted = true;
                aBtnDecrypt.setBackgroundDrawable(getResources().getDrawable(R.drawable.success));
                aBtnDecrypt.setText(Constant.STATUS_SUCCESS_DECRYPT);
                Toast.makeText(DecryptActivity.this,Constant.STATUS_SUCCESS_DECRYPT, Toast.LENGTH_LONG).show();
                aTxtAvatar.setText(aTxtFrom.getText().toString().substring(0,1).toUpperCase());
            } catch (Exception e) {
                Toast.makeText(DecryptActivity.this,Constant.STATUS_FAILURE_INVALID_DECRYPTION_KEY, Toast.LENGTH_LONG).show();
                aBtnDecrypt.setBackgroundDrawable(getResources().getDrawable(R.drawable.failure));
            }
    }

    /**
     * _@purpose change button based on how user chooses to decrypt message
     */
    private void change_btn_txt()
    {
        if(!TextUtils.isEmpty(aTxtKey.getText()))
            aBtnDecrypt.setText(Constant.DIALOGUE_DECRYPT_KEY_PROVIDED);
        else
            aBtnDecrypt.setText(Constant.DIALOGUE_DECRYPT_PRIVATE_KEY);
    }

    /**
     * _@purpoes delete message
     */
    private void deleteOnClick()
    {
        aBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DocumentReference db = FirebaseFirestore.getInstance()
                        .collection(Constant.USER)
                        .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                        .collection(Constant.LIST)
                        .document(mMessageType);

                db.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String,Object> messages = documentSnapshot.getData();
                        assert messages != null;
                        messages.remove(mMessageId);

                        db.set(messages);
                        DecryptActivity.this.onBackPressed();
                        Toast.makeText(DecryptActivity.this,Constant.STATUS_SUCCESS_MESSAGE_DELETED,Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    /**
     * _@purpoes replays to the user
     */
    private void replyOnClick()
    {
        aBtnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DecryptActivity.this,EncryptActivity.class);
                if(mMessageType.equals(Constant.INBOX))
                    intent.putExtra(Constant.PUT_EXTRA_ENCRYPT_RECEIVER, aTxtFrom.getText().toString());
                else
                    intent.putExtra(Constant.PUT_EXTRA_ENCRYPT_RECEIVER, aTxtTo.getText().toString());
                startActivity(intent);
            }
        });
    }

    private void verifyOnClick()
    {
        aBtnVerify.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {
                if(!mVerified)
                    try {
                        RSA.verify(mSignature, aTxtSendersPublicKey.getText().toString());
                        aBtnVerify.setBackgroundDrawable(getResources().getDrawable(R.drawable.success));
                        aBtnVerify.setText(Constant.STATUS_SUCCESS_VERIFICATION);
                        Toast.makeText(DecryptActivity.this, Constant.STATUS_SUCCESS_VERIFICATION, Toast.LENGTH_LONG).show();
                        mVerified = true;
                    } catch (Exception e) {
                        Toast.makeText(DecryptActivity.this, Constant.STATUS_FAILURE_VERIFICATION, Toast.LENGTH_LONG).show();
                        aBtnVerify.setBackgroundDrawable(getResources().getDrawable(R.drawable.failure));
                    }
            }
        });
    }

    /**
     * _@purpose handles text change listeners
     */
    private void textChange()
    {
        aTxtKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                change_btn_txt();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                change_btn_txt();
            }

            @Override
            public void afterTextChanged(Editable s) {
                change_btn_txt();
            }
        });
    }
}
