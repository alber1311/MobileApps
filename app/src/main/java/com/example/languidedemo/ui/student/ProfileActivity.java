package com.example.languidedemo.ui.student;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.example.languidedemo.R;
import com.example.languidedemo.ui.login.LoginActivity;

public class ProfileActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button buttonTest = findViewById(R.id.buttonTest);
        Button buttonLogout = findViewById(R.id.idLogout);

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
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
