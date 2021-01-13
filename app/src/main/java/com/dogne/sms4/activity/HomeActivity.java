package com.dogne.sms4.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dogne.sms4.R;

public class HomeActivity extends AppCompatActivity
{

    private Button aBtnLogin;
    private Button aBtnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        connectLayout();
        onClickMethod();
    }

    /**
     * connects varables to layout
     */
    private void connectLayout()
    {
        aBtnLogin = findViewById(R.id.home_btn_login);
        aBtnRegister = findViewById(R.id.home_btn_reg);
    }

    /**
     * _@purpose handles all onclick methods
     */
    private void onClickMethod()
    {
        loginOnClick();
        registerOnClick();
    }

    /**
     * _@purpose login button on click listener
     */
    private void loginOnClick()
    {
        aBtnLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(HomeActivity.this,LoginActivity.class));
            }
        });
    }

    /**
     * _purpose register button on click listener
     */
    private void registerOnClick()
    {
        aBtnRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(HomeActivity.this,RegisterActivity.class));
            }
        });
    }
}
