package com.dogne.sms4.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dogne.sms4.R;
import com.dogne.sms4.constant.Constant;
import com.dogne.sms4.firebase.Authenticate;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity
{
    private EditText aTxtAlias;
    private EditText aTxtEmail;
    private EditText aTxtPassword;
    private Button aBtnRegister;
    private EditText aTxtConfirm;

    private androidx.appcompat.widget.Toolbar aToolbar;

    private FirebaseAuth.AuthStateListener fAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        onUserSignedIn();
        connectLayout();
        backOnClick();
        registerOnClick();
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

    /**
     * _@purpose defines happens when user is successfully sided in.
     * go to main activity
     */
    private void onUserSignedIn()
    {
        fAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                }
            }
        };
    }

    /**
     * _@purpose connects activity to layout
     */
    private void connectLayout()
    {
        aToolbar = findViewById(R.id.register_toolbar);
        aTxtAlias = findViewById(R.id.register_txt_alias);
        aTxtEmail = findViewById(R.id.register_txt_email);
        aTxtPassword = findViewById(R.id.register_txt_password);
        aBtnRegister = findViewById(R.id.register_btn_register);
        aTxtConfirm = findViewById(R.id.register_txt_confirm);
    }

    /**
     * _@purpose onBack pressed go to HomeActivity
     */
    private void backOnClick()
    {
        aToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.super.onBackPressed();
            }
        });
    }

    /**
     * _@purpose initiate registration process
     */
    private void registerOnClick()
    {
        aBtnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String alias = aTxtAlias.getText().toString().replaceAll("\\s", "");
                String email = aTxtEmail.getText().toString();
                String password = aTxtPassword.getText().toString();
                String confirm = aTxtConfirm.getText().toString();

                //alias length is less then 5 don't register user
                if(alias.length() < 5)
                {
                    Toast.makeText(RegisterActivity.this,Constant.STATUS_OTHER_ALIAS_TOO_SMALL,Toast.LENGTH_LONG).show();
                    return;
                }

                //if pass words don't match don't register user
                if(password.equals(confirm))
                    Authenticate.signUp(RegisterActivity.this,alias,email,password);
                else
                    Toast.makeText(RegisterActivity.this,Constant.STATUS_OTHER_MATCH_PASSWORD,Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * _@purpose override on back pressed
     */
    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
    }
}
