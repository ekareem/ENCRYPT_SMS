package com.dogne.sms4.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dogne.sms4.R;
import com.dogne.sms4.adapter.EncryptPageAdapter;
import com.dogne.sms4.constant.Constant;
import com.dogne.sms4.rsa.RSA;
import com.dogne.sms4.storage.Contact;
import com.dogne.sms4.storage.Message;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;

public class EncryptActivity extends AppCompatActivity
{
    private EditText aTxtReceiver;
    private EditText aTxtKey;
    private EditText aTxtSms;
    private EditText aTxtSign;
    private FloatingActionButton aBtnSend;

    private TabLayout aTabLayout;
    private ViewPager aViewPager;

    private String privateKey;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt);


        connect_layout();
        initializeVariable();
        memberSetup();
        sendOnClick();
        tabFunctionality();
        receiverListener();
    }

    private void initializeVariable()
    {
        //get private key
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
                        privateKey = (String) messages.get(Constant.PRIVATEKEY);
                        aTxtSign.setText(privateKey);

                    }
                });


    }
    /**
     * _@purpose connects activity to layout
     */
    private void connect_layout()
    {
        aTxtReceiver = findViewById(R.id.encrypt_txt_receiver);
        aTxtKey = findViewById(R.id.encrypt_txt_key);
        aTxtSms = findViewById(R.id.encrypt_txt_sms);
        aTxtSign = findViewById(R.id.encrypt_txt_sign);
        aBtnSend = findViewById(R.id.encrypt_btn_send);
        aTabLayout =  findViewById(R.id.encrypt_tab_layout);
        aViewPager = findViewById(R.id.encrypt_view_pager);
    }

    /**
     * _@purpose sets up variable initial states
     */
    private void memberSetup()
    {
        aTabLayout.addTab(aTabLayout.newTab().setText(Constant.CONTACT));
        aTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        EncryptPageAdapter PageAdapter = new EncryptPageAdapter(getSupportFragmentManager(), aTabLayout.getTabCount(), this);
        aViewPager.setAdapter(PageAdapter);
        aViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(aTabLayout));

        aTxtReceiver.setText(getIntent().getStringExtra(Constant.PUT_EXTRA_ENCRYPT_RECEIVER));

        if(aTxtReceiver.getText().toString().length() != 0)
            receiverPublicKey(aTxtReceiver.getText().toString());
    }

    /**
     * sends on click
     */
    private void sendOnClick()
    {
        aBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                sendMessage();
            }
        });
    }

    /**
     * sends message to receiver and update inbox and outbox
     */
    private void sendMessage()
    {
        //if no user inserted
        if(TextUtils.isEmpty(aTxtReceiver.getText()))
            Toast.makeText(EncryptActivity.this,Constant.STATUS_FAILURE_NO_USER,Toast.LENGTH_LONG).show();

        //if no message inserted
        else if(TextUtils.isEmpty(aTxtSms.getText()))
            Toast.makeText(EncryptActivity.this,Constant.STATUS_FAILURE_NO_MESSAGE,Toast.LENGTH_LONG).show();

        else
            send();
    }

    /**
     * sends message to receiver and update inbox and outbox
     */
    private void send()
    {
        final String aliasSid = aTxtReceiver.getText().toString();

        StringTokenizer receivers = new StringTokenizer(aliasSid, Constant.DELIMITER_COMMA);

        while (receivers.hasMoreTokens())
        {
            try {
                String receiver = receivers.nextToken();
                sendMessage(receiver);
            } catch(Exception ignored) {
                Toast.makeText(EncryptActivity.this,Constant.STATUS_FAILURE_USER +Constant.DELIMITER_SPACE + receivers,Toast.LENGTH_LONG).show();
                aBtnSend.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.notVerified)));
            }
        }
    }

    private void receiverPublicKey(String receiver)
    {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(Constant.CONTACT).document(receiver);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null) {
                    aTxtReceiver.setTextColor(getResources().getColor(R.color.verified));
                    final Contact contact = documentSnapshot.toObject(Contact.class);

                    if (contact != null) {
                        FirebaseFirestore.getInstance().collection(Constant.USER).document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                //get receivers information
                                Map<String, Object> user = documentSnapshot.getData();
                                aTxtKey.setText(contact.publicKey);
                            }
                        });
                    }
                    else{
                        aTxtReceiver.setTextColor(getResources().getColor(R.color.notVerified));
                    }
                }
            }
        });
    }

    private void receiverListener()
    {
        aTxtReceiver.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(aTxtReceiver.getText().toString().length() != 0)
                    receiverPublicKey(aTxtReceiver.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    /**
     * _@purpose send message to inserted receiver
     * @param receiver reciver
     */
    private void sendMessage(String receiver)
    {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(Constant.CONTACT).document(receiver);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null) {
                    final Contact contact = documentSnapshot.toObject(Contact.class);

                    if (contact != null) {
                        FirebaseFirestore.getInstance().collection(Constant.USER).document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                //get receivers information
                                Map<String, Object> user = documentSnapshot.getData();

                                assert user != null;
                                String aliasSid = (String) user.get(Constant.ALIASSID);
                                String receiverUid = contact.uid;
                                String receiverAliasSid = contact.aliasSid;

                                //get Encrytion Key
                                String EncryptionKey;

                                EncryptionKey = aTxtKey.getText().toString();

                                //get Message
                                String message = aTxtSms.getText().toString();

                                //insert messange to sender outbox
                                sendOutbox(receiverUid, EncryptionKey, aliasSid, receiverAliasSid, message);
                                Toast.makeText(EncryptActivity.this, Constant.STATUS_SUCCESS_MESSAGE_SENT, Toast.LENGTH_LONG).show();
                                aBtnSend.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.verified)));
                            }
                        });

                    } else {
                        Toast.makeText(EncryptActivity.this, Constant.STATUS_FAILURE_USER, Toast.LENGTH_LONG).show();
                        aBtnSend.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.notVerified)));
                    }
                }
            }
        });
    }


    /**
     * _@purpose send message to senders outbox
     * @param receiverUid receiver
     * @param encryptionKey key
     * @param fromAliasSid sender
     * @param toAliasSid receiver
     * @param sms message
     */
    private void sendOutbox(final String receiverUid, final String encryptionKey, final String fromAliasSid, final String toAliasSid, final String sms)
    {
        final DocumentReference db = FirebaseFirestore.getInstance()
            .collection(Constant.USER)
            .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
            .collection(Constant.LIST)
            .document(Constant.OUTBOX);
            db.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                Map<String, Object> outbox;

                //get sender outbox and insert the new message
                if (documentSnapshot.getData() != null)
                    outbox = documentSnapshot.getData();
                else
                    outbox = new HashMap<>();

                try {
                    //create message object
                    String signatureKey = aTxtSign.getText().toString();
                    Message message = new Message(fromAliasSid, toAliasSid, sms, encryptionKey,signatureKey);
                    String mid = message.id;
                    message.id = RSA.encrypt(message.id,encryptionKey);
                    //insert message to outbox
                    outbox.put(message.id,message);
                    //update database
                    db.set(outbox);

                    //send message to receiver inbox
                    sendInbox(receiverUid,message);
                } catch(Exception e) {
                    Toast.makeText(EncryptActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    aBtnSend.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.notVerified)));
                }
            }
        });
    }

    /**
     * _@purpose sends message to receivers inbox
     * @param receiverUID receiver id
     * @param message message
     */
    private void sendInbox(String receiverUID, final Message message)
    {
        final DocumentReference db = FirebaseFirestore.getInstance()
                .collection(Constant.USER).document(receiverUID)
                .collection(Constant.LIST).document(Constant.INBOX);

        db.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                Map<String, Object> inbox;

                //get receiver inbox and insert the new message
                if (documentSnapshot.getData() != null)
                    inbox = documentSnapshot.getData();
                else
                    inbox = new HashMap<>();

                //send message to receiver inbox
                inbox.put(message.id,message);
                db.set(inbox);
            }
        });
    }

    /**
     * _@purpose tab Functionality
     */
    private void tabFunctionality()
    {
        aTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                aViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    /**
     * _@purpse get aTxtReceiver
     * @return EditText
     */
    public EditText getaTxtReceiver()
    {
        return aTxtReceiver;
    }


}
