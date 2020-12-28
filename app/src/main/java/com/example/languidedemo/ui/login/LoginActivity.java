package com.example.languidedemo.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.languidedemo.*;
import com.example.languidedemo.database.DatabaseAccess;
import com.example.languidedemo.ui.register.RegisterActivity;
import com.example.languidedemo.ui.student.StudentMainActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText emailEditText = findViewById(R.id.email);
        final EditText passwordEditText = findViewById(R.id.password);
        final TextView registerTextView = findViewById(R.id.idRegister);
        final Button loginButton = findViewById(R.id.idLogin);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                if(!login(emailEditText.getText().toString(),
                        passwordEditText.getText().toString())) {
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "Welcome" + emailEditText.getText().toString(), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LoginActivity.this, StudentMainActivity.class));
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

    public boolean login(String email, String password){
        if(email.isEmpty()){
            Toast.makeText(getApplicationContext(), "You have to enter a valid email", Toast.LENGTH_LONG).show();
            return false;
        } else if(password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "You have to enter a valid password", Toast.LENGTH_LONG).show();
            return false;
        } else {
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
            databaseAccess.open();
            if(!databaseAccess.findUser(email, password)) {
                Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_LONG).show();
                return false;
            } else {
                return true;
            }
        }
    }
}