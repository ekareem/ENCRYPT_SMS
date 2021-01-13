package com.dogne.sms4.firebase;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.dogne.sms4.constant.Constant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import static android.content.ContentValues.TAG;

public class Authenticate
{

    public static void signUp(final AppCompatActivity authActivity, final String alias, final String email, String password)
    {
        final FirebaseAuth auth = FirebaseAuth.getInstance();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(alias))
            Toast.makeText(authActivity, "Fields are empty", Toast.LENGTH_LONG).show();

        else
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(authActivity, new OnCompleteListener<AuthResult>()
                    {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(authActivity,Constant.USER_CREATED_SUCCESS, Toast.LENGTH_SHORT).show();
                                Database.onUserCreated(authActivity,alias,email);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(authActivity, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

    }

    public static void signIn(final AppCompatActivity authActivity, final String email, final String password)
    {
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
            Toast.makeText(authActivity, "Fields are empty", Toast.LENGTH_LONG).show();
        else
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(authActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(authActivity, Constant.LOGIN_SUCCESS,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "Log in failure", );
                            Toast.makeText(authActivity, Objects.requireNonNull(task.getException()).getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
