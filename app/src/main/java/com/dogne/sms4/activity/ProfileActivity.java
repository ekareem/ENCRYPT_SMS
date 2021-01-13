package com.dogne.sms4.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dogne.sms4.R;
import com.dogne.sms4.constant.Constant;
import com.dogne.sms4.firebase.Database;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private ImageButton aBtnAlias;
    private ImageButton aBtnPublic;
    private ImageButton aBtnPrivate;

    private TextView aTxtAlias;
    private TextView aTxtPublic;
    private TextView aTxtPrivate;

    private TextView aTxtAvatar;

    private Button aBtnLogout;
    private Button aBtnDelete;

    private FirebaseAuth.AuthStateListener fAuthListener;
    private String mAliasSid;

    private ClipboardManager mClipboard;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initializeVariable();
        onUserSignedIn();
        connectLayout();
        fetchUserData();

        aliasOnClick();
        deleteOnClick();
        logoutOnClick();
        publicOnClick();
        privateOnClick();
    }

    /**
     * _@purpose defines what happens when Login activity is started
     */
    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(fAuthListener);
    }

    private void initializeVariable()
    {
        mClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
    }

    /**
     * _@purpose defines happens when user is logout or deleted sided in.
     * go to main activity
     */
    private void onUserSignedIn()
    {
        fAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                }
            }
        };
    }

    /**
     * _@purpose connects activity to layout
     */
    private void connectLayout()
    {

        aBtnAlias = this.findViewById(R.id.profile_btn_aliassid);
        aBtnPublic = findViewById(R.id.profile_btn_public);
        aBtnPrivate = findViewById(R.id.profile_btn_private);

        aTxtAlias = this.findViewById(R.id.profile_txt_aliassid);
        aTxtPublic = findViewById(R.id.profile_txt_public);
        aTxtPrivate = findViewById(R.id.profile_txt_private);

        aTxtAvatar = findViewById(R.id.profile_txt_avatar);

        aBtnLogout = findViewById(R.id.profile_btn_logout);
        aBtnDelete = findViewById(R.id.profile_btn_delete);
    }

    /**
     * _@purpose fetch userdata and set layout values
     */
    private void fetchUserData()
    {
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            String uid = FirebaseAuth.getInstance().getUid();
            assert uid != null;
            DocumentReference docRef = FirebaseFirestore.getInstance().collection(Constant.USER).document(uid);
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Map<String, Object> user = documentSnapshot.getData();

                    //get user data
                    if(user != null) {
                        mAliasSid = (String) user.get(Constant.ALIASSID);
                        aTxtAlias.setText((String) user.get(Constant.ALIASSID));
                        aTxtPublic.setText((String) user.get(Constant.PUBLICKEY));
                        aTxtPrivate.setText((String) user.get(Constant.PRIVATEKEY));
                        aTxtAvatar.setText(mAliasSid.substring(0,1).toUpperCase());
                    }
                }
            });
        }
    }

    /**
     * _@purpose copy aliassid txt
     */
    @SuppressWarnings("deprecation")
    private void aliasOnClick()
    {
        aBtnAlias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mClipboard != null)
                    mClipboard.setText(aTxtAlias.getText());
            }
        });
    }

    /**
     * _@purpose delete user account
     */
    private void deleteOnClick()
    {
        aBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                new AlertDialog.Builder(ProfileActivity.this)
                        .setMessage(Constant.DIALOGUE_CONFIRM_DELETION)
                        .setCancelable(false)
                        .setPositiveButton(Constant.STATUS_OPTION_YES, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Database.onDeleteUser(mAliasSid);
                            }
                        })
                        .setNegativeButton(Constant.STATUS_OPTION_NO, null)
                        .show();
            }
        });
    }

    /**
     * _@purpose user logs out
     */
    private void logoutOnClick()
    {
        aBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                new AlertDialog.Builder(ProfileActivity.this)
                        .setMessage(Constant.DIALOGUE_CONFIRM_LOGOUT)
                        .setCancelable(false)
                        .setPositiveButton(Constant.STATUS_OPTION_YES, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseAuth.getInstance().signOut();
                            }
                        })
                        .setNegativeButton(Constant.STATUS_OPTION_NO, null)
                        .show();
            }
        });
    }

    /**
     * _@purpose copies the public key
     */
    @SuppressWarnings("deprecation")
    private void publicOnClick()
    {
        aBtnPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mClipboard != null)
                    mClipboard.setText(aTxtPublic.getText());
            }
        });
    }

    /**
     * _@purpose copies the private key
     */
    @SuppressWarnings("deprecation")
    private void privateOnClick()
    {
        aBtnPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mClipboard != null)
                    mClipboard.setText(aTxtPrivate.getText());
            }
        });
    }
}
