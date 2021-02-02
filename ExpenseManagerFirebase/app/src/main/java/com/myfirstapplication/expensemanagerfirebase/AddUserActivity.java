package com.myfirstapplication.expensemanagerfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AddUserActivity extends AppCompatActivity {

    public static boolean executionStatus = false;
    public static String methodType = "update";
    public static boolean progress = false;
    String id = "", name = "";
    FirebaseAuth auth;
    Intent intent;
    EditText edtEmail, edtPassword, edtConfirmPassword;
    ProgressBar progressBar;
    TextView textView;
    Button btnSave;


    private int updateID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        auth = FirebaseAuth.getInstance();
        getIDs();
        textView.setVisibility(View.INVISIBLE);

    }


    void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    public void saveButton(View view) {
        String pass = String.valueOf(edtPassword.getText());
        String confirm = String.valueOf(edtConfirmPassword.getText());
        if (pass.equals(confirm)) {
            String email = String.valueOf(edtEmail.getText()).trim();
            String password = String.valueOf(edtPassword.getText()).trim();
            textView.setVisibility(View.INVISIBLE);

            if (email.isEmpty()) {
                edtEmail.setError("Email is required");
                edtEmail.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edtEmail.setError("Please enter a valid email");
                edtEmail.requestFocus();
                return;
            }
            if (password.isEmpty()) {
                edtPassword.setError("Password is required");
                edtPassword.requestFocus();
                return;
            }
            if (password.length() < 6) {
                edtPassword.setError("Minimum Length of Password should be 6");
                edtPassword.requestFocus();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {

                        Toast.makeText(getApplicationContext(), "User Successfully Registered", Toast.LENGTH_LONG).show();
                        finish();


                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(getApplicationContext(), "User Already Registered", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(AddUserActivity.this, MainActivity.class);
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            if (firebaseUser != null) {
                                intent.putExtra("UserID", firebaseUser.getUid());
                                startActivity(intent);
                            } else
                                finish();

                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });

        } else {
            textView.setVisibility(View.VISIBLE);
        }
    }

    void getIDs() {
        edtEmail = findViewById(R.id.txtAdminEmail);
        edtPassword = findViewById(R.id.txtUserPassword);
        edtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        progressBar = findViewById(R.id.progressbar);
        textView = findViewById(R.id.txtNoMatch);
        btnSave = findViewById(R.id.btnSignUp);
    }

}

