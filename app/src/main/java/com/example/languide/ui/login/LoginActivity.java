package com.example.languide.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.languide.*;
import com.example.languide.database.DatabaseAccess;
import com.example.languide.ui.register.RegisterActivity;
import com.example.languide.ui.student.StudentMainActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText emailEditText = findViewById(R.id.email);
        final EditText passwordEditText = findViewById(R.id.password);
        final TextView registerTextView = findViewById(R.id.idRegister);
        final Button loginButton = findViewById(R.id.idLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!login(emailEditText.getText().toString(),
                        passwordEditText.getText().toString())) {
                } else {
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