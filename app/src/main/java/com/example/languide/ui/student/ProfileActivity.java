package com.example.languide.ui.student;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.languide.R;
import com.example.languide.ui.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity  extends AppCompatActivity {

    TextView textName;
    TextView  textEmail;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String email = intent.getStringExtra("profile_email");

        Button buttonTest = findViewById(R.id.buttonTest);
        Button buttonLogout = findViewById(R.id.idLogout);
        textEmail = findViewById(R.id.idEmailProfile);

        textEmail.setText(email);

        buttonTest.setOnClickListener(v -> openStudentMain());
        buttonLogout.setOnClickListener(v -> logout());
    }

    public void openStudentMain(){
        Intent intent = new Intent(this,StudentMainActivity.class);
        startActivity(intent);
    }

    public void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("EXIT");
        builder.setMessage("Are you sure you want to log out?");
        builder.setPositiveButton("YES", (dialog, which) -> {
            mAuth.signOut();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
        });
        builder.setNegativeButton("NO", (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
