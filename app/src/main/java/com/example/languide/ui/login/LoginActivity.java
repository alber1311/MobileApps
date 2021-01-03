package com.example.languide.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.languide.R;
import com.example.languide.ui.register.RegisterActivity;
import com.example.languide.ui.student.StudentMainActivity;
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

        loginButton.setOnClickListener(v -> {
            if(checkData()){
                login(emailEditText.getText().toString(),passwordEditText.getText().toString());
            }
        });

        registerTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
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
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                startActivity(new Intent(LoginActivity.this, StudentMainActivity.class));
            } else {
                Toast.makeText(LoginActivity.this, "User not found :(", Toast.LENGTH_SHORT).show();
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