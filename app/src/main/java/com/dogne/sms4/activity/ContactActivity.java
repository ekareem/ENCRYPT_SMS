package com.dogne.sms4.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dogne.sms4.R;
import com.dogne.sms4.constant.Constant;
import com.dogne.sms4.storage.Contact;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.Objects;

public class ContactActivity extends AppCompatActivity {

    private TextView aImgAvatar;
    private TextView aTxtAliassid;
    private TextView aTxtPublic;
    private ImageButton aBtnAliassid;
    private ImageButton aBtnPublic;
    private FloatingActionButton aBtnDelete;
    private FloatingActionButton aBtnMsg;

    private ClipboardManager mClipboard;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        initializeVariable();
        WindowSetUP();
        connect_layout();

        aliassidOnClick();
        deleteOnClick();
        fetchUserData();
        msgOnClick();
        publicOnClick();
    }

    private void initializeVariable()
    {
        mClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
    }

    /**
     * _@purpose sets windows dimensions
     */
    public void WindowSetUP()
    {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = (int) (dm.widthPixels * Constant.POPUP_CONTACT_WIDTH);
        int height = (int) (dm.heightPixels* Constant.POPUP_CONTACT_HEIGHT);

        getWindow().setLayout(width,height);
    }

    /**
     * _@purpose connects activity to layout
     */
    public void connect_layout()
    {
        aImgAvatar = findViewById(R.id.contact_avatar_img);
        aTxtAliassid = findViewById(R.id.profile_txt_aliassid);
        aBtnAliassid = findViewById(R.id.profile_btn_aliassid);
        aTxtPublic = findViewById(R.id.profile_txt_private);
        aBtnPublic = findViewById(R.id.profile_btn_public);
        aBtnDelete = findViewById(R.id.contact_delete_btn);
        aBtnMsg = findViewById(R.id.contact_msg_btn);
    }

    /**
     * _@purpose copy aliassid txt
     */
    @SuppressWarnings("deprecation")
    public void aliassidOnClick()
    {
        aBtnAliassid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClipboard.setText(aTxtAliassid.getText());
            }
        });
    }

    /**
     * _@purpose deletes contact
     */
    public void deleteOnClick()
    {
        aBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ContactActivity.this)
                        .setMessage("Are you sure you want to remove user from contact")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteContact();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }


    /**
     * _@purpose fetch userdata and set layout values
     */
    private void fetchUserData()
    {
        FirebaseFirestore.getInstance().collection(Constant.CONTACT)
                .document(Objects.requireNonNull(getIntent().getStringExtra(Constant.ALIASSID)))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Contact c = documentSnapshot.toObject(Contact.class);
                        if(c != null) {
                            aTxtAliassid.setText(c.aliasSid);
                            aTxtPublic.setText(c.publicKey);
                            aImgAvatar.setText(c.aliasSid.substring(0,1).toUpperCase());
                        }
                    }
                });
    }

    /**
     * _@purpose start an Encryption activity
     */
    public void msgOnClick()
    {
        aBtnMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactActivity.this,EncryptActivity.class);
                intent.putExtra(Constant.PUT_EXTRA_ENCRYPT_RECEIVER, aTxtAliassid.getText().toString());
                startActivity(intent);
            }
        });
    }

    /**
     * _@purpose copies the public key
     */
    @SuppressWarnings("deprecation")
    public void publicOnClick()
    {
        aBtnPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClipboard.setText(aTxtPublic.getText());
            }
        });
    }

    /**
     * _@purpose delete contact
     */
    private void deleteContact()
    {
        final DocumentReference db = FirebaseFirestore.getInstance().collection(Constant.USER).document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .collection(Constant.LIST).document(Constant.CONTACT);
        db.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String,Object> contacts = documentSnapshot.getData();
                if(contacts != null) {
                    contacts.remove(aTxtAliassid.getText().toString());

                    db.set(contacts);
                    ContactActivity.this.onBackPressed();
                    Toast.makeText(ContactActivity.this,Constant.STATUS_SUCCESS_AUTH_USER_DELETION,Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}