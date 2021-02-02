package com.myfirstapplication.expensemanagerfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityUserLogin extends AppCompatActivity {

    public static String AdminID, Username;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        mAuth = FirebaseAuth.getInstance();

        Button button = findViewById(R.id.btnLogin);
        final EditText textViewUsername, textViewPassword;
        textViewUsername = findViewById(R.id.txtUserName);
        textViewPassword = findViewById(R.id.txtUserPassword);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email = String.valueOf(textViewUsername.getText()).trim();
                String password = String.valueOf(textViewPassword.getText()).trim();

                if (email.isEmpty()) {
                    textViewUsername.setError("Email is required");
                    textViewUsername.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    textViewUsername.setError("Please enter a valid email");
                    textViewPassword.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    textViewPassword.setError("Password is required");
                    textViewPassword.requestFocus();
                    return;
                }
                if (password.length() < 6) {
                    textViewPassword.setError("Minimum Length of Password should be 6");
                    textViewPassword.requestFocus();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "User Login Successful", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ActivityUserLogin.this, MainActivity.class);
                            intent.putExtra("UserID", mAuth.getCurrentUser());
                            startActivity(intent);
                        } else
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            Intent intent = new Intent(ActivityUserLogin.this, MainActivity.class);
            intent.putExtra("UserID", firebaseUser.getUid());
            startActivity(intent);
        } else {
            Intent intent = new Intent(ActivityUserLogin.this, AddUserActivity.class);
            startActivity(intent);
        }
    }

    public void createNewAccount(View view) {
        Intent intent = new Intent(ActivityUserLogin.this, AddUserActivity.class);
        startActivity(intent);
    }
}
