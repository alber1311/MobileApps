package com.example.languide.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.languide.*;
import com.example.languide.ui.register.RegisterActivity;
import com.example.languide.ui.student.StudentMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        final TextView registerTextView = findViewById(R.id.idRegister);
        final Button loginButton = findViewById(R.id.idLogin);

        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkData()){
                    login(emailEditText.getText().toString(),passwordEditText.getText().toString());
                }
            }
        });

        registerTextView.setOnClickListener(new TextView.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(LoginActivity.this, StudentMainActivity.class));
        }
    }

    public void login(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(LoginActivity.this, StudentMainActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, "User not found :(", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Check if the data input is correct
    public boolean checkData(){
        if (!isEmail(emailEditText)) {
            emailEditText.setError("Enter a valid email!");
            return false;
        }
        if (!isPasswordValid(passwordEditText.getText().toString())) {
            passwordEditText.setError("Password must have at least 6 characters");
            return false;
        }
        return true;
    }

    //Checks if email is valid
    private boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}