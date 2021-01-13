package com.dogne.sms4.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dogne.sms4.R;
import com.dogne.sms4.firebase.Authenticate;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity
{
    private EditText aTxtEmail;
    private EditText aTxtPassword;
    private Button aBtnLogin;
    private androidx.appcompat.widget.Toolbar aToolbar;

    private FirebaseAuth.AuthStateListener fAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        onUserSignedIn();
        connectLayout();
        backOnClick();
        loginBtnOnClick();

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
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }
        };
    }

    /**
     * _@purpose connects activity to layout
     */
    private void connectLayout()
    {
        aTxtEmail = findViewById(R.id.login_txt_email);
        aTxtPassword = findViewById(R.id.login_txt_password);
        aBtnLogin = findViewById(R.id.login_btn_login);
        aToolbar = findViewById(R.id.login_toolbar);
    }

    /**
     * _@purpose onBack pressed go to HomeActivity
     */
    private void backOnClick()
    {
        aToolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LoginActivity.this.onBackPressed();
            }
        });
    }

    /**
     * _@purpose initiate login process
     */
    private void loginBtnOnClick()
    {
        aBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String email = aTxtEmail.getText().toString();
                String password = aTxtPassword.getText().toString();

                Authenticate.signIn(LoginActivity.this, email, password);
            }
        });
    }

    /**
     * _@purpose override on back pressed
     */
    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
    }
}
