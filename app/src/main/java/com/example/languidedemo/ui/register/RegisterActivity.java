package com.example.languidedemo.ui.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.languidedemo.*;
import com.example.languidedemo.database.DatabaseAccess;
import com.example.languidedemo.ui.student.StudentMainActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText name;
    EditText email;
    EditText password;
    EditText confirmPassword;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.idName);
        email = findViewById(R.id.idEmail);
        password = findViewById(R.id.idPass);
        confirmPassword = findViewById(R.id.idConfPass);
        register = findViewById(R.id.register);



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDataEntered();
            }
        });

    }

    //Check if the data input is correct
    public void checkDataEntered(){
        if (isEmpty(name)) {
            Toast t = Toast.makeText(this, "You must enter first name to register!", Toast.LENGTH_SHORT);
            t.show();
            return;
        }
        if (!isEmail(email)) {
            email.setError("Enter a valid email!");
            return;
        }
        if (!isPasswordValid(password.getText().toString())) {
            password.setError("Password must have at least 6 characters");
            return;
        }
        if(!password.getText().toString().equals(confirmPassword.getText().toString())) {
            confirmPassword.setError("Passwords must be the same!");
            return;
        }
        DatabaseAccess databaseAccess = DatabaseAccess.getInstance(this);
        databaseAccess.open();
        if(databaseAccess.findUser(email.getText().toString(), password.getText().toString())) {
            Toast.makeText(this, "User already exists!", Toast.LENGTH_SHORT).show();
        } else {
            if(!databaseAccess.insert(name.getText().toString(), email.getText().toString(), password.getText().toString())) {
                Toast.makeText(this, "An error ocurred :(", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Yaay! You got registered :)", Toast.LENGTH_LONG).show();
                startActivity(new Intent(RegisterActivity.this, StudentMainActivity.class));
            }
        }
    }

    //Checks if an EditText is empty
    private boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
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